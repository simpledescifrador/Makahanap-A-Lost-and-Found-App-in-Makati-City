package com.makatizen.makahanap.ui.return_item;

import android.text.TextUtils;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.api_response.MeetUpDetailsResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;
import com.makatizen.makahanap.utils.DateUtils;

import java.net.SocketException;
import java.net.SocketTimeoutException;

import javax.inject.Inject;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class ReturnItemPresenter<V extends ReturnItemMvpView> extends BasePresenter<V> implements ReturnItemMvpPresenter<V> {

    @Inject
    ReturnItemPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void loadMeetingDetails(int meetUpId) {
        getDataManager().getMeetingPlaceDetails(String.valueOf(meetUpId))
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(new SingleObserver<MeetUpDetailsResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(MeetUpDetailsResponse response) {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (response.getMeetupDate() != null && response.getMeetupPlace() != null) {
                            String location = response.getMeetupPlace();
                            String date = DateUtils.DateFormat(response.getMeetupDate()) + " " + DateUtils.TimeFormat(response.getMeetupDate());
                            getMvpView().setMeetUpDetails(location, date);
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

    @Override
    public boolean validate(String image) {
        if (TextUtils.isEmpty(image)) {
            getMvpView().onError("Please upload image");
        } else {
            return true;
        }
        return false;
    }

    @Override
    public void submit(int meetupId, int itemId, String imagePath) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed);
        } else {
            getDataManager().returnItemTransaction(String.valueOf(meetupId), String.valueOf(itemId), imagePath)
                    .observeOn(getSchedulerProvider().ui())
                    .subscribeOn(getSchedulerProvider().io())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            getMvpView().showLoading();
                        }
                    })
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideLoading();
                        }
                    })
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onComplete() {
                            getMvpView().onSubmitCompleted();
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
