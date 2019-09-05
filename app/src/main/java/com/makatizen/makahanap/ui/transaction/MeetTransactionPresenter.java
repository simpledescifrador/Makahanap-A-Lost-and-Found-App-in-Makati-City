package com.makatizen.makahanap.ui.transaction;

import android.text.TextUtils;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.LocationData;
import com.makatizen.makahanap.pojo.api_response.TransactionNewMeetupResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class MeetTransactionPresenter<V extends MeetTransactionMvpView> extends BasePresenter<V> implements MeetTransactionMvpPresenter<V> {

    @Inject
    MeetTransactionPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public boolean validateForm(LocationData locationData, String date, String time) {
        if (locationData.getId() == null) {
            getMvpView().onError("Select location");
        } else if (date.equals("YYYY/MM/DD")) {
            getMvpView().setDateError("Please select a date");
        } else if (TextUtils.isEmpty(time)) {
            getMvpView().setTimeError("Please select a time");
        } else {
            return true;
        }

        return false;
    }

    @Override
    public void submitMeetingSetup(LocationData locationData, String datetime, String transactionId) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed);
        } else {
            getDataManager().createNewMeetup(locationData, datetime, transactionId)
                    .observeOn(getSchedulerProvider().ui())
                    .subscribeOn(getSchedulerProvider().io())
                    .delaySubscription(2000, TimeUnit.MILLISECONDS)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(final Disposable disposable) throws Exception {
                            getMvpView().showLoading();
                        }
                    })
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideLoading();
                        }
                    })
                    .subscribe(new SingleObserver<TransactionNewMeetupResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(TransactionNewMeetupResponse transactionNewMeetupResponse) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (transactionNewMeetupResponse.getStatus()) {
                                getMvpView().onSubmitSuccess(transactionNewMeetupResponse.getMeetupId());
                            } else {

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
