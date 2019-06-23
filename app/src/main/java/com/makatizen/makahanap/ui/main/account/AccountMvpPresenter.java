package com.makatizen.makahanap.ui.main.account;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface AccountMvpPresenter<V extends AccountMvpView & BaseMvpView> extends Presenter<V> {
    void loadAccountData();
    void requestAccountLogout();
}
