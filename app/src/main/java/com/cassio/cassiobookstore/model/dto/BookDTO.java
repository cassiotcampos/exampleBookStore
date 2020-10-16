
package com.cassio.cassiobookstore.model.dto;

import com.google.gson.annotations.SerializedName;

public class BookDTO {

    @SerializedName("kind")
    private String kind;

    @SerializedName("id")
    private String id;

    @SerializedName("etag")
    private String etag;

    @SerializedName("selfLink")
    private String selfLink;

    @SerializedName("volumeInfo")
    private VolumeInfoDTO volumeInfoDTO;

    @SerializedName("searchInfo")
    private SearchInfoDTO searchInfoDTO;

    @SerializedName("saleInfo")
    private SaleInfoDTO saleInfoDTO;

    public SaleInfoDTO getSaleInfoDTO() {
        return saleInfoDTO;
    }

    public void setSaleInfoDTO(SaleInfoDTO saleInfoDTO) {
        this.saleInfoDTO = saleInfoDTO;
    }

    public SearchInfoDTO getSearchInfoDTO() {
        return searchInfoDTO;
    }

    public void setSearchInfoDTO(SearchInfoDTO searchInfoDTO) {
        this.searchInfoDTO = searchInfoDTO;
    }

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

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public VolumeInfoDTO getVolumeInfoDTO() {
        return volumeInfoDTO;
    }

    public void setVolumeInfoDTO(VolumeInfoDTO volumeInfoDTO) {
        this.volumeInfoDTO = volumeInfoDTO;
    }
}
