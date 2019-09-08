package com.makatizen.makahanap.ui.return_item;

import com.makatizen.makahanap.ui.base.BaseMvpView;

public interface ReturnItemMvpView extends BaseMvpView {
    void setMeetUpDetails(String location, String date);

    void onSubmitCompleted();
}
