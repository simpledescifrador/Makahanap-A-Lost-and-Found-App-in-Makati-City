package com.makatizen.makahanap.data.remote;

import com.makatizen.makahanap.pojo.BarangayData;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.api_response.GetLatestFeedResponse;
import com.makatizen.makahanap.pojo.api_response.LoginResponse;
import com.makatizen.makahanap.pojo.api_response.RegisterReponse;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.List;
import java.util.Map;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET(ApiConstants.GET_ALL_BARANGAY_DATA_URL)
    Observable<List<BarangayData>> getAllBarangayData();

    @GET(ApiConstants.GET_BARANGAY_DATA_URL + "{barangay_id}")
    Single<BarangayData> getBarangayData(int barangayId);

    @GET(ApiConstants.GET_ACCOUNT_DATA_URL + "{account_id}")
    Single<MakahanapAccount> getMakahanapAccountData(@Path("account_id") int accountId);

    @FormUrlEncoded
    @POST(ApiConstants.LOGIN_REQUEST_URL)
    Single<LoginResponse> loginAppRequest(
            @Field("makatizen_number") String makatizenNumber,
            @Field("password") String password
    );

    @POST(ApiConstants.REGISTER_ACCOUNT_URL)
    Single<RegisterReponse> registerNewAccount(@Body MakahanapAccount makahanapAccount);

    @Multipart
    @POST(ApiConstants.REPORT_PERSONAL_THING_URL)
    Completable reportPersonalThing(
            @PartMap Map<String, RequestBody> data,
            @Part List<MultipartBody.Part> itemImages
    );

    @Multipart
    @POST(ApiConstants.REPORT_PET_URL)
    Completable reportPet(
            @PartMap Map<String, RequestBody> data,
            @Part List<MultipartBody.Part> petImages
    );
    @Multipart
    @POST(ApiConstants.REPORT_PERSON_URL)
    Completable reportPerson(
            @PartMap Map<String, RequestBody> data,
            @Part List<MultipartBody.Part> personImages
    );

    @GET(ApiConstants.GET_LATEST_FEED_URL)
    Single<GetLatestFeedResponse> getLatestFeed();
}
