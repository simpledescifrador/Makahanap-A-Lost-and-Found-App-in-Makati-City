package com.makatizen.makahanap.data.local.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.makatizen.makahanap.pojo.BarangayData;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;

@Dao
public interface BarangayDataDao {

    @Insert
    void addAllBarangay(List<BarangayData> barangayDataList);

    @Insert
    void addBarangay(BarangayData barangayData);

    @Delete
    void deleteBarangay(BarangayData barangayData);

    @Query("SELECT * FROM barangay_data")
    Single<List<BarangayData>> getAllBarangay();

    @Query("SELECT name FROM barangay_data")
    Single<List<String>> getAllBarangayNames();

    @Query("SELECT * FROM barangay_data WHERE id=:id")
    Maybe<BarangayData> getBarangay(int id);

    @Update
    void updateBarangay(BarangayData barangayData);


}
