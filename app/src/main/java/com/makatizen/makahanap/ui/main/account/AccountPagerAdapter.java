package com.makatizen.makahanap.ui.main.account;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class AccountPagerAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList = new ArrayList<>();

    public AccountPagerAdapter(final FragmentManager fm) {
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

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return "About";
            case 1:
                return "Reports";
            case 2:
                return "Photos";
            default:
                return null;
        }
    }

    void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }
}
