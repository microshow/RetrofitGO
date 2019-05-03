package io.microshow.fastokhttp.sample;

import android.app.Application;

import io.microshow.fastokhttp.sample.network.RetrofitClientApp;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RetrofitClientApp.initDefaultRetrofitClient(this, BuildConfig.DEBUG);
    }

}
