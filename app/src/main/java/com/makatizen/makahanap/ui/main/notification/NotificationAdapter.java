package com.makatizen.makahanap.ui.main.notification;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.pojo.Notification;
import com.makatizen.makahanap.ui.base.BaseRecyclerViewAdapter;
import com.makatizen.makahanap.ui.main.notification.NotificationAdapter.ViewHolder;
import com.makatizen.makahanap.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class NotificationAdapter extends BaseRecyclerViewAdapter<ViewHolder, Notification> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.notification_created_at)
        TextView mNotificationCreatedAt;

        @BindView(R.id.notification_image)
        ImageView mNotificationImage;

        @BindView(R.id.notification_item_layout)
        ConstraintLayout mNotificationItemLayout;

        @BindView(R.id.notification_option_btn)
        ImageView mNotificationOptionBtn;

        @BindView(R.id.notification_title)
        TextView mNotificationTitle;

        @BindView(R.id.notification_type)
        TextView mNotificationType;

        @BindView(R.id.notification_unread)
        TextView mNotificationUnread;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnNotificationOptionListener {

        void onOptionDelete(int id, int position);

        void onOptionMarkAsUnViewed(int id, int position);

        void onOptionMarkAsViewed(int id, int position);
    }

    private Context mContext;

    private OnNotificationOptionListener mOptionListener;

    @Inject
    public NotificationAdapter(final Context context) {
        mContext = context;
        setData(new ArrayList<Notification>());
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        final Notification notification = getItem(position);

        if (notification.getTitle().isEmpty() || notification.getContent().isEmpty() || notification.getCreatedAt()
                .isEmpty()) {
            //TODO if the notification is empty do something here
        } else {

            if (!notification.isViewed()) {
                viewHolder.mNotificationUnread.setVisibility(View.VISIBLE);
            }

            /*Set Notification Title*/
            viewHolder.mNotificationTitle.setText(
                    String.format("%s: %s", notification.getTitle(), notification.getContent()));
            /* Set Notification Type */
            viewHolder.mNotificationType.setText(notification.getType());
            /*Set Notification Date created*/
            viewHolder.mNotificationCreatedAt.setText(DateUtils.DateToTimeFormat(notification.getCreatedAt()));
            /*Set Notification Image*/
            Glide.with(mContext)
                    .load(notification.getImageUrl()) /*Get Item URL*/
                    .apply(new RequestOptions().override(75, 50)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(viewHolder.mNotificationImage);

            /* Add Notification Popup menu */
            final PopupMenu popupMenu = new PopupMenu(mContext, viewHolder.mNotificationOptionBtn, Gravity.RIGHT);
            popupMenu.getMenuInflater().inflate(R.menu.menu_notification, popupMenu.getMenu());
            viewHolder.mNotificationOptionBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(final View view) {
                    popupMenu.show();
                }
            });

            if (notification.isViewed()) {
                popupMenu.getMenu().findItem(R.id.notification_menu_mark_viewed).setVisible(false);
            }
            if (!notification.isViewed()) {
                popupMenu.getMenu().findItem(R.id.notification_menu_mark_unviewed).setVisible(false);
            }

            popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.notification_menu_mark_viewed:
                            mOptionListener.onOptionMarkAsViewed(notification.getId(), viewHolder.getAdapterPosition());
                            break;
                        case R.id.notification_menu_mark_unviewed:
                            mOptionListener.onOptionMarkAsUnViewed(notification.getId(), viewHolder.getAdapterPosition());
                            break;
                        case R.id.notification_menu_delete:
                            mOptionListener.onOptionDelete(notification.getId(), viewHolder.getAdapterPosition());
                            break;
                    }
                    return true;
                }
            });
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        return new ViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification, viewGroup, false));
    }

    void updateNotifications(List<Notification> updatedNotifications) {
        DiffUtil.DiffResult diffResult = DiffUtil
                .calculateDiff(new NotificationDiffCallback(getData(), updatedNotifications));
        diffResult.dispatchUpdatesTo(this);

        getData().clear();
        setDataNoNotify(updatedNotifications);
    }

    void setNotificationOptionListener(OnNotificationOptionListener listener) {
        this.mOptionListener = listener;
    }

    void setNotificationUnViewed(int position) {
        getData().get(position).setViewed(false);
        notifyItemChanged(position);
    }

    void setNotificationViewed(int position) {
        getData().get(position).setViewed(true);
        notifyItemChanged(position);
    }

}
