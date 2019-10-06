package com.makatizen.makahanap.ui.main.map;

import com.google.android.gms.maps.model.LatLng;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.FeedItem;
import com.makatizen.makahanap.pojo.api_response.GetLatestFeedResponse;
import com.makatizen.makahanap.pojo.api_response.GetMapItemsResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;
import com.makatizen.makahanap.utils.enums.Type;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;

public class MapPresenter<V extends MapMvpView> extends BasePresenter<V> implements MapMvpPresenter<V> {
    private static final String TAG = "MainMap";
    private HashMap<String, List<FeedItem>> feedItems = new HashMap<>();

    @Inject
    MapPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void loadMap() {
        getMvpView().showCurrentLocation();

        //get Markers
        getDataManager().getItemLocations()
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .subscribe(new SingleObserver<GetLatestFeedResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(GetLatestFeedResponse response) {
                        if (!isViewAttached()) {
                            return;
                        }
                        if (response.getTotalResults() > 0) {
                            getMvpView().setMarkers(response.getFeedItems());
                        } else {
                            //No Items
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (e instanceof SocketTimeoutException || e instanceof SocketException) {
                            getMvpView().onError(R.string.error_network_failed);
                        }
                    }
                });
    }

    @Override
    public void loadMapItems() {
        getMvpView().showCurrentLocation();

        //Get Map Items
        getDataManager().getMapItems()
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .delaySubscription(5000, TimeUnit.MILLISECONDS) //Delay for 5 Seconds
                .subscribe(new SingleObserver<List<GetMapItemsResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<GetMapItemsResponse> responses) {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (responses.size() > 0) {
                            for (GetMapItemsResponse mapItem : responses) {
                                getMvpView().setMapMarker(mapItem.getId(), mapItem.getTitle(), mapItem.getAddress(), new LatLng(Double.parseDouble(mapItem.getLat()), Double.parseDouble(mapItem.getLng())));
                                feedItems.put(mapItem.getId(), mapItem.getItems());
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (e instanceof SocketTimeoutException || e instanceof SocketException) {
                            getMvpView().onError(R.string.error_network_failed);
                        }
                    }
                });

    }

    @Override
    public void getLocationItems(String id, String title) {
        List<FeedItem> items = feedItems.get(id);

        if (items == null) {
            return;
        }

        int lost = 0;
        int found = 0;
        for (FeedItem item : items) {
            if (item.getItemType() == Type.LOST) {
                lost++;
            } else {
                found++;
            }
        }

        getMvpView().showLocationItems(items, title, lost, found);
    }

    @Override
    public void updateMap() {
        //Get Map Items
        getDataManager().getMapItems()
                .observeOn(getSchedulerProvider().ui())
                .subscribeOn(getSchedulerProvider().io())
                .delaySubscription(2000, TimeUnit.MILLISECONDS) //Delay for 5 Seconds
                .subscribe(new SingleObserver<List<GetMapItemsResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(List<GetMapItemsResponse> responses) {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (responses.size() > 0) {
                            for (GetMapItemsResponse mapItem : responses) {
                                getMvpView().setMapMarker(mapItem.getId(), mapItem.getTitle(), mapItem.getAddress(), new LatLng(Double.parseDouble(mapItem.getLat()), Double.parseDouble(mapItem.getLng())));
                                feedItems.put(mapItem.getId(), mapItem.getItems());
                            }
                        } else {

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (e instanceof SocketTimeoutException || e instanceof SocketException) {
                            getMvpView().onError(R.string.error_network_failed);
                        }
                    }
                });
    }
}
