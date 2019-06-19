package com.makatizen.makahanap.pojo;

import android.net.Uri;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationData {

    @SerializedName("address")
    @Expose
    private String address;

    private String attributions;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    private LatLng latlng;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("name")
    @Expose
    private String name;

    private String phoneNumber;

    private float rating;

    private Uri websiteUri;

    public LocationData() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public String getAttributions() {
        return attributions;
    }

    public void setAttributions(final String attributions) {
        this.attributions = attributions;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(final String latitude) {
        this.latitude = latitude;
    }

    public LatLng getLatlng() {
        return latlng;
    }

    public void setLatlng(final LatLng latlng) {
        this.latlng = latlng;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(final String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(final String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(final float rating) {
        this.rating = rating;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(final Uri websiteUri) {
        this.websiteUri = websiteUri;
    }
}
