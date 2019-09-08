package com.makatizen.makahanap.ui.chat_convo;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.text.emoji.widget.EmojiEditText;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.remote.ApiConstants;
import com.makatizen.makahanap.pojo.ChatMessage;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.ui.base.BaseActivity;
import com.makatizen.makahanap.ui.item_details.ItemDetailsActivity;
import com.makatizen.makahanap.ui.transaction.MeetTransactionActivity;
import com.makatizen.makahanap.utils.DateUtils;
import com.makatizen.makahanap.utils.IntentExtraKeys;
import com.makatizen.makahanap.utils.RequestCodes;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

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
    @BindView(R.id.convo_transaction_return)
    Button mConvoTransactionReturn;
    @BindView(R.id.convo_transaction_meetup)
    Button mConvoTransactionMeetup;
    @BindView(R.id.convo_transaction_layout)
    LinearLayout mConvoTransactionLayout;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.textView16)
    TextView textView16;
    @BindView(R.id.meeting_details_label)
    TextView mMeetingDetailsLabel;
    @BindView(R.id.toggle_meeting_details)
    ImageView mToggleMeetingDetails;
    @BindView(R.id.meeting_location)
    TextView mMeetingLocation;
    @BindView(R.id.meeting_date)
    TextView mMeetingDate;
    @BindView(R.id.meetup_details_layout)
    LinearLayout mMeetupDetailsLayout;
    @BindView(R.id.transaction_confirmed_info)
    CardView mTransactionConfirmedInfo;
    @BindView(R.id.meet_transaction_message)
    TextView mMeetTransactionMessage;
    @BindView(R.id.meeting_transaction_buttons)
    LinearLayout mMeetingTransactionButtons;
    @BindView(R.id.meeting_done_btn)
    Button mMeetingDoneBtn;
    @BindView(R.id.meeting_failed_btn)
    Button mMeetingFailedBtn;
    @BindView(R.id.tails)
    TextView tails;
    @BindView(R.id.textView34)
    TextView textView34;
    @BindView(R.id.textView33)
    TextView textView33;
    @BindView(R.id.textVi321ew33)
    TextView textVi321ew33;
    @BindView(R.id.item_details_btn)
    Button mItemDetailsBtn;
    @BindView(R.id.item_return_info)
    CardView mItemReturnInfo;

    private int mItemId;
    private boolean mShowMeetingDetails = false;

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
    public void showTransactionButtons() {
        mConvoTransactionLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showConfirmDialog(String title, String location, String date, String imageUrl) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Confirm");
        dialogBuilder.setCancelable(true);
        dialogBuilder.setPositiveButton("Agree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new AlertDialog.Builder(ChatConvoActivity.this)
                        .setTitle("Confirm")
                        .setMessage("Proceed to confirm this person?")
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.confirmItem(mItemId, mSenderAccount.getId());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }).create().show();
            }
        });
        dialogBuilder.setNegativeButton("Disagree", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_transaction_confirmation, null);
        dialogBuilder.setView(dialogView);
        TextView titleTv = dialogView.findViewById(R.id.confirm_item_title);
        titleTv.setText(title);
        TextView locationTv = dialogView.findViewById(R.id.confirm_item_location);
        locationTv.setText(location);
        TextView dateTv = dialogView.findViewById(R.id.confirm_item_date);
        dateTv.setText(DateUtils.DateFormat(date));
        TextView nameTv = dialogView.findViewById(R.id.confirm_account);
        nameTv.setText(mSenderAccount.getFirstName() + " " + mSenderAccount.getLastName());
        ImageView itemImageIv = dialogView.findViewById(R.id.confirm_item_image);

        Glide.with(this)
                .load(ApiConstants.BASE_URL + imageUrl)
                .apply(new RequestOptions().override(100, 100))
                .into(itemImageIv);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onSuccessConfirm(int transactionId) {
        Toast.makeText(this, "Sucessfully Confirmed", Toast.LENGTH_SHORT).show();
        mConvoTransactionReturn.setEnabled(false);
        mConvoTransactionMeetup.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RequestCodes.MEETUP_TRANSACTION) {
                int id = data.getIntExtra(IntentExtraKeys.MEET_ID, 0);
                confirmedAndMeet(id);
            }
        }
    }

    @Override
    public void alreadyConfirmedItem(int transactionId) {
        Toast.makeText(this, "Item was already confirmed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void confirmed() {
        mConvoTransactionLayout.setVisibility(View.VISIBLE);
        mConvoTransactionReturn.setEnabled(false);
        mConvoTransactionMeetup.setEnabled(true);
    }

    @Override
    public void showSetupMeetup(int transactionId) {
        Intent intent = new Intent(this, MeetTransactionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(IntentExtraKeys.TRANSACTION_ID, transactionId);
        startActivityForResult(intent, RequestCodes.MEETUP_TRANSACTION);
    }

    @Override
    public void alreadyConfirmed() {
        Toast.makeText(this, "This Item already confirmed with other user", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void confirmedAndMeet(int meetUpId) {
        mConvoTransactionLayout.setVisibility(View.GONE);
        mPresenter.getMeetingPlaceDetails(meetUpId);
    }

    @Override
    public void hideTransactionOption() {
        mConvoTransactionLayout.setVisibility(View.GONE);
    }

    @Override
    public void showMeetingPlaceDetails(String meetingPlace, String meetingDate) {
        mTransactionConfirmedInfo.setVisibility(View.VISIBLE);
//        mMeetingLocation.setText(meetingPlace);
//        mMeetingDate.setText(DateUtils.DateFormat(meetingDate));
        String message = "Meet this person at \n" + meetingPlace + " on " + DateUtils.DateFormat(meetingDate) + " " + DateUtils.TimeFormat(meetingDate);
        mMeetTransactionMessage.setText(message);
    }

    @Override
    public void hideMeetingDetails() {
        mMeetingDetailsLabel.setText("Show Meeting Place Details");
        mToggleMeetingDetails.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_down_grey_800_24dp));
        mMeetupDetailsLayout.setVisibility(View.GONE);
        mShowMeetingDetails = false;
    }

    @Override
    public void showMeetingDetails() {
        mMeetingDetailsLabel.setText("Hide Meeting Place Details");
        mToggleMeetingDetails.setImageDrawable(getResources().getDrawable(R.drawable.ic_keyboard_arrow_up_grey_800_24dp));
        mMeetupDetailsLayout.setVisibility(View.VISIBLE);
        mShowMeetingDetails = true;
    }

    @Override
    public void hideMeetingConfirmationButtons() {
        mMeetingTransactionButtons.setVisibility(View.GONE);
    }

    @Override
    public void meetupDone() {
        mConvoTransactionLayout.setVisibility(View.GONE);
        mTransactionConfirmedInfo.setVisibility(View.GONE);
        mItemReturnInfo.setVisibility(View.VISIBLE);
    }

    @Override
    public void meetupFailed() {
        mConvoTransactionLayout.setVisibility(View.GONE);
        mTransactionConfirmedInfo.setVisibility(View.GONE);
    }

    @Override
    public void showReturnItemOption() {
        mConvoTransactionLayout.setVisibility(View.GONE);
        mTransactionConfirmedInfo.setVisibility(View.GONE);
        mItemReturnInfo.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideReturnItemOption() {
        mItemReturnInfo.setVisibility(View.GONE);
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

    @OnClick({R.id.item_details_btn, R.id.meeting_done_btn, R.id.meeting_failed_btn, R.id.toggle_meeting_details, R.id.convo_iv_options, R.id.convo_iv_image_gallery, R.id.convo_iv_like, R.id.convo_iv_send, R.id.convo_transaction_return, R.id.convo_transaction_meetup})
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
            case R.id.convo_transaction_return:
                mPresenter.openConfirmTransactionDialog(mItemId);
                break;
            case R.id.convo_transaction_meetup:
                mPresenter.openMeetupSetupDialog();
                break;
            case R.id.toggle_meeting_details:
                mPresenter.toggleMeetingDetails(mShowMeetingDetails);
                break;
            case R.id.meeting_done_btn:
                new AlertDialog.Builder(this)
                        .setTitle("Proceed")
                        .setMessage("Confirm meetup as done?")
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.updateMeetUpConfirmation("Accepted");
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.meeting_failed_btn:
                new AlertDialog.Builder(this)
                        .setTitle("Proceed")
                        .setMessage("Confirm meetup as failed?")
                        .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mPresenter.updateMeetUpConfirmation("Denied");
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                break;
            case R.id.item_details_btn:
                Intent intent = new Intent(this, ItemDetailsActivity.class);
                intent.putExtra(IntentExtraKeys.ITEM_ID, mItemId);
                startActivity(intent);
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
        mItemId = getIntent().getIntExtra(IntentExtraKeys.ITEM_ID, 0);
        int meetupId = getIntent().getIntExtra(IntentExtraKeys.MEET_ID, 0);
        if (meetupId != 0) {
            mPresenter.setMeetupId(meetupId);
        }
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

        //Check Transaction Status
        mPresenter.checkTransaction(mItemId, mSenderAccount.getId());

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
