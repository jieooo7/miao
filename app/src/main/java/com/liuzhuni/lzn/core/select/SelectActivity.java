package com.liuzhuni.lzn.core.select;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.reflect.TypeToken;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.Base2Activity;
import com.liuzhuni.lzn.config.MessageWhat;
import com.liuzhuni.lzn.config.UrlConfig;
import com.liuzhuni.lzn.core.goods.GoodsActivity;
import com.liuzhuni.lzn.core.goods.ui.ListViewForScrollView;
import com.liuzhuni.lzn.core.login.ButtonThread;
import com.liuzhuni.lzn.core.login.Threadable;
import com.liuzhuni.lzn.core.model.BaseListModel;
import com.liuzhuni.lzn.core.select.adapter.BrandAdapter;
import com.liuzhuni.lzn.core.select.adapter.PriceAdapter;
import com.liuzhuni.lzn.core.select.model.BrandModel;
import com.liuzhuni.lzn.core.select.model.PriceModel;
import com.liuzhuni.lzn.core.select.ui.CleanableEditText;
import com.liuzhuni.lzn.core.select.ui.RangeSeekBar;
import com.liuzhuni.lzn.core.siri.TextSiriActivity;
import com.liuzhuni.lzn.db.DbModel;
import com.liuzhuni.lzn.example.qr_codescan.MipcaActivityCapture;
import com.liuzhuni.lzn.utils.ToastUtil;
import com.liuzhuni.lzn.volley.ApiParams;
import com.liuzhuni.lzn.volley.GsonBaseRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SelectActivity extends Base2Activity {

    @ViewInject(R.id.title_left)
    private TextView mBackTv;
    @ViewInject(R.id.title_right_iv)
    private ImageView mRightIv;
    @ViewInject(R.id.title_middle)
    private TextView mMiddleTv;
    @ViewInject(R.id.want_bu_clean_et)
    private CleanableEditText mInputEt;
    @ViewInject(R.id.select_ok_submit)
    private TextView mSubmitTv;
    @ViewInject(R.id.select_tag)
    private TextView mTagTv;
    @ViewInject(R.id.no_brand_iv)
    private ImageView mNoBrandIv;
    @ViewInject(R.id.select_brand_iv)
    private ImageView mSelectBrandIv;
    @ViewInject(R.id.just_cheap_iv)
    private ImageView mJustCheapIv;
    @ViewInject(R.id.set_range_iv)
    private ImageView mSetRangeIv;


    private boolean mIsBrand = true;
    private boolean mIsPrice = true;
    private boolean mIsLessBrand = true;

    private String mKey = "";
    private String mBrand = "";
    private String mPrice = "";

    private String mCurrentText = "";


    @ViewInject(R.id.brand_select_lv)
    private LinearLayout mBrandLv;
    @ViewInject(R.id.range_price_lv)
    private LinearLayout mRangeLv;
    @ViewInject(R.id.brand_list)
    private ListViewForScrollView mBrandList;
    @ViewInject(R.id.price_list)
    private ListViewForScrollView mPriceListView;
    @ViewInject(R.id.want_buy_sv)
    private ScrollView mScrollView;

    @ViewInject(R.id.more_brand_tv)
    private TextView mMoreBrandTv;
    @ViewInject(R.id.less_brand_tv)
    private TextView mLessBrandTv;
    @ViewInject(R.id.range_show_tv)
    private TextView mRangeShowTv;
    @ViewInject(R.id.seek_bar)
    private RangeSeekBar<Integer> mSeekBar;

    private BrandAdapter mAdapter;
    private List<BrandModel> mList = null;


    private PriceAdapter mPriceAdapter;
    private List<PriceModel> mPriceList = null;

    private boolean fromIndex = false;
    private boolean brandThreadFlag = true;
    public static boolean sPriceFlag = true;

    private boolean isClick=true;


    private Drawable mDrawableSelect;
    private Drawable mDrawableUnSelect;
    private ButtonThread mThread = null;
    private final static int SCANNIN_GREQUEST_CODE = 2;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case MessageWhat.BRAND:
                    if (!mIsBrand) {
                        mIsBrand = !mIsBrand;
                        mNoBrandIv.setImageDrawable(mDrawableSelect);
                        mSelectBrandIv.setImageDrawable(mDrawableUnSelect);
                        mBrandLv.setVisibility(View.GONE);



//                        if(mList!=null){
//                            mList.clear();
//                        }
                    }

                    if (!mIsPrice) {
                        mIsPrice = true;
                        mJustCheapIv.setImageDrawable(mDrawableSelect);
                        mSetRangeIv.setImageDrawable(mDrawableUnSelect);

                        sPriceFlag = true;

                        mRangeLv.setVisibility(View.GONE);
                    }
                    break;
            }
        }


    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        initData();
        findViewById();
        initUI();
        setListener();
    }

    @Override
    protected void initData() {
        mList = new ArrayList<BrandModel>();
        mDrawableSelect = getResources().getDrawable(R.drawable.woxiangmai_option_true);
        mDrawableUnSelect = getResources().getDrawable(R.drawable.woxiangmai_option_false);

        if (getIntent() != null) {
            if (getIntent().getExtras() != null) {
                fromIndex = getIntent().getExtras().getBoolean("fromIndex");
            }
        }


    }

    @Override
    protected void findViewById() {
        ViewUtils.inject(this);

    }

    protected void startThread() {
        mThread = new ButtonThread(mSubmitTv, new Threadable() {
            @Override
            public boolean isSubmit() {
                return SelectActivity.this.isSubmit();
            }
        });
        mThread.start();

    }

    protected void brandThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (brandThreadFlag) {
                    if (!TextUtils.isEmpty(mCurrentText) && !mCurrentText.equals(mInputEt.getText().toString().trim())) {
                        mHandler.sendEmptyMessage(MessageWhat.BRAND);

                    }
                    try {
                        Thread.currentThread().sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }).start();

    }

    @Override
    protected void initUI() {

        mMiddleTv.setText(getResources().getText(R.string.i_want_buy));
        mBackTv.setText(getText(R.string.i_want_back));
        mRightIv.setVisibility(View.GONE);
        mRightIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_saoyisao));
        mScrollView.scrollTo(0, 0);
//        mAdapter = new BrandAdapter(this, mList);
//        mBrandList.setAdapter(mAdapter);

    }

    @Override
    protected void setListener() {

    }


    protected boolean isSubmit() {
        String text = mInputEt.getText().toString().trim();

        return text.length() > 0;
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mThread == null) {
            startThread();
        }
        brandThread();

    }

    @Override
    protected void onPause() {
        super.onPause();
        mThread.stopThread();
        brandThreadFlag = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @OnClick(R.id.title_left)
    public void back(View v) {

        finish();
    }

    @OnClick(R.id.more_brand_tv)
    public void moreBrand(View v) {

        if (mIsLessBrand) {
            mIsLessBrand = false;
            mMoreBrandTv.setVisibility(View.GONE);
            mLessBrandTv.setVisibility(View.VISIBLE);
            mAdapter = new BrandAdapter(SelectActivity.this, mList);
            mBrandList.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

    }

    @OnClick(R.id.less_brand_tv)
    public void lessBrand(View v) {
        if (!mIsLessBrand) {
            mIsLessBrand = true;
            mMoreBrandTv.setVisibility(View.VISIBLE);
            mLessBrandTv.setVisibility(View.GONE);
            //list操作
            if (mList.size() < 5) {
                mMoreBrandTv.setVisibility(View.GONE);
                mLessBrandTv.setVisibility(View.GONE);
                mAdapter = new BrandAdapter(SelectActivity.this, mList);
            } else {
                mAdapter = new BrandAdapter(SelectActivity.this, mList.subList(0, 4));
            }
            mBrandList.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }


    }

    @OnClick(R.id.title_right_iv)
    public void qrCode(View v) {
        //扫描条码
        Intent intent = new Intent();
        intent.setClass(SelectActivity.this, MipcaActivityCapture.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


    }

    @OnClick(R.id.select_ok_submit)
    public void submit(View v) {

        //提交
        StringBuffer sb = new StringBuffer();
        StringBuffer id = new StringBuffer();

        if (mList != null && !mList.isEmpty()) {
            for (BrandModel brand : mList) {
                if (brand.isSelect()) {
                    sb.append(brand.getBrand());
                    sb.append(",");
                    id.append(brand.getId());
                    id.append(",");

                }

            }

        }
        if (mPriceList != null && !mPriceList.isEmpty()) {
            for (PriceModel brand : mPriceList) {
                if (brand.isSelect()) {
                    mPrice = brand.getPrice();
                }

            }

        }
        if (mList != null && !mList.isEmpty() && sb.length() > 0) {//至于点击自选品牌时 才进行操作 防止崩溃
            sb.deleteCharAt(sb.lastIndexOf(","));
            id.deleteCharAt(id.lastIndexOf(","));
        }


        mBrand = sb.toString();
        String ids = id.toString();
        String text = mInputEt.getText().toString().trim();
        mKey = text;


        if (text.length() > 0) {
//            loadingdialog.show();

            Bundle bundle = new Bundle();
            bundle.putString("key", mKey);
            bundle.putString("brand", mBrand);
            bundle.putString("price", mPrice);
            bundle.putString("id", ids);

            if (fromIndex) {
                Intent intent = new Intent(SelectActivity.this, TextSiriActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            } else {
                Intent intent = new Intent();
                intent.putExtras(bundle);
                this.setResult(RESULT_OK, intent);
            }
            finish();
//            pullSubmitData(mKey, mBrand, mPrice,ids);
        } else {
            Timer time=new Timer();

            if(isClick){
                isClick=false;

                ToastUtil.customShow(this, getResources().getText(R.string.select_error));
            }

            time.schedule(new TimerTask() {
                @Override
                public void run() {
                    isClick=true; // 取消退出
                }
            }, 500);
        }

    }


    protected synchronized void pullSubmitData(final String key, final String brand, final String price, final String id) {
        executeRequest(new GsonBaseRequest<BaseListModel<DbModel>>(Request.Method.POST, UrlConfig.SELECT_SUBMIT, new TypeToken<BaseListModel<DbModel>>() {
        }.getType(), responseSubmitListener(), errorListener()) {

            protected Map<String, String> getParams() {
                return new ApiParams().with("key", key).with("brands", brand).with("brandids", id).with("price", price);
            }

        });
    }

    private Response.Listener<BaseListModel<DbModel>> responseSubmitListener() {
        return new Response.Listener<BaseListModel<DbModel>>() {
            @Override
            public void onResponse(BaseListModel<DbModel> goodsModel) {
                loadingdialog.dismiss();
                if (goodsModel.getRet() == 0 && goodsModel.getData() != null) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", goodsModel);
                    Intent intent = new Intent(SelectActivity.this, TextSiriActivity.class);
                    intent.putExtras(bundle);
                    if (fromIndex) {
                        startActivity(intent);
                    } else {
                        setResult(RESULT_OK, intent);
                    }
                    finish();

                }

            }
        };

    }


    @OnClick(R.id.select_rl_one)
    public void noBrand(View v) {
        //点击不限制品牌 图标
        if (!mIsBrand) {
            mIsBrand = !mIsBrand;
            mNoBrandIv.setImageDrawable(mDrawableSelect);
            mSelectBrandIv.setImageDrawable(mDrawableUnSelect);
            mBrandLv.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.select_rl_two)
    public void selectBrand(View v) {
        //点击自选品牌 图标
        if (!isSubmit()) {
            ToastUtil.customShow(this, getResources().getText(R.string.select_error));
            return;
        }
        if (mIsBrand) {
            mIsBrand = !mIsBrand;
            mNoBrandIv.setImageDrawable(mDrawableUnSelect);
            mSelectBrandIv.setImageDrawable(mDrawableSelect);
            //初始化 品牌列表
            initBrand();
            mBrandLv.setVisibility(View.VISIBLE);

        }
    }

    @OnClick(R.id.select_rl_three)
    public void justCheap(View v) {
        //点击不限制价格 图标
        if (!mIsPrice) {
            mIsPrice = true;
            mJustCheapIv.setImageDrawable(mDrawableSelect);
            mSetRangeIv.setImageDrawable(mDrawableUnSelect);

            sPriceFlag = true;

            mRangeLv.setVisibility(View.GONE);


        }
    }

    @OnClick(R.id.select_rl_four)
    public void setRange(View v) {
        //点击自选价格 图标
        if (!isSubmit()) {
            ToastUtil.customShow(this, getResources().getText(R.string.select_error));
            return;
        }
        if (mIsPrice) {
            mIsPrice = false;

//            mScrollView.scrollTo(0,mScrollView.getMeasuredHeight());


            mSetRangeIv.setImageDrawable(mDrawableSelect);

            mJustCheapIv.setImageDrawable(mDrawableUnSelect);
            mRangeLv.setVisibility(View.VISIBLE);
            mScrollView.post(new Runnable() {
                public void run() {
                    mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });
            //初始化 滑块
//            initRange(50);

            StringBuffer sb = new StringBuffer();

            if (mList != null && !mList.isEmpty()) {
                for (BrandModel brand : mList) {
                    if (brand.isSelect()) {
                        sb.append(brand.getBrand());
                        sb.append(",");

                    }

                }

            }
            if (mList != null && !mList.isEmpty() && sb.length() > 0) {//至于点击自选品牌时 才进行操作 防止崩溃
                sb.deleteCharAt(sb.lastIndexOf(","));
            }
            String text = mInputEt.getText().toString().trim();
            mCurrentText = text;
            pullPriceData(text, sb.toString());
        }
    }


    protected void initBrand() {
        //操作list
        String text = mInputEt.getText().toString().trim();
        mCurrentText = text;
        String urlCode = "";
        try {
            urlCode = URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        pullBrandData(urlCode, 1);

    }

    protected void initRange(final int maxPrice) {
        mSeekBar.setRangeValues(0, maxPrice + 1);
        mSeekBar.setSelectedMinValue(0);
        mSeekBar.setSelectedMaxValue(maxPrice);
        mRangeShowTv.setText(0 + "~" + maxPrice * 10 + "元以上");

        mSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener<Integer>() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Integer minValue, Integer maxValue) {
                int min = minValue * 10;
                int max = maxValue * 10;
                StringBuffer sb = new StringBuffer();
                sb.append(min);
                sb.append(",");
                sb.append(max);
                mPrice = sb.toString();

                if (min > maxPrice * 10) {
                    min = maxPrice * 10;
                }
                if (max >= maxPrice * 10) {

                    mRangeShowTv.setText(min + "~" + maxPrice * 10 + "元以上");
                } else {

                    mRangeShowTv.setText(min + "~" + max + "元");
                }

            }
        });

    }

    protected synchronized void pullBrandData(final String key, final int type) {
        executeRequest(new GsonBaseRequest<BaseListModel<BrandModel>>(Request.Method.GET, UrlConfig.GET_BRAND + "?key=" + key + "&t=" + type, new TypeToken<BaseListModel<BrandModel>>() {
        }.getType(), responseBrandListener(), errorListener()) {

        });
    }

    protected synchronized void pullPriceData(final String key, final String brand) {
        executeRequest(new GsonBaseRequest<BaseListModel<PriceModel>>(Request.Method.POST, UrlConfig.PRICE_RANGE, new TypeToken<BaseListModel<PriceModel>>() {
        }.getType(), responsePriceListener(), errorListener()) {
            protected Map<String, String> getParams() {
                return new ApiParams().with("key", key).with("brands", brand);
            }


        });
    }


    private Response.Listener<BaseListModel<PriceModel>> responsePriceListener() {
        return new Response.Listener<BaseListModel<PriceModel>>() {
            @Override
            public void onResponse(BaseListModel<PriceModel> brandBuyListModel) {

                if (brandBuyListModel.getRet() == 0) {
//                    int max = brandBuyListModel.getL() / 10;
//                    if (max > 0) {
//                        initRange(max);
//                    } else {
//                        initRange(10);
//                    }

                    if (mPriceList != null) {
                        mPriceList.clear();
                    }

                    mPriceList = brandBuyListModel.getData();
                    mPriceAdapter = new PriceAdapter(SelectActivity.this, mPriceList);
                    mPriceListView.setAdapter(mPriceAdapter);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mPriceAdapter.notifyDataSetChanged();
                        }
                    });


                }

            }
        };
    }

    public Response.ErrorListener errorPriceListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                initRange(50);
            }
        };
    }


    private Response.Listener<BaseListModel<BrandModel>> responseBrandListener() {
        return new Response.Listener<BaseListModel<BrandModel>>() {
            @Override
            public void onResponse(BaseListModel<BrandModel> brandBuyListModel) {

                if (brandBuyListModel.getRet() == 0) {
                    if (mList != null) {
                        mList.clear();
                    }
                    mList = brandBuyListModel.getData();
                    if (mList.size() < 5) {
                        mMoreBrandTv.setVisibility(View.GONE);
                        mLessBrandTv.setVisibility(View.GONE);
                        mAdapter = new BrandAdapter(SelectActivity.this, mList);
                    } else {
                        mIsLessBrand = true;
                        mMoreBrandTv.setVisibility(View.VISIBLE);
                        mLessBrandTv.setVisibility(View.GONE);
                        mAdapter = new BrandAdapter(SelectActivity.this, mList.subList(0, 4));
                    }
                    mBrandList.setAdapter(mAdapter);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });


                }
            }
        };

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
//                    ToastUtil.customShow(this, bundle.getString("result"));
                    Intent intent = new Intent(this, GoodsActivity.class);
                    Bundle goodsBundle = new Bundle();
                    goodsBundle.putString("code", bundle.getString("result"));
                    goodsBundle.putString("mall", "");
                    intent.putExtras(goodsBundle);
                    startActivity(intent);
                }
                break;
        }
    }


}
