package io.microshow.fastokhttp.sample;

import android.app.Application;

import io.microshow.fastokhttp.FastOkHttpManager;

//import com.squareup.leakcanary.LeakCanary;

public class App extends Application {
    @Override public void onCreate() {
        super.onCreate();
//        if (LeakCanary.isInAnalyzerProcess(this)) {
//            // This process is dedicated to LeakCanary for heap analysis.
//            // You should not init your app in this process.
//            return;
//        }
//        LeakCanary.install(this);
        //初始化fastokhttp
        FastOkHttpManager.init(getApplicationContext(), null, BuildConfig.DEBUG);
    }
}
