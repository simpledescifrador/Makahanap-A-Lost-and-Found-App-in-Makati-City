package com.makatizen.makahanap.ui.search;

import android.text.TextUtils;

import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.FeedItem;
import com.makatizen.makahanap.pojo.api_response.SearchItemResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;

import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class SearchPresenter<V extends SearchMvpView> extends BasePresenter<V> implements SearchMvpPresenter<V> {

    private static final String TAG = "Search";

    private String mCurrentKeyword = "";

    @Inject
    SearchPresenter(DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void getItems(String keyword, String limit) {
        mCurrentKeyword = keyword;
        final String query = formatQuery(keyword);

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError("No Internet Connection");
        } else {
            getDataManager().searchItems(query, limit)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .delaySubscription(1000, TimeUnit.MILLISECONDS)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            getMvpView().showSearchLoading();
                        }
                    })
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideSearchLoading();
                        }
                    })
                    .subscribe(new SingleObserver<SearchItemResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(SearchItemResponse searchItemResponse) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (searchItemResponse.getTotalResults() > 0) {
                                Collections.sort(searchItemResponse.getItem(), new Comparator<FeedItem>() {
                                    @Override
                                    public int compare(FeedItem o1, FeedItem o2) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                        if (o1.getItemCreatedAt() == null || o2.getItemCreatedAt() == null) {
                                            return 0;
                                        }

                                        Date date1 = null;
                                        Date date2 = null;
                                        try {
                                            date1 = sdf.parse(o1.getItemCreatedAt());
                                            date2 = sdf.parse(o2.getItemCreatedAt());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        return date2.compareTo(date1);
                                    }
                                });
                                getMvpView().setNumberOfResults(String.valueOf(searchItemResponse.getTotalResults()));
                                getMvpView().setSearchResults(searchItemResponse.getItem());
                            } else {
                                getMvpView().noResultsFound(mCurrentKeyword);
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

    @Override
    public void saveFilterPosition(HashMap<String, Integer> filterMap) {

    }

    @Override
    public void searchFilter(String keyword, HashMap<String, String> filterMap) {
        String itemType = filterMap.get("item_type").equals("All") ? "" : filterMap.get("item_type");
        String reportType = filterMap.get("report_type").equals("All") ? "" : filterMap.get("report_type");
        String status = filterMap.get("status").equals("All") ? "" : filterMap.get("status");
        StringBuilder builder = new StringBuilder();
        String filter = "";

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError("No Network Connection");
            return;
        }

        if (TextUtils.isEmpty(itemType) && TextUtils.isEmpty(reportType) && TextUtils
                .isEmpty(status)) { // if filter is reset
            getDataManager().searchItems(formatQuery(keyword), "20")
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .delaySubscription(1000, TimeUnit.MILLISECONDS)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            getMvpView().showSearchLoading();
                        }
                    })
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideSearchLoading();
                        }
                    })
                    .subscribe(new SingleObserver<SearchItemResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(SearchItemResponse searchItemResponse) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (searchItemResponse.getTotalResults() > 0) {
                                Collections.sort(searchItemResponse.getItem(), new Comparator<FeedItem>() {
                                    @Override
                                    public int compare(FeedItem o1, FeedItem o2) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                        if (o1.getItemCreatedAt() == null || o2.getItemCreatedAt() == null) {
                                            return 0;
                                        }

                                        Date date1 = null;
                                        Date date2 = null;
                                        try {
                                            date1 = sdf.parse(o1.getItemCreatedAt());
                                            date2 = sdf.parse(o2.getItemCreatedAt());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        return date2.compareTo(date1);
                                    }
                                });
                                getMvpView().setNumberOfResults(String.valueOf(searchItemResponse.getTotalResults()));
                                getMvpView().setSearchResults(searchItemResponse.getItem());
                            } else {
                                getMvpView().noResultsFound(mCurrentKeyword);
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

        } else {
            if (!TextUtils.isEmpty(itemType)) {
                builder.append("item type").append(":").append(itemType.toLowerCase());
                if (!TextUtils.isEmpty(reportType)) {
                    builder.append(",").append("report type").append(":").append(reportType.toLowerCase());
                }

                if (!TextUtils.isEmpty(status)) {
                    builder.append(",").append("item status").append(":").append(status.toLowerCase());
                }
            } else if (!TextUtils.isEmpty(reportType)) {
                builder.append("report type").append(":").append(reportType.toLowerCase());
                if (!TextUtils.isEmpty(status)) {
                    builder.append(",").append("item status").append(":").append(status.toLowerCase());
                }
            } else if (!TextUtils.isEmpty(status)) {
                builder.append("item status").append(":").append(status.toLowerCase());
            }

            filter = builder.toString();
            mCurrentKeyword = keyword;
            final String finalFilter = filter;

            getDataManager().searchItemsFiltered(formatQuery(keyword), finalFilter)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .delaySubscription(1000, TimeUnit.MILLISECONDS)
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(Disposable disposable) throws Exception {
                            getMvpView().showSearchLoading();
                        }
                    })
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideSearchLoading();
                        }
                    })
                    .subscribe(new SingleObserver<SearchItemResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                            getCompositeDisposable().add(d);
                        }

                        @Override
                        public void onSuccess(SearchItemResponse searchItemResponse) {
                            if (!isViewAttached()) {
                                return;
                            }

                            if (searchItemResponse.getTotalResults() > 0) {
                                Collections.sort(searchItemResponse.getItem(), new Comparator<FeedItem>() {
                                    @Override
                                    public int compare(FeedItem o1, FeedItem o2) {
                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                                        if (o1.getItemCreatedAt() == null || o2.getItemCreatedAt() == null) {
                                            return 0;
                                        }

                                        Date date1 = null;
                                        Date date2 = null;
                                        try {
                                            date1 = sdf.parse(o1.getItemCreatedAt());
                                            date2 = sdf.parse(o2.getItemCreatedAt());
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                        return date2.compareTo(date1);
                                    }
                                });
                                getMvpView().setNumberOfResults(String.valueOf(searchItemResponse.getTotalResults()));
                                getMvpView().setSearchResults(searchItemResponse.getItem());
                            } else {
                                getMvpView().noResultsFound(mCurrentKeyword);
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

    @Override
    public void getSaveFilterValues() {

    }

    private String formatQuery(String query) {
        return query.replace(" ", "+");
    }

}
