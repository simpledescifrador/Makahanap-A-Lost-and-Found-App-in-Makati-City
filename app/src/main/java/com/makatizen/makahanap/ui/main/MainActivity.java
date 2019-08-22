package com.makatizen.makahanap.ui.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.BottomNavigationView.OnNavigationItemSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.ui.main.account.AccountFragment;
import com.makatizen.makahanap.ui.main.feed.FeedFragment;
import com.makatizen.makahanap.ui.main.home.HomeFragment;
import com.makatizen.makahanap.ui.main.map.MapFragment;
import com.makatizen.makahanap.ui.main.notification.NotificationFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainMvpView, NotificationFragment.NotificationUpdateListener {

    @BindView(R.id.main_bnav_navigation)
    BottomNavigationView mMainBnavNavigation;

    @BindView(R.id.main_vp_content)
    ViewPager mMainVpContent;

    @Inject
    MainMvpPresenter<MainMvpView> mPresenter;

    boolean doubleBackToExitPressedOnce = false;

    private MenuItem mPrevMenuItem;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            String accountId = intent.getStringExtra("account_id");
            int unViewedNotif = intent.getIntExtra("total_unviewed_notification", 0);
            mPresenter.setNotificationBadge(unViewedNotif);
        }
    };

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
    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof NotificationFragment) {
            NotificationFragment notificationFragment = (NotificationFragment) fragment;
            notificationFragment.setOnNotificationUpdateListener(this);
        }
    }

    @Override
    protected void init() {

        mPresenter.loadNotificationBadge();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    public void addBadge(int position, int number) {
        if (number == 0) {
            return;
        }
        BottomNavigationMenuView bottomNavigationView = (BottomNavigationMenuView) mMainBnavNavigation
                .getChildAt(0);
        android.view.View v = bottomNavigationView.getChildAt(position);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;
        android.view.View badge = LayoutInflater.from(this)
                .inflate(R.layout.notification_badge, itemView, true);
        TextView count = badge.findViewById(R.id.notif_count);
        count.setVisibility(android.view.View.VISIBLE);
        if (number < 10 && number > 0) {
            count.setText(String.valueOf(number));
        } else {
            count.setText("9+");
        }
    }

    @Override
    public void setUnViewedNotification(int number) {
        addBadge(3, number);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mBroadcastReceiver, new IntentFilter("com.makatizen.makahanap.notification"));
    }
}
