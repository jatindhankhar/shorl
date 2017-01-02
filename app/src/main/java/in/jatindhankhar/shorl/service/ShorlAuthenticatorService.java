package in.jatindhankhar.shorl.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import in.jatindhankhar.shorl.ui.ShorlAuthenticator;

/**
 * Created by jatin on 12/22/16.
 */

public class ShorlAuthenticatorService extends Service {

    private ShorlAuthenticator mAuthenticator;

    @Override
    public void onCreate() {
        super.onCreate();
        mAuthenticator = new ShorlAuthenticator(this);
    }

    @Nullable

    @Override
    public IBinder onBind(Intent intent) {
        return  mAuthenticator.getIBinder();
    }
}
