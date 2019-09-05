package com.makatizen.makahanap.ui.login;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface LoginMvpPresenter<V extends LoginMvpView & BaseMvpView> extends Presenter<V> {

    void doAppLogin(String makatizenNumber, String password);

    boolean validateLoginDetails(String makatizenNumber, String password);

    void passwordToggle(boolean showPass);
}
