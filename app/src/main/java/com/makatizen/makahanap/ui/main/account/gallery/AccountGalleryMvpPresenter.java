package com.makatizen.makahanap.ui.main.account.gallery;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface AccountGalleryMvpPresenter<V extends AccountGalleryMvpView & BaseMvpView> extends Presenter<V> {

    void loadAccountItemImages();
}
