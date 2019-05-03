package io.microshow.fastokhttp.sample.network;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import io.microshow.rxretrofit.RetrofitClient;
import io.microshow.rxretrofit.arch.Resource;
import io.microshow.rxretrofit.internal.BaseResponse;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.schedulers.Schedulers;

public abstract class SimpleNetBoundResource2<T> {

    private Flowable<Resource<T>> flowable;

    public SimpleNetBoundResource2() {
        flowable = Flowable.create(emitter -> {
            // 加载中
            emitter.onNext(Resource.loading(null));
            // 从网络加载数据
            getServerTime().subscribe(
                    response -> { // 成功
                        fetch(emitter, response);
                    },
                    e -> {  // 失败
//                        RxHelper.handleError(e).subscribe(error -> {
////              ResponseErrorHander.handleError(AppUtils.getContext(), error.getCode(), error.getMessage(), false);
//                            emitter.onNext(Resource.<T>error(error.getCode(), error.getMessage(), null));
//                        });
                        emitter.onNext(Resource.<T>error(e.getMessage(), null));
                    });
        }, BackpressureStrategy.BUFFER);
    }

    protected Flowable<BaseResponse<ServerTimeEntity>> getServerTime() {
        ServerTimeService serverTimeService = RetrofitClient.getInstance().create(ServerTimeService.class, GlobalManager.getInstance().getApiDomain());
        return serverTimeService.getServerTime();
    }

    @SuppressLint("CheckResult")
    private void fetch(final FlowableEmitter<Resource<T>> emitter, BaseResponse<ServerTimeEntity> serverTimeData) {
        //服务器返回的时间正常
        if (serverTimeData != null && serverTimeData.getData() != null && serverTimeData.getData().milliseconds > 0) {
            //update 时间，请求参数拦截器里会去拿最新的时间值
            GlobalManager.getInstance().setServerTime(serverTimeData.getData().milliseconds);

            fetchFromNet().subscribe(response -> {
                emitter.onNext(Resource.success(response)); // 加载数据成功
            }, e -> {  // 失败
//                        RxHelper.handleError(e).subscribe(error -> {
////              ResponseErrorHander.handleError(AppUtils.getContext(), error.getCode(), error.getMessage(), false);
//                            emitter.onNext(Resource.<T>error(error.getCode(), error.getMessage(), null));
//                        });
                emitter.onNext(Resource.<T>error(e.getMessage(), null));
            });
        } else {
            emitter.onNext(Resource.<T>error("获取服务器时间解析异常！", null));
        }

    }

    /**
     * 从网络加载数据
     */
    @NonNull
    protected abstract Flowable<T> fetchFromNet();

    protected void saveResult(T item) {

    }

    public Flowable<Resource<T>> getFlowable() {
        return flowable.subscribeOn(Schedulers.io());
    }

    public Flowable<Resource<T>> getFlowableNormal() {
        return flowable;
    }

}
