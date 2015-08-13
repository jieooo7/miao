package com.liuzhuni.lzn.base;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.config.AppManager;
import com.liuzhuni.lzn.core.loading.LoadingDialog;
import com.liuzhuni.lzn.core.login.LoginActivity;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.utils.fileHelper.CommonUtil;
import com.liuzhuni.lzn.volley.RequestManager;
import com.umeng.analytics.MobclickAgent;


/**
 * Base2Activity.java description:activity 基类 主要是对activity的管理
 *
 * @author Andrew Lee version 2014-10-16 上午8:41:34
 */
public abstract class Base2Activity extends Activity {
    public static final String TAG = "Base2Activity";

    private InputMethodManager manager;
    protected Activity activity;
    public LoadingDialog loadingdialog;
    public boolean isTouch=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;

        // 添加到Activity栈中

        AppManager.getAppManager().addActivity(this);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        loadingdialog = new LoadingDialog(this);

    }

    public void showLoadingDialog(){

        loadingdialog.show();
    }
    public void DismissDialog(){

        loadingdialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        RequestManager.cancelAll(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity
        AppManager.getAppManager().finishActivity(this);
    }


    public void executeRequest(Request<?> request) {
        if (!CommonUtil.checkNetState(this)) {
//            ToastUtil.customShow(this, getResources().getText(R.string.bad_net));
            return;
        }
            RequestManager.addRequest(request, this);

    }

    public Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(activity,getResources().getText(R.string.error_retry), Toast.LENGTH_LONG).show();
                loadingdialog.dismiss();

                isTouch=true;
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 401) {//重新登录
                        PreferencesUtils.putBooleanToSPMap(Base2Activity.this, PreferencesUtils.Keys.IS_LOGIN, false);
                        PreferencesUtils.clearSPMap(Base2Activity.this, PreferencesUtils.Keys.USERINFO);
                        Intent intent = new Intent(Base2Activity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
//                        ToastUtil.customShow(Base2Activity.this, getResources().getText(R.string.error_retry));
                    }
                } else {
//                    ToastUtil.customShow(Base2Activity.this, getResources().getText(R.string.bad_net));
                }
//                RequestManager.cancelAll(this);
            }
        };
    }

    public Response.ErrorListener errorListener(final boolean isToast) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(activity,getResources().getText(R.string.error_retry), Toast.LENGTH_LONG).show();
                loadingdialog.dismiss();
                isTouch=true;
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 401) {//重新登录
                        PreferencesUtils.putBooleanToSPMap(Base2Activity.this, PreferencesUtils.Keys.IS_LOGIN, false);
                        PreferencesUtils.clearSPMap(Base2Activity.this, PreferencesUtils.Keys.USERINFO);
                        Intent intent = new Intent(Base2Activity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        if (isToast) {
                            ToastUtil.customShow(Base2Activity.this, getResources().getText(R.string.error_retry));
                        }
                    }
                } else {
                    if (isToast) {
                        ToastUtil.customShow(Base2Activity.this, getResources().getText(R.string.bad_net));
                    }
                }
//                RequestManager.cancelAll(this);
            }
        };
    }


    @Override
    public void onBackPressed() {
        // scrollToFinishActivity();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (getCurrentFocus() != null
                    && getCurrentFocus().getWindowToken() != null) {
                manager.hideSoftInputFromWindow(getCurrentFocus()
                        .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
        return super.onTouchEvent(event);
    }


    /**
     * 自定义返回键的效果
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 返回键
            finish();
            // overridePendingTransition(R.anim.translate_horizontal_finish_in,
            // R.anim.translate_horizontal_finish_out);
        }

        return true;
    }

    /**
     * 描述：数据初始化
     */
    protected abstract void initData();

    /**
     * 描述：渲染界面
     */
    protected abstract void findViewById();

    /**
     * 描述：界面初始化
     */
    protected abstract void initUI();

    /**
     * 描述：设置监听
     */
    protected abstract void setListener();
}
