package com.makatizen.makahanap.ui.item_details;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

public interface ItemDetailsMvpPresenter<V extends ItemDetailsMvpView & BaseMvpView> extends Presenter<V> {

    void openChat(int account1Id, String type);

    void loadItemDetails(int itemId);

    void loadItemImages(int itemId);

    void openItemChat(int itemId);

    void checkReturnStatus(int itemId);

    void startReturnActivity();

    void checkItemPendingReturnAgreement(int itemId);

    void agreeAgreement();

    void disgreeAgreement();

    void rateAccount(String rating, String feedBack);

    void checkItemReported(String itemId);

    void reportItem(String itemId, String reason);
}
