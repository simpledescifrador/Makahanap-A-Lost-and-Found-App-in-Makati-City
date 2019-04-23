package com.makatizen.makahanap.di.components;

import com.makatizen.makahanap.di.modules.ActivityFragmentModule;
import com.makatizen.makahanap.di.scopes.ActivityScope;
import com.makatizen.makahanap.ui.intro.IntroActivity;
import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityFragmentModule.class)
public interface ActivityFragmentComponent {

    void inject(IntroActivity introActivity);
}
