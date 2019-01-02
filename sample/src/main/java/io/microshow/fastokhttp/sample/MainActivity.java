package io.microshow.fastokhttp.sample;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.zchu.rxcache.RxCache;
import com.zchu.rxcache.data.CacheResult;
import com.zchu.rxcache.data.ResultFrom;
import com.zchu.rxcache.diskconverter.GsonDiskConverter;
import com.zchu.rxcache.stategy.CacheStrategy;
import com.zchu.rxcache.stategy.IStrategy;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;

import io.microshow.fastokhttp.sample.appbar.AppBarLayoutActivity;
import io.microshow.fastokhttp.sample.databinding.ActivityMainBinding;
import io.microshow.fastokhttp.sample.viewmode.OneViewMode;
import io.reactivex.FlowableSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityMainBinding mBinding;

    ServerAPI serverAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mBinding.setOneViewMode(ViewModelProviders.of(this).get(OneViewMode.class));
        mBinding.getOneViewMode().data.observeForever(new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mBinding.result.setText(s);
            }
        });

        mBinding.test.setOnClickListener(this);
        mBinding.test2.setOnClickListener(this);
        mBinding.test3.setOnClickListener(this);

        initRxCache ();
    }

    private void initRxCache () {
        serverAPI = new Retrofit.Builder()
                .baseUrl(ServerAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(new OkHttpClient.Builder().build())
                .build()
                .create(ServerAPI.class);

        RxCache.initializeDefault(new RxCache.Builder()
                .appVersion(2)
                .diskDir(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/1"))
                .diskConverter(new GsonDiskConverter())
                .diskMax(20 * 1024 * 1024)
                .memoryMax(0)
                .setDebug(true)
                .build());

    }

    private void loadData () {
        serverAPI.test("12133232esdsfdsfsf")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableSubscriber<BaseModel<MovieModel>>() {
                    @Override
                    public void onNext(BaseModel<MovieModel> movieModelBaseModel) {
                        mBinding.result.setText(movieModelBaseModel.result.authCode);
                    }

                    @Override
                    public void onError(Throwable t) {
                        mBinding.result.setText(t.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
//                .subscribe(new Subscriber<BaseModel<MovieModel>>() {
//                    @Override
//                    public void onSubscribe(Subscription s) {
//                        s.request(Long.MAX_VALUE);
//                    }
//
//                    @Override
//                    public void onNext(BaseModel<MovieModel> movieModelBaseModel) {
//                        mBinding.result.setText(movieModelBaseModel.result.authCode);
//                    }
//
//                    @Override
//                    public void onError(Throwable t) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

    private void loadData(IStrategy strategy) {
//        if (mSubscription != null && !mSubscription!!.isDisposed) {
//            mSubscription!!.dispose()
//        }

        serverAPI.getInTheatersMovies()
                .compose(RxCache.getDefault().<MovieModel>transformObservable("key",null, strategy))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new io.reactivex.Observer<CacheResult<MovieModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(CacheResult<MovieModel> movieModelCacheResult) {
                        if (ResultFrom.ifFromCache(movieModelCacheResult.getFrom())) {
                            mBinding.result.setText("rxcache ifFromCache: "+movieModelCacheResult.getData().title);
                            Log.e("rxcache", "ifFromCache: "+movieModelCacheResult.getData().title);
                        } else {
                            mBinding.result.setText("rxcache"+movieModelCacheResult.getData().title);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        mBinding.result.setText("rxcache onError"+e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });



//        tvData!!.text = "加载中..."
//        val startTime = System.currentTimeMillis()
//        serverAPI!!.inTheatersMovies
//                .map { it.subjects!! }
//        //泛型这样使用
//                .rxCache("custom_key", strategy)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(object : Observer<CacheResult<List<MovieModel.SubjectsBean>>> {
//            override fun onSubscribe(disposable: Disposable) {
//                mSubscription = disposable
//            }
//
//            override fun onNext(listCacheResult: CacheResult<List<MovieModel.SubjectsBean>>) {
//                Logger.e(listCacheResult)
//                if (ResultFrom.ifFromCache(listCacheResult.from)) {
//                    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//                            .format(Date(listCacheResult.timestamp))
//                    tvData!!.text = "来自缓存  写入时间：" + format + "\n " + listCacheResult.data
//                } else {
//                    tvData!!.text = "来自网络：\n " + listCacheResult.data + "\n 响应时间：" + (System.currentTimeMillis() - startTime) + "毫秒"
//                }
//            }
//
//            override fun onError(throwable: Throwable) {
//                tvData!!.text = throwable.message
//            }
//
//            override fun onComplete() {
//
//            }
//        })
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.test) {
            mBinding.getOneViewMode().loadData();
        } else if (view.getId() == R.id.test2){
            Intent intent = new Intent(this, AppBarLayoutActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.test3){
            loadData(CacheStrategy.firstRemote());
//            loadData();
        }
    }

}
