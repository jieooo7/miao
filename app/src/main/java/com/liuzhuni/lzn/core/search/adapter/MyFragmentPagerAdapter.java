package com.liuzhuni.lzn.core.search.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Andrew Lee on 2015/8/14.
 * E-mail:jieooo7@163.com
 * Date: 2015-08-14
 * Time: 16:58
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter{

    private List<Fragment> list;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int arg0) {
        // TODO Auto-generated method stub
        return list.get(arg0);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.isEmpty() ? 0 : list.size();
    }



}
