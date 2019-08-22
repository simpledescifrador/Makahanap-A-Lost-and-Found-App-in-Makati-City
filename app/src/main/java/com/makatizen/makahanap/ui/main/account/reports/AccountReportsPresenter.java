package com.makatizen.makahanap.ui.main.account.reports;

import android.util.Log;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.api_response.GetLatestFeedResponse;
import com.makatizen.makahanap.ui.base.BasePresenter;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Collections;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;

public class AccountReportsPresenter<V extends AccountReportsMvpView> extends BasePresenter<V>
        implements AccountReportsMvpPresenter<V> {


    private static final String TAG = "AccountReports";

    @Inject
    AccountReportsPresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void checkUpdateAccountReports() {
        int accountId = getDataManager().getCurrentAccount().getId();
        getDataManager().getAccountLatestFeed(accountId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .delaySubscription(3000, TimeUnit.MILLISECONDS)
                .subscribe(new SingleObserver<GetLatestFeedResponse>() {
                    @Override
                    public void onError(final Throwable e) {
                        Log.e(TAG, "onError: Load Account Latest Feed", e);
                        if (e instanceof SocketTimeoutException
                                || e instanceof SocketException) {
                            getMvpView().noNetworkConnection();
                        }
                    }

                    @Override
                    public void onSubscribe(final Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(final GetLatestFeedResponse getLatestFeedResponse) {
                        Log.d(TAG, "onSuccess: Load Account Latest Response");
                        if (getLatestFeedResponse.getTotalResults() != 0) {
                            Collections.reverse(getLatestFeedResponse.getFeedItems());
                            getMvpView().onAccountReportUpdates(getLatestFeedResponse.getFeedItems());
                        } else {
                            //No Feed Items
                            getMvpView().noFeedContent();
                        }
                    }
                });
    }

    @Override
    public void loadAccountReports() {
        int accountId = getDataManager().getCurrentAccount().getId();
        getDataManager().getAccountLatestFeed(accountId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .delaySubscription(3000, TimeUnit.MILLISECONDS)
                .subscribe(new SingleObserver<GetLatestFeedResponse>() {
                    @Override
                    public void onError(final Throwable e) {
                        Log.e(TAG, "onError: Load Account Latest Feed", e);
                        if (e instanceof SocketTimeoutException
                                || e instanceof SocketException) {
                            getMvpView().noNetworkConnection();
                        }
                    }

                    @Override
                    public void onSubscribe(final Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(final GetLatestFeedResponse getLatestFeedResponse) {
                        Log.d(TAG, "onSuccess: Load Account Latest Response");
                        if (getLatestFeedResponse.getTotalResults() != 0) {
                            Collections.reverse(getLatestFeedResponse.getFeedItems());
                            getMvpView().setAccountReports(getLatestFeedResponse.getFeedItems());
                        } else {
                            //No Feed Items
                            getMvpView().noFeedContent();
                        }
                    }
                });
    }
}
