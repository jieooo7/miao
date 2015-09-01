package com.liuzhuni.lzn.core.search.fragment;

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
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.index_new.DetailActivity;
import com.liuzhuni.lzn.core.index_new.adapter.AllNewsAdapter;
import com.liuzhuni.lzn.core.index_new.model.NewsModel;
import com.liuzhuni.lzn.core.login.LoginActivity;
import com.liuzhuni.lzn.core.model.BaseListModel;
import com.liuzhuni.lzn.core.search.Searchable;
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
 * Created by Andrew Lee on 2015/8/15.
 * E-mail:jieooo7@163.com
 * Date: 2015-08-15
 * Time: 11:53
 */
public class FragmentNewSearch extends BaseFragment implements Searchable {


    @ViewInject(R.id.fab)
    private FloatingActionButton fab;

    @ViewInject(R.id.grid_view)
    private PullToRefreshGridView mPullGridView;


    @ViewInject(R.id.no_search)
    private TextView mTextTip;


    private GridView mGridView;

    private boolean isRefresh=true;
    private Boolean isExit = false;// 双击退出
    private boolean isMore = true;

    private int mCurrentPage=0;
    private int mTotalPage=0;
    private String mWord="";





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
        mPullGridView.setPullRefreshEnabled(false);
        mPullGridView.setScrollLoadEnabled(true);
        mGridView= mPullGridView.getRefreshableView();
        mGridView.setNumColumns(2);
        mGridView.setHorizontalSpacing(10);
        mGridView.setVerticalSpacing(8);
        mGridView.setCacheColorHint(Color.TRANSPARENT);
        mGridView.setSelector(getResources().getDrawable(R.drawable.trans));
        mGridView.setAdapter(mAdapter);
//        pullData(0,"back");
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
//                    pullData(backId,"back");
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                isRefresh = false;
                if(isMore){
                    isMore = false;
                        if(mList.size()>=1) {
                            if(mCurrentPage<mTotalPage-1){
                                pullData(++mCurrentPage, mWord);
                            }
                        }else{
                            pullData(0,mWord);
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

                handle.post(new Runnable() {
                    @Override
                    public void run() {

                        if(mList.size()>0){
                            mTextTip.setVisibility(View.GONE);
                        }else{
                            mTextTip.setVisibility(View.VISIBLE);
                        }
                    }
                });
//                RequestManager.cancelAll(this);
            }
        };
    }


    protected  void pullData(final int page,final String word) {
        executeRequest(new GsonBaseRequest<BaseListModel<NewsModel>>(Request.Method.POST, UrlConfig.SEARCH_NEWS, new TypeToken<BaseListModel<NewsModel>>() {
        }.getType(), responseListener(), errorMoreListener()) {

            protected Map<String, String> getParams() {
                return new ApiParams().with("page", "" + page).with("keyword", word);
            }


        });
    }


    private Response.Listener<BaseListModel<NewsModel>> responseListener() {
        return new Response.Listener<BaseListModel<NewsModel>>() {
            @Override
            public void onResponse(BaseListModel<NewsModel> indexBaseListModel) {
                loadingdialog.dismiss();
                isMore = true;
                int totalPage=indexBaseListModel.getTotalpage();
                if(totalPage>0&&totalPage>mTotalPage){

                    mTotalPage=totalPage;
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
                            }
                        });
                    }



                }else{
                    if(!isRefresh){
                        mPullGridView.setHasMoreData(false);
                        ToastUtil.show(getCustomActivity(), getCustomActivity().getResources().getText(R.string.no_more_error));
                        mPullGridView.setHasMoreData(false);
                    }

//                    if(mList.size()>0){
//                        handle.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mTextTip.setVisibility(View.GONE);
//                            }
//                        },500);
//
//                    }else{
//                        handle.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                mTextTip.setVisibility(View.VISIBLE);
//                            }
//                        },500);
//                    }

                }
                handle.post(new Runnable() {
                    @Override
                    public void run() {

                        if(mList.size()>0){
                            mTextTip.setVisibility(View.GONE);
                        }else{
                            mTextTip.setVisibility(View.VISIBLE);
                        }
                    }
                });
                // 下拉加载完成
                mPullGridView.onPullDownRefreshComplete();

                // 上拉刷新完成
                mPullGridView.onPullUpRefreshComplete();
                mPullGridView.setLastUpdatedLabel(mTime);//设置最后刷新时间
                Date date = new Date();
                mTime = mDateFormat.format(date);

            }
        };

    }



//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if(hidden){
//            pullData(0, mWord);
//        }
//
//    }

    @Override
    public void search(String word, boolean isSearch) {

        mWord=word;
        mCurrentPage=0;
        mTotalPage=0;

        if(mList.size()>0){
            mList.clear();
        }
        mPullGridView.setPullLoadEnabled(true);
        if(isVisible()){

//            ToastUtil.show(getActivity(),"执行了");

            pullData(0,mWord);


        }else{

//            onHiddenChanged(true);

        }

    }

}
