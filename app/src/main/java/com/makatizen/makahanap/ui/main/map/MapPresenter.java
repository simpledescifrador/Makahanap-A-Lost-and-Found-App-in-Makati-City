package com.makatizen.makahanap.ui.main.map;

import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.ui.base.BasePresenter;

import javax.inject.Inject;

public class MapPresenter<V extends MapMvpView> extends BasePresenter<V> implements MapMvpPresenter<V> {
    private static final String TAG = "MainMap";
    @Inject
    MapPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void loadMap() {
        getMvpView().showCurrentLocation();

        //get Markers
    }
}
