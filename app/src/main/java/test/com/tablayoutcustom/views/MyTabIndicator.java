package test.com.tablayoutcustom.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.sina.licaishi.commonuilib.indicator.IPagerNavigator;
import com.sina.licaishi.commonuilib.indicator.IndicatorUtils;
import com.sina.licaishi.commonuilib.indicator.callback.OnGetIndicatorViewAdapter;
import com.sina.licaishi.commonuilib.indicator.impl.CommonNavigatorAdapter;
import com.sina.licaishi.commonuilib.indicator.impl.IPagerIndicator;
import com.sina.licaishi.commonuilib.indicator.impl.IPagerTitleView;
import com.sina.licaishi.commonuilib.indicator.impl.indicators.LinePagerIndicator;
import com.sina.licaishi.commonuilib.indicator.impl.titles.ColorFlipPagerTitleView;
import com.sina.licaishi.commonuilib.indicator.impl.titles.SimplePagerTitleView;

import test.com.tablayoutcustom.R;

/**
 * 蚯蚓效果的指示器
 */
public class MyTabIndicator extends FrameLayout {
    private IPagerNavigator navigator;
    private int mTextColor;
    private int mSelectTextColor;
    private int mTextSize;
    private int mIndicatorHeight;
    private int mIndicatorColor;
    private boolean adjustMode;
    private DataSetObserver dataSetObserver;
    private PagerAdapter pagerAdapter;
    private OnGetIndicatorViewAdapter getIndicatorViewAdapter;

    public MyTabIndicator(@NonNull Context context) {
        this(context, (AttributeSet)null);
    }

    public MyTabIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTabIndicator(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TabIndicator, defStyleAttr, 0);
        this.mTextColor = a.getColor(R.styleable.TabIndicator_tab_textColor, Color.parseColor("#515151"));
        this.mSelectTextColor = a.getColor(R.styleable.TabIndicator_tab_selectTextColor, Color.parseColor("#000000"));
        this.mIndicatorColor = a.getColor(R.styleable.TabIndicator_tab_indicatorColor, Color.parseColor("#ff6600"));
        this.mTextSize = a.getDimensionPixelSize(R.styleable.TabIndicator_tab_textSize, 16);
        this.mIndicatorHeight = a.getDimensionPixelSize(R.styleable.TabIndicator_tab_indicatorHeight, IndicatorUtils.dp2px(context, 3.0D));
        this.adjustMode = a.getBoolean(R.styleable.TabIndicator_tab_adjustMode, true);
        a.recycle();
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (this.navigator != null) {
            this.navigator.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

    }

    public void onPageSelected(int position) {
        if (this.navigator != null) {
            this.navigator.onPageSelected(position);
        }

    }

    public void onPageScrollStateChanged(int state) {
        if (this.navigator != null) {
            this.navigator.onPageScrollStateChanged(state);
        }

    }

    public IPagerNavigator getNavigator() {
        return this.navigator;
    }

    public void setNavigator(IPagerNavigator navigator) {
        if (this.navigator != navigator) {
            if (this.navigator != null) {
                this.navigator.onDetachFromIndicator();
            }

            this.navigator = navigator;
            this.removeAllViews();
            if (this.navigator instanceof View) {
                LayoutParams lp = new LayoutParams(-1, -1);
                this.addView((View)navigator, lp);
                navigator.onAttachToIndicator();
            }

        }
    }

    public void setupWithViewPager(ViewPager viewPager) {
        this.pagerAdapter = viewPager.getAdapter();
        if (this.pagerAdapter == null) {
            throw new NullPointerException("PagerAdapter is null");
        } else {
            final IPagerNavigator navigator = this.getDefaultNavigator(viewPager);
            this.setNavigator(navigator);
            MyViewPagerHelper.bind(this, viewPager);
            if (this.dataSetObserver == null) {
                this.dataSetObserver = new DataSetObserver() {
                    public void onChanged() {
                        navigator.notifyDataSetChanged();
                    }
                };
                this.pagerAdapter.registerDataSetObserver(this.dataSetObserver);
            }

        }
    }

    private IPagerNavigator getDefaultNavigator(final ViewPager viewPager) {
        MyCommonNavigator commonNavigator = new MyCommonNavigator(this.getContext());
        commonNavigator.setSkimOver(true);
        if (this.adjustMode && this.pagerAdapter.getCount() < 6) {
            commonNavigator.setAdjustMode(true);
        } else {
            commonNavigator.setAdjustMode(false);
        }

        commonNavigator.setScrollPivotX(0.65F);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            public int getCount() {
                return MyTabIndicator.this.pagerAdapter.getCount();
            }

            public IPagerTitleView getTitleView(Context context, final int index) {
                if (MyTabIndicator.this.getIndicatorViewAdapter != null) {
                    IPagerTitleView titleView = MyTabIndicator.this.getIndicatorViewAdapter.getTitleView(context, index);
                    if (titleView != null) {
                        titleView.setText(MyTabIndicator.this.pagerAdapter.getPageTitle(index).toString());
                        return titleView;
                    }
                }

                SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(MyTabIndicator.this.pagerAdapter.getPageTitle(index).toString());
                simplePagerTitleView.setTextSize((float)MyTabIndicator.this.mTextSize);
                simplePagerTitleView.setMinWidth(IndicatorUtils.dp2px(context, 70.0D));
                simplePagerTitleView.setNormalColor(MyTabIndicator.this.mTextColor);
                simplePagerTitleView.setSelectedColor(MyTabIndicator.this.mSelectTextColor);
                simplePagerTitleView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            public IPagerIndicator getIndicator(Context context) {
                if (MyTabIndicator.this.getIndicatorViewAdapter != null) {
                    IPagerIndicator iPagerIndicator = MyTabIndicator.this.getIndicatorViewAdapter.getIndicator(context);
                    if (iPagerIndicator != null) {
                        return iPagerIndicator;
                    }
                }

                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(new Integer[]{MyTabIndicator.this.mIndicatorColor});
                indicator.setLineHeight((float)MyTabIndicator.this.mIndicatorHeight);
                indicator.setMode(2);
                indicator.setYOffset((float)IndicatorUtils.dp2px(context, 5.0D));
                indicator.setLineWidth((float)IndicatorUtils.dp2px(context, 25.0D));
                indicator.setRoundRadius((float)IndicatorUtils.dp2px(context, 3.0D));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0F));
                return indicator;
            }
        });
        return commonNavigator;
    }

    public void setGetIndicatorViewAdapter(OnGetIndicatorViewAdapter getIndicatorViewAdapter) {
        this.getIndicatorViewAdapter = getIndicatorViewAdapter;
    }
}
