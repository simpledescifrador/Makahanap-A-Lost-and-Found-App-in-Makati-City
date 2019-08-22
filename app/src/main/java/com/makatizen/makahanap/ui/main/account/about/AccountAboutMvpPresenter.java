package com.makatizen.makahanap.ui.main.account.about;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface AccountAboutMvpPresenter<V extends AccountAboutMvpView & BaseMvpView> extends Presenter<V> {
    void loadAccountAbout();
}
