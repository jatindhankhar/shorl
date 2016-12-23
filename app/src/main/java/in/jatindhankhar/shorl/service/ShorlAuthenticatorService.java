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
    @Nullable

    @Override
    public IBinder onBind(Intent intent) {
        ShorlAuthenticator shorlAuthenticator = new ShorlAuthenticator(this);
        return  shorlAuthenticator.getIBinder();
    }
}
