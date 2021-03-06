package com.liuzhuni.lzn.core.index_new;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.reflect.TypeToken;
import com.igexin.sdk.PushManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.BaseFragActivity;
import com.liuzhuni.lzn.config.AppManager;
import com.liuzhuni.lzn.config.Check;
import com.liuzhuni.lzn.config.MessageWhat;
import com.liuzhuni.lzn.config.TypeInt;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.html.HtmlActivity;
import com.liuzhuni.lzn.core.index_new.fragment.CheapFragment;
import com.liuzhuni.lzn.core.index_new.fragment.FragmentIndex;
import com.liuzhuni.lzn.core.index_new.fragment.FragmentInfo;
import com.liuzhuni.lzn.core.index_new.fragment.FragmentNews;
import com.liuzhuni.lzn.core.index_new.model.ProfileModel;
import com.liuzhuni.lzn.core.login.LoginActivity;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.core.search.SearchActivity;
import com.liuzhuni.lzn.core.service.CacheService;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonBaseRequest;
import com.liuzhuni.lzn.volley.GsonRequest;
import com.umeng.update.UmengUpdateAgent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseFragActivity implements Shareable {



    @ViewInject(R.id.title_middle)
    private TextView mMiddleTv;
    @ViewInject(R.id.title_left)
    private ImageView mLeftIv;


    @ViewInject(R.id.right_title_rl)
    private RelativeLayout mLRightRl;


    @ViewInject(R.id.index_pick)
    private TextView mPickBt;

    @ViewInject(R.id.index_people_say)
    private TextView mNewsBt;

    @ViewInject(R.id.index_person_info)
    private TextView minfoBt;

    @ViewInject(R.id.index_cheap)
    private TextView mCheapBt;

    @ViewInject(R.id.container_fragment)
    private FrameLayout mFrame;

    @ViewInject(R.id.title_all)
    private RelativeLayout relAll;

    @ViewInject(R.id.red_dot)
    private ImageView redDot;


    @ViewInject(R.id.red_title_dot)
    private ImageView redTitleDot;

    @ViewInject(R.id.id_sign)
    private TextView signTv;

    @ViewInject(R.id.make_cent)
    private TextView makeCentTv;


    private boolean dayFlag=true;//签到开关

    private PopupWindow window = null;
    private Boolean isExit = false;// 双击退出
    private volatile boolean mIsExit = false;

    private int mState=1;

    private SignDayable mSingDay;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd");





    private FragmentTransaction mTransaction;
    private int screenWidth;

    private FragmentIndex mFragIndex;
    private FragmentInfo mFragInfo;
    private FragmentNews mFragNews;
    private CheapFragment mFragCheap;

    public LocationClient mLocationClient=null;
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

        if(!Check.isNotFirstLoc(this)){
            PreferencesUtils.putBooleanToSPMap(MainActivity.this, PreferencesUtils.Keys.IS_LOC, true);
            mLocationClient = new LocationClient(this.getApplicationContext());
            mMyLocationListener = new MyLocationListener();
            mLocationClient.registerLocationListener(mMyLocationListener);
            initLoc();
        }

        Intent intent = new Intent(this,CacheService.class);
        startService(intent);
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
        mFragCheap=new CheapFragment();

    }


    private void showSign(){
        redTitleDot.setVisibility(View.VISIBLE);

        signTv.setVisibility(View.VISIBLE);

       makeCentTv.setVisibility(View.GONE);


    }


    private void showMakeSign(){

        redTitleDot.setVisibility(View.GONE);

        signTv.setVisibility(View.GONE);

        makeCentTv.setVisibility(View.VISIBLE);

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
        mLeftIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_search));

        sendClientId();

        Date date = new Date();
        String time = mDateFormat.format(date);
        String oldTime=PreferencesUtils.getValueFromSPMap(this, PreferencesUtils.Keys.TODAY,"1",PreferencesUtils.Keys.USERINFO);

        if(!oldTime.equals(time)){
            showSign();
        }else{
            showMakeSign();

        }


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
        if (mLocationClient!=null&&mLocationClient.isStarted()) {

            mLocationClient.stop();

        }
    }



    @OnClick(R.id.id_sign)
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

    @OnClick(R.id.make_cent)
    public void makeCent(View v) {


        //签到
        if (!Check.isLogin(this)) {//没有登录
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else {

            //赚积分
            Intent intent = new Intent(this, HtmlActivity.class);
            Bundle bundle=new Bundle();
            bundle.putInt("url", TypeInt.MAKE_CENT);
            intent.putExtras(bundle);


            startActivity(intent);

        }

    }

    @OnClick(R.id.title_left)
    public void qrCode(View v) {

        //签到 现在改为搜索
        //扫描条码
            Intent intent = new Intent();
            intent.setClass(this, SearchActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
    }


    @OnClick(R.id.index_pick)
    public void pick(View v) {

        if(mState!=1){
            mState=1;

            relAll.setVisibility(View.VISIBLE);

            mMiddleTv.setText(getText(R.string.the_name));
            mLeftIv.setVisibility(View.VISIBLE);
            mLeftIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_search));
            mLRightRl.setVisibility(View.VISIBLE);



            Drawable mDrawable1 = getResources().getDrawable(R.drawable.ic_home_s);
            Drawable mDrawable2 = getResources().getDrawable(R.drawable.ic_publish_n);
            Drawable mDrawable3 = getResources().getDrawable(R.drawable.ic_my_n);
            mDrawable1.setBounds(0,0,mDrawable1.getMinimumWidth(),mDrawable1.getMinimumHeight());
            mDrawable2.setBounds(0,0,mDrawable2.getMinimumWidth(),mDrawable2.getMinimumHeight());
            mDrawable3.setBounds(0,0,mDrawable3.getMinimumWidth(),mDrawable3.getMinimumHeight());
            Drawable mDrawable4 = getResources().getDrawable(R.drawable.ic_baicai_n);
            mDrawable4.setBounds(0,0,mDrawable4.getMinimumWidth(),mDrawable4.getMinimumHeight());

            mPickBt.setCompoundDrawables(null,mDrawable1,null,null);
            mPickBt.setTextColor(getResources().getColor(R.color.red));
            mNewsBt.setCompoundDrawables(null,mDrawable2,null,null);
            mNewsBt.setTextColor(getResources().getColor(R.color.gray_text_color));
            minfoBt.setCompoundDrawables(null,mDrawable3,null,null);
            minfoBt.setTextColor(getResources().getColor(R.color.gray_text_color));
            mCheapBt.setCompoundDrawables(null,mDrawable4,null,null);
            mCheapBt.setTextColor(getResources().getColor(R.color.gray_text_color));


            mTransaction=getSupportFragmentManager().beginTransaction();
            if(!mFragIndex.isAdded()){
                mTransaction.add(R.id.container_fragment,mFragIndex);
            }
            mTransaction.show(mFragIndex);
            mTransaction.hide(mFragInfo);
            mTransaction.hide(mFragNews);
            mTransaction.hide(mFragCheap);
            mTransaction.addToBackStack(null);
            mTransaction.commit();
        }

    }


    @OnClick(R.id.index_people_say)
    public void news(View v) {

        if(mState!=2){
            mState=2;

            relAll.setVisibility(View.VISIBLE);

            mMiddleTv.setText(getText(R.string.all_news));
            mLeftIv.setVisibility(View.GONE);
            mLRightRl.setVisibility(View.GONE);


            Drawable mDrawable1 = getResources().getDrawable(R.drawable.ic_home_n);
            Drawable mDrawable2 = getResources().getDrawable(R.drawable.ic_publish_s);
            Drawable mDrawable3 = getResources().getDrawable(R.drawable.ic_my_n);
            mDrawable1.setBounds(0,0,mDrawable1.getMinimumWidth(),mDrawable1.getMinimumHeight());
            mDrawable2.setBounds(0,0,mDrawable2.getMinimumWidth(),mDrawable2.getMinimumHeight());
            mDrawable3.setBounds(0,0,mDrawable3.getMinimumWidth(),mDrawable3.getMinimumHeight());
            Drawable mDrawable4 = getResources().getDrawable(R.drawable.ic_baicai_n);
            mDrawable4.setBounds(0,0,mDrawable4.getMinimumWidth(),mDrawable4.getMinimumHeight());

            mPickBt.setCompoundDrawables(null,mDrawable1,null,null);
            mPickBt.setTextColor(getResources().getColor(R.color.gray_text_color));
            mNewsBt.setCompoundDrawables(null,mDrawable2,null,null);
            mNewsBt.setTextColor(getResources().getColor(R.color.red));
            minfoBt.setCompoundDrawables(null,mDrawable3,null,null);
            minfoBt.setTextColor(getResources().getColor(R.color.gray_text_color));
            mCheapBt.setCompoundDrawables(null,mDrawable4,null,null);
            mCheapBt.setTextColor(getResources().getColor(R.color.gray_text_color));

            mTransaction=getSupportFragmentManager().beginTransaction();
            if(!mFragNews.isAdded()){
                mTransaction.add(R.id.container_fragment,mFragNews);
            }
            mTransaction.show(mFragNews);
            mTransaction.hide(mFragInfo);
            mTransaction.hide(mFragIndex);
            mTransaction.hide(mFragCheap);
            mTransaction.addToBackStack(null);
            mTransaction.commit();


        }



    }


    @OnClick(R.id.index_person_info)
    public void info(View v) {

        if(mState!=3){
            mState=3;

            relAll.setVisibility(View.GONE);

//            mMiddleTv.setText(getText(R.string.me));
//            mLeftIv.setVisibility(View.GONE);
//            mRightIv.setVisibility(View.VISIBLE);
//            mRightIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_qiandao));


            Drawable mDrawable1 = getResources().getDrawable(R.drawable.ic_home_n);
            Drawable mDrawable2 = getResources().getDrawable(R.drawable.ic_publish_n);
            Drawable mDrawable3 = getResources().getDrawable(R.drawable.ic_my_s);
            Drawable mDrawable4 = getResources().getDrawable(R.drawable.ic_baicai_n);
            mDrawable4.setBounds(0,0,mDrawable4.getMinimumWidth(),mDrawable4.getMinimumHeight());
            mDrawable1.setBounds(0,0,mDrawable1.getMinimumWidth(),mDrawable1.getMinimumHeight());
            mDrawable2.setBounds(0,0,mDrawable2.getMinimumWidth(),mDrawable2.getMinimumHeight());
            mDrawable3.setBounds(0,0,mDrawable3.getMinimumWidth(),mDrawable3.getMinimumHeight());


            mPickBt.setCompoundDrawables(null,mDrawable1,null,null);
            mPickBt.setTextColor(getResources().getColor(R.color.gray_text_color));
            mNewsBt.setCompoundDrawables(null,mDrawable2,null,null);
            mNewsBt.setTextColor(getResources().getColor(R.color.gray_text_color));
            minfoBt.setCompoundDrawables(null,mDrawable3,null,null);
            minfoBt.setTextColor(getResources().getColor(R.color.red));
            mCheapBt.setCompoundDrawables(null,mDrawable4,null,null);
            mCheapBt.setTextColor(getResources().getColor(R.color.gray_text_color));




            mTransaction=getSupportFragmentManager().beginTransaction();

            if(!mFragInfo.isAdded()){
                mTransaction.add(R.id.container_fragment,mFragInfo);
            }
            mTransaction.show(mFragInfo);
            mTransaction.hide(mFragNews);
            mTransaction.hide(mFragIndex);
            mTransaction.hide(mFragCheap);
            mTransaction.addToBackStack(null);
            mTransaction.commit();

        }

    }
    @OnClick(R.id.index_cheap)
    public void cheap(View v) {//极速白菜

        if(mState!=4){
            mState=4;


            relAll.setVisibility(View.VISIBLE);
            mMiddleTv.setText(getText(R.string.cheap_ch));
            mLeftIv.setVisibility(View.GONE);
            mLRightRl.setVisibility(View.GONE);


            Drawable mDrawable1 = getResources().getDrawable(R.drawable.ic_home_n);
            Drawable mDrawable2 = getResources().getDrawable(R.drawable.ic_publish_n);
            Drawable mDrawable3 = getResources().getDrawable(R.drawable.ic_my_n);
            Drawable mDrawable4 = getResources().getDrawable(R.drawable.ic_baicai_s);
            mDrawable1.setBounds(0,0,mDrawable1.getMinimumWidth(),mDrawable1.getMinimumHeight());
            mDrawable2.setBounds(0,0,mDrawable2.getMinimumWidth(),mDrawable2.getMinimumHeight());
            mDrawable3.setBounds(0,0,mDrawable3.getMinimumWidth(),mDrawable3.getMinimumHeight());
            mDrawable4.setBounds(0,0,mDrawable4.getMinimumWidth(),mDrawable4.getMinimumHeight());

            mPickBt.setCompoundDrawables(null,mDrawable1,null,null);
            mPickBt.setTextColor(getResources().getColor(R.color.gray_text_color));
            mNewsBt.setCompoundDrawables(null,mDrawable2,null,null);
            mNewsBt.setTextColor(getResources().getColor(R.color.gray_text_color));
            minfoBt.setCompoundDrawables(null,mDrawable3,null,null);
            minfoBt.setTextColor(getResources().getColor(R.color.gray_text_color));
            mCheapBt.setCompoundDrawables(null,mDrawable4,null,null);
            mCheapBt.setTextColor(getResources().getColor(R.color.red));

            mTransaction=getSupportFragmentManager().beginTransaction();


            if(!mFragCheap.isAdded()){
                mTransaction.add(R.id.container_fragment,mFragCheap);
            }
            mTransaction.show(mFragCheap);
            mTransaction.hide(mFragInfo);
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
                if(sign.getRet()==0){//奖励积分时候

                    if(mSingDay!=null&&mState==3){
                        mSingDay.sign();
                    }else{
                        pullProfileData();
                    }
                }
                showMakeSign();
                Date date = new Date();
                String time = mDateFormat.format(date);
                PreferencesUtils.putValueToSPMap(MainActivity.this, PreferencesUtils.Keys.TODAY, time, PreferencesUtils.Keys.USERINFO);

                Intent intent = new Intent(MainActivity.this, HtmlActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("url", TypeInt.SIGN);
                intent.putExtras(bundle);


                startActivity(intent);



            }
        };

    }

    protected synchronized void pullProfileData() {

        executeRequest(new GsonBaseRequest<BaseModel<ProfileModel>>(Request.Method.GET, UrlConfig.GET_PROFILE, new TypeToken<BaseModel<ProfileModel>>() {
        }.getType(), responseProfileListener(), errorListener()) {


        });
    }

    private Response.Listener<BaseModel<ProfileModel>> responseProfileListener() {
        return new Response.Listener<BaseModel<ProfileModel>>() {
            @Override
            public void onResponse(BaseModel<ProfileModel> shareModel) {
                if (shareModel.getRet() == 0&&shareModel.getData()!=null) {

                     ProfileModel profile=shareModel.getData();
                            PreferencesUtils.putValueToSPMap(MainActivity.this, PreferencesUtils.Keys.LEVEL,"Lv."+profile.getGrade(), PreferencesUtils.Keys.USERINFO);
                            PreferencesUtils.putValueToSPMap(MainActivity.this, PreferencesUtils.Keys.POINTS,"积分 "+profile.getJifen()+" / 金币 "+profile.getLzb(), PreferencesUtils.Keys.USERINFO);
                            PreferencesUtils.putValueToSPMap(MainActivity.this, PreferencesUtils.Keys.SIGN_DAYS,profile.getSign(), PreferencesUtils.Keys.USERINFO);

                }

            }
        };

    }


    @Override
    public void onAttachFragment(Fragment fragment) {

        try {
            if(fragment instanceof SignDayable){
                mSingDay = (SignDayable)fragment;
            }
        } catch (Exception e) {
            throw new ClassCastException(this.toString() + " must implementOnMainListener");
        }
        super.onAttachFragment(fragment);
    }

    @Override
    public void shared() {
        redDot.setVisibility(View.VISIBLE);
    }


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            if (Check.isLogin(MainActivity.this)&& !TextUtils.isEmpty(bdLocation.getCity())) {
                pullAddresData(bdLocation.getProvince(), bdLocation.getCity(), bdLocation.getDistrict());
                PreferencesUtils.putValueToSPMap(MainActivity.this, PreferencesUtils.Keys.CITY, bdLocation.getCity(), PreferencesUtils.Keys.USERINFO);
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
