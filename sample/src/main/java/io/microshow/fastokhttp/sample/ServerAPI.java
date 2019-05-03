package io.microshow.fastokhttp.sample;

import io.microshow.rxretrofit.internal.BaseResponse;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by Super on 2018/12/26.
 */
public interface ServerAPI {

    @GET("v1/rank-index")
    Flowable<BaseResponse<MovieModel>> getRankIndex();

}
