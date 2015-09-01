
package com.liuzhuni.lzn.push;

import android.app.Application;

import com.alibaba.sdk.android.AlibabaSDK;
import com.liuzhuni.lzn.volley.RequestManager;
import com.taobao.tae.sdk.callback.InitResultCallback;

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getName();


    @Override
    public void onCreate() {
        volleyInit();//初始化volley

        aliInit();//初始化alibaichun
    }

    private void volleyInit() {

        RequestManager.init(this);

    }

    private void aliInit(){

        AlibabaSDK.asyncInit(this, new InitResultCallback() {
            @Override
            public void onSuccess() {
//                Toast.makeText(MyApplication.this, "初始化成功", Toast.LENGTH_SHORT)
//                        .show();

            }

            @Override
            public void onFailure(int i, String s) {
//                Toast.makeText(MyApplication.this, "初始化异常", Toast.LENGTH_SHORT)
//                        .show();
            }
        });

    }


}
