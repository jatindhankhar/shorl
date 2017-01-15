package in.jatindhankhar.shorl.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jatin on 12/28/16.
 */

public class NewUrl {
    @SerializedName("longUrl")
    private String longUrl;

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }
}

