package com.makatizen.makahanap.ui.login;

import com.makatizen.makahanap.ui.base.BaseMvpView;

public interface LoginMvpView extends BaseMvpView {

    void onLoginFailed();

    void onLoginRequest();

    void onLoginSuccessful(int accountId);

    void removeErrors();

    void setMakatizenNumberError(int stringId);

    void setPasswordError(int stringId);
}
