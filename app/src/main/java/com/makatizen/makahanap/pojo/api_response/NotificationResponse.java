package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.pojo.Notification;

import java.util.List;

public class NotificationResponse {
    @SerializedName("total_results")
    @Expose
    private int totalResults;
    @SerializedName("notifications")
    @Expose
    private List<Notification> notifications = null;

    public int getTotalResults() {
        return totalResults;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }
}
