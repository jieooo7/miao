package com.liuzhuni.lzn.core.login.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.liuzhuni.lzn.R;

/**
 * Created by Andrew Lee on 2015/4/13.
 * E-mail:jieooo7@163.com
 * Date: 2015-04-13
 * Time: 19:54
 */
public class PasswdView extends EditText {

    private boolean mPasswd = true;
    private Drawable mDrawable = getResources().getDrawable(R.drawable.login_eye_s);
    private Drawable mDrawableVisible = getResources().getDrawable(R.drawable.login_eye_n);
    private Drawable mDrawableLeft = getResources().getDrawable(R.drawable.login_key);

    public PasswdView(Context context) {
        super(context);
    }


    public PasswdView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PasswdView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {


        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:

                boolean isPasswd = (event.getX() > (getWidth() - getTotalPaddingRight())) &&
                        (event.getX() < (getWidth() - getPaddingRight()));
                if (isPasswd) {
                    if (mPasswd) {

                        mPasswd = false;
                        this.setTransformationMethod(HideReturnsTransformationMethod.getInstance());

                        mDrawable.setBounds(0, 0, mDrawable.getMinimumWidth(), mDrawable.getMinimumHeight());
                        mDrawableLeft.setBounds(0, 0, mDrawableLeft.getMinimumWidth(), mDrawableLeft.getMinimumHeight());
                        this.setCompoundDrawables(mDrawableLeft, null, mDrawable, null);

                    } else {
                        mPasswd = true;
                        this.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        mDrawableVisible.setBounds(0, 0, mDrawableVisible.getMinimumWidth(), mDrawableVisible.getMinimumHeight());
                        mDrawableLeft.setBounds(0, 0, mDrawableLeft.getMinimumWidth(), mDrawableLeft.getMinimumHeight());
                        this.setCompoundDrawables(mDrawableLeft, null, mDrawableVisible, null);
                    }
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent(event);
    }
}
