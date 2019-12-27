package io.microshow.retrofitgo.sample;

import java.util.Map;

import io.microshow.retrofitgo.internal.BaseAPI;
import io.microshow.retrofitgo.internal.Response;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

/**
 * Created by Super on 2018/12/26.
 */
public interface ServerAPI /**extends BaseAPI*/ {

    @Headers({"CACHE_MODE:POLICY_ONLY_CACHE"})
    @GET("api/newsflash")
    Flowable<Response<MovieModel>> getMovie(@QueryMap Map<String, String> params);

}
