package com.makatizen.makahanap.ui.main.account;

import android.os.Handler;
import android.util.Log;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.ui.base.BasePresenter;
import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import javax.inject.Inject;

public class AccountPresenter<V extends AccountMvpView> extends BasePresenter<V> implements AccountMvpPresenter<V> {

    private static final String TAG = "Account";

    @Inject
    AccountPresenter(final DataManager dataManager) {
        super(dataManager);
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
}
