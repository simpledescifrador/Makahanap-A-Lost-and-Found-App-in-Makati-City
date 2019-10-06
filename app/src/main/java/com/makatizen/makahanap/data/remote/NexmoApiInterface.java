package com.makatizen.makahanap.data.remote;

import com.makatizen.makahanap.pojo.api_response.SendSmsVerificationResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NexmoApiInterface {
    @GET("send-verification-code")
    Single<SendSmsVerificationResponse> sendVerificationCode(
            @Query("phoneNumber") String phoneNumber,
            @Query("brand") String brand
    );


}
