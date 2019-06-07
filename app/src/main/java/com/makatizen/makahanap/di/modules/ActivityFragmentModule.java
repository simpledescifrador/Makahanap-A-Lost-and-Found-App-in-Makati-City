package com.makatizen.makahanap.di.modules;

import android.app.Activity;
import android.content.Context;
import com.makatizen.makahanap.di.qualifiers.ActivityContext;
import com.makatizen.makahanap.di.scopes.ActivityScope;
import com.makatizen.makahanap.ui.intro.IntroMvpPresenter;
import com.makatizen.makahanap.ui.intro.IntroMvpView;
import com.makatizen.makahanap.ui.intro.IntroPresenter;
import com.makatizen.makahanap.ui.loader.LoaderMvpPresenter;
import com.makatizen.makahanap.ui.loader.LoaderMvpView;
import com.makatizen.makahanap.ui.loader.LoaderPresenter;
import com.makatizen.makahanap.ui.login.LoginMvpPresenter;
import com.makatizen.makahanap.ui.login.LoginMvpView;
import com.makatizen.makahanap.ui.login.LoginPresenter;
import com.makatizen.makahanap.ui.register.RegisterMvpPresenter;
import com.makatizen.makahanap.ui.register.RegisterMvpView;
import com.makatizen.makahanap.ui.register.RegisterPresenter;
import dagger.Module;
import dagger.Provides;

@Module(includes = {AdapterModule.class, RxModule.class})
public class ActivityFragmentModule {

    private Activity activity;

    public ActivityFragmentModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityContext
    Context provideActivityContext() {
        return activity;
    }

    @Provides
    @ActivityScope
    IntroMvpPresenter<IntroMvpView> provideIntroPresenter(IntroPresenter<IntroMvpView> introMvpViewIntroPresenter) {
        return introMvpViewIntroPresenter;
    }

    @Provides
    @ActivityScope
    RegisterMvpPresenter<RegisterMvpView> provideRegisterPresenter(
            RegisterPresenter<RegisterMvpView> registerMvpViewRegisterPresenter) {
        return registerMvpViewRegisterPresenter;
    }

    @Provides
    @ActivityScope
    LoaderMvpPresenter<LoaderMvpView> provideLoaderPresenter(
            LoaderPresenter<LoaderMvpView> loaderMvpViewLoaderPresenter) {
        return loaderMvpViewLoaderPresenter;
    }

    @Provides
    @ActivityScope
    LoginMvpPresenter<LoginMvpView> provideLoginPresenter(LoginPresenter<LoginMvpView> loginMvpViewLoginPresenter) {
        return loginMvpViewLoginPresenter;
    }
}
