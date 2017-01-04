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
import butterknife.BindView;
import butterknife.ButterKnife;
import in.jatindhankhar.shorl.R;
import in.jatindhankhar.shorl.utils.Constants;
import in.jatindhankhar.shorl.utils.Utils;

/**
 * Created by jatin on 1/2/17.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {



    private Context mContext;
    private Cursor mCursor;

    public ListAdapter(Context mContext, Cursor mCursor) {
        this.mContext = mContext;
        this.mCursor = mCursor;
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
        //Log.d("Yolopad","Url is " );
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

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ButterKnife.setDebug(true);
        }

    }
}
