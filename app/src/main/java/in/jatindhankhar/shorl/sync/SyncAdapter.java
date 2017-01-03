package in.jatindhankhar.shorl.sync;

import android.accounts.Account;
import android.app.Activity;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import in.jatindhankhar.shorl.model.DetailedHistoryResponse;
import in.jatindhankhar.shorl.model.ExpandUrlResponse;
import in.jatindhankhar.shorl.network.GooglClient;
import in.jatindhankhar.shorl.network.ServiceGenerator;
import in.jatindhankhar.shorl.ui.MainActivity;
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

    }
}
