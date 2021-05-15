package io.microshow.retrofitgo.internal;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

import io.microshow.retrofitgo.arch.Resource;
import io.microshow.retrofitgo.cache.CachePolicyMode;
import io.microshow.retrofitgo.cache.CacheSpUtils;
import io.microshow.retrofitgo.cache.ClassTypeReflect;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.schedulers.Schedulers;

public abstract class CommonNetBoundResource<T> {

    private Flowable<Resource<T>> flowable;

    private CachePolicyMode mCachePolicyMode;

    @SuppressLint("CheckResult")
    public CommonNetBoundResource(CachePolicyMode cachePolicyMode) {

        mCachePolicyMode = cachePolicyMode;

        flowable = Flowable.create(emitter -> {
            // 加载中
            emitter.onNext(Resource.loading(null));

            //缓存的key,通过这个key查询缓存数据
            String cacheKey = getCacheKey();

            if (mCachePolicyMode == CachePolicyMode.POLICY_CACHE_AND_NETWORK) {
                //先查询本地缓存,不管有没缓存,紧接着都会查询网络数据，此策略会回调两次响应
                T cacheData = getCacheResult(cacheKey);
                if (cacheData != null) {
                    emitter.onNext(Resource.success(cacheData, true));
                    Thread.sleep(100);
                }
            }

            // 从网络加载数据
            fetchFromNet().subscribe(
                    response -> { // 成功
                        saveResult(response);
                        if (mCachePolicyMode != CachePolicyMode.POLICY_ONLY_NETWORK) {
                            //不是只读网络策略，都需要把数据缓存到本地
                            CacheSpUtils.saveCacheData(cacheKey, response);
                        }
                        emitter.onNext(Resource.success(response));
                    },
                    e -> {  // 失败
                        RxHelper.handleError(e).subscribe(error -> {

                            if (mCachePolicyMode == CachePolicyMode.POLICY_NETWORK_ELSE_CACHE) {
                                //网络加载失败在读缓存数据
                                T cacheData = getCacheResult(cacheKey);
                                if (cacheData != null) {
                                    emitter.onNext(Resource.success(cacheData, true));
                                    return;
                                }
                            }

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

    private void saveResult(T item) {

    }

    private <T> T getCacheResult(String cacheKey) {
        return CacheSpUtils.getCacheData(cacheKey, ClassTypeReflect.getModelClazz(getClass()), new TypeToken<T>(){}.getType());
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
