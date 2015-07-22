package com.liuzhuni.lzn.core.personInfo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.igexin.sdk.PushManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.Base2Activity;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.core.personInfo.ui.switch_button.CustomListener;
import com.liuzhuni.lzn.core.personInfo.ui.switch_button.SwitchButton;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonRequest;

import java.util.Map;

public class PushSetActivity extends Base2Activity{

    @ViewInject(R.id.title_left)
    private TextView mBackTv;
    @ViewInject(R.id.title_right)
    private TextView mRightTv;
    @ViewInject(R.id.title_middle)
    private TextView mMiddleTv;

    @ViewInject(R.id.push_set_iv)
    private SwitchButton mPushSet;
    @ViewInject(R.id.push_low_notify)
    private SwitchButton mPushLow;
    @ViewInject(R.id.push_recom)
    private SwitchButton mPushRec;
    @ViewInject(R.id.push_quiet_iv)
    private SwitchButton mPushQuiet;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_set);
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

        mMiddleTv.setText(getResources().getString(R.string.push_set));
        mRightTv.setVisibility(View.GONE);
        mPushSet.setChecked(PreferencesUtils.getBooleanFromSPMap(this, PreferencesUtils.Keys.PUSH_SET));//默认fasle 开启
        mPushLow.setChecked(PreferencesUtils.getBooleanFromSPMap(this, PreferencesUtils.Keys.PUSH_LOW_NOTIFY));
        mPushRec.setChecked(PreferencesUtils.getBooleanFromSPMap(this, PreferencesUtils.Keys.PUSH_RECOM));
        mPushQuiet.setChecked(PreferencesUtils.getBooleanFromSPMap(this,PreferencesUtils.Keys.PUSH_QUIET));

    }

    @Override
    protected void setListener() {

        mPushSet.setCustomListener(new MeCustomListener(PreferencesUtils.Keys.PUSH_SET){

            @Override
            public void writeCach(boolean isChecked) {
                if(isChecked){//
                    mPushLow.setChecked(true);//true 关闭
                    mPushRec.setChecked(true);
                    mPushQuiet.setChecked(true);
                    if(PushManager.getInstance().isPushTurnedOn(PushSetActivity.this)){
                        PushManager.getInstance().turnOffPush(PushSetActivity.this);
                    }

                }else{
                    if(!PushManager.getInstance().isPushTurnedOn(PushSetActivity.this)){
                        PushManager.getInstance().turnOnPush(PushSetActivity.this);
                    }
                    mPushLow.setChecked(PreferencesUtils.getBooleanFromSPMap(PushSetActivity.this, PreferencesUtils.Keys.PUSH_LOW_NOTIFY));
                    mPushRec.setChecked(PreferencesUtils.getBooleanFromSPMap(PushSetActivity.this, PreferencesUtils.Keys.PUSH_RECOM));
                    mPushQuiet.setChecked(PreferencesUtils.getBooleanFromSPMap(PushSetActivity.this,PreferencesUtils.Keys.PUSH_QUIET));
                }

                PreferencesUtils.putBooleanToSPMap(PushSetActivity.this, PreferencesUtils.Keys.PUSH_SET,isChecked);
            }
        });


        mPushLow.setCustomListener(new MeCustomListener(PreferencesUtils.Keys.PUSH_LOW_NOTIFY){

            @Override
            public void writeCach(boolean isChecked) {

                if(!isChecked){
                    mPushSet.setChecked(false);
                    if(!PushManager.getInstance().isPushTurnedOn(PushSetActivity.this)){
                        PushManager.getInstance().turnOnPush(PushSetActivity.this);
                    }
                }

                PreferencesUtils.putBooleanToSPMap(PushSetActivity.this, PreferencesUtils.Keys.PUSH_LOW_NOTIFY,isChecked);

            }
        });


        mPushRec.setCustomListener(new MeCustomListener(PreferencesUtils.Keys.PUSH_RECOM){

            @Override
            public void writeCach(boolean isChecked) {

                if(!isChecked){
                    mPushSet.setChecked(false);
                    if(!PushManager.getInstance().isPushTurnedOn(PushSetActivity.this)){
                        PushManager.getInstance().turnOnPush(PushSetActivity.this);
                    }
                }

                PreferencesUtils.putBooleanToSPMap(PushSetActivity.this, PreferencesUtils.Keys.PUSH_RECOM,isChecked);

            }
        });


        mPushQuiet.setCustomListener(new MeCustomListener(PreferencesUtils.Keys.PUSH_QUIET){

            @Override
            public void writeCach(boolean isChecked) {
                if(!isChecked){
                    mPushSet.setChecked(false);//打开推送
                    if(!PushManager.getInstance().isPushTurnedOn(PushSetActivity.this)){
                        PushManager.getInstance().turnOnPush(PushSetActivity.this);
                    }
                    if(PushManager.getInstance().isPushTurnedOn(PushSetActivity.this)){
                        PushManager.getInstance().setSilentTime(PushSetActivity.this,22,9);
                    }
                }else{
                    if(PushManager.getInstance().isPushTurnedOn(PushSetActivity.this)){
                        PushManager.getInstance().setSilentTime(PushSetActivity.this,22,0);
                    }
                }

                PreferencesUtils.putBooleanToSPMap(PushSetActivity.this, PreferencesUtils.Keys.PUSH_QUIET,isChecked);

            }
        });

    }

    @OnClick(R.id.title_left)
    public void back(View v){
        finish();
    }


    public abstract class MeCustomListener implements CustomListener {
        private String mKey;

        public MeCustomListener(String key) {

            this.mKey=key;
        }

//        @Override
//        public void writeCach(boolean isChecked) {
//
//            PreferencesUtils.putBooleanToSPMap(PushSetActivity.this, mKey,isChecked);
//        }
    }

    protected synchronized void pullPushData(final String push,final String reduction,final String PriceGod,final String Quiet){
        executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.PUSH_SET,BaseModel.class,responsePushListener(),errorListener(false)){

            protected Map<String, String> getParams() {
                return new ApiParams().with("push",push).with("reduction", reduction).with("PriceGod", PriceGod).with("Quiet", Quiet);
            }

        });
    }

    private Response.Listener<BaseModel> responsePushListener() {
        return new Response.Listener<BaseModel>(){
            @Override
            public void onResponse(BaseModel nameModel) {
                if(nameModel.getRet()==0){

                }

            }
        };

    }


    @Override
    protected void onPause() {
        super.onPause();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String push=PreferencesUtils.getBooleanFromSPMap(PushSetActivity.this, PreferencesUtils.Keys.PUSH_SET)?"0":"1";
                String reduction=PreferencesUtils.getBooleanFromSPMap(PushSetActivity.this, PreferencesUtils.Keys.PUSH_LOW_NOTIFY)?"0":"1";
                String PriceGod=PreferencesUtils.getBooleanFromSPMap(PushSetActivity.this, PreferencesUtils.Keys.PUSH_RECOM)?"0":"1";
                String Quiet=PreferencesUtils.getBooleanFromSPMap(PushSetActivity.this, PreferencesUtils.Keys.PUSH_QUIET)?"0":"1";
                pullPushData(push,reduction,PriceGod,Quiet);
            }
        }).start();

    }

//    @OnClick(R.id.title_left)
//    protected void back(View v){
//        String push=PreferencesUtils.getBooleanFromSPMap(this, PreferencesUtils.Keys.PUSH_SET)?"0":"1";
//        String reduction=PreferencesUtils.getBooleanFromSPMap(this, PreferencesUtils.Keys.PUSH_LOW_NOTIFY)?"0":"1";
//        String PriceGod=PreferencesUtils.getBooleanFromSPMap(this, PreferencesUtils.Keys.PUSH_RECOM)?"0":"1";
//        String Quiet=PreferencesUtils.getBooleanFromSPMap(this, PreferencesUtils.Keys.PUSH_QUIET)?"0":"1";
//        pullPushData(push,reduction,PriceGod,Quiet);
//        finish();
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) { // 返回键
//
//            String push=PreferencesUtils.getBooleanFromSPMap(this, PreferencesUtils.Keys.PUSH_SET)?"0":"1";
//            String reduction=PreferencesUtils.getBooleanFromSPMap(this, PreferencesUtils.Keys.PUSH_LOW_NOTIFY)?"0":"1";
//            String PriceGod=PreferencesUtils.getBooleanFromSPMap(this, PreferencesUtils.Keys.PUSH_RECOM)?"0":"1";
//            String Quiet=PreferencesUtils.getBooleanFromSPMap(this, PreferencesUtils.Keys.PUSH_QUIET)?"0":"1";
//            pullPushData(push,reduction,PriceGod,Quiet);
//            finish();
//            // overridePendingTransition(R.anim.translate_horizontal_finish_in,
//            // R.anim.translate_horizontal_finish_out);
//        }
//
//        return true;
//    }




}
