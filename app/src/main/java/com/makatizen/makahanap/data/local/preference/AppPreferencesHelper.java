package com.makatizen.makahanap.data.local.preference;

import javax.inject.Inject;

public class AppPreferencesHelper implements PreferencesHelper {

    private CommonPreferencesHelper commonPreferencesHelper;

    @Inject
    public AppPreferencesHelper(CommonPreferencesHelper commonPreferencesHelper) {
        this.commonPreferencesHelper = commonPreferencesHelper;
    }

}
