package com.liuzhuni.lzn.core.index_new.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.BaseFragment;
import com.liuzhuni.lzn.config.Check;
import com.liuzhuni.lzn.config.TypeInt;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.buylist.BuyActivity;
import com.liuzhuni.lzn.core.html.HtmlActivity;
import com.liuzhuni.lzn.core.index.model.CountModel;
import com.liuzhuni.lzn.core.index_new.DetailActivity;
import com.liuzhuni.lzn.core.index_new.FilterActivity;
import com.liuzhuni.lzn.core.index_new.adapter.PickAdapter;
import com.liuzhuni.lzn.core.index_new.model.CampaignModel;
import com.liuzhuni.lzn.core.index_new.model.PickModel;
import com.liuzhuni.lzn.core.index_new.ui.CircleImageView;
import com.liuzhuni.lzn.core.login.LoginActivity;
import com.liuzhuni.lzn.core.login.Loginable;
import com.liuzhuni.lzn.core.login.TheIntent;
import com.liuzhuni.lzn.core.model.BaseListModel;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.core.select.SelectActivity;
import com.liuzhuni.lzn.core.siri.TextSiriActivity;
import com.liuzhuni.lzn.pinHeader.StickyLayout;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.utils.fileHelper.CommonUtil;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonBaseRequest;
import com.liuzhuni.lzn.volley.RequestManager;
import com.liuzhuni.lzn.xList.XListViewNew;
import com.melnykov.fab.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrew Lee on 2015/7/31.
 * E-mail:jieooo7@163.com
 * Date: 2015-07-31
 * Time: 12:00
 */
public class FragmentIndex extends BaseFragment implements
        StickyLayout.OnGiveUpTouchEventListener,XListViewNew.IXListViewListener,XListViewNew.HideFab {


    @ViewInject(R.id.expandablelist)
    private XListViewNew pinListView;

    @ViewInject(R.id.sticky_layout)
    private StickyLayout stickyLayout;

    @ViewInject(R.id.fab)
    private FloatingActionButton fab;




    @ViewInject(R.id.top_ll)
    private LinearLayout mTopLl;

    @ViewInject(R.id.top_ttle)
    private TextView mTopTv;


    @ViewInject(R.id.tell_me)
    private TextView mNewMessageTv;


    @ViewInject(R.id.tell_me_image)
    private CircleImageView mNewMessageIv;


    @ViewInject(R.id.message_num)
    private TextView mNewMessageNum;


    @ViewInject(R.id.buy_list)
    private TextView mBuyListNum;


    @ViewInject(R.id.the_gift)
    private NetworkImageView mGiftNv;
    @ViewInject(R.id.only_you)
    private NetworkImageView mTopicNv;
    @ViewInject(R.id.touch_me)
    private NetworkImageView mTouchNv;


    private String mGiftUrl="";
    private String mTopicUrl="";
    private String mTouchUrl="";

    private int mTopId;

    private Boolean isMore = true;//防止重复加载

    private boolean hasTop=false;



    private int backId=0;
    private int forwardId=0;


    private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm");
    private String mTime;

    private final int TEXI_SIZE = 30;

    private ImageLoader mImageLoader;
    private List<PickModel> mList = null;
    private List<PickModel> mCurrentList = null;
    private PickAdapter mAdapter;
    private boolean isRefresh = true;

    private String mTag="";


    private final int REQUEST_CODE = 2;

    public Handler mHandler = new Handler();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_index,
                container, false);

        ViewUtils.inject(this, view);

        initData();
        initUI();
        setListener();

        return view;
    }

    protected void initData(){

        mImageLoader = RequestManager.getImageLoader();
        mList = new ArrayList<PickModel>();
        mAdapter = new PickAdapter(getCustomActivity(),mList,mImageLoader);

        mTag= PreferencesUtils.getValueFromSPMap(getCustomActivity(), PreferencesUtils.Keys.TAG, "");


    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    protected void initUI() {

        if (Check.hashead(getCustomActivity())) {
            mImageLoader.get(PreferencesUtils.getValueFromSPMap(getCustomActivity(), PreferencesUtils.Keys.HEAD_URL, "", PreferencesUtils.Keys.USERINFO),mImageLoader.getImageListener(mNewMessageIv,R.drawable.my_touxiang,R.drawable.my_touxiang));
        }

        fab.hide();
        pinListView.setHideFab(this);
        pinListView.setPullLoadEnable(false);
        pinListView.setPullRefreshEnable(true);
        pinListView.setXListViewListener(this);

        pullData(0,"","back");

        Date date = new Date();
        mTime = mDateFormat.format(date);
        stickyLayout.setOnGiveUpTouchEventListener(this);
        pinListView.setAdapter(mAdapter);
        fab.attachToListView(pinListView);
        pullCampaignData();
        pullTopData();

    }


    @Override
    public void onResume() {
        super.onResume();


//        startNet();
        if (!CommonUtil.checkNetState(getCustomActivity())) {

            ToastUtil.customShow(getCustomActivity(),"当前网络不可用\n请检查您的网络设置");
        }

        if (Check.isLogin(getCustomActivity())) {
            //登陆成功
            pullMessageData();

        } else {

        }
    }

    public void hideFab() {
        fab.hide();
    }

    @Override
    public void onRefresh() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isRefresh = true;
                if (isMore) {
                    isMore = false;
                    if (mList.size() >= 1) {
                        pullData(backId, "", "back");
                    } else {
                        pullData(0, "", "back");
                    }
                }

                pinListView.stopRefresh();
            }
        }, 200);

    }

    @Override
    public void onLoadMore() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                isRefresh = false;
                if (isMore) {
                    isMore = false;
                    if (mList.size() >= 1) {
                        pullData(forwardId, "", "forward");
                    } else {
                        pullData(0, "", "back");
                    }
                }
                pinListView.stopLoadMore();
            }
        }, 200);

    }




    protected  void pullData(final int id, final String tags, final String way) {
        executeRequest(new GsonBaseRequest<BaseListModel<PickModel>>(Request.Method.POST, UrlConfig.GET_PICK, new TypeToken<BaseListModel<PickModel>>() {
        }.getType(), responseListener(), errorMoreListener()) {

            protected Map<String, String> getParams() {
                return new ApiParams().with("id", "" + id).with("tags", mTag).with("way", way);
            }

        });
    }

    public Response.ErrorListener errorMoreListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isMore = true;
//                Toast.makeText(activity,getResources().getText(R.string.error_retry), Toast.LENGTH_LONG).show();
                loadingdialog.dismiss();
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 401) {//重新登录
                        PreferencesUtils.putBooleanToSPMap(getCustomActivity(), PreferencesUtils.Keys.IS_LOGIN, false);
                        PreferencesUtils.clearSPMap(getCustomActivity(), PreferencesUtils.Keys.USERINFO);
                        Intent intent = new Intent(getCustomActivity(), LoginActivity.class);
                        startActivity(intent);
                        getCustomActivity().finish();
                    } else {
//                        ToastUtil.customShow(Base2Activity.this, getResources().getText(R.string.error_retry));
                    }
                } else {
//                    ToastUtil.customShow(Base2Activity.this, getResources().getText(R.string.bad_net));
                }
//                RequestManager.cancelAll(this);
            }
        };
    }

    private Response.Listener<BaseListModel<PickModel>> responseListener() {
        return new Response.Listener<BaseListModel<PickModel>>() {
            @Override
            public void onResponse(BaseListModel<PickModel> indexBaseListModel) {
                if(indexBaseListModel.getRet()==0){
                    isMore = true;
                    int tempBackId=indexBaseListModel.getBack();
                    int tempForwardId=indexBaseListModel.getForward();
                    if(tempBackId!=0||tempForwardId!=0){

                        if(forwardId==0||tempForwardId<forwardId){

                            forwardId=tempForwardId;
                        }
                        if(tempBackId>backId){

                            backId=tempBackId;
                        }
                    }

                    if (indexBaseListModel.getData() != null) {

                        mCurrentList = indexBaseListModel.getData();
                        if(isRefresh){

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(hasTop){
                                        mList.addAll(1, mCurrentList);
                                    }else{

                                        mList.addAll(0, mCurrentList);
                                    }
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                            pinListView.setRefreshTime(mTime);
                            Date date = new Date();
                            mTime = mDateFormat.format(date);
                        }else{


                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mList.addAll(mCurrentList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                        }





                    }else{

                        if(!isRefresh){
                            ToastUtil.show(getCustomActivity(), "没有更多了");
                        }
                    }

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {//需要在同一个线程中操作
                            if(mList.size()>5){
                                pinListView.setPullLoadEnable(true);
                            }
                        }
                    });


                    //mlist  为空时 显示错误页面
                }

            }
        };

    }


    protected  void pullTopData() {
        executeRequest(new GsonBaseRequest<BaseModel<PickModel>>(Request.Method.GET, UrlConfig.GET_TOP, new TypeToken<BaseModel<PickModel>>() {
        }.getType(), responseTopListener(), errorListener()) {

        });
    }
    protected  void pullCampaignData() {
        executeRequest(new GsonBaseRequest<BaseListModel<CampaignModel>>(Request.Method.GET, UrlConfig.CAMPAIGN, new TypeToken<BaseListModel<CampaignModel>>() {
        }.getType(), responseCamListener(), errorListener()) {

        });
    }




    public Response.Listener<BaseListModel<CampaignModel>> responseCamListener() {
        return new Response.Listener<BaseListModel<CampaignModel>>() {
            @Override
            public void onResponse(BaseListModel<CampaignModel> indexcountModel) {
                if (indexcountModel.getRet() == 0) {

                    final List<CampaignModel> camModel=indexcountModel.getData();

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(camModel.get(0).getImgUrl())) {
                                mGiftNv.setImageUrl(camModel.get(0).getImgUrl(), mImageLoader);
                                mGiftUrl = camModel.get(0).getZhuanTiUrl();
                                mGiftNv.setVisibility(View.VISIBLE);

                            }
                            if (!TextUtils.isEmpty(camModel.get(1).getImgUrl())) {

                                mTopicNv.setImageUrl(camModel.get(1).getImgUrl(), mImageLoader);
                                mTopicUrl = camModel.get(1).getZhuanTiUrl();
                                mTopicNv.setVisibility(View.VISIBLE);

                            }
                            if (!TextUtils.isEmpty(camModel.get(2).getImgUrl())) {
                                mTouchNv.setImageUrl(camModel.get(2).getImgUrl(), mImageLoader);
                                mTouchUrl = camModel.get(2).getZhuanTiUrl();
                                mTouchNv.setVisibility(View.VISIBLE);

                            }

                            stickyLayout.ModifyHeight();


                        }
                    });


                }
            }
        };

    }
    private Response.Listener<BaseModel<PickModel>> responseTopListener() {
        return new Response.Listener<BaseModel<PickModel>>() {
            @Override
            public void onResponse(BaseModel<PickModel> indexcountModel) {
                if (indexcountModel.getRet() == 0&&indexcountModel.getData()!=null) {
                    final PickModel topModel=indexcountModel.getData();
                    topModel.setIsTop(true);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mList.add(0, topModel);
                            hasTop=true;
                            mAdapter.notifyDataSetChanged();

                        }
                    });


                }
            }
        };

    }

    public Response.ErrorListener errorTopListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                LinearLayout.LayoutParams rp= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0);

                mTopLl.setLayoutParams(rp);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mTopLl.setVisibility(View.GONE);
                    }
                });

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

            }
        };
    }

    private Response.Listener<BaseModel<CountModel>> responseMessageListener() {
        return new Response.Listener<BaseModel<CountModel>>() {
            @Override
            public void onResponse(BaseModel<CountModel> indexcountModel) {
                if (indexcountModel.getRet() == 0) {

                    final CountModel model=indexcountModel.getData();
                    if(model!=null){

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {

                                if(!TextUtils.isEmpty(model.getKeyword())){
                                    mNewMessageTv.setText(model.getKeyword());
                                }
                                if(model.getPushs()>0){

                                    mNewMessageNum.setText(""+model.getPushs());
                                    mNewMessageNum.setVisibility(View.VISIBLE);
                                }else{
                                    mNewMessageNum.setVisibility(View.INVISIBLE);

                                }



                                if(model.getShoplists()>0){
                                    mBuyListNum.setText("("+model.getShoplists()+")");
                                }


                            }
                        });
                    }



                } else {

                }

            }
        };

    }


    protected void setListener() {

        pinListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

//                Toast.makeText(activity, ""+position, Toast.LENGTH_LONG).show();

                    Intent intent = new Intent();
                    intent.setClass(getCustomActivity(), DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", "" + mList.get(position-1).getId());
                    bundle.putBoolean("isSelect", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
            }


        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CODE:
                if(resultCode==getCustomActivity().RESULT_OK){
                    mList.clear();
                    mTag=data.getExtras().getString("tag");
//                    ToastUtil.customShow(this,data.getExtras().getString("tag"));
                    pullData(0,data.getExtras().getString("tag"),"back");
                }
                break;
        }
    }



    @OnClick(R.id.fab)
    public void fab(View v) {
        pinListView.setSelection(0);
        fab.hide();

    }
    @OnClick(R.id.filter_tv)
    public void filter(View v) {
        Intent intent = new Intent(getCustomActivity(), FilterActivity.class);
        startActivityForResult(intent, REQUEST_CODE);

    }
    @OnClick(R.id.the_gift)
    public void gift(View v) {
        Intent intent = new Intent();
        intent.setClass(getCustomActivity(), HtmlActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("url", TypeInt.GIFT);
        intent.putExtras(bundle);
        startActivity(intent);

    }
    @OnClick(R.id.only_you)
    public void topic(View v) {
        Intent intent = new Intent();
        intent.setClass(getCustomActivity(), HtmlActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("url", TypeInt.TOPIC);
        intent.putExtras(bundle);
        startActivity(intent);

    }
    @OnClick(R.id.touch_me)
    public void touch(View v) {
        Intent intent = new Intent();
        intent.setClass(getCustomActivity(), HtmlActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("url", TypeInt.TOUCH);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @OnClick(R.id.top_ll)
    public void top(View v) {
        Intent intent = new Intent();
        intent.setClass(getCustomActivity(), DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", "" + mTopId);
        bundle.putBoolean("isSelect", true);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @OnClick(R.id.index_new_message)
    public void message(View v) {

        //消息
        TheIntent theIntent = new TheIntent(getCustomActivity(), new Loginable() {
            @Override
            public void intent() {
                Intent intent = new Intent(getCustomActivity(), TextSiriActivity.class);
                startActivity(intent);
            }
        });
        theIntent.go();
    }

    @OnClick(R.id.buy_list)
    public void buyList(View v) {

        //购买清单
        TheIntent theIntent = new TheIntent(getCustomActivity(), new Loginable() {
            @Override
            public void intent() {
                Intent intent = new Intent(getCustomActivity(), BuyActivity.class);
                startActivity(intent);
            }
        });
        theIntent.go();

    }

    @OnClick(R.id.to_buy)
    public void wantBuy(View v) {
        //想买
        TheIntent theIntent = new TheIntent(getCustomActivity(), new Loginable() {
            @Override
            public void intent() {
                Intent intent = new Intent(getCustomActivity(), SelectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("fromIndex", true);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        theIntent.go();
    }

    @Override
    public boolean giveUpTouchEvent(MotionEvent event) {//StickyLayout中content何时放弃事件处理


//        stickyLayout.setHeaderHeight(stickyLayout.getOriginHeight(),true);
        if (pinListView.getFirstVisiblePosition() == 0){
            View view0=pinListView.getChildAt(0);
                if (view0 != null && view0.getTop() >= 0&&stickyLayout.getHeaderHeight()==0) {

                    return true;
                }
        }
        return false;
    }







}
