package com.makatizen.makahanap.ui.chat_convo;

import android.util.Log;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.LocationData;
import com.makatizen.makahanap.pojo.Person;
import com.makatizen.makahanap.pojo.PersonalThing;
import com.makatizen.makahanap.pojo.Pet;
import com.makatizen.makahanap.pojo.api_response.AddChatMessageResponse;
import com.makatizen.makahanap.pojo.api_response.ChatMessagesResponse;
import com.makatizen.makahanap.pojo.api_response.CheckTransactionStatusResponse;
import com.makatizen.makahanap.pojo.api_response.ConfirmationStatusResponse;
import com.makatizen.makahanap.pojo.api_response.GetItemDetailsResponse;
import com.makatizen.makahanap.pojo.api_response.MeetTransactionConfirmationResponse;
import com.makatizen.makahanap.pojo.api_response.MeetUpDetailsResponse;
import com.makatizen.makahanap.pojo.api_response.TransactionConfirmResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;
import com.makatizen.makahanap.utils.enums.Type;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class ChatConvoPresenter<V extends ChatConvoMvpView> extends BasePresenter<V>
        implements ChatConvoMvpPresenter<V> {

    private static final String TAG = "ChatConvo";

    private GetItemDetailsResponse mGetItemDetailsResponse;

    private int mTransactionId = 0;

    private int mMeetId = 0;

    private int tempAccountId = 0;

    @Inject
    ChatConvoPresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public int getAccountId() {
        return getDataManager().getCurrentAccount().getId();
    }

    @Override
    public void checkTransactionAvailability(final int itemId) {
        getDataManager().getItemDetails(itemId)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(new SingleObserver<GetItemDetailsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(final GetItemDetailsResponse getItemDetailsResponse) {
                        if (!isViewAttached()) {
                            return;
                        }
                        mGetItemDetailsResponse = getItemDetailsResponse;
                        //Check if the item is report by current account
                        int currentAccountId = getDataManager().getCurrentAccount().getId();
                        Type type = getItemDetailsResponse.getType();


                        if (currentAccountId == getItemDetailsResponse.getAccount().getId() && type == Type.FOUND) {
                            getMvpView().showTransactionButtons();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }
                        if (e instanceof SocketTimeoutException
                                || e instanceof SocketException) {
                            getMvpView().onError(R.string.title_no_network);
                        }
                    }
                });

        getDataManager().getConfirmationStatus(itemId)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(new SingleObserver<ConfirmationStatusResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(ConfirmationStatusResponse confirmationStatusResponse) {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (confirmationStatusResponse.isConfirmed()) {
                            getMvpView().confirmed();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }
                        if (e instanceof SocketTimeoutException
                                || e instanceof SocketException) {
                            getMvpView().onError(R.string.title_no_network);
                        }
                    }
                });

    }

    @Override
    public void openConfirmTransactionDialog(int itemId) {
        LocationData locationData = mGetItemDetailsResponse.getLocationData();
        String title = mGetItemDetailsResponse.getTitle();
        if (mGetItemDetailsResponse.getPersonalThingData() != null) {
            PersonalThing personalThing = mGetItemDetailsResponse.getPersonalThingData();
            getMvpView().showConfirmDialog(title, personalThing.getAdditionalLocationInfo() + " " + locationData.getName(), personalThing.getDate(), personalThing.getItemImagesUrl().get(0));
        } else if (mGetItemDetailsResponse.getPetData() != null) {
            Pet pet = mGetItemDetailsResponse.getPetData();
            getMvpView().showConfirmDialog(title, pet.getAdditionalLocationInfo() + " " + locationData.getName(), pet.getDate(), pet.getPetImagesUrl().get(0));
        } else {
            Person person = mGetItemDetailsResponse.getPersonData();
            getMvpView().showConfirmDialog(title, person.getAdditionalLocationInfo() + " " + locationData.getName(), person.getDate(), person.getPersonImagesUrl().get(0));
        }
    }

    @Override
    public void confirmItem(int itemId, int ownerId) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed);
        } else {
            getDataManager().confirmItemTransaction(String.valueOf(itemId), String.valueOf(ownerId))
                    .observeOn(getSchedulerProvider().ui())
                    .subscribeOn(getSchedulerProvider().io())
                    .delaySubscription(2000, TimeUnit.MILLISECONDS)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            getMvpView().showLoading();
                        }
                    })
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideLoading();
                        }
                    })
                    .subscribe(new SingleObserver<TransactionConfirmResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(TransactionConfirmResponse transactionConfirmResponse) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (transactionConfirmResponse.isAlreadyConfirmed()) {
                                mTransactionId = transactionConfirmResponse.getTransactionId();
                                getMvpView().alreadyConfirmedItem(transactionConfirmResponse.getTransactionId());
                            } else {
                                getMvpView().onSuccessConfirm(transactionConfirmResponse.getTransactionId());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (!isViewAttached()) {
                                return;
                            }
                            if (e instanceof SocketTimeoutException
                                    || e instanceof SocketException) {
                                getMvpView().onError(R.string.title_no_network);
                            }
                        }
                    });

        }
    }

    @Override
    public void openMeetupSetupDialog() {
        getMvpView().showSetupMeetup(mTransactionId);
    }

    @Override
    public void checkTransaction(final int itemId, final int accountId) {
        getDataManager().getItemDetails(itemId)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(new SingleObserver<GetItemDetailsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(final GetItemDetailsResponse getItemDetailsResponse) {
                        if (!isViewAttached()) {
                            return;
                        }
                        mGetItemDetailsResponse = getItemDetailsResponse;

                        if (mGetItemDetailsResponse.getAccount().getId() != accountId) {
                            tempAccountId = accountId;
                        } else {
                            tempAccountId = getDataManager().getCurrentAccount().getId();
                        }

                        getDataManager().checkTransactionStatus(String.valueOf(itemId), String.valueOf(tempAccountId))
                                .observeOn(getSchedulerProvider().ui())
                                .subscribeOn(getSchedulerProvider().io())
                                .subscribe(new SingleObserver<CheckTransactionStatusResponse>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        getCompositeDisposable().add(d);
                                    }

                                    @Override
                                    public void onSuccess(CheckTransactionStatusResponse status) {
                                        if (!isViewAttached()) {
                                            return;
                                        }
                                        int meetupId = status.getMeetupId();
                                        int transactionId = status.getTransactionId();
                                        boolean alreadyConfirmed = status.isAlreadyConfirmed();
                                        String confirmation = status.getConfirmation();


                                        if (confirmation != null && status.getConfirmation().equals("Accepted")) {
                                            getMvpView().meetupDone();
                                        } else if (confirmation != null && status.getConfirmation().equals("Denied")) {
                                            getMvpView().meetupFailed();
                                        } else if (meetupId != 0 && transactionId != 0) {
                                            mTransactionId = transactionId;
                                            mMeetId = meetupId;
                                            //Confirmed and meetup is setup
                                            getMvpView().confirmedAndMeet(mMeetId);
                                        } else if (!alreadyConfirmed && meetupId == 0 && transactionId != 0) {
                                            mTransactionId = transactionId;
                                            //Confirmed but not yet meeting place setup
                                            getMvpView().confirmed();
                                        } else if (alreadyConfirmed && transactionId != 0) {
                                            //Confirmed in other convo
                                            getMvpView().alreadyConfirmed();
                                        } else {
                                            //Not confirmed
                                            getMvpView().showTransactionButtons();
                                        }

                                        int currentAccountId = getDataManager().getCurrentAccount().getId();
                                        if (currentAccountId != mGetItemDetailsResponse.getAccount().getId()) {
                                            getMvpView().hideTransactionOption();
                                            getMvpView().hideMeetingConfirmationButtons();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (!isViewAttached()) {
                                            return;
                                        }

                                        if (e instanceof SocketTimeoutException
                                                || e instanceof SocketException) {
                                            getMvpView().onError(R.string.title_no_network);
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }
                        if (e instanceof SocketTimeoutException
                                || e instanceof SocketException) {
                            getMvpView().onError(R.string.title_no_network);
                        }
                    }
                });
    }

    @Override
    public void getMeetingPlaceDetails(int meetId) {
        mMeetId = meetId;
        getDataManager().getMeetingPlaceDetails(String.valueOf(meetId))
                .delaySubscription(2000, TimeUnit.MILLISECONDS)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(new SingleObserver<MeetUpDetailsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(MeetUpDetailsResponse response) {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (response.getMeetupPlace() != null) {
                            getMvpView().showMeetingPlaceDetails(response.getMeetupPlace(), response.getMeetupDate());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }
                        if (e instanceof SocketTimeoutException
                                || e instanceof SocketException) {
                            getMvpView().onError(R.string.title_no_network);
                        }
                    }
                });

    }

    @Override
    public void toggleMeetingDetails(boolean toggleState) {
        if (toggleState) {
            getMvpView().hideMeetingDetails();
        } else {
            getMvpView().showMeetingDetails();
        }
    }

    @Override
    public void updateMeetUpConfirmation(String confirmation) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed);
        } else {
            getDataManager().updateMeetConfirmation(String.valueOf(mMeetId), confirmation)
                    .observeOn(getSchedulerProvider().ui())
                    .subscribeOn(getSchedulerProvider().io())
                    .delaySubscription(2000, TimeUnit.MILLISECONDS)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            getMvpView().showLoading();
                        }
                    })
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideLoading();
                        }
                    })
                    .subscribe(new SingleObserver<MeetTransactionConfirmationResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(MeetTransactionConfirmationResponse response) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (response.isConfirmed()) {
                                getMvpView().showReturnItemOption();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (!isViewAttached()) {
                                return;
                            }
                            if (e instanceof SocketTimeoutException
                                    || e instanceof SocketException) {
                                getMvpView().onError(R.string.title_no_network);
                            }
                        }
                    });
        }
    }


    @Override
    public void loadChatMessages(final int chatId) {
        Log.d(TAG, "loadChatMessages: ");

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.title_no_network);
        } else {
            getDataManager().getChatMessages(chatId)
                    .observeOn(getSchedulerProvider().ui())
                    .subscribeOn(getSchedulerProvider().io())
                    .delaySubscription(1000, TimeUnit.MILLISECONDS)
                    .subscribe(new SingleObserver<ChatMessagesResponse>() {
                        @Override
                        public void onError(final Throwable e) {
                            Log.e(TAG, "onError: loadChatMessages", e);

                            if (e instanceof SocketTimeoutException
                                    || e instanceof SocketException) {
                                getMvpView().onError(R.string.title_no_network);
                            }
                        }

                        @Override
                        public void onSubscribe(final Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(final ChatMessagesResponse chatMessagesResponse) {
                            Log.d(TAG, "onSuccess: loadChatMessages");

                            if (isViewAttached() && chatMessagesResponse.getTotalMessages() != 0) {
                                getMvpView().setChatMessages(chatMessagesResponse.getChatMessageList());
                            } else {
                                //No Messages
                                getMvpView().onNewConvo();
                            }
                        }
                    });
        }
    }

    @Override
    public void sendMessage(final int chatId, final String message, final int position) {
        Log.d(TAG, "sendMessage: ");
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.title_no_network);
        } else {
            int accountId = getDataManager().getCurrentAccount().getId();
            getDataManager().addChatMessage(chatId, accountId, message)
                    .observeOn(getSchedulerProvider().ui())
                    .subscribeOn(getSchedulerProvider().io())
                    .subscribe(new SingleObserver<AddChatMessageResponse>() {
                        @Override
                        public void onError(final Throwable e) {
                            Log.e(TAG, "onError: Sending Message", e);
                            if (e instanceof SocketTimeoutException
                                    || e instanceof SocketException) {
                                getMvpView().onError(R.string.title_no_network);
                            }
                        }

                        @Override
                        public void onSubscribe(final Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(final AddChatMessageResponse addChatMessageResponse) {
                            Log.d(TAG, "onSuccess: Sending Message");
                            if (isViewAttached() && addChatMessageResponse.isMessageSent()) {
                                //Message sent
                                getMvpView().onMessageSentSuccess(addChatMessageResponse.getMessageTime(), position);
                            } else {
                                //Message Not Sent
                            }
                        }
                    });
        }
    }
}
