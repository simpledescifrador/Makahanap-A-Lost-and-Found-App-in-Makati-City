package com.makatizen.makahanap.ui.register;

import android.text.TextUtils;
import android.util.Log;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.api_response.MakatizenGetDataResponse;
import com.makatizen.makahanap.pojo.api_response.RegisterReponse;
import com.makatizen.makahanap.pojo.api_response.VerifyMakatizenIdResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class RegisterPresenter<V extends RegisterMvpView> extends BasePresenter<V>
        implements RegisterMvpPresenter<V> {

    private static final String TAG = "Register";


    @Inject
    RegisterPresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void getMakatizenData(final String makatizenNumber) {
        getMvpView().hideKeyboard();

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed); //Network Failed
        } else {
            getMvpView().showLoading();
            getDataManager().getMakatizenData(makatizenNumber)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(new SingleObserver<MakatizenGetDataResponse>() {
                        @Override
                        public void onError(final Throwable e) {
                            Log.e(TAG, "onError: ", e);
                            checkViewAttached();
                            getMvpView().hideLoading();

                            if (e instanceof SocketTimeoutException || e instanceof SocketException) {
                                getMvpView().onError(R.string.error_network_failed);
                            }
                        }

                        @Override
                        public void onSubscribe(final Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(final MakatizenGetDataResponse response) {
                            Log.d(TAG, "onSuccess: ");
                            //Check if View is attached
                            checkViewAttached();
                            getMvpView().hideLoading();

                            if (response.isStatus()) {
                                getMvpView().makatizenDataSuccess(response.getMakatizenAccount());
                            } else {
                                getMvpView().onError(response.getMessage());
                            }
                        }
                    });
        }
    }

    @Override
    public void registerAccount(final MakahanapAccount account) {
        Log.d(TAG, "registerAccount: ");
        getMvpView().hideKeyboard();
        getMvpView().removeErrors();

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed); //Network Failed
        } else {
            getMvpView().showLoading();

            getDataManager().registerNewAccount(account)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .delaySubscription(3000, TimeUnit.MILLISECONDS)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().resetButtonText();
                        }
                    })
                    .subscribe(new SingleObserver<RegisterReponse>() {
                        @Override
                        public void onError(final Throwable e) {
                            Log.e(TAG, "onError: ", e);
                            checkViewAttached();
                            getMvpView().hideLoading();

                            if (e instanceof SocketTimeoutException || e instanceof SocketException) {
                                getMvpView().onError(R.string.error_network_failed);
                            }
                        }

                        @Override
                        public void onSubscribe(final Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(final RegisterReponse registerReponse) {
                            Log.d(TAG, "onSuccess: Register Response");
                            getMvpView().hideLoading();

                            if (registerReponse.isSuccessful()) {
                                getMvpView().onRegisterSuccessful(registerReponse.getAccountId());
                            } else {
                                getMvpView().onError(registerReponse.getMessage());
                            }
                        }
                    });
        }
    }

    @Override
    public boolean validatePassword(final String password) {
        if (TextUtils.isEmpty(password)) {
            getMvpView().setPasswordError(R.string.error_password_empty);
        } else if (password.length() < 7) {
            getMvpView().setPasswordError(R.string.error_password_invalid_length);
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void verifyMakatizenId(final String id) {
        getMvpView().hideKeyboard();
        getMvpView().removeErrors();

        if (TextUtils.isEmpty(id)) {
            getMvpView().onError(R.string.error_empty_makatizen_id);
        } else if (id.length() < 12) {
            getMvpView().onError(R.string.error_invalid_makatizen_id);
        } else {
            //Check first if network is connected
            if (!getMvpView().isNetworkConnected()) {
                getMvpView().onError(R.string.error_network_failed); //Network Failed
            } else {
                //Network Connection is Good
                getMvpView().showLoading();
                getDataManager().verifyMakatizenId(id)
                        .subscribeOn(getSchedulerProvider().io())
                        .observeOn(getSchedulerProvider().ui())
                        .delaySubscription(1000, TimeUnit.MILLISECONDS)
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                getMvpView().resetButtonText();
                            }
                        })
                        .subscribe(new SingleObserver<VerifyMakatizenIdResponse>() {
                            @Override
                            public void onError(final Throwable e) {
                                Log.e(TAG, "Makatizen ID Verification Error", e);
                                checkViewAttached();
                                getMvpView().hideLoading();

                                if (e instanceof SocketTimeoutException || e instanceof SocketException) {
                                    getMvpView().onError(R.string.error_network_failed);
                                }
                            }

                            @Override
                            public void onSubscribe(final Disposable d) {
                                Log.d(TAG, "Makatizen Verification Subscribe");
                                getCompositeDisposable().add(d);
                            }

                            @Override
                            public void onSuccess(final VerifyMakatizenIdResponse verifyMakatizenIdResponse) {
                                Log.d(TAG, "Makatizen Verification Success");
                                //Check if View is attached
                                checkViewAttached();
                                getMvpView().hideLoading();

                                if (verifyMakatizenIdResponse.isValid()) {
                                    getMvpView().validMakatizenId();
                                } else {
                                    getMvpView().invalidMakatizenId();
                                }
                            }
                        });
            }
        }
    }

    @Override
    public boolean verifyPasswordIsMatch(final String password, final String repeatedPassword) {
        if (!password.equals(repeatedPassword)) {
            getMvpView().setConfirmPasswordError(R.string.error_confirm_password_not_match);
        } else {
            return true;
        }
        return false;
    }
}
