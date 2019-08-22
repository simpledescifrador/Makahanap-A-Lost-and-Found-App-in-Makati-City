package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    @SerializedName("account_id")
    @Expose
    private int accountId;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("valid")
    @Expose
    private boolean valid;

    public int getAccountId() {
        return accountId;
    }

    public String getMessage() {
        return message;
    }

    public boolean isValid() {
        return valid;
    }
}
