package com.makatizen.makahanap.ui.chat;

import android.util.Log;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.api_response.ChatResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class ChatPresenter<V extends ChatMvpView> extends BasePresenter<V> implements ChatMvpPresenter<V> {

    private static final String TAG = "Chat";

    @Inject
    ChatPresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void loadChatList() {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.title_no_network);
        } else {
            final int currentId = getDataManager().getCurrentAccount().getId();
            getDataManager().getChatList(currentId)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .delaySubscription(2000, TimeUnit.MILLISECONDS)
                    .subscribe(new SingleObserver<ChatResponse>() {
                        @Override
                        public void onError(final Throwable e) {
                            Log.e(TAG, "onError: Loading Chat List", e);
                            if (!isViewAttached()) {
                                return;
                            }

                            if (e instanceof SocketTimeoutException
                                    || e instanceof SocketException) {
                                getMvpView().noNetworkConnection();
                            }
                        }

                        @Override
                        public void onSubscribe(final Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(final ChatResponse chatResponse) {
                            Log.d(TAG, "onSuccess: Loading Chat List");
                            if (isViewAttached() && chatResponse.getTotalChatRooms() != 0) {
                                getMvpView().setItemChatList(chatResponse.getChats(), currentId);
                            } else {
                                //NO Chat or Messages
                                getMvpView().noContent();
                            }
                        }
                    });
        }
    }

    @Override
    public void updateChatList() {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.title_no_network);
        } else {
            final int currentId = getDataManager().getCurrentAccount().getId();
            getDataManager().getChatList(currentId)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .subscribe(new SingleObserver<ChatResponse>() {
                        @Override
                        public void onError(final Throwable e) {
                            Log.e(TAG, "onError: Loading Chat List", e);
                            if (!isViewAttached()) {
                                return;
                            }

                            if (e instanceof SocketTimeoutException
                                    || e instanceof SocketException) {
                                getMvpView().noNetworkConnection();
                            }
                        }

                        @Override
                        public void onSubscribe(final Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(final ChatResponse chatResponse) {
                            Log.d(TAG, "onSuccess: Loading Chat List");
                            if (isViewAttached() && chatResponse.getTotalChatRooms() != 0) {
                                getMvpView().onItemChatListRefresh(chatResponse.getChats());
                            } else {
                                //NO Chat or Messages
                                getMvpView().noContent();
                            }
                        }
                    });
        }
    }
}
