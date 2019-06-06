package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.pojo.MakatizenAccount;

public class MakatizenGetDataResponse {

    @SerializedName("data")
    @Expose
    private MakatizenAccount makatizenAccount;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("status")
    @Expose
    private Boolean status;

    public MakatizenAccount getMakatizenAccount() {
        return makatizenAccount;
    }

    public String getMessage() {
        return message;
    }

    public Boolean isStatus() {
        return status;
    }
}
