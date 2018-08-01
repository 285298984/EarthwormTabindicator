package com.tab.views;

public interface OnPageListener {

     void onPageScrolled(int position,float positionOffset,int positionOffsetPixels);

     void onPageSelected(int position);

     void onPageScrollStateChanged(int state);
}
