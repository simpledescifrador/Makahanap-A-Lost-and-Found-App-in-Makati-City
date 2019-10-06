package com.makatizen.makahanap.ui.main.map;

import com.google.android.gms.maps.model.LatLng;
import com.makatizen.makahanap.pojo.FeedItem;
import com.makatizen.makahanap.ui.base.BaseMvpView;

import java.util.List;

public interface MapMvpView extends BaseMvpView {
    void showCurrentLocation();

    void setMarkers(List<FeedItem> feedItemList);

    void setMapMarker(String tag, String title, String snippet, LatLng location);

    void showLocationItems(List<FeedItem> feedItems, String title, int lost, int found);
}
