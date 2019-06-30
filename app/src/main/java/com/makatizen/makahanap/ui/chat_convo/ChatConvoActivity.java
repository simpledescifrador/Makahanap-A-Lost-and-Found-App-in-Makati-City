package com.makatizen.makahanap.ui.chat_convo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.text.emoji.widget.EmojiEditText;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.pojo.ChatMessage;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.List;
import javax.inject.Inject;
import org.apache.commons.lang3.StringEscapeUtils;

public class ChatConvoActivity extends BaseActivity implements ChatConvoMvpView {

    @Inject
    ChatConvoAdapter mChatConvoAdapter;

    @BindView(R.id.convo_et_message_text)
    EmojiEditText mConvoEtMessageText;

    @BindView(R.id.convo_iv_account_image)
    CircleImageView mConvoIvAccountImage;

    @BindView(R.id.convo_iv_image_gallery)
    ImageView mConvoIvImageGallery;

    @BindView(R.id.convo_iv_like)
    ImageView mConvoIvLike;

    @BindView(R.id.convo_iv_options)
    ImageView mConvoIvOptions;

    @BindView(R.id.convo_iv_send)
    ImageView mConvoIvSend;

    @BindView(R.id.convo_loading_layout)
    FrameLayout mConvoLoadingLayout;

    @BindView(R.id.convo_no_messages_layout)
    FrameLayout mConvoNoMessagesLayout;

    @BindView(R.id.convo_rv_messages)
    RecyclerView mConvoRvMessages;

    @Inject
    ChatConvoMvpPresenter<ChatConvoMvpView> mPresenter;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getExtras() != null) {
                String messageId = intent.getStringExtra("message_id");
                String senderId = intent.getStringExtra("sender_id");
                String messageTime = intent.getStringExtra("message_time");
                String message = intent.getStringExtra("message");
                String chatRoomId = intent.getStringExtra("chat_room_id");
                mConvoRvMessages.smoothScrollToPosition(mChatConvoAdapter.getItemCount());
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setAccountId(senderId);
                chatMessage.setChatRoomId(chatRoomId);
                chatMessage.setCreatedAt(messageTime);
                chatMessage.setId(messageId);
                chatMessage.setMessage(message);
                mChatConvoAdapter.insert(chatMessage, mChatConvoAdapter.getItemCount());
            }
        }
    };

    private int mChatId;

    private MakahanapAccount mSenderAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);

        getSupportActionBar().hide(); //Hide when chat messages loads
        setContentView(R.layout.activity_chat_convo);
        setUnBinder(ButterKnife.bind(this));

        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mBroadcastReceiver, new IntentFilter("com.makatizen.makahanap.messages"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void onMessageSentSuccess(final String messageTime, final int position) {
        mChatConvoAdapter.messageSent(messageTime, position);
        mConvoRvMessages.smoothScrollToPosition(position);
    }

    @Override
    public void onNewConvo() {
        Glide.with(this)
                .load(ApiConstants.MAKATIZEN_API_BASE_URL + mSenderAccount.getProfileImageUrl())
                .into(mConvoIvAccountImage);
        getSupportActionBar().show();
        mConvoLoadingLayout.setVisibility(View.GONE);
        mConvoNoMessagesLayout.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.convo_iv_options, R.id.convo_iv_image_gallery, R.id.convo_iv_like, R.id.convo_iv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.convo_iv_options:
                break;
            case R.id.convo_iv_image_gallery:
                break;
            case R.id.convo_iv_like:
                Toast.makeText(this, "Sending Like", Toast.LENGTH_SHORT).show();
                break;
            case R.id.convo_iv_send:
                mConvoNoMessagesLayout.setVisibility(View.GONE);
                ChatMessage chatMessage = new ChatMessage();
                String messageText = mConvoEtMessageText.getText().toString().trim();

                chatMessage.setAccountId(String.valueOf(mPresenter.getAccountId()));
                chatMessage.setChatRoomId(String.valueOf(mChatId));
                chatMessage.setMessage(StringEscapeUtils.escapeJava(messageText));
                chatMessage.setSending(true);

                mChatConvoAdapter.insert(chatMessage, mChatConvoAdapter.getItemCount());
                int position = mChatConvoAdapter.getItemCount() - 1;
                mConvoRvMessages.smoothScrollToPosition(position);
                mConvoEtMessageText.setText("");
                //Send Message to Server
                mPresenter.sendMessage(mChatId, chatMessage.getMessage(), position);
                break;
        }
    }

    @Override
    public void setChatMessages(final List<ChatMessage> chatMessageList) {
        mConvoLoadingLayout.setVisibility(View.GONE);
        getSupportActionBar().show();
        mChatConvoAdapter.setData(chatMessageList);
        mConvoRvMessages.smoothScrollToPosition(mChatConvoAdapter.getItemCount());
    }

    @Override
    protected void init() {
        showBackButton(true);
        mConvoRvMessages.setAdapter(mChatConvoAdapter);
        mConvoRvMessages.setLayoutManager(new LinearLayoutManager(this));
        mSenderAccount = getIntent().getParcelableExtra(IntentExtraKeys.ACCOUNT);

        //set sender name to title
        setTitle(mSenderAccount.getFirstName() + " " + mSenderAccount.getLastName());

        mChatConvoAdapter
                .setSenderAccount(mSenderAccount);
        mChatId = Integer.parseInt(getIntent().getStringExtra(IntentExtraKeys.CHAT_ID));
        mPresenter.loadChatMessages(mChatId); //Load Chat Messages

        mConvoEtMessageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(final Editable editable) {

            }

            @Override
            public void beforeTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {

            }

            @Override
            public void onTextChanged(final CharSequence charSequence, final int i, final int i1, final int i2) {
                if (charSequence.length() > 0) {
                    mConvoIvSend.setVisibility(View.VISIBLE);
                    mConvoIvLike.setVisibility(View.GONE);

                } else {
                    mConvoIvLike.setVisibility(View.VISIBLE);
                    mConvoIvSend.setVisibility(View.GONE);
                }
            }
        });
    }
}
