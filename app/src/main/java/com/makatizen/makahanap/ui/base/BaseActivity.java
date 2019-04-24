package com.makatizen.makahanap.ui.base;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;
import butterknife.Unbinder;
import com.makatizen.makahanap.MakahanapApplication;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.di.components.ActivityFragmentComponent;
import com.makatizen.makahanap.di.components.DaggerActivityFragmentComponent;
import com.makatizen.makahanap.di.modules.ActivityFragmentModule;
import com.makatizen.makahanap.utils.AppUtils;

public abstract class BaseActivity extends AppCompatActivity implements BaseMvpView {

    private ActivityFragmentComponent mActivityFragmentComponent;

    private ProgressDialog mProgressDialog;

    private Unbinder mUnbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityFragmentComponent = DaggerActivityFragmentComponent.builder()
                .activityFragmentModule(new ActivityFragmentModule(this))
                .applicationComponent(MakahanapApplication.getApplicationComponent())
                .build();
    }

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }

    public ActivityFragmentComponent getActivityFragmentComponent() {
        return mActivityFragmentComponent;
    }

    @Override
    public void hideKeyboard() {
        AppUtils.hideKeyboard(this);
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    @Override
    public boolean isNetworkConnected() {
        return AppUtils.isNetworkAvailable(getApplicationContext());
    }

    @Override
    public void onError(@StringRes int resId) {
        Toast.makeText(this, getString(resId), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnbinder = unBinder;
    }

    public void showBackButton(boolean shouldShow) {
        if (getSupportActionBar() != null) {
            Drawable backArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
            backArrow.setColorFilter(ContextCompat.getColor(this, android.R.color.white),
                    PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(backArrow);
            getSupportActionBar().setHomeButtonEnabled(shouldShow);
            getSupportActionBar().setDisplayHomeAsUpEnabled(shouldShow);
        }
    }

    @Override
    public void showLoading() {
        hideLoading();
        mProgressDialog = AppUtils.showLoadingDialog(this);
    }

    protected abstract void init();
}
