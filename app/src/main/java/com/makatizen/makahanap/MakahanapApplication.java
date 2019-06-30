package com.makatizen.makahanap;

import android.app.Application;
import android.app.Service;
import android.support.annotation.Nullable;
import android.support.text.emoji.EmojiCompat;
import android.support.text.emoji.FontRequestEmojiCompatConfig;
import android.support.v4.provider.FontRequest;
import android.util.Log;
import com.google.android.libraries.places.api.Places;
import com.makatizen.makahanap.di.components.ApplicationComponent;
import com.makatizen.makahanap.di.components.DaggerApplicationComponent;
import com.makatizen.makahanap.di.modules.ApplicationModule;
import com.makatizen.makahanap.di.modules.ContextModule;
import com.makatizen.makahanap.di.modules.RoomModule;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasServiceInjector;
import javax.inject.Inject;

public class MakahanapApplication extends Application implements HasServiceInjector {

    public static ApplicationComponent mApplicationComponent;

    @Inject
    DispatchingAndroidInjector<Service> mServiceDispatchingAndroidInjector;

    public static ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initApplicationDaggerComponent();
        initEmojiTextViewSupport();
        // Initialize Places.
        Places.initialize(this, getResources().getString(R.string.google_api_key));
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return mServiceDispatchingAndroidInjector;
    }

    void initEmojiTextViewSupport() {
        final FontRequest fontRequest = new FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs);

        final EmojiCompat.Config config = new
                FontRequestEmojiCompatConfig(getApplicationContext(), fontRequest)
                .registerInitCallback(new EmojiCompat.InitCallback() {
                    @Override
                    public void onFailed(@Nullable Throwable throwable) {
                        Log.e("App", "EmojiCompat initialization failed", throwable);
                    }

                    @Override
                    public void onInitialized() {
                        Log.i("App", "EmojiCompat initialized");
                    }
                });
        EmojiCompat.init(config);
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
