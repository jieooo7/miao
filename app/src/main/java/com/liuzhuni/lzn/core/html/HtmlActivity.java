package com.liuzhuni.lzn.core.html;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.Base2Activity;

public class HtmlActivity extends Base2Activity {


    private String mUrl;
    private WebView mWebView;

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
                    mUrl=getIntent().getExtras().getString("url");
                }else{
                    mUrl=getIntent().getExtras().getString("url","http://www.m.liuzhuni.com/");
                }
            }
        }

    }

    @Override
    protected void findViewById() {

        mWebView=(WebView)findViewById(R.id.h5_webview);

    }

    @Override
    protected void initUI() {

        WebSettings webSettings = mWebView.getSettings();

        webSettings.setJavaScriptEnabled(true);//设置使用够执行JS脚本
        webSettings.setBuiltInZoomControls(true);//设置使支持缩放

        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        mWebView.addJavascriptInterface(new JavaScriptInterface(this),
                "JSInterface");


        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                view.loadUrl(url);// 使用当前WebView处理跳转
                return super.shouldOverrideUrlLoading(view,url);
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

        mWebView.setWebChromeClient(new WebChromeClient(){

            //获得网页的加载进度，显示在右上角的TextView控件中
            public void onProgressChanged(WebView view, int newProgress) {
            }

            //获得网页的标题，作为应用程序的标题进行显示
            public void onReceivedTitle(WebView view, String title) {

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


        mWebView.loadUrl(mUrl);

    }

    @Override
    protected void setListener() {

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
        public void showImage(final String imagePath) {
//            Intent i = new Intent(activity, ImageActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString(IMAGE_PATH_KEY, imagePath);
//            i.putExtras(bundle);
//            activity.startActivity(i);
        }
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
