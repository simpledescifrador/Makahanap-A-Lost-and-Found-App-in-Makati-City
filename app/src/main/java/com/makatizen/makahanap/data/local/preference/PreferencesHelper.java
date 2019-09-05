package com.makatizen.makahanap.data.local.preference;

import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.utils.enums.Type;

public interface PreferencesHelper {

    interface AccountPreferences {

        void saveCurrentAccount(MakahanapAccount account);
    }

    boolean isFirstTimeUser();

    boolean isLoggedIn();

    void removeStartUpIntro();

    void setCurrentAccountLoggedIn(boolean logged);

    void showStartUpIntro();

    void saveCurrentAccount(MakahanapAccount account);

    MakahanapAccount getCurrentAccount();

    void setSortFeedState(String state);

    String getSortFeedState();

    void setPostViewState(String state);

    String getPostViewState();
}
