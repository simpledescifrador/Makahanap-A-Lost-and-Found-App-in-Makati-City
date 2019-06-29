package com.makatizen.makahanap.ui.chat;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface ChatMvpPresenter<V extends ChatMvpView & BaseMvpView> extends Presenter<V> {

    void loadChatList();

    void updateChatList();
}
