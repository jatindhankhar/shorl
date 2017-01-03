package in.jatindhankhar.shorl.ui;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;



import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.jatindhankhar.shorl.R;
import in.jatindhankhar.shorl.database.UrlProvider;
import in.jatindhankhar.shorl.model.DetailedHistoryResponse;
import in.jatindhankhar.shorl.model.ExpandUrlResponse;
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
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.activity_main)
    CoordinatorLayout coordinatorLayout;


    private AccountManager mAccountManager;
    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        // If not logged in ask user to Login
        if (!Utils.isLoggedIn(mContext)) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        ServiceGenerator serviceGenerator = new ServiceGenerator(MainActivity.this);

        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        recyclerview.setAdapter(new ListAdapter());
        GooglClient googlClient = ServiceGenerator.createService(GooglClient.class, Utils.getAuthToken(mContext));


        Log.d(TAG, "We are creating a new url now ");
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
        }
        Account targetAccount = null;
        for (Account account : AccountManager.get(mContext).getAccountsByType("com.google")) {
            if (account.name.equals("dhankhar.jatin@gmail.com")) {
                targetAccount = account;
                break;
            }
        }
        Log.d(TAG, "Testing service");
        Log.d(TAG, "Account is " + targetAccount.name);
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(targetAccount, "in.jatindhankhar.shorl.database.UrlProvider", settingsBundle);

        /*Log.d(TAG,"Sync requested");*/
        googlClient.processDetaiList("FULL").enqueue(new Callback<DetailedHistoryResponse>() {
            @Override
            public void onResponse(Call<DetailedHistoryResponse> call, Response<DetailedHistoryResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Successful");
                    List<ExpandUrlResponse> r = response.body().getHistoryItems();
                    for (ExpandUrlResponse expandUrlResponse : r) {
                        if (expandUrlResponse != null)
                            Log.d(TAG, " Url is " + expandUrlResponse.getId());
                        else
                            Log.d(TAG, " Object is null :|");
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

        Cursor cursor = mContext.getContentResolver().query(UrlProvider.Urls.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() == 0) {
            Log.d(TAG, "Empty cursors");
        } else {
            ContentValues contentValues = new ContentValues();

            contentValues.put(Constants.COLUMN_KIND_URL, "yo");
            contentValues.put(Constants.COLUMN_SHORT_URL, "me");
            contentValues.put(Constants.COLUMN_LONG_URL, "why");
            contentValues.put(Constants.COLUMN_CREATED_DATE_URL, "nopw");
            contentValues.put(Constants.COLUMN_ANALYTICS_URL, "hah");
            contentValues.put(Constants.COLUMN_STATUS_URL, "23445");

            Log.d(TAG, " Insert new values");
            Uri cursor1 = mContext.getContentResolver().insert(UrlProvider.Urls.CONTENT_URI, contentValues);
        }


    }
}

