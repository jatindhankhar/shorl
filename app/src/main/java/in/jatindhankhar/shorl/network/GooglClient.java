package in.jatindhankhar.shorl.network;


import java.util.List;

import in.jatindhankhar.shorl.model.DetailedHistoryResponse;
import in.jatindhankhar.shorl.model.ExpandUrlResponse;
import in.jatindhankhar.shorl.model.HistoryItem;
import in.jatindhankhar.shorl.model.HistoryResponse;
import in.jatindhankhar.shorl.model.NewUrl;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


/**
 * Created by jatin on 12/18/16.
 */

public interface GooglClient {
    @GET("history")
    Call<HistoryResponse> processList();

    @GET("history")
    Call<DetailedHistoryResponse> processDetaiList(
            @Query("projection") String projection
    );


    @POST("./") // Endpoint is same as base point
    Call<HistoryItem> createUrl(@Body NewUrl newUrl);

    @GET("./") //Endpoint is same as base point
    Call<ExpandUrlResponse> processAnalytics(
            @Query("shortUrl") String shortUrl,
            @Query("projection") String projection
    );
}
