package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.pojo.FeedItem;

import java.util.List;

public class SearchItemResponse {
    @SerializedName("match_items")
    @Expose
    private List<FeedItem> item = null;

    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    public List<FeedItem> getItem() {
        return item;
    }

    public Integer getTotalResults() {
        return totalResults;
    }
}
