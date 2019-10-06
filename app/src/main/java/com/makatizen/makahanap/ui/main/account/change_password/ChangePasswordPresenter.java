package com.makatizen.makahanap.ui.main.account.change_password;

import android.text.TextUtils;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.api_response.ChangePasswordResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;

public class ChangePasswordPresenter<V extends ChangePasswordMvpView> extends BasePresenter<V> implements ChangePasswordMvpPresenter<V> {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +
                    "(?=.*[@#$%^&+=])" +
                    "(?=\\S+$)" +
                    ".{8,}" +
                    "$");

    @Inject
    ChangePasswordPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public boolean validateNewPassword(String newPassword) {
        getMvpView().setPasswordError(null);
        if (TextUtils.isEmpty(newPassword)) {
            getMvpView().setPasswordError("Enter your new password");
        } else if (!PASSWORD_PATTERN.matcher(newPassword).matches()) {
            getMvpView().setPasswordError("Invalid Password");
        } else {
            return true;
        }
        return false;
    }

    @Override
    public boolean confirmPassword(String newPassword, String confirmPassword) {
        getMvpView().setConfirmPasswordError(null);

        if (!newPassword.equals(confirmPassword)) {
            getMvpView().setConfirmPasswordError("Password does not match");
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void changePassword(String newPassword, String oldPassword) {
        String accountId = getDataManager().getCurrentAccount().getId().toString();

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed);
        } else {
            getMvpView().showLoading();
            getDataManager().changePassword(accountId, newPassword, oldPassword)
                    .observeOn(getSchedulerProvider().ui())
                    .subscribeOn(getSchedulerProvider().io())
                    .delaySubscription(1000, TimeUnit.MILLISECONDS)
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

                            if (response.getStatus() == 0) {
                                getMvpView().onError("Invalid Current Password. Please try again");
                            } else if (response.getStatus() == 2) {
                                getMvpView().onError(response.getMessage());
                            } else {
                                getMvpView().onSuccessChangePassword();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (!isViewAttached()) {
                                return;
                            }
                            if (e instanceof SocketTimeoutException
                                    || e instanceof SocketException) {
                                getMvpView().onError(R.string.error_network_failed);
                            }
                        }
                    });
        }
    }
}
