package com.makatizen.makahanap.utils.enums;

import com.google.gson.annotations.SerializedName;

public enum CivilStatus {
    @SerializedName("Single") SINGLE,
    @SerializedName("Married") MARRIED,
    @SerializedName("Common Law") COMMON_LAW,
    @SerializedName("Widowed") WIDOWED,
    @SerializedName("Divorced") DIVORCED,
}
