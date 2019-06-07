package com.makatizen.makahanap.ui.login;

import android.text.TextUtils;
import android.util.Log;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.api_response.LoginResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class LoginPresenter<V extends LoginMvpView> extends BasePresenter<V> implements LoginMvpPresenter<V> {

    private static final String TAG = "Login";

    @Inject
    LoginPresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void doAppLogin(final String makatizenNumber, final String password) {
        Log.d(TAG, "doAppLogin: Your Makatizen Number: " + makatizenNumber + " and your password: " + password);
        getMvpView().hideKeyboard();

        if (getMvpView().isNetworkConnected()) {
            //Network Connected
            getMvpView().showLoading();
            getDataManager().loginAppRequest(makatizenNumber, password)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .delaySubscription(3000, TimeUnit.MILLISECONDS)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(final Disposable disposable) throws Exception {
                            getMvpView().onLoginRequest();
                        }
                    })
                    .subscribe(new SingleObserver<LoginResponse>() {
                        @Override
                        public void onError(final Throwable e) {
                            Log.e(TAG, "onError: Do App Login", e);

                            if (!isViewAttached()) {
                                return;
                            }
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
                        public void onSuccess(final LoginResponse loginResponse) {
                            Log.d(TAG, "onSuccess: App Login ");

                            if (!isViewAttached()) {
                                return;
                            }
                            getMvpView().hideLoading();

                            if (loginResponse.isValid()) {
                                getMvpView().onLoginSuccessful(loginResponse.getAccountId());
                            } else {
                                getMvpView().onError(loginResponse.getMessage());
                                getMvpView().onLoginFailed();
                            }

                        }
                    });
        } else {
            //No internet Access
            getMvpView().onError(R.string.error_network_failed);
        }

    }

    @Override
    public boolean validateLoginDetails(final String makatizenNumber, final String password) {
        getMvpView().removeErrors();
        if (TextUtils.isEmpty(makatizenNumber)) {
            getMvpView().setMakatizenNumberError(R.string.error_empty_makatizen_id);
        } else if (TextUtils.isEmpty(password)) {
            getMvpView().setPasswordError(R.string.error_password_empty);
        } else {
            return true;
        }
        return false;
    }
}
