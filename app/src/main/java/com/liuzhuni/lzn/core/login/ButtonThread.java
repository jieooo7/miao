package com.liuzhuni.lzn.core.login;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.widget.TextView;

import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.config.AppManager;
import com.liuzhuni.lzn.config.MessageWhat;
import com.liuzhuni.lzn.utils.log.CommonLog;
import com.liuzhuni.lzn.utils.log.LogFactory;

/**
 * Created by Andrew Lee on 2015/4/16.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-16
 * Time: 14:07
 */
public class ButtonThread extends Thread{

    private CommonLog log= LogFactory.createLog("ButtonThread");

    private static TextView mSubmitTv;
    private Threadable mInter;

    private volatile boolean mQuit=false;
    private boolean mIsSend;

    private static Drawable unSelect= AppManager.getAppManager().currentActivity().getResources().getDrawable(R.drawable.button_unselect_back);
    private static Drawable select=AppManager.getAppManager().currentActivity().getResources().getDrawable(R.drawable.button_select_back);

    public static Handler mHandler=new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void handleMessage(android.os.Message msg){
            switch (msg.what){
                case MessageWhat.BUTTON_LOGIN_OK:
//                    mSubmitTv.setBackgroundResource(R.drawable.button_select_back);
                    mSubmitTv.setBackgroundDrawable(select);
                    break;
                case MessageWhat.BUTTON_LOGIN_UN:
//                    mSubmitTv.setBackgroundResource(R.drawable.button_unselect_back);
                    mSubmitTv.setBackgroundDrawable(unSelect);
                    break;
            }

        }

    };
    public ButtonThread(TextView SubmitTv,Threadable inter){
        this.mSubmitTv=SubmitTv;
        this.mInter=inter;
    }

    public void stopThread(){
        this.mQuit=true;

    }

    public boolean isSubmit(){

           return mInter.isSubmit();

    }


    public void run() {
        boolean isOk=true;
//        mQuit=false;
        while (!mQuit){
            mIsSend=isSubmit();
            if(mIsSend){
                if(isOk){
                    isOk=false;//标记当前状态
                    mHandler.sendEmptyMessage(MessageWhat.BUTTON_LOGIN_OK);
                }

            }else{
                if(!isOk){
                    isOk=true;
                    mHandler.sendEmptyMessage(MessageWhat.BUTTON_LOGIN_UN);
                }
            }

            try {
                this.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }





}
