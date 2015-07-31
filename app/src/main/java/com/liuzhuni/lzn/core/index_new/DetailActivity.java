package com.liuzhuni.lzn.core.index_new;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.Base2Activity;
import com.liuzhuni.lzn.config.MessageWhat;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.comment.CommentActivity;
import com.liuzhuni.lzn.core.comment.model.CommentModel;
import com.liuzhuni.lzn.core.comment.ui.CommentDialog;
import com.liuzhuni.lzn.core.goods.ToBuyActivity;
import com.liuzhuni.lzn.core.goods.ui.ListViewForScrollView;
import com.liuzhuni.lzn.core.index_new.adapter.DetailAdapter;
import com.liuzhuni.lzn.core.index_new.model.DetailContentModel;
import com.liuzhuni.lzn.core.index_new.model.DetailModel;
import com.liuzhuni.lzn.core.index_new.utils.MessageSpan;
import com.liuzhuni.lzn.core.login.Loginable;
import com.liuzhuni.lzn.core.login.TheIntent;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonBaseRequest;
import com.liuzhuni.lzn.volley.RequestManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;

public class DetailActivity extends Base2Activity {

    @ViewInject(R.id.title_left)
    private TextView mBackTv;

    @ViewInject(R.id.title_middle)
    private TextView mTitleTv;

    @ViewInject(R.id.title_right_iv)
    private ImageView mRightIv;

    @ViewInject(R.id.detail_item_img)
    private NetworkImageView mImageIv;

    @ViewInject(R.id.detail_item_mall)
    private TextView mMallTv;

    @ViewInject(R.id.detail_item_user)
    private TextView mNameTv;
    @ViewInject(R.id.detail_item_time)
    private TextView mTimeTv;

    @ViewInject(R.id.detail_title)
    private TextView mPriceTv;


    @ViewInject(R.id.index_item_expired)
    private ImageView mExpiredTv;

    @ViewInject(R.id.detail_buy)
    private TextView mBuyTv;

    @ViewInject(R.id.comment_nums)
    private TextView mCommenttv;

    @ViewInject(R.id.comment_iv)
    private ImageView mCommentIv;

    @ViewInject(R.id.detail_edit)
    private EditText mCommentEt;

    @ViewInject(R.id.detail_list)
    private ListViewForScrollView mListView;


    @ViewInject(R.id.goods_scroll)
    private ScrollView mScrollview;
    private List<DetailContentModel> mList;

    private DetailAdapter mAdapter;

    private ImageLoader mImageLoader;

    private String mId;
    private boolean mIsFromSelect;

    private String mUrl;

    private String link;
    private String img_link;
    private String title;
    private String content;
    private String name;
    private String copy_text;

    private String mUrlReply;
    private int mReviewNum;


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == MessageWhat.LINK) {
                MessageSpan ms = (MessageSpan) msg.obj;
                Object[] spans = (Object[])ms.getObj();

                for (Object span : spans) {
                    if (span instanceof URLSpan) {
                        Intent intent = new Intent(DetailActivity.this, ToBuyActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putString("url",((URLSpan) span).getURL());
                        bundle.putString("title", "");
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                }
            }
        };
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initData();
        findViewById();
        initUI();
        setListener();
    }

    @Override
    protected void initData() {
        mList=new ArrayList<DetailContentModel>();
        mImageLoader= RequestManager.getImageLoader();

        mId=getIntent().getExtras().getString("id");
        mIsFromSelect=getIntent().getExtras().getBoolean("isSelect");
        mAdapter=new DetailAdapter(mList,this,mImageLoader,handler,mIsFromSelect);

        if(mIsFromSelect){
            mUrlReply= UrlConfig.COMMENT_SEL_REPLY;
        }else{
            mUrlReply=UrlConfig.COMMENT_NEWS_REPLY;
        }


        link="http://www.liuzhuni.com/app";
        img_link="http://m.liuzhuni.com/huimapp/content/img/icon114.png";
        title=getText(R.string.share_title).toString();
        content=getText(R.string.share_content).toString();
        name=getText(R.string.share_title).toString();
        copy_text="http://www.liuzhuni.com/app";

    }

    @Override
    protected void findViewById() {
        ViewUtils.inject(this);
    }

    @Override
    protected void initUI() {


        mBackTv.setText(getText(R.string.back));
        if(mIsFromSelect){
            mTitleTv.setText(getText(R.string.select_detail));
        }else{
            mTitleTv.setText(getText(R.string.all_news_detail));
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeight = dm.heightPixels;

        RelativeLayout.LayoutParams rp= new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,(int)(screenHeight/4));

        mImageIv.setLayoutParams(rp);

        mRightIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_share));


        mListView.setAdapter(mAdapter);
//        mListView.bindLinearLayout(mAdapter);
        if(mIsFromSelect){
            pullData(UrlConfig.GET_SELECT_DETAIL+mId);
        }else{
            pullData(UrlConfig.GET_NEWS_DETAIL+mId);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mListView.setFocusable(false);
        mScrollview.smoothScrollTo(0, 0);
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.title_left)
    public void back(View v) {

        finish();
    }
    @OnClick(R.id.title_right_iv)
    public void share(View v) {

        //分享
        shareShow();
    }

    @OnClick(R.id.comment_rl)
    public void comment(View v) {

        TheIntent theIntent = new TheIntent(this, new Loginable() {
            @Override
            public void intent() {

                Intent intent = new Intent();
                intent.setClass(DetailActivity.this, CommentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", "" + mId);
                bundle.putString("url",mUrl);
                bundle.putBoolean("isSelect", mIsFromSelect);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        theIntent.go();


    }


    @OnClick(R.id.detail_edit)
    public void edit(View v) {


        TheIntent theIntent = new TheIntent(this, new Loginable() {
            @Override
            public void intent() {

                //评价
                final CommentDialog dialog=new CommentDialog(DetailActivity.this);
                dialog.mTitle.setText("发表评论");
                dialog.mSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String text = dialog.mEdit.getText().toString();
                        if (text.length() < 5) {
                            ToastUtil.customShow(DetailActivity.this, "评论内容不少于5个字哦~");
                        } else {
                            pullCommentData(mUrlReply, mId, "0", text);
                            dialog.dismiss();

                        }

                    }
                });


                dialog.show();
//                dialog.mEdit.setFocusable(true);
//                dialog.mEdit.setFocusableInTouchMode(true);
//                dialog.mEdit.requestFocus();
//
//                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.showSoftInput(dialog.mEdit, InputMethodManager.RESULT_SHOWN);
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_NOT_ALWAYS);
//                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
            }
        });
        theIntent.go();



    }


    protected  void pullCommentData(final String url,final String pid,final String rid,final String text) {
        executeRequest(new GsonBaseRequest<BaseModel<CommentModel>>(Request.Method.POST, url, new TypeToken<BaseModel<CommentModel>>() {
        }.getType(), responseComListener(), errorListener()) {

            protected Map<String, String> getParams() {
                return new ApiParams().with("productid", ""+pid).with("reviewid", rid).with("text",text);
            }

        });
    }

    protected Response.Listener<BaseModel<CommentModel>> responseComListener() {
        return new Response.Listener<BaseModel<CommentModel>>() {
            @Override
            public void onResponse(BaseModel<CommentModel> indexBaseListModel) {
                if (indexBaseListModel.getData() != null) {

                    ToastUtil.show(DetailActivity.this, getResources().getText(R.string.success_comment));

                    mCommenttv.setText(mReviewNum+1+"");
                    mCommenttv.setVisibility(View.VISIBLE);


                }

            }
        };

    }




    @OnClick(R.id.detail_buy)
    public void buy(View v) {
//        购买
        Intent intent = new Intent();
        intent.setClass(this, ToBuyActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("url", mUrl);
        bundle.putString("title", "");
        intent.putExtras(bundle);
        startActivity(intent);
    }


    protected void shareShow(){
        //分享
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
        oks.setDialogMode();
// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(link);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(content);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        oks.setImageUrl(img_link);
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(link);
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(name);
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl(link);

        oks.setShareContentCustomizeCallback(
                new ShareContentCustomizeCallback(){

                    @Override
                    public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                        if (SinaWeibo.NAME.equals(platform.getName())) {
                            paramsToShare.setText(content+link);
                        }
                    }
                });






        Bitmap enableLogo = BitmapFactory.decodeResource(getResources(), R.drawable.share_copy_n);
        Bitmap disableLogo = BitmapFactory.decodeResource(getResources(), R.drawable.share_copy_s);
        String label = getResources().getString(R.string.copy_link);
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                copyToClipboard(copy_text);
                ToastUtil.customShow(DetailActivity.this, getText(R.string.copy_tips));
            }
        };
        oks.setCustomerLogo(enableLogo, disableLogo, label, listener);

// 启动分享GUI
        oks.show(this);


    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    private void copyToClipboard(String str){//复制剪切板
        int sdk = Build.VERSION.SDK_INT;
        if(sdk < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(str);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("text label",str);
            clipboard.setPrimaryClip(clip);
        }
    }




    protected  void pullData(final String url) {
        executeRequest(new GsonBaseRequest<BaseModel<DetailModel>>(Request.Method.GET, url, new TypeToken<BaseModel<DetailModel>>() {
        }.getType(), responseListener(), errorListener()) {

        });
    }

    private Response.Listener<BaseModel<DetailModel>> responseListener() {
        return new Response.Listener<BaseModel<DetailModel>>() {
            @Override
            public void onResponse(BaseModel<DetailModel> indexBaseListModel) {
                if (indexBaseListModel.getRet() == 0 && indexBaseListModel.getData() != null) {
                    DetailModel model=indexBaseListModel.getData();
                    mImageIv.setImageUrl(model.getPic(), mImageLoader);
                    if(model.isExpired()){
                        mExpiredTv.setVisibility(View.VISIBLE);
                    }else{
                        mExpiredTv.setVisibility(View.GONE);
                    }
                    mPriceTv.setText(model.getTitle());

                    SpannableString ss = new SpannableString(model.getTitle1());
                    ss.setSpan(new ForegroundColorSpan(Color.argb(0xff, 0xf5, 0x5d, 0x62)),0,ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mPriceTv.append(ss);
                    mMallTv.setText(model.getMall());
                    mNameTv.setText(model.getName());
                    mTimeTv.setText(model.getTime());
                    mReviewNum=model.getReview();
                    if(model.getReview()>0){
                        mCommenttv.setText(""+model.getReview());
                        mCommenttv.setVisibility(View.VISIBLE);
                    }else{
                        mCommenttv.setVisibility(View.GONE);
                    }
                    mUrl=model.getUrl();
                    mList.addAll(model.getContent());


                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });


                    link=model.getShareurl();
                    img_link=model.getPic();
                    title=model.getTitle();
                    content=model.getTitle()+model.getTitle1();
                    name=model.getTitle();
                    copy_text=mUrl;


                }
            }};


        }




}
