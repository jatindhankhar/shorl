package in.jatindhankhar.shorl.utils;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jatin on 12/23/16.
 */

public final class Utils {

    public static boolean isLoggedIn(Context context)
    {

        return context.getSharedPreferences(Constants.PREF_FILE,context.MODE_PRIVATE).getBoolean(Constants.IS_LOGGED_IN,false);
    }

    public static void clearLoginSuccess(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_FILE,context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(Constants.IS_LOGGED_IN,false).apply();
    }

    public static void setLoginSession(Context context,String accountName)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_FILE,context.MODE_PRIVATE);
        sharedPreferences.edit().putString(Constants.ACCOUNT_NAME,accountName).apply();
        sharedPreferences.edit().putBoolean(Constants.IS_LOGGED_IN,true).apply();
    }

    public static String getLoginName(Context context)
    {
        return context.getSharedPreferences(Constants.PREF_FILE,context.MODE_PRIVATE).getString(Constants.ACCOUNT_NAME," ");
    }

    public static String getAuthToken(Context context)
    {
       // AccountManager.get(context).

        return null;
    }
}
