package com.liuzhuni.lzn.base;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
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


public abstract class BaseFragActivity extends FragmentActivity {
	public static final String TAG = "BaseActivity";
    protected Activity activity;
    private InputMethodManager manager;
    public LoadingDialog loadingdialog;
    public boolean isTouch=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

        activity = this;
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
        loadingdialog = new LoadingDialog(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getAppManager().finishActivity(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();

	}

    @Override
    public void onStop() {
        super.onStop();
        RequestManager.cancelAll(this);
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
                        PreferencesUtils.putBooleanToSPMap(BaseFragActivity.this, PreferencesUtils.Keys.IS_LOGIN, false);
                        PreferencesUtils.clearSPMap(BaseFragActivity.this, PreferencesUtils.Keys.USERINFO);
                        Intent intent = new Intent(BaseFragActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
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
                        PreferencesUtils.putBooleanToSPMap(BaseFragActivity.this, PreferencesUtils.Keys.IS_LOGIN, false);
                        PreferencesUtils.clearSPMap(BaseFragActivity.this, PreferencesUtils.Keys.USERINFO);
                        Intent intent = new Intent(BaseFragActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        if (isToast) {
                            ToastUtil.customShow(BaseFragActivity.this, getResources().getText(R.string.error_retry));
                        }
                    }
                } else {
                    if (isToast) {
                        ToastUtil.customShow(BaseFragActivity.this, getResources().getText(R.string.bad_net));
                    }
                }
//                RequestManager.cancelAll(this);
            }
        };
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
	 * 
	 * 描述：数据初始化
	 */
	protected abstract void initData();

	/**
	 * 
	 * 描述：渲染界面
	 */
	protected abstract void findViewById();

	/**
	 * 
	 * 描述：界面初始化
	 */
	protected abstract void initUI();

	/**
	 * 
	 * 描述：设置监听
	 */
	protected abstract void setListener();

}
