package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterTokenResponse {

    @SerializedName("success")
    @Expose
    private Boolean isSucessful;

    @SerializedName("message")
    @Expose
    private String message;

    public Boolean isSucessful() {
        return isSucessful;
    }

    public String getMessage() {
        return message;
    }
}
