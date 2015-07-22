package com.liuzhuni.lzn.core.personInfo.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuzhuni.lzn.R;

/**
 * Created by Andrew Lee on 2015/4/14.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-14
 * Time: 10:49
 */
public class MeView extends FrameLayout {


    private ImageView mLeftImage;
    private TextView mTextView;
    private TextView mNumTextView;

    public MeView(Context context) {
        super(context);
    }

    public MeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.custom_me_view, this);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.custoMeView);

        CharSequence text=a.getText(R.styleable.custoMeView_android_text);
        Drawable drawable=a.getDrawable(R.styleable.custoMeView_android_src);
        mLeftImage=(ImageView) findViewById(R.id.custom_view_left);
        mLeftImage.setImageDrawable(drawable);
        mTextView=(TextView) findViewById(R.id.custom_view_text_main);
        mTextView.setText(text);
        mNumTextView=(TextView) findViewById(R.id.sustom_view_num);
        a.recycle();

    }

    public MeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


    }


    public void setNum(int num){
        if(num>0){
            mNumTextView.setVisibility(View.VISIBLE);
            mNumTextView.setText(""+num);
        }else{
            mNumTextView.setVisibility(View.GONE);

        }


    }





}
