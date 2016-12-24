package in.jatindhankhar.shorl.ui;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.stetho.Stetho;

import in.jatindhankhar.shorl.utils.Constants;
import in.jatindhankhar.shorl.utils.Utils;

import static android.R.attr.accountType;


public class SplashActivity extends AppCompatActivity {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

// Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

// Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(getBaseContext())
        );

// Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

// Initialize Stetho with the Initializer
        Stetho.initialize(initializer);

        mContext = getApplicationContext();


        // Launch correct activity bases on the user session state
        if(Utils.isLoggedIn(mContext))
        {
            startActivity(new Intent(this,MainActivity.class));}
        else
        {
            Intent intent = new Intent(this,LoginActivity.class);
            intent.putExtra(Constants.ARG_IS_ADDING_NEW_ACCOUNT, true);
            startActivity(new Intent(this,LoginActivity.class));}

        finish();

    }


}
