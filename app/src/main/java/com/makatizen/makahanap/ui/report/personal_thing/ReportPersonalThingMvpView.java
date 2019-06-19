package com.makatizen.makahanap.ui.report.personal_thing;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.utils.enums.Categories;
import java.util.List;

public interface ReportPersonalThingMvpView extends BaseMvpView {

    void hideBrandOption();

    void onFoundItemType();

    void onLostItemType();

    void onNoGiveReward();

    void onNoTurnoverItem();

    void onSubmitReportCompleted();

    void onYesGiveReward();

    void onYesTurnoverItem();

    void setBarangayNameList(List<String> barangayList);

    void setBrandError(String error);

    void setCategoryError(String error);

    void setColorError(String error);

    void setDateError(String error);

    void setDescriptionError(String error);

    void setImageError(String error);

    void setItemNameError(String error);

    void setLocationError(String error);

    void setRewardAmountError(String error);

    void setSelectedCategory(Categories selectedCategory);

    void showBrandOption();
}
