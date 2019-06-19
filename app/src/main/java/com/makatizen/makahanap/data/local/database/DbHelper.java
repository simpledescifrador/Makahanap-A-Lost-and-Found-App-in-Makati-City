package com.makatizen.makahanap.data.local.database;

import com.makatizen.makahanap.pojo.BarangayData;
import io.reactivex.Single;
import java.util.List;

public interface DbHelper {

    void addAllBarangayDataToDb(List<BarangayData> barangayDataList);

    void addBarangayDataToDb(BarangayData barangayData);

    Single<List<BarangayData>> getAllBarangayDataFromDb();

    Single<List<String>> getAllBarangayDataNamesFromDb();
}
