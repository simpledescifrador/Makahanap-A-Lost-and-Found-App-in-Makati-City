package com.makatizen.makahanap.data.local.database.repository;

import com.makatizen.makahanap.pojo.BarangayData;
import io.reactivex.Maybe;
import io.reactivex.Single;
import java.util.List;

public interface BarangayDataRepository {

    void addAllBarangayData(List<BarangayData> barangayDataList);

    void addBarangayData(BarangayData barangayData);

    Single<List<BarangayData>> getAllBarangayData();

    Single<List<String>> getAllBarangayDataNames();

    Maybe<BarangayData> getBarangayData(int id);

    void deleteAllBarangayData();
}
