package in.jatindhankhar.shorl.utils;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.text.format.DateUtils;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import in.jatindhankhar.shorl.ui.LoginActivity;

import static in.jatindhankhar.shorl.service.SyncService.TAG;

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

    public static String getGooglShortUrl(@NonNull String shortUrl)
    {
        URL targetUrl= null;
        try {
            targetUrl = new URL(shortUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        if(targetUrl == null)
            return null;
        else
            return targetUrl.getHost() + targetUrl.getPath();
    }

    public static String getReadbleDate(@NonNull  String createdDate)
    {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.getDefault());
        Date date = null;
        try {
            date = sdf.parse(createdDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        sdf.setTimeZone(TimeZone.getDefault());
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        DateFormat targetFormat = new SimpleDateFormat("d MMM, yyyy", Locale.getDefault());
        return targetFormat.format(cal.getTime());
    }

    // Thanks to http://stackoverflow.com/a/24339774/3455743
    public static String getRelativeTime(@NonNull  String createdDate) {
        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SS", Locale.getDefault());
        Date fromdate = null;
        try {
            fromdate = sdf.parse(createdDate);
        } catch (ParseException e) {
            e.printStackTrace();
            FirebaseCrash.logcat(Log.ERROR, TAG, "UnParsable Date");
            FirebaseCrash.report(e);
            return " ";
        }
        long then;
        then = fromdate.getTime();
        Date date = new Date(then);

        StringBuffer dateStr = new StringBuffer();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Calendar now = Calendar.getInstance();

        int days = daysBetween(calendar.getTime(), now.getTime());
        int minutes = hoursBetween(calendar.getTime(), now.getTime());
        int hours = minutes / 60;
        if (days == 0) {

            int second = minuteBetween(calendar.getTime(), now.getTime());
            if (minutes > 60) {

                if (hours >= 1 && hours <= 24) {
                    dateStr.append(hours).append(" hours ago");
                }

            } else {

                if (second <= 10) {
                    dateStr.append("Now");
                } else if (second > 10 && second <= 30) {
                    dateStr.append("few seconds ago");
                } else if (second > 30 && second <= 60) {
                    dateStr.append(second).append(" seconds ago");
                } else if (second >= 60 && minutes <= 60) {
                    dateStr.append(minutes).append(" minutes ago");
                }
            }
        } else

        if (hours > 24 ) {
            dateStr.append(days).append(" days ago");
        }

        else {
           return getReadbleDate(createdDate);
            // ;
        }

        return dateStr.toString();
    }

    public static int minuteBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / DateUtils.SECOND_IN_MILLIS);
    }

    public static int hoursBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / DateUtils.MINUTE_IN_MILLIS);
    }

    public static int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / DateUtils.DAY_IN_MILLIS);
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            // notify user you are online
            return true;

        } else if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {
            return false;
        }
        return false;
    }

    // Thanks to
    public static String getISO8601StringForCurrentDate() {
    Date now = new Date();
    return getISO8601StringForDate(now);
    }


    private static String getISO8601StringForDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

}
