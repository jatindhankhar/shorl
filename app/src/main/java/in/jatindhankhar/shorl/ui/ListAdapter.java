package in.jatindhankhar.shorl.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.crash.FirebaseCrash;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.jatindhankhar.shorl.R;
import in.jatindhankhar.shorl.model.Analytics;
import in.jatindhankhar.shorl.ui.custom.TimeagoLayout;
import in.jatindhankhar.shorl.utils.Constants;
import in.jatindhankhar.shorl.utils.Utils;

/**
 * Created by jatin on 1/2/17.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {


    private static final String TAG = ListAdapter.class.getSimpleName();
    OnItemClickListener mItemClickListener;
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        mCursor.moveToPosition(position);

        String shortUrl = Utils.getGooglShortUrl(mCursor.getString(mCursor.getColumnIndex(Constants.COLUMN_SHORT_URL)));
        holder.shortUrl.setText(shortUrl);
        String clickcount;
        holder.longUrl.setText(mCursor.getString(mCursor.getColumnIndex(Constants.COLUMN_LONG_URL)));
        try {
            clickcount = gson.fromJson(mCursor.getString(mCursor.getColumnIndex(Constants.COLUMN_ANALYTICS_URL)), Analytics.class).getAllTime().getShortUrlClicks();
        } catch (NullPointerException ex) {
            clickcount = "0";
            FirebaseCrash.logcat(Log.ERROR, TAG, "NPE caught while calculating click counts");
            FirebaseCrash.report(ex);
        }
        String createdDate = mCursor.getString(mCursor.getColumnIndex(Constants.COLUMN_CREATED_DATE_URL));
        holder.timeagoLayout.setTargetDate(createdDate);
        holder.clickCount.setText(String.format(mContext.getResources().getString(R.string.click_count), clickcount));
        ;


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

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public Cursor getCursor() {
        return mCursor;
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.card_view)
        CardView cardView;
        @BindView(R.id.short_url)
        TextView shortUrl;
        @BindView(R.id.long_url)
        TextView longUrl;
        @BindView(R.id.click_count)
        TextView clickCount;
        @BindView(R.id.timeagoLayout)
        TimeagoLayout timeagoLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ButterKnife.setDebug(true);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getPosition());
            }
        }
    }
}
