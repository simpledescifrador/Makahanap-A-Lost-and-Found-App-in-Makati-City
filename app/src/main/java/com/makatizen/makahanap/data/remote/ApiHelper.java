package com.makatizen.makahanap.data.remote;

import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.api_response.MakatizenGetDataResponse;
import com.makatizen.makahanap.pojo.api_response.RegisterReponse;
import com.makatizen.makahanap.pojo.api_response.VerifyMakatizenIdResponse;
import io.reactivex.Single;

public interface ApiHelper {

    Single<MakatizenGetDataResponse> getMakatizenData(String makatizenId);

    Single<RegisterReponse> registerNewAccount(MakahanapAccount makahanapAccount);

    Single<VerifyMakatizenIdResponse> verifyMakatizenId(String makatizenId);
}
