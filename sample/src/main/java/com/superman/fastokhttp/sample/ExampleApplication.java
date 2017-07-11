package com.superman.fastokhttp.sample;

import android.app.Application;

import com.superman.fastokhttp.FastOkHttpManager;

//import com.squareup.leakcanary.LeakCanary;

public class ExampleApplication extends Application {
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
