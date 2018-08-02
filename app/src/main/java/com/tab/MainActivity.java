package com.tab;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import com.inidicator.TabIndicator;
import com.tab.adapter.CustomPagerAdapter;
import com.tab.fragment.BlankFragment;
import com.tab.fragment.VideoFragment;
import com.tab.views.LcsCustomOnGetIndicatorViewAdapter;

import java.util.ArrayList;
import java.util.List;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;



public class MainActivity extends AppCompatActivity {

    private TabIndicator tabIndicator;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private List<String> tab_list;
    private CustomPagerAdapter adapter;
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

        adapter = new CustomPagerAdapter(getSupportFragmentManager(),fragmentList,tab_list);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragmentList.size()-1);
        tabIndicator.setGetIndicatorViewAdapter(new LcsCustomOnGetIndicatorViewAdapter() {
            @Override
            public TabIndicator getTabIndicator() {
                return tabIndicator;
            }

            @Override
            public void childTabSelected(int index, String type) {
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
