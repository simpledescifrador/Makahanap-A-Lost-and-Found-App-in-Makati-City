package com.makatizen.makahanap.ui.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.pojo.ChatItemV2;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.ui.base.BaseRecyclerViewAdapter;
import com.makatizen.makahanap.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatItemAdapter extends BaseRecyclerViewAdapter<ChatItemAdapter.ViewHolder, ChatItemV2> {

    static List<MakahanapAccount> senderAccounts = new ArrayList<>();

    private Context mContext;
    private int mCurrentAccountId;

    @Inject
    public ChatItemAdapter(Context mContext) {
        this.mContext = mContext;
        setData(new ArrayList<ChatItemV2>());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(
                LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chatv2, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        ChatItemV2 currentChatItem = getItem(i);
        MakahanapAccount senderAccount = new MakahanapAccount();
        for (int count = 0; count < 2; count++) {
            if (mCurrentAccountId != currentChatItem.getParticipants().get(count).getId()) {
                senderAccount = currentChatItem.getParticipants().get(count);
            }
        }
        senderAccounts.add(i, senderAccount);
        final MakahanapAccount finalAccount = senderAccount;
        RequestOptions requestOptions = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).circleCrop();
        switch (currentChatItem.getType()) {
            case SINGLE:
                if (currentChatItem.getStatus().equals("New")) {
                    //Set Account Image
                    Glide.with(mContext)
                            .load(ApiConstants.MAKATIZEN_API_BASE_URL + senderAccount.getProfileImageUrl())
                            .apply(requestOptions)
                            .into(viewHolder.chatAccountImage);
                    //Set Account Name
                    viewHolder.chatAccountName.setText(senderAccount.getFirstName() + " " + senderAccount.getLastName());
                    //Set Item id
                    viewHolder.chatItemId.setText("Item ID: " + currentChatItem.getItemId().toString());
                    //Set Item Title
                    viewHolder.chatItemTitle.setText(currentChatItem.getItemTitle() + " (item)");

                    if (currentChatItem.getLastestMessage().getCreatedAt() != null) {
                        viewHolder.chatTime.setVisibility(View.VISIBLE);
                        viewHolder.chatTime
                                .setText(DateUtils
                                        .DateToTimeFormat(currentChatItem.getLastestMessage().getCreatedAt()));
                    }


                }
                break;
            case GROUP:
                break;
        }
    }

    void setCurrentAccountId(int currentAccountId) {
        mCurrentAccountId = currentAccountId;
    }

    void updateChatList(List<ChatItemV2> chatItemList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ChatItemDiffCallback(getData(), chatItemList));
        diffResult.dispatchUpdatesTo(this);

        getData().clear();
        setDataNoNotify(chatItemList);
    }

    void updateChatRoom(String chatRoomId, String message, String messageTime) {
        for (int i = 0; i < getItemCount() - 1; i++) {
            if (chatRoomId.equals(getData().get(i).getId())) {
                getData().get(i).getLastestMessage().setCreatedAt(messageTime);
                getData().get(i).getLastestMessage().setMessage(message);
                getData().get(i).setViewed(true);
                notifyItemChanged(i);
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chat_account_image)
        ImageView chatAccountImage;
        @BindView(R.id.chat_account_name)
        TextView chatAccountName;
        @BindView(R.id.chat_item_title)
        TextView chatItemTitle;
        @BindView(R.id.chat_item_id)
        TextView chatItemId;
        @BindView(R.id.chat_time)
        TextView chatTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
