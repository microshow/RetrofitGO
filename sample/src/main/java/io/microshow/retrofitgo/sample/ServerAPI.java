package io.microshow.retrofitgo.sample;

import io.microshow.retrofitgo.internal.Response;
import io.reactivex.Flowable;
import retrofit2.http.GET;

/**
 * Created by Super on 2018/12/26.
 */
public interface ServerAPI {

    @GET("api/newsflash?per_page=5")
    Flowable<Response<MovieModel>> getMovie();

}
