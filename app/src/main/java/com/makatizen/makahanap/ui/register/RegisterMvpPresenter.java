package com.makatizen.makahanap.ui.register;

import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface RegisterMvpPresenter<V extends RegisterMvpView & BaseMvpView> extends Presenter<V> {
    void emailVerification(String email, String token);

    void getMakatizenData(String makatizenNumber);

    void registerAccount(MakahanapAccount account);

    boolean validatePassword(String password);

    void verifyMakatizenId(String id);

    boolean verifyPasswordIsMatch(String password, String repeatedPassword);

    void passwordToggle(boolean showPass);

    void smsVerification(String phoneNumber);
}
