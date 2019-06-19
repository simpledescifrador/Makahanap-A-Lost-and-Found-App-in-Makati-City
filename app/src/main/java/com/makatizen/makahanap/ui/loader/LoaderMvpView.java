package com.makatizen.makahanap.ui.loader;

import com.makatizen.makahanap.ui.base.BaseMvpView;

public interface LoaderMvpView extends BaseMvpView {

    void nextLoadingMessage();

    void onCompletedLoader();

    void onSuccessGetAccountData();
}
