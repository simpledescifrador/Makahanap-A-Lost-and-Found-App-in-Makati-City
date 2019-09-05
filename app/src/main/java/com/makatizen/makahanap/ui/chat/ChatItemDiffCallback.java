package com.makatizen.makahanap.ui.chat;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.makatizen.makahanap.pojo.ChatItemV2;

import java.util.List;

public class ChatItemDiffCallback extends DiffUtil.Callback {

    private List<ChatItemV2> newChatItems;

    private List<ChatItemV2> oldChatItems;

    public ChatItemDiffCallback(List<ChatItemV2> oldChatItems, List<ChatItemV2> newChatItems) {
        this.newChatItems = newChatItems;
        this.oldChatItems = oldChatItems;
    }

    @Override
    public int getOldListSize() {
        return oldChatItems.size();
    }

    @Override
    public int getNewListSize() {
        return newChatItems.size();
    }

    @Override
    public boolean areItemsTheSame(final int oldItemPosition, final int newItemPosition) {
        return oldChatItems.get(oldItemPosition).getId() == newChatItems.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(final int oldItemPosition, final int newItemPosition) {
        return oldChatItems.get(oldItemPosition).getItemId() == newChatItems.get(newItemPosition).getItemId();
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
