package com.liuzhuni.lzn.core.personInfo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.Base2Activity;

public class HelpActivity extends Base2Activity {


    @ViewInject(R.id.title_left)
    private TextView mBackTv;
    @ViewInject(R.id.title_right)
    private TextView mRightTv;
    @ViewInject(R.id.title_middle)
    private TextView mMiddleTv;
    @ViewInject(R.id.help_web)
    private WebView mWebView;

    private boolean mIsHelp=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        initData();
        findViewById();
        initUI();
        setListener();
    }

    @Override
    protected void initData() {

        if(getIntent()!=null){
            if(getIntent().getExtras()!=null){
                mIsHelp=getIntent().getExtras().getBoolean("isHelp");
            }}

    }

    @Override
    protected void findViewById() {

        ViewUtils.inject(this);

    }

    @Override
    protected void initUI() {

        if(mIsHelp){
            mRightTv.setVisibility(View.GONE);
            mMiddleTv.setText(getText(R.string.use_help_center));

        }else{
            mBackTv.setVisibility(View.GONE);
            mRightTv.setText(getText(R.string.i_known));
            mMiddleTv.setText(getText(R.string.use_help));
        }


        mWebView.getSettings().setJavaScriptEnabled(true);//设置使用够执行JS脚本
        mWebView.getSettings().setBuiltInZoomControls(true);//设置使支持缩放
        if(mIsHelp){
            mWebView.loadUrl("http://m.liuzhuni.com/huimapp/help.html");
        }else{
            mWebView.loadUrl("http://m.liuzhuni.com/huimapp/codehelp.html");
        }


        mWebView.setWebChromeClient(new WebChromeClient(){

            //获得网页的加载进度，显示在右上角的TextView控件中
            public void onProgressChanged(WebView view, int newProgress) {
            }

            //获得网页的标题，作为应用程序的标题进行显示
            public void onReceivedTitle(WebView view, String title) {
            }


        });

        mWebView.setWebViewClient(new WebViewClient(){


            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                            view.loadUrl(url);// 使用当前WebView处理跳转
                             return true;
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





    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.title_right)
    public void back(View v){

        finish();
    }
    @OnClick(R.id.title_left)
    public void backLeft(View v){

        finish();
    }


}
