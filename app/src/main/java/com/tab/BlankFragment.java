package com.tab;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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
