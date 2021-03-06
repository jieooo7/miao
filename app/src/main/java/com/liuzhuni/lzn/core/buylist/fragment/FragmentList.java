package com.liuzhuni.lzn.core.buylist.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.BaseFragment;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.buylist.adapter.ListAdapter;
import com.liuzhuni.lzn.core.buylist.model.BuyListModel;
import com.liuzhuni.lzn.core.buylist.ui.FinishDialog;
import com.liuzhuni.lzn.core.login.LoginActivity;
import com.liuzhuni.lzn.core.model.BaseListModel;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.pulltorefresh.PullToRefreshBase;
import com.liuzhuni.lzn.pulltorefresh.PullToRefreshListView;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonBaseRequest;
import com.liuzhuni.lzn.volley.GsonRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrew Lee on 2015/4/23.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-23
 * Time: 11:23
 */
public class FragmentList extends BaseFragment {

    private List<BuyListModel> mList;
    private List<BuyListModel> mCurrentList;

    private PullToRefreshListView mPullList;
    private ListView mListView;

    private FinishDialog mDialog;
    private ListAdapter mAdapter;
    private TextView mNoContent;

    private int mTotal=1;
    private int mIndex=0;
    private boolean isUp = false;


    private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm");
    private String mTime;

    private Handler handler=new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list,
                container, false);
        mNoContent=(TextView)view.findViewById(R.id.list_no_content);
        initData(view);
        pullRefresh();
        longClick();


        return view;
    }

    protected void initData(View view){

        mList=new ArrayList<BuyListModel>();
        mPullList = (PullToRefreshListView) view.findViewById(R.id.fragment_buy_list);

        mPullList.setPullLoadEnabled(true);
        mPullList.setScrollLoadEnabled(true);
        mListView = mPullList.getRefreshableView();
        mListView.setDivider(getResources().getDrawable(R.drawable.divide));
        mListView.setDividerHeight(1);
        mListView.setCacheColorHint(Color.TRANSPARENT);
        mListView.setSelector(getResources().getDrawable(R.drawable.trans));



        mAdapter = new ListAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);

        Date date=new Date();
        mTime=mDateFormat.format(date);

        if(mIndex<mTotal){
            pullData(mIndex,0);
        }




    }

    protected void pullRefresh(){

        mPullList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase <ListView> refreshView) {
                // TODO Auto-generated method stub
                isUp = true;
                mIndex = 0;
                pullData(mIndex, 0);

            }

            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // TODO Auto-generated method stub

                isUp = false;
                if (mIndex < mTotal) {
                    pullData(mIndex,0);

                } else {
                    // 下拉加载完成
                    mPullList.onPullDownRefreshComplete();
//					// 上拉刷新完成
                    mPullList.onPullUpRefreshComplete();
                    // 设置是否有更多的数据
                    mPullList.setHasMoreData(false);
                }

            }

        });
    }




    protected synchronized void pullData(final int id,final int type){

        executeRequest(new GsonBaseRequest<BaseListModel<BuyListModel>>(Request.Method.GET, UrlConfig.WANT_BUY+id+"?t="+type,new TypeToken<BaseListModel<BuyListModel>>(){}.getType(),responseListener(),errorListener()){
        });
    }

    private Response.Listener<BaseListModel<BuyListModel>> responseListener() {
        return new Response.Listener<BaseListModel<BuyListModel>>(){
            @Override
            public void onResponse(BaseListModel<BuyListModel> indexBuyListModel) {

                if(indexBuyListModel.getRet()==0){
                    mTotal=indexBuyListModel.getTotalpage();
                    mIndex++;
                if(indexBuyListModel.getData()!=null){
                    mCurrentList=indexBuyListModel.getData();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(isUp){
                                mList.clear();
                                mPullList.setLastUpdatedLabel(mTime);//设置最后刷新时间
                                Date date=new Date();
                                mTime=mDateFormat.format(date);
                            }
                            mList.addAll(mCurrentList);
                            mAdapter.notifyDataSetChanged();
                        }
                    });



                }

            }else{
                    mIndex=mTotal;
                }

                mPullList.onPullDownRefreshComplete();
                // 上拉刷新完成
                mPullList.onPullUpRefreshComplete();

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(mList.size()<1){

                            mNoContent.setVisibility(View.VISIBLE);
                        }else {

                            mNoContent.setVisibility(View.GONE);
                        }
                    }
                });




            }
        };

    }
    protected synchronized void pullDelData(final int id){
        executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.DELETE_BUY_ITEM+id,BaseModel.class,responseDelListener(),errorListener()){

        });
    }

    public Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mPullList.onPullDownRefreshComplete();

                // 上拉刷新完成
                mPullList.onPullUpRefreshComplete();
//                Toast.makeText(activity,getResources().getText(R.string.error_retry), Toast.LENGTH_LONG).show();
                if(error.networkResponse!=null){
                    if(error.networkResponse.statusCode==402){//重新登录
                        PreferencesUtils.putBooleanToSPMap(getActivity(), PreferencesUtils.Keys.IS_LOGIN, false);
                        PreferencesUtils.clearSPMap(getActivity(),PreferencesUtils.Keys.USERINFO);
                        Intent intent=new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                }
            }
        };
    }

    private Response.Listener<BaseModel> responseDelListener() {
        return new Response.Listener<BaseModel>(){
            @Override
            public void onResponse(BaseModel delListModel) {

                if(delListModel.getRet()==0){

            }}
        };

    }

    protected void longClick(){

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                Object obj = mListView.getItemAtPosition(position);
                if(obj instanceof BuyListModel){
                    final int delId=((BuyListModel) obj).getId();
                    mDialog=new FinishDialog(getActivity(),((BuyListModel) obj).getValue());
                    mDialog.mItemOne.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mList.remove(position);
                            mDialog.dismiss();
                            if(mList.size()<1){

                                mNoContent.setVisibility(View.VISIBLE);

                            }else {
                                mNoContent.setVisibility(View.GONE);
                            }
                            mAdapter.notifyDataSetChanged();

                            pullDelData(delId);
                        }
                    });

                    mDialog.mItemThree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(FragmentFinish.mList!=null){
                                FragmentFinish.mList.add(0,mList.get(position));
                            }
                            mList.remove(position);
                            mDialog.dismiss();
                            if(mList.size()<1){

                                mNoContent.setVisibility(View.VISIBLE);

                            }else {
                                mNoContent.setVisibility(View.GONE);
                            }
                            mAdapter.notifyDataSetChanged();


                            pullBackData(delId);
                        }
                    });

                    mDialog.mItemTwo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mDialog.dismiss();
                        }
                    });


                    mDialog.show();


                }



                return true;
            }
        });

    }


    protected synchronized void pullBackData(final int id){
        executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.BUY_FEED, BaseModel.class,responseDelListener(),errorListener()) {
            protected Map<String, String> getParams() {
                return new ApiParams().with("id",""+id).with("state","1");//1代表满意
            }

        });
    }



}