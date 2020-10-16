package com.cassio.cassiobookstore.model.dto;

import com.google.gson.annotations.SerializedName;

public class SaleInfoDTO {

    @SerializedName("buyLink")
    private String buyLink;

    public String getBuyLink() {
        return buyLink;
    }

    public void setBuyLink(String buyLink) {
        this.buyLink = buyLink;
    }
}
