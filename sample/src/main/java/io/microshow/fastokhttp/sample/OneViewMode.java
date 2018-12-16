package io.microshow.fastokhttp.sample;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import io.microshow.fastokhttp.FastOkHttpManager;
import io.microshow.fastokhttp.callback.INetCallback;
import io.microshow.fastokhttp.core.CachePolicyMode;
import io.microshow.fastokhttp.core.ResponseDataWrapper;

/**
 * Created by Super on 2018/12/16.
 */
public class OneViewMode extends ViewModel {

    public MutableLiveData<String> data = new MutableLiveData();

    public void loadData () {
        FastOkHttpManager.doGet("https://api.douban.com/v2/book/search?q=android&start=0&count=1",null,
                CachePolicyMode.POLICY_NETWORK_ELSE_CACHED,
                new INetCallback<String>(){
                    @Override
                    public void onSuccess(ResponseDataWrapper response, String s) {
                        super.onSuccess(response, s);
                        data.postValue("onSuccess \nisCacheData="+response.isCacheData()+"\ndata="+s);
                    }

                    @Override
                    public void onFailure(int errorCode, String msg) {
                        super.onFailure(errorCode, msg);
                        data.postValue(msg);
                    }
                });
    }

}
