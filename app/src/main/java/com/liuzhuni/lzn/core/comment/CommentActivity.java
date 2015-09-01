package com.liuzhuni.lzn.core.comment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.reflect.TypeToken;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.Base2Activity;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.comment.adapter.CommentAdapter;
import com.liuzhuni.lzn.core.comment.model.CommentModel;
import com.liuzhuni.lzn.core.comment.ui.CommentDialog;
import com.liuzhuni.lzn.core.goods.ToBuyActivity;
import com.liuzhuni.lzn.core.login.LoginActivity;
import com.liuzhuni.lzn.core.model.BaseListModel;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.utils.PreferencesUtils;
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

public class CommentActivity extends Base2Activity implements XListViewNew.IXListViewListener, CommentAdapter.ReplyListener, Replyable {


    private TextView mTitleTv;
    private TextView mBackTv;
    private TextView mBuyTv;
    private EditText mCommentTv;

    private XListViewNew mListView;


    private LinearLayout mll;


    private List<CommentModel> mList;
    private CommentAdapter mAdapter;
    private ImageLoader mImageLoader;


    private SimpleDateFormat mDateFormat = new SimpleDateFormat("HH:mm");
    private String mTime;
    private boolean isMore = true;//防止重复加载


    private String mId;
    private boolean mIsFromSelect;
    private String mUrl;

    private String mUrlGet;
    private String mUrlReply;

    private boolean isRefresh = true;
    private boolean isNewReply = true;

    private int reviewNum = 0;
    private int mPosition;
    private String mText = "";

    private boolean isReviewNum = true;//保证 评论数只初始化一次

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
        mImageLoader = RequestManager.getImageLoader();

        mId = getIntent().getExtras().getString("id");
        mUrl = getIntent().getExtras().getString("url");
        mIsFromSelect = getIntent().getExtras().getBoolean("isSelect");

        if (mIsFromSelect) {
            mUrlGet = UrlConfig.COMMENT_SEL;
            mUrlReply = UrlConfig.COMMENT_SEL_REPLY;
        } else {
            mUrlGet = UrlConfig.COMMENT_NEWS;
            mUrlReply = UrlConfig.COMMENT_NEWS_REPLY;
        }


    }

    @Override
    protected void findViewById() {

        mTitleTv=(TextView)findViewById(R.id.title_middle);
        mBackTv=(TextView)findViewById(R.id.title_left);
        mBuyTv=(TextView)findViewById(R.id.title_right);
        mCommentTv=(EditText)findViewById(R.id.detail_edit);


        mListView=(XListViewNew)findViewById(R.id.comment_list);
        mll=(LinearLayout)findViewById(R.id.when_no_comment);

    }

    @Override
    protected void initUI() {

        loadingdialog.show();
        pullData(mUrlGet, "0", mId, "");
        mList = new ArrayList<CommentModel>();
        mAdapter = new CommentAdapter(this, mList, mImageLoader);
        mAdapter.setReplyListener(this);
        mListView.setXListViewListener(this);
        mListView.setAdapter(mAdapter);
        mListView.setPullLoadEnable(false);

    }

    @Override
    protected void setListener() {


        mBackTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });


        mBuyTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buy();
            }
        });

        mCommentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });

    }


    public void back() {

        finish();
    }

    public void edit() {

        //评价
        isNewReply = true;
        final CommentDialog dialog = new CommentDialog(this);
        dialog.mTitle.setText("发表评论");
        dialog.mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = dialog.mEdit.getText().toString();
                if (text.length() < 5) {
                    ToastUtil.customShow(CommentActivity.this, "评论内容不少于5个字哦~");
                } else {
                    pullCommentData(mUrlReply, mId, "0", text);
                    dialog.dismiss();

                }

            }
        });

        dialog.show();

    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    @Override
    public void reply(int pos) {

        isNewReply = false;
        final CommentDialog dialog = new CommentDialog(this);
        mPosition = pos;
        dialog.mTitle.setText("回复 " + mList.get(mPosition).getUserNick());
        dialog.mEdit.setHint("回复 " + mList.get(mPosition).getUserNick());
        dialog.mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = dialog.mEdit.getText().toString();
                mText = text;
                if (text.length() < 5) {
                    ToastUtil.customShow(CommentActivity.this, "评论内容不少于5个字哦~");
                } else {
                    if (isTouch) {
                        isTouch = false;
                        pullCommentData(mUrlReply, mId, "" + mList.get(mPosition).getId(), text);
                    }
                    dialog.dismiss();

                }

            }
        });

        dialog.show();

    }


    public void buy() {
        Intent intent = new Intent();
        intent.setClass(this, ToBuyActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", mUrl);
        bundle.putString("title", "");
        intent.putExtras(bundle);
        startActivity(intent);
    }


    protected synchronized void pullData(final String url, final String id, final String pid, final String way) {
        executeRequest(new GsonBaseRequest<BaseListModel<CommentModel>>(Request.Method.POST, url, new TypeToken<BaseListModel<CommentModel>>() {
        }.getType(), responseFilterListener(), errorMoreListener()) {

            protected Map<String, String> getParams() {//40455
                return new ApiParams().with("id", id).with("productid", pid).with("way", way);
            }

        });
    }

    protected void pullCommentData(final String url, final String pid, final String rid, final String text) {
        executeRequest(new GsonBaseRequest<BaseModel<CommentModel>>(Request.Method.POST, url, new TypeToken<BaseModel<CommentModel>>() {
        }.getType(), responseComListener(), errorListener()) {

            protected Map<String, String> getParams() {
                return new ApiParams().with("productid", "" + pid).with("reviewid", rid).with("text", text);
            }

        });
    }

    protected Response.Listener<BaseListModel<CommentModel>> responseFilterListener() {
        return new Response.Listener<BaseListModel<CommentModel>>() {
            @Override
            public void onResponse(BaseListModel<CommentModel> indexBaseListModel) {

                loadingdialog.dismiss();
                isTouch = true;
                isMore = true;
                if (isReviewNum) {
                    reviewNum = indexBaseListModel.getReview();
                    isReviewNum = false;
                }
//                if (reviewNum >= 1) {
//                    mTitleTv.setText(reviewNum + "条评论");
//                } else {
//
//                    mHandler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            mll.setVisibility(View.VISIBLE);
//                        }
//                    });
//                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (reviewNum >= 1) {
                            mTitleTv.setText(reviewNum + "条评论");
                        } else {

                            mll.setVisibility(View.VISIBLE);
                        }
                    }
                });

                if (indexBaseListModel.getData() != null) {

                    final List<CommentModel> mCurrentList = indexBaseListModel.getData();
                    if (isRefresh) {


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
                    } else {


                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                mList.addAll(mCurrentList);
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }


//                    ToastUtil.show(CommentActivity.this,mList.get(mList.size()-1).getComment().get(0).getText()+"");
                } else {

                    if (!isRefresh) {
                        ToastUtil.show(CommentActivity.this, getResources().getText(R.string.no_more_error));
                        mListView.setPullLoadEnable(false);
                    }
                }

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mList.size() > 10) {
                            mListView.setPullLoadEnable(true);
                        }
                    }
                });


            }
        };

    }

    protected Response.Listener<BaseModel<CommentModel>> responseComListener() {
        return new Response.Listener<BaseModel<CommentModel>>() {
            @Override
            public void onResponse(BaseModel<CommentModel> indexBaseListModel) {
                isTouch = true;
                if (indexBaseListModel.getData() != null) {

//                    ToastUtil.show(CommentActivity.this, getResources().getText(R.string.success_comment));

                    final CommentModel model = indexBaseListModel.getData();

                    //只用id,其他自己构造

//                        if(mList.get(mPosition).getComment()!=null){
//
//                            mList.get(mPosition).getComment().add(new ReplyModel(PreferencesUtils.getValueFromSPMap(CommentActivity.this, PreferencesUtils.Keys.NICKNAME, "", PreferencesUtils.Keys.USERINFO),mText));
//                        }else{
//                            List<ReplyModel> list=new ArrayList<ReplyModel>();
//                            list.add(new ReplyModel(PreferencesUtils.getValueFromSPMap(CommentActivity.this, PreferencesUtils.Keys.NICKNAME, "", PreferencesUtils.Keys.USERINFO),mText));
//                            mList.get(mPosition).setComment(list);
//                        }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mll.setVisibility(View.GONE);
                            mList.add(0, model);
                            mAdapter.notifyDataSetChanged();
                            mListView.setSelection(0);
                        }
                    });


                } else {

                }
                if(!TextUtils.isEmpty(indexBaseListModel.getMes())){
                    ToastUtil.customShow(CommentActivity.this, indexBaseListModel.getMes());
                }

            }
        };

    }


    @Override
    public void onRefresh() {

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                isRefresh = true;
                if (isMore) {
                    isMore = false;
                    if (mList.size() >= 1) {
                        pullData(mUrlGet, "" + mList.get(0).getId(), mId, "back");
                    } else {
                        pullData(mUrlGet, "0", mId, "back");
                    }
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
                    int size = mList.size();
                    if (size < reviewNum) {
                        if (isMore) {
                            isMore = false;
                            pullData(mUrlGet, "" + mList.get(size - 1).getId(), mId, "forward");
                        }
                    } else {
                        ToastUtil.show(CommentActivity.this, getResources().getText(R.string.no_more_error));
                        mListView.setPullLoadEnable(false);
                    }
                } else {
                    pullData(mUrlGet, "0", mId, "back");
                }
                mListView.stopLoadMore();
            }
        }, 200);

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
                        PreferencesUtils.putBooleanToSPMap(CommentActivity.this, PreferencesUtils.Keys.IS_LOGIN, false);
                        PreferencesUtils.clearSPMap(CommentActivity.this, PreferencesUtils.Keys.USERINFO);
                        Intent intent = new Intent(CommentActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
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


    @Override
    public void replyFloor(int id, String name) {

        isNewReply = false;
        final int rId = id;
        final CommentDialog dialog = new CommentDialog(this);
        dialog.mTitle.setText("回复 " + name);
        dialog.mEdit.setHint("回复 " + name);
        dialog.mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = dialog.mEdit.getText().toString();
                mText = text;
                if (text.length() < 5) {
                    ToastUtil.customShow(CommentActivity.this, "评论内容不少于5个字哦~");
                } else {
                    if (isTouch) {
                        isTouch = false;
                        pullCommentData(mUrlReply, mId, "" + rId, text);
                    }
                    dialog.dismiss();

                }

            }
        });

        dialog.show();


    }
}
