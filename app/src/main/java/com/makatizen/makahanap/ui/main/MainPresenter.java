package com.makatizen.makahanap.ui.main;

import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.api_response.NotificationTotalResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;
import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class MainPresenter<V extends MainMvpView> extends BasePresenter<V> implements MainMvpPresenter<V>{

    private static final String TAG = "Main";

    @Inject
    MainPresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void loadNotificationBadge() {
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
                        if (notificationTotalResponse.getTotalNotification() > 0) {
                            /* It have unviewed notifications */
                            getMvpView().addBadge(3, notificationTotalResponse.getTotalNotification());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }
    @Override
    public void setNotificationBadge(int number) {
        if (number > 0) {
            getMvpView().addBadge(3, number);
        }
    }

    @Override
    public void updateUnViewedNotification(int number) {

    }
}
