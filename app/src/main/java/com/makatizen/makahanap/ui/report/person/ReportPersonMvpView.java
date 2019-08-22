package com.makatizen.makahanap.ui.report.person;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.utils.enums.AgeGroup;

public interface ReportPersonMvpView extends BaseMvpView {

    void onFoundItemType();

    void onLostItemType();

    void onNoGiveReward();

    void onSubmitReportCompleted();

    void onYesGiveReward();

    void setDateError(String error);

    void setDescriptionError(String error);

    void setImageError(String error);

    void setLocationError(String error);

    void setPersonAgeError(String error);

    void setPersonNameError(String error);

    void setRewardAmountError(String error);

    void showAgeRangeSeekDialog(AgeGroup ageGroup);
}
