package in.jatindhankhar.shorl.database;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.NotNull;
import net.simonvt.schematic.annotation.PrimaryKey;

import in.jatindhankhar.shorl.utils.Constants;

/**
 * Created by jatin on 12/30/16.
 */

public interface UrlColumns {
    @DataType(DataType.Type.INTEGER) @PrimaryKey @AutoIncrement
      String _ID = Constants.COLUMN_ID;
    @DataType(DataType.Type.TEXT) @NotNull
     String SHORT_URL = Constants.COLUMN_SHORT_URL;
    @DataType(DataType.Type.TEXT) @NotNull
     String LONG_URL = Constants.COLUMN_LONG_URL;
    @DataType(DataType.Type.TEXT) @NotNull
     String KIND_URL = Constants.COLUMN_KIND_URL;
    @DataType(DataType.Type.TEXT) @NotNull
     String CREATED_URL = Constants.COLUMN_CREATED_DATE_URL;
    @DataType(DataType.Type.TEXT) @NotNull
     String STATUS_URL = Constants.COLUMN_STATUS_URL;
    @DataType(DataType.Type.TEXT) @NotNull
     String ANALYTICS_URL = Constants.COLUMN_ANALYTICS_URL;
}
