package in.jatindhankhar.shorl.utils;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import in.jatindhankhar.shorl.ui.LoginActivity;

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
        sharedPreferences.edit().remove(Constants.ACCOUNT_NAME).apply();
        sharedPreferences.edit().remove(Constants.AUTH_TOKEN).apply();
        sharedPreferences.edit().remove(Constants.ACCOUNT_EMAIL).apply();
    }

    public static void setLoginSession(Context context,String accountName,String accountEmail)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_FILE,context.MODE_PRIVATE);
        sharedPreferences.edit().putString(Constants.ACCOUNT_NAME,accountName).
                putBoolean(Constants.IS_LOGGED_IN,true).
                putString(Constants.ACCOUNT_EMAIL,accountEmail).apply();
    }

    public static void setAuthToken(Context context,String authToken)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.PREF_FILE,context.MODE_PRIVATE);
        sharedPreferences.edit().putString(Constants.AUTH_TOKEN,authToken).apply();
    }

    public static String getLoginName(Context context)
    {
        return context.getSharedPreferences(Constants.PREF_FILE,context.MODE_PRIVATE).getString(Constants.ACCOUNT_NAME,"");
    }

    public static String getLoginEmail(Context context)
    {
        return  context.getSharedPreferences(Constants.PREF_FILE,context.MODE_PRIVATE).getString(Constants.ACCOUNT_EMAIL,"");
    }

    public static String getAuthToken(Context context)
    {
        return context.getSharedPreferences(Constants.PREF_FILE,context.MODE_PRIVATE).getString(Constants.AUTH_TOKEN,"");
    }


}
