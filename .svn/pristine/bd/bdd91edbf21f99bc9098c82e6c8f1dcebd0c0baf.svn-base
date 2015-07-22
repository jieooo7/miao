package com.liuzhuni.lzn.core.index_new.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.URLSpan;
import android.view.View;

import com.liuzhuni.lzn.core.goods.ToBuyActivity;

/**
 * Created by Andrew Lee on 2015/7/17.
 * E-mail:jieooo7@163.com
 * Date: 2015-07-17
 * Time: 10:42
 */
public class CustomURLSpan extends URLSpan{

    public CustomURLSpan(String url) {
        super(url);
    }

    @Override
    public void onClick(View widget) {

        Context context = widget.getContext();
        Intent intent = new Intent(context, ToBuyActivity.class);
        Bundle bundle = new Bundle();

        bundle.putString("url",getURL());
        bundle.putString("title","");
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
