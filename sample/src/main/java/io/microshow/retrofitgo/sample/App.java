package io.microshow.retrofitgo.sample;

import android.app.Application;

import io.microshow.retrofitgo.sample.network.RetrofitClientApp;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RetrofitClientApp.initDefaultRetrofitClient(this, BuildConfig.DEBUG);
    }

}
