package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmailVerificationRequest {
    @SerializedName("email_sent")
    @Expose
    private Boolean isEmailSent;

    public Boolean isEmailSent() {
        return isEmailSent;
    }
}
