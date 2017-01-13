package in.jatindhankhar.shorl.database;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.DefaultValue;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.Unique;

import in.jatindhankhar.shorl.utils.Constants;

/**
 * Created by jatin on 12/30/16.
 */

public interface UrlColumns {
    @DataType(DataType.Type.TEXT) @NotNull @Unique
     String SHORT_URL = Constants.COLUMN_SHORT_URL;
    @DataType(DataType.Type.TEXT) @NotNull
     String LONG_URL = Constants.COLUMN_LONG_URL;
    @DataType(DataType.Type.TEXT)
     String KIND_URL = Constants.COLUMN_KIND_URL;
    @DataType(DataType.Type.TEXT) @NotNull
     String CREATED_URL = Constants.COLUMN_CREATED_DATE_URL;
    @DataType(DataType.Type.TEXT)
     String STATUS_URL = Constants.COLUMN_STATUS_URL;
    @DataType(DataType.Type.TEXT)
     String ANALYTICS_URL = Constants.COLUMN_ANALYTICS_URL;
    @DataType(DataType.Type.INTEGER) @NotNull @DefaultValue("0")
    String FAVOURITES = Constants.COLUMN_FAVOURITE;
}
