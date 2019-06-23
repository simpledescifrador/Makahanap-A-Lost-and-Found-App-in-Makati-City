package com.makatizen.makahanap.data.local.database.data_source;

import com.makatizen.makahanap.data.local.database.dao.BarangayDataDao;
import com.makatizen.makahanap.data.local.database.repository.BarangayDataRepository;
import com.makatizen.makahanap.pojo.BarangayData;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import java.util.List;
import javax.inject.Inject;

public class BarangayDataDataSource implements BarangayDataRepository {

    private BarangayDataDao mBarangayDataDao;

    @Inject
    public BarangayDataDataSource(final BarangayDataDao barangayDataDao) {
        mBarangayDataDao = barangayDataDao;
    }

    @Override
    public void addAllBarangayData(final List<BarangayData> barangayDataList) {
        mBarangayDataDao.addAllBarangay(barangayDataList);
    }

    @Override
    public void addBarangayData(final BarangayData barangayData) {
        mBarangayDataDao.addBarangay(barangayData);
    }

    @Override
    public Single<List<BarangayData>> getAllBarangayData() {
        return mBarangayDataDao.getAllBarangay();
    }

    @Override
    public Single<List<String>> getAllBarangayDataNames() {
        return mBarangayDataDao.getAllBarangayNames();
    }

    @Override
    public Maybe<BarangayData> getBarangayData(final int id) {
        return mBarangayDataDao.getBarangay(id);
    }

    @Override
    public void deleteAllBarangayData() {
        mBarangayDataDao.deleteAllBarangayData();
    }
}
