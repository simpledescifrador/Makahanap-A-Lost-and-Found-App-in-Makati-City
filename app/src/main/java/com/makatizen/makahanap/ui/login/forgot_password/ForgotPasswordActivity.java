package com.makatizen.makahanap.ui.login.forgot_password;

import android.os.Bundle;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class ForgotPasswordActivity extends BaseActivity implements ForgotPasswordMvpView {

    @Inject
    ForgotPasswordMvpPresenter<ForgotPasswordMvpView> mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);
        setContentView(R.layout.activity_forgot_password);
        setUnBinder(ButterKnife.bind(this));
        init();
    }

    @Override
    protected void init() {
        showBackButton(true);
        setTitle("Forgot Password");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }
}
