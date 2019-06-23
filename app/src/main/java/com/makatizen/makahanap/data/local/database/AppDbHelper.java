package com.makatizen.makahanap.data.local.database;

import com.makatizen.makahanap.data.local.database.repository.BarangayDataRepository;
import com.makatizen.makahanap.pojo.BarangayData;
import io.reactivex.Single;
import java.util.List;
import javax.inject.Inject;

public class AppDbHelper implements DbHelper {


    private BarangayDataRepository mBarangayDataRepository;

    @Inject
    public AppDbHelper(BarangayDataRepository barangayDataRepository) {
        mBarangayDataRepository = barangayDataRepository;
    }


    @Override
    public void addAllBarangayDataToDb(final List<BarangayData> barangayDataList) {
        mBarangayDataRepository.addAllBarangayData(barangayDataList);
    }

    @Override
    public void addBarangayDataToDb(final BarangayData barangayData) {
        mBarangayDataRepository.addBarangayData(barangayData);
    }

    @Override
    public Single<List<BarangayData>> getAllBarangayDataFromDb() {
        return mBarangayDataRepository.getAllBarangayData();
    }

    @Override
    public Single<List<String>> getAllBarangayDataNamesFromDb() {
        return mBarangayDataRepository.getAllBarangayDataNames();
    }

    @Override
    public void deleteAllBarangayDataFromDb() {
        mBarangayDataRepository.deleteAllBarangayData();
    }
}
