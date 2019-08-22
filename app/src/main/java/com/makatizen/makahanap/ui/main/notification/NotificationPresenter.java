package com.makatizen.makahanap.ui.main.notification;

import android.util.Log;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.Notification;
import com.makatizen.makahanap.pojo.api_response.NotificationDeleteResponse;
import com.makatizen.makahanap.pojo.api_response.NotificationResponse;
import com.makatizen.makahanap.pojo.api_response.NotificationTotalResponse;
import com.makatizen.makahanap.pojo.api_response.NotificationUpdateResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class NotificationPresenter<V extends NotificationMvpView> extends BasePresenter<V>
        implements NotificationMvpPresenter<V> {

    private static final String TAG = "notification";

    @Inject
    NotificationPresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void loadNotification() {
        Log.d(TAG, "loadNotification: ");
        String accountId = String.valueOf(getDataManager().getCurrentAccount().getId());
        getDataManager().getNotifications(accountId)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(new SingleObserver<NotificationResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(NotificationResponse notificationResponse) {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (notificationResponse.getTotalResults() != 0) {
                            Collections.reverse(notificationResponse.getNotifications());
                            getMvpView().setNotifications(notificationResponse.getNotifications());
                        } else {
                            //No Notifications
                            getMvpView().noNotification();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: Loading Notifications", e);
                        if (!isViewAttached()) {
                            return;
                        }

                        if (e instanceof SocketTimeoutException
                                || e instanceof SocketException) {
                            getMvpView().noNetworkConnection();
                        }
                    }
                });
    }

    @Override
    public void updateNotification() {
        Log.d(TAG, "updateNotification: ");
        String accountId = String.valueOf(getDataManager().getCurrentAccount().getId());

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.title_no_network);
        } else {
            getDataManager().getNotifications(accountId)
                    .delaySubscription(2000, TimeUnit.MILLISECONDS)
                    .observeOn(getSchedulerProvider().ui())
                    .subscribeOn(getSchedulerProvider().io())
                    .subscribe(new SingleObserver<NotificationResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(NotificationResponse notificationResponse) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (notificationResponse.getTotalResults() != 0) {
                                Collections.reverse(notificationResponse.getNotifications());
                                getMvpView().onUpdatedNotifications(notificationResponse.getNotifications());
                            } else {
                                //No Notifications
                                getMvpView().noNotification();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e(TAG, "onError: Loading Notifications", e);
                            if (!isViewAttached()) {
                                return;
                            }

                            if (e instanceof SocketTimeoutException
                                    || e instanceof SocketException) {
                                getMvpView().noNetworkConnection();
                            }
                        }
                    });
        }

    }

    @Override
    public void deleteNotification(String id, final int position) {
        final String accountId = String.valueOf(getDataManager().getCurrentAccount().getId());
        getDataManager().deleteNotification(id)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(new SingleObserver<NotificationDeleteResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(final NotificationDeleteResponse notificationDeleteResponse) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().onDeleteSuccess(position);

                        //Check if no notifications left
                        getDataManager().getTotalNotifications(accountId)
                                .observeOn(getSchedulerProvider().ui())
                                .subscribeOn(getSchedulerProvider().io())
                                .subscribe(new SingleObserver<NotificationTotalResponse>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {
                                        getCompositeDisposable().add(d);
                                    }

                                    @Override
                                    public void onSuccess(NotificationTotalResponse notificationTotalResponse) {
                                        if (!isViewAttached()) {
                                            return;
                                        }

                                        if (notificationTotalResponse.getTotalNotification() == 0) {
                                            getMvpView().noNotification();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        if (!isViewAttached()) {
                                            return;
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (e instanceof SocketTimeoutException
                                || e instanceof SocketException) {
                            getMvpView().onError("No Network Connection. Unable to delete.");
                        }
                    }
                });
    }

    @Override
    public void setNotificationAsUnViewed(String id, final int position) {
        getDataManager().setNotificationUnviewed(id)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(new SingleObserver<NotificationUpdateResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(NotificationUpdateResponse notificationUpdateResponse) {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().setNotificationUnViewed(position);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (e instanceof SocketTimeoutException
                                || e instanceof SocketException) {
                            getMvpView().onError("No Network Connection. Unable to update");
                        }
                    }
                });
    }

    @Override
    public void setNotificationAsViewed(String id, final int position) {
        getDataManager().setNotificationViewed(id)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(new SingleObserver<NotificationUpdateResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(NotificationUpdateResponse notificationUpdateResponse) {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().setNotificationViewed(position);
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (e instanceof SocketTimeoutException
                                || e instanceof SocketException) {
                            getMvpView().onError("No Network Connection. Unable to update");
                        }
                    }
                });
    }

    @Override
    public void updateNotificationBadge() {
        String accountId = String.valueOf(getDataManager().getCurrentAccount().getId());
        getDataManager().getTotalUnviewedNotifications(accountId)
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(new SingleObserver<NotificationTotalResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(NotificationTotalResponse notificationTotalResponse) {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().onUpdateNotificationBadge(notificationTotalResponse.getTotalNotification());
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }
                    }
                });

    }
}
