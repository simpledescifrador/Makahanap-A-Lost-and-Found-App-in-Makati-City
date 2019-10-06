package com.makatizen.makahanap.ui.login.forgot_password;

import android.text.TextUtils;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.api_response.ChangePasswordResponse;
import com.makatizen.makahanap.pojo.api_response.CheckMakatizenResponse;
import com.makatizen.makahanap.pojo.api_response.EmailVerificationRequest;
import com.makatizen.makahanap.ui.base.BasePresenter;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

public class ForgotPasswordPresenter<V extends ForgotPasswordMvpView> extends BasePresenter<V> implements ForgotPasswordMvpPresenter<V> {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{8,}" +
                    "$");

    @Inject
    ForgotPasswordPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void checkMakatizenNumber(String makatizenNumber) {
        getMvpView().hideKeyboard();
        getMvpView().removeErrors();
        //Validate Makatizen Number
        if (TextUtils.isEmpty(makatizenNumber)) {
            getMvpView().setMakatizenNumberError("Enter your makatizen number");
        } else if (makatizenNumber.length() < 12) {
            getMvpView().setMakatizenNumberError("Invalid makatizen number");
        } else if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed);
        } else {
            getMvpView().showLoading();
            getDataManager().checkMakatizenNumberRegistration(makatizenNumber)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .delaySubscription(1000, TimeUnit.MILLISECONDS)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideLoading();
                        }
                    })
                    .subscribe(new SingleObserver<CheckMakatizenResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(CheckMakatizenResponse response) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (response.getStatus() == 1) { //Registered
                                String email = response.getEmail();
                                getMvpView().onSuccessRegisteredMakatizen(email);
                            } else {
                                getMvpView().onError("Makatizen number is not registered");
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
    public void sendEmailVerification(final String email) {
        getMvpView().showLoading();
        getDataManager().requestForgotPasswordEmailVerification(email)
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
                            getMvpView().successEmailVerification(email);
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

    @Override
    public boolean validatePassword(String password, String confPassword) {
        getMvpView().hideKeyboard();
        getMvpView().removeErrors();
        if (TextUtils.isEmpty(password)) {
            getMvpView().setPasswordError(R.string.error_password_empty);
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            getMvpView().setPasswordError(R.string.error_invalid_password);
        } else if (!password.equals(confPassword)) {
            getMvpView().setConfirmPasswordError(R.string.error_confirm_password_not_match);
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void resetPassword(String makatizenNumber, String password) {
        getMvpView().hideKeyboard();
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed);
        } else {
            getMvpView().showLoading();
            getDataManager().resetPassword(makatizenNumber, password)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .delaySubscription(2000, TimeUnit.MILLISECONDS)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideLoading();
                        }
                    })
                    .subscribe(new SingleObserver<ChangePasswordResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(ChangePasswordResponse response) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (response.getStatus() == 1) { //Success Reset Password
                                getMvpView().onSuccessResetPassword();
                            } else {
                                getMvpView().onFailedResetPassword();
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
