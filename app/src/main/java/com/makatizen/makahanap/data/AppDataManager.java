package com.makatizen.makahanap.data;

import android.content.Context;
import com.makatizen.makahanap.data.local.database.DbHelper;
import com.makatizen.makahanap.data.local.preference.PreferencesHelper;
import com.makatizen.makahanap.data.remote.ApiHelper;
import com.makatizen.makahanap.di.qualifiers.ApplicationContext;
import com.makatizen.makahanap.pojo.BarangayData;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.Person;
import com.makatizen.makahanap.pojo.PersonalThing;
import com.makatizen.makahanap.pojo.Pet;
import com.makatizen.makahanap.pojo.api_response.GetLatestFeedResponse;
import com.makatizen.makahanap.pojo.api_response.LoginResponse;
import com.makatizen.makahanap.pojo.api_response.MakatizenGetDataResponse;
import com.makatizen.makahanap.pojo.api_response.RegisterReponse;
import com.makatizen.makahanap.pojo.api_response.VerifyMakatizenIdResponse;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.List;
import javax.inject.Inject;

public class AppDataManager implements DataManager {

    private final ApiHelper apiHelper;

    private final PreferencesHelper applicationPreferences;

    private final Context context;

    private final DbHelper dbHelper;

    @Inject
    public AppDataManager(@ApplicationContext Context context, DbHelper dbHelper,
            PreferencesHelper applicationPreferences, ApiHelper apiHelper) {
        this.context = context;
        this.dbHelper = dbHelper;
        this.applicationPreferences = applicationPreferences;
        this.apiHelper = apiHelper;
    }

    @Override
    public void addAllBarangayDataToDb(final List<BarangayData> barangayDataList) {
        dbHelper.addAllBarangayDataToDb(barangayDataList);
    }

    @Override
    public void addBarangayDataToDb(final BarangayData barangayData) {
        dbHelper.addBarangayDataToDb(barangayData);
    }

    @Override
    public Observable<List<BarangayData>> getAllBarangayData() {
        return apiHelper.getAllBarangayData();
    }

    @Override
    public Single<List<BarangayData>> getAllBarangayDataFromDb() {
        return dbHelper.getAllBarangayDataFromDb();
    }

    @Override
    public Single<List<String>> getAllBarangayDataNamesFromDb() {
        return dbHelper.getAllBarangayDataNamesFromDb();
    }

    @Override
    public Single<BarangayData> getBarangayData(final int barangayId) {
        return apiHelper.getBarangayData(barangayId);
    }

    @Override
    public MakahanapAccount getCurrentAccount() {
        return applicationPreferences.getCurrentAccount();
    }

    @Override
    public Single<MakahanapAccount> getMakahanapAccountData(final int accountId) {
        return apiHelper.getMakahanapAccountData(accountId);
    }

    @Override
    public Single<MakatizenGetDataResponse> getMakatizenData(final String makatizenId) {
        return apiHelper.getMakatizenData(makatizenId);
    }

    @Override
    public boolean isFirstTimeUser() {
        return applicationPreferences.isFirstTimeUser();
    }

    @Override
    public boolean isLoggedIn() {
        return applicationPreferences.isLoggedIn();
    }

    @Override
    public Single<LoginResponse> loginAppRequest(final String makatizenNumber, final String password) {
        return apiHelper.loginAppRequest(makatizenNumber, password);
    }

    @Override
    public Single<RegisterReponse> registerNewAccount(final MakahanapAccount makahanapAccount) {
        return apiHelper.registerNewAccount(makahanapAccount);
    }

    @Override
    public Completable reportPerson(final Person person) {
        return apiHelper.reportPerson(person);
    }

    @Override
    public void removeStartUpIntro() {
        applicationPreferences.removeStartUpIntro();
    }

    @Override
    public Completable reportPersonalThing(final PersonalThing personalThing) {
        return apiHelper.reportPersonalThing(personalThing);
    }

    @Override
    public Completable reportPet(final Pet pet) {
        return apiHelper.reportPet(pet);
    }

    @Override
    public void saveCurrentAccount(final MakahanapAccount account) {
        applicationPreferences.saveCurrentAccount(account);
    }

    @Override
    public void setCurrentAccountLoggedIn(final boolean logged) {
        applicationPreferences.setCurrentAccountLoggedIn(logged);
    }

    @Override
    public void showStartUpIntro() {
        applicationPreferences.showStartUpIntro();
    }

    @Override
    public Single<VerifyMakatizenIdResponse> verifyMakatizenId(final String makatizenId) {
        return apiHelper.verifyMakatizenId(makatizenId);
    }

    @Override
    public Single<GetLatestFeedResponse> getLatestFeed() {
        return apiHelper.getLatestFeed();
    }
}
