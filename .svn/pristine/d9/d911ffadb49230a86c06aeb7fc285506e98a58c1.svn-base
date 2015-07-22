package com.liuzhuni.lzn.core.personInfo;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.Base2Activity;
import com.liuzhuni.lzn.config.Check;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.login.ButtonThread;
import com.liuzhuni.lzn.core.login.Threadable;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.utils.TextModify;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonRequest;

import java.util.Map;

public class NicknameActivity extends Base2Activity {


    @ViewInject(R.id.title_left)
    private TextView mBackTv;
    @ViewInject(R.id.title_right)
    private TextView mRightTv;
    @ViewInject(R.id.title_middle)
    private TextView mMiddleTv;
    @ViewInject(R.id.submit_nick_name)
    private TextView mSubmitTv;
    @ViewInject(R.id.nick_name_et)
    private EditText mNickName;

    private String mName;

    private ButtonThread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);
        initData();
        findViewById();
        initUI();
        setListener();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void findViewById() {

        ViewUtils.inject(this);

    }

    @Override
    protected void initUI() {

        mBackTv.setText(getResources().getText(R.string.me));
        mRightTv.setVisibility(View.GONE);
        mMiddleTv.setText(getResources().getString(R.string.nickname_set));
        if (Check.hasNickname(this)) {
            mNickName.setText(PreferencesUtils.getValueFromSPMap(this, PreferencesUtils.Keys.NICKNAME,"",PreferencesUtils.Keys.USERINFO));
        }

    }

    @Override
    protected void setListener() {

    }

    protected void startThread(){

        mThread=new ButtonThread(mSubmitTv,new Threadable() {
            @Override
            public boolean isSubmit() {
                return NicknameActivity.this.isSubmit();
            }
        });
        mThread.start();
    }


    @OnClick(R.id.title_left)
    public void back(View v){
        finish();
    }


    @OnClick(R.id.submit_nick_name)
    public void submit(View v){
        String name=mNickName.getText().toString().toString().trim();
        if(!TextModify.getInstance().isNickName(name)){
            ToastUtil.customShow(this,getResources().getString(R.string.nickname_not_pass));
            return;
        }
        mName=name;
        pullNameData(name);
    }

    protected synchronized void pullNameData(final String name){
        loadingdialog.show();
        executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.NAME_SET, BaseModel.class, responseNameListener(), errorListener()) {

            protected Map<String, String> getParams() {
                return new ApiParams().with("name", name);
            }

        });
    }

    private Response.Listener<BaseModel> responseNameListener() {
        return new Response.Listener<BaseModel>(){
            @Override
            public void onResponse(BaseModel nameModel) {
                loadingdialog.dismiss();
                if(nameModel.getRet()==0){
                    PreferencesUtils.putValueToSPMap(NicknameActivity.this, PreferencesUtils.Keys.NICKNAME,mName,PreferencesUtils.Keys.USERINFO);

                    finish();
                }else{
                    ToastUtil.customShow(NicknameActivity.this, nameModel.getMes());
                }

            }
        };

    }




    protected boolean isSubmit(){
        String name=mNickName.getText().toString().toString().trim();
        return TextModify.getInstance().isNickName(name);
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


}
