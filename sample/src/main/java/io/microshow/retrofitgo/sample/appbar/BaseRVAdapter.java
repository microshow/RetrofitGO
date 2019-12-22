package io.microshow.retrofitgo.sample.appbar;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Collection;
import java.util.List;

/**
 * Created by Super on 2019/5/15.
 */
public abstract class BaseRVAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    public BaseRVAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public BaseRVAdapter() {
        super();
    }

    public BaseRVAdapter(@Nullable List<T> data) {
        super(data);
    }

    public BaseRVAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    public void setNewData(@Nullable List<T> data) {
        super.setNewData(data);
    }

    @Override
    public void addData(@NonNull Collection<? extends T> newData) {
        super.addData(newData);
    }

}
