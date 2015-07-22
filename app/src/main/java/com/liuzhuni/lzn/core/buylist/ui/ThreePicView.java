package com.liuzhuni.lzn.core.buylist.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.liuzhuni.lzn.R;

/**
 * Created by Andrew Lee on 2015/5/9.
 * E-mail:jieooo7@163.com
 * Date: 2015-05-09
 * Time: 16:25
 */
public class ThreePicView extends ImageView {

    private Paint mPaint;
    private Context context;

    private int mType=0;
//    private final int ONE=0;
//    private final int TWO=1;
//    private final int THREE=2;
//    private final int[] arrayType={0,1,2,3,4,5,6,7};
    private final int[] arrayDrawble={ R.drawable.qingdan_loading_01,R.drawable.qingdan_loading_02,R.drawable.qingdan_loading_03,R.drawable.qingdan_loading_04,R.drawable.qingdan_loading_05,R.drawable.qingdan_loading_06,R.drawable.qingdan_loading_07,R.drawable.qingdan_loading_08};


//    private Bitmap mPicOne;
    private Bitmap[] arrayBitmap=new Bitmap[8];
//    private Bitmap mPicTwo;
//    private Bitmap mPicThree;
    private Bitmap mBitmap;



    public ThreePicView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        initView(context);
    }


    protected void initView(Context context) {
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
//        mPicOne= BitmapFactory.decodeResource(context.getResources(),
//                R.drawable.my_setting_push_black);
//        mPicTwo= BitmapFactory.decodeResource(context.getResources(),
//                R.drawable.my_setting_push_border_n);
//        mPicThree= BitmapFactory.decodeResource(context.getResources(),
//                R.drawable.my_setting_push_border_s);

        for(int i=0;i<8;i++){
            arrayBitmap[i]=BitmapFactory.decodeResource(context.getResources(),
                    arrayDrawble[i]);
        }
        mBitmap=arrayBitmap[0];


        startAnimate();



    }

    protected void startAnimate(){

        this.postDelayed(new Runnable(){


            @Override
            public void run() {


                if(mType==7){
                    mType=0;
                    mBitmap=arrayBitmap[0];

                }else{
//                    mType++;
                    mBitmap=arrayBitmap[++mType];
                }



//                switch (mType){
//                    case 0:
//                        mType=arrayType[1];
//                        mBitmap=arrayBitmap[1];
//                        break;
//                    case 1:
//                        mType=arrayType[2];
//                        mBitmap=arrayBitmap[2];
//                        break;
//                    case 2:
//
//                        mType=arrayType[3];
//                        mBitmap=arrayBitmap[3];
//
//                        break;
//                    case 3:
//
//                        mType=arrayType[4];
//                        mBitmap=arrayBitmap[4];
//
//                        break;
//                    case 4:
//
//                        mType=arrayType[5];
//                        mBitmap=arrayBitmap[5];
//
//                        break;
//                    case 5:
//
//                        mType=arrayType[6];
//                        mBitmap=arrayBitmap[6];
//
//                        break;
//                    case 6:
//
//                        mType=arrayType[7];
//                        mBitmap=arrayBitmap[7];
//
//                        break;
//                    case 7:
//
//                        mType=arrayType[0];
//                        mBitmap=arrayBitmap[0];
//
//                        break;
//                }
                invalidate();
                startAnimate();
            }

        },400);

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }



    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }







    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap,0,0,mPaint);
    }
}
