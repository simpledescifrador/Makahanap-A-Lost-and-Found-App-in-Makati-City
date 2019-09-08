package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateReturnTransactionResponse {
    @SerializedName("updated")
    @Expose
    private Boolean updated;

    @SerializedName("account_name")
    @Expose
    private String accountName;

    @SerializedName("profile_image")
    @Expose
    private String profileImage;

    @SerializedName("account_id")
    @Expose
    private Integer accountId;

    public Integer getAccountId() {
        return accountId;
    }

    public String getAccountName() {
        return accountName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public Boolean isUpdated() {
        return updated;
    }
}
