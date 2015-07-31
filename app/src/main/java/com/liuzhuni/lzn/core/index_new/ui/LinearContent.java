package com.liuzhuni.lzn.core.index_new.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.liuzhuni.lzn.utils.log.CommonLog;
import com.liuzhuni.lzn.utils.log.LogFactory;

/**
 * Created by Andrew Lee on 2015/7/29.
 * E-mail:jieooo7@163.com
 * Date: 2015-07-29
 * Time: 18:36
 */

public class LinearContent extends LinearLayout {

    private CommonLog log = LogFactory.createLog("linear");
    private Context context;



    public LinearContent(Context context) {
        super(context);
        this.context=context;
    }

    public LinearContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }


    public void bindLinearLayout(BaseAdapter adapter) {
        this.removeAllViews();
        int count = adapter.getCount();

        for (int i = 0; i < count; i++) {
            View v = adapter.getView(i, null, null);
            addView(v);
//            setDividerDrawable();
        }
    }

}
