package com.makatizen.makahanap.ui.loader;

import android.os.Handler;
import android.util.Log;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.BarangayData;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.api_response.RegisterTokenResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class LoaderPresenter<V extends LoaderMvpView> extends BasePresenter<V> implements LoaderMvpPresenter<V> {

    private static final String TAG = "Loader";

    @Inject
    LoaderPresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void getAllBarangayData() {
        if (getMvpView().isNetworkConnected()) {
            //Get All Barangay Data
            getDataManager().getAllBarangayData()
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .delaySubscription(2000, TimeUnit.MILLISECONDS)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(final Disposable disposable) throws Exception {
                            getMvpView().nextLoadingMessage();
                            getMvpView().updateProgress(20);
                        }
                    })
                    .subscribe(new Observer<List<BarangayData>>() {
                        @Override
                        public void onComplete() {

                        }

                        @Override
                        public void onError(final Throwable e) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (e instanceof SocketTimeoutException
                                    || e instanceof SocketException) {
                                getMvpView().onError(R.string.error_network_failed);
                            }
                        }

                        @Override
                        public void onNext(final List<BarangayData> barangayDataList) {
                            Log.d(TAG, "onNext: Get The Barangay Data");
                            Completable.fromAction(new Action() {
                                @Override
                                public void run() throws Exception {
                                    getDataManager().addAllBarangayDataToDb(barangayDataList);
                                }
                            }).subscribeOn(getSchedulerProvider().io())
                                    .observeOn(getSchedulerProvider().ui())
                                    .subscribe(new CompletableObserver() {
                                        @Override
                                        public void onComplete() {
                                            getMvpView().onSuccessGetAllBarangayData();
                                        }

                                        @Override
                                        public void onError(final Throwable e) {
                                            Log.e(TAG, "onError: Add Barangay Data", e);
                                        }

                                        @Override
                                        public void onSubscribe(final Disposable d) {
                                            getCompositeDisposable().add(d);
                                        }
                                    });
                        }

                        @Override
                        public void onSubscribe(final Disposable d) {
                            getCompositeDisposable().add(d);
                        }
                    });
        }
    }

    @Override
    public void getCurrentAccountData(final int accountId) {
        if (accountId == 0) {
            getMvpView().onError("Error Occurred. Please Try Again");
            return;
        }
        if (getMvpView().isNetworkConnected()) {
            //Get Makahanap Account Data
            getDataManager().getMakahanapAccountData(accountId)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .delaySubscription(2000, TimeUnit.MILLISECONDS)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(final Disposable disposable) throws Exception {
                            getMvpView().nextLoadingMessage();
                            getMvpView().updateProgress(20);
                        }
                    })
                    .subscribe(new SingleObserver<MakahanapAccount>() {
                        @Override
                        public void onError(final Throwable e) {
                            Log.e(TAG, "onError: Get makahanap Data", e);

                            if (!isViewAttached()) {
                                return;
                            }

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
                        public void onSuccess(final MakahanapAccount makahanapAccount) {
                            if (!isViewAttached()) {
                                return;
                            }
                            if (makahanapAccount != null) {
                                getDataManager()
                                        .saveCurrentAccount(makahanapAccount); //Save Makahanap Account to Shared Pref
                                getDataManager().setCurrentAccountLoggedIn(true); // Set account current logged in
                                getMvpView().onSuccessGetAccountData();
                            }
                        }
                    });
        }
    }

    @Override
    public void registerAccountToken(final String token) {
        int accountId = getDataManager().getCurrentAccount().getId();
        getDataManager().registerTokenToServer(token, accountId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .delaySubscription(2000, TimeUnit.MILLISECONDS)
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(final Disposable disposable) throws Exception {
                        getMvpView().nextLoadingMessage();
                        getMvpView().updateProgress(20);
                    }
                })
                .subscribe(new SingleObserver<RegisterTokenResponse>() {
                    @Override
                    public void onError(final Throwable e) {
                        Log.e(TAG, "onError: Register Token", e);
                    }

                    @Override
                    public void onSubscribe(final Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(final RegisterTokenResponse registerTokenResponse) {
                        Log.d(TAG, "onSuccess: Register Token");
                        if (registerTokenResponse.isSucessful()) {
                            getMvpView().updateProgress(20);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getMvpView().onCompletedLoader();
                                }
                            },2000);
                        }
                    }
                });
    }
}
