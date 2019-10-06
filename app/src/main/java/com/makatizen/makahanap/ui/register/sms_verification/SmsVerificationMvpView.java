package com.makatizen.makahanap.ui.register.sms_verification;

import com.makatizen.makahanap.ui.base.BaseMvpView;

public interface SmsVerificationMvpView extends BaseMvpView {
    void setCodeError(String error);

    void consumeAttempts();

    void onSuccessfulVerification();

    void resetAttempts();

    void onSuccessfulResendCode(String requestId);

    void setExpirationTime(long expirationTime);
}
