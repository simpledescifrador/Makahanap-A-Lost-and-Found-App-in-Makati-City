package com.makatizen.makahanap.data.remote;

import android.net.Uri;
import android.support.annotation.NonNull;

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
import com.makatizen.makahanap.utils.enums.Type;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;

public class AppApiHelper implements ApiHelper {

    private ApiInterface mApiInterface;

    private MakatizenApiInterface mMakatizenApiInterface;

    @Inject
    public AppApiHelper(final ApiInterface apiInterface, final MakatizenApiInterface makatizenApiInterface) {
        mApiInterface = apiInterface;
        mMakatizenApiInterface = makatizenApiInterface;
    }


    @Override
    public Single<CommonResponse> checkItemReported(String itemId, String accountId) {
        return mApiInterface.checkItemReported(itemId, accountId);
    }

    @Override
    public Single<CommonResponse> reportItem(String itemId, String reportedBy, String reason) {
        return mApiInterface.reportItem(itemId, reportedBy, reason);
    }

    @Override
    public Single<ChangePasswordResponse> resetPassword(String makatizenNumber, String password) {
        return mApiInterface.resetPassword(makatizenNumber, password);
    }

    @Override
    public Single<EmailVerificationRequest> requestForgotPasswordEmailVerification(String email) {
        return mApiInterface.requestForgotPasswordEmailVerification(email);
    }

    @Override
    public Single<CheckMakatizenResponse> checkMakatizenNumberRegistration(String makatizenNumber) {
        return mApiInterface.checkMakatizenNumberRegistration(makatizenNumber);
    }

    @Override
    public Single<ChangePasswordResponse> changePassword(String accountId, String newPassword, String oldPassword) {
        return mApiInterface.changePassword(accountId, newPassword, oldPassword);
    }

    @Override
    public Single<List<GetMapItemsResponse>> getMapItems() {
        return mApiInterface.getMapItems();
    }

    @Override
    public Single<SendSmsRequestResponse> sendSmsRequest(String number) {
        return mApiInterface.sendSmsRequest(number);
    }

    @Override
    public Single<CancelSmsRequestResponse> cancelSmsRequest(String requestId) {
        return mApiInterface.cancelSmsRequest(requestId);
    }

    @Override
    public Single<CheckSmsRequestResponse> checkSmsRequest(String code, String requestId) {
        return mApiInterface.checkSmsRequest(code, requestId);
    }

    @Override
    public Single<VerifyEmailVerificationCodeResponse> verifyEmailVerificationCode(String email, String verificationCode) {
        return mApiInterface.verifyEmailVerificationCode(email, verificationCode);
    }

    @Override
    public Single<EmailVerificationRequest> emailVerificationRequest(String email, String token) {
        return mApiInterface.requestEmailVerification(email, token);
    }

    @Override
    public Single<GetLatestFeedResponse> getItemLocations() {
        return mApiInterface.getItemLocationsLatlng(1);
    }

    @Override
    public Single<AccountAverageRatingResponse> getAccountAverageRating(String accountId) {
        return mApiInterface.getAccountAverageRating(accountId);
    }

    @Override
    public Completable newAccountRating(String ratedToAccount, String ratedByAccount, String feedBack, String rating) {
        return mApiInterface.newAccountRating(ratedToAccount, ratedByAccount, feedBack, rating);
    }

    @Override
    public Single<UpdateReturnTransactionResponse> confirmReturnTransaction(String transaction) {
        return mApiInterface.confirmReturnTransaction(transaction);
    }

    @Override
    public Single<UpdateReturnTransactionResponse> deniedReturnTransaction(String transaction) {
        return mApiInterface.deniedReturnTransaction(transaction);
    }

    @Override
    public Single<ReturnPendingTransactionResponse> checkPendingReturnAgreement(String itemId, String accountId) {
        return mApiInterface.checkPendingReturnAgreement(itemId, accountId);
    }

    @Override
    public Completable returnItemTransaction(String meetUpId, String itemId, String imagePath) {
        Map<String, RequestBody> partMap = new HashMap<>();
        Uri imageUri = Uri.fromFile(new File(imagePath));

        partMap.put("meetup_id", createPartFromString(meetUpId));
        partMap.put("item_id", createPartFromString(itemId));

        Part imagePart = prepareFilePart("meetup_image", imageUri);

        return mApiInterface.returnItemTransaction(partMap, imagePart);
    }

    @Override
    public Single<CheckReturnStatusResponse> checkItemReturnStatus(String itemId) {
        return mApiInterface.checkItemReturnStatus(itemId);
    }

    @Override
    public Single<MeetTransactionConfirmationResponse> updateMeetConfirmation(String meetupId, String confirmation) {
        return mApiInterface.updateMeetConfirmation(meetupId, confirmation);
    }

    @Override
    public Single<MeetUpDetailsResponse> getMeetingPlaceDetails(String id) {
        return mApiInterface.getMeetingPlaceDetails(id);
    }

    @Override
    public Single<CheckTransactionStatusResponse> checkTransactionStatus(String itemId, String accountId) {
        return mApiInterface.checkTransactionStatus(itemId, accountId);
    }

    @Override
    public Single<TransactionNewMeetupResponse> createNewMeetup(LocationData locationData, String date, String transactionId) {
        String locationId = locationData.getId();
        String locationName = locationData.getName();
        String locationAddress = locationData.getAddress();
        String locationLat = String.valueOf(locationData.getLatlng().latitude);
        String locationLong = String.valueOf(locationData.getLatlng().longitude);
        return mApiInterface.createNewMeetup(locationId, locationName, locationAddress, locationLat, locationLong, date, transactionId);
    }

    @Override
    public Single<ConfirmationStatusResponse> getConfirmationStatus(int itemId) {
        return mApiInterface.getConfrimationStatus(String.valueOf(itemId));
    }

    @Override
    public Single<TransactionConfirmResponse> confirmItemTransaction(String itemId, String accountId) {
        return mApiInterface.confirmItemTransaction(itemId, accountId);
    }

    @Override
    public Single<CreateChatResponse> getItemChatId(int itemId, int accountId) {
        return mApiInterface.getItemChatId(String.valueOf(itemId), String.valueOf(accountId));
    }

    @Override
    public Single<SearchItemResponse> searchItems(String query, String limit) {
        return mApiInterface.searchItems(query, limit);
    }

    @Override
    public Single<SearchItemResponse> searchItemsFiltered(String query, String filter) {
        return mApiInterface.searchItemsFiltered(query, filter);
    }

    @Override
    public Single<NotificationResponse> getNotifications(String accountId) {
        return mApiInterface.getNotifications(accountId);
    }

    @Override
    public Single<NotificationTotalResponse> getTotalNotifications(String accountId) {
        return mApiInterface.getTotalNotifications(accountId);
    }

    @Override
    public Single<NotificationTotalResponse> getTotalUnviewedNotifications(String accountId) {
        return mApiInterface.getTotalUnViewedNotifications(accountId);
    }

    @Override
    public Single<NotificationUpdateResponse> setNotificationUnviewed(String id) {
        return mApiInterface.setNotificationUnviewed(id);
    }

    @Override
    public Single<NotificationUpdateResponse> setNotificationViewed(String id) {
        return mApiInterface.setNotificationViewed(id);
    }

    @Override
    public Single<NotificationDeleteResponse> deleteNotification(String id) {
        return mApiInterface.deleteNotification(id);
    }

    @Override
    public Single<AddChatMessageResponse> addChatMessage(final int chatId, final int accountId,
                                                         final String message) {
        return mApiInterface.addChatMessage(chatId, accountId, message);
    }

    @Override
    public Single<CreateChatResponse> createChat(final int account1Id, final int account2Id, final String type) {
        return mApiInterface.createChat(account1Id, account2Id, type);
    }

    @Override
    public Maybe<List<String>> getAccountItemImages(final int accountId) {
        return mApiInterface.getAccountItemImages(accountId);
    }

    @Override
    public Single<GetLatestFeedResponse> getAccountLatestFeed(final int accountId) {
        return mApiInterface.getAccountLatestFeed(accountId);
    }

    @Override
    public Observable<List<BarangayData>> getAllBarangayData() {
        return mApiInterface.getAllBarangayData();
    }

    @Override
    public Single<BarangayData> getBarangayData(final int barangayId) {
        return mApiInterface.getBarangayData(barangayId);
    }

    @Override
    public Single<ChatResponse> getChatList(final int accountId) {
        return mApiInterface.getChatList(accountId);
    }

    @Override
    public Single<ChatMessagesResponse> getChatMessages(final int chatId) {
        return mApiInterface.getChatMessages(chatId);
    }

    @Override
    public Single<GetItemDetailsResponse> getItemDetails(final int itemId) {
        return mApiInterface.getItemDetails(itemId);
    }

    @Override
    public Maybe<List<String>> getItemImages(final int itemId) {
        return mApiInterface.getItemImages(itemId);
    }

    @Override
    public Single<GetLatestFeedResponse> getLatestFeed() {
        return mApiInterface.getLatestFeed();
    }

    @Override
    public Single<MakahanapAccount> getMakahanapAccountData(final int accountId) {
        return mApiInterface.getMakahanapAccountData(accountId);
    }

    @Override
    public Single<MakatizenGetDataResponse> getMakatizenData(final String makatizenId) {
        return mMakatizenApiInterface.getMakatizenData(makatizenId);
    }

    @Override
    public Single<CountResponse> getStatusCount(final int accountId, final String status) {
        return mApiInterface.getStatusCount(accountId, status);
    }

    @Override
    public Single<CountResponse> getTypeCount(final int accountId, final String type) {
        return mApiInterface.getTypeCount(accountId, type);
    }

    @Override
    public Single<LoginResponse> loginAppRequest(final String makatizenNumber, final String password) {
        return mApiInterface.loginAppRequest(makatizenNumber, password);
    }

    @Override
    public Single<RegisterReponse> registerNewAccount(final MakahanapAccount makahanapAccount) {
        return mApiInterface.registerNewAccount(makahanapAccount);
    }

    @Override
    public Single<RegisterTokenResponse> registerTokenToServer(final String token, final int accountId) {
        return mApiInterface.registerTokenToServer(token, accountId);
    }

    @Override
    public Completable reportPerson(final Person person) {
        Map<String, RequestBody> partMap = new HashMap<>();
        List<Part> personImagesPart = new ArrayList<>();
        Uri imageUri;
        //Location Info
        partMap.put("location_address", createPartFromString(person.getLocationData().getAddress()));
        partMap.put("location_name", createPartFromString(person.getLocationData().getName()));
        partMap.put("location_id", createPartFromString(person.getLocationData().getId()));
        partMap.put("location_latitude",
                createPartFromString(String.valueOf(person.getLocationData().getLatlng().latitude)));
        partMap.put("location_longitude",
                createPartFromString(String.valueOf(person.getLocationData().getLatlng().longitude)));
        partMap.put("additional_location_info", createPartFromString(person.getAdditionalLocationInfo()));

        partMap.put("account_id", createPartFromString(String.valueOf(person.getAccountData().getId())));
        partMap.put("name", createPartFromString(person.getName() != null ? person.getName() : ""));
        partMap.put("age_range", createPartFromString(person.getAgeRange()));
        partMap.put("age_group", createPartFromString(person.getAgeGroup()));
        partMap.put("sex", createPartFromString(person.getSex()));
        partMap.put("date", createPartFromString(person.getDate()));
        partMap.put("description", createPartFromString(person.getDescription()));

        if (person.getType() == Type.LOST) {
            partMap.put("type", createPartFromString("Lost"));
            partMap.put("reward",
                    createPartFromString(person.getReward() != null ? person.getReward().toString() : "0.0"));
        } else {
            partMap.put("type", createPartFromString("Found"));
        }
        int itemImagesCount = person.getPersonImagesUrl().size();
        for (int i = 0; i < itemImagesCount; i++) {
            imageUri = Uri.fromFile(new File(person.getPersonImagesUrl().get(i)));
            personImagesPart.add(prepareFilePart("person_images[]", imageUri));
        }
        return mApiInterface.reportPerson(partMap, personImagesPart);
    }

    @Override
    public Completable reportPersonalThing(final PersonalThing personalThing) {
        Map<String, RequestBody> partMap = new HashMap<>();
        List<Part> itemImagesPart = new ArrayList<>();
        Uri imageUri;

        //Location Info
        partMap.put("location_address", createPartFromString(personalThing.getLocationData().getAddress()));
        partMap.put("location_name", createPartFromString(personalThing.getLocationData().getName()));
        partMap.put("location_id", createPartFromString(personalThing.getLocationData().getId()));
        partMap.put("location_latitude",
                createPartFromString(String.valueOf(personalThing.getLocationData().getLatlng().latitude)));
        partMap.put("location_longitude",
                createPartFromString(String.valueOf(personalThing.getLocationData().getLatlng().longitude)));
        partMap.put("additional_location_info", createPartFromString(personalThing.getAdditionalLocationInfo()));

        partMap.put("item_name", createPartFromString(personalThing.getItemName()));
        partMap.put("item_category", createPartFromString(personalThing.getCategory()));
        partMap.put("brand", createPartFromString(personalThing.getBrand()));
        partMap.put("item_color", createPartFromString(personalThing.getColor()));
        partMap.put("item_description", createPartFromString(personalThing.getDescription()));
        partMap.put("account_id", createPartFromString(String.valueOf(personalThing.getAccountData().getId())));

        if (personalThing.getType() == Type.LOST) {
            partMap.put("type", createPartFromString("Lost"));
            partMap.put("date_lost", createPartFromString(personalThing.getDate()));
            partMap.put("reward", createPartFromString(String.valueOf(personalThing.getReward())));
        } else {
            partMap.put("type", createPartFromString("Found"));
            partMap.put("date_found", createPartFromString(personalThing.getDate()));
            partMap.put("barangay_id", createPartFromString(String.valueOf(personalThing.getBarangayData().getId())));
            partMap.put("s", createPartFromString(
                    String.valueOf(personalThing.isItemSurrendered() ? 1 : 0))); //if them is to be surrender
        }

        int itemImagesCount = personalThing.getItemImagesUrl().size();
        for (int i = 0; i < itemImagesCount; i++) {
            imageUri = Uri.fromFile(new File(personalThing.getItemImagesUrl().get(i)));
            itemImagesPart.add(prepareFilePart("item_images[]", imageUri));
        }
        return mApiInterface.reportPersonalThing(partMap, itemImagesPart);
    }

    @Override
    public Completable reportPet(final Pet pet) {
        Map<String, RequestBody> partMap = new HashMap<>();
        List<Part> petImagesPart = new ArrayList<>();
        Uri imageUri;

        //Location Info
        partMap.put("location_address", createPartFromString(pet.getLocationData().getAddress()));
        partMap.put("location_name", createPartFromString(pet.getLocationData().getName()));
        partMap.put("location_id", createPartFromString(pet.getLocationData().getId()));
        partMap.put("location_latitude",
                createPartFromString(String.valueOf(pet.getLocationData().getLatlng().latitude)));
        partMap.put("location_longitude",
                createPartFromString(String.valueOf(pet.getLocationData().getLatlng().longitude)));
        partMap.put("additional_location_info", createPartFromString(pet.getAdditionalLocationInfo()));

        partMap.put("account_id", createPartFromString(String.valueOf(pet.getAccountData().getId())));
        partMap.put("pet_name", createPartFromString(pet.getName() != null ? pet.getName() : ""));
        partMap.put("pet_breed", createPartFromString(pet.getBreed() != null ? pet.getBreed() : ""));
        partMap.put("pet_type", createPartFromString(pet.getPetType()));
        partMap.put("pet_condition", createPartFromString(pet.getCondition()));
        partMap.put("pet_description", createPartFromString(pet.getDescription()));
        partMap.put("reward", createPartFromString(pet.getReward() != null ? pet.getReward().toString() : "0"));
        partMap.put("date", createPartFromString(pet.getDate()));

        if (pet.getItemType() == Type.LOST) {
            partMap.put("type", createPartFromString("Lost"));
        } else {
            partMap.put("type", createPartFromString("Found"));
        }

        int itemImagesCount = pet.getPetImagesUrl().size();
        for (int i = 0; i < itemImagesCount; i++) {
            imageUri = Uri.fromFile(new File(pet.getPetImagesUrl().get(i)));
            petImagesPart.add(prepareFilePart("pet_images[]", imageUri));
        }
        return mApiInterface.reportPet(partMap, petImagesPart);
    }

    @Override
    public Single<VerifyMakatizenIdResponse> verifyMakatizenId(final String makatizenId) {
        return mMakatizenApiInterface.validateMakatizenNumber(makatizenId);
    }

    @NonNull
    private RequestBody createPartFromString(String partString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, partString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = new File(fileUri.getEncodedPath());
        RequestBody requestFile = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                file
        );
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
