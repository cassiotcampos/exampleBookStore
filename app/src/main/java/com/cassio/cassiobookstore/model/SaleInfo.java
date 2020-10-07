package com.cassio.cassiobookstore.model;

import com.google.gson.annotations.SerializedName;

public class SaleInfo {

    @SerializedName("buyLink")
    private String buyLink;

    public String getBuyLink() {
        return buyLink;
    }

    public void setBuyLink(String buyLink) {
        this.buyLink = buyLink;
    }
}
