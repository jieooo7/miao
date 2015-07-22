package com.liuzhuni.lzn.xList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.liuzhuni.lzn.R;

/**
 * Created by Andrew Lee on 2015/6/11.
 * E-mail:jieooo7@163.com
 * Date: 2015-06-11
 * Time: 10:55
 */
public class CustomHeader extends LinearLayout {
    private LinearLayout mContainer;
    private ProgressBar mProgressBar;
    private int mState = STATE_NORMAL;



    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;


    public CustomHeader(Context context) {
        super(context);
        initView(context);
    }

    public CustomHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }


    private void initView(Context context) {
        // 初始情况，设置下拉刷新view高度为0
        mContainer = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.custom_xlist_header, null);
        addView(mContainer);

        setGravity(Gravity.CENTER);

        mProgressBar = (ProgressBar)findViewById(R.id.custom_head);
    }

    public void setState(int state) {
        if (state == mState) return ;

        if (state == STATE_REFRESHING) {	// 显示进度
            mProgressBar.setVisibility(View.VISIBLE);
        } else {	// 显示箭头图片
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        mState = state;
    }

    public void setVisiableHeight(int height) {
        if (height < 0)
            height = 0;
        LayoutParams lp = (LayoutParams) mContainer
                .getLayoutParams();
        lp.height = height;
        mContainer.setLayoutParams(lp);
    }

    public int getVisiableHeight() {
        return mContainer.getLayoutParams().height;
    }


}
