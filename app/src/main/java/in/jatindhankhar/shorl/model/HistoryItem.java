package in.jatindhankhar.shorl.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jatin on 12/18/16.
 */

public class HistoryItem {
    @SerializedName("kind")
    private String kind;

    @SerializedName("id")
    private String id;

    @SerializedName("longUrl")
    private String longUrl;

    @SerializedName("status")
    private String status;

    @SerializedName("created")
    private String created;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
