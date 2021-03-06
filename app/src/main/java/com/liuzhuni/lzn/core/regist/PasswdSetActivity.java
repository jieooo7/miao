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
import com.liuzhuni.lzn.core.index_new.MainActivity;
import com.liuzhuni.lzn.core.login.ButtonThread;
import com.liuzhuni.lzn.core.login.Threadable;
import com.liuzhuni.lzn.core.login.model.LoginModel;
import com.liuzhuni.lzn.core.login.ui.DoubleRightView;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.core.personInfo.ui.LoginoutDialog;
import com.liuzhuni.lzn.sec.RsaEncode;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.utils.log.CommonLog;
import com.liuzhuni.lzn.utils.log.LogFactory;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonBaseRequest;
import com.liuzhuni.lzn.volley.GsonRequest;

import java.util.Map;

public class PasswdSetActivity extends Base2Activity {

    private CommonLog log = LogFactory.createLog("passwdset");
    private ButtonThread mThread;


    @ViewInject(R.id.title_left)
    private TextView mBackTv;
    @ViewInject(R.id.title_right)
    private TextView mRightTv;
    @ViewInject(R.id.title_middle)
    private TextView mMiddleTv;
    @ViewInject(R.id.passwd_set_tv)
    private TextView mSubmitTv;
    @ViewInject(R.id.passwd_set_et)
    private DoubleRightView mPasswdEt;

    private boolean mIsRegister;
    private String mCodes = "";

    private boolean isSend = true;

    private LoginoutDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwd_set);
        initData();
        findViewById();
        initUI();
        setListener();
    }

    @Override
    protected void initData() {

//判断注册还是忘记密码
        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                mIsRegister = getIntent().getExtras().getBoolean("isRegister") ? true : false;
                if (getIntent().getExtras().getString("codes") != null) {

                    mCodes = getIntent().getExtras().getString("codes");
                }

            }

        }

    }

    @Override
    protected void findViewById() {
        ViewUtils.inject(this);

    }

    @Override
    protected void initUI() {


        mBackTv.setText(getResources().getText(R.string.back));
//        mMiddleTv.setText(getResources().getString(R.string.passwd_set));
        mRightTv.setVisibility(View.GONE);

        if(mIsRegister){
            mSubmitTv.setText(getResources().getString(R.string.passwd_set));
            mMiddleTv.setText(getResources().getString(R.string.passwd_set));
        }else{
            mSubmitTv.setText(getResources().getString(R.string.button_submit));
            mMiddleTv.setText(getResources().getString(R.string.button_submit));
        }

    }

    @Override
    protected void setListener() {

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

    @OnClick(R.id.passwd_set_tv)
    public void submit(View v) {
        String text = mPasswdEt.getText().toString().trim();
        if (text.length() < 6 || text.length() > 12) {
            ToastUtil.customShow(PasswdSetActivity.this, getResources().getText(R.string.passwd_error));
            return;
        }
//        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);

        //点击提交处理
//        if(isSend) {
//            isSend = false;
        String code = "";
        try {
            code = RsaEncode.encode(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mIsRegister) {
            pullData(code, mCodes, UrlConfig.REG_PASSWD_SET);
        } else {//重置密码
            pullData(code, mCodes, UrlConfig.FORGOT_PASSWD_SET);
        }


//        }else{
//            isSend=true;
//        }


    }

    protected synchronized void pullData(final String pwd, final String code, final String url) {
        loadingdialog.show();
        executeRequest(new GsonBaseRequest<BaseModel<LoginModel>>(Request.Method.POST, url, new TypeToken<BaseModel<LoginModel>>() {
        }.getType(), responseListener(), errorListener()) {
            protected Map<String, String> getParams() {
                return new ApiParams().with("codes", code).with("pwd", pwd);
            }
        });
    }

    private Response.Listener<BaseModel<LoginModel>> responseListener() {
        return new Response.Listener<BaseModel<LoginModel>>() {
            @Override
            public void onResponse(BaseModel<LoginModel> codeModel) {
                loadingdialog.dismiss();
                if (codeModel.getRet() == 0) {
                    PreferencesUtils.putBooleanToSPMap(PasswdSetActivity.this, PreferencesUtils.Keys.IS_LOGIN, true);
                    PreferencesUtils.putValueToSPMap(PasswdSetActivity.this, PreferencesUtils.Keys.AUTH, codeModel.getData().getAuthName(), PreferencesUtils.Keys.USERINFO);
                    PreferencesUtils.putValueToSPMap(PasswdSetActivity.this, PreferencesUtils.Keys.TOKEN, codeModel.getData().getToken(), PreferencesUtils.Keys.USERINFO);
                    PreferencesUtils.putValueToSPMap(PasswdSetActivity.this, PreferencesUtils.Keys.HEAD_URL, codeModel.getData().getPic(), PreferencesUtils.Keys.USERINFO);
                    PreferencesUtils.putValueToSPMap(PasswdSetActivity.this, PreferencesUtils.Keys.NICKNAME, codeModel.getData().getName(), PreferencesUtils.Keys.USERINFO);
                    PreferencesUtils.putValueToSPMap(PasswdSetActivity.this, PreferencesUtils.Keys.TEL, codeModel.getData().getPhone(), PreferencesUtils.Keys.USERINFO);
                    PreferencesUtils.putValueToSPMap(PasswdSetActivity.this, PreferencesUtils.Keys.LEVEL, "Lv." + codeModel.getData().getGrade(), PreferencesUtils.Keys.USERINFO);
                    PreferencesUtils.putIntToSPMap(PasswdSetActivity.this, PreferencesUtils.Keys.UN_READ, codeModel.getData().getUnreadNum(), PreferencesUtils.Keys.USERINFO);

                    if (PreferencesUtils.getBooleanFromSPMap(PasswdSetActivity.this, PreferencesUtils.Keys.IS_SEND_SEX, PreferencesUtils.Keys.USERINFO)) {
                        pullSexData();
                    }

                    Intent intent = new Intent(PasswdSetActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    AppManager.getAppManager().finishActivity(RegistActivity.class);
                    AppManager.getAppManager().finishActivity(SendCodeActivity.class);

                } else {
                    ToastUtil.customShow(PasswdSetActivity.this, codeModel.getMes());
                }

            }
        };

    }


    protected synchronized void pullSexData() {
        executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.SEX_SET, BaseModel.class, responseSexListener(), errorListener()) {

            protected Map<String, String> getParams() {
                return new ApiParams().with("v", PreferencesUtils.getValueFromSPMap(PasswdSetActivity.this, PreferencesUtils.Keys.SEX, "1", PreferencesUtils.Keys.USERINFO));
            }

        });
    }

    private Response.Listener<BaseModel> responseSexListener() {
        return new Response.Listener<BaseModel>() {
            @Override
            public void onResponse(BaseModel sign) {
                if (sign.getRet() == 0) {
                    PreferencesUtils.putBooleanToSPMap(PasswdSetActivity.this, PreferencesUtils.Keys.IS_SEND_SEX, true, PreferencesUtils.Keys.USERINFO);
                }

            }
        };

    }


    protected boolean isSetOk() {
        String text = mPasswdEt.getText().toString().trim();
        return text.length() >= 6 && text.length() <= 12;

    }


    protected void startThread() {

        mThread = new ButtonThread(mSubmitTv, new Threadable() {
            @Override
            public boolean isSubmit() {
                return PasswdSetActivity.this.isSetOk();
            }
        });
        mThread.start();
    }


    public void showDialog() {

        if (mIsRegister) {
            mDialog = new LoginoutDialog(this, getResources().getString(R.string.register_dialog));

            mDialog.mSubmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                    AppManager.getAppManager().finishActivity(RegistActivity.class);
                    AppManager.getAppManager().finishActivity(SendCodeActivity.class);
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
        log.i("ondestroy-----" + mThread.hashCode() + "====" + mThread.getId() + "---------" + mThread.getName() + "-----" + mThread.isAlive() + "-----" + mThread.isInterrupted());
    }


}
