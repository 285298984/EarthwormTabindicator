package test.com.tablayoutcustom;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class MyAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> list;
    private List<String> tab_list;
    public MyAdapter(FragmentManager fm, List<Fragment> list, List<String> tab_list) {
        super(fm);
        this.list = list;
        this.tab_list = tab_list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list==null?0:list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tab_list.get(position%tab_list.size());
    }

}
