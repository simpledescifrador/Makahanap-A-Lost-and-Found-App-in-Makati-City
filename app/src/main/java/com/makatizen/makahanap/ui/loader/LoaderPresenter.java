package com.makatizen.makahanap.ui.loader;

import android.os.Handler;
import android.util.Log;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.ui.base.BasePresenter;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class LoaderPresenter<V extends LoaderMvpView> extends BasePresenter<V> implements LoaderMvpPresenter<V> {

    private static final String TAG = "Loader";

    @Inject
    LoaderPresenter(final DataManager dataManager) {
        super(dataManager);
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
                    .delaySubscription(3000, TimeUnit.MILLISECONDS)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(final Disposable disposable) throws Exception {
                            getMvpView().nextLoadingMessage();
                        }
                    })
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().nextLoadingMessage();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getMvpView().onCompletedLoader();
                                }
                            }, 2000);
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
                            }
                        }
                    });
        }
    }
}
