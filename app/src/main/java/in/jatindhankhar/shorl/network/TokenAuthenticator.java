package in.jatindhankhar.shorl.network;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

import in.jatindhankhar.shorl.ui.LoginActivity;
import in.jatindhankhar.shorl.ui.MainActivity;
import in.jatindhankhar.shorl.utils.Constants;
import in.jatindhankhar.shorl.utils.Utils;

import static android.support.v4.app.ActivityCompat.startActivityForResult;

/**
 * Created by jatin on 12/23/16.
 */

public class TokenAuthenticator implements Authenticator {
    private static final  int AUTHORIZATION_CODE = 1998;
    private Context mContext;
    private AccountManager mAccountManager;
    private static String TAG = TokenAuthenticator.class.getSimpleName();

    public TokenAuthenticator(Context context) {
        this.mContext = context;
        mAccountManager = AccountManager.get(mContext);
    }

    @Override
    public Request authenticate(Proxy proxy, Response response) throws IOException {
        if (Utils.isLoggedIn(mContext) && !Utils.getLoginEmail(mContext).isEmpty()) // If logged in and email not empty
        {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            Log.d(TAG," Old Auth Token : " + Utils.getAuthToken(mContext));
            String email = Utils.getLoginEmail(mContext);
            Account targetAccount = null;
            for (Account account : mAccountManager.getAccountsByType("com.google"))
            {
                if(account.name.equals(email))
                {
                    targetAccount = account;
                    break;
                }
            }

            if(targetAccount == null)
            {
                Toast.makeText(mContext, "Account Manager is null", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(mContext, "Account Manager not null " + targetAccount.name, Toast.LENGTH_SHORT).show();
                mAccountManager.getAuthToken(targetAccount,  Constants.URL_SHORTNER_SCOPE, null, (Activity) mContext,
                        new OnTokenAcquired(), null);
                Log.d(TAG," Old Auth Token : " + Utils.getAuthToken(mContext));

                // Add new header to rejected request and retry it
                return response.request().newBuilder()
                        .addHeader("Authorization","Bearer " + Utils.getAuthToken(mContext) )
                        .build();

            }
        }
        else
        {
            // Start Login Activity
            ((Activity)mContext).startActivity(new Intent(mContext,LoginActivity.class));
            return null;
        }

        return null;
    }

    @Override
    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        // Null indicates no attempt to authenticate.
        return null;
    }


    private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> future) {
            try {
                Bundle bundle = future.getResult();
                Intent launch = (Intent) bundle.get(AccountManager.KEY_INTENT);
                if (launch != null)
                {

                    ((Activity)mContext).startActivityForResult(launch,AUTHORIZATION_CODE);

                }
                else
                {
                    String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                    Log.d(TAG,"New token is " + token);
                    Utils.setAuthToken(mContext,token);
                    Log.d(TAG,"Stored token is " +Utils.getAuthToken(mContext));
                    Log.d(TAG,"New email is "+ Utils.getLoginEmail(mContext));
                }
            } catch (OperationCanceledException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (AuthenticatorException e) {
                e.printStackTrace();
            }
        }
    }
}
