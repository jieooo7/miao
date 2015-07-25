package com.liuzhuni.lzn.core.comment.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import com.liuzhuni.lzn.utils.log.CommonLog;
import com.liuzhuni.lzn.utils.log.LogFactory;

/**
 * Created by Andrew Lee on 2015/7/20.
 * E-mail:jieooo7@163.com
 * Date: 2015-07-20
 * Time: 16:56
 */
public class CommentLinearLayout extends LinearLayout {

    private CommonLog log = LogFactory.createLog("linear");
    private Context context;



    public CommentLinearLayout(Context context) {
        super(context);
        this.context=context;
    }

    public CommentLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
    }


    public void bindLinearLayout(BaseAdapter adapter) {
        int count = adapter.getCount();
        if(count==0){
            return;
        }

        this.removeAllViews();
        for (int i = 0; i < count; i++) {
            View v = adapter.getView(i, null, null);
            addView(v);
//            setDividerDrawable();
//            if(i<count-1){
//
//                TextView tv=new TextView(context);
//                LayoutParams lpTv = new LayoutParams(
//                        LayoutParams.MATCH_PARENT,1);
//                lpTv.setMargins(0, DensityUtil.dip2px(context, 2),0,DensityUtil.dip2px(context,2));
//                tv.setLayoutParams(lpTv);
//                tv.setBackgroundDrawable(new ColorDrawable(0xffebebeb));
//                addView(tv);
//            }
        }
    }

}
