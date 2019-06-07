package com.makatizen.makahanap.data.remote;

import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.api_response.LoginResponse;
import com.makatizen.makahanap.pojo.api_response.RegisterReponse;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET(ApiConstants.GET_ACCOUNT_DATA + "{account_id}")
    Single<MakahanapAccount> getMakahanapAccountData(@Path("account_id") int accountId);

    @FormUrlEncoded
    @POST(ApiConstants.LOGIN_REQUEST_URL)
    Single<LoginResponse> loginAppRequest(
            @Field("makatizen_number") String makatizenNumber,
            @Field("password") String password
    );

    @POST(ApiConstants.REGISTER_ACCOUNT_URL)
    Single<RegisterReponse> registerNewAccount(@Body MakahanapAccount makahanapAccount);
}
