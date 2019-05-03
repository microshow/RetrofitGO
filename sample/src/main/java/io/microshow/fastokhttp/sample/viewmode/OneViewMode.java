package io.microshow.fastokhttp.sample.viewmode;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import io.microshow.fastokhttp.sample.MovieModel;
import io.microshow.fastokhttp.sample.ServerAPI;
import io.microshow.fastokhttp.sample.network.GlobalManager;
import io.microshow.rxretrofit.RetrofitClient;
import io.microshow.rxretrofit.internal.RxHelper;
import io.microshow.fastokhttp.sample.network.SimpleNetBoundResource2;
import io.microshow.rxretrofit.arch.Resource;
import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Super on 2018/12/16.
 */
public class OneViewMode extends ViewModel {

    protected final CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public MutableLiveData<Resource<MovieModel>> data2 = new MutableLiveData();

    public void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    public void loadData2 () {
        ServerAPI serverAPI = RetrofitClient.getInstance().create(ServerAPI.class, GlobalManager.getInstance().getApiDomain());

        addDisposable(new SimpleNetBoundResource2<MovieModel>() {
            @NonNull
            @Override
            protected Flowable fetchFromNet() {
                return serverAPI.getRankIndex().compose(RxHelper::handleResponse);
            }
        }.getFlowable().subscribe(resource -> {
            data2.postValue(resource);
        }));
    }



}
