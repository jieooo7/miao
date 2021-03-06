package com.liuzhuni.lzn.core.regist;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.Base2Activity;
import com.liuzhuni.lzn.config.AppManager;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.login.ButtonThread;
import com.liuzhuni.lzn.core.login.Threadable;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.core.personInfo.ui.LoginoutDialog;
import com.liuzhuni.lzn.core.regist.model.CodeModel;
import com.liuzhuni.lzn.core.select.ui.CleanableEditText;
import com.liuzhuni.lzn.utils.TextModify;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.utils.log.CommonLog;
import com.liuzhuni.lzn.utils.log.LogFactory;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonBaseRequest;
import com.liuzhuni.lzn.volley.GsonRequest;

import java.util.Map;

public class SendCodeActivity extends Base2Activity {


    private CommonLog log = LogFactory.createLog("sendCode");

    private ButtonThread mThread;
    private SendCodeThread mCodeThread = null;


    private String mTel = "";
    private String mCode = "";

    @ViewInject(R.id.title_left)
    private TextView mBackTv;
    @ViewInject(R.id.title_right)
    private TextView mRightTv;
    @ViewInject(R.id.title_middle)
    private TextView mMiddleTv;
    @ViewInject(R.id.code_tip)
    private TextView mTipTv;
    @ViewInject(R.id.send_again)
    private TextView send_again;
    @ViewInject(R.id.send_next_code)
    private TextView mNextTv;
    @ViewInject(R.id.send_et)
    private CleanableEditText mCodeEt;

    private boolean mIsRegister;

    private LoginoutDialog mDialog;

//    private boolean isSend=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_code);
        initData();
        findViewById();
        initUI();
        startCodeThread();//网络请求,启动线程

        setListener();
    }

    @Override
    protected void initData() {

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                mIsRegister = getIntent().getExtras().getBoolean("isRegister") ? true : false;

            }

        }

        mTel = getIntent().getExtras().getString("tel");


    }

    @Override
    protected void findViewById() {
        ViewUtils.inject(this);

    }

    @Override
    protected void initUI() {

        mCodeThread = new SendCodeThread(this, send_again);

        mBackTv.setText(getResources().getText(R.string.back));
        mMiddleTv.setText(getResources().getString(R.string.send_code_title));
        mRightTv.setVisibility(View.GONE);
        mTipTv.setText(getResources().getString(R.string.had_send) + mTel);

    }

    @Override
    protected void setListener() {

        //网络请求,启动线程
        if(isTouch){
            isTouch=false;
            if (mIsRegister) {
                pullCodeData(UrlConfig.SEND_CODE, mTel);
            } else {
                pullCodeData(UrlConfig.SEND_FOGOT_CODE, mTel);
            }
        }
    }

    protected void startCodeThread() {
        mCodeThread = new SendCodeThread(this, send_again);
        if (mCodeThread.isSendAgain()) {
            mCodeThread.start();
        }
    }

    protected void startThread() {


        mThread = new ButtonThread(mNextTv, new Threadable() {
            @Override
            public boolean isSubmit() {
                return isCode();
            }
        });

        mThread.start();

    }

    protected boolean isCode() {

        String code = mCodeEt.getText().toString().trim();

        return TextModify.getInstance().isCode(code);


    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 返回键
            showDialog();
        }

        return true;
    }


    @OnClick(R.id.title_left)
    public void back(View v) {

        showDialog();
    }

    @OnClick(R.id.send_again)
    public void sendCode(View v) {

        if (mCodeThread.isSendAgain()) {
            //网络请求,启动线程
            startCodeThread();
            if (mIsRegister) {
                pullCodeData(UrlConfig.SEND_CODE, mTel);
            } else {
                pullCodeData(UrlConfig.SEND_FOGOT_CODE, mTel);
            }
        }

    }

    protected synchronized void pullCodeData(final String url, final String tel) {
        executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, url, BaseModel.class, responseCodeListener(), errorListener()) {
            protected Map<String, String> getParams() {
                return new ApiParams().with("phone", tel);
            }
        });
    }

    private Response.Listener<BaseModel> responseCodeListener() {
        return new Response.Listener<BaseModel>() {
            @Override
            public void onResponse(BaseModel baseModel) {
                isTouch=true;
                if (baseModel.getRet() != 0) {

                    ToastUtil.customShow(SendCodeActivity.this, baseModel.getMes());

                }
            }
        };
    }


    @OnClick(R.id.send_next_code)
    public void submitNext(View v) {
        String code = mCodeEt.getText().toString().trim();

        if (!TextModify.getInstance().isCode(code)) {
            ToastUtil.customShow(SendCodeActivity.this, getResources().getText(R.string.code_error));
            return;
        }
        mCode = code;
        if (mIsRegister) {
            pullData(UrlConfig.CHECK_CODE, mTel, code);
        } else {
            pullData(UrlConfig.FORGOT_CHECK_CODE, mTel, code);
        }

    }

    protected synchronized void pullData(final String url, final String tel, final String code) {
        loadingdialog.show();
        executeRequest(new GsonBaseRequest<BaseModel<CodeModel>>(Request.Method.POST, url, new TypeToken<BaseModel<CodeModel>>() {
        }.getType(), responseListener(), errorListener()) {
            protected Map<String, String> getParams() {
                return new ApiParams().with("phone", tel).with("code", code);
            }
        });
    }

    private Response.Listener<BaseModel<CodeModel>> responseListener() {
        return new Response.Listener<BaseModel<CodeModel>>() {
            @Override
            public void onResponse(BaseModel<CodeModel> codeModel) {
                loadingdialog.dismiss();
                if (codeModel.getRet() == 0) {
                    Intent intent = new Intent(SendCodeActivity.this, PasswdSetActivity.class);
                    Bundle bundle = new Bundle();
                    if (mIsRegister) {
                        bundle.putBoolean("isRegister", true);
                    } else {
                        bundle.putBoolean("isRegister", false);
                    }
                    bundle.putString("codes", codeModel.getData().getCodes());
                    intent.putExtras(bundle);
                    startActivity(intent);
                    if (mCodeThread.isAlive()) {
                        mCodeThread.stopThread();
                    }

                } else {

                    ToastUtil.customShow(SendCodeActivity.this, codeModel.getMes());
                }

            }
        };

    }


    public void showDialog() {

        if (mIsRegister) {
            mDialog = new LoginoutDialog(this, getResources().getString(R.string.register_dialog));

            mDialog.mSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                    finish();
                    AppManager.getAppManager().finishActivity(RegistActivity.class);
                }
            });
            mDialog.mCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });

            mDialog.show();

        } else {
            finish();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();

        mThread = null;
        startThread();
        log.i("onResume" + mThread.hashCode() + "====" + mThread.getId() + "---------" + mThread.getName() + "-----" + mThread.isAlive() + "-----" + mThread.isInterrupted());
    }

    @Override
    protected void onPause() {

        super.onPause();
        mThread.stopThread();
        log.i("" + mThread.getId() + "---------" + mThread.getName() + "-----" + mThread.isAlive());
        log.i("onpause" + mThread.hashCode() + "====" + mThread.getId() + "---------" + mThread.getName() + "-----" + mThread.isAlive() + "-----" + mThread.isInterrupted());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCodeThread.stopThread();
        log.i("ondestroy-----" + mThread.hashCode() + "====" + mThread.getId() + "---------" + mThread.getName() + "-----" + mThread.isAlive() + "-----" + mThread.isInterrupted());
    }


}
