package in.jatindhankhar.shorl.database;

import android.database.sqlite.SQLiteDatabase;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.ExecOnCreate;
import net.simonvt.schematic.annotation.OnUpgrade;
import net.simonvt.schematic.annotation.Table;

import in.jatindhankhar.shorl.utils.Constants;

/**
 * Created by jatin on 12/30/16.
 */
@Database(version = UrlDatabase.VERSION)
public final class UrlDatabase {
    private UrlDatabase(){}
    public static final int VERSION = Constants.DATABASE_VERSION;

    @Table(UrlColumns.class) public static final String URLS = Constants.TABLE_NAME;

    @OnUpgrade
    public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME + ";");
            // Then create new Database
            db.execSQL(in.jatindhankhar.shorl.database.generated.UrlDatabase.URLS);
        }

}
