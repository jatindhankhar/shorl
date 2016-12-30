package in.jatindhankhar.shorl.ui;

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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.SignInButton;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.jatindhankhar.shorl.R;
import in.jatindhankhar.shorl.model.Analytics;
import in.jatindhankhar.shorl.model.CountData;
import in.jatindhankhar.shorl.model.DetailedHistoryResponse;
import in.jatindhankhar.shorl.model.ExpandUrlResponse;
import in.jatindhankhar.shorl.model.HistoryItem;
import in.jatindhankhar.shorl.model.HistoryResponse;
import in.jatindhankhar.shorl.model.NewUrl;
import in.jatindhankhar.shorl.network.GooglClient;
import in.jatindhankhar.shorl.network.ServiceGenerator;
import in.jatindhankhar.shorl.utils.Constants;
import in.jatindhankhar.shorl.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    private AccountManager mAccountManager;
    private Context mContext;
    @BindView(R.id.sign_in_button)
    SignInButton signInButton;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    @BindView(R.id.hello_text)
    TextView helloText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mContext = this;
        // If not logged in ask user to Login
        if(!Utils.isLoggedIn(mContext))
        {
            startActivity(new Intent(this,LoginActivity.class));
        }
        ServiceGenerator serviceGenerator = new ServiceGenerator(MainActivity.this);


        GooglClient googlClient = ServiceGenerator.createService(GooglClient.class,Utils.getAuthToken(mContext));



        Log.d(TAG,"We are creating a new url now ");
        NewUrl newUrl = new NewUrl();
        newUrl.setLongUrl("https://reddit.com/r/programming");
      /*  googlClient.createUrl(newUrl).enqueue(new Callback<HistoryItem>() {
            @Override
            public void onResponse(Call<HistoryItem> call, Response<HistoryItem> response) {
                HistoryItem historyItem = response.body();
               // Log.d(TAG,"HistoryItem id is " + historyItem.getId());
            }

            @Override
            public void onFailure(Call<HistoryItem> call, Throwable t) {

            }
        }); */


        /* googlClient.processList().enqueue(new Callback<HistoryResponse>() {
            @Override
            public void onResponse(Call<HistoryResponse> call, Response<HistoryResponse> response) {
                if(response.isSuccessful())
                {
                List<HistoryItem> historyItems = response.body().getHistoryItems();
                for(HistoryItem historyItem : historyItems) {
                    Log.d(TAG," Url is " + historyItem.getId());
                }

                }
            }

            @Override
            public void onFailure(Call<HistoryResponse> call, Throwable t) {

            }
        }); */

        googlClient.processDetaiList("FULL").enqueue(new Callback<DetailedHistoryResponse>() {
            @Override
            public void onResponse(Call<DetailedHistoryResponse> call, Response<DetailedHistoryResponse> response) {
                if(response.isSuccessful())
                {
                    Log.d(TAG,"Successful");
                    List<ExpandUrlResponse> r = response.body().getHistoryItems();
                    for(ExpandUrlResponse expandUrlResponse  : r) {
                        if(expandUrlResponse != null)
                        Log.d(TAG," Url is " + expandUrlResponse.getId());
                        else
                            Log.d(TAG," Object is null :|");
                    }
                }
            }

            @Override
            public void onFailure(Call<DetailedHistoryResponse> call, Throwable t) {

            }
        });

        /* googlClient.processAnalytics("http://goo.gl/fbsS","FULL").enqueue(new Callback<ExpandUrlResponse>() {
            @Override
            public void onResponse(Call<ExpandUrlResponse> call, Response<ExpandUrlResponse> response) {
                if(response.isSuccessful())
                {
                    Log.d(TAG,"Successful");
                     Analytics analytics = response.body().getAnalytics();
                    List<CountData> r = analytics.getAllTime().getBrowsers();
                    for(CountData cd : r)
                    {
                        Log.d(TAG, "Id is " + cd.getId());
                    }

                }
            }

            @Override
            public void onFailure(Call<ExpandUrlResponse> call, Throwable t) {

            }
        }); */
    }
    }

