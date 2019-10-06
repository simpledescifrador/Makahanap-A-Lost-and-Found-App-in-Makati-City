package com.makatizen.makahanap.ui.item_details;

import android.util.Log;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.pojo.api_response.CheckReturnStatusResponse;
import com.makatizen.makahanap.pojo.api_response.CommonResponse;
import com.makatizen.makahanap.pojo.api_response.CreateChatResponse;
import com.makatizen.makahanap.pojo.api_response.GetItemDetailsResponse;
import com.makatizen.makahanap.pojo.api_response.ReturnPendingTransactionResponse;
import com.makatizen.makahanap.pojo.api_response.UpdateReturnTransactionResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class ItemDetailsPresenter<V extends ItemDetailsMvpView> extends BasePresenter<V>
        implements ItemDetailsMvpPresenter<V> {

    private static final String TAG = "ItemDetails";

    private int mPostAccountId = 0;

    private int mMeetUpId = 0;

    private int mReturnTransactionId = 0;

    private int mRatedToAccountId = 0;

    @Inject
    ItemDetailsPresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void loadItemDetails(final int itemId) {
        Log.d(TAG, "loadItemDetails: ");
        getDataManager().getItemDetails(itemId)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .delaySubscription(1000, TimeUnit.MILLISECONDS)
                .subscribe(new SingleObserver<GetItemDetailsResponse>() {
                    @Override
                    public void onError(final Throwable e) {
                        Log.e(TAG, "onError: Loading item Details", e);
                        if (e instanceof SocketTimeoutException
                                || e instanceof SocketException) {
                            getMvpView().onError(R.string.error_network_failed);
                        }
                    }

                    @Override
                    public void onSubscribe(final Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(final GetItemDetailsResponse getItemDetailsResponse) {
                        if (isViewAttached()) {
                            getMvpView().hideItemDetailsLoading();


                            //Check if the item is report by current account
                            int currentAccountId = getDataManager().getCurrentAccount().getId();
                            mPostAccountId = getItemDetailsResponse.getAccount().getId();
                            if (currentAccountId == getItemDetailsResponse.getAccount().getId()) {
                                getMvpView().removeMessageButton();
                                getMvpView().showRemoveOption();
                            }

                            if (getItemDetailsResponse.getReportedBy().equals("Brgy User")) {
                                getMvpView().removeMessageButton();
                            }


                            if (getItemDetailsResponse.getPersonalThingData() != null) {
                                getMvpView().onPersonalThingData(getItemDetailsResponse);
                            } else if (getItemDetailsResponse.getPetData() != null) {
                                getMvpView().onPetData(getItemDetailsResponse);
                            } else {
                                getMvpView().onPersonData(getItemDetailsResponse);
                            }
                        }
                    }
                });

    }

    @Override
    public void loadItemImages(final int itemId) {
        Log.d(TAG, "loadItemImages: ");
        getDataManager().getItemImages(itemId)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .delaySubscription(2000, TimeUnit.MILLISECONDS)
                .subscribe(new MaybeObserver<List<String>>() {
                    @Override
                    public void onComplete() {

                    }

                    @Override
                    public void onError(final Throwable e) {
                        Log.e(TAG, "onError: Loading Item Images", e);
                        if (e instanceof SocketTimeoutException
                                || e instanceof SocketException) {
                            getMvpView().onError(R.string.error_network_failed);
                        }
                    }

                    @Override
                    public void onSubscribe(final Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(final List<String> imageUrls) {
                        if (isViewAttached()) {
                            List<String> fixedImagesUrls = new ArrayList<>();
                            for (String s : imageUrls) {
                                fixedImagesUrls.add(ApiConstants.BASE_URL + s);
                            }
                            getMvpView().hideItemImageLoading();
                            getMvpView().setItemImages(fixedImagesUrls);
                        }
                    }
                });

    }

    @Override
    public void openItemChat(int itemId) {

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.title_no_network);
        } else {
            getDataManager().getItemChatId(itemId, getDataManager().getCurrentAccount().getId())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribeOn(getSchedulerProvider().io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(final Disposable disposable) throws Exception {
                            getMvpView().showLoading();
                        }
                    })
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideLoading();
                        }
                    })
                    .subscribe(new SingleObserver<CreateChatResponse>() {
                        @Override
                        public void onError(final Throwable e) {
                            Log.e(TAG, "onError: Loading Item Images", e);
                            if (e instanceof SocketTimeoutException
                                    || e instanceof SocketException) {
                                getMvpView().onError(R.string.error_network_failed);
                            }
                        }

                        @Override
                        public void onSubscribe(final Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(final CreateChatResponse createChatResponse) {
                            Log.d(TAG, "onSuccess: Open Chat");
                            if (isViewAttached() && createChatResponse.isSuccess()) {
                                getMvpView().onSuccessGetChatId(createChatResponse.getChatId());
                            } else {
                                getMvpView().onError("Unable to open chat. Try again.");
                            }
                        }
                    });

        }
    }

    @Override
    public void checkReturnStatus(int itemId) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed);
        } else {
            getDataManager().checkItemReturnStatus(String.valueOf(itemId))
                    .observeOn(getSchedulerProvider().ui())
                    .subscribeOn(getSchedulerProvider().io())
                    .delaySubscription(1000, TimeUnit.MILLISECONDS)
                    .subscribe(new SingleObserver<CheckReturnStatusResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(CheckReturnStatusResponse response) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (response.isValidReturn() && response.getAccountId() == getDataManager().getCurrentAccount().getId()) {
                                mMeetUpId = response.getMeetupId();
                                getMvpView().showReturnOption();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (!isViewAttached()) {
                                return;
                            }
                            if (e instanceof SocketTimeoutException
                                    || e instanceof SocketException) {
                                getMvpView().onError(R.string.error_network_failed);
                            }
                        }
                    });
        }
    }

    @Override
    public void startReturnActivity() {
        if (mMeetUpId != 0) {
            getMvpView().openReturnItemActivity(mMeetUpId);
        }
    }

    @Override
    public void checkItemPendingReturnAgreement(int itemId) {
        int accountId = getDataManager().getCurrentAccount().getId();
        getDataManager().checkPendingReturnAgreement(String.valueOf(itemId), String.valueOf(accountId))
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .delaySubscription(1000, TimeUnit.MILLISECONDS)
                .subscribe(new SingleObserver<ReturnPendingTransactionResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(ReturnPendingTransactionResponse response) {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (response.isReturned()) {
                            mReturnTransactionId = response.getReturnTransationId();
                            getMvpView().showReturnAgreement();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }
                        if (e instanceof SocketTimeoutException
                                || e instanceof SocketException) {
                            getMvpView().onError(R.string.error_network_failed);
                        }
                    }
                });

    }

    @Override
    public void agreeAgreement() {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed);
        } else {
            getDataManager().confirmReturnTransaction(String.valueOf(mReturnTransactionId))
                    .observeOn(getSchedulerProvider().ui())
                    .subscribeOn(getSchedulerProvider().io())
                    .delaySubscription(1000, TimeUnit.MILLISECONDS)
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
                    .subscribe(new SingleObserver<UpdateReturnTransactionResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(UpdateReturnTransactionResponse response) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (response.isUpdated()) {
                                mRatedToAccountId = response.getAccountId();
                                getMvpView().showRatingDialog(response.getProfileImage(), response.getAccountName());
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (!isViewAttached()) {
                                return;
                            }
                            if (e instanceof SocketTimeoutException
                                    || e instanceof SocketException) {
                                getMvpView().onError(R.string.error_network_failed);
                            }
                        }
                    });
        }
    }

    @Override
    public void disgreeAgreement() {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed);
        } else {
            getDataManager().deniedReturnTransaction(String.valueOf(mReturnTransactionId))
                    .observeOn(getSchedulerProvider().ui())
                    .subscribeOn(getSchedulerProvider().io())
                    .delaySubscription(1000, TimeUnit.MILLISECONDS)
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
                    .subscribe(new SingleObserver<UpdateReturnTransactionResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(UpdateReturnTransactionResponse response) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (response.isUpdated()) {
                                getMvpView().hideReturnAgreement();
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            if (!isViewAttached()) {
                                return;
                            }
                            if (e instanceof SocketTimeoutException
                                    || e instanceof SocketException) {
                                getMvpView().onError(R.string.error_network_failed);
                            }
                        }
                    });
        }
    }

    @Override
    public void rateAccount(String rating, String feedBack) {
        String ratedBy = String.valueOf(getDataManager().getCurrentAccount().getId());
        getDataManager().newAccountRating(String.valueOf(mRatedToAccountId), ratedBy, feedBack, rating)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(final Disposable disposable) throws Exception {
                        getMvpView().showLoading();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        getMvpView().hideLoading();
                    }
                })
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onComplete() {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().onSuccessReturn();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (e instanceof SocketTimeoutException
                                || e instanceof SocketException) {
                            getMvpView().onError(R.string.error_network_failed);
                        }
                    }
                });
    }

    @Override
    public void checkItemReported(String itemId) {
        getDataManager().checkItemReported(itemId, String.valueOf(getDataManager().getCurrentAccount().getId()))
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(new SingleObserver<CommonResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(CommonResponse response) {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (mPostAccountId == getDataManager().getCurrentAccount().getId()) {
                            getMvpView().hideReportOption();
                            return;
                        }

                        if (response.getStatus() == 0) {
                            getMvpView().showReportOption();
                        }


                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (e instanceof SocketTimeoutException
                                || e instanceof SocketException) {
                            getMvpView().onError(R.string.error_network_failed);
                        }
                    }
                });
    }

    @Override
    public void reportItem(String itemId, String reason) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed);
        } else {
            getMvpView().showLoading();
            String accountId = String.valueOf(getDataManager().getCurrentAccount().getId());
            getDataManager().reportItem(itemId, accountId, reason)
                    .observeOn(getSchedulerProvider().ui())
                    .subscribeOn(getSchedulerProvider().io())
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideLoading();
                        }
                    })
                    .delaySubscription(2000, TimeUnit.MILLISECONDS)
                    .subscribe(new SingleObserver<CommonResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onSuccess(CommonResponse response) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (response.getStatus() == 1) { //Reported Successfully
                                getMvpView().onSuccessReportItem();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (e instanceof SocketTimeoutException
                                    || e instanceof SocketException) {
                                getMvpView().onError(R.string.error_network_failed);
                            }
                        }
                    });
        }
    }

    @Override
    public void openChat(final int account1Id, final String type) {
        Log.d(TAG, "openChat: ");

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.title_no_network);
        } else {
            int account2Id = getDataManager().getCurrentAccount().getId();
            getDataManager().createChat(account1Id, account2Id, type)
                    .observeOn(getSchedulerProvider().ui())
                    .subscribeOn(getSchedulerProvider().io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(final Disposable disposable) throws Exception {
                            getMvpView().showLoading();
                        }
                    })
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideLoading();
                        }
                    })
                    .subscribe(new SingleObserver<CreateChatResponse>() {
                        @Override
                        public void onError(final Throwable e) {
                            Log.e(TAG, "onError: Loading Item Images", e);
                            if (e instanceof SocketTimeoutException
                                    || e instanceof SocketException) {
                                getMvpView().onError(R.string.error_network_failed);
                            }
                        }

                        @Override
                        public void onSubscribe(final Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(final CreateChatResponse createChatResponse) {
                            Log.d(TAG, "onSuccess: Open Chat");
                            if (isViewAttached() && createChatResponse.isSuccess()) {
                                getMvpView().onSuccessGetChatId(createChatResponse.getChatId());
                            } else {
                                getMvpView().onError("Unable to open chat. Try again.");
                            }
                        }
                    });
        }
    }
}
