package com.makatizen.makahanap.ui.report.personal_thing;

import android.text.TextUtils;
import android.util.Log;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.BarangayData;
import com.makatizen.makahanap.pojo.PersonalThing;
import com.makatizen.makahanap.ui.base.BasePresenter;
import com.makatizen.makahanap.utils.enums.Categories;
import com.makatizen.makahanap.utils.enums.Type;
import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class ReportPersonalThingPresenter<V extends ReportPersonalThingMvpView> extends BasePresenter<V>
        implements ReportPersonalThingMvpPresenter<V> {

    private static final String TAG = "Report PT";

    private List<BarangayData> mBarangayDataList = new ArrayList<>();

    @Inject
    ReportPersonalThingPresenter(final DataManager dataManager) {
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
    public void loadBarangayList() {
        getDataManager().getAllBarangayDataFromDb()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new SingleObserver<List<BarangayData>>() {
                    @Override
                    public void onError(final Throwable e) {
                        Log.e(TAG, "onError: Get All Barangay Data", e);
                    }

                    @Override
                    public void onSubscribe(final Disposable d) {
                        getCompositeDisposable().add(d);
                    }

                    @Override
                    public void onSuccess(final List<BarangayData> barangayDataList) {
                        Log.d(TAG, "onSuccess: Get All Barangay Data");
                        mBarangayDataList = barangayDataList;
                        List<String> brgyNames = new ArrayList<>();
                        for (BarangayData data : barangayDataList) {
                            brgyNames.add(data.getName());
                        }
                        getMvpView().setBarangayNameList(brgyNames);
                    }
                });
    }

    @Override
    public void selectCategory(final Categories selectedCategory) {
        getMvpView().setSelectedCategory(selectedCategory);
        switch (selectedCategory) {
            case MOBILE_PHONE:
                getMvpView().showBrandOption();
                break;
            case UMBRELLA:
                getMvpView().showBrandOption();
                break;
            case WALLET:
                getMvpView().showBrandOption();
                break;
            case LAPTOP:
                getMvpView().showBrandOption();
                break;
            case KEYS:
                getMvpView().showBrandOption();
                break;
            case BAG:
                getMvpView().showBrandOption();
                break;
            case ID:
                getMvpView().hideBrandOption();
                break;
            case SCHOOL_SUPPLIES:
                getMvpView().showBrandOption();
                break;
            case ART:
                getMvpView().hideBrandOption();
                break;
            case BOOKS:
                getMvpView().hideBrandOption();
                break;
            case OTHERS:
                getMvpView().hideBrandOption();
                break;
            case CLOTHES:
                getMvpView().showBrandOption();
                break;
            case JEWELRY:
                getMvpView().showBrandOption();
                break;
            case eREADERS:
                getMvpView().showBrandOption();
            case BANK_CARDS:
                getMvpView().hideBrandOption();
                break;
            case PAPER_WORKS:
                getMvpView().hideBrandOption();
                break;
            case PC_ACCESSORIES:
                getMvpView().showBrandOption();
                break;
            case SPORTS_EQUIPMENT:
                getMvpView().showBrandOption();
                break;
            case BEAUTY_ACCESSORIES:
                getMvpView().showBrandOption();
                break;
            case PHOTOGRAPHY_EQUIPMENT:
                getMvpView().showBrandOption();
                break;
            case MOBILE_PHONE_ACCESSORIES:
                getMvpView().showBrandOption();
                break;
        }
    }

    @Override
    public void submitReport(final PersonalThing personalThing) {
        if (personalThing.getType() == Type.FOUND) {
            for (BarangayData barangay : mBarangayDataList) {
                if (personalThing.getBarangayData().getName().equals(barangay.getName())) {
                    personalThing.setBarangayData(barangay);
                }
            }
        }
        personalThing.setAccountData(getDataManager().getCurrentAccount());
        getMvpView().hideKeyboard();
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed); //Network Failed
        } else {
            getDataManager().reportPersonalThing(personalThing)
                    .subscribeOn(getSchedulerProvider().io())
                    .observeOn(getSchedulerProvider().ui())
                    .doOnSubscribe(new Consumer<Disposable>() {
                        @Override
                        public void accept(final Disposable disposable) throws Exception {
                            getMvpView().showLoading();
                        }
                    })
                    .subscribe(new CompletableObserver() {
                        @Override
                        public void onComplete() {
                            Log.d(TAG, "onComplete: Submitting Report!");
                            if (!isViewAttached()) {
                                return;
                            }
                            getMvpView().hideLoading();
                            getMvpView().onSubmitReportCompleted();
                        }

                        @Override
                        public void onError(final Throwable e) {
                            Log.e(TAG, "onError: Submit Report", e);
                            if (!isViewAttached()) {
                                return;
                            }
                            getMvpView().hideLoading();

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
    public boolean validateReport(final PersonalThing personalThing, final boolean isRewarded) {
        clearErrors();

        if (TextUtils.isEmpty(personalThing.getCategory())) {
            getMvpView().setCategoryError("Please select category");
        } else if (TextUtils.isEmpty(personalThing.getItemName())) {
            getMvpView().setItemNameError("Please enter the item name");
        } else if (TextUtils.isEmpty(personalThing.getDescription())) {
            getMvpView().setDescriptionError("Please enter the description");
        } else if (personalThing.getLocationData().getId() == null) {
            getMvpView().setLocationError("Please select a location");
        } else if (TextUtils.isEmpty(personalThing.getDate())) {
            getMvpView().setDateError("Please select a date");
        } else if (TextUtils.isEmpty(personalThing.getColor())) {
            getMvpView().setColorError("Please enter the color");
        } else if (personalThing.getItemImagesUrl().size() == 0) {
            getMvpView().setImageError("Please add the item image/s");
        } else if (isRewarded && personalThing.getReward() == 0.00 && personalThing.getType() == Type.LOST) {
            getMvpView().setRewardAmountError("Please enter the amount");
        } else if (isRewarded && personalThing.getReward() < 100 && personalThing.getType() == Type.LOST) {
            getMvpView().setRewardAmountError("Please give an amount at least 100");
        } else {
            return true;
        }
        return false;

    }

    private void clearErrors() {
        getMvpView().setBrandError(null);
        getMvpView().setColorError(null);
        getMvpView().setDateError(null);
        getMvpView().setDescriptionError(null);
        getMvpView().setItemNameError(null);
        getMvpView().setRewardAmountError(null);
    }
}
