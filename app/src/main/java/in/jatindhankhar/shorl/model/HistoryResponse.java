package in.jatindhankhar.shorl.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by jatin on 12/28/16.
 */

public class HistoryResponse {

    @SerializedName("kind")
    
    private String kind;
    @SerializedName("totalItems")
    
    private Integer totalItems;
    @SerializedName("itemsPerPage")
    
    private Integer itemsPerPage;
    @SerializedName("items")
    
    private List<HistoryItem> historyItems = null;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public Integer getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(Integer itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public List<HistoryItem> getHistoryItems() {
        return historyItems;
    }

    public void setHistoryItems(List<HistoryItem> items) {
        this.historyItems = items;
    }

}