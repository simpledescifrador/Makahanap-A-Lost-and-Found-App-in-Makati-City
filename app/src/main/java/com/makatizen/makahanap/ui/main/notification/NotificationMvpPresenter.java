package com.makatizen.makahanap.ui.main.notification;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface NotificationMvpPresenter<V extends NotificationMvpView & BaseMvpView> extends Presenter<V> {

    void loadNotification();

    void updateNotification();

    void deleteNotification(String id, int position);

    void setNotificationAsUnViewed(String id, int position);

    void setNotificationAsViewed(String id, int position);

    void updateNotificationBadge();
}
