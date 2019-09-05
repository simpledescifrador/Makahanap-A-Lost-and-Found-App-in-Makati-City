package com.makatizen.makahanap.ui.transaction;

import com.makatizen.makahanap.ui.base.BaseMvpView;

public interface MeetTransactionMvpView extends BaseMvpView {
    void onSubmitSuccess(int meetup);

    void setDateError(String dateError);

    void setTimeError(String timeError);
}
