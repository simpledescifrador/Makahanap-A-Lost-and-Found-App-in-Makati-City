package com.makatizen.makahanap.ui.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import com.makatizen.makahanap.pojo.ChatItem;
import java.util.List;

public class ChatDiffCallback extends DiffUtil.Callback {

    private List<ChatItem> newChatItems;

    private List<ChatItem> oldChatItems;

    public ChatDiffCallback(final List<ChatItem> oldChatItems, final List<ChatItem> newChatItems) {
        this.newChatItems = newChatItems;
        this.oldChatItems = oldChatItems;
    }

    @Override
    public boolean areContentsTheSame(final int oldItemPosition, final int newItemPosition) {
        return oldChatItems.get(oldItemPosition).getLastestMessage().getMessage()
                .equals(newChatItems.get(newItemPosition).getLastestMessage().getMessage());
    }

    @Override
    public boolean areItemsTheSame(final int oldItemPosition, final int newItemPosition) {
        return oldChatItems.get(oldItemPosition).getId() == newChatItems.get(newItemPosition).getId();
    }

    @Nullable
    @Override
    public Object getChangePayload(final int oldItemPosition, final int newItemPosition) {
        ChatItem newChatItem = newChatItems.get(newItemPosition);
        ChatItem oldChatItem = oldChatItems.get(oldItemPosition);

        Bundle diff = new Bundle();

        if (!newChatItem.getLastestMessage().getMessage().equals(oldChatItem.getLastestMessage().getMessage())) {
            diff.putString("chat_message", newChatItem.getLastestMessage().getMessage());
        }

        if (!newChatItem.getLastestMessage().getCreatedAt().equals(oldChatItem.getLastestMessage().getCreatedAt())) {
            diff.putString("chat_message_created_at", newChatItem.getLastestMessage().getCreatedAt());
        }

        return diff;
    }

    @Override
    public int getNewListSize() {
        return newChatItems.size();
    }

    @Override
    public int getOldListSize() {
        return oldChatItems.size();
    }
}
