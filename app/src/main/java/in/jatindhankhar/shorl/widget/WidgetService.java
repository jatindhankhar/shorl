package in.jatindhankhar.shorl.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by jatin on 1/10/17.
 */

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetFactory(this.getApplicationContext(), intent);
    }
}
