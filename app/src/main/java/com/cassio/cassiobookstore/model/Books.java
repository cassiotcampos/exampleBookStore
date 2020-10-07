
package com.cassio.cassiobookstore.model;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Books {

    @SerializedName("kind")

    private String kind;
    @SerializedName("totalItems")

    private Integer totalItems;
    @SerializedName("items")

    private List<Item> items = null;

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

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

}
