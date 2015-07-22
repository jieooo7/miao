package com.liuzhuni.lzn.core.index;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
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
import com.liuzhuni.lzn.base.Base2Activity;
import com.liuzhuni.lzn.config.AppManager;
import com.liuzhuni.lzn.config.Check;
import com.liuzhuni.lzn.config.MessageWhat;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.buylist.BuyActivity;
import com.liuzhuni.lzn.core.index.adapter.IndexAdapter;
import com.liuzhuni.lzn.core.index.model.CountModel;
import com.liuzhuni.lzn.core.index.model.IndexModel;
import com.liuzhuni.lzn.core.login.LoginActivity;
import com.liuzhuni.lzn.core.login.Loginable;
import com.liuzhuni.lzn.core.login.TheIntent;
import com.liuzhuni.lzn.core.model.BaseListModel;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.core.personInfo.InfoActivity;
import com.liuzhuni.lzn.core.select.SelectActivity;
import com.liuzhuni.lzn.core.siri.TextSiriActivity;
import com.liuzhuni.lzn.example.qr_codescan.MipcaActivityCapture;
import com.liuzhuni.lzn.pulltorefresh.PullToRefreshBase;
import com.liuzhuni.lzn.pulltorefresh.PullToRefreshListView;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.utils.fileHelper.CommonUtil;
import com.liuzhuni.lzn.utils.log.CommonLog;
import com.liuzhuni.lzn.utils.log.LogFactory;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonBaseRequest;
import com.liuzhuni.lzn.volley.GsonRequest;
import com.liuzhuni.lzn.volley.RequestManager;
import com.umeng.update.UmengUpdateAgent;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class IndexActivity extends Base2Activity {

    private CommonLog log = LogFactory.createLog("index");

    @ViewInject(R.id.title_right_iv)
    private ImageView mRightIv;
    @ViewInject(R.id.index_person_info)
    private ImageView mInfoIv;
    @ViewInject(R.id.index_want_buy)
    private ImageView mWantBuyIv;
    @ViewInject(R.id.title_middle)
    private TextView mMiddleTv;
    @ViewInject(R.id.index_new_message)
    private TextView mMessageTv;
    @ViewInject(R.id.index_buy_list)
    private TextView mBuyTv;
    @ViewInject(R.id.want_buy_ll)
    private LinearLayout mBuyLl;
    @ViewInject(R.id.home_list_view)
    private PullToRefreshListView mPullList;
    @ViewInject(R.id.title)
    private View mTitleView;
    @ViewInject(R.id.index_rl)
    private RelativeLayout mRel;


    private boolean dayFlag=true;

    private PopupWindow window = null;
    private int screenWidth;

    private volatile boolean mIsExit = false;
    private volatile boolean mIsNetExit = false;//退出网络请求线程

    private volatile int mCurrentId = 0;
    private volatile int mOldId = -1;

    public LocationClient mLocationClient;
    public MyLocationListener mMyLocationListener;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm");
    private String mTime;

    private final int TEXI_SIZE = 30;
    private Boolean isExit = false;// 双击退出


    private ImageLoader mImageLoader;
    private List<IndexModel> mList = null;
    private List<IndexModel> mOldList = null;
    private List<IndexModel> mCurrentList = null;
    private IndexAdapter mAdapter;
    private ListView mListView;
    private boolean isUp = true;

    private final int SCANNIN_GREQUEST_CODE = 2;


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

        setContentView(R.layout.activity_index);
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
    protected void initData() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;
        mImageLoader = RequestManager.getImageLoader();
        mList = new ArrayList<IndexModel>();
        mAdapter = new IndexAdapter(this, mList, mImageLoader);


    }

    @Override
    protected void findViewById() {
        ViewUtils.inject(this);
    }

    @Override
    protected void initUI() {

        mMiddleTv.setText(getResources().getText(R.string.the_name));
        mPullList.setPullLoadEnabled(true);
        mPullList.setScrollLoadEnabled(true);
        mListView = mPullList.getRefreshableView();
        mListView.setDivider(getResources().getDrawable(R.drawable.divide));
        mListView.setDividerHeight(1);
        mListView.setCacheColorHint(Color.TRANSPARENT);
        mListView.setSelector(getResources().getDrawable(R.drawable.trans));
        mListView.setVerticalScrollBarEnabled(true);

        mListView.setAdapter(mAdapter);


        Date date = new Date();
        mTime = mDateFormat.format(date);


        sendClientId();


    }


    @Override
    protected void onResume() {
        super.onResume();
        mIsNetExit = false;
        pullData(mCurrentId);
//        startNet();
        if (!CommonUtil.checkNetState(this)) {

            ToastUtil.customShow(this, getResources().getText(R.string.bad_net));
        }

        if (Check.isLogin(this)) {
            //登陆成功
            pullMessageData();

        } else {
            mMessageTv.setText(getResources().getText(R.string.new_message));
            mBuyTv.setText(getResources().getText(R.string.buy_list));

        }

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

    protected void showPopup() {

        View contentView = LayoutInflater.from(this).inflate(
                R.layout.popup_window, null);

        ImageView boyIv = (ImageView) contentView.findViewById(R.id.pop_boy);
        ImageView girlIv = (ImageView) contentView.findViewById(R.id.pop_girl);
        window = new PopupWindow(contentView, screenWidth - getResources().getDimensionPixelOffset(R.dimen.pop_offset), LinearLayout.LayoutParams.WRAP_CONTENT, true);
        window.setTouchable(true);
        window.setOutsideTouchable(false);
//            window.showAsDropDown(mTitleView, 0, 20);
        window.showAtLocation(mRel, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 260);
        backgroundAlpha(0.45f);
        boyIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backgroundAlpha(1.0f);
                window.dismiss();
                PreferencesUtils.putBooleanToSPMap(IndexActivity.this, PreferencesUtils.Keys.IS_FIRST, true);
                PreferencesUtils.putIntToSPMap(IndexActivity.this, PreferencesUtils.Keys.SEX, 0, PreferencesUtils.Keys.USERINFO);
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
                PreferencesUtils.putBooleanToSPMap(IndexActivity.this, PreferencesUtils.Keys.IS_FIRST, true);
                PreferencesUtils.putIntToSPMap(IndexActivity.this, PreferencesUtils.Keys.SEX, 1, PreferencesUtils.Keys.USERINFO);
            }
        });


    }

    protected void startThread() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mIsExit) {
                    if (mRel.getVisibility() == View.VISIBLE && mRel.isShown()) {
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

    protected void startNet() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!mIsNetExit) {
                    if (mCurrentId > mOldId) {
                        pullData(mCurrentId);
                    }
                    try {
                        Thread.currentThread().sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();


    }

    protected  void pullData(final int id) {
        executeRequest(new GsonBaseRequest<BaseListModel<IndexModel>>(Request.Method.GET, UrlConfig.INDEX + id, new TypeToken<BaseListModel<IndexModel>>() {
        }.getType(), responseListener(), errorListener()) {

        });
    }

    private Response.Listener<BaseListModel<IndexModel>> responseListener() {
        return new Response.Listener<BaseListModel<IndexModel>>() {
            @Override
            public void onResponse(BaseListModel<IndexModel> indexBaseListModel) {
                if (indexBaseListModel.getData() != null) {
                    mCurrentList = indexBaseListModel.getData();
                    mList.addAll(0, mCurrentList);
                    mAdapter.notifyDataSetChanged();

                }
                // 下拉加载完成
                mPullList.onPullDownRefreshComplete();

                // 上拉刷新完成
                mPullList.onPullUpRefreshComplete();
                mPullList.setLastUpdatedLabel(mTime);//设置最后刷新时间
                Date date = new Date();
                mTime = mDateFormat.format(date);
                mOldId = mCurrentId;
                if (indexBaseListModel.getL() > mCurrentId) {
                    mCurrentId = indexBaseListModel.getL();
                }
                //mlist  为空时 显示错误页面

            }
        };

    }


    protected  void pullMessageData() {
        executeRequest(new GsonBaseRequest<BaseModel<CountModel>>(Request.Method.GET, UrlConfig.GET_MESSAGE_COUNT, new TypeToken<BaseModel<CountModel>>() {
        }.getType(), responseMessageListener(), errorMesListener()) {

        });
    }

    public Response.ErrorListener errorMesListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mMessageTv.setText(getResources().getText(R.string.new_message));
                mBuyTv.setText(getResources().getText(R.string.buy_list));

            }
        };
    }

    private Response.Listener<BaseModel<CountModel>> responseMessageListener() {
        return new Response.Listener<BaseModel<CountModel>>() {
            @Override
            public void onResponse(BaseModel<CountModel> indexcountModel) {
                if (indexcountModel.getRet() == 0) {
                    SpannableString spanMessage = new SpannableString("" + indexcountModel.getData().getPushs());
                    spanMessage.setSpan(new AbsoluteSizeSpan(TEXI_SIZE, true), 0, spanMessage.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//第二个参数boolean dip，如果为true，表示前面的字体大小单位为dip，否则为像素，同上。
                    mMessageTv.setText(spanMessage);
                    mMessageTv.append(getResources().getText(R.string.new_message_enter));

                    SpannableString spanBuy = new SpannableString("" + indexcountModel.getData().getShoplists());
                    spanBuy.setSpan(new AbsoluteSizeSpan(TEXI_SIZE, true), 0, spanBuy.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mBuyTv.setText(spanBuy);
                    mBuyTv.append(getResources().getText(R.string.buy_list_enter));


                } else {
                    mMessageTv.setText(getResources().getText(R.string.new_message));
                    mBuyTv.setText(getResources().getText(R.string.buy_list));
                }

            }
        };

    }

    protected  void pullDayData() {  //签到

            executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.USER_SIGN, BaseModel.class, responseDayListener(), errorListener()) {

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
                        PreferencesUtils.putBooleanToSPMap(IndexActivity.this, PreferencesUtils.Keys.IS_LOGIN, false);
                        PreferencesUtils.clearSPMap(IndexActivity.this, PreferencesUtils.Keys.USERINFO);
                        Intent intent = new Intent(IndexActivity.this, LoginActivity.class);
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
                if (sign.getRet() == 0) {

                    ToastUtil.customShow(IndexActivity.this, sign.getMes());

                } else {
                    ToastUtil.customShow(IndexActivity.this, sign.getMes());
                }

            }
        };

    }


    @Override
    protected void onPause() {
        super.onPause();
        mIsExit = true;
        mIsNetExit = true;//停止网络线程

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient.isStarted()) {

            mLocationClient.stop();

        }
    }

    @Override
    protected void setListener() {

        mPullList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
//                isUp = true;
//                pullData(mCurrentId);
                isUp = false;
                if (mCurrentId > mOldId) {
                    pullData(mCurrentId);
                } else {
                    // 下拉加载完成
                    mPullList.onPullDownRefreshComplete();
                    mPullList.onPullDownRefreshComplete();
                    // 上拉刷新完成
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

                mPullList.onPullUpRefreshComplete();

                mPullList.onPullDownRefreshComplete();
                // 设置是否有更多的数据
                mPullList.setHasMoreData(false);

            }
        });


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
        intent.setClass(IndexActivity.this, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @OnClick(R.id.index_person_info)
    public void info(View v) {

        //个人信息
        Intent intent = new Intent(IndexActivity.this, InfoActivity.class);
        startActivity(intent);

//        showPopup();
    }

    @OnClick(R.id.index_new_message)
    public void message(View v) {

        //消息
        TheIntent theIntent = new TheIntent(this, new Loginable() {
            @Override
            public void intent() {
                Intent intent = new Intent(IndexActivity.this, TextSiriActivity.class);
                startActivity(intent);
            }
        });
        theIntent.go();
    }

    @OnClick(R.id.index_buy_list)
    public void buyList(View v) {

        //购买清单
        TheIntent theIntent = new TheIntent(this, new Loginable() {
            @Override
            public void intent() {
                Intent intent = new Intent(IndexActivity.this, BuyActivity.class);
                startActivity(intent);
            }
        });
        theIntent.go();

    }

    @OnClick(R.id.want_buy_ll)
    public void wantBuy(View v) {
        //想买
        TheIntent theIntent = new TheIntent(this, new Loginable() {
            @Override
            public void intent() {
                Intent intent = new Intent(IndexActivity.this, SelectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("fromIndex", true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        theIntent.go();
    }


    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {

            if (Check.isLogin(IndexActivity.this) && bdLocation.getAddrStr() != null) {
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
                    PreferencesUtils.putBooleanToSPMap(IndexActivity.this, PreferencesUtils.Keys.IS_SEND_CLIENTID, true, PreferencesUtils.Keys.USERINFO);
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
