package in.jatindhankhar.shorl.utils;

import in.jatindhankhar.shorl.BuildConfig;

/**
 * Created by jatin on 12/23/16.
 */

public final class Constants {


    public static final String PREF_FILE = "ShorlPref"; // TODO - Set name programmatically
    public static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    public static final String ACCOUNT_NAME = "ACCOUNT_NAME";
    public static final String ACCOUNT_EMAIL = "ACCOUNT_EMAIL";
    public static final String URL_SHORTNER_SCOPE = "oauth2: https://www.googleapis.com/auth/urlshortener";
    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
    public final static String AUTH_TOKEN = "AUTH_TOKEN";
    public final static String URL_API = "https://www.googleapis.com/auth/urlshortener";
    public final static int RC_SIGN_IN = 9001;
    public final static String PACKAGE_NAME = BuildConfig.APPLICATION_ID;
    public final static String TABLE_NAME = "urls";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_SHORT_URL = "short_url";
    public static final String COLUMN_LONG_URL = "long_url";
    public static final String COLUMN_CREATED_DATE_URL = "created_at";
    public static final String COLUMN_STATUS_URL = "status";
    public static final String COLUMN_KIND_URL = "kind";
    public static final String COLUMN_ANALYTICS_URL = "analytics";
    public static final String COLUMN_FAVOURITE = "favourite";
    public final static String DATABASE_NAME = "shorl";
    public final static String ARG_SHORT_URL = "arg_short_url";
    public final static String ARG_LONG_URL = "arg_long_url";
    public final static String ARG_CREATED_DATE = "arg_created_date";
    public final static String ARG_ANALYTICS_DATA = "arg_analytics_data";
    public final static String ARG_NEW_USER = "arg_new_user";
    public final static int DATABASE_VERSION = 3;
    public static final String ACTION_SYNC_FINISHED = PACKAGE_NAME + "SYNC_FINISHED";
    private Constants() {
    } // To avoid Instantion
}
