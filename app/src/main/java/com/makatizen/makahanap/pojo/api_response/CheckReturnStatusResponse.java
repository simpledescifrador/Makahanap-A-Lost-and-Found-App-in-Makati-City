package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CheckReturnStatusResponse {
    @SerializedName("return_valid")
    @Expose
    private Boolean valid;

    @SerializedName("meetup_id")
    @Expose
    private Integer meetupId;
    @SerializedName("account_id")
    @Expose
    private Integer accountId;
    @SerializedName("return_transaction_id")
    @Expose
    private Integer returnTransactionId;

    public Integer getAccountId() {
        return accountId;
    }

    public Integer getReturnTransactionId() {
        return returnTransactionId;
    }

    public Boolean isValidReturn() {
        return valid;
    }

    public Integer getMeetupId() {
        return meetupId;
    }
}
