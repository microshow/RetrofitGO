package io.microshow.retrofitgo.sample.viewmode;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

import io.microshow.retrofitgo.internal.SimpleNetBoundResource;
import io.microshow.retrofitgo.sample.MovieModel;
import io.microshow.retrofitgo.sample.ServerAPI;
import io.microshow.retrofitgo.RetrofitClient;
import io.microshow.retrofitgo.internal.RxHelper;
import io.microshow.retrofitgo.arch.Resource;
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

    public void loadMovieData() {
        ServerAPI serverAPI = RetrofitClient.getInstance().create(ServerAPI.class, "https://36kr.com/");

        addDisposable(new SimpleNetBoundResource<MovieModel>() {
            @NonNull
            @Override
            protected Flowable fetchFromNet() {
                Map<String, String> params = new HashMap<>();
                params.put("per_page","3");
                return serverAPI.getMovie(params).compose(RxHelper::handleResponse);
            }
        }.getFlowable().subscribe(resource -> {
            data2.postValue(resource);
        }));
    }

}
