package test.com.tablayoutcustom.views;


import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sina.licaishi.commonuilib.indicator.IPagerNavigator;
import com.sina.licaishi.commonuilib.indicator.callback.OnNavigatorScrollListener;
import com.sina.licaishi.commonuilib.indicator.helper.NavigatorHelper;
import com.sina.licaishi.commonuilib.indicator.impl.CommonNavigatorAdapter;
import com.sina.licaishi.commonuilib.indicator.impl.IMeasureablePagerTitleView;
import com.sina.licaishi.commonuilib.indicator.impl.IPagerIndicator;
import com.sina.licaishi.commonuilib.indicator.impl.IPagerTitleView;
import com.sina.licaishi.commonuilib.indicator.impl.PositionData;

import java.util.ArrayList;
import java.util.List;

import test.com.tablayoutcustom.R;


public class MyCommonNavigator extends FrameLayout implements IPagerNavigator, OnNavigatorScrollListener {
    private HorizontalScrollView mScrollView;
    private LinearLayout mTitleContainer;
    private LinearLayout mIndicatorContainer;
    private IPagerIndicator mIndicator;
    private boolean mAdjustMode;
    private boolean mEnablePrivotScroll;
    private float mScrollPivotX = 0.5F;
    private boolean mSmoothScroll = true;
    private int mLeftPadding;
    private int mRightPadding;
    private boolean mFollowTouch = true;
    private boolean mSkimOver;
    private boolean mIndicatorOnTop;
    private boolean mReselectWhenLayout = true;
    private NavigatorHelper mNavigatorHelper = new NavigatorHelper();
    private CommonNavigatorAdapter adapter;
    private List<PositionData> mPositionDataList = new ArrayList();
    private DataSetObserver mObserver = new DataSetObserver() {
        public void onChanged() {
            MyCommonNavigator.this.mNavigatorHelper.setTotalCount(MyCommonNavigator.this.adapter.getCount());
            MyCommonNavigator.this.init();
        }
    };

    public MyCommonNavigator(@NonNull Context context) {
        super(context);
        this.mNavigatorHelper.setOnNavigatorScrollListener(this);
    }

    public boolean isAdjustMode() {
        return this.mAdjustMode;
    }

    public void setAdjustMode(boolean mAdjustMode) {
        this.mAdjustMode = mAdjustMode;
    }

    public CommonNavigatorAdapter getAdapter() {
        return this.adapter;
    }

    public void setAdapter(CommonNavigatorAdapter adapter) {
        if (this.adapter != adapter) {
            if (this.adapter != null) {
                this.adapter.unregisterDataSetObserver(this.mObserver);
            }

            this.adapter = adapter;
            if (this.adapter != null) {
                this.adapter.registerDataSetObserver(this.mObserver);
                this.mNavigatorHelper.setTotalCount(this.adapter.getCount());
                if (this.mTitleContainer != null) {
                    this.adapter.notifyDataSetChanged();
                }
            } else {
                this.mNavigatorHelper.setTotalCount(0);
                this.init();
            }

        }
    }

    private void init() {
        this.removeAllViews();
        View root;
        if (this.mAdjustMode) {
            root = LayoutInflater.from(this.getContext()).inflate(R.layout.my_lcs_indicator_pager_navigator_layout_no_scroll, this);
        } else {
            root = LayoutInflater.from(this.getContext()).inflate(R.layout.lcs_indicator_pager_navigator_layout, this);
        }

        this.mScrollView = (HorizontalScrollView)root.findViewById(R.id.scroll_view);
        this.mTitleContainer = (LinearLayout)root.findViewById(R.id.title_container);
        this.mTitleContainer.setPadding(this.mLeftPadding, 0, this.mRightPadding, 0);
        this.mIndicatorContainer = (LinearLayout)root.findViewById(R.id.indicator_container);
        if (this.mIndicatorOnTop) {
            this.mIndicatorContainer.getParent().bringChildToFront(this.mIndicatorContainer);
        }

        this.initTitlesAndIndicator();
    }

    private void initTitlesAndIndicator() {
        int i = 0;

        for(int j = this.mNavigatorHelper.getTotalCount(); i < j; ++i) {
            IPagerTitleView v = this.adapter.getTitleView(this.getContext(), i);
            if (v instanceof View) {
                View view = (View)v;
                LayoutParams lp;
                LinearLayout.LayoutParams llp;
                if (this.mAdjustMode) {
                    lp = new LayoutParams(0, -1);
                    //todo 这里强转有个问题～梁超杰
                    //我们见到的指示器一般都是线性的
                    llp = new LinearLayout.LayoutParams(0, -1);
                    llp.weight = this.adapter.getTitleWeight(this.getContext(), i);
                    this.mTitleContainer.addView(view, llp);
                } else {
                    lp = new LayoutParams(-2, -1);
                    this.mTitleContainer.addView(view, lp);
                }


            }
        }

        if (this.adapter != null) {
            this.mIndicator = this.adapter.getIndicator(this.getContext());
            if (this.mIndicator instanceof View) {
                android.widget.FrameLayout.LayoutParams lp = new android.widget.FrameLayout.LayoutParams(-1, -1);
                this.mIndicatorContainer.addView((View)this.mIndicator, lp);
            }
        }

    }

    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.adapter != null) {
            this.preparePositionData();
            if (this.mIndicator != null) {
                this.mIndicator.onPositionDataProvide(this.mPositionDataList);
            }

            if (this.mReselectWhenLayout && this.mNavigatorHelper.getScrollState() == 0) {
                this.onPageSelected(this.mNavigatorHelper.getCurrentIndex());
                this.onPageScrolled(this.mNavigatorHelper.getCurrentIndex(), 0.0F, 0);
            }
        }

    }

    private void preparePositionData() {
        this.mPositionDataList.clear();

        for(int i = 0; i < this.mNavigatorHelper.getTotalCount(); ++i) {
            PositionData data = new PositionData();
            View view = this.mTitleContainer.getChildAt(i);
            if (view != null) {
                data.mLeft = view.getLeft();
                data.mTop = view.getTop();
                data.mRight = view.getRight();
                data.mBottom = view.getBottom();
                if (view instanceof IMeasureablePagerTitleView) {
                    IMeasureablePagerTitleView v1 = (IMeasureablePagerTitleView)view;
                    data.mContentLeft = v1.getContentLeft();
                    data.mContentTop = v1.getContentTop();
                    data.mContentRight = v1.getContentRight();
                    data.mContentBottom = v1.getContentBottom();
                } else {
                    data.mContentLeft = data.mLeft;
                    data.mContentTop = data.mTop;
                    data.mContentRight = data.mRight;
                    data.mContentBottom = data.mBottom;
                }
            }

            this.mPositionDataList.add(data);
        }

    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (this.adapter != null) {
            this.mNavigatorHelper.onPageScrolled(position, positionOffset, positionOffsetPixels);
            if (this.mIndicator != null) {
                this.mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            if (this.mScrollView != null && this.mPositionDataList.size() > 0 && position >= 0 && position < this.mPositionDataList.size() && this.mFollowTouch) {
                int currentPosition = Math.min(this.mPositionDataList.size() - 1, position);
                int nextPosition = Math.min(this.mPositionDataList.size() - 1, position + 1);
                PositionData current = (PositionData)this.mPositionDataList.get(currentPosition);
                PositionData next = (PositionData)this.mPositionDataList.get(nextPosition);
                float scrollTo = (float)current.horizontalCenter() - (float)this.mScrollView.getWidth() * this.mScrollPivotX;
                float nextScrollTo = (float)next.horizontalCenter() - (float)this.mScrollView.getWidth() * this.mScrollPivotX;
                this.mScrollView.scrollTo((int)(scrollTo + (nextScrollTo - scrollTo) * positionOffset), 0);
            }
        }

    }

    public void onPageSelected(int position) {
        if (this.adapter != null) {
            this.mNavigatorHelper.onPageSelected(position);
            if (this.mIndicator != null) {
                this.mIndicator.onPageSelected(position);
            }
        }

    }

    public void onPageScrollStateChanged(int state) {
        if (this.adapter != null) {
            this.mNavigatorHelper.onPageScrollStateChanged(state);
            if (this.mIndicator != null) {
                this.mIndicator.onPageScrollStateChanged(state);
            }
        }

    }

    public void onAttachToIndicator() {
        this.init();
    }

    public void onDetachFromIndicator() {
    }

    public void notifyDataSetChanged() {
        if (this.adapter != null) {
            this.adapter.notifyDataSetChanged();
        }

    }

    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        if (this.mTitleContainer != null) {
            View childAt = this.mTitleContainer.getChildAt(index);
            if (childAt instanceof IPagerTitleView) {
                ((IPagerTitleView)childAt).onEnter(index, totalCount, enterPercent, leftToRight);
            }

        }
    }

    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        if (this.mTitleContainer != null) {
            View childAt = this.mTitleContainer.getChildAt(index);
            if (childAt instanceof IPagerTitleView) {
                ((IPagerTitleView)childAt).inLeave(index, totalCount, leavePercent, leftToRight);
            }

        }
    }

    public void onSelected(int index, int totalCount) {
        if (this.mTitleContainer != null) {
            View childAt = this.mTitleContainer.getChildAt(index);
            if (childAt instanceof IPagerTitleView) {
                ((IPagerTitleView)childAt).onSelected(index, totalCount);
            }

            if (!this.mAdjustMode && !this.mFollowTouch && this.mScrollView != null && this.mPositionDataList.size() > 0) {
                int currentIndex = Math.min(this.mPositionDataList.size() - 1, index);
                PositionData currData = (PositionData)this.mPositionDataList.get(currentIndex);
                if (this.mEnablePrivotScroll) {
                    float scrollTo = (float)currData.horizontalCenter() - (float)this.mScrollView.getWidth() * this.mScrollPivotX;
                    if (this.mSmoothScroll) {
                        this.mScrollView.smoothScrollTo((int)scrollTo, 0);
                    } else {
                        this.mScrollView.scrollTo((int)scrollTo, 0);
                    }
                } else if (this.mScrollView.getScaleX() > (float)currData.mLeft) {
                    if (this.mSmoothScroll) {
                        this.mScrollView.smoothScrollTo(currData.mLeft, 0);
                    } else {
                        this.mScrollView.scrollTo(currData.mLeft, 0);
                    }
                } else if (this.mScrollView.getScaleX() + (float)this.getWidth() < (float)currData.mRight) {
                    if (this.mSmoothScroll) {
                        this.mScrollView.smoothScrollTo(currData.mRight - this.getWidth(), 0);
                    } else {
                        this.mScrollView.scrollTo(currData.mRight - this.getWidth(), 0);
                    }
                }
            }

        }
    }

    public void onDeselected(int index, int totalCount) {
        if (this.mTitleContainer != null) {
            View childAt = this.mTitleContainer.getChildAt(index);
            if (childAt instanceof IPagerTitleView) {
                ((IPagerTitleView)childAt).onDeselect(index, totalCount);
            }

        }
    }

    public void setScrollPivotX(float mScrollPivotX) {
        this.mScrollPivotX = mScrollPivotX;
    }

    public float getScrollPivotX() {
        return this.mScrollPivotX;
    }

    public boolean isEnablePrivotScroll() {
        return this.mEnablePrivotScroll;
    }

    public void setEnablePrivotScroll(boolean mEnablePrivotScroll) {
        this.mEnablePrivotScroll = mEnablePrivotScroll;
    }

    public boolean isSkimOver() {
        return this.mSkimOver;
    }

    public void setSkimOver(boolean mSkimOver) {
        this.mSkimOver = mSkimOver;
        this.mNavigatorHelper.setSkimOver(mSkimOver);
    }

    public IPagerIndicator getPagerIndicator() {
        return this.mIndicator;
    }

    public IPagerTitleView getPagerTitleView(int index) {
        return this.mTitleContainer == null ? null : (IPagerTitleView)this.mTitleContainer.getChildAt(index);
    }

    public LinearLayout getTitleContainer() {
        return this.mTitleContainer;
    }
}

