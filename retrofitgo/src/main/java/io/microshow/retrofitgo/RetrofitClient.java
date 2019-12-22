package io.microshow.retrofitgo;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Retrofit管理类
 *
 * 注意：在使用之前，需要调用init方法初始化
 */
public final class RetrofitClient {

  private static RetrofitClient instance;
  private final Application sApplication;
  private final Map<String, Retrofit> retrofitCache = new ConcurrentHashMap<>();
  private AuthCallback authCallback;

  public final RetrofitConfig DEFAULT_CONFIG;

  private RetrofitClient(Application application, RetrofitConfig config) {
    this.sApplication = application;
    this.DEFAULT_CONFIG = config;
  }

  /**
   * 初始化Retrofit客户端管理器
   *
   * @param application application
   * @param defaultConfig retrofit默认配置
   */
  public static void init(Application application, @NonNull RetrofitConfig defaultConfig) {
    if (instance == null) {
      synchronized (RetrofitClient.class) {
        if (instance == null) {
          instance = new RetrofitClient(application, defaultConfig);
        }
      }
    }
  }

  public static RetrofitClient getInstance() {
    return instance;
  }

  public static RetrofitClient get() {
    if (instance == null) {
      throw new NullPointerException("You must init RetrofitClient first!");
    }
    return instance;
  }

  public RetrofitConfig.Builder newConfigBuilder() {
    return new RetrofitConfig.Builder(DEFAULT_CONFIG);
  }

  /**
   * 获取全局上下文
   *
   * @return Context
   */
  public Context getContext() {
    return sApplication.getApplicationContext();
  }

  /**
   * 创建Retrofit实例
   *
   * @param config Retrofit配置
   * @return Retrofit
   */
  private Retrofit newRetrofit(final String baseUrl, final RetrofitConfig config) {
    Retrofit.Builder builder = new Retrofit.Builder();
    builder.client(newOkHttpClient(config));
    builder.baseUrl(baseUrl);
    for (Converter.Factory factory : config.converterFactories()) {
      builder.addConverterFactory(factory);
    }
    for (CallAdapter.Factory factory : config.callAdapterFactories()) {
      builder.addCallAdapterFactory(factory);
    }
    return builder.build();
  }

  /**
   * 创建OkHttpClient实例
   *
   * @param config Retrofit配置
   * @return OkHttpClient
   */
  private OkHttpClient newOkHttpClient(final RetrofitConfig config) {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();

//    // 关闭OkHttp重试机制
//    // 不应该使用OkHttp重试机制，这里存在一个可能造成死循环的bug
//    builder.retryOnConnectionFailure(false);
    builder.retryOnConnectionFailure(true);

    // 只使用Http 1.1协议
//    builder.protocols(Collections.singletonList(Protocol.HTTP_1_1));

    // 设置超时时间
    builder.connectTimeout(config.connectTimeout(), TimeUnit.SECONDS);
    builder.writeTimeout(config.writeTimeout(), TimeUnit.SECONDS);
    builder.readTimeout(config.readTimeout(), TimeUnit.SECONDS);

    // 缓存
    if (config.shouldCache() && config.cache() != null) {
      builder.cache(config.cache());
    }

    // CookieJar
    if (config.cookieJar() != null) {
      builder.cookieJar(config.cookieJar());
    }

    // 添加ssl证书
    if (config.sslSocketFactory() != null) {
      builder.sslSocketFactory(config.sslSocketFactory(), config.x509TrustManager());
    }
    // 域名验证
    if (config.hostnameVerifier() != null) {
      builder.hostnameVerifier(config.hostnameVerifier());
    }

    // 添加拦截器
    for (Interceptor interceptor : config.interceptors()) {
      builder.addInterceptor(interceptor);
    }
    for (Interceptor interceptor : config.networkInterceptors()) {
      builder.addNetworkInterceptor(interceptor);
    }

    return builder.build();
  }

  private Retrofit findRetrofit(final String baseUrl) {
    Retrofit result = retrofitCache.get(baseUrl);
    if (result != null) {
      return result;
    }
    synchronized (retrofitCache) {
      result = retrofitCache.get(baseUrl);
      if (result == null) {
        result = newRetrofit(baseUrl, DEFAULT_CONFIG);
        retrofitCache.put(baseUrl, result);
      }
    }
    return result;
  }

  /**
   * 使用默认RetrofitConfig创建一个Service实例
   *
   * 1. 通过clazz的Host注解反射得到baseUrl。
   * 2. 通过baseUrl去缓存查找Retrofit实例，如果已存在，则直接返回此实例；否则将会使用默认配置创建新的实例。
   *
   * @param clazz Service类
   * @return T
   */
  public <T> T create(Class<T> clazz) {
    return findRetrofit(parseHostAnnotation(clazz)).create(clazz);
  }

  /**
   * 使用默认RetrofitConfig创建一个Service实例
   *
   * 使用传入的baseUrl去缓存查找Retrofit实例，如果已存在，则直接返回此实例；否则将会使用默认配置创建新的实例。
   *
   * @param clazz Service类
   * @param baseUrl baseUrl
   * @return T
   */
  public <T> T create(Class<T> clazz, final String baseUrl) {
    return findRetrofit(baseUrl).create(clazz);
  }

  /**
   * 使用新的RetrofitConfig创建一个Service实例。
   *
   * 1. baseUrl使用clazz设置的Host注解。
   * 2. 使用baseUrl和RetrofitConfig创建一个Retrofit实例，并且不会缓存此实例。
   *
   * 注意：此方法适用于某些需要在默认配置基础上修改某些配置项的场景。比如：
   * 上传大文件时，读写超时时间应该比普通请求设置得更长一些。
   *
   * @param clazz Service类
   * @param config Retrofit配置
   * @return T
   */
  public <T> T createFromNewConfig(Class<T> clazz, final RetrofitConfig config) {
    return createFromNewConfig(clazz, parseHostAnnotation(clazz), config);
  }

  /**
   * 使用新的RetrofitConfig创建一个Service实例。
   *
   * 1. 使用传入的baseUrl和RetrofitConfig创建一个Retrofit实例，并且不会缓存此实例。
   *
   * 注意：此方法适用于某些需要在默认配置基础上修改某些配置项的场景。比如：
   * 上传大文件时，读写超时时间应该比普通请求设置得更长一些。
   *
   * @param clazz Service类
   * @param config Retrofit配置
   * @return T
   */
  public <T> T createFromNewConfig(Class<T> clazz, String baseUrl, RetrofitConfig config) {
    return newRetrofit(baseUrl, config).create(clazz);
  }

  private <T> String parseHostAnnotation(Class<T> clazz) {
    Host host = clazz.getAnnotation(Host.class);
    if (host == null) {
      throw new IllegalArgumentException("service must have Host Annotation");
    }
    return host.baseUrl();
  }

  public AuthCallback getAuthCallback() {
    return authCallback;
  }

  public void setAuthCallback(AuthCallback authCallback) {
    this.authCallback = authCallback;
  }

  public interface AuthCallback {

    void onTokenExpired(String msg);
  }

}
