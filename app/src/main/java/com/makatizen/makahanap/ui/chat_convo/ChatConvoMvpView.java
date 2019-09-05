package com.makatizen.makahanap.ui.chat_convo;

import com.makatizen.makahanap.pojo.ChatMessage;
import com.makatizen.makahanap.pojo.FeedItem;
import com.makatizen.makahanap.ui.base.BaseMvpView;
import java.util.List;

public interface ChatConvoMvpView extends BaseMvpView {

    void onNewConvo();

    void setChatMessages(List<ChatMessage> chatMessageList);

    void onMessageSentSuccess(String messageTime, int position);

    void showTransactionButtons();

    void showConfirmDialog(String title, String location, String date, String imageUrl);

    void onSuccessConfirm(int transactionId);

    void alreadyConfirmedItem(int transactionId);

    void confirmed();

    void showSetupMeetup(int transactionId);

    void alreadyConfirmed();

    void confirmedAndMeet(int meetUpId);

    void hideTransactionOption();

    void showMeetingPlaceDetails(String meetingPlace, String meetingDate);

    void hideMeetingDetails();

    void showMeetingDetails();

    void hideMeetingConfirmationButtons();

    void meetupDone();

    void meetupFailed();

    void showReturnItemOption();

    void hideReturnItemOption();
}
