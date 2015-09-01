package com.liuzhuni.lzn.core.index_new.utils;

import android.os.Bundle;
import android.widget.Toast;

import com.alibaba.sdk.android.AlibabaSDK;
import com.alibaba.sdk.android.ResultCode;
import com.alibaba.sdk.android.trade.ItemService;
import com.alibaba.sdk.android.trade.callback.TradeProcessCallback;
import com.alibaba.sdk.android.trade.model.TaokeParams;
import com.alibaba.sdk.android.trade.model.TradeResult;
import com.liuzhuni.lzn.base.Base2Activity;
import com.taobao.tae.sdk.webview.TaeWebViewUiSettings;

public class TaokeActivity extends Base2Activity {

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    protected void initData() {

    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initUI() {

    }

    @Override
    protected void setListener() {

    }


    public void showTaokeItemDetail() {
        TaeWebViewUiSettings taeWebViewUiSettings = new TaeWebViewUiSettings();
        TaokeParams taokeParams = new TaokeParams();
        taokeParams.pid="mm_88888888_6666666_68686868";
        taokeParams.unionId="null";
        ItemService itemService = AlibabaSDK.getService(ItemService.class);
        itemService.showTaokeItemDetailByOpenItemId(TaokeActivity.this, new TradeProcessCallback() {

            @Override
            public void onPaySuccess(TradeResult tradeResult) {
                Toast.makeText(TaokeActivity.this, "支付成功", Toast.LENGTH_SHORT)
                        .show();

            }

            @Override
            public void onFailure(int code, String msg) {
                if (code == ResultCode.QUERY_ORDER_RESULT_EXCEPTION.code) {
                    Toast.makeText(TaokeActivity.this, "确认交易订单失败",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TaokeActivity.this, "交易取消",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, taeWebViewUiSettings, "AAEdNWF_ABrk9HpuLchgVlYX", 1, null,taokeParams);
    }


}
