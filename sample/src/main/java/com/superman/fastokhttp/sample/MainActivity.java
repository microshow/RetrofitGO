package com.superman.fastokhttp.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.superman.fastokhttp.FastOkHttpManager;
import com.superman.fastokhttp.callback.INetCallback;
import com.superman.fastokhttp.core.CachePolicyMode;
import com.superman.fastokhttp.core.ResponseDataWrapper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FastOkHttpManager.doGet("https://api.douban.com/v2/book/search?q=java&start=0&count=1",null,
                        CachePolicyMode.POLICY_NETWORK_ELSE_CACHED,
                        new INetCallback<String>(){
                            @Override
                            public void onSuccess(ResponseDataWrapper response, String s) {
                                super.onSuccess(response, s);
                                Toast.makeText(getApplication(),"onSuccess:isCacheData="+response.isCacheData()+";data="+s, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int errorCode, String msg) {
                                super.onFailure(errorCode, msg);
                                Toast.makeText(getApplication(),"onFailure:"+msg, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

}
