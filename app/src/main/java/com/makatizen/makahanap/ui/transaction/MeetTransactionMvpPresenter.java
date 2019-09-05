package com.makatizen.makahanap.ui.transaction;

import com.makatizen.makahanap.pojo.LocationData;
import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface MeetTransactionMvpPresenter<V extends MeetTransactionMvpView & BaseMvpView> extends Presenter<V> {
    boolean validateForm(LocationData locationData, String date, String time);

    void submitMeetingSetup(LocationData locationData, String datetime, String transactionId);
}
