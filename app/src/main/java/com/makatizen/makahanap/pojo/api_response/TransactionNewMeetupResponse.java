package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionNewMeetupResponse {
    @SerializedName("status")
    @Expose
    private Boolean status;

    @SerializedName("meetup_id")
    @Expose
    private Integer meetupId;

    public Boolean getStatus() {
        return status;
    }

    public Integer getMeetupId() {
        return meetupId;
    }
}
