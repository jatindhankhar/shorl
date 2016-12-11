package in.jatindhankhar.shorl.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class SplashActivity extends AppCompatActivity {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        // Launch correct acitivity bases on the user session state
        if(isLoggedIn())
            startActivity(new Intent(this,LoginActivity.class));
        else
        startActivity(new Intent(this,MainActivity.class));

        finish();

    }

    private boolean isLoggedIn()
    {
        return mContext.getSharedPreferences(LoginActivity.PREF_FILE,mContext.MODE_PRIVATE).getBoolean(LoginActivity.IS_LOGGED_IN,false);
    }
}
