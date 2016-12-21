package in.jatindhankhar.shorl.network;

import com.squareup.okhttp.ResponseBody;

import java.util.List;

import in.jatindhankhar.shorl.model.HistoryItem;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by jatin on 12/18/16.
 */

public interface GooglClient {
    @GET("history")
    Call<List<HistoryItem>> displayUser();
}
