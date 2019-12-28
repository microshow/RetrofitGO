package io.microshow.retrofitgo.sample.viewmode;

import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.support.annotation.NonNull;

import io.microshow.retrofitgo.sample.base.BaseFactory;
import io.microshow.retrofitgo.sample.reponsitory.OneReponsitory;
import io.microshow.retrofitgo.sample.webservice.OneWebService;

/**
 * Created by Super on 2019/12/28.
 */
public class OneFactory extends BaseFactory {

    private static volatile OneFactory INSTANCE;

    private final Context context;
    private final OneReponsitory mReponsitory;

    public static OneFactory get(Context context) {
        if (INSTANCE == null) {
            synchronized (OneFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new OneFactory(context.getApplicationContext(),
                            new OneReponsitory(
                                    createRetrofitClient(getApiDomain(), OneWebService.class)));
                }
            }
        }
        return INSTANCE;
    }

    private OneFactory(Context context, OneReponsitory repository) {
        this.context = context;
        this.mReponsitory = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(OneViewMode.class)) {
            return (T) new OneViewMode(context.getApplicationContext(), mReponsitory);
        }
        return super.create(modelClass);
    }


}
