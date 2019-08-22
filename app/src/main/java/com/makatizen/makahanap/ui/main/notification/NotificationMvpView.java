package com.makatizen.makahanap.ui.main.notification;

import com.makatizen.makahanap.pojo.Notification;
import com.makatizen.makahanap.ui.base.BaseMvpView;
import java.util.List;

public interface NotificationMvpView extends BaseMvpView {

    void noNetworkConnection();

    void noNotification();

    void onUpdatedNotifications(List<Notification> notificationList);

    void setNotifications(List<Notification> notificationList);

    void onDeleteSuccess(int position);

    void setNotificationUnViewed(int position);

    void setNotificationViewed(int position);

    void onUpdateNotificationBadge(int number);
}
