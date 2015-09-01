package com.getui.demo;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.liuzhuni.lzn.core.goods.ToBuyActivity;
import com.liuzhuni.lzn.core.index_new.DetailActivity;
import com.liuzhuni.lzn.core.index_new.MainActivity;
import com.liuzhuni.lzn.core.siri.TextSiriActivity;

/**
 * Created by Andrew Lee on 2015/8/13.
 * E-mail:jieooo7@163.com
 * Date: 2015-08-13
 * Time: 21:14
 * 通知栏跳转逻辑处理(根据Scheme Host确定具体跳转到哪一个页面)
 */
public class HuimScheme extends Activity {
    public static final String SCHEME = "huim://";

    public static final String DETAIL = "detail";
    public static final String INDEX = "index";
    public static final String DIALOG = "dialog";
    public static final String BROKE = "broke";
    public static final String BAICAI = "baicai";

    public static final String ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Uri uri = intent.getData();
        String host = uri.getHost();

//        Log.e("uri.getScheme()", uri.getScheme());
//        Log.e("uri.getAuthority()", uri.getAuthority());
//        Log.e("uri.getPath()", uri.getPath());
//        Log.e("uri.getQuery()", uri.getQuery());
//        Log.e("uri.getHost()", uri.getHost());

        action(host, uri);
    }

    private void action(String host, Uri uri) {
        if (host.startsWith(DETAIL)) {
            String id = uri.getQueryParameter(ID);
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("isSelect", true);
            startActivity(intent);
            finish();
            return;
        }
        if (host.startsWith(INDEX)) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        if (host.startsWith(DIALOG)) {
            Intent intent = new Intent(this, TextSiriActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        if (host.startsWith(BROKE)) {
            String id = uri.getQueryParameter(ID);
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("isSelect", false);
            startActivity(intent);
            finish();
            return;
        }
        if (host.startsWith(BAICAI)) {
            String id = uri.getQueryParameter(ID);
            Intent intent = new Intent(this, ToBuyActivity.class);
            intent.putExtra("url", id);
            intent.putExtra("title", " ");
            startActivity(intent);
            finish();
            return;
        }
    }

    private void startAct(Class<? extends  Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
