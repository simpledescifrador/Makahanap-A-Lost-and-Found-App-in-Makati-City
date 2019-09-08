package com.makatizen.makahanap.data.remote;

import com.makatizen.makahanap.pojo.BarangayData;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.api_response.AddChatMessageResponse;
import com.makatizen.makahanap.pojo.api_response.ChatMessagesResponse;
import com.makatizen.makahanap.pojo.api_response.ChatResponse;
import com.makatizen.makahanap.pojo.api_response.CheckReturnStatusResponse;
import com.makatizen.makahanap.pojo.api_response.CheckTransactionStatusResponse;
import com.makatizen.makahanap.pojo.api_response.ConfirmationStatusResponse;
import com.makatizen.makahanap.pojo.api_response.CountResponse;
import com.makatizen.makahanap.pojo.api_response.CreateChatResponse;
import com.makatizen.makahanap.pojo.api_response.GetItemDetailsResponse;
import com.makatizen.makahanap.pojo.api_response.GetLatestFeedResponse;
import com.makatizen.makahanap.pojo.api_response.LoginResponse;
import com.makatizen.makahanap.pojo.api_response.MeetTransactionConfirmationResponse;
import com.makatizen.makahanap.pojo.api_response.MeetUpDetailsResponse;
import com.makatizen.makahanap.pojo.api_response.NotificationDeleteResponse;
import com.makatizen.makahanap.pojo.api_response.NotificationResponse;
import com.makatizen.makahanap.pojo.api_response.NotificationTotalResponse;
import com.makatizen.makahanap.pojo.api_response.NotificationUpdateResponse;
import com.makatizen.makahanap.pojo.api_response.RegisterReponse;
import com.makatizen.makahanap.pojo.api_response.RegisterTokenResponse;
import com.makatizen.makahanap.pojo.api_response.ReturnItemTransactionResponse;
import com.makatizen.makahanap.pojo.api_response.ReturnPendingTransactionResponse;
import com.makatizen.makahanap.pojo.api_response.SearchItemResponse;
import com.makatizen.makahanap.pojo.api_response.TransactionConfirmResponse;
import com.makatizen.makahanap.pojo.api_response.TransactionNewMeetupResponse;
import com.makatizen.makahanap.pojo.api_response.UpdateReturnTransactionResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @FormUrlEncoded
    @POST(ApiConstants.POST_NEW_RATING_URL)
    Completable newAccountRating(
            @Field("rated_to") String ratedToAccount,
            @Field("rated_by") String ratedByAccount,
            @Field("feed_back") String feedBack,
            @Field("rating") String rating
    );

    @FormUrlEncoded
    @PUT(ApiConstants.PUT_CONFIRM_RETURN_TRANSACTION_URL)
    Single<UpdateReturnTransactionResponse> confirmReturnTransaction(
            @Field("transaction_id") String transactionId
    );

    @FormUrlEncoded
    @PUT(ApiConstants.PUT_DENIED_RETURN_TRANSACTION_URL)
    Single<UpdateReturnTransactionResponse> deniedReturnTransaction(
            @Field("transaction_id") String transactionId
    );

    @GET(ApiConstants.GET_CHECK_PENDING_TRANSACTION_URL + "{item_id}/{account_id}")
    Single<ReturnPendingTransactionResponse> checkPendingReturnAgreement(
            @Path("item_id") String itemId,
            @Path("account_id") String accountId
    );

    @Multipart
    @POST(ApiConstants.POST_RETURN_ITEM_TRANSACTION_URL)
    Completable returnItemTransaction(
            @PartMap Map<String, RequestBody> data,
            @Part MultipartBody.Part transactionImage
    );

    @GET(ApiConstants.GET_CHECK_RETURN_STATUS_URL + "{item_id}/return/status")
    Single<CheckReturnStatusResponse> checkItemReturnStatus(@Path("item_id") String itemId);

    @FormUrlEncoded
    @PUT(ApiConstants.PUT_MEETUP_CONFIRMATION_URL)
    Single<MeetTransactionConfirmationResponse> updateMeetConfirmation(
            @Field("id") String meetUpId,
            @Field("confirmation") String confirmation
    );

    @GET(ApiConstants.GET_MEETUP_DETAILS_URL + "{id}/details")
    Single<MeetUpDetailsResponse> getMeetingPlaceDetails(
            @Path("id") String id
    );

    @GET(ApiConstants.GET_CHECK_TRANSACTION_STATUS_URL)
    Single<CheckTransactionStatusResponse> checkTransactionStatus(
            @Query("item_id") String itemId,
            @Query("account_id") String accountId
    );

    @FormUrlEncoded
    @POST(ApiConstants.POST_TRANSACTION_NEW_MEETUP_URL)
    Single<TransactionNewMeetupResponse> createNewMeetup(
            @Field("location_id") String locationId,
            @Field("location_name") String locationName,
            @Field("location_address") String locationAddress,
            @Field("location_latitude") String locationLatitude,
            @Field("location_longitude") String locationLongitude,
            @Field("date") String date,
            @Field("transaction_id") String transactionid
    );

    @GET(ApiConstants.GET_TRANSACTION_CONFIRMATION_STATUS_URL + "{item_id}")
    Single<ConfirmationStatusResponse> getConfrimationStatus(
            @Path("item_id") String itemId
    );

    @FormUrlEncoded
    @POST(ApiConstants.POST_TRANSACTION_ITEM_CONFIRM_URL)
    Single<TransactionConfirmResponse> confirmItemTransaction(
            @Field("item_id") String itemId,
            @Field("account_id") String accountId
    );

    @FormUrlEncoded
    @POST(ApiConstants.NEW_ITEM_CHAT_URL)
    Single<CreateChatResponse> getItemChatId(
            @Field("item_id") String itemId,
            @Field("account_id") String accountId
    );

    @GET(ApiConstants.GET_SEARCH_ITEMS_URL)
    Single<SearchItemResponse> searchItems(
            @Query("q") String query,
            @Query("limit") String limit
    );

    @GET(ApiConstants.GET_SEARCH_ITEMS_URL)
    Single<SearchItemResponse> searchItemsFiltered(
            @Query("q") String query,
            @Query("filter") String filter
    );

    @GET(ApiConstants.GET_NOTIFICATIONS_URL)
    Single<NotificationResponse> getNotifications(@Query("account_id") String accountId);

    @GET(ApiConstants.GET_TOTAL_UNVIEWED_NOTIFICATION_URL + "{account_id}")
    Single<NotificationTotalResponse> getTotalUnViewedNotifications(@Path("account_id") String accountId);

    @GET(ApiConstants.GET_TOTAL_ACCOUNT_NOTIFICATION_URL + "{account_id}")
    Single<NotificationTotalResponse> getTotalNotifications(@Path("account_id") String accountId);

    @PUT(ApiConstants.PUT_NOTIFICATION_UNVIEWED_URL + "{id}")
    Single<NotificationUpdateResponse> setNotificationUnviewed(@Path("id") String id);

    @PUT(ApiConstants.PUT_NOTIFICATION_VIEWED_URL + "{id}")
    Single<NotificationUpdateResponse> setNotificationViewed(@Path("id") String id);

    @PUT(ApiConstants.PUT_NOTIFICATION_DELETE_URL + "{id}")
    Single<NotificationDeleteResponse> deleteNotification(@Path("id") String id);

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

    @GET(ApiConstants.GET_ACCOUNTS_URL + "{account_id}/items/status")
    Single<CountResponse> getStatusCount(
            @Path("account_id") int accountId,
            @Query("status") String status
    );

    @GET(ApiConstants.GET_ACCOUNTS_URL + "{account_id}/items/type")
    Single<CountResponse> getTypeCount(
            @Path("account_id") int accountId,
            @Query("type") String type
    );

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
