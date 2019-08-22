package com.makatizen.makahanap.ui.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();

    @Inject
    MainViewPagerAdapter(final FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public Fragment getItem(final int i) {
        return mFragmentList.get(i);
    }

    void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }
}
