package com.makatizen.makahanap.ui.register;

import android.text.TextUtils;
import android.util.Log;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.api_response.EmailVerificationRequest;
import com.makatizen.makahanap.pojo.api_response.MakatizenGetDataResponse;
import com.makatizen.makahanap.pojo.api_response.RegisterReponse;
import com.makatizen.makahanap.pojo.api_response.SendSmsRequestResponse;
import com.makatizen.makahanap.pojo.api_response.VerifyMakatizenIdResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

public class RegisterPresenter<V extends RegisterMvpView> extends BasePresenter<V>
        implements RegisterMvpPresenter<V> {

    private static final String TAG = "Register";

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{8,}" +
                    "$");

    @Inject
    RegisterPresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void emailVerification(final String email, String token) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed);
        } else {
            getMvpView().showLoading();
            getDataManager().emailVerificationRequest(email, token)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideLoading();
                        }
                    })
                    .subscribe(new SingleObserver<EmailVerificationRequest>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(EmailVerificationRequest response) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (response.isEmailSent()) {
                                getMvpView().successfulEmailVerificationRequest(email);
                            } else {
                                getMvpView().onError("Failed to request email verification");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (!isViewAttached()) {
                                return;
                            }
                            if (e instanceof SocketTimeoutException || e instanceof SocketException) {
                                getMvpView().onError(R.string.error_network_failed);
                            }
                        }
                    });
        }
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
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            getMvpView().setPasswordError(R.string.error_invalid_password);
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
//                                    getMvpView().validMakatizenId();
                                    getDataManager().getMakatizenData(id)
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

                                                    if (response.isStatus()) {
                                                        getMvpView().showVerificationOption(response.getMakatizenAccount().getEmailAddress(), response.getMakatizenAccount().getContactNumber());
                                                    } else {
                                                        getMvpView().onError(response.getMessage());
                                                    }
                                                }
                                            });
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

    @Override
    public void passwordToggle(boolean showPass) {
        if (showPass) {
            getMvpView().showPassword();
        } else {
            getMvpView().hidePassword();
        }
    }

    @Override
    public void smsVerification(final String phoneNumber) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed);
        } else {
            getMvpView().showLoading();
            getDataManager().sendSmsRequest(phoneNumber)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideLoading();
                        }
                    })
                    .subscribe(new SingleObserver<SendSmsRequestResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(SendSmsRequestResponse response) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (response.isSmsSent()) {
                                getMvpView().successfulSmsVerificationRequest(response.getRequestId(), phoneNumber);
                            } else {
                                getMvpView().onError("Failed to send sms request. Please try again");
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (!isViewAttached()) {
                                return;
                            }
                            if (e instanceof SocketTimeoutException || e instanceof SocketException) {
                                getMvpView().onError(R.string.error_network_failed);
                            }
                        }
                    });
        }
    }
}
