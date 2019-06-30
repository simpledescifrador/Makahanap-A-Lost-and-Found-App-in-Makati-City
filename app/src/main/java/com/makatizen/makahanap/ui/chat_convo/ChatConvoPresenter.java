package com.makatizen.makahanap.ui.chat_convo;

import android.util.Log;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.api_response.AddChatMessageResponse;
import com.makatizen.makahanap.pojo.api_response.ChatMessagesResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class ChatConvoPresenter<V extends ChatConvoMvpView> extends BasePresenter<V>
        implements ChatConvoMvpPresenter<V> {

    private static final String TAG = "ChatConvo";

    @Inject
    ChatConvoPresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public int getAccountId() {
        return getDataManager().getCurrentAccount().getId();
    }

    @Override
    public void loadChatMessages(final int chatId) {
        Log.d(TAG, "loadChatMessages: ");

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.title_no_network);
        } else {
            getDataManager().getChatMessages(chatId)
                    .observeOn(getSchedulerProvider().ui())
                    .subscribeOn(getSchedulerProvider().io())
                    .delaySubscription(1000, TimeUnit.MILLISECONDS)
                    .subscribe(new SingleObserver<ChatMessagesResponse>() {
                        @Override
                        public void onError(final Throwable e) {
                            Log.e(TAG, "onError: loadChatMessages", e);

                            if (e instanceof SocketTimeoutException
                                    || e instanceof SocketException) {
                                getMvpView().onError(R.string.title_no_network);
                            }
                        }

                        @Override
                        public void onSubscribe(final Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(final ChatMessagesResponse chatMessagesResponse) {
                            Log.d(TAG, "onSuccess: loadChatMessages");

                            if (isViewAttached() && chatMessagesResponse.getTotalMessages() != 0) {
                                getMvpView().setChatMessages(chatMessagesResponse.getChatMessageList());
                            } else {
                                //No Messages
                                getMvpView().onNewConvo();
                            }
                        }
                    });
        }
    }

    @Override
    public void sendMessage(final int chatId, final String message, final int position) {
        Log.d(TAG, "sendMessage: ");
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.title_no_network);
        } else {
            int accountId = getDataManager().getCurrentAccount().getId();
            getDataManager().addChatMessage(chatId, accountId, message)
                    .observeOn(getSchedulerProvider().ui())
                    .subscribeOn(getSchedulerProvider().io())
                    .subscribe(new SingleObserver<AddChatMessageResponse>() {
                        @Override
                        public void onError(final Throwable e) {
                            Log.e(TAG, "onError: Sending Message", e);
                            if (e instanceof SocketTimeoutException
                                    || e instanceof SocketException) {
                                getMvpView().onError(R.string.title_no_network);
                            }
                        }

                        @Override
                        public void onSubscribe(final Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(final AddChatMessageResponse addChatMessageResponse) {
                            Log.d(TAG, "onSuccess: Sending Message");
                            if (isViewAttached() && addChatMessageResponse.isMessageSent()) {
                                //Message sent
                                getMvpView().onMessageSentSuccess(addChatMessageResponse.getMessageTime(), position);
                            } else {
                                //Message Not Sent
                            }
                        }
                    });
        }
    }
}
