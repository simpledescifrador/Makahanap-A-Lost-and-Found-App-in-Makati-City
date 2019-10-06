package com.makatizen.makahanap.ui.register.email_verification;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface EmailVerificationMvpPresenter<V extends EmailVerificationMvpView & BaseMvpView> extends Presenter<V> {
    void startExpirationCountdown();

    void verifyCode(String email, String code);

    void resendCode(String email, String token);

    boolean validateCode(String code);

    void emailVerification(String email, String token);
}
