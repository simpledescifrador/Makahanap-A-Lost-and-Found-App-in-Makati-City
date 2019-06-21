package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.pojo.FeedItem;
import java.util.List;

public class GetLatestFeedResponse {
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    @SerializedName("feed_items")
    @Expose
    private List<FeedItem> feedItems;

    public Integer getTotalResults() {
        return totalResults;
    }

    public List<FeedItem> getFeedItems() {
        return feedItems;
    }
}
