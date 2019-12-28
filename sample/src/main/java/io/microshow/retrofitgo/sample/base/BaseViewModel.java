package io.microshow.retrofitgo.sample.base;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModel;
import android.content.Context;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by Super on 2019/12/28.
 */
public abstract class BaseViewModel extends ViewModel {

    @SuppressLint("StaticFieldLeak")
    protected final Context mContext;

    protected final CompositeDisposable mCompositeDisposable;

    public BaseViewModel(Context context) {
        this.mContext = context.getApplicationContext();
        mCompositeDisposable = new CompositeDisposable();
    }

    public void addDisposable(Disposable disposable) {
        mCompositeDisposable.add(disposable);
    }

    public void dispose() {
        mCompositeDisposable.dispose();
        super.onCleared();
    }

    public void clear() {
        mCompositeDisposable.clear();
        super.onCleared();
    }

}
