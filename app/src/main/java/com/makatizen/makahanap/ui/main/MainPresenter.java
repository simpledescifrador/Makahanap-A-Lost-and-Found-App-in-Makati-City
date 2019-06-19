package com.makatizen.makahanap.ui.main;

import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.ui.base.BasePresenter;
import javax.inject.Inject;

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V> implements MainMvpPresenter<V>{

    private static final String TAG = "Main";

    @Inject
    MainPresenter(final DataManager dataManager) {
        super(dataManager);
    }
}
