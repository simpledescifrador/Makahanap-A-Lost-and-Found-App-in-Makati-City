package com.makatizen.makahanap.data.remote;

import com.makatizen.makahanap.pojo.BarangayData;
import com.makatizen.makahanap.pojo.LocationData;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.Person;
import com.makatizen.makahanap.pojo.PersonalThing;
import com.makatizen.makahanap.pojo.Pet;
import com.makatizen.makahanap.pojo.api_response.AddChatMessageResponse;
import com.makatizen.makahanap.pojo.api_response.ChatMessagesResponse;
import com.makatizen.makahanap.pojo.api_response.ChatResponse;
import com.makatizen.makahanap.pojo.api_response.CheckTransactionStatusResponse;
import com.makatizen.makahanap.pojo.api_response.ConfirmationStatusResponse;
import com.makatizen.makahanap.pojo.api_response.CountResponse;
import com.makatizen.makahanap.pojo.api_response.CreateChatResponse;
import com.makatizen.makahanap.pojo.api_response.GetItemDetailsResponse;
import com.makatizen.makahanap.pojo.api_response.GetLatestFeedResponse;
import com.makatizen.makahanap.pojo.api_response.LoginResponse;
import com.makatizen.makahanap.pojo.api_response.MakatizenGetDataResponse;
import com.makatizen.makahanap.pojo.api_response.MeetTransactionConfirmationResponse;
import com.makatizen.makahanap.pojo.api_response.MeetUpDetailsResponse;
import com.makatizen.makahanap.pojo.api_response.NotificationDeleteResponse;
import com.makatizen.makahanap.pojo.api_response.NotificationResponse;
import com.makatizen.makahanap.pojo.api_response.NotificationTotalResponse;
import com.makatizen.makahanap.pojo.api_response.NotificationUpdateResponse;
import com.makatizen.makahanap.pojo.api_response.RegisterReponse;
import com.makatizen.makahanap.pojo.api_response.RegisterTokenResponse;
import com.makatizen.makahanap.pojo.api_response.SearchItemResponse;
import com.makatizen.makahanap.pojo.api_response.TransactionConfirmResponse;
import com.makatizen.makahanap.pojo.api_response.TransactionNewMeetupResponse;
import com.makatizen.makahanap.pojo.api_response.VerifyMakatizenIdResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface ApiHelper {
    Single<MeetTransactionConfirmationResponse> updateMeetConfirmation(String meetupId, String confirmation);

    Single<MeetUpDetailsResponse> getMeetingPlaceDetails(String id);

    Single<CheckTransactionStatusResponse> checkTransactionStatus(String itemId, String accountId);

    Single<TransactionNewMeetupResponse> createNewMeetup(LocationData locationData, String date, String transactionId);

    Single<ConfirmationStatusResponse> getConfirmationStatus(int itemId);

    Single<TransactionConfirmResponse> confirmItemTransaction(String itemId, String accountId);

    Single<CreateChatResponse> getItemChatId(int itemId, int accountId);

    Single<SearchItemResponse> searchItems(String query, String limit);

    Single<SearchItemResponse> searchItemsFiltered(String query, String filter);

    Single<NotificationResponse> getNotifications(String accountId);

    Single<NotificationTotalResponse> getTotalNotifications(String accountId);

    Single<NotificationTotalResponse> getTotalUnviewedNotifications(String accountId);

    Single<NotificationUpdateResponse> setNotificationUnviewed(String id);

    Single<NotificationUpdateResponse> setNotificationViewed(String id);

    Single<NotificationDeleteResponse> deleteNotification(String id);

    Single<AddChatMessageResponse> addChatMessage(int chatId, int accountId, String message);

    Single<CreateChatResponse> createChat(int account1Id, int account2Id, String type);

    Maybe<List<String>> getAccountItemImages(int accountId);

    Single<GetLatestFeedResponse> getAccountLatestFeed(int accountId);

    Observable<List<BarangayData>> getAllBarangayData();

    Single<BarangayData> getBarangayData(int barangayId);

    Single<ChatResponse> getChatList(int accountId);

    Single<ChatMessagesResponse> getChatMessages(int chatId);

    Single<GetItemDetailsResponse> getItemDetails(int itemId);

    Maybe<List<String>> getItemImages(int itemId);

    Single<GetLatestFeedResponse> getLatestFeed();

    Single<MakahanapAccount> getMakahanapAccountData(int accountId);

    Single<MakatizenGetDataResponse> getMakatizenData(String makatizenId);

    Single<CountResponse> getStatusCount(int accountId, String status);

    Single<CountResponse> getTypeCount(int accountId, String type);

    Single<LoginResponse> loginAppRequest(String makatizenNumber, String password);

    Single<RegisterReponse> registerNewAccount(MakahanapAccount makahanapAccount);

    Single<RegisterTokenResponse> registerTokenToServer(String token, int accountId);

    Completable reportPerson(Person person);

    Completable reportPersonalThing(PersonalThing personalThing);

    Completable reportPet(Pet pet);

    Single<VerifyMakatizenIdResponse> verifyMakatizenId(String makatizenId);
}
