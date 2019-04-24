package com.makatizen.makahanap.data.local.preference;

import javax.inject.Inject;

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_SHOW_INTRO = "PREF_KEY_SHOW_INTRO";

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
    public void removeStartUpIntro() {
        commonPreferencesHelper.setBooleanToPrefs(PREF_KEY_SHOW_INTRO, false);
    }

    @Override
    public void showStartUpIntro() {
        commonPreferencesHelper.setBooleanToPrefs(PREF_KEY_SHOW_INTRO, true);
    }
}
