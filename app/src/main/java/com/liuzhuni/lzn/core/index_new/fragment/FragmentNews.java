package com.liuzhuni.lzn.core.index_new.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.liuzhuni.lzn.core.index_new.DetailActivity;
import com.liuzhuni.lzn.core.index_new.adapter.AllNewsAdapter;
import com.liuzhuni.lzn.core.index_new.model.NewsModel;
import com.liuzhuni.lzn.core.login.LoginActivity;
import com.liuzhuni.lzn.core.model.BaseListModel;
import com.liuzhuni.lzn.pulltorefresh.PullToRefreshBase;
import com.liuzhuni.lzn.pulltorefresh.PullToRefreshGridView;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonBaseRequest;
import com.liuzhuni.lzn.volley.RequestManager;
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
public class FragmentNews extends BaseFragment {


    @ViewInject(R.id.fab)
    private FloatingActionButton fab;

    @ViewInject(R.id.grid_view)
    private PullToRefreshGridView mPullGridView;


    private GridView mGridView;

    private boolean isRefresh=true;
    private Boolean isExit = false;// 双击退出
    private boolean isMore = true;





    private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm");
    private String mTime;

    private ImageLoader mImageLoader;

    private int backId=0;
    private int forwardId=0;


    private List<NewsModel> mList = null;
    private List<NewsModel> mCurrentList = null;
    private AllNewsAdapter mAdapter;


    private Handler handle=new Handler();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news,
                container, false);

        ViewUtils.inject(this, view);

        initData();
        initUI();
        setListener();

        return view;
    }

    protected void initData() {

        mImageLoader = RequestManager.getImageLoader();

        mList = new ArrayList<NewsModel>();
        mAdapter = new AllNewsAdapter(mList, getCustomActivity(), mImageLoader);

    }

    protected void initUI() {

        Date date = new Date();
        mTime = mDateFormat.format(date);

        mPullGridView.setPullLoadEnabled(true);
        mPullGridView.setScrollLoadEnabled(true);
        mGridView= mPullGridView.getRefreshableView();
        mGridView.setNumColumns(2);
        mGridView.setHorizontalSpacing(10);
        mGridView.setVerticalSpacing(8);
        mGridView.setCacheColorHint(Color.TRANSPARENT);
        mGridView.setSelector(getCustomActivity().getResources().getDrawable(R.drawable.trans));
        mGridView.setAdapter(mAdapter);
        pullData(0,"back");
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
                if(!mList.isEmpty()){
                    pullData(backId,"back");
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
                if(isMore){
                    isMore = false;
                    if(!mList.isEmpty()){
                        pullData(forwardId,"forward");
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

                Intent intent = new Intent();
                intent.setClass(getCustomActivity(), DetailActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id",""+mList.get(position).getId());
                bundle.putBoolean("isSelect",false);
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


    protected  void pullData(final int id,final String way) {
        executeRequest(new GsonBaseRequest<BaseListModel<NewsModel>>(Request.Method.POST, UrlConfig.GET_NEWS, new TypeToken<BaseListModel<NewsModel>>() {
        }.getType(), responseListener(), errorMoreListener()) {

            protected Map<String, String> getParams() {
                return new ApiParams().with("id", ""+id).with("way",way);
            }


        });
    }


    private Response.Listener<BaseListModel<NewsModel>> responseListener() {
        return new Response.Listener<BaseListModel<NewsModel>>() {
            @Override
            public void onResponse(BaseListModel<NewsModel> indexBaseListModel) {
                loadingdialog.dismiss();
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

                        handle.post(new Runnable() {
                            @Override
                            public void run() {
                                mList.addAll(0, mCurrentList);
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }else{

                        handle.post(new Runnable() {
                            @Override
                            public void run() {
                                mList.addAll(mCurrentList);
                                mAdapter.notifyDataSetChanged();
                                mGridView.smoothScrollBy(50,200);

                            }
                        });
                    }



                }else{
                    if(!isRefresh){
                        mPullGridView.setHasMoreData(false);
                        ToastUtil.show(getCustomActivity(), "没有更多了");
                    }

                }
                mPullGridView.setLastUpdatedLabel(mTime);//设置最后刷新时间
                Date date = new Date();
                mTime = mDateFormat.format(date);

            }
        };

    }




}
