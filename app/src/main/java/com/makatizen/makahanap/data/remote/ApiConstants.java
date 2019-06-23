package com.makatizen.makahanap.data.remote;

import okhttp3.Credentials;

public class ApiConstants {

    /*------- API HEADERS -------------*/
    //Base Urls
    public static final String BASE_URL = "http://192.168.43.35/makahanap/";

    public static final String MAKATIZEN_API_BASE_URL = "http://192.168.43.35/makatizen/";

    public static final String API_KEY = "makahanap@key2018";

    public static final String MAKATIZEN_API_KEY = "makatizen@key2018";

    public static final String CONTENT_TYPE = "application/json";

    public static final String AUTHORIZATION = Credentials.basic("admin", "1234");

    /*------- END OF API HEADERS -------------*/


    /* -------------  API URL'S ------------ */
    //Authentication
    static final String LOGIN_REQUEST_URL = "api/v1/auth/login";
    static final String REGISTER_ACCOUNT_URL = "api/v1/auth/register";

    //Account
    static final String GET_ACCOUNT_DATA_URL = "api/v1/account/";
    static final String GET_LATEST_ACCOUNT_FEED_URL = "api/v1/accounts/";
    static final String GET_ACCOUNT_ITEM_IMAGES_URL = "api/v1/accounts/";

    //Barangay
    static final String GET_ALL_BARANGAY_DATA_URL = "api/v1/barangay";
    static final String GET_BARANGAY_DATA_URL = "api/v1/barangay/";

    //Report
    static final String REPORT_PERSONAL_THING_URL = "api/v1/report/pt";
    static final String REPORT_PET_URL = "api/v1/report/pet";
    static final String REPORT_PERSON_URL = "api/v1/report/person";

    //Items
    static final String GET_LATEST_FEED_URL = "api/v1/items";
    static final String GET_ITEM_IMAGES_URL = "api/v1/items/"/*itemID*/;
    static final String GET_ITEM_DETAILS_URL = "api/v1/items/"/*itemID */;
    /* ------------- END OF API URL'S ------------ */


    /* ------------- MAKATIZEN API URL'S -----------*/
    static final String VERIFY_MAKATIZEN_ID_URL = "api/v1/account/validate";

    static final String GET_MAKATIZEN_DATA_URL = "api/v1/account/";

    /* ------------- END OF API URL'S ------------ */

}
