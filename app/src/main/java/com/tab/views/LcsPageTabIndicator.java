package com.tab.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.licaishi.commonuilib.indicator.IPagerNavigator;
import com.sina.licaishi.commonuilib.indicator.IndicatorUtils;
import com.sina.licaishi.commonuilib.indicator.callback.OnGetIndicatorViewAdapter;
import com.sina.licaishi.commonuilib.indicator.impl.CommonNavigator;
import com.sina.licaishi.commonuilib.indicator.impl.CommonNavigatorAdapter;
import com.sina.licaishi.commonuilib.indicator.impl.IPagerIndicator;
import com.sina.licaishi.commonuilib.indicator.impl.IPagerTitleView;
import com.sina.licaishi.commonuilib.indicator.impl.indicators.LinePagerIndicator;
import com.tab.MyAdapter;
import com.tab.R;


/**
 * 理财师3.0主页蚯蚓效果的指示器
 */
public class LcsPageTabIndicator extends FrameLayout {
    private IPagerNavigator navigator;
    private int mTextColor;
    private int mSelectTextColor;
    private int mTextSize;
    private int mIndicatorHeight;
    private int mIndicatorColor;
    private boolean adjustMode;
    private DataSetObserver dataSetObserver;
    private MyAdapter pagerAdapter;
    private OnGetIndicatorViewAdapter getIndicatorViewAdapter;
    private int currentIndex;
    private static int ARROW_UP = 0; //箭头朝上
    private static int ARROW_DOWN = 1;
    private int arrow_direction = 1;//默认箭头是朝下的

    //view
    private TextView tv_man;
    private TextView tv_woman;
    private PopupWindow window;
    public LcsPageTabIndicator(@NonNull Context context) {
        this(context, (AttributeSet)null);
    }

    public LcsPageTabIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LcsPageTabIndicator(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
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
        this.pagerAdapter = (MyAdapter) viewPager.getAdapter();
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
                return LcsPageTabIndicator.this.pagerAdapter.getCount();
            }

            public IPagerIndicator getIndicator(Context context) {
                if (LcsPageTabIndicator.this.getIndicatorViewAdapter != null) {
                    IPagerIndicator iPagerIndicator = LcsPageTabIndicator.this.getIndicatorViewAdapter.getIndicator(context);
                    if (iPagerIndicator != null) {
                        return iPagerIndicator;
                    }
                }

                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(new Integer[]{LcsPageTabIndicator.this.mIndicatorColor});
                indicator.setLineHeight((float) LcsPageTabIndicator.this.mIndicatorHeight);
                indicator.setMode(2);
                indicator.setYOffset((float)IndicatorUtils.dp2px(context, 5.0D));
                indicator.setLineWidth((float)IndicatorUtils.dp2px(context, 25.0D));
                indicator.setRoundRadius((float)IndicatorUtils.dp2px(context, 3.0D));
                indicator.setStartInterpolator(new AccelerateInterpolator());
                indicator.setEndInterpolator(new DecelerateInterpolator(2.0F));
                return indicator;
            }


            public IPagerTitleView getTitleView(Context context, final int index) {
                if (LcsPageTabIndicator.this.getIndicatorViewAdapter != null) {
                    IPagerTitleView titleView = LcsPageTabIndicator.this.getIndicatorViewAdapter.getTitleView(context, index);
                    if (titleView != null) {
                        titleView.setText(LcsPageTabIndicator.this.pagerAdapter.getPageTitle(index).toString());
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

                final MyCustomView simplePagerTitleView = new MyCustomView(context);
                simplePagerTitleView.setText(LcsPageTabIndicator.this.pagerAdapter.getPageTitle(index).toString());
                simplePagerTitleView.setShowArrow(LcsPageTabIndicator.this.pagerAdapter.isShowArrow(index));
                simplePagerTitleView.setTextSize((float) LcsPageTabIndicator.this.mTextSize);
                simplePagerTitleView.setmNormalColor(LcsPageTabIndicator.this.mTextColor);
                simplePagerTitleView.setmSelectedColor(LcsPageTabIndicator.this.mSelectTextColor);
                simplePagerTitleView.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if(currentIndex!=index){//旧的item转到新的item

                            Toast.makeText(getContext(),"new index",Toast.LENGTH_SHORT).show();
                            viewPager.setCurrentItem(index);
                            arrow_direction = ARROW_DOWN;
                            currentIndex = index;
                            if(simplePagerTitleView.isShowArrow()){
                                simplePagerTitleView.setArrowDirection(arrow_direction);
                            }
                        }else {//重复点击此item

                            if(!simplePagerTitleView.isShowArrow()) return;//如果此item不包括箭头就不用判断后面的操作了

                            if(arrow_direction==ARROW_DOWN){
                                showPopwindow(simplePagerTitleView);
                                simplePagerTitleView.setArrowDirection(arrow_direction);
                                arrow_direction = ARROW_UP;
                            }else {
                                simplePagerTitleView.setArrowDirection(arrow_direction);
                                arrow_direction=ARROW_DOWN;
                            }

                        }
                    }
                });
                return simplePagerTitleView;
            }


        });
        return commonNavigator;
    }



    public void showPopwindow(MyCustomView view){
        if(getContext()==null) return;

        View contentView = LayoutInflater.from(getContext()).inflate(R.layout.pop_item, null, false);
        window = new PopupWindow(contentView, view.getWidth(), 150, true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        initView(contentView,view);
        window.showAsDropDown(view, 0, 0);
        window.setFocusable(false);
    }

    private void initView(View contentView, final MyCustomView titleView) {
        if(contentView==null||titleView==null) return;

        tv_man = contentView.findViewById(R.id.tv_man);
        tv_woman = contentView.findViewById(R.id.tv_woman);
        tv_man.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(window!=null&&window.isShowing()){
                    window.dismiss();
                }
                titleView.setText("男性");
                arrow_direction = ARROW_DOWN;
                if(titleView.isShowArrow()){
                    titleView.setArrowDirection(arrow_direction);
                }
            }
        });
        tv_woman.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(window!=null&&window.isShowing()){
                    window.dismiss();
                }
                titleView.setText("女性");
                arrow_direction = ARROW_DOWN;
                if(titleView.isShowArrow()){
                    titleView.setArrowDirection(arrow_direction);
                }
            }
        });
        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

            }
        });

    }
}