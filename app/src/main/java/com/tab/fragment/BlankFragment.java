package com.tab.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.tab.R;
import com.tab.base.LazyFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends LazyFragment {


    private TextView textView;


    @Override
    public void lazyInit(View view, Bundle savedInstanceState) {
        textView=view.findViewById(R.id.tv_context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_blank;
    }


    public void reloadData(String type) {
        textView.setText(type);
    }
}
