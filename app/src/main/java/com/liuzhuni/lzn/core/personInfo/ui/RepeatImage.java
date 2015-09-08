package com.liuzhuni.lzn.core.personInfo.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import com.liuzhuni.lzn.R;

/**
 * Created by Andrew Lee on 2015/9/6.
 * E-mail:jieooo7@163.com
 * Date: 2015-09-06
 * Time: 14:27
 */
public class RepeatImage extends ImageView {

    private int space;
    private int drawable;

    private Drawable mDrawable;


    private Bitmap mBitmap;

    public RepeatImage(Context context) {
        super(context);
    }

    public RepeatImage(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RepeatImageView);

        space=a.getDimensionPixelSize(R.styleable.RepeatImageView_spacing, 0);
        drawable=a.getResourceId(R.styleable.RepeatImageView_src, 0);
        mDrawable=a.getDrawable(R.styleable.RepeatImageView_src);



        a.recycle();




    }

    public RepeatImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public RepeatImage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

    public void setTheDrawable(int id){
        drawable=id;
        mDrawable=getContext().getResources().getDrawable(id);
        invalidate();

    }

    public  void setSpace(int space){

        this.space=space;
        invalidate();


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int wid=MeasureSpec.makeMeasureSpec(widthMeasureSpec,
                MeasureSpec.AT_MOST);
        int height=MeasureSpec.makeMeasureSpec(mDrawable.getIntrinsicHeight(),
                MeasureSpec.AT_MOST);

        setMeasuredDimension(wid, height);

    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        mBitmap=createRepeater();
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawBitmap(mBitmap, 0, 0, null);
    }

    private Bitmap createRepeater(){

        int width=((View)getParent()).getWidth();
        Bitmap src=BitmapFactory.decodeResource(getContext().getResources(),
                drawable);
        int count = (width + src.getWidth() +space - 1) / (src.getWidth()+space);

        Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);



        for(int idx = 0; idx < count; ++ idx){
            canvas.drawBitmap(src, idx *(space+src.getWidth()), 0, null);
        }
        return bitmap;
    }



}
