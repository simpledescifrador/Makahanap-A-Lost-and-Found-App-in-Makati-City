package com.makatizen.makahanap.ui.report.personal_thing;

import com.makatizen.makahanap.pojo.PersonalThing;
import com.makatizen.makahanap.ui.base.BaseMvpView;
import com.makatizen.makahanap.ui.base.Presenter;
import com.makatizen.makahanap.utils.enums.Categories;
import com.makatizen.makahanap.utils.enums.Type;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

public interface ReportPersonalThingMvpPresenter<V extends ReportPersonalThingMvpView & BaseMvpView> extends
        Presenter<V> {

    void checkType(@NonNull Type type);

    void loadBarangayList();

    void selectCategory(Categories selectedCategory);

    void submitReport(@NonNull PersonalThing personalThing);

    boolean validateReport(@NonNull PersonalThing personalThing, @Nullable boolean isRewarded);
}
