package in.jatindhankhar.shorl.ui;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.jatindhankhar.shorl.R;
import in.jatindhankhar.shorl.database.UrlProvider;
import in.jatindhankhar.shorl.model.NewUrl;
import in.jatindhankhar.shorl.network.GooglClient;
import in.jatindhankhar.shorl.network.ServiceGenerator;
import in.jatindhankhar.shorl.utils.Constants;
import in.jatindhankhar.shorl.utils.Utils;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 1;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.activity_main)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.search_view)
    MaterialSearchView searchView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;


    private AccountManager mAccountManager;
    private ListAdapter mListAdpater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        final Context mContext = this;
        // If not logged in ask user to Login
        if (!Utils.isLoggedIn(mContext)) {
            invokeLogin();
        }
        if(getIntent().getBooleanExtra(Constants.ARG_NEW_USER,false))
        {
            requestSync();
        }

        ServiceGenerator serviceGenerator = new ServiceGenerator(MainActivity.this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Shorl");
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mListAdpater = new ListAdapter(mContext, null);

        // Thanks to http://stackoverflow.com/a/25958900/3455743 it doesn't overrides onTouch Events of subviews :D
        mListAdpater.SetOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this,DetailActivity.class);
                Cursor cursor =  mListAdpater.getCursor();
                cursor.moveToPosition(position);
                intent.putExtra(Constants.ARG_SHORT_URL,cursor.getString(cursor.getColumnIndex(Constants.COLUMN_SHORT_URL)));
                intent.putExtra(Constants.ARG_LONG_URL,cursor.getString(cursor.getColumnIndex(Constants.COLUMN_LONG_URL)));
                intent.putExtra(Constants.ARG_CREATED_DATE,cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CREATED_DATE_URL)));
                intent.putExtra(Constants.ARG_ANALYTICS_DATA,cursor.getString(cursor.getColumnIndex(Constants.COLUMN_ANALYTICS_URL)));
                startActivity(intent);
            }
        });

        recyclerview.setAdapter(mListAdpater);
        GooglClient googlClient = ServiceGenerator.createService(GooglClient.class, Utils.getAuthToken(mContext));


        Log.d(TAG, "We are creating a new url now ");
        NewUrl newUrl = new NewUrl();
        newUrl.setLongUrl("https://reddit.com/r/programming");
        getSupportLoaderManager().initLoader(LOADER_ID, null, MainActivity.this);

        if( ! Utils.isConnected(mContext))
        {
           Snackbar sb = Snackbar.make(coordinatorLayout,"It looks like you are not connected. Some features requires Internet Connection",Snackbar.LENGTH_LONG);
           sb.setAction("Dismiss", new View.OnClickListener() {
               @Override
               public void onClick(View v) {

               }
           }).setActionTextColor(Color.YELLOW);

            // Enable multiline snack bar
            TextView tv= (TextView) sb.getView().findViewById(android.support.design.R.id.snackbar_text);
            tv.setMaxLines(3);


            sb.show();

            swipeRefreshLayout.setEnabled(false);

        }

        else
        {
            swipeRefreshLayout.setEnabled(true);
        }

      /*  googlClient.createUrl(newUrl).enqueue(new Callback<HistoryItem>() {
            @Override
            public void onResponse(Call<HistoryItem> call, Response<HistoryItem> response) {
                HistoryItem historyItem = response.body();
               // Log.d(TAG,"HistoryItem id is " + historyItem.getId());
            }

            @Override
            public void onFailure(Call<HistoryItem> call, Throwable t) {

            }
        }); */

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestSync();
                Snackbar sb = Snackbar.make(coordinatorLayout,"Syncing data. This may take some time depending upon the internet connection",Snackbar.LENGTH_INDEFINITE);
                sb.show();
                sb.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        swipeRefreshLayout.setRefreshing(false);

                    }
                }).setActionTextColor(Color.YELLOW);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, UrlProvider.Urls.CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mListAdpater.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mListAdpater.swapCursor(null);
    }



    private void requestSync()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //return;
            //ActivityCompat.requestPermissions();
            invokeLogin();
        }
        Account targetAccount = null;
        for (Account account : AccountManager.get(getBaseContext()).getAccountsByType("com.google")) {
            if (account.name.equals(Utils.getLoginEmail(getBaseContext()))) {
                targetAccount = account;
                break;
            }
        }

        if(targetAccount != null)
        {
            Log.d(TAG,"Requesting immediate sync");
            Bundle settingsBundle = new Bundle();
            settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
            settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL,true);
            settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED,true);
            ContentResolver.requestSync(targetAccount,"in.jatindhankhar.shorl.database.UrlProvider",settingsBundle);
        }

        else
        {
            invokeLogin();
        }
    }

    private  void invokeLogin()
    {
        startActivity(new Intent(this, LoginActivity.class));
    }
}

