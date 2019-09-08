package com.makatizen.makahanap.ui.chat_convo;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface ChatConvoMvpPresenter<V extends ChatConvoMvpView & BaseMvpView> extends Presenter<V> {

    void loadChatMessages(int chatId);

    void sendMessage(int chatId, String message, int position);

    int getAccountId();

    void checkTransactionAvailability(int itemId);

    void openConfirmTransactionDialog(int itemId);

    void confirmItem(int itemId, int ownerId);

    void openMeetupSetupDialog();

    void checkTransaction(int itemId, int accountId);

    void getMeetingPlaceDetails(int meetId);

    void toggleMeetingDetails(boolean showDetails);

    void updateMeetUpConfirmation(String confirmation);

    void setMeetupId(int meetupId);
}
