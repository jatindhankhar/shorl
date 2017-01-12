package in.jatindhankhar.shorl.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import in.jatindhankhar.shorl.R;
import in.jatindhankhar.shorl.utils.Constants;

/**
 * Created by jatin on 1/10/17.
 */

public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private int appWidgetId;
    private Cursor mCursor;
    private String shortUrl;
    public WidgetFactory(Context applicationContext, Intent intent) {
            this.mContext = applicationContext;
            this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        mCursor = mContext.getContentResolver().query(in.jatindhankhar.shorl.database.UrlProvider.Urls.CONTENT_URI,null
                ,null,null,null);
    }

    @Override
    public void onDataSetChanged() {
        if(mCursor != null) mCursor.close();
        mCursor = mContext.getContentResolver().query(in.jatindhankhar.shorl.database.UrlProvider.Urls.CONTENT_URI,null
        ,null,null,null);

    }

    @Override
    public void onDestroy() {
        if(mCursor != null)
        {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if(mCursor.moveToPosition(position))
        {
            shortUrl = mCursor.getString(mCursor.getColumnIndex(Constants.COLUMN_SHORT_URL));
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(),R.layout.widget_item_layout);
        rv.setTextViewText(R.id.short_url,shortUrl);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
       // return new RemoteViews(mContext.getPackageName(), R.layout.list_item);
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
