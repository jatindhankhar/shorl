package in.jatindhankhar.shorl.ui;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import in.jatindhankhar.shorl.R;
import in.jatindhankhar.shorl.database.UrlProvider;
import in.jatindhankhar.shorl.model.ExpandUrlResponse;
import in.jatindhankhar.shorl.model.HistoryItem;
import in.jatindhankhar.shorl.model.NewUrl;
import in.jatindhankhar.shorl.network.GooglClient;
import in.jatindhankhar.shorl.network.ServiceGenerator;
import in.jatindhankhar.shorl.utils.Constants;
import in.jatindhankhar.shorl.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int LOADER_ID = 1;
    private static IntentFilter syncIntentFilter = new IntentFilter(Constants.ACTION_SYNC_FINISHED);
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
    @BindView(R.id.fab_add)
    FloatingActionButton floatingActionButton;
    @BindView(R.id.input_box)
    View inputBox;
    @BindView(R.id.cleartext)
    Button clearText;
    @BindView(R.id.url_input)
    AppCompatEditText inputField;
    @BindView(R.id.submit)
    ImageView submitButton;
    @BindView(R.id.empty_error_layout)
    View emptyErrorLayout;

    private AccountManager mAccountManager;
    private ListAdapter mListAdpater;
    private GooglClient googlClient;
    private ContentObserver mContentObserver;
    private RecyclerView.AdapterDataObserver dataObserver;
    private Snackbar sb;
    private BroadcastReceiver syncBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {


            swipeRefreshLayout.setRefreshing(false);

            if (sb != null) {
                sb.dismiss();

                sb.setText(R.string.sync_finished).show();
            }
        }
    };

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
        if (getIntent().getBooleanExtra(Constants.ARG_IS_ADDING_NEW_ACCOUNT, false)) {
            swipeRefreshLayout.setRefreshing(true);
            requestSync();
        }

        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        recyclerview.setHasFixedSize(true);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mListAdpater = new ListAdapter(mContext, null);
        dataObserver = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                setEmptyErrorLayout(mListAdpater.getItemCount() == 0);

            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                setEmptyErrorLayout(itemCount == 0);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                setEmptyErrorLayout(itemCount == 0);
            }
        };
        // Thanks to http://stackoverflow.com/a/25958900/3455743 it doesn't overrides onTouch Events of subviews :D
        mListAdpater.SetOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                Cursor cursor = mListAdpater.getCursor();
                cursor.moveToPosition(position);
                intent.putExtra(Constants.ARG_SHORT_URL, cursor.getString(cursor.getColumnIndex(Constants.COLUMN_SHORT_URL)));
                intent.putExtra(Constants.ARG_LONG_URL, cursor.getString(cursor.getColumnIndex(Constants.COLUMN_LONG_URL)));
                intent.putExtra(Constants.ARG_CREATED_DATE, cursor.getString(cursor.getColumnIndex(Constants.COLUMN_CREATED_DATE_URL)));
                intent.putExtra(Constants.ARG_ANALYTICS_DATA, cursor.getString(cursor.getColumnIndex(Constants.COLUMN_ANALYTICS_URL)));
                startActivity(intent);
            }
        });


        recyclerview.setAdapter(mListAdpater);
        mListAdpater.registerAdapterDataObserver(dataObserver);
        googlClient = ServiceGenerator.createService(GooglClient.class, Utils.getAuthToken(mContext));


        getSupportLoaderManager().initLoader(LOADER_ID, null, MainActivity.this);


        if (!Utils.isConnected(mContext)) {
            sb = Snackbar.make(coordinatorLayout, R.string.no_internet_message, Snackbar.LENGTH_LONG);
            sb.setAction(R.string.dismiss_action, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            }).setActionTextColor(Color.YELLOW);

            // Enable multiline snack bar
            TextView tv = (TextView) sb.getView().findViewById(android.support.design.R.id.snackbar_text);
            tv.setMaxLines(3);


            sb.show();

            swipeRefreshLayout.setEnabled(false);

        } else {
            swipeRefreshLayout.setEnabled(true);
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestSync();
                Snackbar sb = Snackbar.make(coordinatorLayout, R.string.sync_message, Snackbar.LENGTH_LONG);
                sb.show();
                sb.setAction(R.string.dismiss_action, new View.OnClickListener() {
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

    private void requestSync() {


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {

            // Do nothing
            FirebaseCrash.logcat(Log.ERROR, TAG, "Activity invoked without granting permissions");
        }
        Account targetAccount = null;
        for (Account account : AccountManager.get(getBaseContext()).getAccountsByType(Constants.PACKAGE_NAME)) {
            if (account.name.equals(Utils.getLoginName(getBaseContext()))) {
                targetAccount = account;
                break;
            }

        }

        if (targetAccount != null) {
            final Account finalTargetAccount = targetAccount;
            Runnable syncThread = new Runnable() {

                @Override
                public void run() {
                    Log.d(TAG, "Requesting immediate sync");
                    Bundle settingsBundle = new Bundle();
                    settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                    settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
                    settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
                    ContentResolver.requestSync(finalTargetAccount, UrlProvider.AUTHORITY, settingsBundle);
                }
            };

            syncThread.run();

        } else {
            invokeLogin();
        }
    }

    private void invokeLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }

    private void addNewUrl() {
        String userUrl = inputField.getText().toString();
        NewUrl newUrl = new NewUrl();
        newUrl.setLongUrl(userUrl);
        final ProgressDialog progressDialog = createProgressDialog();
        googlClient.createUrl(newUrl).enqueue(new Callback<HistoryItem>() {
            @Override
            public void onResponse(Call<HistoryItem> call, Response<HistoryItem> response) {
                if (response.isSuccessful()) {
                    HistoryItem historyItem = response.body();
                    final Gson gson = new GsonBuilder().create();
                    final String shortUrl = historyItem.getId();
                    final ContentValues cv = new ContentValues();
                    // We need to make two calls to API because first API doesn't return all the details and some details are required -_-
                    // Retrieve detail Analytics
                    googlClient.processAnalytics(shortUrl, "FULL").enqueue(new Callback<ExpandUrlResponse>() {
                        @Override
                        public void onResponse(Call<ExpandUrlResponse> call, Response<ExpandUrlResponse> response) {
                            if (response.isSuccessful()) {
                                ExpandUrlResponse ex = response.body();

                                cv.put(Constants.COLUMN_STATUS_URL, ex.getStatus());
                                cv.put(Constants.COLUMN_SHORT_URL, ex.getId());
                                cv.put(Constants.COLUMN_KIND_URL, ex.getKind());
                                cv.put(Constants.COLUMN_LONG_URL, ex.getLongUrl());
                                cv.put(Constants.COLUMN_CREATED_DATE_URL, ex.getCreated());
                                cv.put(Constants.COLUMN_ANALYTICS_URL, gson.toJson(ex.getAnalytics()));
                                getContentResolver().insert(UrlProvider.Urls.CONTENT_URI, cv);
                                FirebaseCrash.log("Added Short Url");
                                progressDialog.dismiss();
                                createAlertDialog(getResources().getString(R.string.new_url_create_success));
                                mListAdpater.notifyDataSetChanged();

                            }


                        }

                        @Override
                        public void onFailure(Call<ExpandUrlResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            createAlertDialog(getResources().getString(R.string.new_url_creation_error));
                        }
                    });

                    // Clear content value to avoid leak
                    cv.clear();
                } else {
                    progressDialog.dismiss();
                    createAlertDialog(getResources().getString(R.string.new_url_creation_error));
                }
            }

            @Override
            public void onFailure(Call<HistoryItem> call, Throwable t) {
                progressDialog.dismiss();
                createAlertDialog(getResources().getString(R.string.new_url_creation_error));
            }
        });
    }

    private boolean isInputVisible() {
        return inputBox.getVisibility() == View.VISIBLE;
    }

    // Thanks http://stackoverflow.com/a/35720930/3455743
    public void rotateFabForward() {
        ViewCompat.animate(floatingActionButton)
                .rotation(135.0F)
                .withLayer()
                .setDuration(500L)
                .setInterpolator(new OvershootInterpolator(1.0F))
                .start();
    }

    public void rotateFabBackward() {
        ViewCompat.animate(floatingActionButton)
                .rotation(0.0F)
                .withLayer()
                .setDuration(500L)
                .setInterpolator(new OvershootInterpolator(1.0F))
                .start();
    }

    @OnClick(R.id.fab_add)
    public void onClick() {
        if (!isInputVisible()) {
            rotateFabForward();
            inputBox.setVisibility(View.VISIBLE);
            submitButton.setVisibility(View.GONE);
            clearText.setVisibility(View.GONE);
            inputBox.animate().alpha(1.0f).setDuration(300);


        } else {
            rotateFabBackward();

            inputBox.setVisibility(View.GONE);
            inputBox.animate().alpha(0.0f).setDuration(300);
        }
    }

    @OnClick(R.id.cleartext)
    public void OnClick() {
        inputField.setText(null);
    }

    @OnClick(R.id.submit)
    public void submit() {

        addNewUrl();
    }

    @OnTextChanged(R.id.url_input)
    public void onTextChanged(CharSequence s, int start, int before,
                              int count) {
        if (s != null && !s.toString().isEmpty()) {

            submitButton.setVisibility(View.VISIBLE);
            clearText.setVisibility(View.VISIBLE);

        } else {
            submitButton.setVisibility(View.GONE);
            clearText.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(syncBroadcastReceiver, syncIntentFilter);

    }

    @Override
    protected void onPause() {
        unregisterReceiver(syncBroadcastReceiver);
        super.onPause();
    }

    private void setEmptyErrorLayout(boolean empty) {
        if (empty) {
            recyclerview.setVisibility(View.GONE);
            emptyErrorLayout.setVisibility(View.VISIBLE);
        } else {
            recyclerview.setVisibility(View.VISIBLE);
            emptyErrorLayout.setVisibility(View.GONE);
        }
    }

    // Thanks http://stackoverflow.com/a/28627878/3455743
    private ProgressDialog createProgressDialog() {
        ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(getString(R.string.new_url_waiting_message));
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;

    }

    private void createAlertDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(message)
                .setIcon(R.drawable.ic_error)
                .setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

    }
}

