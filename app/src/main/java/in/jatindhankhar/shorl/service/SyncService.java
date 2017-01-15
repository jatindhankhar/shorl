package in.jatindhankhar.shorl.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import in.jatindhankhar.shorl.sync.SyncAdapter;

/**
 * Created by jatin on 12/31/16.
 */

public class SyncService extends Service {
    public static final String TAG = SyncService.class.getSimpleName();
    private static final Object mSyncAdapterLock = new Object();
    private static SyncAdapter mSyncAdapter;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Starting SyncService");
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
