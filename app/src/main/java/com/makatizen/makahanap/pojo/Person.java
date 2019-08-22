package com.makatizen.makahanap.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.utils.enums.Type;
import java.util.List;

public class Person {

    @SerializedName("account_data")
    @Expose
    private MakahanapAccount accountData;

    @SerializedName("additional_location_info")
    @Expose
    private String additionalLocationInfo;

    @SerializedName("age_group")
    @Expose
    private String ageGroup;

    @SerializedName("age_range")
    @Expose
    private String ageRange;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("location_data")
    @Expose
    private LocationData locationData;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("person_images")
    @Expose
    private List<String> personImagesUrl;

    @SerializedName("reward")
    @Expose
    private Double reward;

    @SerializedName("sex")
    @Expose
    private String sex;

    @SerializedName("type")
    @Expose
    private Type type;

    public Person() {
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

    public String getAgeGroup() {
        return ageGroup;
    }

    public void setAgeGroup(final String ageGroup) {
        this.ageGroup = ageGroup;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(final String ageRange) {
        this.ageRange = ageRange;
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

    public List<String> getPersonImagesUrl() {
        return personImagesUrl;
    }

    public void setPersonImagesUrl(final List<String> personImagesUrl) {
        this.personImagesUrl = personImagesUrl;
    }

    public Double getReward() {
        return reward;
    }

    public void setReward(final Double reward) {
        this.reward = reward;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(final String sex) {
        this.sex = sex;
    }

    public Type getType() {
        return type;
    }

    public void setType(final Type type) {
        this.type = type;
    }
}
