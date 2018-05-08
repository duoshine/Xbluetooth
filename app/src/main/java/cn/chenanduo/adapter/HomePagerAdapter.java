package cn.chenanduo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by chen on 2017
 */

public class HomePagerAdapter extends FragmentStatePagerAdapter {
    private List<String> mDatas;
    private List<Fragment> fragments;
    public HomePagerAdapter(FragmentManager fm, List<Fragment> fragments, List mDatas) {
        super(fm);
        this.mDatas = mDatas;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mDatas.get(position);
    }
}
