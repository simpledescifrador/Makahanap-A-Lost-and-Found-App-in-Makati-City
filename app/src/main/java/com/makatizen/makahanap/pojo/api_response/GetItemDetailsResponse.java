package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.pojo.LocationData;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.Person;
import com.makatizen.makahanap.pojo.PersonalThing;
import com.makatizen.makahanap.pojo.Pet;
import com.makatizen.makahanap.utils.enums.Type;

public class GetItemDetailsResponse {

    @SerializedName("account_data")
    @Expose
    private MakahanapAccount account;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("location_data")
    @Expose
    private LocationData locationData;

    @SerializedName("person_data")
    @Expose
    private Person personData;

    @SerializedName("pt_data")
    @Expose
    private PersonalThing personalThingData = null;

    @SerializedName("pet_data")
    @Expose
    private Pet petData;

    @SerializedName("report_type")
    @Expose
    private String reportType;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("type")
    @Expose
    private Type type;

    public MakahanapAccount getAccount() {
        return account;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public LocationData getLocationData() {
        return locationData;
    }

    public Person getPersonData() {
        return personData;
    }

    public PersonalThing getPersonalThingData() {
        return personalThingData;
    }

    public Pet getPetData() {
        return petData;
    }

    public String getReportType() {
        return reportType;
    }

    public String getTitle() {
        return title;
    }

    public Type getType() {
        return type;
    }
}
