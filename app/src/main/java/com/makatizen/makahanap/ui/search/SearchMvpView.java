package com.makatizen.makahanap.ui.search;

import com.makatizen.makahanap.pojo.FeedItem;
import com.makatizen.makahanap.ui.base.BaseMvpView;

import java.util.List;

public interface SearchMvpView extends BaseMvpView {
    void showSearchLoading();

    void hideSearchLoading();

    void noResultsFound(String keyword);

    void setNumberOfResults(String numberOfResults);

    void setSearchResults(List<FeedItem>feedItemList);
}
