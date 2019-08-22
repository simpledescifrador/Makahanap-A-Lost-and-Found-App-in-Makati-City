package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationDeleteResponse {
    @SerializedName("notification_id")
    @Expose
    private Integer notificationId;

    @SerializedName("success")
    @Expose
    private Boolean updateSuccess;

    public Integer getNotificationId() {
        return notificationId;
    }

    public Boolean isUpdateSuccess() {
        return updateSuccess;
    }
}
