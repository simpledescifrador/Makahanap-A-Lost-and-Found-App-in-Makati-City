package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TransactionConfirmResponse {
    @SerializedName("already_confirmed")
    @Expose
    private Boolean alreadyConfirmed;

    @SerializedName("transaction_id")
    @Expose
    private Integer transactionId;

    public Boolean isAlreadyConfirmed() {
        return alreadyConfirmed;
    }

    public Integer getTransactionId() {
        return transactionId;
    }
}
