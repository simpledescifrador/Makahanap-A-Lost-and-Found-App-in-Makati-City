package com.makatizen.makahanap.ui.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.ui.main.account.AccountFragment;
import com.makatizen.makahanap.ui.main.feed.FeedFragment;
import com.makatizen.makahanap.ui.main.home.HomeFragment;
import com.makatizen.makahanap.ui.main.map.MapFragment;
import com.makatizen.makahanap.ui.main.notification.NotificationFragment;
import javax.inject.Inject;

public class MainActivity extends BaseActivity implements MainMvpView {

    @BindView(R.id.main_bnav_navigation)
    BottomNavigationView mMainBnavNavigation;

    @BindView(R.id.main_vp_content)
    ViewPager mMainVpContent;

    @Inject
    MainMvpPresenter<MainMvpView> mPresenter;

    private MenuItem mPrevMenuItem;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);
        setContentView(R.layout.activity_main);
        setUnBinder(ButterKnife.bind(this));

        init();
        setUpViewPager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    protected void init() {
        //Set-up Bottom Navigation Listener
        mMainBnavNavigation.setOnNavigationItemSelectedListener(new OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull final MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.main_nav_home:
                        mMainVpContent.setCurrentItem(0);
                        break;
                    case R.id.main_nav_feed:
                        mMainVpContent.setCurrentItem(1);
                        break;
                    case R.id.main_nav_map:
                        mMainVpContent.setCurrentItem(2);
                        break;
                    case R.id.main_nav_notification:
                        mMainVpContent.setCurrentItem(3);
                        break;
                    case R.id.main_nav_account:
                        mMainVpContent.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });

        //Set-Up ViewPager OnPageChange Listener
        mMainVpContent.addOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(final int i) {

            }

            @Override
            public void onPageScrolled(final int i, final float v, final int i1) {

            }

            @Override
            public void onPageSelected(final int i) {
                if (mPrevMenuItem != null) {
                    mPrevMenuItem.setChecked(false);
                } else {
                    mMainBnavNavigation.getMenu().getItem(0).setChecked(false);
                }
                mMainBnavNavigation.getMenu().getItem(i).setChecked(true);

                mPrevMenuItem = mMainBnavNavigation.getMenu().getItem(i);
            }
        });
    }

    private void setUpViewPager() {
        // TODO: 6/8/19 Fix Dependency Injection
        final MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mainViewPagerAdapter.addFragment(new HomeFragment());
        mainViewPagerAdapter.addFragment(new FeedFragment());
        mainViewPagerAdapter.addFragment(new MapFragment());
        mainViewPagerAdapter.addFragment(new NotificationFragment());
        mainViewPagerAdapter.addFragment(new AccountFragment());

        mMainVpContent.setOffscreenPageLimit(mainViewPagerAdapter.getCount() - 1);
        mMainVpContent.setAdapter(mainViewPagerAdapter);
    }
}
