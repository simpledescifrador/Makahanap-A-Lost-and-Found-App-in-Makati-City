package com.makatizen.makahanap.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.utils.enums.Type;
import java.util.List;

public class Pet {

    @SerializedName("account_id")
    @Expose
    private String accountId;

    @SerializedName("additional_location_info")
    @Expose
    private String additionalLocationInfo;

    @SerializedName("breed")
    @Expose
    private String breed;

    @SerializedName("condition")
    @Expose
    private String condition;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private Type itemType;

    @SerializedName("location_date")
    @Expose
    private LocationData locationData;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("pet_images")
    @Expose
    private List<String> petImagesUrl;

    @SerializedName("pet_type")
    @Expose
    private String petType;

    @SerializedName("reward")
    @Expose
    private Double reward;

    public Pet() {
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(final String accountId) {
        this.accountId = accountId;
    }

    public String getAdditionalLocationInfo() {
        return additionalLocationInfo;
    }

    public void setAdditionalLocationInfo(final String additionalLocationInfo) {
        this.additionalLocationInfo = additionalLocationInfo;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(final String breed) {
        this.breed = breed;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(final String condition) {
        this.condition = condition;
    }

    public String getDate() {
        return date;
    }

    public void setDate(final String date) {
        this.date = date;
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

    public Type getItemType() {
        return itemType;
    }

    public void setItemType(final Type itemType) {
        this.itemType = itemType;
    }

    public LocationData getLocationData() {
        return locationData;
    }

    public void setLocationData(final LocationData locationData) {
        this.locationData = locationData;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<String> getPetImagesUrl() {
        return petImagesUrl;
    }

    public void setPetImagesUrl(final List<String> petImagesUrl) {
        this.petImagesUrl = petImagesUrl;
    }

    public String getPetType() {
        return petType;
    }

    public void setPetType(final String petType) {
        this.petType = petType;
    }

    public Double getReward() {
        return reward;
    }

    public void setReward(final Double reward) {
        this.reward = reward;
    }

}
