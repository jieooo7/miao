package com.liuzhuni.lzn.base;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.liuzhuni.lzn.config.AppManager;
import com.liuzhuni.lzn.volley.RequestManager;


public abstract class BaseFragActivity extends FragmentActivity {
	public static final String TAG = "BaseActivity";
    protected Activity activity;
    private InputMethodManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

        activity = this;
		super.onCreate(savedInstanceState);
		AppManager.getAppManager().addActivity(this);
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


    protected void executeRequest(Request<?> request) {
        RequestManager.addRequest(request, this);
    }

    protected Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, error.getMessage(), Toast.LENGTH_LONG).show();
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
