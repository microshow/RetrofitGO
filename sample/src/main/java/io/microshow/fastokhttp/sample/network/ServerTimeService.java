package io.microshow.fastokhttp.sample.network;

import io.microshow.rxretrofit.internal.BaseResponse;
import io.reactivex.Flowable;
import retrofit2.http.GET;

/**
 *
 */
public interface ServerTimeService {

    @GET("v1/server-time")
    Flowable<BaseResponse<ServerTimeEntity>> getServerTime();

}
