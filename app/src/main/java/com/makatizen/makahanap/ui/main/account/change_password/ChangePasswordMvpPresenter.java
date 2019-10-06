package com.makatizen.makahanap.ui.main.account.change_password;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface ChangePasswordMvpPresenter<V extends ChangePasswordMvpView & BaseMvpView> extends Presenter<V> {
    boolean validateNewPassword(String newPassword);

    boolean confirmPassword(String newPassword, String confirmPassword);

    void changePassword(String newPassword, String oldPassword);
}
