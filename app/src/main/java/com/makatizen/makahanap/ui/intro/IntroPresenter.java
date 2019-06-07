package com.makatizen.makahanap.ui.intro;

import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.ui.base.BasePresenter;
import javax.inject.Inject;

public class IntroPresenter<V extends IntroMvpView> extends BasePresenter<V>
        implements IntroMvpPresenter<V> {

    @Inject
    public IntroPresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void checkIfIntroWillBeLoaded() {
        if (getDataManager().isLoggedIn()) {
            getMvpView().loadMain(); // Load Main Activity
        } else if (getDataManager().isFirstTimeUser()) { //Intro should be loaded
            getMvpView().loadIntro();
        } else { //Load Login Instead
            getMvpView().loadLogin();
        }
    }

    @Override
    public void onPageChange(final int position) {
        checkViewAttached();
        if (isViewAttached()) {
            getMvpView().onSelectedPage(position);
        }
    }

    @Override
    public void removeIntro(final boolean remove) {
        if (remove) {
            //Remove Intro in App Start-Up
            getDataManager().removeStartUpIntro();
        } else {
            //Show Intro in App Start-Up
            getDataManager().showStartUpIntro();
        }
    }
}
