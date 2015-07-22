package com.liuzhuni.lzn.core.index_new;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.Base2Activity;
import com.liuzhuni.lzn.config.AppManager;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.index_new.adapter.AllNewsAdapter;
import com.liuzhuni.lzn.core.index_new.model.NewsModel;
import com.liuzhuni.lzn.core.model.BaseListModel;
import com.liuzhuni.lzn.core.personInfo.InfoActivity;
import com.liuzhuni.lzn.pulltorefresh.PullToRefreshBase;
import com.liuzhuni.lzn.pulltorefresh.PullToRefreshGridView;
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
import java.util.Timer;
import java.util.TimerTask;

public class AllNewsActivity extends Base2Activity {

    @ViewInject(R.id.index_person_info)
    private TextView mInfoIv;
    @ViewInject(R.id.index_select)
    private TextView mSelectIv;

    @ViewInject(R.id.fab)
    private FloatingActionButton fab;

    @ViewInject(R.id.grid_view)
    private PullToRefreshGridView mPullGridView;


    private GridView mGridView;

    private boolean isRefresh=true;
    private Boolean isExit = false;// 双击退出





    private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm");
    private String mTime;

    private ImageLoader mImageLoader;


    private List<NewsModel> mList = null;
    private List<NewsModel> mCurrentList = null;
    private AllNewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_news);
        initData();
        findViewById();
        initUI();
        setListener();
    }

    @Override
    protected void initData() {

        mImageLoader = RequestManager.getImageLoader();

        mList = new ArrayList<NewsModel>();
        mAdapter = new AllNewsAdapter(mList, this, mImageLoader);

    }

    @Override
    protected void findViewById() {

        ViewUtils.inject(this);

    }

    @Override
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
        mGridView.setSelector(getResources().getDrawable(R.drawable.trans));
        mGridView.setAdapter(mAdapter);
        pullData(0,"back");

        fab.hide();
        fab.attachToListView(mGridView);

    }

    @Override
    protected void setListener() {

        mPullGridView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<GridView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
//                isUp = true;
//                pullData(mCurrentId);
                isRefresh = true;
                if(!mList.isEmpty()){
                    pullData(mList.get(0).getId(),"back");
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
                isRefresh = false;
                if(!mList.isEmpty()){
                    pullData(mList.get(mList.size()-1).getId(),"forward");
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

                Intent intent = new Intent();
                intent.setClass(AllNewsActivity.this, DetailActivity.class);
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
    @OnClick(R.id.index_person_info)
    public void personal(View v) {

        Intent intent = new Intent(this, InfoActivity.class);
        startActivity(intent);
        finish();

    }
    @OnClick(R.id.index_select)
    public void index(View v) {

        Intent intent = new Intent(this, IndexNewActivity.class);
        startActivity(intent);
        finish();

    }


    protected  void pullData(final int id,final String way) {
        executeRequest(new GsonBaseRequest<BaseListModel<NewsModel>>(Request.Method.POST, UrlConfig.GET_NEWS, new TypeToken<BaseListModel<NewsModel>>() {
        }.getType(), responseListener(), errorListener()) {

            protected Map<String, String> getParams() {
                return new ApiParams().with("id", ""+id).with("way",way);
            }


        });
    }

    private Response.Listener<BaseListModel<NewsModel>> responseListener() {
        return new Response.Listener<BaseListModel<NewsModel>>() {
            @Override
            public void onResponse(BaseListModel<NewsModel> indexBaseListModel) {
                if (indexBaseListModel.getData() != null) {
                    mCurrentList = indexBaseListModel.getData();
                    if(isRefresh){
                        mList.addAll(0, mCurrentList);
                    }else{
                        mList.addAll(mCurrentList);
                    }
                    mAdapter.notifyDataSetChanged();

                }else{
                    if(!isRefresh){
                        mPullGridView.setHasMoreData(false);
                        ToastUtil.show(AllNewsActivity.this, getResources().getText(R.string.no_more_error));
                    }

                }
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
