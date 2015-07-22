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
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.login.ButtonThread;
import com.liuzhuni.lzn.core.login.Threadable;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.utils.log.CommonLog;
import com.liuzhuni.lzn.utils.log.LogFactory;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonRequest;

import java.util.Map;

public class FeedbackActivity extends Base2Activity {


    private CommonLog log = LogFactory.createLog("feedback");


    @ViewInject(R.id.title_left)
    private TextView mBackTv;
    @ViewInject(R.id.title_right)
    private TextView mRightTv;
    @ViewInject(R.id.title_middle)
    private TextView mMiddleTv;
    @ViewInject(R.id.feedback_et)
    private EditText mFeedbackEt;
    @ViewInject(R.id.feedback_submit_tv)
    private TextView mSubmitTv;

    private ButtonThread mThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
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

//        mBackTv.setText(getResources().getText(R.string.back));
        mMiddleTv.setText(getResources().getString(R.string.Feedback));
        mRightTv.setVisibility(View.GONE);

    }

    @Override
    protected void setListener() {

    }


    protected void startThread() {


        mThread = new ButtonThread(mSubmitTv, new Threadable() {
            @Override
            public boolean isSubmit() {
                return isFeedSubmit();
            }
        });

        mThread.start();

    }

    @OnClick(R.id.title_left)
    public void back(View v) {

        finish();
    }


    @OnClick(R.id.feedback_submit_tv)
    public void submit(View v) {

        String feedback = mFeedbackEt.getText().toString();
        if(feedback.trim().length()<6){

            ToastUtil.customShow(FeedbackActivity.this, getResources().getText(R.string.feed_error));
            return;
        }
        pullFeedData(feedback);

        //提交信息
    }

    protected synchronized void pullFeedData(final String content){
        loadingdialog.show();
        executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.FEED_BACK,BaseModel.class,responseSexListener(),errorListener()){

            protected Map<String, String> getParams() {
                return new ApiParams().with("con",content);
            }

        });
    }

    private Response.Listener<BaseModel> responseSexListener() {
        return new Response.Listener<BaseModel>(){
            @Override
            public void onResponse(BaseModel sign) {
                loadingdialog.dismiss();
                if(sign.getRet()==0){
                    ToastUtil.customShow(FeedbackActivity.this, getResources().getText(R.string.feed_ok));
                    finish();
                }else{
                    ToastUtil.customShow(FeedbackActivity.this,sign.getMes());
                }

            }
        };

    }




    protected boolean isFeedSubmit() {

        String feedback = mFeedbackEt.getText().toString().trim();

        return feedback.length() > 5 ? true : false;
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
