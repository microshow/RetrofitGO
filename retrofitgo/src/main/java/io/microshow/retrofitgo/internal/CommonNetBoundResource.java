package io.microshow.retrofitgo.internal;

import android.support.annotation.NonNull;

import io.microshow.retrofitgo.arch.Resource;
import io.microshow.retrofitgo.cache.CachePolicyMode;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public abstract class CommonNetBoundResource<T> {

    private Flowable<Resource<T>> flowable;

    private CachePolicyMode mCachePolicyMode;

    public CommonNetBoundResource(CachePolicyMode cachePolicyMode) {

        mCachePolicyMode = cachePolicyMode;

        flowable = Flowable.create(emitter -> {
            // 加载中
            emitter.onNext(Resource.loading(null));

            //缓存的key,通过这个key查询缓存数据
            String cacheKey = getCacheKey();

            if (mCachePolicyMode == CachePolicyMode.POLICY_ONLY_NETWORK) {
                //只读网络


            } else if (mCachePolicyMode == CachePolicyMode.POLICY_ONLY_CACHE) {
                //只读缓存


            } else if (mCachePolicyMode == CachePolicyMode.POLICY_NETWORK_ELSE_CACHE) {
                //先查询网络数据，如果没有，再查询本地缓存


            } else if (mCachePolicyMode == CachePolicyMode.POLICY_CACHE_AND_NETWORK) {
                //先查询本地缓存,不管有没缓存,紧接着都会查询网络数据，此策略会回调两次响应


            }

            // 从网络加载数据
            fetchFromNet().subscribe(
                    response -> { // 成功
                        saveResult(response);
                        emitter.onNext(Resource.success(response));
                    },
                    e -> {  // 失败
                        RxHelper.handleError(e).subscribe(error -> {
                            emitter.onNext(Resource.<T>error(error.getCode(), error.getMessage(), null));
                        });
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

    //获取缓存的key
    protected String getCacheKey() {
        return null;
    }

    public Flowable<Resource<T>> getFlowable() {
        return flowable.subscribeOn(Schedulers.io());
    }

    public Flowable<Resource<T>> getFlowableNormal() {
        return flowable;
    }

}
