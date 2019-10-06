package com.makatizen.makahanap.data;

import android.content.Context;

import com.makatizen.makahanap.data.local.database.DbHelper;
import com.makatizen.makahanap.data.local.preference.PreferencesHelper;
import com.makatizen.makahanap.data.remote.ApiHelper;
import com.makatizen.makahanap.di.qualifiers.ApplicationContext;
import com.makatizen.makahanap.pojo.BarangayData;
import com.makatizen.makahanap.pojo.LocationData;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.Person;
import com.makatizen.makahanap.pojo.PersonalThing;
import com.makatizen.makahanap.pojo.Pet;
import com.makatizen.makahanap.pojo.api_response.AccountAverageRatingResponse;
import com.makatizen.makahanap.pojo.api_response.AddChatMessageResponse;
import com.makatizen.makahanap.pojo.api_response.CancelSmsRequestResponse;
import com.makatizen.makahanap.pojo.api_response.ChangePasswordResponse;
import com.makatizen.makahanap.pojo.api_response.ChatMessagesResponse;
import com.makatizen.makahanap.pojo.api_response.ChatResponse;
import com.makatizen.makahanap.pojo.api_response.CheckMakatizenResponse;
import com.makatizen.makahanap.pojo.api_response.CheckReturnStatusResponse;
import com.makatizen.makahanap.pojo.api_response.CheckSmsRequestResponse;
import com.makatizen.makahanap.pojo.api_response.CheckTransactionStatusResponse;
import com.makatizen.makahanap.pojo.api_response.CommonResponse;
import com.makatizen.makahanap.pojo.api_response.ConfirmationStatusResponse;
import com.makatizen.makahanap.pojo.api_response.CountResponse;
import com.makatizen.makahanap.pojo.api_response.CreateChatResponse;
import com.makatizen.makahanap.pojo.api_response.EmailVerificationRequest;
import com.makatizen.makahanap.pojo.api_response.GetItemDetailsResponse;
import com.makatizen.makahanap.pojo.api_response.GetLatestFeedResponse;
import com.makatizen.makahanap.pojo.api_response.GetMapItemsResponse;
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
import com.makatizen.makahanap.pojo.api_response.ReturnPendingTransactionResponse;
import com.makatizen.makahanap.pojo.api_response.SearchItemResponse;
import com.makatizen.makahanap.pojo.api_response.SendSmsRequestResponse;
import com.makatizen.makahanap.pojo.api_response.TransactionConfirmResponse;
import com.makatizen.makahanap.pojo.api_response.TransactionNewMeetupResponse;
import com.makatizen.makahanap.pojo.api_response.UpdateReturnTransactionResponse;
import com.makatizen.makahanap.pojo.api_response.VerifyEmailVerificationCodeResponse;
import com.makatizen.makahanap.pojo.api_response.VerifyMakatizenIdResponse;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

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
    public Single<CommonResponse> checkItemReported(String itemId, String accountId) {
        return apiHelper.checkItemReported(itemId, accountId);
    }

    @Override
    public Single<CommonResponse> reportItem(String itemId, String reportedBy, String reason) {
        return apiHelper.reportItem(itemId, reportedBy, reason);
    }

    @Override
    public Single<ChangePasswordResponse> resetPassword(String makatizenNumber, String password) {
        return apiHelper.resetPassword(makatizenNumber, password);
    }

    @Override
    public Single<EmailVerificationRequest> requestForgotPasswordEmailVerification(String email) {
        return apiHelper.requestForgotPasswordEmailVerification(email);
    }

    @Override
    public Single<CheckMakatizenResponse> checkMakatizenNumberRegistration(String makatizenNumber) {
        return apiHelper.checkMakatizenNumberRegistration(makatizenNumber);
    }

    @Override
    public Single<ChangePasswordResponse> changePassword(String accountId, String newPassword, String oldPassword) {
        return apiHelper.changePassword(accountId, newPassword, oldPassword);
    }

    @Override
    public Single<List<GetMapItemsResponse>> getMapItems() {
        return apiHelper.getMapItems();
    }

    @Override
    public Single<SendSmsRequestResponse> sendSmsRequest(String number) {
        return apiHelper.sendSmsRequest(number);
    }

    @Override
    public Single<CancelSmsRequestResponse> cancelSmsRequest(String requestId) {
        return apiHelper.cancelSmsRequest(requestId);
    }

    @Override
    public Single<CheckSmsRequestResponse> checkSmsRequest(String code, String requestId) {
        return apiHelper.checkSmsRequest(code, requestId);
    }

    @Override
    public Single<VerifyEmailVerificationCodeResponse> verifyEmailVerificationCode(String email, String verificationCode) {
        return apiHelper.verifyEmailVerificationCode(email, verificationCode);
    }

    @Override
    public Single<EmailVerificationRequest> emailVerificationRequest(String email, String token) {
        return apiHelper.emailVerificationRequest(email, token);
    }

    @Override
    public Single<GetLatestFeedResponse> getItemLocations() {
        return apiHelper.getItemLocations();
    }

    @Override
    public Single<AccountAverageRatingResponse> getAccountAverageRating(String accountId) {
        return apiHelper.getAccountAverageRating(accountId);
    }

    @Override
    public Completable newAccountRating(String ratedToAccount, String ratedByAccount, String feedBack, String rating) {
        return apiHelper.newAccountRating(ratedToAccount, ratedByAccount, feedBack, rating);
    }

    @Override
    public Single<UpdateReturnTransactionResponse> confirmReturnTransaction(String transaction) {
        return apiHelper.confirmReturnTransaction(transaction);
    }

    @Override
    public Single<UpdateReturnTransactionResponse> deniedReturnTransaction(String transaction) {
        return apiHelper.deniedReturnTransaction(transaction);
    }

    @Override
    public Single<ReturnPendingTransactionResponse> checkPendingReturnAgreement(String itemId, String accountId) {
        return apiHelper.checkPendingReturnAgreement(itemId, accountId);
    }

    @Override
    public Completable returnItemTransaction(String meetUpId, String itemId, String imagePath) {
        return apiHelper.returnItemTransaction(meetUpId, itemId, imagePath);
    }

    @Override
    public Single<CheckReturnStatusResponse> checkItemReturnStatus(String itemId) {
        return apiHelper.checkItemReturnStatus(itemId);
    }

    @Override
    public Single<MeetTransactionConfirmationResponse> updateMeetConfirmation(String meetupId, String confirmation) {
        return apiHelper.updateMeetConfirmation(meetupId, confirmation);
    }

    @Override
    public Single<MeetUpDetailsResponse> getMeetingPlaceDetails(String id) {
        return apiHelper.getMeetingPlaceDetails(id);
    }

    @Override
    public Single<CheckTransactionStatusResponse> checkTransactionStatus(String itemId, String accountId) {
        return apiHelper.checkTransactionStatus(itemId, accountId);
    }

    @Override
    public Single<TransactionNewMeetupResponse> createNewMeetup(LocationData locationData, String date, String transactionId) {
        return apiHelper.createNewMeetup(locationData, date, transactionId);
    }

    @Override
    public Single<ConfirmationStatusResponse> getConfirmationStatus(int itemId) {
        return apiHelper.getConfirmationStatus(itemId);
    }

    @Override
    public Single<TransactionConfirmResponse> confirmItemTransaction(String itemId, String accountId) {
        return apiHelper.confirmItemTransaction(itemId, accountId);
    }

    @Override
    public Single<CreateChatResponse> getItemChatId(int itemId, int accountId) {
        return apiHelper.getItemChatId(itemId, accountId);
    }

    @Override
    public Single<SearchItemResponse> searchItems(String query, String limit) {
        return apiHelper.searchItems(query, limit);
    }

    @Override
    public Single<SearchItemResponse> searchItemsFiltered(String query, String filter) {
        return apiHelper.searchItemsFiltered(query, filter);
    }

    @Override
    public Single<NotificationResponse> getNotifications(String accountId) {
        return apiHelper.getNotifications(accountId);
    }

    @Override
    public Single<NotificationTotalResponse> getTotalNotifications(String accountId) {
        return apiHelper.getTotalNotifications(accountId);
    }

    @Override
    public Single<NotificationTotalResponse> getTotalUnviewedNotifications(String accountId) {
        return apiHelper.getTotalUnviewedNotifications(accountId);
    }

    @Override
    public Single<NotificationUpdateResponse> setNotificationUnviewed(String id) {
        return apiHelper.setNotificationUnviewed(id);
    }

    @Override
    public Single<NotificationUpdateResponse> setNotificationViewed(String id) {
        return apiHelper.setNotificationViewed(id);
    }

    @Override
    public Single<NotificationDeleteResponse> deleteNotification(String id) {
        return apiHelper.deleteNotification(id);
    }

    @Override
    public Single<AddChatMessageResponse> addChatMessage(final int chatId, final int accountId,
                                                         final String message) {
        return apiHelper.addChatMessage(chatId, accountId, message);
    }

    @Override
    public Single<CreateChatResponse> createChat(final int account1Id, final int account2Id, final String type) {
        return apiHelper.createChat(account1Id, account2Id, type);
    }

    @Override
    public void deleteAllBarangayDataFromDb() {
        dbHelper.deleteAllBarangayDataFromDb();
    }

    @Override
    public Maybe<List<String>> getAccountItemImages(final int accountId) {
        return apiHelper.getAccountItemImages(accountId);
    }

    @Override
    public Single<GetLatestFeedResponse> getAccountLatestFeed(final int accountId) {
        return apiHelper.getAccountLatestFeed(accountId);
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
    public Single<ChatResponse> getChatList(final int accountId) {
        return apiHelper.getChatList(accountId);
    }

    @Override
    public Single<ChatMessagesResponse> getChatMessages(final int chatId) {
        return apiHelper.getChatMessages(chatId);
    }

    @Override
    public MakahanapAccount getCurrentAccount() {
        return applicationPreferences.getCurrentAccount();
    }

    @Override
    public String getSortFeedState() {
        return applicationPreferences.getSortFeedState();
    }

    @Override
    public void setSortFeedState(String state) {
        applicationPreferences.setSortFeedState(state);
    }

    @Override
    public String getPostViewState() {
        return applicationPreferences.getPostViewState();
    }

    @Override
    public void setPostViewState(String state) {
        applicationPreferences.setPostViewState(state);
    }

    @Override
    public Single<GetItemDetailsResponse> getItemDetails(final int itemId) {
        return apiHelper.getItemDetails(itemId);
    }

    @Override
    public Maybe<List<String>> getItemImages(final int itemId) {
        return apiHelper.getItemImages(itemId);
    }

    @Override
    public Single<GetLatestFeedResponse> getLatestFeed() {
        return apiHelper.getLatestFeed();
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
    public Single<CountResponse> getStatusCount(final int accountId, final String status) {
        return apiHelper.getStatusCount(accountId, status);
    }

    @Override
    public Single<CountResponse> getTypeCount(final int accountId, final String type) {
        return apiHelper.getTypeCount(accountId, type);
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
    public Single<RegisterTokenResponse> registerTokenToServer(final String token, final int accountId) {
        return apiHelper.registerTokenToServer(token, accountId);
    }

    @Override
    public void removeStartUpIntro() {
        applicationPreferences.removeStartUpIntro();
    }

    @Override
    public Completable reportPerson(final Person person) {
        return apiHelper.reportPerson(person);
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
}
