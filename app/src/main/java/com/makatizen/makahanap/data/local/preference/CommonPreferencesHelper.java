package com.makatizen.makahanap.data.local.preference;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.makatizen.makahanap.di.qualifiers.ApplicationContext;
import com.makatizen.makahanap.pojo.MakahanapAccount;

public class CommonPreferencesHelper implements BasePreferencesHelper {

    //shared prefs name
    private static final String NAME_PREF = "2xwmqlFblWKUgKQgqof9_prefs";

    private final SharedPreferences preferences;

    public CommonPreferencesHelper(@ApplicationContext Context context) {
        this.preferences = context.getSharedPreferences(NAME_PREF, Context.MODE_PRIVATE);
    }
    /*
    *  Method for saving account in prefs in json form
    * */
    public void setAccountToPrefs(MakahanapAccount makahanapAccount) {
        String jsonAccount = new Gson().toJson(makahanapAccount);
        preferences.edit().putString(AppPreferencesHelper.PREF_KEY_ACCOUNT, jsonAccount).apply();
    }
    /*
     *  Method for getting account in prefs
     * */
    public MakahanapAccount getAccountFromPrefs() {
        String jsonAccount = preferences.getString(AppPreferencesHelper.PREF_KEY_ACCOUNT, "");
        return new Gson().fromJson(jsonAccount, MakahanapAccount.class);
    }

    @Override
    public boolean checkIfContainsValue(final String key) {
        return preferences.contains(key);
    }

    /**
     * Method for clearPrefs values in prefs
     */
    @Override
    public void clearPrefs() {
        preferences.edit().clear().apply();
    }

    /**
     * Method for getting boolean value from prefs
     *
     * @return boolean value
     */
    @Override
    public boolean getBooleanFromPrefs(String key) {
        return preferences.getBoolean(key, false);
    }

    /**
     * Method for getting int value from prefs
     *
     * @return integer value
     */
    @Override
    public int getIntFromPrefs(String key) {
        return preferences.getInt(key, 0);
    }

    /**
     * Method for getting long value from prefs
     *
     * @return long value
     */
    @Override
    public long getLongFromPrefs(String key) {
        return preferences.getLong(key, 0);
    }

    /**
     * Method for getting string value from prefs
     *
     * @return string value
     */
    @Override
    public String getStringFromPrefs(String key) {
        return preferences.getString(key, "");
    }

    /**
     * Method for getting string value from prefs with default value
     *
     * @return string value
     */
    @Override
    public String getStringFromPrefs(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    /**
     * Method for setting boolean value from prefs
     */
    @Override
    public void setBooleanToPrefs(String key, boolean value) {
        preferences.edit().putBoolean(key, value).apply();
    }

    /**
     * Method for setting int value from prefs
     */
    @Override
    public void setIntToPrefs(String key, int value) {
        preferences.edit().putInt(key, value).apply();
    }

    /**
     * Method for setting long value from prefs
     */
    @Override
    public void setLongToPrefs(String key, long value) {
        preferences.edit().putLong(key, value).apply();
    }

    /**
     * Method for setting string value from prefs
     */
    @Override
    public void setStringToPrefs(String key, String value) {
        preferences.edit().putString(key, value).apply();
    }

}
