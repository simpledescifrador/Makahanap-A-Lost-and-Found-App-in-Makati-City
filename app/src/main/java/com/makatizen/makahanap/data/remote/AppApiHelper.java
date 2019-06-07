package com.makatizen.makahanap.data.remote;

import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.api_response.LoginResponse;
import com.makatizen.makahanap.pojo.api_response.MakatizenGetDataResponse;
import com.makatizen.makahanap.pojo.api_response.RegisterReponse;
import com.makatizen.makahanap.pojo.api_response.VerifyMakatizenIdResponse;
import io.reactivex.Single;
import javax.inject.Inject;

public class AppApiHelper implements ApiHelper {

    private ApiInterface mApiInterface;

    private MakatizenApiInterface mMakatizenApiInterface;

    @Inject
    public AppApiHelper(final ApiInterface apiInterface, final MakatizenApiInterface makatizenApiInterface) {
        mApiInterface = apiInterface;
        mMakatizenApiInterface = makatizenApiInterface;
    }


    @Override
    public Single<MakahanapAccount> getMakahanapAccountData(final int accountId) {
        return mApiInterface.getMakahanapAccountData(accountId);
    }

    @Override
    public Single<MakatizenGetDataResponse> getMakatizenData(final String makatizenId) {
        return mMakatizenApiInterface.getMakatizenData(makatizenId);
    }

    @Override
    public Single<RegisterReponse> registerNewAccount(final MakahanapAccount makahanapAccount) {
        return mApiInterface.registerNewAccount(makahanapAccount);
    }

    @Override
    public Single<VerifyMakatizenIdResponse> verifyMakatizenId(final String makatizenId) {
        return mMakatizenApiInterface.validateMakatizenNumber(makatizenId);
    }

    @Override
    public Single<LoginResponse> loginAppRequest(final String makatizenNumber, final String password) {
        return mApiInterface.loginAppRequest(makatizenNumber, password);
    }
}
