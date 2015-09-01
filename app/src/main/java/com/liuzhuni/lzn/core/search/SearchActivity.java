package com.liuzhuni.lzn.core.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.liuzhuni.lzn.R;
import com.liuzhuni.lzn.base.BaseFragActivity;
import com.liuzhuni.lzn.core.search.adapter.MyFragmentPagerAdapter;
import com.liuzhuni.lzn.core.search.fragment.FragmentNewSearch;
import com.liuzhuni.lzn.core.search.fragment.FragmentPick;
import com.liuzhuni.lzn.core.select.ui.CleanableEditText;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseFragActivity {


    @ViewInject(R.id.search_et)
    private CleanableEditText mMiddleTv;

    @ViewInject(R.id.viewpager)
    private ViewPager mViewPager;

    @ViewInject(R.id.block_ll)
    private LinearLayout mBlockLl;

    @ViewInject(R.id.block_tv)
    private TextView mBlockLine;

    @ViewInject(R.id.pick_tv)
    private TextView mPickTv;

    @ViewInject(R.id.pick_line)
    private TextView mPickLine;

    @ViewInject(R.id.news_tv)
    private TextView mNewsTv;

    @ViewInject(R.id.news_line)
    private TextView mNewsLine;


    private List<Fragment> fragmentsList;
    private FragmentTransaction transaction;
    private FragmentManager mFragmentManager;


    private FragmentPick mFragPick;
    private FragmentNewSearch mFragNews;
    private String mWord="";

    private Searchable mSearchble;
    private PickSearchable mPickSearchble;









    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initData();
        findViewById();
        initUI();
        setListener();
    }

    @Override
    protected void initData() {

        mFragPick=new FragmentPick();
        mFragNews=new FragmentNewSearch();
        fragmentsList=new ArrayList<Fragment>();
        fragmentsList.add(mFragPick);
        fragmentsList.add(mFragNews);
        mFragmentManager=getSupportFragmentManager();

    }

    @Override
    protected void findViewById() {

        ViewUtils.inject(this);

    }

    @Override
    protected void initUI() {

        mBlockLine.setVisibility(View.GONE);
        mBlockLl.setVisibility(View.GONE);
        mViewPager.setOffscreenPageLimit(1);

        mViewPager.setAdapter(new MyFragmentPagerAdapter(mFragmentManager,
                fragmentsList));

        mViewPager.setCurrentItem(0);
        mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());

    }


    /* 页卡切换监听 */
    public class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int arg0) {
            setTextTitleSelectedColor(arg0);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }




    /* 设置标题文本的颜色 */
    private void setTextTitleSelectedColor(int arg0) {
            if (arg0 == 0) {
                mPickLine.setVisibility(View.VISIBLE);
                mNewsLine.setVisibility(View.GONE);
                mPickTv.setTextColor(getResources().getColor(R.color.key));
                mNewsTv.setTextColor(getResources().getColor(R.color.index_text));
            } else {
                mPickLine.setVisibility(View.GONE);
                mNewsLine.setVisibility(View.VISIBLE);
                mPickTv.setTextColor(getResources().getColor(R.color.index_text));
                mNewsTv.setTextColor(getResources().getColor(R.color.key));
            }


    }


    @Override
    protected void setListener() {

    }



    @OnClick(R.id.title_left)
    public void back(View v) {

        finish();
    }

    @OnClick(R.id.title_right_iv)
    public void search(View v) {

        //搜索点击

        if (getCurrentFocus() != null
                && getCurrentFocus().getWindowToken() != null) {
            getInputManager().hideSoftInputFromWindow(getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        String input=mMiddleTv.getText().toString();
        if(TextUtils.isEmpty(input)){
            return;
        }

        if(mWord.equals(input)){
            return;
        }
        mWord=input;


        mBlockLine.setVisibility(View.VISIBLE);
        mBlockLl.setVisibility(View.VISIBLE);
        mViewPager.setCurrentItem(0);

        mPickSearchble.search(mWord,true);//只有显示时请求网络,实现时判断
        mSearchble.search(mWord,true);//只有显示时请求网络,实现时判断

    }

    @OnClick(R.id.pick_ll)
    public void pick(View v) {

        //精选点击
        mViewPager.setCurrentItem(0);

    }


    @OnClick(R.id.news_ll)
    public void news(View v) {

        //爆料点击
        mViewPager.setCurrentItem(1);

    }


    @Override
    public void onAttachFragment(Fragment fragment) {

        try {
            if(fragment instanceof Searchable){

                mSearchble = (Searchable)fragment;
            }
            if(fragment instanceof PickSearchable)
            {
                mPickSearchble = (PickSearchable)fragment;
            }
        } catch (Exception e) {
            throw new ClassCastException(this.toString() + " must implementOnMainListener");
        }
        super.onAttachFragment(fragment);
    }


}
