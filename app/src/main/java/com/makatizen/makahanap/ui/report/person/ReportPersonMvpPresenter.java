package com.makatizen.makahanap.ui.report.person;

import com.makatizen.makahanap.pojo.Person;
import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;
import com.makatizen.makahanap.utils.enums.AgeGroup;
import com.makatizen.makahanap.utils.enums.Type;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public interface ReportPersonMvpPresenter<V extends ReportPersonMvpView & BaseMvpView> extends Presenter<V> {

    void checkType(@NonNull Type type);

    void submitReport(Person person);

    void selectedAgeGroup(AgeGroup ageGroup);

    boolean validateReport(@NonNull Person person, @Nullable boolean isRewarded);
}
