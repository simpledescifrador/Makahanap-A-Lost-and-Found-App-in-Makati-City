package com.makatizen.makahanap.ui.report.pet;

import android.text.TextUtils;
import android.util.Log;
import com.makatizen.makahanap.R;
import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.pojo.Pet;
import com.makatizen.makahanap.ui.base.BasePresenter;
import com.makatizen.makahanap.utils.enums.PetType;
import com.makatizen.makahanap.utils.enums.Type;
import io.reactivex.CompletableObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import javax.inject.Inject;

public class ReportPetPresenter<V extends ReportPetMvpView> extends BasePresenter<V>
        implements ReportPetMvpPresenter<V> {

    private static final String TAG = "Report PeT";

    @Inject
    ReportPetPresenter(final DataManager dataManager) {
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
    public void selectedPetType(final PetType petType) {
        getMvpView().setSelectedType(petType);
        getMvpView().showPetSubType(petType);
    }

    @Override
    public void submitReport(final Pet pet) {
        pet.setAccountData(getDataManager().getCurrentAccount());
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().onError(R.string.error_network_failed); //Network Failed
        } else {
            getDataManager().reportPet(pet)
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
                            Log.d(TAG, "onComplete: ");
                            if (!isViewAttached()) {
                                return;
                            }
                            getMvpView().hideLoading();
                            getMvpView().onSubmitReportCompleted();
                        }

                        @Override
                        public void onError(final Throwable e) {
                            Log.e(TAG, "onError: Submit Pet Report", e);
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
    public boolean validateReport(final Pet pet, final boolean isRewarded) {
        clearErrors();
        if (TextUtils.isEmpty(pet.getName()) && pet.getItemType() == Type.LOST) {
            getMvpView().setPetNameError("Enter the pet name");
        } else if (TextUtils.isEmpty(pet.getPetType())) {
            getMvpView().setPetTypeError("Please select the pet type");
        } else if (TextUtils.isEmpty(pet.getDescription())) {
            getMvpView().setDescriptionError("Please enter the description");
        } else if (pet.getLocationData().getId() == null) {
            getMvpView().setLocationError("Please select the location");
        } else if (TextUtils.isEmpty(pet.getDate())) {
            getMvpView().setDateError("Please select a date");
        } else if (pet.getPetImagesUrl().size() == 0) {
            getMvpView().setImageError("Please add pet image");
        } else if (isRewarded && pet.getReward() == 0.00 && pet.getItemType() == Type.LOST) {
            getMvpView().setRewardAmountError("Please enter the amount");
        } else if (isRewarded && pet.getReward() < 100 && pet.getItemType() == Type.LOST) {
            getMvpView().setRewardAmountError("Please give an amount at least 100");
        } else {
            return true;
        }
        return false;
    }

    private void clearErrors() {
        getMvpView().setDateError(null);
        getMvpView().setDescriptionError(null);
        getMvpView().setPetNameError(null);
        getMvpView().setRewardAmountError(null);

    }
}
