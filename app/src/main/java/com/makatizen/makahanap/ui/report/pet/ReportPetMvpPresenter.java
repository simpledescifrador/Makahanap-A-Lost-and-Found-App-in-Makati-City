package com.makatizen.makahanap.ui.report.pet;

import com.makatizen.makahanap.pojo.Pet;
import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;
import com.makatizen.makahanap.utils.enums.PetType;
import com.makatizen.makahanap.utils.enums.Type;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public interface ReportPetMvpPresenter<V extends ReportPetMvpView & BaseMvpView> extends Presenter<V> {

    void checkType(@NonNull Type type);

    void selectedPetType(PetType petType);

    void submitReport(Pet pet);

    boolean validateReport(@NonNull Pet pet, @Nullable boolean isRewarded);
}
