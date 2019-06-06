package com.makatizen.makahanap.ui.register;

import com.makatizen.makahanap.pojo.MakatizenAccount;
import com.makatizen.makahanap.ui.base.BaseMvpView;

public interface RegisterMvpView extends BaseMvpView {

    void invalidMakatizenId();

    void makatizenDataSuccess(MakatizenAccount makatizenAccount);

    void onRegisterSuccessful(int accountId);

    void resetButtonText();

    void setConfirmPasswordError(int stringId);

    void setPasswordError(int stringId);

    void validMakatizenId();
}
