package com.makatizen.makahanap.ui.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.pojo.ChatItem;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.ui.chat_convo.ChatConvoActivity;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import com.makatizen.makahanap.utils.RecyclerItemUtils;
import com.makatizen.makahanap.utils.RecyclerItemUtils.OnItemClickListener;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends BaseActivity implements ChatMvpView, OnItemClickListener {

    @Inject
    ChatAdapter mChatAdapter;

    @BindView(R.id.chat_rv_list)
    RecyclerView mChatRvList;

    @BindView(R.id.chat_srl_refresh)
    SwipeRefreshLayout mChatSrlRefresh;

    @BindView(R.id.no_content_animation)
    LottieAnimationView mNoContentAnimation;

    @BindView(R.id.no_content_btn_report)
    Button mNoContentBtnReport;

    @BindView(R.id.no_content_iv_icon)
    ImageView mNoContentIvIcon;

    @BindView(R.id.no_content_layout)
    LinearLayout mNoContentLayout;

    @BindView(R.id.no_content_message)
    TextView mNoContentMessage;

    @BindView(R.id.no_content_title)
    TextView mNoContentTitle;

    @BindView(R.id.no_internet_animation)
    LottieAnimationView mNoInternetAnimation;

    @BindView(R.id.no_internet_btn_retry)
    Button mNoInternetBtnRetry;

    @BindView(R.id.no_internet_layout)
    LinearLayout mNoInternetLayout;

    @BindView(R.id.no_internet_message)
    TextView mNoInternetMessage;

    @BindView(R.id.no_internet_title)
    TextView mNoInternetTitle;

    @Inject
    ChatMvpPresenter<ChatMvpView> mPresenter;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            if (intent.getExtras() != null) {
                String messageId = intent.getStringExtra("message_id");
                String senderId = intent.getStringExtra("sender_id");
                String messageTime = intent.getStringExtra("message_time");
                String message = intent.getStringExtra("message");
                String chatRoomId = intent.getStringExtra("chat_room_id");
                mChatRvList.smoothScrollToPosition(0);
                mChatAdapter.updateChatRoom(chatRoomId, message, messageTime);
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivityFragmentComponent().inject(this);
        mPresenter.attachView(this);

        setContentView(R.layout.activity_chat);
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
    public void noContent() {
        mChatSrlRefresh.setRefreshing(false);
        mChatRvList.setVisibility(View.GONE);
        mNoContentLayout.setVisibility(View.VISIBLE);

        mNoContentTitle.setTextSize(18);
        mNoContentIvIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_no_message));
        mNoContentIvIcon.setVisibility(View.VISIBLE);
        mNoContentAnimation.setVisibility(View.GONE);
        mNoContentTitle.setText(getResources().getString(R.string.title_no_chat_content));
        mNoContentMessage.setText(getResources().getString(R.string.message_no_chat_content));
        mNoContentBtnReport.setVisibility(View.GONE);
    }

    @Override
    public void noNetworkConnection() {
        mNoInternetLayout.setVisibility(View.VISIBLE);
        mChatRvList.setVisibility(View.GONE);
        mChatSrlRefresh.setRefreshing(false);

        mNoInternetTitle.setTextSize(18);
        mNoInternetTitle.setText(getResources().getString(R.string.title_no_network));
        mNoInternetMessage.setText(getResources().getString(R.string.message_no_network));
        mNoInternetBtnRetry.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View view) {
                mNoInternetLayout.setVisibility(View.GONE);
                mChatSrlRefresh.setRefreshing(true);
                mPresenter.loadChatList();
            }
        });
    }

    @Override
    public void onChatListRefresh(final List<ChatItem> chatItemList) {
        mChatSrlRefresh.setRefreshing(false);

        mChatAdapter.updateChatList(chatItemList);
        mChatAdapter.sort(ChatItem.byLatestMessageDate);
    }

    @Override
    public void onItemClicked(final RecyclerView recyclerView, final int position, final View v) {
        ChatItem chatItem = mChatAdapter.getItem(position);
        Intent intent = new Intent(this, ChatConvoActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(IntentExtraKeys.SELECTED_POSITION, position);
        intent.putExtra(IntentExtraKeys.ACCOUNT, ChatAdapter.senderAccounts.get(position));
        intent.putExtra(IntentExtraKeys.CHAT_ID, chatItem.getId());
        startActivity(intent);
    }

    @Override
    public void setChatList(final List<ChatItem> chatItemList, final int currentAccountId) {
        mChatSrlRefresh.setRefreshing(false);
        mChatRvList.setVisibility(View.VISIBLE);

        mChatAdapter.setCurrentAccountId(currentAccountId);
        mChatAdapter.setData(chatItemList);
        mChatAdapter.sort(ChatItem.byLatestMessageDate);
    }

    @Override
    protected void init() {
        mChatRvList.setAdapter(mChatAdapter);
        mChatRvList.setLayoutManager(new LinearLayoutManager(this));
        RecyclerItemUtils.addTo(mChatRvList).setOnItemClickListener(this);

        mChatSrlRefresh.setRefreshing(true);
        mPresenter.loadChatList();

        mChatSrlRefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(ChatActivity.this, "Updating", Toast.LENGTH_SHORT).show();
                mPresenter.updateChatList();
            }
        });
    }
}
