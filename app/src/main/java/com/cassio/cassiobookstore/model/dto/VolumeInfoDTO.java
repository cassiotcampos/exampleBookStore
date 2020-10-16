
package com.cassio.cassiobookstore.model.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VolumeInfoDTO {

    @SerializedName("title")
    private String title;
    @SerializedName("authors")
    private List<String> authors = null;
    @SerializedName("publisher")
    private String publisher;
    @SerializedName("publishedDate")
    private String publishedDate;
    @SerializedName("imageLinks")
    private ImageLinksDTO imageLinksDTO;
    @SerializedName("description")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public ImageLinksDTO getImageLinksDTO() {
        return imageLinksDTO;
    }

    public void setImageLinksDTO(ImageLinksDTO imageLinksDTO) {
        this.imageLinksDTO = imageLinksDTO;
    }
}
