package com.makatizen.makahanap.data.remote;

import com.makatizen.makahanap.pojo.api_response.MakatizenGetDataResponse;
import com.makatizen.makahanap.pojo.api_response.VerifyMakatizenIdResponse;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MakatizenApiInterface {

    @GET(ApiConstants.GET_MAKATIZEN_DATA_URL + "{id}")
    Single<MakatizenGetDataResponse> getMakatizenData(@Path("id") String makatizenId);

    @FormUrlEncoded
    @POST(ApiConstants.VERIFY_MAKATIZEN_ID_URL)
    Single<VerifyMakatizenIdResponse> validateMakatizenNumber(@Field("makatizen_number") String id);
}
