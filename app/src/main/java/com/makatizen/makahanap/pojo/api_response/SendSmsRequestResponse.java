package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendSmsRequestResponse {
    @SerializedName("sms_sent")
    @Expose
    private Boolean smsSent;

    @SerializedName("request_id")
    @Expose
    private String requestId;

    public Boolean isSmsSent() {
        return smsSent;
    }

    public String getRequestId() {
        return requestId;
    }
}
