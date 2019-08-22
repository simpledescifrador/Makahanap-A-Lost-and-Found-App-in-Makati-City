package com.makatizen.makahanap.data;

import com.makatizen.makahanap.data.local.database.DbHelper;
import com.makatizen.makahanap.data.local.preference.PreferencesHelper;
import com.makatizen.makahanap.data.remote.ApiHelper;

public interface DataManager extends DbHelper, ApiHelper, PreferencesHelper {

}
