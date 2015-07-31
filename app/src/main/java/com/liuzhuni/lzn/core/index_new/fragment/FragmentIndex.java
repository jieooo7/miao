package com.liuzhuni.lzn.core.index_new.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.BaseFragment;
import com.liuzhuni.lzn.config.Check;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.buylist.BuyActivity;
import com.liuzhuni.lzn.core.index.model.CountModel;
import com.liuzhuni.lzn.core.index_new.DetailActivity;
import com.liuzhuni.lzn.core.index_new.FilterActivity;
import com.liuzhuni.lzn.core.index_new.adapter.PickAdapter;
import com.liuzhuni.lzn.core.index_new.model.PickModel;
import com.liuzhuni.lzn.core.login.LoginActivity;
import com.liuzhuni.lzn.core.login.Loginable;
import com.liuzhuni.lzn.core.login.TheIntent;
import com.liuzhuni.lzn.core.model.BaseListModel;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.core.select.SelectActivity;
import com.liuzhuni.lzn.core.siri.TextSiriActivity;
import com.liuzhuni.lzn.pinHeader.PinnedSectionListView;
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
        StickyLayout.OnGiveUpTouchEventListener,XListViewNew.IXListViewListener,PinnedSectionListView.HideFab {


    @ViewInject(R.id.expandablelist)
    private PinnedSectionListView pinListView;
    @ViewInject(R.id.sticky_layout)
    private StickyLayout stickyLayout;
    @ViewInject(R.id.fab)
    private FloatingActionButton fab;


    @ViewInject(R.id.index_want_buy)
    private ImageView mWantBuyIv;

    @ViewInject(R.id.index_new_message)
    private TextView mMessageTv;

    @ViewInject(R.id.index_buy_list)
    private TextView mBuyTv;

    @ViewInject(R.id.want_buy_ll)
    private LinearLayout mBuyLl;
    @ViewInject(R.id.index_rl)
    private RelativeLayout mRel;

    private Boolean isMore = true;//防止重复加载






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
        mList.add(0,new PickModel(-1));
        mAdapter = new PickAdapter(getActivity(),mList,mImageLoader);

        mTag= PreferencesUtils.getValueFromSPMap(getActivity(), PreferencesUtils.Keys.TAG, "");


    }


    protected void initUI() {

        fab.hide();
        pinListView.setPullLoadEnable(true);
        pinListView.setPullRefreshEnable(true);
        pinListView.setXListViewListener(this);
        pinListView.setAdapter(mAdapter);
        pinListView.setHideFab(this);
        pullData(0,"","back");

        Date date = new Date();
        mTime = mDateFormat.format(date);
        stickyLayout.setOnGiveUpTouchEventListener(this);
        pinListView.setAdapter(mAdapter);
        fab.attachToListView(pinListView);


    }


    @Override
    public void onResume() {
        super.onResume();


//        startNet();
        if (!CommonUtil.checkNetState(getActivity())) {

            ToastUtil.customShow(getActivity(), getResources().getText(R.string.bad_net));
        }

        if (Check.isLogin(getActivity())) {
            //登陆成功
            pullMessageData();

        } else {
            mMessageTv.setText(getResources().getText(R.string.new_message));
            mBuyTv.setText(getResources().getText(R.string.buy_list));

        }
    }

    @Override
    public void hideFab() {
        fab.hide();
    }

    @Override
    public void onRefresh() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isRefresh=true;
                if(isMore){
                    isMore = false;
                    if(mList.size()>=2){
                        pullData(mList.get(1).getId(),"","back");
                    }else{
                        pullData(0,"","back");
                    }}

                pinListView.stopRefresh();
            }
        },200);

    }

    @Override
    public void onLoadMore() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                isRefresh=false;
                if(isMore){
                    isMore = false;
                    if(mList.size()>=2) {
                        pullData(mList.get(mList.size() - 1).getId(), "", "forward");
                    }else{
                        pullData(0,"","back");
                    }
                }
                pinListView.stopLoadMore();
            }
        },200);

    }




    protected  void pullData(final int id,final String tags,final String way) {
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
                        PreferencesUtils.putBooleanToSPMap(getActivity(), PreferencesUtils.Keys.IS_LOGIN, false);
                        PreferencesUtils.clearSPMap(getActivity(), PreferencesUtils.Keys.USERINFO);
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
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

                    if (indexBaseListModel.getData() != null) {

                        mCurrentList = indexBaseListModel.getData();
                        if(isRefresh){
                            mList.addAll(1, mCurrentList);
                            pinListView.setRefreshTime(mTime);
                            Date date = new Date();
                            mTime = mDateFormat.format(date);
                        }else{

                            mList.addAll(mCurrentList);
                        }


                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                            }
                        });


                    }else{

                        if(!isRefresh){
                            ToastUtil.show(getActivity(), getResources().getText(R.string.no_more_error));
                        }
                    }

                    //mlist  为空时 显示错误页面
                }

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


    protected void setListener() {

        pinListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

//                Toast.makeText(activity, ""+position, Toast.LENGTH_LONG).show();

                if (position != 1) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), DetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", "" + mList.get(position-1).getId());
                    bundle.putBoolean("isSelect", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), FilterActivity.class);
                    startActivityForResult(intent, REQUEST_CODE);
                }

            }


        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case REQUEST_CODE:
                if(resultCode==getActivity().RESULT_OK){
                    mList.clear();
                    mList.add(0,new PickModel(-1));
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

    @OnClick(R.id.index_new_message)
    public void message(View v) {

        //消息
        TheIntent theIntent = new TheIntent(getActivity(), new Loginable() {
            @Override
            public void intent() {
                Intent intent = new Intent(getActivity(), TextSiriActivity.class);
                startActivity(intent);
            }
        });
        theIntent.go();
    }

    @OnClick(R.id.index_buy_list)
    public void buyList(View v) {

        //购买清单
        TheIntent theIntent = new TheIntent(getActivity(), new Loginable() {
            @Override
            public void intent() {
                Intent intent = new Intent(getActivity(), BuyActivity.class);
                startActivity(intent);
            }
        });
        theIntent.go();

    }

    @OnClick(R.id.want_buy_ll)
    public void wantBuy(View v) {
        //想买
        TheIntent theIntent = new TheIntent(getActivity(), new Loginable() {
            @Override
            public void intent() {
                Intent intent = new Intent(getActivity(), SelectActivity.class);
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


        if (pinListView.getFirstVisiblePosition() == 0){
            View view0=pinListView.getChildAt(0);
            int height=view0.getMeasuredHeight();
            if(mList.size()>1){

                View view = pinListView.getChildAt(1);
                if (view != null && view.getTop() >= height&&stickyLayout.getHeaderHeight()==0) {
                    return true;
                }
            }
        }
        return false;
    }







}
