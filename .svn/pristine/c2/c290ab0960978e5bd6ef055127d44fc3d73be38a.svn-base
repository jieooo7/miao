package com.liuzhuni.lzn.core.personInfo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.Base2Activity;
import com.liuzhuni.lzn.config.Check;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.index_new.AllNewsActivity;
import com.liuzhuni.lzn.core.index_new.IndexNewActivity;
import com.liuzhuni.lzn.core.login.LoginActivity;
import com.liuzhuni.lzn.core.login.Loginable;
import com.liuzhuni.lzn.core.login.TheIntent;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.core.personInfo.model.ShareModel;
import com.liuzhuni.lzn.core.personInfo.ui.LoginoutDialog;
import com.liuzhuni.lzn.core.personInfo.ui.MeView;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.volley.GsonBaseRequest;
import com.liuzhuni.lzn.volley.GsonRequest;
import com.liuzhuni.lzn.volley.RequestManager;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;


public class InfoActivity extends Base2Activity {



    @ViewInject(R.id.title_left)
    private TextView mBackTv;
    @ViewInject(R.id.title_right_iv)
    private ImageView mSignIv;
    @ViewInject(R.id.info_push)
    private MeView mPushMv;
    @ViewInject(R.id.info_message)
    private MeView mMessageMv;
    @ViewInject(R.id.info_share)
    private MeView mShareMv;
    @ViewInject(R.id.info_feedback)
    private MeView mFeedbackMv;
    @ViewInject(R.id.info_help)
    private MeView mHelpMv;
    @ViewInject(R.id.info_loginout)
    private MeView mLoginoutMv;
    @ViewInject(R.id.head_info)
    private RelativeLayout mHeadRl;
    @ViewInject(R.id.not_login_rl)
    private RelativeLayout mNotLogin;
    @ViewInject(R.id.please_login)
    private TextView mLogin;
    @ViewInject(R.id.info_loginout_line)
    private TextView mLine;
    @ViewInject(R.id.info_loginout_line_up)
    private TextView mLineUp;
    @ViewInject(R.id.info_left)
    private NetworkImageView mHeadImage;
    @ViewInject(R.id.info_head_name)
    private TextView mNickName;
    @ViewInject(R.id.info_level)
    private TextView mLevel;


    @ViewInject(R.id.index_people_say)
    private TextView mNewsIv;
    @ViewInject(R.id.index_select)
    private TextView mSelectIv;

    private String link;
    private String img_link;
    private String title;
    private String content;
    private String name;
    private String copy_text;

    private boolean dayFlag=true;

    //        链接 link  图片地址 img_link 标题 title 内容 content 网站名称 name  剪贴板文字 copy_text



    private LoginoutDialog mDialog;

    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        initData();
        findViewById();
        initUI();
        setListener();
    }


    @Override
    protected void onResume() {
        super.onResume();


        mLevel.setText(PreferencesUtils.getValueFromSPMap(this,PreferencesUtils.Keys.LEVEL,getResources().getString(R.string.defalt_level),PreferencesUtils.Keys.USERINFO));

        mMessageMv.setNum(PreferencesUtils.getIntFromSPMap(this, PreferencesUtils.Keys.UN_READ,PreferencesUtils.Keys.USERINFO));

        mHeadImage.setDefaultImageResId(R.drawable.my_touxiang);
//        mHeadImage.setImageDrawable(getResources().getDrawable(R.drawable.my_touxiang));
        if (Check.hashead(this)) {
            mHeadImage.setImageUrl(PreferencesUtils.getValueFromSPMap(this, PreferencesUtils.Keys.HEAD_URL,"",PreferencesUtils.Keys.USERINFO), mImageLoader);
            mHeadImage.setErrorImageResId(R.drawable.my_touxiang);
        }

        if (Check.hasNickname(this)) {
            mNickName.setText(PreferencesUtils.getValueFromSPMap(this, PreferencesUtils.Keys.NICKNAME,"",PreferencesUtils.Keys.USERINFO));
        } else {
            mNickName.setText(getResources().getText(R.string.no_nickname));
        }


        if(Check.isLogin(InfoActivity.this)){
            mNotLogin.setVisibility(View.GONE);
            mLoginoutMv.setVisibility(View.VISIBLE);
            mLine.setVisibility(View.VISIBLE);
            mLineUp.setVisibility(View.VISIBLE);
        }else{
            mNotLogin.setVisibility(View.VISIBLE);
            mLoginoutMv.setVisibility(View.GONE);
            mLine.setVisibility(View.GONE);
            mLineUp.setVisibility(View.GONE);
        }

    }

    @Override
    protected void initData() {

        link="http://www.liuzhuni.com/app";
        img_link="http://m.liuzhuni.com/huimapp/content/img/icon114.png";
        title=getText(R.string.share_title).toString();
        content=getText(R.string.share_content).toString();
        name=getText(R.string.share_title).toString();
        copy_text="http://www.liuzhuni.com/app";

        mImageLoader = RequestManager.getImageLoader();
    }

    @Override
    protected void findViewById() {

        ViewUtils.inject(this);

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void setListener() {

    }


    @OnClick(R.id.title_left)
    public void toIndex(View v){

        //返回主页面
        finish();
    }
    @OnClick(R.id.title_right_iv)
    public void toSign(View v){

        //签到
        if(!Check.isLogin(this)){//没有登录
            Intent intent=new Intent(this,LoginActivity.class);
            startActivity(intent);

        }else{
            if(dayFlag){
                dayFlag=false;
                pullDayData();
            }


        }

    }


    protected synchronized void pullDayData(){  //签到
        executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.USER_SIGN,BaseModel.class,responseDayListener(),errorDayListener()){

        });
    }

    private Response.Listener<BaseModel> responseDayListener() {
        return new Response.Listener<BaseModel>(){
            @Override
            public void onResponse(BaseModel sign) {
                dayFlag=true;
                if(sign.getRet()==0){

                    ToastUtil.customShow(InfoActivity.this, sign.getMes());

                }else{
                    ToastUtil.customShow(InfoActivity.this, sign.getMes());
                }

            }
        };

    }

    public Response.ErrorListener errorDayListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(activity,getResources().getText(R.string.error_retry), Toast.LENGTH_LONG).show();
                loadingdialog.dismiss();
                dayFlag=true;
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 401) {//重新登录
                        PreferencesUtils.putBooleanToSPMap(InfoActivity.this, PreferencesUtils.Keys.IS_LOGIN, false);
                        PreferencesUtils.clearSPMap(InfoActivity.this, PreferencesUtils.Keys.USERINFO);
                        Intent intent = new Intent(InfoActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        };
    }




    @OnClick(R.id.info_push)
    public void push(View v){

        //推送
        TheIntent theIntent=new TheIntent(this,new Loginable() {
            @Override
            public void intent() {
                Intent intent=new Intent(InfoActivity.this,PushSetActivity.class);
                startActivity(intent);
            }
        });
        theIntent.go();
    }
    @OnClick(R.id.info_share)
    public void share(View v){

        ShareSDK.initSDK(this);
        pullShareData();
    }
    @OnClick(R.id.index_people_say)
    public void news(View v){

        Intent intent = new Intent(this, AllNewsActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.index_select)
    public void select(View v){

        Intent intent = new Intent(this, IndexNewActivity.class);
        startActivity(intent);
        finish();
    }

    protected synchronized void pullShareData() {

        executeRequest(new GsonBaseRequest<BaseModel<ShareModel>>(Request.Method.GET, UrlConfig.GET_SHARE, new TypeToken<BaseModel<ShareModel>>() {
        }.getType(), responseLoginListener(), errorShareListener()) {


        });
    }

    private Response.Listener<BaseModel<ShareModel>> responseLoginListener() {
        return new Response.Listener<BaseModel<ShareModel>>() {
            @Override
            public void onResponse(BaseModel<ShareModel> shareModel) {
                if (shareModel.getRet() == 0&&shareModel.getData()!=null) {
                    link=shareModel.getData().getLink();
                    img_link=shareModel.getData().getImg_link();
                    title=shareModel.getData().getTitle();
                    content=shareModel.getData().getContent();
                    name=shareModel.getData().getName();
                    copy_text=shareModel.getData().getCopy_text();


                }
                shareShow();

            }
        };

    }

    public Response.ErrorListener errorShareListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                shareShow();

            }
        };
    }




    protected void shareShow(){
        //分享

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
                ToastUtil.customShow(InfoActivity.this,getText(R.string.copy_tips));
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

    @OnClick(R.id.info_message)
    public void messageCenter(View v){

        //消息中心

        TheIntent theIntent=new TheIntent(this,new Loginable() {
            @Override
            public void intent() {
                Intent intent=new Intent(InfoActivity.this,MessageCenterActivity.class);
                startActivity(intent);
            }
        });
        theIntent.go();


    }
    @OnClick(R.id.info_feedback)
    public void feedback(View v){

        //反馈
        Intent intent=new Intent(this,FeedbackActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.info_help)
    public void help(View v){

        //帮助
        Intent intent=new Intent(this,HelpActivity.class);
        Bundle goodsBundle = new Bundle();
        goodsBundle.putBoolean("isHelp", true);
        intent.putExtras(goodsBundle);

        startActivity(intent);

    }
    @OnClick(R.id.info_loginout)
    public void loginout(View v){

        //退出登录
        mDialog=new LoginoutDialog(this,null);

        mDialog.mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pullLoginOutData();
                PreferencesUtils.putBooleanToSPMap(InfoActivity.this, PreferencesUtils.Keys.IS_LOGIN, false);
                PreferencesUtils.clearSPMap(InfoActivity.this,PreferencesUtils.Keys.USERINFO);
                Intent intent=new Intent(InfoActivity.this,LoginActivity.class);
                startActivity(intent);
                mDialog.dismiss();
                finish();
            }
        });
        mDialog.mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();


    }

    protected synchronized void pullLoginOutData(){
        executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.LOGOUT,BaseModel.class,responseLoginOutListener(),errorListener()){


        });
    }

    private Response.Listener<BaseModel> responseLoginOutListener() {
        return new Response.Listener<BaseModel>(){
            @Override
            public void onResponse(BaseModel sign) {

            }
        };

    }
    @OnClick(R.id.please_login)
    public void login(View v){

        //登录
        Intent intent=new Intent(InfoActivity.this,LoginActivity.class);
        startActivity(intent);

    }
    @OnClick(R.id.head_info)
    public void headInfo(View v){

        //个人信息
//        Intent intent=new Intent(this,DetailInfoActivity.class);
//        startActivity(intent);

        TheIntent theIntent=new TheIntent(this,new Loginable() {
            @Override
            public void intent() {
                Intent intent=new Intent(InfoActivity.this,DetailInfoActivity.class);
                startActivity(intent);
            }
        });
        theIntent.go();
    }



}
