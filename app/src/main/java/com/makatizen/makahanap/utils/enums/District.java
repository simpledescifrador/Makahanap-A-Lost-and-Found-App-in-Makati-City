package com.makatizen.makahanap.utils.enums;

import com.google.gson.annotations.SerializedName;

public enum District {
    
    @SerializedName("1") ONE(1),

    @SerializedName("2") TWO(2);

    private int code;

    District(final int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
