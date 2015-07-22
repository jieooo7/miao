package com.liuzhuni.lzn.core.login.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.core.select.ui.CleanableEditText;

/**
 * Created by Andrew Lee on 2015/5/4.
 * E-mail:jieooo7@163.com
 * Date: 2015-05-04
 * Time: 10:38
 */
public class DoubleRightView extends FrameLayout {

    private ImageView mRightIv;
    private CleanableEditText mText;

    private boolean mPasswd = true;
    private Drawable mDrawable = getResources().getDrawable(R.drawable.login_eye_s);
    private Drawable mDrawableVisible = getResources().getDrawable(R.drawable.login_eye_n);

    public DoubleRightView(Context context) {
        super(context);
    }

    public DoubleRightView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.double_right_view, this);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.doubleRight);
        CharSequence hint=a.getText(R.styleable.doubleRight_android_hint);
        int length=a.getInt(R.styleable.doubleRight_android_maxLength, 20);
//        System.out.print(length);
        mRightIv=(ImageView) findViewById(R.id.double_right);
        mText=(CleanableEditText)findViewById(R.id.double_passwd);
        mText.setHint(hint);


        // 设置maxlength
        mText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});

        mRightIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPasswd) {

                    mPasswd = false;
                    mText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    mRightIv.setImageDrawable(mDrawable);

                } else {
                    mPasswd = true;
                    mText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    mRightIv.setImageDrawable(mDrawableVisible);
                }
            }
        });


    }

    public DoubleRightView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CharSequence getText(){


        return mText.getText();
    }

}
