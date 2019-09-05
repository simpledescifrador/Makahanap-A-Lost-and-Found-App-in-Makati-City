package com.makatizen.makahanap.ui.item_details;

import android.util.Log;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.pojo.api_response.CreateChatResponse;
import com.makatizen.makahanap.pojo.api_response.GetItemDetailsResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;
import io.reactivex.MaybeObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class ItemDetailsPresenter<V extends ItemDetailsMvpView> extends BasePresenter<V>
        implements ItemDetailsMvpPresenter<V> {

    private static final String TAG = "ItemDetails";

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
                            if (currentAccountId == getItemDetailsResponse.getAccount().getId()) {
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
