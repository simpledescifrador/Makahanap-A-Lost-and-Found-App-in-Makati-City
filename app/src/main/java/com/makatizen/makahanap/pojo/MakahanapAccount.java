package com.makatizen.makahanap.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.utils.enums.AccountStatus;
import com.makatizen.makahanap.utils.enums.CivilStatus;
import com.makatizen.makahanap.utils.enums.Gender;

public class MakahanapAccount implements Parcelable {

    public static final Creator<MakahanapAccount> CREATOR = new Creator<MakahanapAccount>() {
        @Override
        public MakahanapAccount createFromParcel(Parcel in) {
            return new MakahanapAccount(in);
        }

        @Override
        public MakahanapAccount[] newArray(int size) {
            return new MakahanapAccount[size];
        }
    };

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

    @SerializedName("email_address")
    @Expose
    private String emailAddress;

    @SerializedName("first_name")
    @Expose
    private String firstName;

    @SerializedName("sex")
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

    private String password;

    @SerializedName("profile_image_url")
    @Expose
    private String profileImageUrl;

    @SerializedName("status")
    @Expose
    private AccountStatus status;

    public MakahanapAccount() {
    }

    protected MakahanapAccount(Parcel in) {
        address = in.readString();
        if (in.readByte() == 0) {
            age = null;
        } else {
            age = in.readInt();
        }
        cardNumber = in.readString();
        contactNumber = in.readString();
        dateCreated = in.readString();
        emailAddress = in.readString();
        firstName = in.readString();
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        lastName = in.readString();
        makatizenNumber = in.readString();
        middleName = in.readString();
        password = in.readString();
        profileImageUrl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
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

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(final String emailAddress) {
        this.emailAddress = emailAddress;
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

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public String getMakatizenNumber() {
        return makatizenNumber;
    }

    public void setMakatizenNumber(final String makatizenNumber) {
        this.makatizenNumber = makatizenNumber;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(final String middleName) {
        this.middleName = middleName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(final String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public AccountStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "MakahanapAccount{" +
                "address='" + address + '\'' +
                ", age=" + age +
                ", cardNumber='" + cardNumber + '\'' +
                ", civilStatus=" + civilStatus +
                ", contactNumber='" + contactNumber + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", firstName='" + firstName + '\'' +
                ", gender=" + gender +
                ", id=" + id +
                ", profileImageUrl='" + profileImageUrl + '\'' +
                ", lastName='" + lastName + '\'' +
                ", makatizenNumber='" + makatizenNumber + '\'' +
                ", middleName='" + middleName + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                '}';
    }

    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeString(address);
        if (age == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(age);
        }
        parcel.writeString(cardNumber);
        parcel.writeString(contactNumber);
        parcel.writeString(dateCreated);
        parcel.writeString(emailAddress);
        parcel.writeString(firstName);
        if (id == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(id);
        }
        parcel.writeString(lastName);
        parcel.writeString(makatizenNumber);
        parcel.writeString(middleName);
        parcel.writeString(password);
        parcel.writeString(profileImageUrl);
    }
}
