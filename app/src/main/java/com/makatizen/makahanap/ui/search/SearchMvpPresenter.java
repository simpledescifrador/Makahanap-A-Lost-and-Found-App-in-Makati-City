package com.makatizen.makahanap.ui.search;

import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;

import java.util.HashMap;

public interface SearchMvpPresenter<V extends SearchMvpView & BaseMvpView> extends Presenter<V> {
    void getItems(String keyword, String limit);

    void saveFilterPosition(HashMap<String, Integer> filterMap);

    void searchFilter(String keyword, HashMap<String, String> filterMap);

    void getSaveFilterValues();
}
