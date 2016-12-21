package in.jatindhankhar.shorl.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.jatindhankhar.shorl.R;
import in.jatindhankhar.shorl.model.AsyncResponse;
import in.jatindhankhar.shorl.model.HistoryItem;
import in.jatindhankhar.shorl.network.GetToken;
import in.jatindhankhar.shorl.network.GooglClient;
import in.jatindhankhar.shorl.network.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, AsyncResponse {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "MainActivity";
    public static final String URL_SHORTNER_SCOPE = "oauth2: https://www.googleapis.com/auth/urlshortener";
    private static final int RECOVERABLE_REQUEST_CODE = 101;
    @BindView(R.id.sign_in_button)
    SignInButton signInButton;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    @BindView(R.id.hello_text)
    TextView helloText;
    private GoogleApiClient mGoogleApiClient;
    final private String URL_API = "https://www.googleapis.com/auth/urlshortener";
    final private Scope URL_SCOPE= new Scope(URL_API);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().requestScopes(new Scope(Scopes.PROFILE),URL_SCOPE).requestId()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }


    @OnClick(R.id.sign_in_button)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            try {
                handleSignInResult(result);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleSignInResult(GoogleSignInResult result) throws IOException, GoogleAuthException {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            helloText.setText(acct.getDisplayName());
            Log.d(TAG, "Display Name " + acct.getDisplayName());
            Log.d(TAG,"Email " + acct.getEmail());
            Log.d(TAG,"Id " + acct.getId());
            GetToken g = new GetToken(this,acct.getAccount(),URL_SHORTNER_SCOPE,this);

            Log.d(TAG,"About to get value");
             g.execute();


            //updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            //updateUI(false);
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Yolopad", "Connection Failed");
    }

    @Override
    public void processFinish(String output) {
        Log.d(TAG,"AsyncTask Response token is " + output);
        GooglClient googlClient = ServiceGenerator.createService(GooglClient.class,output);
        googlClient.displayUser().enqueue(new Callback<List<HistoryItem>>() {
            @Override
            public void onResponse(Call<List<HistoryItem>> call, Response<List<HistoryItem>> response) {
                Log.d(TAG,response.isSuccessful() + "");

            }

            @Override
            public void onFailure(Call<List<HistoryItem>> call, Throwable t) {

            }
        });
    }
}

