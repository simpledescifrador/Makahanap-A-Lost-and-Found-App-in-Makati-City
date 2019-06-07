package com.makatizen.makahanap.data.remote;

import okhttp3.Credentials;

public class ApiConstants {

    /*------- API HEADERS -------------*/
    //Base Urls
    public static final String BASE_URL = "http://192.168.1.100/makahanap/";

    public static final String MAKATIZEN_API_BASE_URL = "http://192.168.1.100/makatizen/";

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
    static final String GET_ACCOUNT_DATA = "api/v1/account/";
    /* ------------- END OF API URL'S ------------ */


    /* ------------- MAKATIZEN API URL'S -----------*/
    static final String VERIFY_MAKATIZEN_ID = "api/v1/account/validate";

    static final String GET_MAKATIZEN_DATA = "api/v1/account/";

    /* ------------- END OF API URL'S ------------ */

}
