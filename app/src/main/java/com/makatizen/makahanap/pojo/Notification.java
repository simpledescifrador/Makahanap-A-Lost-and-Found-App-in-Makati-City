package com.makatizen.makahanap.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("ref_id")
    @Expose
    private Integer referenceId;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("viewed")
    @Expose
    private Boolean viewed;

    public Notification() {
    }

    public Notification(Integer id, String content, String createdAt, String imageUrl, Integer referenceId, String title, String type, Boolean viewed) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.imageUrl = imageUrl;
        this.referenceId = referenceId;
        this.title = title;
        this.type = type;
        this.viewed = viewed;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Integer getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getReferenceId() {
        return referenceId;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public Boolean isViewed() {
        return viewed;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setReferenceId(Integer referenceId) {
        this.referenceId = referenceId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setViewed(Boolean viewed) {
        this.viewed = viewed;
    }
}
