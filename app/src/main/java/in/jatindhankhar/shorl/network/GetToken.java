package in.jatindhankhar.shorl.network;

import android.accounts.Account;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;

import java.io.IOException;

/**
 * Created by jatin on 12/12/16.
 */

public class GetToken extends AsyncTask<Void,Void,String> {

    private static final String TAG =  GetToken.class.getSimpleName();
    private Context mAppContext;
    private Account mAccountName;
    private String AUTH_TOKEN_TYPE;

    public GetToken(Context mAppContext,Account mAccountName,String AUTH_TOKEN_TYPE)
    {
        this.mAppContext = mAppContext;
        this.mAccountName = mAccountName;
        this.AUTH_TOKEN_TYPE = AUTH_TOKEN_TYPE;
    }
    @Override
    protected String doInBackground(Void... params) {
     try {
            if(isCancelled())
            {
                Log.d(TAG,"doInBackground: task cancelled, so giving up on auth.");
                return null;
            }
         final String token = GoogleAuthUtil.getToken(mAppContext, mAccountName, AUTH_TOKEN_TYPE);
         Log.d(TAG,"So token is " + token);
         return token;
     } catch (UserRecoverableAuthException e) {
         e.printStackTrace();
     } catch (GoogleAuthException e) {
         e.printStackTrace();
     } catch (IOException e) {
         e.printStackTrace();
     }
        return null;
    }
}
