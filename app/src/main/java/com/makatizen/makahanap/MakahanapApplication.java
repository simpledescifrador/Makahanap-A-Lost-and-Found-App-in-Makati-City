package com.makatizen.makahanap;

import android.app.Application;
import com.makatizen.makahanap.di.components.ApplicationComponent;
import com.makatizen.makahanap.di.components.DaggerApplicationComponent;
import com.makatizen.makahanap.di.modules.ApplicationModule;
import com.makatizen.makahanap.di.modules.ContextModule;

public class MakahanapApplication extends Application {

    public static ApplicationComponent mApplicationComponent;

    public static ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initApplicationDaggerComponent();
    }

    private void initApplicationDaggerComponent() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .contextModule(new ContextModule(this))
                .build();

        mApplicationComponent.inject(this);
    }
}
