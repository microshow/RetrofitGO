package io.microshow.retrofitgo.sample.reponsitory;

import android.support.annotation.NonNull;

import java.util.Map;

import io.microshow.retrofitgo.arch.Resource;
import io.microshow.retrofitgo.cache.CachePolicyMode;
import io.microshow.retrofitgo.internal.CommonNetBoundResource;
import io.microshow.retrofitgo.internal.RxHelper;
import io.microshow.retrofitgo.sample.MovieModel;
import io.microshow.retrofitgo.sample.webservice.OneWebService;
import io.reactivex.Flowable;

/**
 * Created by Super on 2019/12/28.
 */
public class OneReponsitory {

    private OneWebService mOneWebService;

    public OneReponsitory(OneWebService oneWebService) {
        this.mOneWebService = oneWebService;
    }

    public Flowable<Resource<MovieModel>> loadOneData(Map<String, String> params) {
        String mCacheKey = this.getClass().getName()
                + "." + Thread.currentThread().getStackTrace()[2].getMethodName()
                + "." + params.hashCode();
        return new CommonNetBoundResource<MovieModel>(CachePolicyMode.POLICY_CACHE_AND_NETWORK) {
            @NonNull
            @Override
            protected Flowable fetchFromNet() {
                return mOneWebService.getMovie(params).compose(RxHelper::handleResponse);
            }

            @Override
            protected String getCacheKey() {
                return mCacheKey;
            }
        }.getFlowable();
    }

}
