package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationTotalResponse {
    @SerializedName("total_results")
    @Expose
    private Integer totalNotification;

    public Integer getTotalNotification() {
        return totalNotification;
    }
}
