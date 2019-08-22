package com.makatizen.makahanap.di.modules;

import android.content.Context;
import com.makatizen.makahanap.data.local.preference.AppPreferencesHelper;
import com.makatizen.makahanap.data.local.preference.CommonPreferencesHelper;
import com.makatizen.makahanap.data.local.preference.PreferencesHelper;
import com.makatizen.makahanap.di.qualifiers.ApplicationContext;
import com.makatizen.makahanap.di.scopes.ApplicationScope;
import dagger.Module;
import dagger.Provides;

@Module
public class PreferencesModule {

    @Provides
    @ApplicationScope
    static PreferencesHelper provideApplicationPreferences(
            CommonPreferencesHelper commonPreferencesHelper) {
        return new AppPreferencesHelper(commonPreferencesHelper);
    }

    @Provides
    @ApplicationScope
    static CommonPreferencesHelper providePreferencesHelper(
            @ApplicationContext Context context) {
        return new CommonPreferencesHelper(context);
    }
}
