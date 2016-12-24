package in.jatindhankhar.shorl.utils;

import in.jatindhankhar.shorl.BuildConfig;

/**
 * Created by jatin on 12/23/16.
 */

public final  class Constants {
    private Constants() { } // To avoid Instantion
    public static final String PREF_FILE = "ShorlPref"; // TODO - Set name programmatically
    public static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    public static final String ACCOUNT_NAME = "ACCOUNT_NAME";
    public static final String URL_SHORTNER_SCOPE = "oauth2: https://www.googleapis.com/auth/urlshortener";
    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
    public final static String URL_API = "https://www.googleapis.com/auth/urlshortener";
    public final static int RC_SIGN_IN = 9001;
    public final static String PACKAGE_NAME = BuildConfig.APPLICATION_ID;
}
