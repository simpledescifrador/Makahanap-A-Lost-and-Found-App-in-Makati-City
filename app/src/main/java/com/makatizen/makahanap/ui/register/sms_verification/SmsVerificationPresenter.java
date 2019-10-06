package com.makatizen.makahanap.ui.register.sms_verification;

import android.text.TextUtils;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.api_response.CancelSmsRequestResponse;
import com.makatizen.makahanap.pojo.api_response.CheckSmsRequestResponse;
import com.makatizen.makahanap.pojo.api_response.SendSmsRequestResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

public class SmsVerificationPresenter<V extends SmsVerificationMvpView> extends BasePresenter<V> implements SmsVerificationMvpPresenter<V> {

    @Inject
    SmsVerificationPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public boolean validateCode(String code, int attempts) {
        getMvpView().setCodeError(null);
        if (attempts >= 2) {
            getMvpView().onError("No Valid Attempts. Please resend code");
        } else if (TextUtils.isEmpty(code)) {
            getMvpView().setCodeError("Provide the code");
        } else if (code.length() > 4) {
            getMvpView().setCodeError("Enter a 4-digit code");
        } else if (code.length() < 4) {
            getMvpView().setCodeError("Enter a 4-digit code");
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void resendRequest(final String number, final String requestId) {
        //Cancel request first
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed);
        } else {
            getMvpView().showLoading();
            getDataManager().cancelSmsRequest(requestId)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(new SingleObserver<CancelSmsRequestResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(CancelSmsRequestResponse response) {
                            if (!isViewAttached()) {
                                return;
                            }

                            //Now Resent code
                            getDataManager().sendSmsRequest(number)
                                    .subscribeOn(getSchedulerProvider().io())
                                    .observeOn(getSchedulerProvider().ui())
                                    .subscribeOn(getSchedulerProvider().io())
                                    .observeOn(getSchedulerProvider().ui())
                                    .delaySubscription(2000, TimeUnit.MILLISECONDS)
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
                                                getMvpView().resetAttempts();
                                                getMvpView().setExpirationTime(300000);
                                                getMvpView().onSuccessfulResendCode(response.getRequestId());
                                            } else {
                                                getMvpView().onError("Failed to resent new verification code");
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
    public void checkRequest(String code, final String requestId) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed);
        } else {
            getMvpView().showLoading();
            getMvpView().consumeAttempts();
            getDataManager().checkSmsRequest(code, requestId)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .delaySubscription(2000, TimeUnit.MILLISECONDS)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideLoading();
                        }
                    })
                    .subscribe(new SingleObserver<CheckSmsRequestResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(CheckSmsRequestResponse response) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (response.isVerified()) {
                                getMvpView().onSuccessfulVerification();
                            } else {
                                getMvpView().onError("Verification code is invalid");
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
    public void cancelRequest(String requestId) {
        getDataManager().cancelSmsRequest(requestId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new SingleObserver<CancelSmsRequestResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(CancelSmsRequestResponse response) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    @Override
    public void startExpirationCountdown() {
        getMvpView().setExpirationTime(300000);
    }
}
