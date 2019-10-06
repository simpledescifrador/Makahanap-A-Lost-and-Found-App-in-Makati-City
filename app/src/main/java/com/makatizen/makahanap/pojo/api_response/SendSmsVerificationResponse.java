package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendSmsVerificationResponse {
    @SerializedName("request_id")
    @Expose
    private String requestId;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("error_text")
    @Expose
    private String errorText;

    public String getRequestId() {
        return requestId;
    }

    public String getStatus() {
        return status;
    }

    public String getErrorText() {
        return errorText;
    }
}
