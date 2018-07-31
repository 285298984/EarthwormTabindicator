package com.inidicator.callback;

import android.content.Context;

import com.inidicator.impl.IPagerIndicator;
import com.inidicator.impl.IPagerTitleView;

public class OnGetIndicatorViewAdapter {
    public IPagerTitleView getTitleView(Context context, int index){
        return null;
    }

    public IPagerIndicator getIndicator(Context context){
        return null;
    }
}
