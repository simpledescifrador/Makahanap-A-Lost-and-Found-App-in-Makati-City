package com.makatizen.makahanap.ui.report.pet;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.utils.enums.PetType;

public interface ReportPetMvpView extends BaseMvpView {

    void onFoundItemType();

    void onLostItemType();

    void onNoGiveReward();

    void onSubmitReportCompleted();

    void onYesGiveReward();

    void setDateError(String error);

    void setDescriptionError(String error);

    void setImageError(String error);

    void setLocationError(String error);

    void setPetNameError(String error);

    void setPetTypeError(String error);

    void setRewardAmountError(String error);

    void setSelectedType(PetType petType);

    void showPetSubType(PetType petType);
}
