package in.jatindhankhar.shorl.service;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import in.jatindhankhar.shorl.BuildConfig;
import in.jatindhankhar.shorl.sync.SyncAdapter;
import in.jatindhankhar.shorl.ui.LoginActivity;
import in.jatindhankhar.shorl.ui.MainActivity;

/**
 * Created by jatin on 12/31/16.
 */

public class SyncService extends Service {
    public static final String TAG = SyncService.class.getSimpleName();
    private static SyncAdapter mSyncAdapter;
    private static final Object mSyncAdapterLock = new Object();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"Starting SyncService");
        //android.os.Debug.waitForDebugger();

        synchronized (mSyncAdapterLock) {
            if (mSyncAdapter == null)
                mSyncAdapter = new SyncAdapter(this, true);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mSyncAdapter.getSyncAdapterBinder();
    }
}
