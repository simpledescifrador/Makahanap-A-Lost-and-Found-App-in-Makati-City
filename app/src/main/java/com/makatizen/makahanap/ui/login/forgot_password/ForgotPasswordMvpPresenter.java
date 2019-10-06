package com.makatizen.makahanap.ui.login.forgot_password;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface ForgotPasswordMvpPresenter<V extends ForgotPasswordMvpView & BaseMvpView> extends Presenter<V> {
    void checkMakatizenNumber(String makatizenNumber);

    void sendEmailVerification(String email);

    boolean validatePassword(String password, String confPassword);

    void resetPassword(String makatizenNumber, String password);
}
