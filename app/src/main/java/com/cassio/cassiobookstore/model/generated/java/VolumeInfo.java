
package com.cassio.cassiobookstore.model.generated.java;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VolumeInfo {

    @SerializedName("title")
    private String title;
    @SerializedName("authors")
    private List<String> authors = null;
    @SerializedName("publisher")
    private String publisher;
    @SerializedName("publishedDate")
    private String publishedDate;
    @SerializedName("imageLinks")
    private ImageLinks imageLinks;

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

    public ImageLinks getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(ImageLinks imageLinks) {
        this.imageLinks = imageLinks;
    }
}
