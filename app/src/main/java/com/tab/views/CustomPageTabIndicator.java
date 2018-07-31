package com.tab.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;

import com.inidicator.IPagerNavigator;
import com.inidicator.IndicatorUtils;
import com.inidicator.callback.OnGetIndicatorViewAdapter;
import com.inidicator.impl.CommonNavigator;
import com.inidicator.impl.CommonNavigatorAdapter;
import com.inidicator.impl.IPagerIndicator;
import com.inidicator.impl.IPagerTitleView;
import com.inidicator.impl.indicators.LinePagerIndicator;
import com.tab.adapter.CustomPagerAdapter;
import com.tab.R;


/**
 * 理财师3.0主页蚯蚓效果的指示器
 */
public class CustomPageTabIndicator extends FrameLayout {
    private IPagerNavigator navigator;
    private int mTextColor;
    private int mSelectTextColor;
    private int mTextSize;
    private int mIndicatorHeight;
    private int mIndicatorColor;
    private boolean adjustMode;
    private DataSetObserver dataSetObserver;
    private CustomPagerAdapter pagerAdapter;
    private OnGetIndicatorViewAdapter getIndicatorViewAdapter;
    private int currentIndex;
    public static int ARROW_UP = 0; //箭头朝上
    public static int ARROW_DOWN = 1;

    public int getArrow_direction() {
        return arrow_direction;
    }

    private int arrow_direction = 1;//默认箭头是朝下的



    public void setArrow_direction(int arrow_direction) {
        this.arrow_direction = arrow_direction;
    }

    private OnChildTabSelectedListener onChildTabSelectedListener;
    public interface OnChildTabSelectedListener{
        void showPopwindow(CustomTabView view, int index);
        void dismissPopWindow();
    }

    public void setOnChildTabSelectedListener(OnChildTabSelectedListener onChildTabSelectedListener) {
        this.onChildTabSelectedListener = onChildTabSelectedListener;
    }


    public CustomPageTabIndicator(@NonNull Context context) {
        this(context, (AttributeSet)null);
    }

    public CustomPageTabIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPageTabIndicator(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
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
            currentIndex = position;
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
        this.pagerAdapter = (CustomPagerAdapter) viewPager.getAdapter();
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


    public void setGetIndicatorViewAdapter(OnGetIndicatorViewAdapter getIndicatorViewAdapter) {
        this.getIndicatorViewAdapter = getIndicatorViewAdapter;
    }


    /********************************在这个方法里面对自定义view进行处理**********************************************/
    private IPagerNavigator getDefaultNavigator(final ViewPager viewPager) {
        CommonNavigator commonNavigator = new CommonNavigator(this.getContext());
        commonNavigator.setSkimOver(true);
        if (this.adjustMode && this.pagerAdapter.getCount() < 6) {
            commonNavigator.setAdjustMode(true);
        } else {
            commonNavigator.setAdjustMode(false);
        }

        commonNavigator.setScrollPivotX(0.65F);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            public int getCount() {
                return CustomPageTabIndicator.this.pagerAdapter.getCount();
            }

            public IPagerIndicator getIndicator(Context context) {
                if (CustomPageTabIndicator.this.getIndicatorViewAdapter != null) {
                    IPagerIndicator iPagerIndicator = CustomPageTabIndicator.this.getIndicatorViewAdapter.getIndicator(context);
                    if (iPagerIndicator != null) {
                        return iPagerIndicator;
                    }
                }

                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(new Integer[]{CustomPageTabIndicator.this.mIndicatorColor});
                indicator.setLineHeight((float) CustomPageTabIndicator.this.mIndicatorHeight);
                indicator.setMode(2);
                indicator.setYOffset((float)IndicatorUtils.dp2px(context, 5.0D));
                indicator.setLineWidth((float)IndicatorUtils.dp2px(context, 25.0D));
                indicator.setRoundRadius((float)IndicatorUtils.dp2px(context, 3.0D));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0F));
                return indicator;
            }


            public IPagerTitleView getTitleView(Context context, final int index) {
                if (CustomPageTabIndicator.this.getIndicatorViewAdapter != null) {
                    IPagerTitleView titleView = CustomPageTabIndicator.this.getIndicatorViewAdapter.getTitleView(context, index);
                    if (titleView != null) {
                        titleView.setText(CustomPageTabIndicator.this.pagerAdapter.getPageTitle(index).toString());
                        return titleView;
                    }
                }

                /*SimplePagerTitleView simplePagerTitleView = new ColorFlipPagerTitleView(context);
                simplePagerTitleView.setText(MyTabIndicator.this.pagerAdapter.getPageTitle(index).toString());
                simplePagerTitleView.setTextSize((float)MyTabIndicator.this.mTextSize);
                simplePagerTitleView.setMinWidth(IndicatorUtils.dp2px(context, 70.0D));
                simplePagerTitleView.setNormalColor(MyTabIndicator.this.mTextColor);
                simplePagerTitleView.setSelectedColor(MyTabIndicator.this.mSelectTextColor);
                simplePagerTitleView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });*/

                final CustomTabView simplePagerTitleView = new CustomTabView(context);
                simplePagerTitleView.setText(CustomPageTabIndicator.this.pagerAdapter.getPageTitle(index).toString());
                simplePagerTitleView.setShowArrow(CustomPageTabIndicator.this.pagerAdapter.isShowArrow(index));
                simplePagerTitleView.setTextSize((float) CustomPageTabIndicator.this.mTextSize);
                simplePagerTitleView.setmNormalColor(CustomPageTabIndicator.this.mTextColor);
                simplePagerTitleView.setmSelectedColor(CustomPageTabIndicator.this.mSelectTextColor);
                simplePagerTitleView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if(currentIndex!=index){//旧的item转到新的item

                            viewPager.setCurrentItem(index);
                            arrow_direction = ARROW_DOWN;
                            currentIndex = index;
                            if(simplePagerTitleView.isShowArrow()){
                                simplePagerTitleView.setArrowDirection(arrow_direction);
                            }
                        }else {//重复点击此item

                            if(!simplePagerTitleView.isShowArrow()) return;//如果此item不包括箭头就不用判断后面的操作了

                            if(arrow_direction==ARROW_DOWN){
                                if(onChildTabSelectedListener!=null){
                                    onChildTabSelectedListener.showPopwindow(simplePagerTitleView,index);
                                }
                            }else {
                                if(onChildTabSelectedListener!=null){
                                    onChildTabSelectedListener.dismissPopWindow();
                                }
                                arrow_direction=ARROW_DOWN;
                                simplePagerTitleView.setArrowDirection(arrow_direction);

                            }

                        }
                    }
                });
                return simplePagerTitleView;
            }


        });
        return commonNavigator;
    }

}
