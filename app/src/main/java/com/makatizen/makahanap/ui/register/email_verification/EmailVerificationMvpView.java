package com.makatizen.makahanap.ui.register.email_verification;

import com.makatizen.makahanap.ui.base.BaseMvpView;

public interface EmailVerificationMvpView extends BaseMvpView {
    void setExpirationTime(long expirationTime);

    void setCodeError(String error);

    void onSuccessfulValidation();

    void onSuccessResendVerificationCode();
}
