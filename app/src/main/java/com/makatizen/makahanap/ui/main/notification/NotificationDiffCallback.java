package com.makatizen.makahanap.ui.main.notification;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import com.makatizen.makahanap.pojo.Notification;
import java.util.List;

public class NotificationDiffCallback extends DiffUtil.Callback {

    private List<Notification> newNotifications;

    private List<Notification> oldNotifications;

    NotificationDiffCallback(final List<Notification> oldNotifications,
            final List<Notification> newNotifications) {
        this.newNotifications = newNotifications;
        this.oldNotifications = oldNotifications;
    }

    @Override
    public boolean areContentsTheSame(final int oldItemPosition, final int newItemPosition) {
        return oldNotifications.get(oldItemPosition).getContent()
                .equals(newNotifications.get(newItemPosition).getContent());
    }

    @Override
    public boolean areItemsTheSame(final int oldItemPosition, final int newItemPosition) {
        return oldNotifications.get(oldItemPosition).getId() == newNotifications.get(newItemPosition).getId();
    }

    @Nullable
    @Override
    public Object getChangePayload(final int oldItemPosition, final int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }

    @Override
    public int getNewListSize() {
        return newNotifications.size();
    }

    @Override
    public int getOldListSize() {
        return oldNotifications.size();
    }
}
