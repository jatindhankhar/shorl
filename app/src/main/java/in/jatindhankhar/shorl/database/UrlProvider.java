package in.jatindhankhar.shorl.database;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

import in.jatindhankhar.shorl.utils.Constants;

/**
 * Created by jatin on 12/30/16.
 */

@ContentProvider(authority = UrlProvider.AUTHORITY, database = UrlDatabase.class)
public final class UrlProvider {
    public static final String AUTHORITY = Constants.PACKAGE_NAME + ".database.UrlProvider";

    @TableEndpoint(table = UrlDatabase.URLS)
    public static class Urls {
        @ContentUri(
                path = Constants.TABLE_NAME,
                type = "vnd.android.cursor.dir/urls",
                defaultSort = UrlColumns.CREATED_URL + " DESC")
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/urls");
    }
}
