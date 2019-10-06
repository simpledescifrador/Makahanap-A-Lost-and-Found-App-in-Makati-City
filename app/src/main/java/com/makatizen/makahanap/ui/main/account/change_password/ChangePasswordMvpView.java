package com.makatizen.makahanap.ui.main.account.change_password;

import com.makatizen.makahanap.ui.base.BaseMvpView;

public interface ChangePasswordMvpView extends BaseMvpView {
    void setPasswordError(String error);

    void setConfirmPasswordError(String error);

    void onSuccessChangePassword();
}
