package io.microshow.rxretrofit;

import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.Interceptor;
import retrofit2.CallAdapter;
import retrofit2.Converter;

/**
 * Retrofit配置
 */
public final class RetrofitConfig {

  private boolean debug;
  private long connectTimeout;
  private long readTimeout;
  private long writeTimeout;
  private boolean shouldCache;
  private Cache cache;
  private CookieJar cookieJar;
  private SSLSocketFactory sslSocketFactory;
  private X509TrustManager x509TrustManager;
  private HostnameVerifier hostnameVerifier;
  private List<Converter.Factory> converterFactories;
  private List<CallAdapter.Factory> callAdapterFactories;
  private List<Interceptor> interceptors;
  private List<Interceptor> networkInterceptors;

  RetrofitConfig(Builder builder) {
    this.debug = builder.debug;
    this.connectTimeout = builder.connectTimeout;
    this.readTimeout = builder.readTimeout;
    this.writeTimeout = builder.writeTimeout;
    this.shouldCache = builder.shouldCache;
    this.cache = builder.cache;
    this.cookieJar = builder.cookieJar;
    this.sslSocketFactory = builder.sslSocketFactory;
    this.x509TrustManager = builder.x509TrustManager;
    this.hostnameVerifier = builder.hostnameVerifier;
    this.converterFactories = builder.converterFactories;
    this.callAdapterFactories = builder.callAdapterFactories;
    this.interceptors = builder.interceptors;
    this.networkInterceptors = builder.networkInterceptors;
  }

  public boolean debug() {
    return this.debug;
  }

  public long connectTimeout() {
    return this.connectTimeout;
  }

  public long readTimeout() {
    return this.readTimeout;
  }

  public long writeTimeout() {
    return this.writeTimeout;
  }

  public boolean shouldCache() {
    return this.shouldCache;
  }

  public Cache cache() {
    return this.cache;
  }

  public CookieJar cookieJar() {
    return this.cookieJar;
  }

  public SSLSocketFactory sslSocketFactory() {
    return this.sslSocketFactory;
  }

  public X509TrustManager x509TrustManager() {
    return this.x509TrustManager;
  }

  public HostnameVerifier hostnameVerifier() {
    return this.hostnameVerifier;
  }

  public List<Converter.Factory> converterFactories() {
    return this.converterFactories;
  }

  public List<CallAdapter.Factory> callAdapterFactories() {
    return this.callAdapterFactories;
  }

  public List<Interceptor> interceptors() {
    return this.interceptors;
  }

  public List<Interceptor> networkInterceptors() {
    return this.networkInterceptors;
  }

  public Builder newBuilder() {
    return new Builder(this);
  }

  public static class Builder {

    private boolean debug;
    private long connectTimeout;
    private long readTimeout;
    private long writeTimeout;
    private boolean shouldCache;
    private Cache cache;
    private CookieJar cookieJar;
    private SSLSocketFactory sslSocketFactory;
    private X509TrustManager x509TrustManager;
    private HostnameVerifier hostnameVerifier;
    private List<Converter.Factory> converterFactories;
    private List<CallAdapter.Factory> callAdapterFactories;
    private List<Interceptor> interceptors;
    private List<Interceptor> networkInterceptors;

    public Builder() {
      this.debug = false;
      this.connectTimeout = 60;
      this.readTimeout = 60;
      this.writeTimeout = 60;
      this.shouldCache = false;
      this.cache = null;
      this.cookieJar = null;
      this.sslSocketFactory = null;
      this.x509TrustManager = null;
      this.hostnameVerifier = null;
      this.converterFactories = new ArrayList<>();
      this.callAdapterFactories = new ArrayList<>();
      this.interceptors = new ArrayList<>();
      this.networkInterceptors = new ArrayList<>();
    }

    Builder(RetrofitConfig config) {
      this.debug = config.debug;
      this.connectTimeout = config.connectTimeout;
      this.readTimeout = config.readTimeout;
      this.writeTimeout = config.writeTimeout;
      this.shouldCache = config.shouldCache;
      this.cache = config.cache;
      this.cookieJar = config.cookieJar;
      this.sslSocketFactory = config.sslSocketFactory;
      this.x509TrustManager = config.x509TrustManager;
      this.hostnameVerifier = config.hostnameVerifier;
      this.converterFactories = config.converterFactories;
      this.callAdapterFactories = config.callAdapterFactories;
      this.interceptors = config.interceptors;
      this.networkInterceptors = config.networkInterceptors;
    }

    public Builder debug(boolean debug) {
      this.debug = debug;
      return this;
    }

    public Builder connectTimeout(long connectTimeout) {
      this.connectTimeout = connectTimeout;
      return this;
    }

    public Builder readTimeout(long readTimeout) {
      this.readTimeout = readTimeout;
      return this;
    }

    public Builder writeTimeout(long writeTimeout) {
      this.writeTimeout = writeTimeout;
      return this;
    }

    public Builder shouldCache(boolean shouldCache) {
      this.shouldCache = shouldCache;
      return this;
    }

    public Builder cache(Cache cache) {
      this.cache = cache;
      return this;
    }

    public Builder cookieJar(CookieJar cookieJar) {
      this.cookieJar = cookieJar;
      return this;
    }

    public Builder sslSocketFactory(SSLSocketFactory sslSocketFactory) {
      this.sslSocketFactory = sslSocketFactory;
      return this;
    }

    public Builder x509TrustManager(X509TrustManager x509TrustManager) {
      this.x509TrustManager = x509TrustManager;
      return this;
    }

    public Builder hostnameVerifier(HostnameVerifier hostnameVerifier) {
      this.hostnameVerifier = hostnameVerifier;
      return this;
    }

    public Builder addConverterFactory(Converter.Factory factory) {
      this.converterFactories.add(factory);
      return this;
    }

    public Builder addCallAdapterFactory(CallAdapter.Factory factory) {
      this.callAdapterFactories.add(factory);
      return this;
    }

    public Builder addInterceptor(Interceptor interceptor) {
      this.interceptors.add(interceptor);
      return this;
    }

    public List<Interceptor> getInterceptors() {
      return interceptors;
    }

    public Builder addNetworkInterceptor(Interceptor interceptor) {
      this.networkInterceptors.add(interceptor);
      return this;
    }

    public RetrofitConfig build() {

      return new RetrofitConfig(this);
    }
  }
}
