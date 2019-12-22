package io.microshow.retrofitgo.sample.appbar;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import io.microshow.retrofitgo.sample.R;

/**
 * Created by Super on 2018/12/23.
 */
public class RVAdapter extends BaseRVAdapter<RVAdapter.RVItem, BaseViewHolder> {

    public static void init (RecyclerView recyclerView) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        List<RVItem> datas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            datas.add(new RVItem("data="+i));
        }
        datas.add(new RVItem("last"));

        RVAdapter rvAdapter = new RVAdapter (datas);

        recyclerView.setAdapter(rvAdapter);
    }

    public RVAdapter(@Nullable List<RVItem> data) {
        super(R.layout.activity_rv_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RVItem item) {

        helper.setText(R.id.text, item.getTitle());

    }

    public static class RVItem {
        String title;

        public RVItem (String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


}
