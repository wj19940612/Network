package com.hyx.network;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.text);

        APIManager.getInstance().reset();


        mTextView.postDelayed(new Runnable() {
            @SuppressLint("CheckResult")
            @Override
            public void run() {
                NetworkApi networkApi = APIManager.getInstance().getNetworkApi();
                HashMap hashMap = new HashMap();
                hashMap.put("test","2222");
                networkApi.getData()
                        .subscribeOn(Schedulers.io())//IO线程加载数据
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<Object>() {

                            @Override
                            public void accept(Object o) throws Exception {
                                Log.d("wj", " " + o.toString());
                            }
                        });
            }
        },1500);

    }
}
