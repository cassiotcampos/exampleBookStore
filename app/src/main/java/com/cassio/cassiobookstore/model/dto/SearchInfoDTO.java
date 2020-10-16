package com.cassio.cassiobookstore.model.dto;

import com.google.gson.annotations.SerializedName;

public class SearchInfoDTO {

    @SerializedName("textSnippet")
    private String textSnippet;

    public String getTextSnippet() {
        return textSnippet;
    }

    public void setTextSnippet(String textSnippet) {
        this.textSnippet = textSnippet;
    }
}
