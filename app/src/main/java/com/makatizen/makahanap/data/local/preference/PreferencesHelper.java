package com.makatizen.makahanap.data.local.preference;

public interface PreferencesHelper {

    boolean isFirstTimeUser();

    void removeStartUpIntro();

    void showStartUpIntro();

}
