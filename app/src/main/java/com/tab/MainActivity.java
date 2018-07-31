package com.tab;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import com.tab.views.LcsPageTabIndicator;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private LcsPageTabIndicator tabIndicator;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private List<String> tab_list;
    private MyAdapter adapter;
    private BlankFragment fragment1;
    private BlankFragment fragment2;
    private BlankFragment fragment3;
    private BlankFragment fragment4;
    private BlankFragment fragment5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabIndicator = findViewById(R.id.tabIndicator);
        viewPager = findViewById(R.id.viewpage);

        initData();

        adapter = new MyAdapter(getSupportFragmentManager(),fragmentList,tab_list);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(fragmentList.size()-1);
        tabIndicator.setOnChildTabSelectedListener(new LcsPageTabIndicator.OnChildTabSelectedListener() {
            @Override
            public void onSelected(String type) {
                fragment1.reloadData(type);
            }
        });
        tabIndicator.setupWithViewPager(viewPager);

    }

    private void initData() {
        fragmentList = new ArrayList<>();
        fragment1 = new BlankFragment();
        fragment2 = new BlankFragment();
        fragment3 = new BlankFragment();
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
        tab_list.add("历史");
        tab_list.add("时尚");
        tab_list.add("生活");
    }

}
