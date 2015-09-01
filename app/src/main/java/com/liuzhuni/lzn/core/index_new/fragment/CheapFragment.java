package com.liuzhuni.lzn.core.index_new.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.trade.ItemService;
import com.alibaba.sdk.android.trade.callback.TradeProcessCallback;
import com.alibaba.sdk.android.trade.model.TaokeParams;
import com.alibaba.sdk.android.trade.model.TradeResult;
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
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.goods.ToBuyActivity;
import com.liuzhuni.lzn.core.index_new.adapter.CheapAdapter;
import com.liuzhuni.lzn.core.index_new.model.CheapModel;
import com.liuzhuni.lzn.core.login.LoginActivity;
import com.liuzhuni.lzn.core.model.BaseListModel;
import com.liuzhuni.lzn.pulltorefresh.PullToRefreshBase;
import com.liuzhuni.lzn.pulltorefresh.PullToRefreshGridView;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonBaseRequest;
import com.liuzhuni.lzn.volley.RequestManager;
import com.melnykov.fab.FloatingActionButton;
import com.taobao.tae.sdk.webview.TaeWebViewUiSettings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class CheapFragment extends BaseFragment {


    @ViewInject(R.id.fab)
    private FloatingActionButton fab;


//    @ViewInject(R.id.no_more_cheap)
//    private TextView lastTv;

    @ViewInject(R.id.grid_view)
    private PullToRefreshGridView mPullGridView;


    private GridView mGridView;

    private boolean isRefresh = true;
    private Boolean isExit = false;// 双击退出
    private boolean isMore = true;


    private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm");
    private String mTime;

    private ImageLoader mImageLoader;

    private int backId = 0;
    private int forwardId = 0;


    private List<CheapModel> mList = null;
    private List<CheapModel> mCurrentList = null;
    private CheapAdapter mAdapter;


    private Handler handle = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cheap,
                container, false);

        ViewUtils.inject(this, view);

        initData();
        initUI();
        setListener();

        return view;
    }


    protected void initData() {

        mImageLoader = RequestManager.getImageLoader();

        mList = new ArrayList<CheapModel>();
        mAdapter = new CheapAdapter(mList, getCustomActivity(), mImageLoader);

    }

    protected void initUI() {

        Date date = new Date();
        mTime = mDateFormat.format(date);

        mPullGridView.setPullLoadEnabled(true);
        mPullGridView.setScrollLoadEnabled(true);
        mGridView = mPullGridView.getRefreshableView();
        mGridView.setNumColumns(2);
        mGridView.setHorizontalSpacing(10);
        mGridView.setVerticalSpacing(8);
        mGridView.setCacheColorHint(Color.TRANSPARENT);
        mGridView.setSelector(getCustomActivity().getResources().getDrawable(R.drawable.trans));
        mGridView.setAdapter(mAdapter);
        pullData(0, "back");
        loadingdialog.show();
        fab.hide();
        fab.attachToListView(mGridView);

    }


    protected void setListener() {

        mPullGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
//                isUp = true;
//                pullData(mCurrentId);
                isRefresh = true;
                if (!mList.isEmpty()) {
                    pullData(backId, "back");
                }

                handle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullGridView.onPullUpRefreshComplete();
//
                        mPullGridView.onPullDownRefreshComplete();
                    }
                },300);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                isRefresh = false;
                if (isMore) {
                    isMore = false;
                    if (!mList.isEmpty()) {
                        pullData(forwardId, "forward");
                    }
                }
                handle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullGridView.onPullUpRefreshComplete();
//
                        mPullGridView.onPullDownRefreshComplete();
                    }
                },300);
//                mPullGridView.onPullUpRefreshComplete();
//
//                mPullGridView.onPullDownRefreshComplete();
//                // 设置是否有更多的数据
//                mPullGridView.setHasMoreData(false);

            }
        });


        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String tId = mList.get(position).getTbid();
                int type = mList.get(position).getMallid();


                if (TextUtils.isEmpty(tId)) {
                    Intent intent = new Intent();
                    intent.setClass(getCustomActivity(), ToBuyActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", mList.get(position).getUrl());
                    bundle.putString("title", " ");
                    intent.putExtras(bundle);
                    startActivity(intent);

                } else {

                    showTaokeItemDetail(tId, type);
                }


//                Intent intent = new Intent();
//                intent.setClass(getActivity(), ToBuyActivity.class);
//                Bundle bundle=new Bundle();
//                bundle.putString("url", mList.get(position).getUrl());
//                bundle.putString("title", " ");
//                intent.putExtras(bundle);
//                startActivity(intent);

            }
        });


    }


    public void showTaokeItemDetail(String id, int type) {
        TaeWebViewUiSettings taeWebViewUiSettings = new TaeWebViewUiSettings();
        TaokeParams taokeParams = new TaokeParams();
        taokeParams.pid = "40490058";
        taokeParams.unionId = "null";
        ItemService itemService = AlibabaSDK.getService(ItemService.class);
        itemService.showTaokeItemDetailByOpenItemId(getCustomActivity(), new TradeProcessCallback() {

            @Override
            public void onPaySuccess(TradeResult tradeResult) {

            }

            @Override
            public void onFailure(int code, String msg) {
            }
        }, taeWebViewUiSettings, id, type, null, taokeParams);//1：淘宝商品;2：天猫商品。
    }


    @OnClick(R.id.fab)
    public void fab(View v) {
        fab.hide(true);
        mGridView.setSelection(0);

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


    protected void pullData(final int id, final String way) {
        executeRequest(new GsonBaseRequest<BaseListModel<CheapModel>>(Request.Method.POST, UrlConfig.GET_CHEAP, new TypeToken<BaseListModel<CheapModel>>() {
        }.getType(), responseListener(), errorMoreListener()) {

            protected Map<String, String> getParams() {
                return new ApiParams().with("id", "" + id).with("way", way);
            }

        });
    }


    private Response.Listener<BaseListModel<CheapModel>> responseListener() {
        return new Response.Listener<BaseListModel<CheapModel>>() {
            @Override
            public void onResponse(BaseListModel<CheapModel> indexBaseListModel) {

                loadingdialog.dismiss();
                isMore = true;

                int tempBackId = indexBaseListModel.getBack();
                int tempForwardId = indexBaseListModel.getForward();
                if (tempBackId != 0 || tempForwardId != 0) {
                    if (forwardId == 0 || tempForwardId < forwardId) {//forward 为小

                        forwardId = tempForwardId;
                    }
                    if (tempBackId > backId) {

                        backId = tempBackId;
                    }
                }


                if (indexBaseListModel.getRet() == 0 && indexBaseListModel.getData() != null) {
                    mCurrentList = indexBaseListModel.getData();
                    if (isRefresh) {

                        handle.post(new Runnable() {
                            @Override
                            public void run() {
                                mList.addAll(0, mCurrentList);
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {

                        handle.post(new Runnable() {
                            @Override
                            public void run() {

                                mList.addAll(mCurrentList);
                                mAdapter.notifyDataSetChanged();
                                mGridView.smoothScrollBy(50,200);
                            }
                        });
                    }


//                    mPullGridView.onPullUpRefreshComplete();

                } else {//没有更多数据时
                    if (!isRefresh) {
                        mPullGridView.setHasMoreData(false);

                        final View view = LayoutInflater.from(getCustomActivity()).inflate(
                                R.layout.no_more, null);
////                        view.getScrollY()
                        handle.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mPullGridView.addNewView(view);
                            }
                        },1000);

//                        ToastUtil.show(getActivity(), getResources().getText(R.string.no_more_error));
                    }

                }
                // 下拉加载完成
//                mPullGridView.onPullDownRefreshComplete();

                // 上拉刷新完成
//                mPullGridView.onPullUpRefreshComplete();
                mPullGridView.setLastUpdatedLabel(mTime);//设置最后刷新时间
                Date date = new Date();
                mTime = mDateFormat.format(date);

            }
        };

    }


}
