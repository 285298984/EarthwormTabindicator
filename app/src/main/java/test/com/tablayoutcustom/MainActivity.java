package test.com.tablayoutcustom;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;


import java.util.ArrayList;
import java.util.List;

import test.com.tablayoutcustom.views.MyTabIndicator;

public class MainActivity extends AppCompatActivity {

    private MyTabIndicator tabIndicator;
    private ViewPager viewPager;
    private List<Fragment> fragmentList;
    private List<String> tab_list;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabIndicator = findViewById(R.id.tabIndicator);
        viewPager = findViewById(R.id.viewpage);

        initData();

        adapter = new MyAdapter(getSupportFragmentManager(),fragmentList,tab_list);
        viewPager.setAdapter(adapter);
        tabIndicator.setupWithViewPager(viewPager);

    }

    private void initData() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new BlankFragment());
        fragmentList.add(new BlankFragment());
        fragmentList.add(new BlankFragment());
        fragmentList.add(new BlankFragment());
        fragmentList.add(new BlankFragment());


        tab_list = new ArrayList<>();
        tab_list.add("科技");
        tab_list.add("创业");
        tab_list.add("历史");
        tab_list.add("家庭");
        tab_list.add("伦理");
    }

}
