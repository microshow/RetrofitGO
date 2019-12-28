package io.microshow.retrofitgo.sample.viewmode;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import io.microshow.retrofitgo.sample.MovieModel;
import io.microshow.retrofitgo.sample.base.BaseViewModel;
import io.microshow.retrofitgo.sample.reponsitory.OneReponsitory;
import io.microshow.retrofitgo.arch.Resource;

/**
 * Created by Super on 2018/12/16.
 */
public class OneViewMode extends BaseViewModel {

    public MutableLiveData<Resource<MovieModel>> data2 = new MutableLiveData();

    private OneReponsitory repository;

    public OneViewMode(Context context, OneReponsitory repository) {
        super(context);
        this.repository = repository;
    }

    public void loadMovieData2() {
        Map<String, String> params = new HashMap<>();
        params.put("per_page", "20");
        addDisposable(repository.loadOneData(params).subscribe(resource -> {
            data2.postValue(resource);
        }));
    }

}
