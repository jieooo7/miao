package com.liuzhuni.lzn.core.buylist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.BaseFragActivity;
import com.liuzhuni.lzn.config.Check;
import com.liuzhuni.lzn.core.buylist.fragment.FragmentFinish;
import com.liuzhuni.lzn.core.buylist.fragment.FragmentList;
import com.liuzhuni.lzn.core.select.SelectActivity;
import com.liuzhuni.lzn.utils.PreferencesUtils;

public class BuyActivity extends BaseFragActivity {

    @ViewInject(R.id.title_left)
    private TextView mBackTv;
    @ViewInject(R.id.title_right)
    private TextView mRightTv;
    @ViewInject(R.id.title_middle)
    private TextView mMiddleTv;
    @ViewInject(R.id.buy_want_buy)
    private TextView mBuyTv;
    @ViewInject(R.id.buy_had_finish)
    private TextView mFinishTv;

    @ViewInject(R.id.img)
    private ImageView imageView;




    private FragmentTransaction mTransaction;
    private boolean mIsBuy=true;
    private FragmentList mFragList;
    private FragmentFinish mFragFinish;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        initData();
        findViewById();
        initUI();
        setListener();


    }

    @Override
    protected void initData() {


        mFragList=new FragmentList();
        mFragFinish=new FragmentFinish();


    }

    @Override
    protected void findViewById() {

        ViewUtils.inject(this);

    }



    @Override
    protected void initUI() {

        mBackTv.setText(getText(R.string.i_want_back));
        mMiddleTv.setText(getResources().getString(R.string.my_want_buy));
        mRightTv.setText(getResources().getString(R.string.i_want_buy));

        if (!Check.isGuideFirst(this)) {
            imageView.setVisibility(View.VISIBLE);
        }



        mTransaction=getSupportFragmentManager().beginTransaction();
        mTransaction.replace(R.id.container_fragment, mFragList);
        mTransaction.addToBackStack(null);
        mTransaction.commit();
    }

    @Override
    protected void setListener() {

    }

    @OnClick(R.id.title_left)
    public void back(View v){

        finish();
    }
    @OnClick(R.id.img)
    public void gone(View v){
        PreferencesUtils.putBooleanToSPMap(BuyActivity.this, PreferencesUtils.Keys.IS_GUIDE_F, true);
        imageView.setVisibility(View.GONE);

    }
    @OnClick(R.id.title_right)
    public void buy(View v){

        Intent intent=new Intent(this, SelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("fromIndex", true);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();

    }

    @OnClick(R.id.buy_want_buy)
    public void left(View v){

        //想买按钮
        if(!mIsBuy){
            mIsBuy=!mIsBuy;
//            mFragList=new FragmentList();
            setFragment(R.drawable.left_solid,R.color.white,R.drawable.right_rect,R.color.text_black,mFragList);
            if(!mFragList.isAdded()){
                mTransaction.add(R.id.container_fragment,mFragList);
            }
            mTransaction.show(mFragList);
            mTransaction.hide(mFragFinish);
            mTransaction.addToBackStack(null);
            mTransaction.commit();
//            mBuyTv.setBackgroundResource(R.drawable.left_solid);
//            mBuyTv.setTextColor(getResources().getColor(R.color.white));
//            mFinishTv.setBackgroundResource(R.drawable.right_rect);
//            mFinishTv.setTextColor(getResources().getColor(R.color.text_black));
//            mTransaction=getSupportFragmentManager().beginTransaction();
//            mTransaction.replace(R.id.container_fragment, mFragList);
//            mTransaction.addToBackStack(null);
//            mTransaction.commit();


        }
    }

    @OnClick(R.id.buy_had_finish)
    public void right(View v){

        //已买按钮
        if(mIsBuy){
            mIsBuy=!mIsBuy;
//            mFragFinish=new FragmentFinish();
            setFragment(R.drawable.left_rect,R.color.text_black,R.drawable.right_solid,R.color.white,mFragFinish);
            if(!mFragFinish.isAdded()){
                mTransaction.add(R.id.container_fragment,mFragFinish);
            }
            mTransaction.show(mFragFinish);
            mTransaction.hide(mFragList);
            mTransaction.addToBackStack(null);
            mTransaction.commit();
//            mBuyTv.setBackgroundResource(R.drawable.left_rect);
//            mBuyTv.setTextColor(getResources().getColor(R.color.text_black));
//            mFinishTv.setBackgroundResource(R.drawable.right_solid);
//            mFinishTv.setTextColor(getResources().getColor(R.color.white));
//            mTransaction=getSupportFragmentManager().beginTransaction();
//            mTransaction.replace(R.id.container_fragment, mFragFinish);
//            mTransaction.addToBackStack(null);
//            mTransaction.commit();

        }
    }


    protected void setFragment(int backLeft,int colorLeft,int backRight,int colorRight,Fragment fragment){
        mBuyTv.setBackgroundResource(backLeft);
        mBuyTv.setTextColor(getResources().getColor(colorLeft));
        mFinishTv.setBackgroundResource(backRight);
        mFinishTv.setTextColor(getResources().getColor(colorRight));
        mTransaction=getSupportFragmentManager().beginTransaction();
//        mTransaction.replace(R.id.container_fragment, fragment);
//        mTransaction.add()
//        mTransaction.show()
//        mTransaction.hide()
//        mTransaction.addToBackStack(null);
//        mTransaction.commit();

    }



}