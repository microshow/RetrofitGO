package io.microshow.retrofitgo.sample;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.Objects;

import io.microshow.retrofitgo.sample.appbar.AppBarLayoutActivity;
import io.microshow.retrofitgo.sample.appbar.AppBarLayoutActivity2;
import io.microshow.retrofitgo.sample.databinding.ActivityMainBinding;
import io.microshow.retrofitgo.sample.viewmode.OneFactory;
import io.microshow.retrofitgo.sample.viewmode.OneViewMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mBinding.setOneViewMode(ViewModelProviders.of(this, OneFactory.get(this)).get(OneViewMode.class));

        mBinding.getOneViewMode().data2.observe(this, resource -> {
            switch (Objects.requireNonNull(resource).status) {
                case ERROR:
                    Log.i("xxx ERROR ", resource.message);
                    mBinding.result.setText(resource.message);
                    break;
                case LOADING:
                    mBinding.result.setText("loading");
                    break;
                case SUCCESS:
                    Log.i("xxx SUCCESS ", resource.data.items.get(0).title);
                    mBinding.result.setText("来自缓存数据：" + resource.isCache + " -> " + resource.data.items.get(0).title);
                    break;
            }
        });

        mBinding.test.setOnClickListener(this);
        mBinding.test2.setOnClickListener(this);
        mBinding.test3.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.test) {

            Intent intent = new Intent(this, AppBarLayoutActivity2.class);
            startActivity(intent);

        } else if (view.getId() == R.id.test2) {
            Intent intent = new Intent(this, AppBarLayoutActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.test3) {
            mBinding.getOneViewMode().loadMovieData2();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding.getOneViewMode().dispose();
    }
}
