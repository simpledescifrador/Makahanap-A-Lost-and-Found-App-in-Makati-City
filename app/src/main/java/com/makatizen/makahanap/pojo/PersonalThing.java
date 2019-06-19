package com.makatizen.makahanap.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.utils.enums.Type;
import java.util.List;

public class PersonalThing {

    @SerializedName("account_data")
    @Expose
    private MakahanapAccount accountData;

    @SerializedName("additional_location_info")
    @Expose
    private String additionalLocationInfo;

    @SerializedName("barangay_data")
    @Expose
    private BarangayData barangayData;

    @SerializedName("brand")
    @Expose
    private String brand;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("date_surrendered")
    @Expose
    private String dateSurrendered;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("item_surrendered")
    @Expose
    private Boolean isItemSurrendered;

    @SerializedName("item_images")
    @Expose
    private List<String> itemImagesUrl;

    @SerializedName("item_name")
    @Expose
    private String itemName;

    @SerializedName("location_data")
    @Expose
    private LocationData locationData;

    @SerializedName("reward")
    @Expose
    private Double reward;

    @SerializedName("type")
    @Expose
    private Type type;

    public PersonalThing() {
    }

    public MakahanapAccount getAccountData() {
        return accountData;
    }

    public void setAccountData(final MakahanapAccount accountData) {
        this.accountData = accountData;
    }

    public String getAdditionalLocationInfo() {
        return additionalLocationInfo;
    }

    public void setAdditionalLocationInfo(final String additionalLocationInfo) {
        this.additionalLocationInfo = additionalLocationInfo;
    }

    public BarangayData getBarangayData() {
        return barangayData;
    }

    public void setBarangayData(final BarangayData barangayData) {
        this.barangayData = barangayData;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(final String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(final String category) {
        this.category = category;
    }

    public String getColor() {
        return color;
    }

    public void setColor(final String color) {
        this.color = color;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
    }

    public String getDateSurrendered() {
        return dateSurrendered;
    }

    public void setDateSurrendered(final String dateSurrendered) {
        this.dateSurrendered = dateSurrendered;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public List<String> getItemImagesUrl() {
        return itemImagesUrl;
    }

    public void setItemImagesUrl(final List<String> itemImagesUrl) {
        this.itemImagesUrl = itemImagesUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(final String itemName) {
        this.itemName = itemName;
    }

    public Boolean getItemSurrendered() {
        return isItemSurrendered;
    }

    public void setItemSurrendered(final Boolean itemSurrendered) {
        isItemSurrendered = itemSurrendered;
    }

    public LocationData getLocationData() {
        return locationData;
    }

    public void setLocationData(final LocationData locationData) {
        this.locationData = locationData;
    }

    public Double getReward() {
        return reward;
    }

    public void setReward(final Double reward) {
        this.reward = reward;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
    }
}
