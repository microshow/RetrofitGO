package io.microshow.retrofitgo.sample.webservice;

import java.util.Map;

import io.microshow.retrofitgo.internal.Response;
import io.microshow.retrofitgo.sample.MovieModel;
import io.reactivex.Flowable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

/**
 * Created by Super on 2018/12/26.
 */
public interface OneWebService {

    @GET("api/newsflash")
    Flowable<Response<MovieModel>> getMovie(@QueryMap Map<String, String> params);

}
