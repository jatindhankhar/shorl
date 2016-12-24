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
import in.jatindhankhar.shorl.utils.Constants;
import in.jatindhankhar.shorl.utils.Utils;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, AsyncResponse {


    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int ACCOUNT_CODE = 1601;

    final private Scope URL_SCOPE = new Scope(Constants.URL_API);
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
        String accountName = getIntent().getStringExtra(Constants.ARG_ACCOUNT_NAME);
        mAuthTokenType = getIntent().getStringExtra(Constants.ARG_AUTH_TYPE);
        chooseAccount();
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
        startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if (acct != null) {
                mAccountName =  acct.getDisplayName();
            }

            new GetToken(this,acct.getAccount(),Constants.URL_SHORTNER_SCOPE,this).execute();
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }

    private void updateUI(boolean loginSuccess) {
        if(loginSuccess)
        {
            Utils.setLoginSession(mContext,mAccountName);
            startActivity(new Intent(mContext,MainActivity.class));
        }
        else
        {
            Utils.clearLoginSuccess(mContext);
        }
    }





    @Override
    public void processFinish(String output) {

        final Account account = new Account(mAccountName, Constants.PACKAGE_NAME);
        if (getIntent().getBooleanExtra(Constants.ARG_IS_ADDING_NEW_ACCOUNT, false)) {

            String authtokenType = mAuthTokenType;
            mAccountManager.addAccountExplicitly(account, "", null);
            mAccountManager.setAuthToken(account, authtokenType, output);
        } else {
            // Just reset the token
            mAccountManager.setAuthToken(account,mAuthTokenType,output);
        }
        Utils.setLoginSession(mContext,mAccountName);
        startActivity(new Intent(this,MainActivity.class));




    }

    private void chooseAccount() {
        // use https://github.com/frakbot/Android-AccountChooser for
        // compatibility with older devices
        Intent intent = AccountManager.newChooseAccountIntent(null, null,
                new String[] { "com.google" }, false, null, null, null, null);
        startActivityForResult(intent, ACCOUNT_CODE);
    }

}
