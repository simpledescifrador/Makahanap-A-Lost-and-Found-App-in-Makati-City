package com.makatizen.makahanap.ui.return_item;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface ReturnItemMvpPresenter<V extends ReturnItemMvpView & BaseMvpView> extends Presenter<V> {
    void loadMeetingDetails(int meetUpId);

    boolean validate(String image);

    void submit(int meetupId, int itemId, String imagePath);
}
