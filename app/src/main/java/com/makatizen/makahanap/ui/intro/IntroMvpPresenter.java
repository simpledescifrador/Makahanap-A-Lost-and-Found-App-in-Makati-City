package com.makatizen.makahanap.ui.intro;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface IntroMvpPresenter<V extends IntroMvpView & BaseMvpView> extends Presenter<V> {
    void onPageChange(int position);

    void removeIntro(boolean remove);

    void checkIfIntroWillBeLoaded();
}
