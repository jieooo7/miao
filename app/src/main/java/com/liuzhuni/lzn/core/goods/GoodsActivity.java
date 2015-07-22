package com.liuzhuni.lzn.core.goods;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.liuzhuni.lzn.config.Check;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.goods.adapter.GoodsAdapter;
import com.liuzhuni.lzn.core.goods.model.GoodsModel;
import com.liuzhuni.lzn.core.goods.model.ShopModel;
import com.liuzhuni.lzn.core.goods.ui.ListViewForScrollView;
import com.liuzhuni.lzn.core.login.LoginActivity;
import com.liuzhuni.lzn.core.model.BaseModel;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonBaseRequest;
import com.liuzhuni.lzn.volley.GsonRequest;
import com.liuzhuni.lzn.volley.RequestManager;

import java.util.Map;

public class GoodsActivity extends Base2Activity {


    private ImageLoader mImageLoader;
    private GoodsAdapter mAdapter;
    private GoodsModel mGoods;

    @ViewInject(R.id.goods_back)
    private TextView mBackTv;
    @ViewInject(R.id.goods_share)
    private ImageView mShareTv;
    @ViewInject(R.id.goods_noti_low)
    private TextView mLowNotifyTv;
    @ViewInject(R.id.goods_to_buy)
    private TextView mToBuyTv;
    @ViewInject(R.id.goods_notify_img)
    private ImageView mLowNotifyIv;
    @ViewInject(R.id.goods_scroll)
    private ScrollView mScrollview;
    @ViewInject(R.id.goods_pic)
    private NetworkImageView mPicIv;
    @ViewInject(R.id.goods_intro)
    private TextView mGoodsNameTv;
    @ViewInject(R.id.price)
    private TextView mNowPriceTv;
    @ViewInject(R.id.goods_shop)
    private TextView mShopTv;
    @ViewInject(R.id.goods_discount)
    private TextView mDiscountTv;
    @ViewInject(R.id.origin_price)
    private TextView mOriginPriceTv;
    @ViewInject(R.id.goods_drop)
    private TextView mDropTv;
    @ViewInject(R.id.low_deep)
    private TextView mDeep;
    @ViewInject(R.id.goods_list)
    private ListViewForScrollView mListView;


    @ViewInject(R.id.goods_shop_ll)
    private LinearLayout mShopLl;
    @ViewInject(R.id.goods_shop_line)
    private TextView mShopLine;

    private String mUrl="";
    private String mTitle="";

    private boolean mIsNoti=true;

    private String code="";
    private String mall="";

    private boolean mIsCode=true;
    private boolean mIsRemind=true;

    private GoodsModel mGoodsScan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);
        findViewById();
        initUI();
        initData();
        setListener();
    }

    @Override
    protected void initData() {

        mImageLoader= RequestManager.getImageLoader();
            if(getIntent()!=null){
                if(getIntent().getExtras()!=null){

                    mIsCode=getIntent().getExtras().getBoolean("isCode");


                    if(mIsCode){

                        mGoodsScan=(GoodsModel)getIntent().getExtras().getSerializable("model");
                        initScan(mGoodsScan);

                    }else{
                        code=getIntent().getExtras().getString("code");
                        mall=getIntent().getExtras().getString("mall");
                        loadingdialog.show();
                        pullGoodsData(code,mall);
                    }

                }
            }
//        mGoods=new GoodsModel();


    }
    protected synchronized void pullCodeGoodsData(final String code){
        executeRequest(new GsonBaseRequest<BaseModel<GoodsModel>>(Request.Method.POST, UrlConfig.SCAN,new TypeToken<BaseModel<GoodsModel>>(){}.getType(),responseGoodsListener(),errorListener()){

            protected Map<String, String> getParams() {
                return new ApiParams().with("code", code);
            }

        });
    }
    protected synchronized void pullGoodsData(final String code,final String mall){
        executeRequest(new GsonBaseRequest<BaseModel<GoodsModel>>(Request.Method.POST, UrlConfig.SCAN_DETAIL,new TypeToken<BaseModel<GoodsModel>>(){}.getType(),responseGoodsListener(),errorListener()){

            protected Map<String, String> getParams() {
                return new ApiParams().with("code", code).with("mall",mall);
            }

        });
    }

    private Response.Listener<BaseModel<GoodsModel>> responseGoodsListener() {
        return new Response.Listener<BaseModel<GoodsModel>>(){
            @Override
            public void onResponse(BaseModel<GoodsModel> goodsModel) {
                loadingdialog.dismiss();
                if(goodsModel.getRet()==0){
                    if(goodsModel.getData()==null){
                        //提示没找到
                        finish();
                        return;
                    }
                    mGoods=goodsModel.getData();
                    mUrl=mGoods.getUrl();
                    mTitle=mGoods.getTitle();
//                    mPicIv.setDefaultImageResId();
//                    mPicIv.setErrorImageResId();
                    mPicIv.setImageUrl(mGoods.getImg(),mImageLoader);
                    mGoodsNameTv.setText(mGoods.getTitle());
                    mNowPriceTv.setText("¥"+mGoods.getPrice());
                    mShopTv.setText(mGoods.getMallname());
                    mDiscountTv.setText(mGoods.getDiscount());
                    mOriginPriceTv.setText(mGoods.getPriceOld());
                    mDropTv.setText(mGoods.getRange());
                    mIsRemind=mGoods.isRemind();
                    if(mGoods.isLowest()){
                        mDeep.setVisibility(View.VISIBLE);
                    }


                    if(!mIsRemind){
                        mLowNotifyTv.setText(getResources().getText(R.string.had_noti_low));
                        mLowNotifyIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_tixing_s));
                    }


                    if(mGoods.getMallprice()!=null){
                        mAdapter=new GoodsAdapter(GoodsActivity.this,mGoods.getMallprice(),mImageLoader);
                        mListView.setAdapter(mAdapter);
                        otherClick();
                    }else{
                        mShopLl.setVisibility(View.GONE);
                        mShopLine.setVisibility(View.GONE);
                    }

                }

            }
        };

    }


    protected void otherClick(){

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                             @Override
                                             public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                                 Object obj = mListView.getItemAtPosition(position);
                                                  if(obj instanceof ShopModel){
                                                      Intent intent = new Intent();
                                                      intent.setClass(GoodsActivity.this, ToBuyActivity.class);
                                                      Bundle bundle=new Bundle();
                                                      bundle.putString("url",((ShopModel) obj).getUrl());
                                                      bundle.putString("title","");
                                                      intent.putExtras(bundle);
                                                      startActivity(intent);
                                                  }
                                             }});
    }

    protected void initScan(GoodsModel model){

        mUrl=model.getUrl();
        mTitle=model.getTitle();
//                    mPicIv.setDefaultImageResId();
//                    mPicIv.setErrorImageResId();
        mPicIv.setImageUrl(model.getImg(),mImageLoader);
        mGoodsNameTv.setText(model.getTitle());
        mNowPriceTv.setText(model.getPrice());
        mShopTv.setText(model.getMallname());
        mDiscountTv.setText(model.getDiscount());
        mOriginPriceTv.setText(model.getPriceOld());
        mDropTv.setText(model.getRange());
        if(model.isLowest()){
            mDeep.setVisibility(View.VISIBLE);
        }

        code=model.getCode();
        mall=model.getMallname();
        mIsRemind=model.isRemind();
        if(!mIsRemind){
            mLowNotifyTv.setText(getResources().getText(R.string.had_noti_low));
            mLowNotifyIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_tixing_s));
        }

//        code=model.get;
//        mall=getIntent().getExtras().getString("mall");

        if(model.getMallprice()!=null){
            mAdapter=new GoodsAdapter(GoodsActivity.this,model.getMallprice(),mImageLoader);
            mListView.setAdapter(mAdapter);
        }else{
            mShopLl.setVisibility(View.GONE);
            mShopLine.setVisibility(View.GONE);
        }
    }

    @Override
    protected void findViewById() {

        ViewUtils.inject(this);

    }

    @Override
    protected void initUI() {

//        mListView.setAdapter(mAdapter);
        mScrollview.scrollTo(0,0);
//        mScrollview.smoothScrollTo(0,0);

    }

    @Override
    protected void setListener() {

    }


    @OnClick(R.id.goods_back)
    public void back(View v){

        finish();
    }

    @OnClick(R.id.goods_to_buy)
    public void buy(View v){

        //购买
        Intent intent = new Intent();
        intent.setClass(this, ToBuyActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("url",mUrl);
        bundle.putString("title",mTitle);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @OnClick(R.id.goods_noti_low)
    public void noti(View v){

        //低价提醒

        if(!Check.isLogin(this)){//没有登录
            Intent intent=new Intent(this,LoginActivity.class);
            this.startActivity(intent);

        }else{
            if(mIsNoti&&mIsRemind){
                mIsNoti=false;
                pullNotiData(code,mall);
            }
        }

    }

    @OnClick(R.id.goods_share)
    public void share(View v){

        //分享

    }


    protected synchronized void pullNotiData(final String pid,final String mall){
        executeRequest(new GsonRequest<BaseModel>(Request.Method.POST, UrlConfig.DROP_NOTI, BaseModel.class, responseNotiListener(), errorListener(false)) {

            protected Map<String, String> getParams() {
                return new ApiParams().with("productid", pid).with("mall", mall);
            }

        });
        mLowNotifyTv.setText(getResources().getText(R.string.had_noti_low));
        mLowNotifyIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_tixing_s));

    }

    private Response.Listener<BaseModel> responseNotiListener() {

        return new Response.Listener<BaseModel>(){
            @Override
            public void onResponse(BaseModel sign) {
                if(sign.getRet()==0){


                }else {

                    ToastUtil.customShow(GoodsActivity.this, sign.getMes());
                }

            }
        };

    }





}
