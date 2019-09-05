package com.makatizen.makahanap.ui.main.home;

import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.ui.base.BasePresenter;
import com.makatizen.makahanap.utils.enums.Type;
import javax.inject.Inject;

public class HomePresenter<V extends HomeMvpView> extends BasePresenter<V> implements HomeMvpPresenter<V> {

    private static final String TAG = "Home";

    @Inject
    HomePresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void reportItem(final Type type) {
        getMvpView().showOptionDialog(type);
    }

    @Override
    public void showAccountMessages() {
        int accountId = getDataManager().getCurrentAccount().getId();
        getMvpView().openChatBox(accountId); //It Opens Account ChatItem Box
    }
}
