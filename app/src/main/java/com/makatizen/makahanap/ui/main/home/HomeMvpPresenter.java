package com.makatizen.makahanap.ui.main.home;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;
import com.makatizen.makahanap.utils.enums.Type;

public interface HomeMvpPresenter<V extends HomeMvpView & BaseMvpView> extends Presenter<V> {
    void reportItem(Type type);
}
