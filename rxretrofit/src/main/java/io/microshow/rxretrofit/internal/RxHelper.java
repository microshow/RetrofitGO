package io.microshow.rxretrofit.internal;

import org.reactivestreams.Publisher;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Function;

/**
 *
 */
public final class RxHelper {

    public static <T> FlowableTransformer<BaseResponse<T>, T> handleResponse() {
        return RxHelper::handleResponse;
    }

    public static <T> Publisher<T> handleResponse(Flowable<BaseResponse<T>> upstream) {
        return upstream.flatMap((Function<BaseResponse<T>, Publisher<T>>) response -> {
            if (response.getCode() != ResponseState.SUCCESS) {
                return Flowable.error(new ApiException(response.getCode(), response.getMessage()));
            }

            if (response.getData() == null) {
                return Flowable.error(new ApiException(response.getCode(), response.getMessage()));
            }

            return Flowable.just(response.getData());
        });
    }



}
