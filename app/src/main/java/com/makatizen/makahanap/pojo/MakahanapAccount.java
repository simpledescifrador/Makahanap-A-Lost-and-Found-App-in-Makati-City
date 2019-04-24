package com.makatizen.makahanap.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.utils.enums.AccountStatus;
import com.makatizen.makahanap.utils.enums.CivilStatus;
import com.makatizen.makahanap.utils.enums.Gender;

public class MakahanapAccount {

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

    @SerializedName("date_created")
    @Expose
    private String dateCreated;

    @SerializedName("first_name")
    @Expose
    private String firstName;

    @SerializedName("sex")
    @Expose
    private Gender gender;

    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("image_url")
    @Expose
    private String imageUrl;

    @SerializedName("last_name")
    @Expose
    private String lastName;

    @SerializedName("middle_name")
    @Expose
    private String middleName;

    @SerializedName("status")
    @Expose
    private AccountStatus status;

    public MakahanapAccount() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(final String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public CivilStatus getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(final CivilStatus civilStatus) {
        this.civilStatus = civilStatus;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(final String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(final Gender gender) {
        this.gender = gender;
    }

    public Integer getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(final String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(final String middleName) {
        this.middleName = middleName;
    }

    public AccountStatus getStatus() {
        return status;
    }
}
