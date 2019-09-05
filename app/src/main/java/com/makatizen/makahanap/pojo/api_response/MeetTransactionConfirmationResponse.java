package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MeetTransactionConfirmationResponse {
    @SerializedName("confirmation")
    @Expose
    private Boolean confirmation;

    public Boolean isConfirmed() {
        return confirmation;
    }
}
