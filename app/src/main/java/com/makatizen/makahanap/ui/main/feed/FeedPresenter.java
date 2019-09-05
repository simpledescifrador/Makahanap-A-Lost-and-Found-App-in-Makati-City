package com.makatizen.makahanap.ui.main.feed;

import android.util.Log;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.FeedItem;
import com.makatizen.makahanap.pojo.api_response.GetLatestFeedResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class FeedPresenter<V extends FeedMvpView> extends BasePresenter<V> implements FeedMvpPresenter<V> {

    private static final String TAG = "Feed";

    @Inject
    FeedPresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void loadLatestFeed() {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().noNetworkConnection();
        } else {
            getDataManager().getLatestFeed()
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .delaySubscription(3000, TimeUnit.MILLISECONDS)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(final Disposable disposable) throws Exception {
                            getMvpView().showFeedLoading();
                        }
                    })
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideFeedLoading();
                        }
                    })
                    .subscribe(new SingleObserver<GetLatestFeedResponse>() {
                        @Override
                        public void onError(final Throwable e) {
                            Log.e(TAG, "onError: Load Feed Items", e);
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
                        public void onSuccess(final GetLatestFeedResponse getLatestFeedResponse) {
                            Log.d(TAG, "onSuccess: " + getLatestFeedResponse.getTotalResults());
                            if (getLatestFeedResponse.getTotalResults() != 0) {
                                Collections.reverse(getLatestFeedResponse.getFeedItems());
                                for (final FeedItem feedItem : getLatestFeedResponse.getFeedItems()) {
                                    getMvpView().setFeedItem(feedItem);
                                }
                            } else {
                                //No Feed Items
                                getMvpView().noFeedContent();
                            }
                        }
                    });
        }
    }

    @Override
    public void refreshFeed() {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.title_no_network);
        } else {
            getDataManager().getLatestFeed()
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .delaySubscription(3000, TimeUnit.MILLISECONDS)
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideFeedLoading();
                        }
                    })
                    .subscribe(new SingleObserver<GetLatestFeedResponse>() {
                        @Override
                        public void onError(final Throwable e) {
                            Log.e(TAG, "onError: Load Feed Items", e);
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
                        public void onSuccess(final GetLatestFeedResponse getLatestFeedResponse) {
                            Log.d(TAG, "onSuccess: " + getLatestFeedResponse.getTotalResults());
                            if (getLatestFeedResponse.getTotalResults() != 0) {
                                Collections.reverse(getLatestFeedResponse.getFeedItems());
                                getMvpView().onFeedItemsUpdate(getLatestFeedResponse.getFeedItems());
                            } else {
                                //No Feed Items
                                getMvpView().noFeedContent();
                            }
                        }
                    });
        }
    }

    @Override
    public void setSortFeed(String sortState) {
        getDataManager().setSortFeedState(sortState);
        if (sortState.equals("Lost")) {
            getMvpView().sortFeedByLost();
        } else if (sortState.equals("Found")) {
            getMvpView().sortFeedByFound();
        } else {
            getMvpView().sortFeedByAll();
        }
    }

    @Override
    public void setPostViewState(String viewState) {
        getDataManager().setPostViewState(viewState);
        if (viewState.equals("Grid")) {
            getMvpView().gridView();
        } else {
            getMvpView().listView();
        }
    }

    @Override
    public void loadFeedState() {
        String sortState = getDataManager().getSortFeedState();
        String viewState = getDataManager().getPostViewState();
        setSortFeed(sortState);
        setPostViewState(viewState);
    }
}
