package com.makatizen.makahanap.ui.main.map;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface MapMvpPresenter<V extends MapMvpView & BaseMvpView> extends Presenter<V> {
    void loadMap();
}
