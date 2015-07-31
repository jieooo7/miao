package com.liuzhuni.lzn.core.guide;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.Base2Activity;
import com.liuzhuni.lzn.config.MessageWhat;
import com.liuzhuni.lzn.core.index_new.MainActivity;
import com.liuzhuni.lzn.utils.PreferencesUtils;

public class StartActivity extends Base2Activity {


    public Handler mHandler=new Handler() {
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case MessageWhat.START_UP:
                    Intent intent = new Intent();
                    intent.setClass(StartActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!PreferencesUtils.getBooleanFromSPMap(this, PreferencesUtils.Keys.IS_WELCOME)) {
            startActivity(new Intent(this, GuideActivity.class));
            finish();
        }else{
            initData();
        }
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        findViewById();
        initUI();
        setListener();
    }

    @Override
    protected void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                mHandler.sendEmptyMessage(MessageWhat.START_UP);

            }
        }).start();

    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void setListener() {

    }


}
