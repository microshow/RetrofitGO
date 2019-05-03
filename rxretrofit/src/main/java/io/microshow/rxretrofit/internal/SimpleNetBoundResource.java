package io.microshow.rxretrofit.internal;

import android.support.annotation.NonNull;

import io.microshow.rxretrofit.arch.Resource;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public abstract class SimpleNetBoundResource<T> {

    private Flowable<Resource<T>> flowable;

    public SimpleNetBoundResource() {
        flowable = Flowable.create(emitter -> {
            // 加载中
            emitter.onNext(Resource.loading(null));
            // 从网络加载数据
            fetchFromNet().subscribe(
                    response -> { // 成功
                        saveResult(response);
                        emitter.onNext(Resource.success(response));
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
