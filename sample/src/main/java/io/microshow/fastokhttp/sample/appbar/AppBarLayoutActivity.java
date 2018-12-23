package io.microshow.fastokhttp.sample.appbar;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import io.microshow.fastokhttp.sample.R;
import io.microshow.fastokhttp.sample.databinding.ActivityAppbarBinding;

/**
 * Created by Super on 2018/12/23.
 */
public class AppBarLayoutActivity extends AppCompatActivity {

    private ActivityAppbarBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_appbar);

        ImmersionBar.with(AppBarLayoutActivity.this).titleBar(binding.toolbar)
                .statusBarDarkFont(false).transparentNavigationBar()
                .init();

        for(int i = 0; i < 2; i++) {
            binding.tablayout.addTab(binding.tablayout.newTab());
        }

        binding.appbarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int verticalOffset) {
                if( state == State.EXPANDED ) {
                    //展开状态
                    binding.topTitle.setVisibility(View.INVISIBLE);
                    ImmersionBar.with(AppBarLayoutActivity.this).titleBar(binding.toolbar)
                            .statusBarDarkFont(false)
                            .init();

                }else if(state == State.COLLAPSED || state == State.MIDDLE){
                    //折叠状态 或者中间状态
                    binding.topTitle.setVisibility(View.VISIBLE);
                    ImmersionBar.with(AppBarLayoutActivity.this).titleBar(binding.toolbar)
                            .statusBarDarkFont(true)
                            .init();
                }else {
                    //其它状态
                }
            }
        });

        binding.viewpager.setAdapter(new IFragmentPagerAdapter(getSupportFragmentManager()));

        // tablayout 绑定 viewpager
        binding.tablayout.setupWithViewPager(binding.viewpager);

        binding.tablayout.setTabsFromPagerAdapter(binding.viewpager.getAdapter());//给Tabs设置适配器
    }

    public class IFragmentPagerAdapter extends FragmentPagerAdapter {

        private FragmentManager fragmetnmanager;  //创建FragmentManager
        private List<Fragment> listfragment = new ArrayList<>(); //创建一个List<Fragment>

        public IFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragmetnmanager=fm;
            this.listfragment.add(TestFragment.newInstance("hello"));
            this.listfragment.add(TestFragment2.newInstance("world"));
        }

        @Override
        public Fragment getItem(int arg0) {
            // TODO Auto-generated method stub
            return listfragment.get(arg0); //返回第几个fragment
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listfragment.size(); //总共有多少个fragment
        }

        //ViewPager与TabLayout绑定后，这里获取到PageTitle就是Tab的Text
        @Override
        public CharSequence getPageTitle(int position) {
            return "发现"+position;
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}
