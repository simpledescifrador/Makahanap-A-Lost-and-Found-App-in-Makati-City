package com.makatizen.makahanap.ui.report.person;

import android.text.TextUtils;
import android.util.Log;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.Person;
import com.makatizen.makahanap.ui.base.BasePresenter;
import com.makatizen.makahanap.utils.enums.AgeGroup;
import com.makatizen.makahanap.utils.enums.Type;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import javax.inject.Inject;

public class ReportPersonPresenter<V extends ReportPersonMvpView> extends BasePresenter<V>
        implements ReportPersonMvpPresenter<V> {

    private static final String TAG = "Report Person";

    @Inject
    ReportPersonPresenter(final DataManager dataManager) {
        super(dataManager);
    }

    @Override
    public void checkType(final Type type) {
        switch (type) {
            case LOST:
                getMvpView().onLostItemType();
                break;
            case FOUND:
                getMvpView().onFoundItemType();
                break;
        }
    }

    @Override
    public void selectedAgeGroup(final AgeGroup ageGroup) {
        getMvpView().showAgeRangeSeekDialog(ageGroup);
    }

    @Override
    public void submitReport(final Person person) {
        person.setAccountData(getDataManager().getCurrentAccount());
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed); //Network Failed
        } else {
            getDataManager().reportPerson(person)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(final Disposable disposable) throws Exception {
                            getMvpView().showLoading();
                        }
                    })
                    .doFinally(new Action() {
                        @Override
                        public void run() throws Exception {
                            getMvpView().hideLoading();
                        }
                    })
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete: Submit Person Report");
                            if (!isViewAttached()) {
                                return;
                            }
                            getMvpView().onSubmitReportCompleted();
                        }

                        @Override
                        public void onError(final Throwable e) {
                            Log.e(TAG, "onError: Submit Pet Report", e);
                            if (!isViewAttached()) {
                                return;
                            }

                            if (e instanceof SocketTimeoutException || e instanceof SocketException) {
                                getMvpView().onError(R.string.error_network_failed);
                            }
                        }

                        @Override
                        public void onSubscribe(final Disposable d) {
                            getCompositeDisposable().add(d);
                        }
                    });
        }
    }

    @Override
    public boolean validateReport(final Person person, final boolean isRewarded) {
        clearError();
        if (TextUtils.isEmpty(person.getAgeGroup())) {
            getMvpView().setPersonAgeError("Please choose the person age");
        } else if (TextUtils.isEmpty(person.getName()) && person.getType() == Type.LOST) {
            getMvpView().setPersonNameError("Please enter the lost person name");
        } else if (TextUtils.isEmpty(person.getDescription())) {
            getMvpView().setDescriptionError("Please enter the lost person description");
        } else if (person.getLocationData().getId() == null) {
            getMvpView().setLocationError("Please select the location");
        } else if (TextUtils.isEmpty(person.getDate())) {
            getMvpView().setDateError("Please select a date");
        } else if (person.getPersonImagesUrl().size() == 0) {
            getMvpView().setImageError("Please add person image");
        } else if (isRewarded && person.getReward() == 0.0 && person.getType() == Type.LOST) {
            getMvpView().setRewardAmountError("Please enter the amount");
        } else if (isRewarded && person.getType() == Type.LOST && person.getReward() < 100) {
            getMvpView().setRewardAmountError("Please give an amount at least 100");
        } else {
            return true;
        }
        return false;
    }

    private void clearError() {
        getMvpView().setRewardAmountError(null);
        getMvpView().setDescriptionError(null);
        getMvpView().setPersonNameError(null);
    }
}
