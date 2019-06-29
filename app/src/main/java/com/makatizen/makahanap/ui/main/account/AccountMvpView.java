package com.makatizen.makahanap.ui.main.account;

import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.ui.base.BaseMvpView;

public interface AccountMvpView extends BaseMvpView {

    void setAccountData(MakahanapAccount makahanapAccount);

    void onSuccessLogout();

    void openChat(int accountId);

}
