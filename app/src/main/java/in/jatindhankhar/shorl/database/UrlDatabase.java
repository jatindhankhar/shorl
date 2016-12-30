package in.jatindhankhar.shorl.database;

import net.simonvt.schematic.annotation.Database;
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
}
