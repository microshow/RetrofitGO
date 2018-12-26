package io.microshow.fastokhttp.sample;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by Super on 2018/12/26.
 */
public interface ServerAPI {

    String BASE_URL = "https://api.douban.com";

    @GET("/v2/movie/in_theaters?city=上海")
    Observable<MovieModel> getInTheatersMovies();

}
