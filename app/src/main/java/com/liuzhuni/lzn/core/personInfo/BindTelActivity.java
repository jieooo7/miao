package com.liuzhuni.lzn.core.personInfo;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.login.ButtonThread;
import com.liuzhuni.lzn.core.login.Threadable;
import com.liuzhuni.lzn.core.login.ui.CleanNoEditText;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.core.regist.PasswdSetActivity;
import com.liuzhuni.lzn.core.regist.SendCodeThread;
import com.liuzhuni.lzn.core.regist.model.CodeModel;
import com.liuzhuni.lzn.core.select.ui.CleanableEditText;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.utils.TextModify;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonBaseRequest;
import com.liuzhuni.lzn.volley.GsonRequest;

import java.util.Map;

public class BindTelActivity extends Base2Activity {


    @ViewInject(R.id.title_left)
    private TextView mBackTv;
    @ViewInject(R.id.title_right)
    private TextView mRightTv;
    @ViewInject(R.id.title_middle)
    private TextView mMiddleTv;
    @ViewInject(R.id.bind_tel_et)
    private CleanNoEditText mTelEt;
    @ViewInject(R.id.bind_tel_code_et)
    private CleanableEditText mCodeEt;
    @ViewInject(R.id.bind_tel_send_again)
    private TextView mSendCodeTv;
    @ViewInject(R.id.bind_tel_send_next)
    private TextView mSubmitTv;

    private String mTel="";

    private ButtonThread mThread;
    private SendCodeThread mCodeThread=null;

    private boolean mIsPassWd=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_tel);
        initData();
        findViewById();
        initUI();
        setListener();
    }

    @Override
    protected void initData() {

        if(getIntent()!=null){
            if(getIntent().getExtras()!=null){
                mIsPassWd=getIntent().getExtras().getBoolean("isPassWd");
            }
        }



    }

    @Override
    protected void findViewById() {

        ViewUtils.inject(this);

    }

    @Override
    protected void initUI() {

        mBackTv.setText(getResources().getText(R.string.me));
        mRightTv.setVisibility(View.GONE);
        mMiddleTv.setText(getResources().getString(R.string.bind_tel_no));


    }

    @Override
    protected void setListener() {

    }


    protected void startCodeThread(){
        mCodeThread=new SendCodeThread(this,mSendCodeTv);
        if(mCodeThread.isSendAgain()){
            mCodeThread.start();
        }
    }

    protected void startThread(){


        mThread=new ButtonThread(mSubmitTv,new Threadable() {
            @Override
            public boolean isSubmit() {
                return isTheSubmit();
            }
        });

        mThread.start();

    }



    @OnClick(R.id.title_left)
    public void back(View v){
        finish();
    }
    @OnClick(R.id.bind_tel_send_again)
    public void code(View v){
        //发送验证码
        mTel=delSpace(mTelEt.getText().toString().toString().trim());
        if(TextUtils.isEmpty(mTel)){
            ToastUtil.customShow(this, getResources().getString(R.string.tel_error));
            return;
        }
        if(TextModify.getInstance().isTel(mTel)){

            pullCodeData(mTel);
        }



    }

    @OnClick(R.id.bind_tel_send_next)
    public void submit(View v){
        String tel=delSpace(mTelEt.getText().toString().toString().trim());
        String code=mCodeEt.getText().toString().toString().trim();
        if(!TextModify.getInstance().isTel(tel)||!TextModify.getInstance().isCode(code)||!tel.equals(mTel)){
            ToastUtil.customShow(this, getResources().getString(R.string.tel_code_error));
            return;
        }

        pullBindTelData(mTel,code);


    }


    protected synchronized void pullCodeData(final String tel){
        executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.BIND_TEL,BaseModel.class,responseCodeListener(),errorListener()){

            protected Map<String, String> getParams() {
                return new ApiParams().with("phone", tel);
            }

        });
    }

    private Response.Listener<BaseModel> responseCodeListener() {
        return new Response.Listener<BaseModel>(){
            @Override
            public void onResponse(BaseModel addresModel) {
                if(addresModel.getRet()==0){
                    startCodeThread();
                }else{
                    ToastUtil.customShow(BindTelActivity.this,addresModel.getMes());
                }


                //备用

            }

        };

    }
    protected synchronized void pullBindTelData(final String tel,final String code){
        loadingdialog.show();
        executeRequest(new GsonBaseRequest<BaseModel<CodeModel>>(Request.Method.POST, UrlConfig.BIND_TEL_SUB,new TypeToken<BaseModel<CodeModel>>() {
        }.getType(),responseBindTelListener(),errorListener()){

            protected Map<String, String> getParams() {
                return new ApiParams().with("phone", tel).with("code",code);
            }

        });
    }

    private Response.Listener<BaseModel<CodeModel>> responseBindTelListener() {
        return new Response.Listener<BaseModel<CodeModel>>(){

            @Override
            public void onResponse(BaseModel<CodeModel> addresModel) {
                loadingdialog.dismiss();
                if(addresModel.getRet()==0){
                    if(mIsPassWd){
                        Intent registIntent = new Intent(BindTelActivity.this, PasswdSetActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isRegister", false);
                        bundle.putString("codes", addresModel.getData().getCodes());
                        registIntent.putExtras(bundle);
                        startActivity(registIntent);
                    }else{
                    }
                    PreferencesUtils.putValueToSPMap(BindTelActivity.this, PreferencesUtils.Keys.TEL,mTel, PreferencesUtils.Keys.USERINFO);
                    finish();
                }else{

                    ToastUtil.customShow(BindTelActivity.this, addresModel.getMes());

                }

                //备用
            }

        };

    }

    protected boolean isTheSubmit(){
        String tel=delSpace(mTelEt.getText().toString().toString().trim());
        String code=mCodeEt.getText().toString().toString().trim();
        return TextModify.getInstance().isTel(tel)&&TextModify.getInstance().isCode(code);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mThread=null;
        startThread();
    }

    @Override
    protected void onPause() {

        super.onPause();
        mThread.stopThread();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mCodeThread!=null){
            if(mCodeThread.isAlive()){
                mCodeThread.stopThread();
            }
        }

    }


    public String delSpace(String string){

        if(string.length()!=13){
            return "";
        }


        StringBuffer buffer = new StringBuffer();
        buffer.append(string);
        int index = 0;
        while (index < buffer.length()) {
            if (buffer.charAt(index) == ' ') {
                buffer.deleteCharAt(index);
            } else {

                index++;
            }
        }
        return buffer.toString();

    }

}
