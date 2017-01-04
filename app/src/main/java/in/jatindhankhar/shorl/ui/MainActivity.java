package in.jatindhankhar.shorl.ui;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.jatindhankhar.shorl.R;
import in.jatindhankhar.shorl.database.UrlProvider;
import in.jatindhankhar.shorl.model.NewUrl;
import in.jatindhankhar.shorl.network.GooglClient;
import in.jatindhankhar.shorl.network.ServiceGenerator;
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


    private AccountManager mAccountManager;
    private ListAdapter mListAdpater;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Context mContext = this;
        // If not logged in ask user to Login
        if (!Utils.isLoggedIn(mContext)) {
            startActivity(new Intent(this, LoginActivity.class));
        }
        ServiceGenerator serviceGenerator = new ServiceGenerator(MainActivity.this);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Shorl");
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mListAdpater = new ListAdapter(getApplicationContext(), null);
        recyclerview.setAdapter(mListAdpater);
        GooglClient googlClient = ServiceGenerator.createService(GooglClient.class, Utils.getAuthToken(mContext));


        Log.d(TAG, "We are creating a new url now ");
        NewUrl newUrl = new NewUrl();
        newUrl.setLongUrl("https://reddit.com/r/programming");
        getSupportLoaderManager().initLoader(LOADER_ID, null, MainActivity.this);


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


}

