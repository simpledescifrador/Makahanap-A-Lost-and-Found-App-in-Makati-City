package com.makatizen.makahanap.pojo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.utils.enums.Type;

@Entity(tableName = "feed_item")
public class FeedItem {

    @SerializedName("account_image_url")
    @Expose
    private String accountImageUrl;

    @SerializedName("account_name")
    @Expose
    private String accountName;

    @SerializedName("item_created_at")
    @Expose
    private String itemCreatedAt;

    @SerializedName("item_description")
    @Expose
    private String itemDescription;

    @PrimaryKey(autoGenerate = false)
    @SerializedName("item_id")
    @Expose
    private Integer itemId;

    @SerializedName("item_image_url")
    @Expose
    private String itemImageUrl;

    @SerializedName("item_location")
    @Expose
    private String itemLocation;

    @SerializedName("item_title")
    @Expose
    private String itemTitle;

    @SerializedName("item_type")
    @Expose
    private Type itemType;

    public FeedItem() {
    }

    public FeedItem(final String accountImageUrl, final String accountName, final String itemCreatedAt,
            final String itemDescription,
            final Integer itemId, final String itemImageUrl, final String itemTitle) {
        this.accountImageUrl = accountImageUrl;
        this.accountName = accountName;
        this.itemCreatedAt = itemCreatedAt;
        this.itemDescription = itemDescription;
        this.itemId = itemId;
        this.itemImageUrl = itemImageUrl;
        this.itemTitle = itemTitle;
    }

    public String getAccountImageUrl() {
        return accountImageUrl;
    }

    public void setAccountImageUrl(final String accountImageUrl) {
        this.accountImageUrl = accountImageUrl;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(final String accountName) {
        this.accountName = accountName;
    }

    public String getItemCreatedAt() {
        return itemCreatedAt;
    }

    public void setItemCreatedAt(final String itemCreatedAt) {
        this.itemCreatedAt = itemCreatedAt;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(final String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(final Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemImageUrl() {
        return itemImageUrl;
    }

    public void setItemImageUrl(final String itemImageUrl) {
        this.itemImageUrl = itemImageUrl;
    }

    public String getItemLocation() {
        return itemLocation;
    }

    public void setItemLocation(final String itemLocation) {
        this.itemLocation = itemLocation;
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(final String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public Type getItemType() {
        return itemType;
    }

    public void setItemType(final Type itemType) {
        this.itemType = itemType;
    }
}
