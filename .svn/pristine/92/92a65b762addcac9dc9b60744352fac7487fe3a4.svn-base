package com.liuzhuni.lzn.core.login;

import android.app.Activity;
import android.content.Intent;

import com.liuzhuni.lzn.config.Check;

/**
 * Created by Andrew Lee on 2015/4/21.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-21
 * Time: 15:21
 */
public class TheIntent {

    private Loginable mLogin;
    private Activity mActivity;

    public TheIntent(Activity activity,Loginable login) {
        this.mLogin = login;
        this.mActivity=activity;

    }


    public void go(){

        if(!Check.isLogin(mActivity)){//没有登录
            Intent intent=new Intent(mActivity,LoginActivity.class);
            mActivity.startActivity(intent);

        }else{
            this.mLogin.intent();

        }

    }
}
