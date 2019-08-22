package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationUpdateResponse {
    @SerializedName("success")
    @Expose
    private Boolean updateSuccess;

    public Boolean isUpdateSuccess() {
        return updateSuccess;
    }

}
