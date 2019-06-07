package com.makatizen.makahanap.data.local.preference;

import com.makatizen.makahanap.pojo.MakahanapAccount;

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
}
