package in.jatindhankhar.shorl.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import in.jatindhankhar.shorl.utils.Constants;
import in.jatindhankhar.shorl.utils.Utils;




public class SplashActivity extends AppCompatActivity {
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();


        // Launch correct activity based on the user session state
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
