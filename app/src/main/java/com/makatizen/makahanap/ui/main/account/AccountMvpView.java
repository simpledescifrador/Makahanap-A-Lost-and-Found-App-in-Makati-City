package com.makatizen.makahanap.ui.main.account;

import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.ui.base.BaseMvpView;

public interface AccountMvpView extends BaseMvpView {

    void onSuccessLogout();

    void openChat(int accountId);

    void setAccountData(MakahanapAccount makahanapAccount);

    void setFoundCount(int count);

    void setLostCount(int count);

    void setReturnedCount(int count);

}
