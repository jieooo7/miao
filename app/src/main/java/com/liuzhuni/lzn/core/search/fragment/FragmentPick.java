package com.liuzhuni.lzn.core.search.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import com.liuzhuni.lzn.core.index_new.adapter.PickAdapter;
import com.liuzhuni.lzn.core.index_new.model.PickModel;
import com.liuzhuni.lzn.core.login.LoginActivity;
import com.liuzhuni.lzn.core.model.BaseListModel;
import com.liuzhuni.lzn.core.search.PickSearchable;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.utils.ToastUtil;
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
 * Created by Andrew Lee on 2015/8/15.
 * E-mail:jieooo7@163.com
 * Date: 2015-08-15
 * Time: 09:53
 */
public class FragmentPick extends BaseFragment implements
        XListViewNew.IXListViewListener,XListViewNew.HideFab,PickSearchable {

    @ViewInject(R.id.expandablelist)
    private XListViewNew pinListView;


    @ViewInject(R.id.fab)
    private FloatingActionButton fab;


    @ViewInject(R.id.no_search)
    private TextView mTextTip;






    private int mCurrentPage=0;
    private int mTotalPage=0;

    private int mTopId;

    private Boolean isMore = true;//防止重复加载

    private String mWord="";



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

        View view = inflater.inflate(R.layout.fragment_pick,
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



    }


    protected void initUI() {


        fab.hide();
        pinListView.setHideFab(this);
        pinListView.setPullLoadEnable(false);
        pinListView.setPullRefreshEnable(false);
        pinListView.setXListViewListener(this);
        pinListView.setAdapter(mAdapter);


        Date date = new Date();
        mTime = mDateFormat.format(date);
        pinListView.setAdapter(mAdapter);
        fab.attachToListView(pinListView);


    }


    @Override
    public void onResume() {
        super.onResume();
    }

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
                    if(mList.size()>=1){
                    }else{
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
                    if(mList.size()>=1) {
                        if(mCurrentPage<mTotalPage-1){
                            pullData(++mCurrentPage, mWord);
                        }
                    }else{
                        pullData(0,mWord);
                    }
                }
                pinListView.stopLoadMore();
            }
        },200);

    }




    protected  void pullData(final int page,final String word) {
        executeRequest(new GsonBaseRequest<BaseListModel<PickModel>>(Request.Method.POST, UrlConfig.SEARCH_PICK, new TypeToken<BaseListModel<PickModel>>() {
        }.getType(), responseListener(), errorMoreListener()) {

            protected Map<String, String> getParams() {
                return new ApiParams().with("page", "" + page).with("keyword", word);
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


                mHandler.post(new Runnable() {
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

    private Response.Listener<BaseListModel<PickModel>> responseListener() {
        return new Response.Listener<BaseListModel<PickModel>>() {
            @Override
            public void onResponse(BaseListModel<PickModel> indexBaseListModel) {
                if(indexBaseListModel.getRet()==0){
                    isMore = true;
                    int totalPage=indexBaseListModel.getTotalpage();
                        if(totalPage>0&&totalPage>mTotalPage){

                            mTotalPage=totalPage;
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
                            ToastUtil.show(getCustomActivity(), getCustomActivity().getResources().getText(R.string.no_more_error));
                        }


                    }


                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(mList.size()>5){
                                pinListView.setPullLoadEnable(true);
                            }

                            if(mList.size()>0){
                                mTextTip.setVisibility(View.GONE);
                            }else{
                                mTextTip.setVisibility(View.VISIBLE);
                            }
                        }
                    });





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


    @OnClick(R.id.fab)
    public void fab(View v) {
        pinListView.setSelection(0);
        fab.hide();

    }


//    @Override
//    public void onHiddenChanged(boolean hidden) {
//        super.onHiddenChanged(hidden);
//        if(hidden){
//            pullData(0,mWord);
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
        pinListView.setPullLoadEnable(false);
        if(isVisible()){

            pullData(0,mWord);


        }else{

//            onHiddenChanged(true);

        }

    }
}
