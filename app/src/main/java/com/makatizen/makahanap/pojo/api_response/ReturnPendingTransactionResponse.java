package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReturnPendingTransactionResponse {
    @SerializedName("returned")
    @Expose
    private Boolean returned;

    @SerializedName("return_transaction_id")
    @Expose
    private Integer returnTransationId = null;

    public Boolean isReturned() {
        return returned;
    }

    public Integer getReturnTransationId() {
        return returnTransationId;
    }
}
