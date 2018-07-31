package com.tab.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class LcsCustomAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> list;
    private List<String> tab_list;
    private boolean showArrow = false;//显示箭头吗?默认不显示
    public LcsCustomAdapter(FragmentManager fm, List<Fragment> list, List<String> tab_list) {
        super(fm);
        this.list = list;
        this.tab_list = tab_list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_list.get(position%tab_list.size());
    }

    /**
     * 在这里处理显示与否的逻辑
     * @param position
     * @return
     */
    public boolean isShowArrow(int position){
        if(position==0) return true;
        return false;
    }
}
