package com.makatizen.makahanap.ui.item_details;

import com.makatizen.makahanap.pojo.api_response.GetItemDetailsResponse;
import com.makatizen.makahanap.ui.base.BaseMvpView;
import java.util.List;

public interface ItemDetailsMvpView extends BaseMvpView {
    void setItemImages(List<String> itemImages);

    void hideItemImageLoading();

    void hideItemDetailsLoading();

    void onPersonalThingData(GetItemDetailsResponse response);

    void onPetData(GetItemDetailsResponse response);

    void onPersonData(GetItemDetailsResponse response);
}
