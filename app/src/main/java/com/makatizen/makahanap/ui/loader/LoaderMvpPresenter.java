package com.makatizen.makahanap.ui.loader;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface LoaderMvpPresenter<V extends LoaderMvpView & BaseMvpView> extends Presenter<V> {

    void getCurrentAccountData(int accountId);

}
