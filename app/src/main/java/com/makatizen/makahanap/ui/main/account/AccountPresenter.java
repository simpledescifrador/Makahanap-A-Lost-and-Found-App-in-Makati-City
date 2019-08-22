package com.makatizen.makahanap.ui.main.account;

import android.os.Handler;
import android.util.Log;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.api_response.CountResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import javax.inject.Inject;

public class AccountPresenter<V extends AccountMvpView> extends BasePresenter<V> implements AccountMvpPresenter<V> {

    private static final String TAG = "Account";

    @Inject
    AccountPresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void getFoundCount() {
        int accountId = getDataManager().getCurrentAccount().getId();
        //get found count
        getDataManager().getTypeCount(accountId, "found")
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(new SingleObserver<CountResponse>() {
                    @Override
                    public void onError(final Throwable e) {

                    }

                    @Override
                    public void onSubscribe(final Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(final CountResponse countResponse) {
                        if (isViewAttached()) {
                            getMvpView().setFoundCount(countResponse.getCount());
                        }
                    }
                });
    }

    @Override
    public void getLostCount() {
        int accountId = getDataManager().getCurrentAccount().getId();
        //Get Lost Item Count
        getDataManager().getTypeCount(accountId, "Lost")
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(new SingleObserver<CountResponse>() {
                    @Override
                    public void onError(final Throwable e) {
                        Log.e(TAG, "onError: Loading Account Counters", e);
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
                    public void onSuccess(final CountResponse countResponse) {
                        if (isViewAttached() && countResponse != null) {
                            getMvpView().setLostCount(countResponse.getCount());
                        }
                    }
                });
    }

    @Override
    public void getReturnedCount() {
        int accountId = getDataManager().getCurrentAccount().getId();
        //get returned count
        getDataManager().getStatusCount(accountId, "returned")
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(new SingleObserver<CountResponse>() {
                    @Override
                    public void onError(final Throwable e) {
                        Log.e(TAG, "onError: Loading Account Counters", e);
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
                    public void onSuccess(final CountResponse countResponse) {
                        if (isViewAttached() && countResponse != null) {
                            getMvpView().setReturnedCount(countResponse.getCount());
                        }
                    }
                });

    }


    @Override
    public void loadAccountData() {
        getMvpView().setAccountData(getDataManager().getCurrentAccount());
    }


    @Override
    public void requestAccountLogout() {
        getMvpView().showLoading();
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        getDataManager().setCurrentAccountLoggedIn(false);
                        Completable.fromAction(new Action() {
                            @Override
                            public void run() throws Exception {
                                getDataManager().deleteAllBarangayDataFromDb();
                            }
                        }).subscribeOn(getSchedulerProvider().io())
                                .observeOn(getSchedulerProvider().ui())
                                .subscribe(new CompletableObserver() {
                                    @Override
                                    public void onComplete() {
                                        if (isViewAttached()) {
                                            if (!getDataManager().isLoggedIn()) {
                                                getMvpView().hideLoading();
                                                getMvpView().onSuccessLogout();
                                            } else {
                                                getMvpView().onError("Logout failed");
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(final Throwable e) {
                                        Log.e(TAG, "onError: Delete All Barangay ", e);
                                    }

                                    @Override
                                    public void onSubscribe(final Disposable d) {
                                        getCompositeDisposable().add(d);
                                    }
                                });

                    }
                }, 2000);


    }

    @Override
    public void showAccountMessages() {
        int accountId = getDataManager().getCurrentAccount().getId();
        getMvpView().openChat(accountId); //It Opens Account ChatItem Box
    }
}
