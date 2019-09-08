package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnItemTransactionResponse {
    @SerializedName("success")
    @Expose
    private Boolean success;

    @SerializedName("transaction_id")
    @Expose
    private Integer transactionId;

    public Boolean isSuccess() {
        return success;
    }

    public Integer getTransactionId() {
        return transactionId;
    }
}
