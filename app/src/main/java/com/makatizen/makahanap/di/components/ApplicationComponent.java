package com.makatizen.makahanap.di.components;

import android.content.Context;
import com.makatizen.makahanap.MakahanapApplication;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.di.modules.ApplicationModule;
import com.makatizen.makahanap.di.qualifiers.ApplicationContext;
import com.makatizen.makahanap.di.scopes.ApplicationScope;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@ApplicationScope
@Component(modules = {ApplicationModule.class, AndroidInjectionModule.class})
public interface ApplicationComponent {

    @ApplicationContext
    Context getContext();

    DataManager getDataManager();

    void inject(MakahanapApplication application);
}
