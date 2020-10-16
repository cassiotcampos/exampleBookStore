
package com.cassio.cassiobookstore.model.dto;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookListDTO {

    @SerializedName("kind")

    private String kind;
    @SerializedName("totalItems")

    private Integer totalItems;
    @SerializedName("items")

    private List<BookDTO> bookDTOS = null;

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

    public List<BookDTO> getBookDTOS() {
        return bookDTOS;
    }

    public void setBookDTOS(List<BookDTO> bookDTOS) {
        this.bookDTOS = bookDTOS;
    }

}
