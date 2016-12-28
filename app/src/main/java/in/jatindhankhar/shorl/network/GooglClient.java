package in.jatindhankhar.shorl.network;


import java.util.List;

import in.jatindhankhar.shorl.model.HistoryItem;
import in.jatindhankhar.shorl.model.HistoryResponse;
import in.jatindhankhar.shorl.model.NewUrl;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;


/**
 * Created by jatin on 12/18/16.
 */

public interface GooglClient {
    @GET("history")
    Call<HistoryResponse> displayUser();

    @POST(".") // Endpoint is same as base point
    Call<HistoryItem> createUrl(@Body NewUrl newUrl);
}
