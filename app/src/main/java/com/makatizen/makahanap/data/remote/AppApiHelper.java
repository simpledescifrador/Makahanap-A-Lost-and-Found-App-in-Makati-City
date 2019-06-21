package com.makatizen.makahanap.data.remote;

import android.net.Uri;
import android.support.annotation.NonNull;
import com.makatizen.makahanap.pojo.BarangayData;
import com.makatizen.makahanap.pojo.MakahanapAccount;
import com.makatizen.makahanap.pojo.Person;
import com.makatizen.makahanap.pojo.PersonalThing;
import com.makatizen.makahanap.pojo.Pet;
import com.makatizen.makahanap.pojo.api_response.GetLatestFeedResponse;
import com.makatizen.makahanap.pojo.api_response.LoginResponse;
import com.makatizen.makahanap.pojo.api_response.MakatizenGetDataResponse;
import com.makatizen.makahanap.pojo.api_response.RegisterReponse;
import com.makatizen.makahanap.pojo.api_response.VerifyMakatizenIdResponse;
import com.makatizen.makahanap.utils.enums.Type;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;

public class AppApiHelper implements ApiHelper {

    private ApiInterface mApiInterface;

    private MakatizenApiInterface mMakatizenApiInterface;

    @Inject
    public AppApiHelper(final ApiInterface apiInterface, final MakatizenApiInterface makatizenApiInterface) {
        mApiInterface = apiInterface;
        mMakatizenApiInterface = makatizenApiInterface;
    }


    @Override
    public Observable<List<BarangayData>> getAllBarangayData() {
        return mApiInterface.getAllBarangayData();
    }

    @Override
    public Single<BarangayData> getBarangayData(final int barangayId) {
        return mApiInterface.getBarangayData(barangayId);
    }

    @Override
    public Single<MakahanapAccount> getMakahanapAccountData(final int accountId) {
        return mApiInterface.getMakahanapAccountData(accountId);
    }

    @Override
    public Single<MakatizenGetDataResponse> getMakatizenData(final String makatizenId) {
        return mMakatizenApiInterface.getMakatizenData(makatizenId);
    }

    @Override
    public Single<LoginResponse> loginAppRequest(final String makatizenNumber, final String password) {
        return mApiInterface.loginAppRequest(makatizenNumber, password);
    }

    @Override
    public Single<RegisterReponse> registerNewAccount(final MakahanapAccount makahanapAccount) {
        return mApiInterface.registerNewAccount(makahanapAccount);
    }

    @Override
    public Completable reportPerson(final Person person) {
        Map<String, RequestBody> partMap = new HashMap<>();
        List<Part> personImagesPart = new ArrayList<>();
        Uri imageUri;
        //Location Info
        partMap.put("location_address", createPartFromString(person.getLocationData().getAddress()));
        partMap.put("location_name", createPartFromString(person.getLocationData().getName()));
        partMap.put("location_id", createPartFromString(person.getLocationData().getId()));
        partMap.put("location_latitude",
                createPartFromString(String.valueOf(person.getLocationData().getLatlng().latitude)));
        partMap.put("location_longitude",
                createPartFromString(String.valueOf(person.getLocationData().getLatlng().longitude)));
        partMap.put("additional_location_info", createPartFromString(person.getAdditionalLocationInfo()));

        partMap.put("account_id", createPartFromString(String.valueOf(person.getAccountData().getId())));
        partMap.put("name", createPartFromString(person.getName() != null ? person.getName() : ""));
        partMap.put("age_range", createPartFromString(person.getAgeRange()));
        partMap.put("age_group", createPartFromString(person.getAgeGroup()));
        partMap.put("sex", createPartFromString(person.getSex()));
        partMap.put("date", createPartFromString(person.getDate()));
        partMap.put("description", createPartFromString(person.getDescription()));

        if (person.getType() == Type.LOST) {
            partMap.put("type", createPartFromString("Lost"));
            partMap.put("reward",
                    createPartFromString(person.getReward() != null ? person.getReward().toString() : "0.0"));
        } else {
            partMap.put("type", createPartFromString("Found"));
        }
        int itemImagesCount = person.getPersonImagesUrl().size();
        for (int i = 0; i < itemImagesCount; i++) {
            imageUri = Uri.fromFile(new File(person.getPersonImagesUrl().get(i)));
            personImagesPart.add(prepareFilePart("person_images[]", imageUri));
        }
        return mApiInterface.reportPerson(partMap, personImagesPart);
    }

    @Override
    public Completable reportPersonalThing(final PersonalThing personalThing) {
        Map<String, RequestBody> partMap = new HashMap<>();
        List<Part> itemImagesPart = new ArrayList<>();
        Uri imageUri;

        //Location Info
        partMap.put("location_address", createPartFromString(personalThing.getLocationData().getAddress()));
        partMap.put("location_name", createPartFromString(personalThing.getLocationData().getName()));
        partMap.put("location_id", createPartFromString(personalThing.getLocationData().getId()));
        partMap.put("location_latitude",
                createPartFromString(String.valueOf(personalThing.getLocationData().getLatlng().latitude)));
        partMap.put("location_longitude",
                createPartFromString(String.valueOf(personalThing.getLocationData().getLatlng().longitude)));
        partMap.put("additional_location_info", createPartFromString(personalThing.getAdditionalLocationInfo()));

        partMap.put("item_name", createPartFromString(personalThing.getItemName()));
        partMap.put("item_category", createPartFromString(personalThing.getCategory()));
        partMap.put("brand", createPartFromString(personalThing.getBrand()));
        partMap.put("item_color", createPartFromString(personalThing.getColor()));
        partMap.put("item_description", createPartFromString(personalThing.getDescription()));
        partMap.put("account_id", createPartFromString(String.valueOf(personalThing.getAccountData().getId())));

        if (personalThing.getType() == Type.LOST) {
            partMap.put("type", createPartFromString("Lost"));
            partMap.put("date_lost", createPartFromString(personalThing.getDate()));
            partMap.put("reward", createPartFromString(String.valueOf(personalThing.getReward())));
        } else {
            partMap.put("type", createPartFromString("Found"));
            partMap.put("date_found", createPartFromString(personalThing.getDate()));
            partMap.put("barangay_id", createPartFromString(String.valueOf(personalThing.getBarangayData().getId())));
            partMap.put("s", createPartFromString(
                    String.valueOf(personalThing.getItemSurrendered() ? 1 : 0))); //if them is to be surrender
        }

        int itemImagesCount = personalThing.getItemImagesUrl().size();
        for (int i = 0; i < itemImagesCount; i++) {
            imageUri = Uri.fromFile(new File(personalThing.getItemImagesUrl().get(i)));
            itemImagesPart.add(prepareFilePart("item_images[]", imageUri));
        }
        return mApiInterface.reportPersonalThing(partMap, itemImagesPart);
    }

    @Override
    public Completable reportPet(final Pet pet) {
        Map<String, RequestBody> partMap = new HashMap<>();
        List<Part> petImagesPart = new ArrayList<>();
        Uri imageUri;

        //Location Info
        partMap.put("location_address", createPartFromString(pet.getLocationData().getAddress()));
        partMap.put("location_name", createPartFromString(pet.getLocationData().getName()));
        partMap.put("location_id", createPartFromString(pet.getLocationData().getId()));
        partMap.put("location_latitude",
                createPartFromString(String.valueOf(pet.getLocationData().getLatlng().latitude)));
        partMap.put("location_longitude",
                createPartFromString(String.valueOf(pet.getLocationData().getLatlng().longitude)));
        partMap.put("additional_location_info", createPartFromString(pet.getAdditionalLocationInfo()));

        partMap.put("account_id", createPartFromString(String.valueOf(pet.getAccountData().getId())));
        partMap.put("pet_name", createPartFromString(pet.getName() != null ? pet.getName() : ""));
        partMap.put("pet_breed", createPartFromString(pet.getBreed() != null ? pet.getBreed() : ""));
        partMap.put("pet_type", createPartFromString(pet.getPetType()));
        partMap.put("pet_condition", createPartFromString(pet.getCondition()));
        partMap.put("pet_description", createPartFromString(pet.getDescription()));
        partMap.put("reward", createPartFromString(pet.getReward() != null ? pet.getReward().toString() : "0"));
        partMap.put("date", createPartFromString(pet.getDate()));

        if (pet.getItemType() == Type.LOST) {
            partMap.put("type", createPartFromString("Lost"));
        } else {
            partMap.put("type", createPartFromString("Found"));
        }

        int itemImagesCount = pet.getPetImagesUrl().size();
        for (int i = 0; i < itemImagesCount; i++) {
            imageUri = Uri.fromFile(new File(pet.getPetImagesUrl().get(i)));
            petImagesPart.add(prepareFilePart("pet_images[]", imageUri));
        }
        return mApiInterface.reportPet(partMap, petImagesPart);
    }

    @Override
    public Single<VerifyMakatizenIdResponse> verifyMakatizenId(final String makatizenId) {
        return mMakatizenApiInterface.validateMakatizenNumber(makatizenId);
    }

    @Override
    public Single<GetLatestFeedResponse> getLatestFeed() {
        return mApiInterface.getLatestFeed();
    }

    @NonNull
    private RequestBody createPartFromString(String partString) {
        return RequestBody.create(
                okhttp3.MultipartBody.FORM, partString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = new File(fileUri.getEncodedPath());
        RequestBody requestFile = RequestBody.create(
                MediaType.parse("multipart/form-data"),
                file
        );
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
