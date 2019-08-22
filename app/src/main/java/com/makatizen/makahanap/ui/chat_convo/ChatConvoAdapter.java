package com.makatizen.makahanap.ui.chat_convo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.text.emoji.widget.EmojiTextView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.pojo.ChatMessage;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.ui.base.BaseRecyclerViewAdapter;
import com.makatizen.makahanap.utils.DateUtils;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import javax.inject.Inject;
import org.apache.commons.lang3.StringEscapeUtils;

public class ChatConvoAdapter extends BaseRecyclerViewAdapter<ViewHolder, ChatMessage> {

    class MyMessageViewHolder extends ViewHolder {

        @BindView(R.id.my_convo_created_at)
        TextView mMyConvoCreatedAt;

        @BindView(R.id.my_convo_message_body)
        EmojiTextView mMyConvoMessageBody;

        @BindView(R.id.my_convo_send_failed)
        TextView mMyConvoSendFailed;

        @BindView(R.id.my_convo_sending)
        TextView mMyConvoSending;

        public MyMessageViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class OtherMessageViewHolder extends ViewHolder {

        @BindView(R.id.other_convo_account_image)
        CircleImageView mOtherConvoAccountImage;

        @BindView(R.id.other_convo_created_at)
        TextView mOtherConvoCreatedAt;

        @BindView(R.id.other_convo_message_body)
        EmojiTextView mOtherConvoMessageBody;

        public OtherMessageViewHolder(@NonNull final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private final int MY_MESSAGE = 1;

    private final int OTHER_MESSAGE = 2;

    private Context mContext;

    private MakahanapAccount mSenderAccount = new MakahanapAccount();

    @Inject
    public ChatConvoAdapter(final Context context) {
        mContext = context;
        setData(new ArrayList<ChatMessage>());
    }

    @Override
    public int getItemViewType(final int position) {
        if (mSenderAccount.getId() != Integer.parseInt(getItem(position).getAccountId())) {
            return MY_MESSAGE;
        } else {
            return OTHER_MESSAGE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int position) {
        ChatMessage chatMessage = getItem(position);
        String decodedMessage = StringEscapeUtils.unescapeJava(chatMessage.getMessage());

        switch (viewHolder.getItemViewType()) {
            case MY_MESSAGE:
                final MyMessageViewHolder myMessageVh = (MyMessageViewHolder) viewHolder;
                myMessageVh.mMyConvoMessageBody.setText(decodedMessage);
                myMessageVh.mMyConvoMessageBody.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        if (myMessageVh.mMyConvoCreatedAt.getVisibility() == View.VISIBLE
                                && myMessageVh.mMyConvoCreatedAt != null) {
                            myMessageVh.mMyConvoCreatedAt.setVisibility(View.GONE);
                        } else {
                            myMessageVh.mMyConvoCreatedAt.setVisibility(View.VISIBLE);
                        }
                    }
                });

                if (chatMessage.getSent() != null && chatMessage.getSent()) {
                    myMessageVh.mMyConvoSending.setVisibility(View.GONE);
                    myMessageVh.mMyConvoCreatedAt.setVisibility(View.VISIBLE);
                    myMessageVh.mMyConvoCreatedAt.setText(DateUtils.DateToTimeFormat(chatMessage.getCreatedAt()));
                }

                if (chatMessage.getSending() != null && chatMessage.getSending()) {
                    myMessageVh.mMyConvoSending.setVisibility(View.VISIBLE);
                } else {
                    myMessageVh.mMyConvoCreatedAt.setText(DateUtils.DateToTimeFormat(chatMessage.getCreatedAt()));
                    myMessageVh.mMyConvoSending.setVisibility(View.GONE);
                }
                break;
            case OTHER_MESSAGE:
                final OtherMessageViewHolder otherMessageVh = (OtherMessageViewHolder) viewHolder;
                otherMessageVh.mOtherConvoMessageBody.setText(decodedMessage);
                otherMessageVh.mOtherConvoCreatedAt.setText(DateUtils.DateToTimeFormat(chatMessage.getCreatedAt()));
                otherMessageVh.mOtherConvoMessageBody.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        if (otherMessageVh.mOtherConvoCreatedAt.getVisibility() == View.VISIBLE
                                && otherMessageVh.mOtherConvoCreatedAt != null) {
                            otherMessageVh.mOtherConvoCreatedAt.setVisibility(View.GONE);
                        } else {
                            otherMessageVh.mOtherConvoCreatedAt.setVisibility(View.VISIBLE);
                        }
                    }
                });

                Glide.with(mContext)
                        .load(ApiConstants.MAKATIZEN_API_BASE_URL + mSenderAccount.getProfileImageUrl())
                        .apply(new RequestOptions().diskCacheStrategy(
                                DiskCacheStrategy.ALL).override(50, 50))
                        .into(otherMessageVh.mOtherConvoAccountImage);

                break;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, final int viewType) {
        switch (viewType) {
            case MY_MESSAGE:
                return new MyMessageViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_my_convo, viewGroup, false));
            case OTHER_MESSAGE:
                return new OtherMessageViewHolder(LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.item_other_convo, viewGroup, false));
        }
        return null;
    }


    void messageSent(String messageTime, int position) {
        getData().get(position).setSent(true);
        getData().get(position).setSending(false);
        getData().get(position).setCreatedAt(messageTime);
        getItemViewType(position);
        notifyItemChanged(position);
    }

    void setSenderAccount(MakahanapAccount account) {
        mSenderAccount = account;
    }
}
