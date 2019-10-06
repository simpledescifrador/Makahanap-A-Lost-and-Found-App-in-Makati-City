package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.pojo.FeedItem;

import java.util.List;

public class GetMapItemsResponse {

    private String id;
    private String title;
    private String address;
    private String lat;
    private String lng;
    @SerializedName("data")
    @Expose
    private List<FeedItem> items;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public List<FeedItem> getItems() {
        return items;
    }
}
