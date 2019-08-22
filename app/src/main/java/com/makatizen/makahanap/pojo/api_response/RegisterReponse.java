package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterReponse {

    @SerializedName("account_id")
    @Expose
    private Integer accountId;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("successful")
    @Expose
    private Boolean successful;

    public Integer getAccountId() {
        return accountId;
    }

    public String getMessage() {
        return message;
    }

    public Boolean isSuccessful() {

        return successful;
    }
}
