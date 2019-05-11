package io.microshow.fastokhttp.sample;

import io.microshow.rxretrofit.internal.Response;
import io.reactivex.Flowable;
import retrofit2.http.GET;

/**
 * Created by Super on 2018/12/26.
 */
public interface ServerAPI {

    @GET("v1/rank-index")
    Flowable<Response<MovieModel>> getRankIndex();

}
