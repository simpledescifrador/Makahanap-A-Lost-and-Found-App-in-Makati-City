package com.makatizen.makahanap.data.remote;

import okhttp3.Credentials;

public class ApiConstants {

    /*------- API HEADERS -------------*/
    //Base Urls
    public static final String BASE_URL = "http://makahanap.x10host.com/";

    public static final String MAKATIZEN_API_BASE_URL = "http://makatizen.x10host.com/";

    public static final String API_KEY = "makahanap@key2018";

    public static final String MAKATIZEN_API_KEY = "makatizen@key2018";

    public static final String CONTENT_TYPE = "application/json";

    public static final String AUTHORIZATION = Credentials.basic("admin", "1234");

    /*------- END OF API HEADERS -------------*/


    /* -------------  API URL'S ------------ */
    //Authentication
    static final String LOGIN_REQUEST_URL = "api/v1/auth/login";

    static final String REGISTER_ACCOUNT_URL = "api/v1/auth/register";

    static final String REGISTER_ACCOUNT_TOKEN_URL = "api/auth/token/register";

    //Account
    static final String GET_ACCOUNT_DATA_URL = "api/v1/account/";

    static final String GET_LATEST_ACCOUNT_FEED_URL = "api/v1/accounts/";

    static final String GET_ACCOUNT_ITEM_IMAGES_URL = "api/v1/accounts/";

    static final String GET_ACCOUNTS_URL = "api/v1/accounts/";

    static final String POST_NEW_RATING_URL = "api/v1/accounts/rating/new";

    static final String GET_ACCOUNT_AVERAGE_RATING_URL = "api/v1/accounts/rating/avg/";

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

    //Chat
    static final String GET_CHAT_URL = "api/v1/chats/";

    static final String CREATE_CHAT_URL = "api/v1/chats/create";

    static final String NEW_ITEM_CHAT_URL = "api/v1/chats/new";

    //Notification
    static final String GET_NOTIFICATIONS_URL = "api/v1/account/notifications";

    static final String PUT_NOTIFICATION_VIEWED_URL = "api/v1/account/notification/mark/viewed/";

    static final String PUT_NOTIFICATION_UNVIEWED_URL = "api/v1/account/notification/mark/unviewed/";

    static final String PUT_NOTIFICATION_DELETE_URL = "api/v1/account/notification/delete/";

    static final String GET_TOTAL_UNVIEWED_NOTIFICATION_URL = "api/v1/account/notification/unviewed/";

    static final String GET_TOTAL_ACCOUNT_NOTIFICATION_URL = "api/v1/account/notification/total/";

    //Search
    static final String GET_SEARCH_ITEMS_URL = "api/v1/items/search";

    //Transaction
    static final String POST_TRANSACTION_ITEM_CONFIRM_URL = "api/v1/transactions/confirm";

    static final String GET_TRANSACTION_CONFIRMATION_STATUS_URL = "api/v1/transactions/confirmation/status/";

    static final String POST_TRANSACTION_NEW_MEETUP_URL = "api/v1/transactions/meetup/new";

    static final String GET_CHECK_TRANSACTION_STATUS_URL = "api/v1/transactions/confirmation/check";

    static final String GET_MEETUP_DETAILS_URL = "api/v1/transactions/meet/";

    static final String PUT_MEETUP_CONFIRMATION_URL = "api/v1/transactions/meet/confirmation";

    static final String GET_CHECK_RETURN_STATUS_URL = "api/v1/transaction/item/";

    static final String GET_CHECK_PENDING_TRANSACTION_URL = "api/v1/transaction/pending/";

    static final String POST_RETURN_ITEM_TRANSACTION_URL = "api/v1/transactions/item/return";

    static final String PUT_CONFIRM_RETURN_TRANSACTION_URL = "api/v1/transaction/item/confirmed";

    static final String PUT_DENIED_RETURN_TRANSACTION_URL = "api/v1/transaction/item/denied";

    /* ------------- END OF API URL'S ------------ */


    /* ------------- MAKATIZEN API URL'S -----------*/
    static final String VERIFY_MAKATIZEN_ID_URL = "api/v1/account/validate";

    static final String GET_MAKATIZEN_DATA_URL = "api/v1/account/";

    /* ------------- END OF API URL'S ------------ */

}
