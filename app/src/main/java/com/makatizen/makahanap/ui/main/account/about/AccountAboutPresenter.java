package com.makatizen.makahanap.ui.main.account.about;

import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.ui.base.BasePresenter;
import javax.inject.Inject;

public class AccountAboutPresenter<V extends AccountAboutMvpView> extends BasePresenter<V>
        implements AccountAboutMvpPresenter<V> {

    @Inject
    AccountAboutPresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void loadAccountAbout() {
        getMvpView().setAccountAboutData(getDataManager().getCurrentAccount());
    }
}
