package com.makatizen.makahanap.ui.chat;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.text.emoji.widget.EmojiTextView;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.pojo.ChatItem;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.ui.base.BaseRecyclerViewAdapter;
import com.makatizen.makahanap.ui.chat.ChatAdapter.ViewHolder;
import com.makatizen.makahanap.utils.DateUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.StringEscapeUtils;

public class ChatAdapter extends BaseRecyclerViewAdapter<ViewHolder, ChatItem> {

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.chat_etv_latest_message)
        EmojiTextView mChatEtvLatestMessage;

        @BindView(R.id.chat_item_layout)
        ConstraintLayout mChatItemLayout;

        @BindView(R.id.chat_iv_account_image)
        ImageView mChatIvAccountImage;

        @BindView(R.id.chat_tv_account_name)
        TextView mChatTvAccountName;

        @BindView(R.id.chat_tv_message_time)
        TextView mChatTvMessageTime;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private Context mContext;

    private int mCurrentAccountId;

    public ChatAdapter(final Context context) {
        mContext = context;
        setData(new ArrayList<ChatItem>());
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position,
            @NonNull final List<Object> payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else {
            Bundle o = (Bundle) payloads.get(0);
            for (String key : o.keySet()) {
                if (key.equals("chat_message")) {
                    holder.mChatEtvLatestMessage.setText(o.getString(key));
                }
                if (key.equals("chat_message_created_at")) {
                    holder.mChatTvMessageTime.setText(o.getString(key));
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        ChatItem currentChatItem = getItem(i);

        if (currentChatItem.isViewed()) {
            viewHolder.mChatEtvLatestMessage.setTypeface(null, Typeface.BOLD);
        } else {
            viewHolder.mChatEtvLatestMessage.setTypeface(null, Typeface.NORMAL);
        }

        MakahanapAccount senderAccount = new MakahanapAccount();
        for (int count = 0; count < 2; count++) {
            if (mCurrentAccountId != currentChatItem.getParticipants().get(count).getId()) {
                senderAccount = currentChatItem.getParticipants().get(count);
            }
        }
        final MakahanapAccount finalAccount = senderAccount;
        RequestOptions requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL);
        switch (currentChatItem.getType()) {
            case SINGLE:
                if (currentChatItem.getStatus().equals("New")) {
                    Glide.with(mContext)
                            .load(ApiConstants.MAKATIZEN_API_BASE_URL + senderAccount.getProfileImageUrl())
                            .apply(requestOptions)
                            .into(viewHolder.mChatIvAccountImage);
                    viewHolder.mChatTvAccountName
                            .setText(senderAccount.getFirstName() + " " + senderAccount.getLastName());
                    String decodedMessage = StringEscapeUtils
                            .unescapeJava(currentChatItem.getLastestMessage().getMessage());

                    if (currentChatItem.getLastestMessage().getMessage() != null) {
                        if (!senderAccount.getId()
                                .equals(Integer.parseInt(currentChatItem.getLastestMessage().getAccountId()))) {
                            viewHolder.mChatEtvLatestMessage.setText("You :" + decodedMessage);
                        } else {
                            viewHolder.mChatEtvLatestMessage.setText(decodedMessage);
                        }
                        viewHolder.mChatTvMessageTime
                                .setText(DateUtils
                                        .DateToTimeFormat(currentChatItem.getLastestMessage().getCreatedAt()));

                    } else {
                        viewHolder.mChatEtvLatestMessage.setText("No Conversation yet!");
                    }
                } else if (currentChatItem.getStatus().equals("Transaction")) {

                }
                break;
            case GROUP:
                break;
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int i) {
        return new ViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat, viewGroup, false));
    }

    void setCurrentAccountId(int currentAccountId) {
        mCurrentAccountId = currentAccountId;
    }

    void updateChatList(List<ChatItem> chatItemList) {
        DiffUtil.DiffResult diffResult = DiffUtil
                .calculateDiff(new ChatDiffCallback(getData(), chatItemList));
        diffResult.dispatchUpdatesTo(this);

        getData().clear();
        setDataNoNotify(chatItemList);
    }
}
