package com.liuzhuni.lzn.core.comment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.comment.adapter.CommentAdapter;
import com.liuzhuni.lzn.core.comment.model.CommentModel;
import com.liuzhuni.lzn.core.comment.ui.CommentDialog;
import com.liuzhuni.lzn.core.goods.ToBuyActivity;
import com.liuzhuni.lzn.core.model.BaseListModel;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonBaseRequest;
import com.liuzhuni.lzn.volley.RequestManager;
import com.liuzhuni.lzn.xList.XListViewNew;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CommentActivity extends Base2Activity implements XListViewNew.IXListViewListener,CommentAdapter.ReplyListener{


    @ViewInject(R.id.title_middle)
    private TextView mTitleTv;

    @ViewInject(R.id.detail_edit)
    private TextView mEditTv;

    @ViewInject(R.id.comment_list)
    private XListViewNew mListView;



    private List<CommentModel> mList;
    private CommentAdapter mAdapter;
    private ImageLoader mImageLoader;


    private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm");
    private String mTime;


    private String mId;
    private boolean mIsFromSelect;
    private String mUrl;

    private String mUrlGet;
    private String mUrlReply;

    private boolean isRefresh = true;
    private boolean isNewReply = true;

    private int reviewNum;
    private int mPosition;
    private String mText="";

    public Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initData();
        findViewById();
        initUI();
        setListener();
    }

    @Override
    protected void initData() {
        mImageLoader= RequestManager.getImageLoader();

        mId=getIntent().getExtras().getString("id");
        mUrl=getIntent().getExtras().getString("url");
        mIsFromSelect=getIntent().getExtras().getBoolean("isSelect");

        if(mIsFromSelect){
            mUrlGet= UrlConfig.COMMENT_SEL;
            mUrlReply= UrlConfig.COMMENT_SEL_REPLY;
        }else{
            mUrlGet=UrlConfig.COMMENT_NEWS;
            mUrlReply=UrlConfig.COMMENT_NEWS_REPLY;
        }

        pullData(mUrlGet,"0",mId,"");

    }

    @Override
    protected void findViewById() {
            ViewUtils.inject(this);
    }

    @Override
    protected void initUI() {


        mList=new ArrayList<CommentModel>();
        mAdapter=new CommentAdapter(this,mList,mImageLoader);
        mAdapter.setReplyListener(this);
        mListView.setXListViewListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setPullLoadEnable(false);

    }

    @Override
    protected void setListener() {

    }



    @OnClick(R.id.title_left)
    public void back(View v) {

        finish();
    }

    @OnClick(R.id.detail_edit)
    public void edit(View v) {

        //评价
        isNewReply=true;
        final CommentDialog dialog=new CommentDialog(this);
        dialog.mTitle.setText("发表评论");
        dialog.mSubmit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String text=dialog.mEdit.getText().toString();
                if(text.length()<5){
                    ToastUtil.customShow(CommentActivity.this, "评论内容不少于5个字哦~");
                }else{
                    pullCommentData(mUrlReply,mId,"0",text);
                    dialog.dismiss();

                }

            }
        });

        dialog.show();

    }


    @Override
    public void reply(int pos) {

        isNewReply=false;
        final CommentDialog dialog=new CommentDialog(this);
        mPosition =pos;
        dialog.mTitle.setText("回复 "+mList.get(mPosition).getUserNick());
        dialog.mEdit.setHint("回复 " + mList.get(mPosition).getUserNick());
        dialog.mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = dialog.mEdit.getText().toString();
                mText=text;
                if (text.length() < 5) {
                    ToastUtil.customShow(CommentActivity.this, "评论内容不少于5个字哦~");
                } else {
                    pullCommentData(mUrlReply, mId, "" + mList.get(mPosition).getId(), text);
                    dialog.dismiss();

                }

            }
        });

        dialog.show();

    }




    @OnClick(R.id.title_right)
    public void buy(View v) {

        Intent intent = new Intent();
        intent.setClass(this, ToBuyActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("url",mUrl);
        bundle.putString("title","");
        intent.putExtras(bundle);
        startActivity(intent);
    }




    protected  void pullData(final String url,final String id,final String pid,final String way) {
        executeRequest(new GsonBaseRequest<BaseListModel<CommentModel>>(Request.Method.POST, url, new TypeToken<BaseListModel<CommentModel>>() {
        }.getType(), responseFilterListener(), errorListener()) {

            protected Map<String, String> getParams() {//40455
                return new ApiParams().with("id", id).with("productid", pid).with("way",way);
            }

        });
    }
    protected  void pullCommentData(final String url,final String pid,final String rid,final String text) {
        executeRequest(new GsonBaseRequest<BaseModel<CommentModel>>(Request.Method.POST, url, new TypeToken<BaseModel<CommentModel>>() {
        }.getType(), responseComListener(), errorListener()) {

            protected Map<String, String> getParams() {
                return new ApiParams().with("productid", ""+pid).with("reviewid", rid).with("text",text);
            }

        });
    }

    protected Response.Listener<BaseListModel<CommentModel>> responseFilterListener() {
        return new Response.Listener<BaseListModel<CommentModel>>() {
            @Override
            public void onResponse(BaseListModel<CommentModel> indexBaseListModel) {
                reviewNum=indexBaseListModel.getReview();
                if(reviewNum>=1){
                    mTitleTv.setText(reviewNum+"条评论");
                }


                if (indexBaseListModel.getData() != null) {

                    List<CommentModel> mCurrentList = indexBaseListModel.getData();
                    if(isRefresh){
                        mList.addAll(0, mCurrentList);
                        mListView.setRefreshTime(mTime);
                        Date date = new Date();
                        mTime = mDateFormat.format(date);
                    }else{

                        mList.addAll(mCurrentList);
                    }
                    if(mList.size()>10){
                        mListView.setPullLoadEnable(true);
                    }
                    mAdapter.notifyDataSetChanged();

                }else{

                    if(!isRefresh){
                        ToastUtil.show(CommentActivity.this, getResources().getText(R.string.no_more_error));
                    }
                }

            }
        };

    }
    protected Response.Listener<BaseModel<CommentModel>> responseComListener() {
        return new Response.Listener<BaseModel<CommentModel>>() {
            @Override
            public void onResponse(BaseModel<CommentModel> indexBaseListModel) {
                if (indexBaseListModel.getData() != null) {

                    ToastUtil.show(CommentActivity.this, getResources().getText(R.string.success_comment));

                        mList.add(0, indexBaseListModel.getData());//只用id,其他自己构造

//                        if(mList.get(mPosition).getComment()!=null){
//
//                            mList.get(mPosition).getComment().add(new ReplyModel(PreferencesUtils.getValueFromSPMap(CommentActivity.this, PreferencesUtils.Keys.NICKNAME, "", PreferencesUtils.Keys.USERINFO),mText));
//                        }else{
//                            List<ReplyModel> list=new ArrayList<ReplyModel>();
//                            list.add(new ReplyModel(PreferencesUtils.getValueFromSPMap(CommentActivity.this, PreferencesUtils.Keys.NICKNAME, "", PreferencesUtils.Keys.USERINFO),mText));
//                            mList.get(mPosition).setComment(list);
//                        }


                    mAdapter.notifyDataSetChanged();

                }

            }
        };

    }


    @Override
    public void onRefresh() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isRefresh=true;

                if(mList.size()>=1){
                    pullData(mUrlGet,""+mList.get(0).getId(),mId,"back");
                }else{
                    pullData(mUrlGet,"0",mId,"back");
                }

                mListView.stopRefresh();
            }
        },200);

    }

    @Override
    public void onLoadMore() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {

                isRefresh=false;
                if(mList.size()>=1) {
                    int size=mList.size();
                    if(size<reviewNum){
                        pullData(mUrlGet,""+mList.get(size-1).getId(),mId,"forward");
                    }else{
                        ToastUtil.show(CommentActivity.this, getResources().getText(R.string.no_more_error));
                    }
                }else{
                    pullData(mUrlGet,"0",mId,"back");
                }
                mListView.stopLoadMore();
            }
        },200);

    }


}
