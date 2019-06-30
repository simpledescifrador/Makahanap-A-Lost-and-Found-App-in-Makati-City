package com.makatizen.makahanap.data.remote;

import com.makatizen.makahanap.pojo.BarangayData;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.api_response.AddChatMessageResponse;
import com.makatizen.makahanap.pojo.api_response.ChatMessagesResponse;
import com.makatizen.makahanap.pojo.api_response.ChatResponse;
import com.makatizen.makahanap.pojo.api_response.CreateChatResponse;
import com.makatizen.makahanap.pojo.api_response.GetItemDetailsResponse;
import com.makatizen.makahanap.pojo.api_response.GetLatestFeedResponse;
import com.makatizen.makahanap.pojo.api_response.LoginResponse;
import com.makatizen.makahanap.pojo.api_response.RegisterReponse;
import com.makatizen.makahanap.pojo.api_response.RegisterTokenResponse;
import io.reactivex.Completable;
import io.reactivex.Maybe;
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
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST(ApiConstants.GET_CHAT_URL + "{chat_id}/messages/add")
    Single<AddChatMessageResponse> addChatMessage(
            @Path("chat_id") int chatId,
            @Field("account_id") int accountId,
            @Field("message") String message
    );

    @FormUrlEncoded
    @POST(ApiConstants.CREATE_CHAT_URL)
    Single<CreateChatResponse> createChat(
            @Field("account1_id") int account1Id,
            @Field("account2_id") int account2Id,
            @Field("type") String type
    );

    @GET(ApiConstants.GET_ACCOUNT_ITEM_IMAGES_URL + "{account_id}/images")
    Maybe<List<String>> getAccountItemImages(@Path("account_id") int accountId);

    @GET(ApiConstants.GET_LATEST_ACCOUNT_FEED_URL + "{account_id}/items")
    Single<GetLatestFeedResponse> getAccountLatestFeed(@Path("account_id") int accountId);

    @GET(ApiConstants.GET_ALL_BARANGAY_DATA_URL)
    Observable<List<BarangayData>> getAllBarangayData();

    @GET(ApiConstants.GET_BARANGAY_DATA_URL + "{barangay_id}")
    Single<BarangayData> getBarangayData(int barangayId);

    @GET(ApiConstants.GET_CHAT_URL)
    Single<ChatResponse> getChatList(
            @Query("account_id") int accountId
    );

    @GET(ApiConstants.GET_CHAT_URL + "{chat_id}/messages")
    Single<ChatMessagesResponse> getChatMessages(@Path("chat_id") int chatId);

    @GET(ApiConstants.GET_ITEM_DETAILS_URL + "{item_id}")
    Single<GetItemDetailsResponse> getItemDetails(@Path("item_id") int itemId);

    @GET(ApiConstants.GET_ITEM_IMAGES_URL + "{item_id}/images")
    Maybe<List<String>> getItemImages(@Path("item_id") int itemId);

    @GET(ApiConstants.GET_LATEST_FEED_URL)
    Single<GetLatestFeedResponse> getLatestFeed();

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

    @FormUrlEncoded
    @POST(ApiConstants.REGISTER_ACCOUNT_TOKEN_URL)
    Single<RegisterTokenResponse> registerTokenToServer(
            @Field("token") String token,
            @Field("account_id") int accountId
    );

    @Multipart
    @POST(ApiConstants.REPORT_PERSON_URL)
    Completable reportPerson(
            @PartMap Map<String, RequestBody> data,
            @Part List<MultipartBody.Part> personImages
    );

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
}
