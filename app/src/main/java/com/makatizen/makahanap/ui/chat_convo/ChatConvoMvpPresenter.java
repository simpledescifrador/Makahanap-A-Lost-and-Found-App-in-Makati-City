package com.makatizen.makahanap.ui.chat_convo;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface ChatConvoMvpPresenter<V extends ChatConvoMvpView & BaseMvpView> extends Presenter<V> {

    void loadChatMessages(int chatId);

    void sendMessage(int chatId, String message, int position);

    int getAccountId();
}
