package com.makatizen.makahanap.ui.main.feed;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface FeedMvpPresenter<V extends FeedMvpView & BaseMvpView>  extends Presenter<V> {
    void loadLatestFeed();

    void refreshFeed();

    void setSortFeed(String sortState);

    void setPostViewState(String viewState);

    void loadFeedState();
}
