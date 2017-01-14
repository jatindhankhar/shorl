package in.jatindhankhar.shorl.network;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;

import java.io.IOException;

import in.jatindhankhar.shorl.utils.Constants;
import in.jatindhankhar.shorl.utils.Utils;
import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/**
 * Created by jatin on 12/27/16.
 */

public class TokenAuthenticator implements Authenticator {

    private Context mContext;
    private static String TAG = TokenAuthenticator.class.getSimpleName();

    public TokenAuthenticator(Context context)
    {
        this.mContext = context;
    }
    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        Log.d(TAG,"Re-authenticating requests");
        if (responseCount(response) >= 3) {
            return null; // If we've failed 3 times, give up. - in real life, never give up!!
        }
        else
        {
            try {
                if(mContext == null)
                    Log.d(TAG,"Context is null");
                String token = GoogleAuthUtil.getToken(mContext, Utils.getLoginEmail(mContext), Constants.URL_SHORTNER_SCOPE);
                Log.d(TAG,"New token is " + token);
                Utils.setAuthToken(mContext,token);
                return response.request().newBuilder()
                        .header("Authorization","Bearer " + Utils.getAuthToken(mContext) )
                        .build();
            } catch (GoogleAuthException e) {
                e.printStackTrace();
                return null;
            }

        }


    }




    // Thanks http://stackoverflow.com/a/34819354/3455743
    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }
    }
