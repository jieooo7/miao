package com.liuzhuni.lzn.core.index_new;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.igexin.sdk.PushManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.BaseFragActivity;
import com.liuzhuni.lzn.config.AppManager;
import com.liuzhuni.lzn.config.Check;
import com.liuzhuni.lzn.config.MessageWhat;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.index_new.fragment.FragmentIndex;
import com.liuzhuni.lzn.core.index_new.fragment.FragmentInfo;
import com.liuzhuni.lzn.core.index_new.fragment.FragmentNews;
import com.liuzhuni.lzn.core.login.LoginActivity;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.example.qr_codescan.MipcaActivityCapture;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonRequest;
import com.umeng.update.UmengUpdateAgent;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseFragActivity {



    @ViewInject(R.id.title_right_iv)
    private ImageView mRightIv;
    @ViewInject(R.id.title_middle)
    private TextView mMiddleTv;
    @ViewInject(R.id.title_left)
    private ImageView mLeftIv;


    @ViewInject(R.id.index_pick)
    private TextView mPickBt;

    @ViewInject(R.id.index_people_say)
    private TextView mNewsBt;

    @ViewInject(R.id.index_person_info)
    private TextView minfoBt;

    @ViewInject(R.id.container_fragment)
    private FrameLayout mFrame;


    private boolean dayFlag=true;//签到开关

    private PopupWindow window = null;
    private Boolean isExit = false;// 双击退出
    private volatile boolean mIsExit = false;

    private int mState=1;





    private FragmentTransaction mTransaction;
    private int screenWidth;

    private FragmentIndex mFragIndex;
    private FragmentInfo mFragInfo;
    private FragmentNews mFragNews;

    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;



    public Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MessageWhat.SHOW_POP:

                    showPopup();
                    break;
            }

        }

    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UmengUpdateAgent.setUpdateOnlyWifi(false);
        UmengUpdateAgent.update(this);
        setContentView(R.layout.activity_main);

        PushManager.getInstance().initialize(this.getApplicationContext());
        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);

        initLoc();

        initData();
        findViewById();
        initUI();
        setListener();

        if (!Check.isNotFirst(this)) {
            startThread();
        }
    }

    @Override
    protected void initData() {

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;

        mFragIndex=new FragmentIndex();
        mFragInfo=new FragmentInfo();
        mFragNews=new FragmentNews();

    }

    protected void initLoc() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09");
        option.setIsNeedAddress(true);
        option.setScanSpan(50000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    protected void findViewById() {

        ViewUtils.inject(this);

    }

    @Override
    protected void initUI() {


        mMiddleTv.setText(getText(R.string.the_name));

        sendClientId();


        mTransaction=getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.container_fragment, mFragIndex);
        mTransaction.addToBackStack(null);
        mTransaction.commit();

    }

    @Override
    protected void setListener() {

    }



    protected void showPopup() {

        View contentView = LayoutInflater.from(this).inflate(
                R.layout.popup_window, null);

        ImageView boyIv = (ImageView) contentView.findViewById(R.id.pop_boy);
        ImageView girlIv = (ImageView) contentView.findViewById(R.id.pop_girl);
        window = new PopupWindow(contentView, screenWidth - getResources().getDimensionPixelOffset(R.dimen.pop_offset), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        window.setTouchable(true);
        window.setOutsideTouchable(false);
//            window.showAsDropDown(mTitleView, 0, 20);
        window.showAtLocation(mFrame, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 260);
        backgroundAlpha(0.45f);
        boyIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundAlpha(1.0f);
                window.dismiss();
                PreferencesUtils.putBooleanToSPMap(MainActivity.this, PreferencesUtils.Keys.IS_FIRST, true);
                PreferencesUtils.putIntToSPMap(MainActivity.this, PreferencesUtils.Keys.SEX, 0, PreferencesUtils.Keys.USERINFO);
            }
        });

//            if (window != null && window.isShowing()) {
//                window.dismiss();
//            }
        girlIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundAlpha(1.0f);
                window.dismiss();
                PreferencesUtils.putBooleanToSPMap(MainActivity.this, PreferencesUtils.Keys.IS_FIRST, true);
                PreferencesUtils.putIntToSPMap(MainActivity.this, PreferencesUtils.Keys.SEX, 1, PreferencesUtils.Keys.USERINFO);
            }
        });


    }

    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha)
    {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }

    protected void startThread() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mIsExit) {
                    if (mMiddleTv.getVisibility() == View.VISIBLE && mMiddleTv.isShown()) {
                        mHandler.sendEmptyMessage(MessageWhat.SHOW_POP);
                        break;
                    } else {

                        try {
                            Thread.currentThread().sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }).start();

    }



    @Override
    protected void onPause() {
        super.onPause();
        mIsExit = true;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient.isStarted()) {

            mLocationClient.stop();

        }
    }



    @OnClick(R.id.title_left)
    public void check(View v) {

        //签到
        if (!Check.isLogin(this)) {//没有登录
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else {

            if(dayFlag){
                dayFlag=false;
                pullDayData();
            }


        }


    }

    @OnClick(R.id.title_right_iv)
    public void qrCode(View v) {

        //签到
        //扫描条码
        Intent intent = new Intent();
        intent.setClass(this, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


    @OnClick(R.id.index_pick)
    public void pick(View v) {

        if(mState!=1){
            mState=1;

            mMiddleTv.setText(getText(R.string.the_name));
            mLeftIv.setVisibility(View.VISIBLE);
            mRightIv.setVisibility(View.VISIBLE);

            Drawable mDrawable1 = getResources().getDrawable(R.drawable.ic_home_s);
            Drawable mDrawable2 = getResources().getDrawable(R.drawable.ic_publish_n);
            Drawable mDrawable3 = getResources().getDrawable(R.drawable.ic_my_n);
            mDrawable1.setBounds(0,0,mDrawable1.getMinimumWidth(),mDrawable1.getMinimumHeight());
            mDrawable2.setBounds(0,0,mDrawable2.getMinimumWidth(),mDrawable2.getMinimumHeight());
            mDrawable3.setBounds(0,0,mDrawable3.getMinimumWidth(),mDrawable3.getMinimumHeight());

            mPickBt.setCompoundDrawables(null,mDrawable1,null,null);
            mPickBt.setTextColor(getResources().getColor(R.color.red));
            mNewsBt.setCompoundDrawables(null,mDrawable2,null,null);
            mNewsBt.setTextColor(getResources().getColor(R.color.gray_text_color));
            minfoBt.setCompoundDrawables(null,mDrawable3,null,null);
            minfoBt.setTextColor(getResources().getColor(R.color.gray_text_color));


            mTransaction=getSupportFragmentManager().beginTransaction();
            if(!mFragIndex.isAdded()){
                mTransaction.add(R.id.container_fragment,mFragIndex);
            }
            mTransaction.show(mFragIndex);
            mTransaction.hide(mFragInfo);
            mTransaction.hide(mFragNews);
            mTransaction.addToBackStack(null);
            mTransaction.commit();
        }

    }


    @OnClick(R.id.index_people_say)
    public void news(View v) {

        if(mState!=2){
            mState=2;

            mMiddleTv.setText(getText(R.string.all_news));
            mLeftIv.setVisibility(View.GONE);
            mRightIv.setVisibility(View.GONE);


            Drawable mDrawable1 = getResources().getDrawable(R.drawable.ic_home_n);
            Drawable mDrawable2 = getResources().getDrawable(R.drawable.ic_publish_s);
            Drawable mDrawable3 = getResources().getDrawable(R.drawable.ic_my_n);
            mDrawable1.setBounds(0,0,mDrawable1.getMinimumWidth(),mDrawable1.getMinimumHeight());
            mDrawable2.setBounds(0,0,mDrawable2.getMinimumWidth(),mDrawable2.getMinimumHeight());
            mDrawable3.setBounds(0,0,mDrawable3.getMinimumWidth(),mDrawable3.getMinimumHeight());

            mPickBt.setCompoundDrawables(null,mDrawable1,null,null);
            mPickBt.setTextColor(getResources().getColor(R.color.gray_text_color));
            mNewsBt.setCompoundDrawables(null,mDrawable2,null,null);
            mNewsBt.setTextColor(getResources().getColor(R.color.red));
            minfoBt.setCompoundDrawables(null,mDrawable3,null,null);
            minfoBt.setTextColor(getResources().getColor(R.color.gray_text_color));

            mTransaction=getSupportFragmentManager().beginTransaction();
            if(!mFragNews.isAdded()){
                mTransaction.add(R.id.container_fragment,mFragNews);
            }
            mTransaction.show(mFragNews);
            mTransaction.hide(mFragInfo);
            mTransaction.hide(mFragIndex);
            mTransaction.addToBackStack(null);
            mTransaction.commit();


        }



    }


    @OnClick(R.id.index_person_info)
    public void info(View v) {

        if(mState!=3){
            mState=3;

            mMiddleTv.setText(getText(R.string.me));
            mLeftIv.setVisibility(View.GONE);
            mRightIv.setVisibility(View.VISIBLE);


            Drawable mDrawable1 = getResources().getDrawable(R.drawable.ic_home_n);
            Drawable mDrawable2 = getResources().getDrawable(R.drawable.ic_publish_n);
            Drawable mDrawable3 = getResources().getDrawable(R.drawable.ic_my_s);
            mDrawable1.setBounds(0,0,mDrawable1.getMinimumWidth(),mDrawable1.getMinimumHeight());
            mDrawable2.setBounds(0,0,mDrawable2.getMinimumWidth(),mDrawable2.getMinimumHeight());
            mDrawable3.setBounds(0,0,mDrawable3.getMinimumWidth(),mDrawable3.getMinimumHeight());

            mPickBt.setCompoundDrawables(null,mDrawable1,null,null);
            mPickBt.setTextColor(getResources().getColor(R.color.gray_text_color));
            mNewsBt.setCompoundDrawables(null,mDrawable2,null,null);
            mNewsBt.setTextColor(getResources().getColor(R.color.gray_text_color));
            minfoBt.setCompoundDrawables(null,mDrawable3,null,null);
            minfoBt.setTextColor(getResources().getColor(R.color.red));




            mTransaction=getSupportFragmentManager().beginTransaction();

            if(!mFragInfo.isAdded()){
                mTransaction.add(R.id.container_fragment,mFragInfo);
            }
            mTransaction.show(mFragInfo);
            mTransaction.hide(mFragNews);
            mTransaction.hide(mFragIndex);
            mTransaction.addToBackStack(null);
            mTransaction.commit();

        }

    }



    protected  void pullDayData() {  //签到

        executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.USER_SIGN, BaseModel.class, responseDayListener(), errorDayListener()) {

        });

    }

    public Response.ErrorListener errorDayListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(activity,getResources().getText(R.string.error_retry), Toast.LENGTH_LONG).show();
                loadingdialog.dismiss();
                dayFlag=true;
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 401) {//重新登录
                        PreferencesUtils.putBooleanToSPMap(MainActivity.this, PreferencesUtils.Keys.IS_LOGIN, false);
                        PreferencesUtils.clearSPMap(MainActivity.this, PreferencesUtils.Keys.USERINFO);
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
    }

    private Response.Listener<BaseModel> responseDayListener() {
        return new Response.Listener<BaseModel>() {
            @Override
            public void onResponse(BaseModel sign) {
                dayFlag=true;
                    ToastUtil.customShow(MainActivity.this, sign.getMes());


            }
        };

    }


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            if (Check.isLogin(MainActivity.this) && bdLocation.getAddrStr() != null) {
                pullAddresData(bdLocation.getProvince(), bdLocation.getCity(), bdLocation.getDistrict());
            }
            mLocationClient.stop();
        }
    }

    /**
     * 发送地址
     *
     * @param pro  省
     * @param city 市
     * @param area 区
     */
    protected  void pullAddresData(final String pro, final String city, final String area) {
        executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.POST_ADDRES, BaseModel.class, responseAddresListener(), errorListener(false)) {

            protected Map<String, String> getParams() {
                return new ApiParams().with("Province", pro).with("City", city).with("Area", area);
            }

        });
    }

    private Response.Listener<BaseModel> responseAddresListener() {
        return new Response.Listener<BaseModel>() {
            @Override
            public void onResponse(BaseModel addresModel) {

                //备用
            }

        };

    }



    protected void sendClientId() {

        if (Check.isLogin(this) && Check.isSendClientId(this) && !"".equals(PreferencesUtils.getValueFromSPMap(this, PreferencesUtils.Keys.CLIENTID, "", PreferencesUtils.Keys.USERINFO))) {//发送clientid,成功后 PreferencesUtils.putBooleanToSPMap(context, PreferencesUtils.Keys.IS_SEND_CLIENTID, true, PreferencesUtils.Keys.USERINFO);
            pullClientData(PreferencesUtils.getValueFromSPMap(this, PreferencesUtils.Keys.CLIENTID, "", PreferencesUtils.Keys.USERINFO));

        }

    }

    protected  void pullClientData(final String clientId) {
        executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.PUSH_CLIENT_ID, BaseModel.class, responseClientListener(), errorListener(false)) {

            protected Map<String, String> getParams() {
                return new ApiParams().with("id", clientId);
            }

        });
    }

    private Response.Listener<BaseModel> responseClientListener() {
        return new Response.Listener<BaseModel>() {
            @Override
            public void onResponse(BaseModel addresModel) {

                if (addresModel.getRet() == 0) {
                    PreferencesUtils.putBooleanToSPMap(MainActivity.this, PreferencesUtils.Keys.IS_SEND_CLIENTID, true, PreferencesUtils.Keys.USERINFO);
                }

                //备用
            }

        };

    }



    /**
     * 自定义返回键的效果
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 返回键
            exitBy2Click();
        }
        return true;
    }

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            // Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            ToastUtil.show(this, getResources()
                    .getString(R.string.back_to_exit));
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 1000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
//            MobclickAgent.onKillProcess(this);
            // finish();
            AppManager.getAppManager().finishAllActivity();
            AppManager.getAppManager().AppExit(this);
        }
    }





}
