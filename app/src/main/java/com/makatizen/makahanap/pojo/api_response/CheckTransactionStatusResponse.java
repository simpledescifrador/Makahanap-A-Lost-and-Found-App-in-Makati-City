package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckTransactionStatusResponse {
    @SerializedName("return")
    @Expose
    private Boolean toReturned = false;
    @SerializedName("already_confirmed")
    @Expose
    private Boolean alreadyConfirmed = false;
    @SerializedName("transaction_id")
    @Expose
    private Integer transactionId = 0;
    @SerializedName("meetup_id")
    @Expose
    private Integer meetupId = 0;
    @SerializedName("confirmation")
    @Expose
    private String confirmation = null;

    public Boolean isToReturned() {
        return toReturned;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public Boolean isAlreadyConfirmed() {
        return alreadyConfirmed;
    }

    public Integer getTransactionId() {
        return transactionId;
    }

    public Integer getMeetupId() {
        return meetupId;
    }
}
