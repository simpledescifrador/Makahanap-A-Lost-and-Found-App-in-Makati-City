package com.makatizen.makahanap.pojo.api_response;

public class VerifyEmailVerificationCodeResponse {

    private Boolean valid;

    private Boolean expired;

    public Boolean isValid() {
        return valid;
    }

    public Boolean isExpired() {
        return expired;
    }
}
