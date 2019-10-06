package com.makatizen.makahanap.ui.register.email_verification;

import android.text.TextUtils;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.api_response.EmailVerificationRequest;
import com.makatizen.makahanap.pojo.api_response.VerifyEmailVerificationCodeResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

public class EmailVerificationPresenter<V extends EmailVerificationMvpView> extends BasePresenter<V> implements EmailVerificationMvpPresenter<V> {

    @Inject
    EmailVerificationPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void startExpirationCountdown() {
        getMvpView().setExpirationTime(300000);
    }

    @Override
    public void verifyCode(String email, String code) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed);
        } else {
            getMvpView().showLoading();
            getDataManager().verifyEmailVerificationCode(email, code)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .delaySubscription(2000, TimeUnit.MILLISECONDS)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideLoading();
                        }
                    })
                    .subscribe(new SingleObserver<VerifyEmailVerificationCodeResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(VerifyEmailVerificationCodeResponse response) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (response.isExpired()) {
                                getMvpView().onError("The verification code is already expired");
                            } else if (!response.isValid()) {
                                getMvpView().onError("The verification code is not valid");
                            } else {
                                getMvpView().onSuccessfulValidation();
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
    public void resendCode(String email, String token) {

    }

    @Override
    public boolean validateCode(String code) {
        getMvpView().setCodeError(null);
        if (TextUtils.isEmpty(code)) {
            getMvpView().setCodeError("Provide the verification code");
        } else if (code.length() > 6) {
            getMvpView().setCodeError("Enter a 6-digit code");
        } else if (code.length() < 6) {
            getMvpView().setCodeError("Enter a 6-digit code");
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void emailVerification(String email, String token) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed);
        } else {
            getMvpView().showLoading();
            getDataManager().emailVerificationRequest(email, token)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .delaySubscription(2000, TimeUnit.MILLISECONDS)
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
                                getMvpView().onSuccessResendVerificationCode();
                                getMvpView().setExpirationTime(300000);
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
}
