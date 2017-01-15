package in.jatindhankhar.shorl.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jatin on 12/29/16.
 */
public class CountData {
    @SerializedName("count")
    String count;

    @SerializedName("id")
    String id;

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
