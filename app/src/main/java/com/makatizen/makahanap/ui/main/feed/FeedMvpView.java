package com.makatizen.makahanap.ui.main.feed;

import com.makatizen.makahanap.pojo.FeedItem;
import com.makatizen.makahanap.ui.base.BaseMvpView;
import java.util.List;

public interface FeedMvpView extends BaseMvpView {

    void hideFeedLoading();

    void noFeedContent();

    void noNetworkConnection();

    void onFeedItemsUpdate(List<FeedItem> feedItems);

    void setFeedItem(FeedItem feedItem);

    void showFeedLoading();

    void sortFeedByAll();

    void sortFeedByLost();

    void sortFeedByFound();

    void gridView();

    void listView();
}
