package com.makatizen.makahanap.ui.login.forgot_password;

import com.makatizen.makahanap.ui.base.BaseMvpView;

public interface ForgotPasswordMvpView extends BaseMvpView {
    void setMakatizenNumberError(String error);

    void onSuccessRegisteredMakatizen(String email);

    void successEmailVerification(String email);

    void setPasswordError(int resId);

    void setConfirmPasswordError(int resId);

    void onSuccessResetPassword();

    void onFailedResetPassword();

    void removeErrors();
}
