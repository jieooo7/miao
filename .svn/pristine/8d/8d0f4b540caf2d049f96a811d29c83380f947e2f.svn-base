package com.liuzhuni.lzn.core.goods;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.Base2Activity;
import com.liuzhuni.lzn.utils.DensityUtil;

public class ToBuyActivity extends Base2Activity {


    @ViewInject(R.id.to_buy_close)
    private ImageView mBackTv;
    @ViewInject(R.id.to_buy_title)
    private TextView mTitleTv;


    @ViewInject(R.id.web_progress)
    private TextView mProgressTv;


    @ViewInject(R.id.to_buy_back)
    private ImageView mBack;
    @ViewInject(R.id.to_buy_forward)
    private ImageView mForward;
    @ViewInject(R.id.to_buy_browser)
    private ImageView mBrowser;
    @ViewInject(R.id.to_buy_refresh)
    private ImageView mRefresh;
    @ViewInject(R.id.to_buy_webview)
    private WebView mWebView;



    private String mUrl="";
    private String mTitle="";

    private int screenWidth=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_buy);
        initData();
        findViewById();
        initUI();
        setListener();
    }

    @Override
    protected void initData() {

        if(getIntent()!=null){
            if( getIntent().getExtras()!=null){
                mUrl=getIntent().getExtras().getString("url");
                mTitle=getIntent().getExtras().getString("title","");
            }
        }

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;



    }

    @Override
    protected void findViewById() {

        ViewUtils.inject(this);

    }

    @Override
    protected void initUI() {

        mTitleTv.setText(mTitle);

        mWebView.getSettings().setJavaScriptEnabled(true);//设置使用够执行JS脚本
        mWebView.getSettings().setBuiltInZoomControls(true);//设置使支持缩放

        mWebView.setWebViewClient(new WebViewClient(){

            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);// 使用当前WebView处理跳转
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressTv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                mProgressTv.setVisibility(View.GONE);

            }
        });

        mWebView.setWebChromeClient(new WebChromeClient(){

            //获得网页的加载进度，显示在右上角的TextView控件中
            public void onProgressChanged(WebView view, int newProgress) {
                LinearLayout.LayoutParams rp= new LinearLayout.LayoutParams((int)(newProgress/100.0*screenWidth),DensityUtil.dip2px(ToBuyActivity.this, 4));
                mProgressTv.setLayoutParams(rp);
            }

            //获得网页的标题，作为应用程序的标题进行显示
            public void onReceivedTitle(WebView view, String title) {

                mTitleTv.setText(title);
            }


        });


        mWebView.loadUrl(mUrl);

    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.to_buy_close)
    public void back(View v){

        finish();
    }
    @OnClick(R.id.to_buy_back)
    public void goBack(View v){

        if(mWebView.canGoBack()){

            mWebView.goBack();
        }
    }
    @OnClick(R.id.to_buy_forward)
    public void goForward(View v){

        if(mWebView.canGoForward()){

            mWebView.goForward();
        }
    }
//    @OnClick({R.id.to_buy_refresh,R.id.to_buy_forward})
    @OnClick(R.id.to_buy_refresh)
    public void refresh(View v){
        mWebView.reload();
    }
    @OnClick(R.id.to_buy_browser)
    public void browser(View v){

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(mWebView.getUrl()));
        startActivity(intent);


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWebView.stopLoading();
    }
}
