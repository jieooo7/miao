package com.liuzhuni.lzn.core.html;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.google.gson.reflect.TypeToken;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.Base2Activity;
import com.liuzhuni.lzn.config.Check;
import com.liuzhuni.lzn.config.TypeInt;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.login.LoginActivity;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.pulltorefresh.PullToRefreshBase;
import com.liuzhuni.lzn.pulltorefresh.PullToRefreshWebView;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.volley.GsonBaseRequest;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class HtmlActivity extends Base2Activity {


    private String mUrl="http://www.m.liuzhuni.com/";
    private int mTarget;
    private String mTargetPara;

    private WebView mWebView;
    private PullToRefreshWebView mPullWebView;

    private Handler handle = new Handler();

    private TextView mBackTv;
    private TextView mCloseTv;
    private TextView mTitleTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_html);
        initData();
        findViewById();
        initUI();
        setListener();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initData() {

        if(getIntent()!=null){
            if( getIntent().getExtras()!=null){
                int sdk = Build.VERSION.SDK_INT;
                if(sdk<12){
                    mTarget=getIntent().getExtras().getInt("url");
                }else{
                    mTarget=getIntent().getExtras().getInt("url",-1);
                }


            }
        }

        switch (mTarget){
            case TypeInt.SIGN:
                mTargetPara="sign";

                break;
            case TypeInt.MAKE_CENT:
                mTargetPara="sign";
                break;
            case TypeInt.TOPIC:
                mTargetPara="topic";
                break;
            case TypeInt.GIFT:
                mTargetPara="gift";
                break;
            case TypeInt.TOUCH:
                mTargetPara="touch";

                break;


        }

    }

    @Override
    protected void findViewById() {

        mPullWebView=(PullToRefreshWebView)findViewById(R.id.h5_webview);
        mTitleTv=(TextView)findViewById(R.id.title_middle);
        mBackTv=(TextView)findViewById(R.id.goods_back);
        mCloseTv=(TextView)findViewById(R.id.goods_close);

    }

    @Override
    protected void initUI() {
        mWebView=mPullWebView.getRefreshableView();
        mPullWebView.setPullLoadEnabled(false);
        WebSettings webSettings = mWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);//设置使用够执行JS脚本
        webSettings.setBuiltInZoomControls(true);//设置使支持缩放

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        mWebView.addJavascriptInterface(new JavaScriptInterface(this),
                "JSInterface");


        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);// 使用当前WebView处理跳转
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {

            //获得网页的加载进度，显示在右上角的TextView控件中
            public void onProgressChanged(WebView view, int newProgress) {
            }

            //获得网页的标题，作为应用程序的标题进行显示
            public void onReceivedTitle(WebView view, String title) {
                mTitleTv.setText(title);

            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
                return super.onJsPrompt(view, url, message, defaultValue, result);
            }


        });

        pullData(mTargetPara);

//        mWebView.postUrl();

    }

    @Override
    protected void setListener() {

        mPullWebView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<WebView>(){


            @Override
            public void onPullDownToRefresh(PullToRefreshBase<WebView> refreshView) {


                //刷新

                mWebView.reload();


                handle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullWebView.onPullUpRefreshComplete();
//
                        mPullWebView.onPullDownRefreshComplete();

                    }
                }, 300);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<WebView> refreshView) {

            }


        });

        mCloseTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
        mBackTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(mWebView.canGoBack()){
                    mWebView.goBack();
                    if(mCloseTv.getVisibility()==View.GONE){
                        mCloseTv.setVisibility(View.VISIBLE);
                    }
                }else{
                    finish();
                }

            }
        });



    }


    public class JavaScriptInterface {
        private Activity activity;

        public JavaScriptInterface(Activity activity) {
            this.activity = activity;
        }

        @JavascriptInterface
        public void closeApp() {
            activity.finish();
        }

        @JavascriptInterface
        public void runShareTimeline(String title,String desc,String link,String imgUrl) {
            shareWeixin(title,desc,link,imgUrl, Wechat.NAME);
        }

        @JavascriptInterface
        public void runShareFriends(String title,String desc,String link,String imgUrl) {
            shareWeixin(title, desc, link, imgUrl, WechatMoments.NAME);
        }


        @JavascriptInterface
        public void runJumpStation(final String href) {

            if(href.startsWith("huim")){
                Uri uri=Uri.parse(href);

                Intent contentIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(contentIntent);
            }
        }


        @JavascriptInterface
        public void  openLoginView(final String href) {
            if (!Check.isLogin(HtmlActivity.this)) {//没有登录
                Intent intent = new Intent(HtmlActivity.this, LoginActivity.class);
                startActivity(intent);

            } else {
               //赚积分

            }


        }



        protected void shareWeixin(String title,String desc,String link,String imgUrl,String plat) {
            ShareSDK.initSDK(HtmlActivity.this);
            Platform.ShareParams sp = new Platform.ShareParams();

            sp.setTitle(title);
            // text是分享文本，所有平台都需要这个字段
            sp.setText(desc);
            sp.setImageUrl(imgUrl);
            // url仅在微信（包括好友和朋友圈）中使用
            sp.setUrl(link);
            sp.setShareType(Platform.SHARE_WEBPAGE);


            Platform weixin = ShareSDK.getPlatform(HtmlActivity.this, plat);
            weixin.setPlatformActionListener(new PlatformActionListener() {

                public void onError(Platform platform, int action, Throwable t) {
                    // 操作失败的处理代码
                    ToastUtil.customShow(HtmlActivity.this, "分享失败");
                }

                @Override
                public void onComplete(Platform platform, int i, HashMap<String, Object> stringObjectHashMap) {

                    ToastUtil.customShow(HtmlActivity.this, "分享成功");

                }

                public void onCancel(Platform platform, int action) {
                    // 操作取消的处理代码
                }

            });

            weixin.share(sp);

        }

        @JavascriptInterface
        public void showImage(final String imagePath) {
//            Intent i = new Intent(activity, ImageActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString(IMAGE_PATH_KEY, imagePath);
//            i.putExtras(bundle);
//            activity.startActivity(i);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.stopLoading();
    }



    protected synchronized void pullData(String target) {

        executeRequest(new GsonBaseRequest<BaseModel<String>>(Request.Method.GET, UrlConfig.GET_HTML+"?target="+target, new TypeToken<BaseModel<String>>() {
        }.getType(), responseProfileListener(), errorListener()) {


        });
    }

    private Response.Listener<BaseModel<String>> responseProfileListener() {
        return new Response.Listener<BaseModel<String>>() {
            @Override
            public void onResponse(BaseModel<String> shareModel) {
                if (shareModel.getRet() == 0) {
                    if(!TextUtils.isEmpty(shareModel.getData())){

                        mWebView.loadUrl(shareModel.getData());

                    }


                }

            }
        };

    }

//java 调用js
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (keyCode == KeyEvent.KEYCODE_BACK) {
//                webview.loadUrl("javascript: window.backAction();");
//                return true;
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }


//js 调用 java
//    String onClick1 = "onclick=\"javascript: window.JSInterface.showImage('htmlsample/'+"
//            + "$(this).attr('src')" + ");\"";
}
