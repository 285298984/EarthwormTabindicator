package com.tab;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.tab.adapter.LcsCustomAdapter;
import com.tab.fragment.BlankFragment;
import com.tab.fragment.VideoFragment;
import com.tab.views.BaseTabIndicator;
import com.tab.views.LcsCustomOnGetIndicatorViewAdapter;
import com.tab.views.LcsCustomTabView;
import com.tab.views.LcsPageTabIndicator;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import static com.tab.views.LcsPageTabIndicator.ARROW_DOWN;
import static com.tab.views.LcsPageTabIndicator.ARROW_UP;
import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity {

    private BaseTabIndicator tabIndicator;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private List<String> tab_list;
    private LcsCustomAdapter adapter;
    private BlankFragment fragment1;
    private BlankFragment fragment2;
    private VideoFragment fragment3;
    private BlankFragment fragment4;
    private BlankFragment fragment5;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        tabIndicator = findViewById(R.id.tabIndicator);
        viewPager = findViewById(R.id.viewpage);

        initData();

        adapter = new LcsCustomAdapter(getSupportFragmentManager(),fragmentList,tab_list);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragmentList.size()-1);
        tabIndicator.setGetIndicatorViewAdapter(new LcsCustomOnGetIndicatorViewAdapter() {
            @Override
            public BaseTabIndicator getTabIndicator() {
                return tabIndicator;
            }

            @Override
            public void childTabSelected(String type) {
                fragment1.reloadData(type);
            }

        });
        tabIndicator.setupWithViewPager(viewPager);

    }

    private void initData() {
        fragmentList = new ArrayList<>();
        fragment1 = new BlankFragment();
        fragment2 = new BlankFragment();
        fragment3 = new VideoFragment();
        fragment4 = new BlankFragment();
        fragment5 = new BlankFragment();
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        fragmentList.add(fragment4);
        fragmentList.add(fragment5);


        tab_list = new ArrayList<>();
        tab_list.add("全部");
        tab_list.add("创业");
        tab_list.add("视频");
        tab_list.add("时尚");
        tab_list.add("生活");
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(App.instance.VideoPlaying!=null)
        {
            if(App.instance.VideoPlaying.currentState== JCVideoPlayer.CURRENT_STATE_PLAYING)
            {
                App.instance.VideoPlaying.startButton.performClick();
            }else if (App.instance.VideoPlaying.currentState== JCVideoPlayer.CURRENT_STATE_PREPAREING)
            {
                JCVideoPlayer.releaseAllVideos();
            }
        }
    }
}
