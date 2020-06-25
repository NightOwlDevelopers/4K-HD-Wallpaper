package com.panaceasoft.pswallpaper.utils;

/**
 * Created by Panacea-Soft on 2019-09-07.
 * Contact Email : teamps.is.cool@gmail.com
 */

/**
 * Constants Value For App
 * Please don't change, if you not very sure the impact
 */
public class Constants {

    /**
     * Language
     */
    public static final String LANGUAGE_KEY = "Language";

    /**
     * Wallpaper
     */
    public static final String INTENT__FLAG = "flag";
    public static final String WALLPAPER = "wallpaper";//for backen type
    public static final String LIVE_WALLPAPER = "video_wallpaper";//for backen type
    public static final String FREE = "1";
    public static final String PREMIUM = "2";
    public static final String GIF = "1";
    public static final String IS_GIF = ".gif";
    public static final String NOGIF = "0";
    public static final String PORTRAIT = "portrait";
    public static final String LANDSCAPE = "landscape";
    public static final String SQUARE = "square";
    public static final String DONWLOADQUERY = "download";
    public static final String FAVQUERY = "favorite";
    public static final String downloadFunction = "download";
    public static final String setAsFunction = "setAs";
    public static final String shareFunction = "share";

    /**
     * User
     */

    public static final String FACEBOOK_ID = "FACEBOOK_ID";
    public static final String LAST_APP_OPENED_TIME = "LAST_APP_OPENED_TIME";
    public static final String PHONE_ID = "PHONE_ID";
    public static final String GOOGLE_ID = "GOOGLE_ID";
    public static final String USER_ID = "USER_ID";
    public static final String USER_NAME = "USER_NAME";
    public static final String USER_PHONE = "USER_PHONE";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_PASSWORD = "password";
    public static final String USER_EMAIL_TO_VERIFY = "USER_EMAIL_TO_VERIFY";
    public static final String USER_PASSWORD_TO_VERIFY = "USER_PASSWORD_TO_VERIFY";
    public static final String USER_NAME_TO_VERIFY = "USER_NAME_TO_VERIFY";
    public static final String USER_ID_TO_VERIFY = "USER_ID_TO_VERIFY";
    public static final String USER_NO_DEVICE_TOKEN = "nodevicetoken"; // Don't Change

    /**
     * LANGUAGE
     */
    public static String LANGUAGE = "Language";
    public static String LANGUAGE_CODE = "Language";
    public static String LANGUAGE_COUNTRY_CODE ="Language_Country_Code";

    /**
     * Noti
     */
    public static final String NOTI_TOKEN = "NOTI_TOKEN";

    /**
     * Intent Key
     */
    public static final String INTENT__CAT_ID = "cat_id";
    public static final String INTENT__CAT_NAME = "cat_name";
    public static final String INTENT__COLOR_ID = "color_id";
    public static final String INTENT__COLOR_CODE = "color_code";
    public static final String INTENT__COLOR_NAME = "color_name";
    public static final String INTENT__WALLPAPER_NAME = "wallpaper_name";
    public static final String INTENT__WALLPAPER_ID = "wallpaper_id";
    public static final String INTENT__WALLPAPER_PARAM_HOLDER = "wallpaper_param_holder";
    public static final String INTENT__WALLPAPER_TYPE = "wallpaper_type";
    public static final String INTENT__WALLPAPER_FREE = "free";
    public static final String INTENT__WALLPAPER_PREMIUM = "premium";

    public static final String INTENT__LIVE_WALLPAPER_ID = "live_wallpaper_id";
    public static final String INTENT__LIVE_WALLPAPER_PATH = "live_wallpaper_path";
    public static final String VIDEO_URI = "video_uri";

    /**
     * Admob
     */
    public static final String CURRENT_ADMOB_AT_DOWNLOAD_INTERVAL_COUNT_KEY = "CURRENT_ADMOB_AT_DOWNLOAD_INTERVAL_COUNT_KEY";
    public static final String CURRENT_ADMOB_AT_SWIPE_COUNT_KEY = "CURRENT_ADMOB_AT_SWIPE_COUNT_KEY";

    /**
     * Claim Points
     */
    public static final String CURRENT_REWARD_POINT_1_DAILY_COUNT_KEY = "CURRENT_REWARD_POINT_1_DAILY_COUNT_KEY";
    public static final String CURRENT_REWARD_POINT_1_DAILY_DATE_KEY = "CURRENT_REWARD_POINT_1_DAILY_DATE_KEY";
    public static final String CURRENT_REWARD_POINT_2_DAILY_COUNT_KEY = "CURRENT_REWARD_POINT_2_DAILY_COUNT_KEY";
    public static final String CURRENT_REWARD_POINT_2_DAILY_DATE_KEY = "CURRENT_REWARD_POINT_2_DAILY_DATE_KEY";

    /**
     * General
     */
    public static final String EMPTY_STRING = "";
    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String TWO = "2";
    public static final String THREE = "3";

    /**
     * Result Code
     */
    public static final int RESULT_CODE__SEARCH_WITH_CATEGORY = 2001;
    public static final int RESULT_CODE__SEARCH_WITH_COLOR = 3001;
    public static final int REQUEST_CODE__SEARCH_FRAGMENT = 1001;
    public static final int REQUEST_CODE__GOOGLE_SIGN = 1002;
    public static final int RESULT_CODE__WALLPAPER_FREE = 4001;
    public static final int RESULT_CODE__WALLPAPER_PREMIUM = 5001;

    /**
     * Play Store
     */
    public static final String PLAYSTORE_MARKET_URL_FIX = "market://details?id=";
    public static final String PLAYSTORE_HTTP_URL_FIX = "http://play.google.com/store/apps/details?id=";

    /**
     * App Info
     */
    //AppInfo Delete
    public static final String APPINFO_START_DATE = "SHOP_START_DATE";
    public static final String APPINFO_END_DATE = "SHOP_END_DATE";
    public static final String APPINFO_FORCE_UPDATE_MSG = "APPINFO_FORCE_UPDATE_MSG";
    public static final String APPINFO_FORCE_UPDATE_TITLE = "APPINFO_FORCE_UPDATE_TITLE";
    public static final String APPINFO_PREF_VERSION_NO = "APPINFO_PREF_VERSION_NO";
    public static final String APPINFO_PREF_FORCE_UPDATE = "APPINFO_PREF_FORCE_UPDATE";


    /**
     * Grid Type
     */
    public enum LAYOUT_TYPE {GRID_LAYOUT, PINTEREST_LAYOUT}
}
