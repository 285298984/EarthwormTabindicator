package com.tab.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.inidicator.callback.OnGetIndicatorViewAdapter;
import com.inidicator.impl.IPagerIndicator;
import com.inidicator.impl.IPagerTitleView;
import com.tab.R;
import com.tab.adapter.LcsCustomAdapter;

/**
 * 自定义View在此处理
 */
public abstract class LcsCustomOnGetIndicatorViewAdapter extends OnGetIndicatorViewAdapter {
    //view
    private TextView tv_man;
    private TextView tv_woman;
    private PopupWindow window;
    private Context context;
    public static int ARROW_UP = 0; //箭头朝上
    public static int ARROW_DOWN = 1;

    private int arrow_direction = 1;//默认箭头是朝下的
    private int currentIndex;



    private BaseTabIndicator tabIndicator;
    public  abstract BaseTabIndicator getTabIndicator();


    public  abstract void childTabSelected(String type);

    @Override
    public void getSelectedIndex(int index) {
        currentIndex = index;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {

        if(getTabIndicator()==null||context==null) return null;

        this.context = context;
        tabIndicator = getTabIndicator();
        final LcsCustomTabView simplePagerTitleView = new LcsCustomTabView(context);
        simplePagerTitleView.setText(tabIndicator.getPagerAdapter().getPageTitle(index).toString());
        simplePagerTitleView.setShowArrow(((LcsCustomAdapter)tabIndicator.getPagerAdapter()).isShowArrow(index));
        simplePagerTitleView.setTextSize((float) tabIndicator.getmTextSize());
        simplePagerTitleView.setmNormalColor(tabIndicator.getmTextColor());
        simplePagerTitleView.setmSelectedColor(tabIndicator.getmSelectTextColor());
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(currentIndex!=index){//旧的item转到新的item

                    tabIndicator.getViewPager().setCurrentItem(index);
                    arrow_direction = ARROW_DOWN;
                    currentIndex = index;
                    if(simplePagerTitleView.isShowArrow()){
                        simplePagerTitleView.setArrowDirection(arrow_direction);
                    }
                }else {//重复点击此item

                    if(!simplePagerTitleView.isShowArrow()) return;//如果此item不包括箭头就不用判断后面的操作了

                    if(arrow_direction==ARROW_DOWN){
                        showPopwindow(simplePagerTitleView,index);
                    }else {
                        dismissPopWindow();
                        arrow_direction=ARROW_DOWN;
                        simplePagerTitleView.setArrowDirection(arrow_direction);

                    }

                }
            }
        });
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        return super.getIndicator(context);
    }


    public void showPopwindow(LcsCustomTabView view, int index) {

        arrow_direction = ARROW_UP;
        view.setArrowDirection(arrow_direction);

        if(context==null) return;
        View contentView = LayoutInflater.from(context).inflate(R.layout.pop_item, null, false);
        window = new PopupWindow(contentView, view.getWidth(), 150, true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        window.setFocusable(false);//要在显示之前设置，为了防止和点击其他tab冲突，所以选择不获取焦点
        initView(contentView,view,index);
        window.showAsDropDown(view, 0, 0);
    }

    public void dismissPopWindow() {
        if(window!=null&& window.isShowing()){
            window.dismiss();
        }
    }

    private void initView(View contentView, final LcsCustomTabView titleView,int index) {
        if(contentView==null||titleView==null) return;

        tv_man = contentView.findViewById(R.id.tv_man);
        tv_woman = contentView.findViewById(R.id.tv_woman);
        tv_man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(window!=null&&window.isShowing()){
                    window.dismiss();
                }
                titleView.setText("男性");
                if(titleView.isShowArrow()){
                    arrow_direction = ARROW_DOWN;
                    titleView.setArrowDirection(arrow_direction);
                }
                    childTabSelected("男性");
            }
        });
        tv_woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(window!=null&&window.isShowing()){
                    window.dismiss();
                }
                titleView.setText("女性");
                if(titleView.isShowArrow()){
                    arrow_direction = ARROW_DOWN;
                    titleView.setArrowDirection(arrow_direction);
                }
                childTabSelected("女性");
            }
        });

    }
}
