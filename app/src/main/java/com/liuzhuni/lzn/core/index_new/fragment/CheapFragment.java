package com.liuzhuni.lzn.core.index_new.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class CheapFragment extends BaseFragment {


    @ViewInject(R.id.fab)
    private FloatingActionButton fab;

    @ViewInject(R.id.grid_view)
    private PullToRefreshGridView mPullGridView;


    private GridView mGridView;

    private boolean isRefresh = true;
    private Boolean isExit = false;// 双击退出
    private boolean isMore = true;


    private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm");
    private String mTime;

    private ImageLoader mImageLoader;

    private  int backId = 0;
    private  int forwardId = 0;


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
        mAdapter = new CheapAdapter(mList, getActivity(), mImageLoader);

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
        mGridView.setSelector(getResources().getDrawable(R.drawable.trans));
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

//                Intent intent = new Intent();
//                intent.setClass(getActivity(), DetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("id", "" + mList.get(position).getId());
//                bundle.putBoolean("isSelect", true);
//                intent.putExtras(bundle);
//                startActivity(intent);

                Intent intent = new Intent();
                intent.setClass(getActivity(), ToBuyActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("url", mList.get(position).getUrl());
                bundle.putString("title", "");
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });


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

                int tempBackId=indexBaseListModel.getBack();
                int tempForwardId=indexBaseListModel.getForward();
                if(tempBackId!=0||tempForwardId!=0){
                    if(forwardId==0||tempForwardId<forwardId){//forward 为小

                        forwardId=tempForwardId;
                    }
                    if(tempBackId>backId){

                        backId=tempBackId;
                    }
                }




                if (indexBaseListModel.getRet()==0&&indexBaseListModel.getData() != null) {
                    mCurrentList = indexBaseListModel.getData();
                    if (isRefresh) {
                        mList.addAll(0, mCurrentList);
                    } else {
                        mList.addAll(mCurrentList);
                    }
                    handle.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });

                    mPullGridView.onPullUpRefreshComplete();

                } else {
                    if (!isRefresh) {
                        mPullGridView.setHasMoreData(false);
                        mPullGridView.removeFooter();
//                        mPullGridView.addView();

                        final View view = LayoutInflater.from(getActivity()).inflate(
                                R.layout.no_more, null);
//                        view.getScrollY()
                        handle.post(new Runnable() {
                            @Override
                            public void run() {
                                mPullGridView.addView(view);
                            }
                        });

//                        ToastUtil.show(getActivity(), getResources().getText(R.string.no_more_error));
                    }

                }
                // 下拉加载完成
                mPullGridView.onPullDownRefreshComplete();

                // 上拉刷新完成
//                mPullGridView.onPullUpRefreshComplete();
                mPullGridView.setLastUpdatedLabel(mTime);//设置最后刷新时间
                Date date = new Date();
                mTime = mDateFormat.format(date);

            }
        };

    }


}
