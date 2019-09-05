package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetUpDetailsResponse {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("transaction_id")
    @Expose
    private Integer transactionId;

    @SerializedName("meetup_place")
    @Expose
    private String meetupPlace;

    @SerializedName("meetup_date")
    @Expose
    private String meetupDate;

    public Integer getId() {
        return id;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public String getMeetupPlace() {
        return meetupPlace;
    }

    public String getMeetupDate() {
        return meetupDate;
    }
}
