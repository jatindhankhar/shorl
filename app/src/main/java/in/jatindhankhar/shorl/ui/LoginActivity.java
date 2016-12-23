package in.jatindhankhar.shorl.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.jatindhankhar.shorl.R;
import in.jatindhankhar.shorl.model.AsyncResponse;
import in.jatindhankhar.shorl.network.GetToken;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, AsyncResponse {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = LoginActivity.class.getSimpleName();
    public static final String PREF_FILE = "ShorlPref"; // TODO - Set name programmatically
    public static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    public static final String ACCOUNT_NAME = "ACCOUNT_NAME";
    public static final String URL_SHORTNER_SCOPE = "oauth2: https://www.googleapis.com/auth/urlshortener";
    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";
    final private String URL_API = "https://www.googleapis.com/auth/urlshortener";
    final private Scope URL_SCOPE= new Scope(URL_API);
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.sign_in_button)
    SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    private Context mContext;
    private AccountManager mAccountManager;
    private String mAuthTokenType;
    public String mAccountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        mContext = getApplicationContext();
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestScopes(URL_SCOPE).requestId().requestId()
                .build();
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* Activity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Prepare the account manager
        mAccountManager = AccountManager.get(mContext);
        String accountName = getIntent().getStringExtra(ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null)
             mAuthTokenType ="Full access";
        signInButton.setSize(SignInButton.SIZE_WIDE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContext = getApplicationContext();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @OnClick(R.id.sign_in_button)
    public void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            mAccountName =  acct.getDisplayName();
            //Toast.makeText(mContext, "Server Auth Token --> " +acct.getServerAuthCode() , Toast.LENGTH_SHORT).show();
            //Log.d(TAG,"Server Auth Token --> " +acct.getServerAuthCode());
            //mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            //updateUI(true);
            new GetToken(this,acct.getAccount(),URL_SHORTNER_SCOPE,this).execute();
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    private void updateUI(boolean loginSuccess) {
        if(loginSuccess)
        {
            setLoginSession();
            startActivity(new Intent(mContext,MainActivity.class));
        }
        else
        {
            clearLoginSuccess();
        }
    }

    public void setLoginSession()
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_FILE,mContext.MODE_PRIVATE);
        sharedPreferences.edit().putString(ACCOUNT_NAME,mAccountName).apply();
        sharedPreferences.edit().putBoolean(IS_LOGGED_IN,true).apply();
    }

    public void clearLoginSuccess()
    {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(PREF_FILE,mContext.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(IS_LOGGED_IN,false).apply();
    }

    @Override
    public void processFinish(String output) {

        final Account account = new Account(mAccountName, "in.jatindhankhar.shorl");
        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, false)) {

            String authtokenType = mAuthTokenType;
            mAccountManager.addAccountExplicitly(account, "", null);
            mAccountManager.setAuthToken(account, authtokenType, output);
        } else {
            // Just reset the token
            mAccountManager.setAuthToken(account,mAuthTokenType,output);
        }
        setLoginSession();
        startActivity(new Intent(this,MainActivity.class));




    }
}
