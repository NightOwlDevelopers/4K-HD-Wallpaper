package com.panaceasoft.pswallpaper;

import com.panaceasoft.pswallpaper.utils.Constants;

/**
 * Created by Panacea-Soft on 7/15/15.
 * Contact Email : teamps.is.cool@gmail.com
 */

public class Config {

    /**
     * AppVersion
     * Current App Version
     */
    public static String APP_VERSION = "2.8";

    /**
     * APP Setting
     * It will turn off logging
     */
    public static boolean IS_DEVELOPMENT = true; // set false, your app is production


    /**
     * API Related
     * Change your API URL Here
     */
    public static final String APP_API_URL = "http://www.panacea-soft.com/pswallpapers-admin/index.php/";
    public static final String APP_IMAGES_URL = "http://www.panacea-soft.com/pswallpapers-admin/uploads/";
    public static final String APP_IMAGES_THUMB_URL = "http://www.panacea-soft.com/pswallpapers-admin/uploads/thumbnail/";


    /**
     * API KEY
     * *** If you change here, you need to update in server ****
     */
    public static final String API_KEY = "teampsisthebest";

    /**
     * For default language change, please check
     * LanguageFragment for language code and country code
     * ..............................................................
     * Language             | Language Code     | Country Code
     * ..............................................................
     * "English"            | "en"              | ""
     * "Arabic"             | "ar"              | ""
     * "Chinese (Mandarin)" | "zh"              | ""
     * "French"             | "fr"              | ""
     * "German"             | "de"              | ""
     * "India (Hindi)"      | "hi"              | "rIN"
     * "Indonesian"         | "in"              | ""
     * "Italian"            | "it"              | ""
     * "Japanese"           | "ja"              | ""
     * "Korean"             | "ko"              | ""
     * "Malay"              | "ms"              | ""
     * "Portuguese"         | "pt"              | ""
     * "Russian"            | "ru"              | ""
     * "Spanish"            | "es"              | ""
     * "Thai"               | "th"              | ""
     * "Turkish"            | "tr"              | ""
     * ..............................................................
     */
    public static final String LANGUAGE_CODE = "en";
    public static final String DEFAULT_LANGUAGE_COUNTRY_CODE = "";
    public static final String DEFAULT_LANGUAGE = LANGUAGE_CODE;


    /**
     * Admob Setting
     */
    public static final Boolean SHOW_ADMOB = true;
    public static final Boolean SHOW_ADMOB_AT_DOWNLOAD = true;
    public static final int SHOW_ADMOB_AT_DOWNLOAD_INTERVAL_COUNT = 5;

    public static final Boolean SHOW_ADMOB_AT_SWIPE = true;
    public static final int SHOW_ADMOB_AT_SWIPE_COUNT = 10;


    /**
     * Claim Points
     */
    public static final int REWARD_INIT = 0;

    public static final String DAILY_POINT = "100";
    public static final boolean SHOW_DAILY_POINT = true;

    public static final String REWARD_POINT_1 = "200";
    public static final boolean SHOW_REWARD_POINT_1 = true;
    public static final int REWARD_POINT_1_DAILY_LIMIT = 6;

    public static final String REWARD_POINT_2 = "300";
    public static final boolean SHOW_REWARD_POINT_2 = true;
    public static final int REWARD_POINT_2_DAILY_LIMIT = 3;

    /**
     * Loading Limit Count Setting
     */
    public static final int API_SERVICE_CACHE_LIMIT = 5; // Minutes Cache

    public static final int ALL_WALLPAPERS_COUNT = 20;

    public static final int TRENDING_COUNT = 20;

    public static final int CATEGORY_COUNT = 30;

    public static final int COLOR_COUNT = 30;

    public static final int LATEST_COUNT = 20;

    public static final int UPLOAD_PHOTO_COUNT = 30;

    public static final int PORTRAIT_COUNT = 20;

    public static final int LANDSCAPE_COUNT = 20;

    public static final int SQUARE_COUNT = 20;

    public static final int FAVORITE_COUNT = 30;

    public static final int DOWNLOADED_COUNT = 30;

    public static final int FEATURE_COUNT = 20;

    public static final int ALL_WALLPAPERS_COUNT_BY_CATEGORY = 30;

    public static final int ALL_WALLPAPERS_CATEGORY = 30;

    public static final int PAGER_COUNT = 30;

    public static final String ZERO_COUNT = "0";

    public static final int GIF_COUNT = 20;

    //Minimum LiveWallpaper you want to buffer while Playing
    public static final int MIN_BUFFER_DURATION = 3000;
    //Max LiveWallpaper you want to buffer during PlayBack
    public static final int MAX_BUFFER_DURATION = 5000;
    //Min LiveWallpaper you want to buffer before start Playing it
    public static final int MIN_PLAYBACK_START_BUFFER = 1500;
    //Min video You want to buffer when user resumes video
    public static final int MIN_PLAYBACK_RESUME_BUFFER = 5000;

    /**
     * Cache Limit
     */
    public static final int MAXIMUMWALLPAPERCOUNT = 1000;


    /**
     * Image Cache and Loading
     */
    public static int IMAGE_CACHE_LIMIT = 1; // Mb
    public static boolean PRE_LOAD_FULL_IMAGE = true;


    /**
     * GDPR For Admob And Policy URL
     */
    public static String CONSENTSTATUS_PERSONALIZED = "PERSONALIZED";
    public static String CONSENTSTATUS_NON_PERSONALIZED = "NON_PERSONALIZED";
    public static String CONSENTSTATUS_UNKNOWN = "UNKNOWN";
    public static String CONSENTSTATUS_CURRENT_STATUS = "UNKNOWN";
    public static String CONSENTSTATUS_IS_READY_KEY = "CONSENTSTATUS_IS_READY";
    public static String POLICY_URL = "http://www.panacea-soft.com/policy/policy.html";



    /**
     * Allow User To Upload Wallpaper
     * "1" = Yes Allow
     * "0" = No Don't Allow
     */
    public static final boolean ENABLE_UPLOAD_WALLPAPER = true;

    /**
     * Show or Hide Gif
     */
    public static boolean ENABLE_GIF = true;

    /**
     * Show or Hide Live Wallpaper
     */
    public static boolean ENABLE_LIVE_WALLPAPER = true;

    /**
     * Facebook login Config
     */
    public static boolean ENABLE_FACEBOOK_LOGIN = true;

    /**
     * Google login Config
     */
    public static boolean ENABLE_GOOGLE_LOGIN = true;

    /**
     * Phone login Config
     */
    public static boolean ENABLE_PHONE_LOGIN = true;

    /**
     * Show or Hide Premium
     */
    public static boolean ENABLE_PREMIUM = true;

    /**
     * Layout Types
     * Constants.LAYOUT_TYPE.GRID_LAYOUT,
     * Constants.LAYOUT_TYPE.PINTEREST_LAYOUT
     */
    public static Constants.LAYOUT_TYPE APP_GRID_LAYOUT = Constants.LAYOUT_TYPE.PINTEREST_LAYOUT;

    /**
     * CELL HEIGHT
     * This property is only for "Constants.LAYOUT_TYPE.GRID_LAYOUT"
     */
    public static int GRID_CELL_HEIGHT = 150;

    /**
     * MIN_COLUMN_COUNT
     * How many column you want to show in app grid.
     */
    public static int MIN_COLUMN_COUNT = 2;

}
