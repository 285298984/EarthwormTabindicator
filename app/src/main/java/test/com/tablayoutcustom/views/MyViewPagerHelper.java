package test.com.tablayoutcustom.views;

import android.support.v4.view.ViewPager;

public class MyViewPagerHelper {
    public MyViewPagerHelper() {
    }

    public static void bind(final MyTabIndicator tabIndicator, ViewPager pager) {
        if (tabIndicator != null && pager != null) {
            pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    tabIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }

                public void onPageSelected(int position) {
                    tabIndicator.onPageSelected(position);
                }

                public void onPageScrollStateChanged(int state) {
                    tabIndicator.onPageScrollStateChanged(state);
                }
            });
        }
    }
}
