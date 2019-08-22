package com.makatizen.makahanap.ui.chat;

import com.makatizen.makahanap.pojo.ChatItem;
import com.makatizen.makahanap.ui.base.BaseMvpView;
import java.util.List;

public interface ChatMvpView extends BaseMvpView {

    void noContent();

    void noNetworkConnection();

    void onChatListRefresh(List<ChatItem> chatItemList);

    void setChatList(List<ChatItem> chatItemList, int currentAccountId);
}
