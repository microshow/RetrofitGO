package io.microshow.fastokhttp.sample.network;

import android.app.Application;
import android.util.Log;

import io.microshow.rxretrofit.RetrofitClient;
import io.microshow.rxretrofit.RetrofitConfig;
import io.microshow.rxretrofit.converter.CommonGsonConverterFactory;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 *
 */
public class RetrofitClientApp {

    /**
     * 初始化默认的RetrofitClient
     * @param application
     * @param debug
     */
    public static void initDefaultRetrofitClient(Application application, boolean debug) {
        RetrofitConfig.Builder builder = new RetrofitConfig.Builder()
                .addConverterFactory(CommonGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addInterceptor(new CommonRequestHeadersInterceptor())
                .addInterceptor(new CommonRequestParamsInterceptor());

        if (debug) {
            // 添加日志拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d("RetrofitClient", "OkHttp====Message:" + message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //日志拦截器需要放到其它拦截器后面，不然有些信息打印不出，比如请求头信息
            //OkHttp进行添加拦截器loggingInterceptor
            builder.addInterceptor(loggingInterceptor);
        }

        RetrofitClient.init(application, builder.build());
    }

}
