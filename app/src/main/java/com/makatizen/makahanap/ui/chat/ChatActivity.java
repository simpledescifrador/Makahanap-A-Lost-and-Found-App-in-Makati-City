package com.makatizen.makahanap.ui.chat;

import android.os.Bundle;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import com.airbnb.lottie.LottieAnimationView;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.pojo.ChatItem;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.utils.RecyclerItemUtils.OnItemClickListener;
import java.util.List;
import javax.inject.Inject;

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
        // TODO: 6/27/19 On Chat Item Clicked
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
