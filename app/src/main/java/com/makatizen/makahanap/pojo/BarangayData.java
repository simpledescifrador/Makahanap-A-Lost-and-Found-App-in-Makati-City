package com.makatizen.makahanap.pojo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.utils.converters.DistrictConverter;
import com.makatizen.makahanap.utils.enums.District;

@Entity(tableName = "barangay_data")
public class BarangayData {

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("district_no")
    @Expose
    @TypeConverters(DistrictConverter.class)
    private District districtNo;

    @PrimaryKey(autoGenerate = false)
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("name")
    @Expose
    private String name;

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public District getDistrictNo() {
        return districtNo;
    }

    public void setDistrictNo(final District districtNo) {
        this.districtNo = districtNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(final String latitude) {
        this.latitude = latitude;
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
}
