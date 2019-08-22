package com.makatizen.makahanap.utils.enums;

import com.google.gson.annotations.SerializedName;

public enum AccountStatus {
    @SerializedName("New") NEW,
    @SerializedName("Active") ACTIVE,
    @SerializedName("Banned") BANNED,
    @SerializedName("Reported") REPORTED
}
