package com.makatizen.makahanap.ui.item_details;

import com.makatizen.makahanap.pojo.api_response.GetItemDetailsResponse;
import com.makatizen.makahanap.ui.base.BaseMvpView;
import java.util.List;

public interface ItemDetailsMvpView extends BaseMvpView {

    void hideItemDetailsLoading();

    void hideItemImageLoading();

    void onPersonData(GetItemDetailsResponse response);

    void onPersonalThingData(GetItemDetailsResponse response);

    void onPetData(GetItemDetailsResponse response);

    void onSuccessGetChatId(int chatId);

    void removeMessageButton();

    void setItemImages(List<String> itemImages);

    void showReturnOption();

    void openReturnItemActivity(int meetupId);

    void showReturnAgreement();

    void showRatingDialog(String profileUrl ,String accountName);

    void hideReturnAgreement();

    void onSuccessReturn();
}
