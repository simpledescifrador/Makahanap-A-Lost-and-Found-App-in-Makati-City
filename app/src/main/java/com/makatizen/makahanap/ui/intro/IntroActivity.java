package com.makatizen.makahanap.ui.intro;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.ui.login.LoginActivity;
import com.makatizen.makahanap.ui.main.MainActivity;
import com.makatizen.makahanap.ui.register.RegisterActivity;
import javax.inject.Inject;

public class IntroActivity extends BaseActivity implements OnPageChangeListener, IntroMvpView {

    @Inject
    IntroAdapter mIntroAdapter;

    @BindView(R.id.intro_navigation)
    LinearLayout mIntroNavigation;

    @BindView(R.id.intro_next_btn)
    Button mIntroNextBtn;

    @BindView(R.id.intro_pager)
    ViewPager mIntroPager;

    @BindView(R.id.intro_parent_layout)
    ConstraintLayout mIntroParentLayout;

    @BindView(R.id.intro_prev_btn)
    Button mIntroPrevBtn;

    @BindView(R.id.intro_repeat_cb)
    CheckBox mIntroRepeatCb;

    @BindView(R.id.intro_started_btn)
    Button mIntroStartedBtn;

    @Inject
    IntroPresenter<IntroMvpView> mPresenter;

    private int mCurrentPage;

    private TextView[] mDots;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);
        mPresenter.checkIfIntroWillBeLoaded();

        // Check if we're running on Android 5.0 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    protected void onDestroy() {
        mPresenter.detachView();
        mIntroAdapter = null;
        mDots = null;
        super.onDestroy();
    }

    @Override
    public void loadIntro() {
        setContentView(R.layout.activity_intro);
        setUnBinder(ButterKnife.bind(this));
        init();
    }

    @Override
    public void loadLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public void loadMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    public void onPageScrollStateChanged(final int i) {

    }

    @Override
    public void onPageScrolled(final int i, final float v, final int i1) {

    }

    @Override
    public void onPageSelected(final int position) {
        addDotsIndicator(position);
        mCurrentPage = position;
        mPresenter.onPageChange(position);
    }

    @Override
    public void onSelectedPage(final int position) {
        switch (position) {
            case 0:
                mIntroNextBtn.setEnabled(true);
                mIntroPrevBtn.setEnabled(false);
                mIntroPrevBtn.setVisibility(View.INVISIBLE);
                mIntroNextBtn.setText("Start");
                mIntroNextBtn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                mIntroPrevBtn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                mIntroPrevBtn.setText("");
                break;
            case 1:
                mIntroNextBtn.setEnabled(true);
                mIntroPrevBtn.setEnabled(true);
                mIntroPrevBtn.setVisibility(View.VISIBLE);
                mIntroNextBtn.setText("Next");
                mIntroNextBtn.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                mIntroPrevBtn.setText("Back");

                break;
            case 2:

                mIntroNextBtn.setEnabled(true);
                mIntroPrevBtn.setEnabled(true);
                mIntroPrevBtn.setVisibility(View.VISIBLE);
                mIntroNextBtn.setText("Next");
                mIntroPrevBtn.setText("Back");
                break;
            case 3:
                mIntroNextBtn.setEnabled(true);
                mIntroPrevBtn.setEnabled(true);
                mIntroPrevBtn.setVisibility(View.VISIBLE);
                mIntroNextBtn.setText("Next");
                mIntroPrevBtn.setText("Back");
                break;
            case 4:
                mIntroNextBtn.setEnabled(true);
                mIntroPrevBtn.setEnabled(true);
                mIntroPrevBtn.setVisibility(View.VISIBLE);
                mIntroNextBtn.setText("Next");
                mIntroPrevBtn.setText("Back");
                break;
            case 5:
                mIntroRepeatCb.setVisibility(View.VISIBLE);
                mIntroNextBtn.setEnabled(true);
                mIntroPrevBtn.setEnabled(true);
                mIntroPrevBtn.setVisibility(View.VISIBLE);
                mIntroNextBtn.setText("Get Started");
                mIntroPrevBtn.setText("Back");
                break;
        }
    }

    @OnClick({R.id.intro_started_btn, R.id.intro_next_btn, R.id.intro_prev_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.intro_started_btn:
                startActivity(new Intent(this, RegisterActivity.class));
                finish();
                break;
            case R.id.intro_next_btn:
                if (mCurrentPage == mDots.length - 1) {
                    startActivity(new Intent(this, RegisterActivity.class));
                    finish();
                }
                mIntroPager.setCurrentItem(mCurrentPage + 1);
                break;
            case R.id.intro_prev_btn:
                mIntroPager.setCurrentItem(mCurrentPage - 1);
                break;
        }
    }

    @Override
    protected void init() {
        mIntroPager.setAdapter(mIntroAdapter);
        addDotsIndicator(0);
        mIntroPager.addOnPageChangeListener(this);

        mIntroRepeatCb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean isChecked) {
                //Remove or Show again Intro on App Start
                mPresenter.removeIntro(isChecked);
            }
        });

    }

    private void addDotsIndicator(int position) {
        mIntroNavigation.removeAllViews();
        mDots = new TextView[mIntroAdapter.getCount()];
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(Color.WHITE);

            mIntroNavigation.addView(mDots[i]);
        }
        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }
}
