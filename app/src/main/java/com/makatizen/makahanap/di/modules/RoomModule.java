package com.makatizen.makahanap.di.modules;

import android.app.Application;
import android.arch.persistence.room.Room;
import com.makatizen.makahanap.data.local.database.AppDatabase;
import com.makatizen.makahanap.data.local.database.dao.BarangayDataDao;
import com.makatizen.makahanap.data.local.database.data_source.BarangayDataDataSource;
import com.makatizen.makahanap.data.local.database.repository.BarangayDataRepository;
import com.makatizen.makahanap.di.scopes.ApplicationScope;
import dagger.Module;
import dagger.Provides;

@Module
public class RoomModule {

    private AppDatabase mAppDatabase;

    public RoomModule(Application application) {
        mAppDatabase = Room.databaseBuilder(application, AppDatabase.class, "app-db")
                .fallbackToDestructiveMigration()
                .build();
    }

    @Provides
    @ApplicationScope
    AppDatabase provideAppDatabase() {
        return mAppDatabase;
    }

    @Provides
    @ApplicationScope
    BarangayDataDao provideBarangayDataDao() {
        return mAppDatabase.getBarangayDao();
    }

    @Provides
    @ApplicationScope
    BarangayDataRepository provideBarangayDataRepository(BarangayDataDao barangayDataDao) {
        return new BarangayDataDataSource(barangayDataDao);
    }

}
