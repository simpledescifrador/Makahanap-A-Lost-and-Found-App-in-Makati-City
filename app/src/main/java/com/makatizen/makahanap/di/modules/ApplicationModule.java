package com.makatizen.makahanap.di.modules;

import android.app.Application;
import android.content.Context;
import com.makatizen.makahanap.data.AppDataManager;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.data.local.database.DbHelper;
import com.makatizen.makahanap.data.local.preference.PreferencesHelper;
import com.makatizen.makahanap.data.remote.ApiHelper;
import com.makatizen.makahanap.di.qualifiers.ApplicationContext;
import com.makatizen.makahanap.di.scopes.ApplicationScope;
import dagger.Module;
import dagger.Provides;

@Module(includes = {ContextModule.class, PreferencesModule.class, RetrofitModule.class, DatabaseModule.class})
public class ApplicationModule {

    private final Application application;

    public ApplicationModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationScope
    static AppDataManager provideAppDataManager(@ApplicationContext Context context,
            PreferencesHelper preferencesHelper,
            DbHelper dbHelper,
            ApiHelper apiHelper) {
        return new AppDataManager(context, dbHelper, preferencesHelper, apiHelper);
    }

    @Provides
    @ApplicationScope
    static DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }
}
