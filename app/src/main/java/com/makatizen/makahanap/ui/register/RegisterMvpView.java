package com.makatizen.makahanap.ui.register;

import com.makatizen.makahanap.pojo.MakatizenAccount;
import com.makatizen.makahanap.ui.base.BaseMvpView;

public interface RegisterMvpView extends BaseMvpView {

    void invalidMakatizenId();

    void makatizenDataSuccess(MakatizenAccount makatizenAccount);

    void onRegisterSuccessful(int accountId);

    void removeErrors();

    void resetButtonText();

    void setConfirmPasswordError(int stringId);

    void setPasswordError(int stringId);

    void validMakatizenId();

    void showPassword();

    void hidePassword();

    void showVerificationOption(String email, String contactNumber);

    void successfulEmailVerificationRequest(String email);

    void successfulSmsVerificationRequest(String requestId, String number);
}
