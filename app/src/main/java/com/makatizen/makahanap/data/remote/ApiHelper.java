package com.makatizen.makahanap.data.remote;

import com.makatizen.makahanap.pojo.BarangayData;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.Person;
import com.makatizen.makahanap.pojo.PersonalThing;
import com.makatizen.makahanap.pojo.Pet;
import com.makatizen.makahanap.pojo.api_response.ChatResponse;
import com.makatizen.makahanap.pojo.api_response.GetItemDetailsResponse;
import com.makatizen.makahanap.pojo.api_response.GetLatestFeedResponse;
import com.makatizen.makahanap.pojo.api_response.LoginResponse;
import com.makatizen.makahanap.pojo.api_response.MakatizenGetDataResponse;
import com.makatizen.makahanap.pojo.api_response.RegisterReponse;
import com.makatizen.makahanap.pojo.api_response.RegisterTokenResponse;
import com.makatizen.makahanap.pojo.api_response.VerifyMakatizenIdResponse;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.List;

public interface ApiHelper {

    Maybe<List<String>> getAccountItemImages(int accountId);

    Single<GetLatestFeedResponse> getAccountLatestFeed(int accountId);

    Observable<List<BarangayData>> getAllBarangayData();

    Single<BarangayData> getBarangayData(int barangayId);

    Single<ChatResponse> getChatList(int accountId);

    Single<GetItemDetailsResponse> getItemDetails(int itemId);

    Maybe<List<String>> getItemImages(int itemId);

    Single<GetLatestFeedResponse> getLatestFeed();

    Single<MakahanapAccount> getMakahanapAccountData(int accountId);

    Single<MakatizenGetDataResponse> getMakatizenData(String makatizenId);

    Single<LoginResponse> loginAppRequest(String makatizenNumber, String password);

    Single<RegisterReponse> registerNewAccount(MakahanapAccount makahanapAccount);

    Single<RegisterTokenResponse> registerTokenToServer(String token, int accountId);

    Completable reportPerson(Person person);

    Completable reportPersonalThing(PersonalThing personalThing);

    Completable reportPet(Pet pet);

    Single<VerifyMakatizenIdResponse> verifyMakatizenId(String makatizenId);
}
