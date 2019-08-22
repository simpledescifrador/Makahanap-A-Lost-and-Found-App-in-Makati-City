package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyMakatizenIdResponse {

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("valid")
    @Expose
    private boolean valid;

    public String getMessage() {
        return message;
    }

    public boolean isValid() {
        return valid;
    }

}
