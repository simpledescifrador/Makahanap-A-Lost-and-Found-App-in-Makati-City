package com.makatizen.makahanap.ui.main.account.reports;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import com.makatizen.makahanap.pojo.FeedItem;
import java.util.List;

public class AccountReportsDiffCallback extends DiffUtil.Callback {

    private List<FeedItem> newFeedItems;

    private List<FeedItem> oldFeedItems;

    AccountReportsDiffCallback(final List<FeedItem> oldFeedItems,
            final List<FeedItem> newFeedItems) {
        this.oldFeedItems = oldFeedItems;
        this.newFeedItems = newFeedItems;
    }

    @Override
    public boolean areContentsTheSame(final int oldItemPosition, final int newItemPosition) {
        return oldFeedItems.get(oldItemPosition).getItemTitle()
                .equals(newFeedItems.get(newItemPosition).getItemTitle());
    }

    @Override
    public boolean areItemsTheSame(final int oldItemPosition, final int newItemPosition) {
        return oldFeedItems.get(oldItemPosition).getItemId() == (newFeedItems.get(newItemPosition).getItemId());
    }

    @Nullable
    @Override
    public Object getChangePayload(final int oldItemPosition, final int newItemPosition) {
        FeedItem newFeedItem = newFeedItems.get(newItemPosition);
        FeedItem oldFeedItem = oldFeedItems.get(oldItemPosition);

        Bundle diff = new Bundle();

        if (!newFeedItem.getItemTitle().equals(oldFeedItem.getItemTitle())) {
            diff.putString("item_title", newFeedItem.getItemTitle());
        }

        if (!newFeedItem.getItemLocation().equals(oldFeedItem.getItemLocation())) {
            diff.putString("item_location", newFeedItem.getItemLocation());
        }

        if (!newFeedItem.getItemDescription().equals(oldFeedItem.getItemDescription())) {
            diff.putString("item_description", newFeedItem.getItemDescription());
        }

        return diff;
    }

    @Override
    public int getNewListSize() {
        return newFeedItems.size();
    }

    @Override
    public int getOldListSize() {
        return oldFeedItems.size();
    }
}
