package com.makatizen.makahanap.di.modules;

import com.makatizen.makahanap.data.local.database.AppDbHelper;
import com.makatizen.makahanap.data.local.database.DbHelper;
import com.makatizen.makahanap.di.scopes.ApplicationScope;
import dagger.Module;
import dagger.Provides;

@Module(includes = ContextModule.class)
public class DatabaseModule {

    @Provides
    @ApplicationScope
    DbHelper provideDbHelper() {
        return new AppDbHelper();
    }
}
