package io.microshow.retrofitgo.internal;

import com.google.gson.JsonParseException;
import com.google.gson.stream.MalformedJsonException;

import org.json.JSONException;
import org.reactivestreams.Publisher;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLException;

import io.microshow.retrofitgo.RetrofitClient;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.functions.Function;
import retrofit2.HttpException;

/**
 * RxHelper
 */
public class RxHelper {

    public static <T> FlowableTransformer<Response<T>, T> handleResponse() {
        return RxHelper::handleResponse;
    }

    public static <T> Publisher<T> handleResponse(Flowable<Response<T>> upstream) {
        return upstream.flatMap((Function<Response<T>, Publisher<T>>) response -> {
            if (response.getCode() != ErrorHelper.SUCCESS) {
                return Flowable.error(new ApiException(response.getCode(), response.getMessage()));
            }

            if (response.getData() == null) {
                return Flowable.error(new ApiException(response.getCode(), response.getMessage()));
            }

            return Flowable.just(response.getData());
        });
    }

    public static Flowable<Error> handleError(final Throwable e) {
        return Flowable.create(emitter -> {
            e.printStackTrace();
            if (e instanceof ConnectException || e instanceof UnknownHostException) {
                emitter.onNext(ErrorHelper.netError());
            } else if (e instanceof HttpException) {
                emitter.onNext(ErrorHelper.httpError());
                //eos的接口查询失败竟然是500
//                Request request = Objects.requireNonNull(((HttpException) e).response().raw().networkResponse()).request();
//                if(request!=null){
//                    HttpUrl url = request.url();
//                    URI uri = url.uri();
//                    String host = uri.getHost();
//                    if(host.equals(getEosApiHost())){
//                        emitter.onNext(new Error(EOS_ERROR,e.getMessage()));
//                    }else{
//                        emitter.onNext(ErrorHelper.httpError());
//                    }
//                }
            } else if (e instanceof SocketTimeoutException) {
                emitter.onNext(ErrorHelper.timeout());
            } else if (e instanceof SSLException) {
                emitter.onNext(ErrorHelper.sslError());
            } else if (e instanceof MalformedJsonException || e instanceof JSONException ||
                    e instanceof JsonParseException) {
                emitter.onNext(ErrorHelper.parseError());
            } else if (e instanceof ClassCastException) {
                emitter.onNext(ErrorHelper.castError());
            } else if (e instanceof ApiException) {
                // 接口请求失败
                ApiException apiException = (ApiException) e;
                if (apiException.getError() == ErrorHelper.TOKEN_EXPIRED
                        && RetrofitClient.get().getAuthCallback() != null) {
                    RetrofitClient.get().getAuthCallback().onTokenExpired(apiException.getMsg());
                } else {
                    emitter.onNext(ErrorHelper.apiError(apiException.getError(), apiException.getMsg()));
                }
            } else if (e instanceof IOException) {
                emitter.onNext(ErrorHelper.parseError());
            } else {
                // 未知错误
                emitter.onNext(ErrorHelper.parseError());
            }
        }, BackpressureStrategy.BUFFER);
    }


}
