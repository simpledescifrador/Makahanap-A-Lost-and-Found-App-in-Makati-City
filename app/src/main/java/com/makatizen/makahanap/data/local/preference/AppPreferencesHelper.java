package com.makatizen.makahanap.data.local.preference;

import com.makatizen.makahanap.data.local.preference.PreferencesHelper.AccountPreferences;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import javax.inject.Inject;

public class AppPreferencesHelper implements PreferencesHelper, AccountPreferences {

    private static final String PREF_KEY_SHOW_INTRO = "PREF_KEY_SHOW_INTRO";

    private static final String PREF_KEY_LOGGED_IN = "PREF_KEY_LOGGED_IN";

    static final String PREF_KEY_ACCOUNT = "PREF_KEY_ACCOUNT";

    private CommonPreferencesHelper commonPreferencesHelper;

    @Inject
    public AppPreferencesHelper(CommonPreferencesHelper commonPreferencesHelper) {
        this.commonPreferencesHelper = commonPreferencesHelper;
    }

    @Override
    public boolean isFirstTimeUser() {
        return !commonPreferencesHelper.checkIfContainsValue(PREF_KEY_SHOW_INTRO) || commonPreferencesHelper
                .getBooleanFromPrefs(PREF_KEY_SHOW_INTRO);
    }

    @Override
    public boolean isLoggedIn() {
        return commonPreferencesHelper.getBooleanFromPrefs(PREF_KEY_LOGGED_IN);
    }

    @Override
    public void removeStartUpIntro() {
        commonPreferencesHelper.setBooleanToPrefs(PREF_KEY_SHOW_INTRO, false);
    }

    @Override
    public void saveCurrentAccount(final MakahanapAccount account) {
        commonPreferencesHelper.setAccountToPrefs(account);
    }

    @Override
    public MakahanapAccount getCurrentAccount() {
        return commonPreferencesHelper.getAccountFromPrefs();
    }

    @Override
    public void setCurrentAccountLoggedIn(final boolean logged) {
        commonPreferencesHelper.setBooleanToPrefs(PREF_KEY_LOGGED_IN, logged);
        if (!logged) {
            commonPreferencesHelper.clearPrefs();
        }
    }

    @Override
    public void showStartUpIntro() {
        commonPreferencesHelper.setBooleanToPrefs(PREF_KEY_SHOW_INTRO, true);
    }

}
