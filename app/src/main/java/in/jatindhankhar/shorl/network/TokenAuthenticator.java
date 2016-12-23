package in.jatindhankhar.shorl.network;

import android.accounts.AccountManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.Proxy;

/**
 * Created by jatin on 12/23/16.
 */

public class TokenAuthenticator implements Authenticator, GoogleApiClient.OnConnectionFailedListener {
    private Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private AccountManager mAccountManager;
    public static final String ACCOUNT_NAME = "ACCOUNT_NAME";
    public static final String URL_SHORTNER_SCOPE = "oauth2: https://www.googleapis.com/auth/urlshortener";
    final private String URL_API = "https://www.googleapis.com/auth/urlshortener";
    final private Scope URL_SCOPE= new Scope(URL_API);

    public TokenAuthenticator(Context context)
    {
        this.mContext = context;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestScopes(URL_SCOPE).requestId().requestId()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAccountManager = AccountManager.get(mContext);

    }

    @Override
    public Request authenticate(Proxy proxy, Response response) throws IOException {

        return null;
    }

    @Override
    public Request authenticateProxy(Proxy proxy, Response response) throws IOException {
        // Null indicates no attempt to authenticate.
        return null;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
