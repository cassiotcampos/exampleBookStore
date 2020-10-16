
package com.cassio.cassiobookstore.model.dto;

import com.google.gson.annotations.SerializedName;

public class ImageLinksDTO {

    @SerializedName("smallThumbnail")

    private String smallThumbnail;
    @SerializedName("thumbnail")

    private String thumbnail;

    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public void setSmallThumbnail(String smallThumbnail) {
        this.smallThumbnail = smallThumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

}
