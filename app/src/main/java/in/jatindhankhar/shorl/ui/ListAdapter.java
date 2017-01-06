package in.jatindhankhar.shorl.ui;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.jatindhankhar.shorl.R;
import in.jatindhankhar.shorl.model.Analytics;
import in.jatindhankhar.shorl.model.TimeData;
import in.jatindhankhar.shorl.utils.Constants;
import in.jatindhankhar.shorl.utils.Utils;

/**
 * Created by jatin on 1/2/17.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {



    private Context mContext;
    private Cursor mCursor;
    private Gson gson;

    public ListAdapter(Context mContext, Cursor mCursor) {
        this.mContext = mContext;
        this.mCursor = mCursor;
        gson = new Gson();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);

        String shortUrl = Utils.getGooglShortUrl(mCursor.getString(mCursor.getColumnIndex(Constants.COLUMN_SHORT_URL)));
        holder.shortUrl.setText(shortUrl);

        holder.longUrl.setText(mCursor.getString(mCursor.getColumnIndex(Constants.COLUMN_LONG_URL)));
        String clickcount = gson.fromJson(mCursor.getString(mCursor.getColumnIndex(Constants.COLUMN_ANALYTICS_URL)),Analytics.class).getAllTime().getShortUrlClicks();
        String createdDate = mCursor.getString(mCursor.getColumnIndex(Constants.COLUMN_CREATED_DATE_URL));
        holder.createdText.setText(Utils.getReadbleDate(createdDate));
        holder.clickCount.setText(clickcount + " Clicks ");
        Log.d("Yolopad", "Custom date is " + Utils.getRelativeTime(createdDate));


    }

    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor)
            return null;
        Cursor oldCursor = mCursor;
        this.mCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    @Override
    public int getItemCount() {

        return (mCursor == null) ? 0 : mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.short_url)
        TextView shortUrl;
        @BindView(R.id.long_url)
        TextView longUrl;
        @BindView(R.id.click_count)
        TextView clickCount;
        @BindView(R.id.created_text)
        TextView createdText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ButterKnife.setDebug(true);

        }

    }
}
