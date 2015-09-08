package com.liuzhuni.lzn.core.personInfo;

import android.os.Bundle;
import android.os.Handler;
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
import com.liuzhuni.lzn.core.model.BaseListModel;
import com.liuzhuni.lzn.core.personInfo.adapter.CouponAdapter;
import com.liuzhuni.lzn.core.personInfo.model.CouponModel;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonBaseRequest;
import com.liuzhuni.lzn.xList.XListViewNew;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CouponActivity extends Base2Activity implements XListViewNew.IXListViewListener{


    @ViewInject(R.id.title_middle)
    private TextView mMiddleTv;

    @ViewInject(R.id.title_right)
    private TextView mRightTv;

    @ViewInject(R.id.no_coupon)
    private TextView mNoContentTv;


    @ViewInject(R.id.coupon_list)
    private XListViewNew mListView;





    public Handler mHandler = new Handler();

    private int backId=0;
    private int forwardId=0;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm");
    private String mTime;

    private CouponAdapter mAdapter;

    private List<CouponModel> mList = null;
    private List<CouponModel> mCurrentList = null;

    private boolean isRefresh = true;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);


        initData();
        findViewById();
        initUI();
        setListener();
    }

    @Override
    protected void initData() {
        mList = new ArrayList<CouponModel>();
        mAdapter = new CouponAdapter(this,mList);

    }

    @Override
    protected void findViewById() {

        ViewUtils.inject(this);
    }

    @Override
    protected void initUI() {
        mRightTv.setVisibility(View.GONE);
        mMiddleTv.setText(getText(R.string.coupon_title));


        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);

        mListView.setXListViewListener(this);

        mListView.setAdapter(mAdapter);

        Date date = new Date();
        mTime = mDateFormat.format(date);

        showLoadingDialog();
        pullData(0, "back");

    }

    @Override
    protected void setListener() {

    }





    @OnClick(R.id.title_left)
    public void back(View v) {

        finish();
    }


    @Override
    public void onRefresh() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isRefresh = true;
                if (mList.size() >= 1) {
                    pullData(backId, "back");
                } else {
                    pullData(0, "back");
                }

                mListView.stopRefresh();
            }
        }, 200);

    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                isRefresh = false;
                    if (mList.size() >= 1) {
                        pullData(forwardId, "forward");
                    } else {
                        pullData(0, "back");
                    }
                mListView.stopLoadMore();
            }
        }, 200);

    }


    protected  void pullData(final int id, final String way) {
        executeRequest(new GsonBaseRequest<BaseListModel<CouponModel>>(Request.Method.POST, UrlConfig.GET_COUPON, new TypeToken<BaseListModel<CouponModel>>() {
        }.getType(), responseListener(), errorListener()) {

            protected Map<String, String> getParams() {
                return new ApiParams().with("id", "" + id).with("way", way);
            }

        });
    }


    private Response.Listener<BaseListModel<CouponModel>> responseListener() {
        return new Response.Listener<BaseListModel<CouponModel>>() {
            @Override
            public void onResponse(BaseListModel<CouponModel> indexBaseListModel) {
                DismissDialog();
                if(indexBaseListModel.getRet()==0){
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

                                    mList.addAll(0, mCurrentList);
                                    mAdapter.notifyDataSetChanged();
                                }
                            });
                            mListView.setRefreshTime(mTime);
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

                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {//需要在同一个线程中操作
                                if(mList.size()>5){
                                    mListView.setPullLoadEnable(true);
                                }
                            }
                        });



                    }else{

                        if(!isRefresh){
                            ToastUtil.show(CouponActivity.this, "没有更多了");
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {//需要在同一个线程中操作
                                    if (mList.size() > 5) {
                                        mListView.setPullLoadEnable(false);
                                    }
                                }
                            });
                        }
                    }


                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {//需要在同一个线程中操作
                            if (mList.size() <1) {
                                mNoContentTv.setVisibility(View.VISIBLE);
                            }else{
                                mNoContentTv.setVisibility(View.GONE);
                            }
                        }
                    });

                    //mlist  为空时 显示错误页面
                }

            }
        };

    }






}
