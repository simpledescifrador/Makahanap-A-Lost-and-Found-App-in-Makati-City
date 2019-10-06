package com.makatizen.makahanap.ui.register.sms_verification;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface SmsVerificationMvpPresenter<V extends SmsVerificationMvpView & BaseMvpView> extends Presenter<V> {
    boolean validateCode(String code, int attempts);

    void resendRequest(String number, String requestId);

    void checkRequest(String code, String requestId);

    void cancelRequest(String requestId);

    void startExpirationCountdown();
}
