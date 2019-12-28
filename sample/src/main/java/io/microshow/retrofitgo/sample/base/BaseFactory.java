package io.microshow.retrofitgo.sample.base;

import android.arch.lifecycle.ViewModelProvider;

import io.microshow.retrofitgo.RetrofitClient;

/**
 * Created by Super on 2019/12/28.
 */
public class BaseFactory extends ViewModelProvider.NewInstanceFactory {

    public static String getApiDomain() {
        return "https://36kr.com/";
    }

    /**
     * 创建一个 RetrofitClient
     *
     * @param baseUrl
     * @param service
     * @param <T>
     * @return
     */
    protected static <T> T createRetrofitClient(String baseUrl, Class<T> service) {
        return RetrofitClient.getInstance().create(service, baseUrl);
    }

}
