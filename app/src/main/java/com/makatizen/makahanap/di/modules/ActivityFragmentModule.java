package com.makatizen.makahanap.di.modules;

import android.app.Activity;
import android.content.Context;
import com.makatizen.makahanap.di.qualifiers.ActivityContext;
import com.makatizen.makahanap.di.scopes.ActivityScope;
import com.makatizen.makahanap.ui.intro.IntroMvpPresenter;
import com.makatizen.makahanap.ui.intro.IntroMvpView;
import com.makatizen.makahanap.ui.intro.IntroPresenter;
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
}
