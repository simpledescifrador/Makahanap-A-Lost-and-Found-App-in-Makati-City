package com.makatizen.makahanap.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.utils.enums.CivilStatus;
import com.makatizen.makahanap.utils.enums.Gender;

public class MakatizenAccount {

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("age")
    @Expose
    private Integer age;

    @SerializedName("card_number")
    @Expose
    private String cardNumber;

    @SerializedName("civil_status")
    @Expose
    private CivilStatus civilStatus;

    @SerializedName("contact_number")
    @Expose
    private String contactNumber;

    @SerializedName("email_address")
    @Expose
    private String emailAddress;

    @SerializedName("first_name")
    @Expose
    private String firstName;

    @SerializedName("gender")
    @Expose
    private Gender gender;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("makatizen_number")
    @Expose
    private String makatizenNumber;

    @SerializedName("middle_name")
    @Expose
    private String middleName;

    @SerializedName("profile_image_url")
    @Expose
    private String profileImageUrl;

    public MakatizenAccount() {
    }

    public String getAddress() {
        return address;
    }

    public Integer getAge() {
        return age;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public CivilStatus getCivilStatus() {
        return civilStatus;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getFirstName() {
        return firstName;
    }

    public Gender getGender() {
        return gender;
    }

    public Integer getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getMakatizenNumber() {
        return makatizenNumber;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }
}
