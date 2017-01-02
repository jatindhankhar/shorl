package in.jatindhankhar.shorl.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * Created by jatin on 12/29/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_CREATE = "create table "
            + Constants.TABLE_NAME + "( " + Constants.COLUMN_ID
            + " integer primary key autoincrement, " + Constants.COLUMN_SHORT_URL
            + " text not null , " + Constants.COLUMN_LONG_URL +
            "text not null, " + Constants.COLUMN_CREATED_DATE_URL +
            "text not null, " + Constants.COLUMN_KIND_URL +
            ");";

    private static final String DATABASE_DESTORY =  "drop table if exists " + Constants.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DATABASE_DESTORY);
    }
}
