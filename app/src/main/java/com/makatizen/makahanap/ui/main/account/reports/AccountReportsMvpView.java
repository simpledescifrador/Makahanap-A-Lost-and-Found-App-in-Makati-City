package com.makatizen.makahanap.ui.main.account.reports;

import com.makatizen.makahanap.pojo.FeedItem;
import com.makatizen.makahanap.ui.base.BaseMvpView;
import java.util.List;

public interface AccountReportsMvpView extends BaseMvpView {

    void noFeedContent();

    void noNetworkConnection();

    void setAccountReports(List<FeedItem> feedItemList);

    void onAccountReportUpdates(List<FeedItem> feedItems);
}
