package com.tab.views;

import android.support.v4.view.ViewPager;

/**
 * 面向抽象编程，所有实现OnPageListener的可以进行绑定
 */
public class BaseViewPagerHelper {

    public static void bind(final OnPageListener listener, ViewPager pager) {
        if (listener != null && pager != null) {
            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }

                public void onPageSelected(int position) {
                    listener.onPageSelected(position);
                }

                public void onPageScrollStateChanged(int state) {
                    listener.onPageScrollStateChanged(state);
                }
            });
        }
    }
}
