
package com.liuzhuni.lzn.push;

import android.app.Application;

import com.liuzhuni.lzn.volley.RequestManager;

public class MyApplication extends Application {
    private static final String TAG = MyApplication.class.getName();


    @Override
    public void onCreate() {
        volleyInit();//初始化volley
    }

    private void volleyInit() {

        RequestManager.init(this);

    }

}
