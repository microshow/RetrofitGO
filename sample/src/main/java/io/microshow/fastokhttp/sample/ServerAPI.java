package io.microshow.fastokhttp.sample;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Super on 2018/12/26.
 */
public interface ServerAPI {

    String BASE_URL = "https://api.douban.com";

    @GET("/v2/movie/in_theaters?city=上海")
    Observable<MovieModel> getInTheatersMovies();

    @GET("auth")
    Flowable<BaseModel<MovieModel>> test(@Query("address") String address);

}
