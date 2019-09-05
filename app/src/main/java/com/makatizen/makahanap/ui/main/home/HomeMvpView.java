package com.makatizen.makahanap.ui.main.home;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.utils.enums.Type;

public interface HomeMvpView extends BaseMvpView {
    void showOptionDialog(Type type);

    void openChatBox(int accountId);
}
