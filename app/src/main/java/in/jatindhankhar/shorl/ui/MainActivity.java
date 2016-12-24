package in.jatindhankhar.shorl.ui;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.SignInButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.jatindhankhar.shorl.R;


public class MainActivity extends AppCompatActivity {

    private static final int ACCOUNT_CODE = 1601;

    private AccountManager mAccountManager;
    private Context mContext;
    @BindView(R.id.sign_in_button)
    SignInButton signInButton;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;
    @BindView(R.id.hello_text)
    TextView helloText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        //TODO Add check if loggged in , if not open up LoginActivity again
        mContext = getApplicationContext();
        // String accountName =
        //String accountName =
        //mAccountManager = AccountManager.get(mContext).peekAuthToken()
        //chooseAccount();


    }

    private void chooseAccount() {
        // use https://github.com/frakbot/Android-AccountChooser for
        // compatibility with older devices
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                new String[] {"com.google", "com.google.android.legacyimap"},
                false, null, null, null, null);
        startActivityForResult(intent,23);
    }


    @OnClick(R.id.sign_in_button)
    public void onClick() {
        Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();
        chooseAccount();
    }
}

