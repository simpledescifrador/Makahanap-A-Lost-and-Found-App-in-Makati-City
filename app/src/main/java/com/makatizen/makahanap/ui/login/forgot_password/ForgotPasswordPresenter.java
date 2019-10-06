package com.makatizen.makahanap.ui.login.forgot_password;

import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.ui.base.BasePresenter;

import javax.inject.Inject;

public class ForgotPasswordPresenter<V extends ForgotPasswordMvpView> extends BasePresenter<V> implements ForgotPasswordMvpPresenter<V> {

    @Inject
    ForgotPasswordPresenter(DataManager dataManager) {
        super(dataManager);
    }
}
