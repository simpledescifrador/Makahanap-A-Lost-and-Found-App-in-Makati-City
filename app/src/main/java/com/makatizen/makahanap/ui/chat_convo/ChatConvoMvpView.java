package com.makatizen.makahanap.ui.chat_convo;

import com.makatizen.makahanap.pojo.ChatMessage;
import com.makatizen.makahanap.ui.base.BaseMvpView;
import java.util.List;

public interface ChatConvoMvpView extends BaseMvpView {

    void onNewConvo();

    void setChatMessages(List<ChatMessage> chatMessageList);

    void onMessageSentSuccess(String messageTime, int position);
}
