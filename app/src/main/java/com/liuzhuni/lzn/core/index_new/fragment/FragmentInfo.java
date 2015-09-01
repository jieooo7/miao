package com.liuzhuni.lzn.core.index_new.fragment;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.liuzhuni.lzn.base.BaseFragment;
import com.liuzhuni.lzn.config.Check;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.index_new.Shareable;
import com.liuzhuni.lzn.core.index_new.SignDayable;
import com.liuzhuni.lzn.core.index_new.model.ProfileModel;
import com.liuzhuni.lzn.core.index_new.model.ShareFirstModel;
import com.liuzhuni.lzn.core.login.LoginActivity;
import com.liuzhuni.lzn.core.login.Loginable;
import com.liuzhuni.lzn.core.login.TheIntent;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.core.personInfo.DetailInfoActivity;
import com.liuzhuni.lzn.core.personInfo.FeedbackActivity;
import com.liuzhuni.lzn.core.personInfo.HelpActivity;
import com.liuzhuni.lzn.core.personInfo.MessageCenterActivity;
import com.liuzhuni.lzn.core.personInfo.PushSetActivity;
import com.liuzhuni.lzn.core.personInfo.model.ShareModel;
import com.liuzhuni.lzn.core.personInfo.ui.LoginoutDialog;
import com.liuzhuni.lzn.core.personInfo.ui.MeView;
import com.liuzhuni.lzn.utils.PreferencesUtils;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonBaseRequest;
import com.liuzhuni.lzn.volley.GsonRequest;
import com.liuzhuni.lzn.volley.RequestManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Andrew Lee on 2015/7/31.
 * E-mail:jieooo7@163.com
 * Date: 2015-07-31
 * Time: 12:01
 */
public class FragmentInfo extends BaseFragment implements SignDayable {

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


    @ViewInject(R.id.cent)
    private TextView mCents;
    @ViewInject(R.id.sign_days)
    private TextView mSignDays;


    private String link;
    private String img_link;
    private String title;
    private String content;
    private String name;
    private String copy_text;

    private boolean isClick = true;

    private boolean dayFlag = true;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd");

    //        链接 link  图片地址 img_link 标题 title 内容 content 网站名称 name  剪贴板文字 copy_text


    private LoginoutDialog mDialog;

    private ImageLoader mImageLoader;

    private Shareable shareListener = null;

    private Handler mHandler = new Handler();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_info,
                container, false);

        ViewUtils.inject(this, view);

        initData();

        return view;
    }


    protected void initData() {

        link = "http://www.liuzhuni.com/app";
        img_link = "http://m.liuzhuni.com/huimapp/content/img/icon114.png";
        title = getText(R.string.share_title).toString();
        content = getText(R.string.share_content).toString();
        name = getText(R.string.share_title).toString();
        copy_text = "http://www.liuzhuni.com/app";

        mImageLoader = RequestManager.getImageLoader();

        if (!Check.isFirstShared(getCustomActivity())) {//没有分享过
            mShareMv.hiddenRedDot(true);
            mShareMv.hiddenRightTv(true);
        } else {
            mShareMv.hiddenRedDot(false);
            mShareMv.hiddenRightTv(false);
        }

        checkShareed();
    }


    private void checkShareed() {

        pullShareCheckData();
    }


    @Override
    public void onAttach(Activity activity) {

        try {
            if (activity instanceof Shareable) {

                shareListener = (Shareable) activity;
            }
        } catch (Exception e) {
            throw new ClassCastException(this.toString() + " must implementOnMainListener");
        }
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();


        mLevel.setText(PreferencesUtils.getValueFromSPMap(getCustomActivity(), PreferencesUtils.Keys.LEVEL, getString(R.string.defalt_level), PreferencesUtils.Keys.USERINFO));
        mCents.setText(PreferencesUtils.getValueFromSPMap(getCustomActivity(), PreferencesUtils.Keys.POINTS, "", PreferencesUtils.Keys.USERINFO));
        mSignDays.setText(PreferencesUtils.getValueFromSPMap(getCustomActivity(), PreferencesUtils.Keys.SIGN_DAYS, getString(R.string.sign_no_every), PreferencesUtils.Keys.USERINFO));


        mMessageMv.setNum(PreferencesUtils.getIntFromSPMap(getCustomActivity(), PreferencesUtils.Keys.UN_READ, PreferencesUtils.Keys.USERINFO));

        mHeadImage.setDefaultImageResId(R.drawable.my_touxiang);
//        mHeadImage.setImageDrawable(getResources().getDrawable(R.drawable.my_touxiang));
        if (Check.hashead(getCustomActivity())) {
            mHeadImage.setImageUrl(PreferencesUtils.getValueFromSPMap(getCustomActivity(), PreferencesUtils.Keys.HEAD_URL, "", PreferencesUtils.Keys.USERINFO), mImageLoader);
            mHeadImage.setErrorImageResId(R.drawable.my_touxiang);
        }

        if (Check.hasNickname(getCustomActivity())) {
            mNickName.setText(PreferencesUtils.getValueFromSPMap(getCustomActivity(), PreferencesUtils.Keys.NICKNAME, "", PreferencesUtils.Keys.USERINFO));
        } else {
            mNickName.setText("未设置");
        }


        if (Check.isLogin(getCustomActivity())) {
            mNotLogin.setVisibility(View.GONE);
            mLoginoutMv.setVisibility(View.VISIBLE);
            mLine.setVisibility(View.VISIBLE);
            mLineUp.setVisibility(View.VISIBLE);
            pullProfileData();//获取个人信息
        } else {
            mNotLogin.setVisibility(View.VISIBLE);
            mLoginoutMv.setVisibility(View.GONE);
            mLine.setVisibility(View.GONE);
            mLineUp.setVisibility(View.GONE);
        }

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
//        if(getCustomActivity()!=null){
//
//            ToastUtil.show(getCustomActivity(),""+hidden);
//        }

        if (!hidden) {//hidden为true时 离开  false 进入页面时执行
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLevel.setText(PreferencesUtils.getValueFromSPMap(getCustomActivity(), PreferencesUtils.Keys.LEVEL, getString(R.string.defalt_level), PreferencesUtils.Keys.USERINFO));
                    mCents.setText(PreferencesUtils.getValueFromSPMap(getCustomActivity(), PreferencesUtils.Keys.POINTS, "", PreferencesUtils.Keys.USERINFO));
                    mSignDays.setText(PreferencesUtils.getValueFromSPMap(getCustomActivity(), PreferencesUtils.Keys.SIGN_DAYS, getString(R.string.sign_every), PreferencesUtils.Keys.USERINFO));


                }
            }, 500);

        }


        super.onHiddenChanged(hidden);
    }

    @OnClick(R.id.info_push)
    public void push(View v) {

        //推送
        TheIntent theIntent = new TheIntent(getCustomActivity(), new Loginable() {
            @Override
            public void intent() {
                Intent intent = new Intent(getCustomActivity(), PushSetActivity.class);
                startActivity(intent);
            }
        });
        theIntent.go();
    }

    @OnClick(R.id.info_share)
    public void share(View v) {

        ShareSDK.initSDK(getCustomActivity());
        Timer time = new Timer();
        if (isTouch && isClick) {
            isClick = false;
            isTouch = false;
            pullShareData();
        }

        time.schedule(new TimerTask() {
            @Override
            public void run() {
                isClick = true;
            }
        }, 800);
    }


    protected synchronized void pullShareSuccessData() {

        executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.SUCCESS_SHARE, BaseModel.class, responseShareSuccessListener(), errorListener()) {
            protected Map<String, String> getParams() {
                return new ApiParams().with("type", "app").with("data","");
            }


        });
    }

    private Response.Listener<BaseModel> responseShareSuccessListener() {
        return new Response.Listener<BaseModel>() {
            @Override
            public void onResponse(BaseModel shareModel) {
                if (shareModel.getRet() == 0) {
                    ToastUtil.customShow(getCustomActivity(), shareModel.getMes());
                    mShareMv.hiddenRedDot(false);
                    mShareMv.hiddenRightTv(false);
                    if (shareListener != null) {
                        shareListener.shared();
                    }
                    PreferencesUtils.putBooleanToSPMap(getCustomActivity(), PreferencesUtils.Keys.IS_FIRST_SHARE, true, PreferencesUtils.Keys.USERINFO);

                }

            }
        };

    }

    protected synchronized void pullShareCheckData() {

        executeRequest(new GsonBaseRequest<BaseModel<ShareFirstModel>>(Request.Method.POST, UrlConfig.IS_SHARE, new TypeToken<BaseModel<ShareFirstModel>>() {
        }.getType(), responseShareCheckListener(), errorListener()) {


        });
    }

    private Response.Listener<BaseModel<ShareFirstModel>> responseShareCheckListener() {
        return new Response.Listener<BaseModel<ShareFirstModel>>() {
            @Override
            public void onResponse(BaseModel<ShareFirstModel> shareModel) {
                if (shareModel.getRet() == 0 && shareModel.getData() != null) {
                    if (shareModel.getData().isshare()) {

                        mShareMv.hiddenRedDot(false);
                        mShareMv.hiddenRightTv(false);
                        PreferencesUtils.putBooleanToSPMap(getCustomActivity(), PreferencesUtils.Keys.IS_FIRST_SHARE, true, PreferencesUtils.Keys.USERINFO);
                    }

                }

            }
        };

    }

    protected synchronized void pullProfileData() {

        executeRequest(new GsonBaseRequest<BaseModel<ProfileModel>>(Request.Method.GET, UrlConfig.GET_PROFILE, new TypeToken<BaseModel<ProfileModel>>() {
        }.getType(), responseProfileListener(), errorListener()) {


        });
    }

    private Response.Listener<BaseModel<ProfileModel>> responseProfileListener() {
        return new Response.Listener<BaseModel<ProfileModel>>() {
            @Override
            public void onResponse(BaseModel<ProfileModel> shareModel) {
                if (shareModel.getRet() == 0 && shareModel.getData() != null) {

                    final ProfileModel profile = shareModel.getData();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mLevel.setText("Lv." + profile.getGrade());
                            mCents.setText("积分 " + profile.getJifen() + " / 金币 " + profile.getLzb());
                            mSignDays.setText(profile.getSign());
                            PreferencesUtils.putValueToSPMap(getCustomActivity(), PreferencesUtils.Keys.LEVEL, "Lv." + profile.getGrade(), PreferencesUtils.Keys.USERINFO);
                            PreferencesUtils.putValueToSPMap(getCustomActivity(), PreferencesUtils.Keys.POINTS, "积分 " + profile.getJifen() + " / 金币 " + profile.getLzb(), PreferencesUtils.Keys.USERINFO);
                            PreferencesUtils.putValueToSPMap(getCustomActivity(), PreferencesUtils.Keys.SIGN_DAYS, profile.getSign(), PreferencesUtils.Keys.USERINFO);
                        }
                    });

                }

            }
        };

    }

    public Response.ErrorListener errorShareListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isTouch = true;
//                shareShow();

                shareWeixin();
            }
        };
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
                isTouch = true;
                if (shareModel.getRet() == 0 && shareModel.getData() != null) {
                    link = shareModel.getData().getLink();
                    img_link = shareModel.getData().getImg_link();
                    title = shareModel.getData().getTitle();
                    content = shareModel.getData().getContent();
                    name = shareModel.getData().getName();
                    copy_text = shareModel.getData().getCopy_text();


                }
//                shareShow();

                shareWeixin();
            }
        };

    }


    protected void shareWeixin() {
        Platform.ShareParams sp = new Platform.ShareParams();

        sp.setTitle(title);
        // text是分享文本，所有平台都需要这个字段
        sp.setText(content);
        sp.setImageUrl(img_link);
        // url仅在微信（包括好友和朋友圈）中使用
        sp.setUrl(link);
        sp.setShareType(Platform.SHARE_WEBPAGE);


        Platform weixin = ShareSDK.getPlatform(getCustomActivity(), WechatMoments.NAME);
        weixin.setPlatformActionListener(new PlatformActionListener() {

            public void onError(Platform platform, int action, Throwable t) {
                // 操作失败的处理代码
                ToastUtil.customShow(getCustomActivity(), "分享失败");
            }

            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> stringObjectHashMap) {

                //网络请求之后

                if (!Check.isFirstShared(getCustomActivity())) {
//                    mShareMv.hiddenRedDot(false);
//                    mShareMv.hiddenRightTv(false);
//                    PreferencesUtils.putBooleanToSPMap(getCustomActivity(), PreferencesUtils.Keys.IS_FIRST_SHARE, true, PreferencesUtils.Keys.USERINFO);

                    pullShareSuccessData();

                }

            }

            public void onCancel(Platform platform, int action) {
                // 操作取消的处理代码
            }

        });

        weixin.share(sp);

    }


    protected void shareShow() {
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
                new ShareContentCustomizeCallback() {

                    @Override
                    public void onShare(Platform platform, Platform.ShareParams paramsToShare) {
                        if (SinaWeibo.NAME.equals(platform.getName())) {
                            paramsToShare.setText(content + link);
                        }
                    }
                });


        Bitmap enableLogo = BitmapFactory.decodeResource(getResources(), R.drawable.share_copy_n);
        Bitmap disableLogo = BitmapFactory.decodeResource(getResources(), R.drawable.share_copy_s);
        String label = getString(R.string.copy_link);
        View.OnClickListener listener = new View.OnClickListener() {
            public void onClick(View v) {
                copyToClipboard(copy_text);
                ToastUtil.customShow(getCustomActivity(), getText(R.string.copy_tips));
            }
        };
        oks.setCustomerLogo(enableLogo, disableLogo, label, listener);

// 启动分享GUI
        oks.show(getCustomActivity());


    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    private void copyToClipboard(String str) {//复制剪切板
        int sdk = Build.VERSION.SDK_INT;
        if (sdk < Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getCustomActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(str);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getCustomActivity().getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("text label", str);
            clipboard.setPrimaryClip(clip);
        }
    }

    @OnClick(R.id.info_message)
    public void messageCenter(View v) {

        //消息中心

        TheIntent theIntent = new TheIntent(getCustomActivity(), new Loginable() {
            @Override
            public void intent() {
                Intent intent = new Intent(getCustomActivity(), MessageCenterActivity.class);
                startActivity(intent);
            }
        });
        theIntent.go();


    }

    @OnClick(R.id.info_feedback)
    public void feedback(View v) {

        //反馈
        Intent intent = new Intent(getCustomActivity(), FeedbackActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.sign_days)
    public void sign(View v) {


        Date date = new Date();
        String time = mDateFormat.format(date);
        String oldTime = PreferencesUtils.getValueFromSPMap(getCustomActivity(), PreferencesUtils.Keys.TODAY, "1", PreferencesUtils.Keys.USERINFO);


        //签到
        if (!Check.isLogin(getCustomActivity())) {//没有登录
            Intent intent = new Intent(getCustomActivity(), LoginActivity.class);
            startActivity(intent);

        } else {


            if (!oldTime.equals(time)) {
                //签到

                if (dayFlag) {
                    dayFlag = false;
                    pullDayData();
                }
            } else {
                //其他页面


            }
        }

    }


    protected void pullDayData() {  //签到

        executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.USER_SIGN, BaseModel.class, responseDayListener(), errorDayListener()) {

        });
    }

    public Response.ErrorListener errorDayListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(activity,getResources().getText(R.string.error_retry), Toast.LENGTH_LONG).show();
                loadingdialog.dismiss();
                dayFlag = true;
                if (error.networkResponse != null) {
                    if (error.networkResponse.statusCode == 401) {//重新登录
                        PreferencesUtils.putBooleanToSPMap(getCustomActivity(), PreferencesUtils.Keys.IS_LOGIN, false);
                        PreferencesUtils.clearSPMap(getCustomActivity(), PreferencesUtils.Keys.USERINFO);
                        Intent intent = new Intent(getCustomActivity(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }
        };
    }

    private Response.Listener<BaseModel> responseDayListener() {
        return new Response.Listener<BaseModel>() {
            @Override
            public void onResponse(BaseModel sign) {
                dayFlag = true;
                ToastUtil.customShow(getCustomActivity(), sign.getMes());
                if (sign.getRet() == 0) {//奖励积分时候
                    pullProfileData();
                }
                Date date = new Date();
                String time = mDateFormat.format(date);
                PreferencesUtils.putValueToSPMap(getCustomActivity(), PreferencesUtils.Keys.TODAY, time, PreferencesUtils.Keys.USERINFO);


            }
        };

    }


    @OnClick(R.id.info_help)
    public void help(View v) {

        //帮助
        Intent intent = new Intent(getCustomActivity(), HelpActivity.class);
        Bundle goodsBundle = new Bundle();
        goodsBundle.putBoolean("isHelp", true);
        intent.putExtras(goodsBundle);

        startActivity(intent);

    }

    @OnClick(R.id.info_loginout)
    public void loginout(View v) {

        //退出登录
        mDialog = new LoginoutDialog(getCustomActivity(), null);

        mDialog.mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pullLoginOutData();
                PreferencesUtils.putBooleanToSPMap(getCustomActivity(), PreferencesUtils.Keys.IS_LOGIN, false);
                PreferencesUtils.clearSPMap(getCustomActivity(), PreferencesUtils.Keys.USERINFO);
                Intent intent = new Intent(getCustomActivity(), LoginActivity.class);
                startActivity(intent);
                mDialog.dismiss();
//                finish();
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

    protected synchronized void pullLoginOutData() {
        executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.LOGOUT, BaseModel.class, responseLoginOutListener(), errorListener()) {


        });
    }

    private Response.Listener<BaseModel> responseLoginOutListener() {
        return new Response.Listener<BaseModel>() {
            @Override
            public void onResponse(BaseModel sign) {

            }
        };

    }

    @OnClick(R.id.please_login)
    public void login(View v) {

        //登录
        Intent intent = new Intent(getCustomActivity(), LoginActivity.class);
        startActivity(intent);

    }


    @OnClick(R.id.sign_days)
    public void cent(View v) {

        //积分页面


    }

    @OnClick(R.id.info_profile)
    public void headInfo(View v) {

        //个人信息
//        Intent intent=new Intent(this,DetailInfoActivity.class);
//        startActivity(intent);

        TheIntent theIntent = new TheIntent(getCustomActivity(), new Loginable() {
            @Override
            public void intent() {
                Intent intent = new Intent(getCustomActivity(), DetailInfoActivity.class);
                startActivity(intent);
            }
        });
        theIntent.go();
    }


    @Override
    public void sign() {
        pullProfileData();
    }
}
