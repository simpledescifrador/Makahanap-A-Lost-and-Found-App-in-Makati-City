package com.makatizen.makahanap;

import android.app.Application;
import com.google.android.libraries.places.api.Places;
import com.makatizen.makahanap.di.components.ApplicationComponent;
import com.makatizen.makahanap.di.components.DaggerApplicationComponent;
import com.makatizen.makahanap.di.modules.ApplicationModule;
import com.makatizen.makahanap.di.modules.ContextModule;
import com.makatizen.makahanap.di.modules.RoomModule;

public class MakahanapApplication extends Application {

    public static ApplicationComponent mApplicationComponent;

    public static ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initApplicationDaggerComponent();

        // Initialize Places.
        Places.initialize(this, getResources().getString(R.string.google_api_key));
    }

    private void initApplicationDaggerComponent() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .contextModule(new ContextModule(this))
                .roomModule(new RoomModule(this))
                .build();

        mApplicationComponent.inject(this);
    }
}
