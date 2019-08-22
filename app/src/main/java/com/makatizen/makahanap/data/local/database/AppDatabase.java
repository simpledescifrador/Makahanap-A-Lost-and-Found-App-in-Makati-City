package com.makatizen.makahanap.data.local.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.makatizen.makahanap.data.local.database.dao.BarangayDataDao;
import com.makatizen.makahanap.pojo.BarangayData;

@Database(entities = {BarangayData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BarangayDataDao getBarangayDao();
}
