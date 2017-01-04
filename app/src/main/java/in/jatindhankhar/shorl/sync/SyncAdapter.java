package in.jatindhankhar.shorl.sync;

import android.accounts.Account;
import android.app.Activity;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import in.jatindhankhar.shorl.database.UrlDatabase;
import in.jatindhankhar.shorl.database.UrlProvider;
import in.jatindhankhar.shorl.model.DetailedHistoryResponse;
import in.jatindhankhar.shorl.model.ExpandUrlResponse;
import in.jatindhankhar.shorl.network.GooglClient;
import in.jatindhankhar.shorl.network.ServiceGenerator;
import in.jatindhankhar.shorl.ui.MainActivity;
import in.jatindhankhar.shorl.utils.Constants;
import in.jatindhankhar.shorl.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jatin on 12/31/16.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = SyncAdapter.class.getSimpleName();
    ContentResolver mContentResolver;
    ServiceGenerator serviceGenerator;
    String mAuthToken;

    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        serviceGenerator= new ServiceGenerator(getContext().getApplicationContext());
        mAuthToken = Utils.getAuthToken(context);
        mContentResolver = context.getContentResolver();

    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
    Log.d(TAG,"We about to start some real stuff");
       GooglClient googlClient  = ServiceGenerator.createService(GooglClient.class,mAuthToken);
    Log.d(TAG,"Starting Auth Service");
        googlClient.processDetaiList("FULL").enqueue(new Callback<DetailedHistoryResponse>() {
            @Override
            public void onResponse(Call<DetailedHistoryResponse> call, Response<DetailedHistoryResponse> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Successful");
                    ArrayList<ContentValues> contentValues = new ArrayList<ContentValues>();
                    Gson gson = new GsonBuilder().create();
                    List<ExpandUrlResponse> r = response.body().getHistoryItems();
                    for (ExpandUrlResponse expandUrlResponse : r) {

                        {
                            ContentValues cv = new ContentValues();
                            cv.put(Constants.COLUMN_KIND_URL, expandUrlResponse.getKind());
                            cv.put(Constants.COLUMN_SHORT_URL, expandUrlResponse.getId());
                            cv.put(Constants.COLUMN_LONG_URL, expandUrlResponse.getLongUrl());
                            cv.put(Constants.COLUMN_CREATED_DATE_URL, expandUrlResponse.getCreated());
                            cv.put(Constants.COLUMN_ANALYTICS_URL,gson.toJson(expandUrlResponse.getAnalytics()));
                            cv.put(Constants.COLUMN_STATUS_URL, expandUrlResponse.getStatus());
                            contentValues.add(cv);
                            cv.clear();
                            Log.d(TAG, " Url is " + expandUrlResponse.getId());

                        }

                        // Bulk Insert values
                        int count  = mContentResolver.query(UrlProvider.Urls.CONTENT_URI, null, null, null, null).getCount();
                        if ( count > 0 )
                        {
                            // If not empty clear all rows
                            int res = mContentResolver.delete(UrlProvider.Urls.CONTENT_URI, "1", null);
                            Log.d(TAG,"Deleted " + res + " rows");
                        }

                        int res = mContentResolver.bulkInsert(UrlProvider.Urls.CONTENT_URI, contentValues.toArray(new ContentValues[contentValues.size()]));
                        Log.d(TAG,"Inserted " + res + " rows");
                    }

                }
            }

            @Override
            public void onFailure(Call<DetailedHistoryResponse> call, Throwable t) {
                Log.d(TAG,"Failed to sync");
            }
        });

    }
}
