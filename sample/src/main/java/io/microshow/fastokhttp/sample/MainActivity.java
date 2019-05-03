package io.microshow.fastokhttp.sample;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.util.Objects;

import io.microshow.fastokhttp.sample.appbar.AppBarLayoutActivity;
import io.microshow.fastokhttp.sample.databinding.ActivityMainBinding;
import io.microshow.fastokhttp.sample.viewmode.OneViewMode;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setOneViewMode(ViewModelProviders.of(this).get(OneViewMode.class));

        mBinding.getOneViewMode().data2.observe(this,resource->{
            switch (Objects.requireNonNull(resource).status){
                case ERROR:
                    mBinding.result.setText(resource.message);
                    break;
                case LOADING:
                    mBinding.result.setText("loading");
                    break;
                case SUCCESS:
                    mBinding.result.setText(resource.data.cateGoryList.get(0).dict_value);
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

        } else if (view.getId() == R.id.test2){
            Intent intent = new Intent(this, AppBarLayoutActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.test3){
            mBinding.getOneViewMode().loadData2();
        }
    }


}
