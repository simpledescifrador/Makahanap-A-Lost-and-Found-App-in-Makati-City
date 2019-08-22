package com.makatizen.makahanap.ui.intro;

import com.makatizen.makahanap.ui.base.BaseMvpView;

public interface IntroMvpView extends BaseMvpView {

    void loadIntro();

    void loadLogin();

    void loadMain();

    void onSelectedPage(int position);
}
