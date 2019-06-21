package com.makatizen.makahanap.ui.item_details;

import com.makatizen.makahanap.data.DataManager;
import com.makatizen.makahanap.ui.base.BasePresenter;
import javax.inject.Inject;

public class ItemDetailsPresenter<V extends ItemDetailsMvpView> extends BasePresenter<V> implements ItemDetailsMvpPresenter<V> {

    @Inject
    ItemDetailsPresenter(final DataManager dataManager) {
        super(dataManager);
    }
}
