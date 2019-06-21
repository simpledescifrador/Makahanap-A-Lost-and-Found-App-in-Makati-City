package com.makatizen.makahanap.di.modules;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import com.makatizen.makahanap.di.qualifiers.ActivityContext;
import com.makatizen.makahanap.di.scopes.ActivityScope;
import com.makatizen.makahanap.ui.intro.IntroMvpPresenter;
import com.makatizen.makahanap.ui.intro.IntroMvpView;
import com.makatizen.makahanap.ui.intro.IntroPresenter;
import com.makatizen.makahanap.ui.item_details.ItemDetailsMvpPresenter;
import com.makatizen.makahanap.ui.item_details.ItemDetailsMvpView;
import com.makatizen.makahanap.ui.item_details.ItemDetailsPresenter;
import com.makatizen.makahanap.ui.loader.LoaderMvpPresenter;
import com.makatizen.makahanap.ui.loader.LoaderMvpView;
import com.makatizen.makahanap.ui.loader.LoaderPresenter;
import com.makatizen.makahanap.ui.login.LoginMvpPresenter;
import com.makatizen.makahanap.ui.login.LoginMvpView;
import com.makatizen.makahanap.ui.login.LoginPresenter;
import com.makatizen.makahanap.ui.main.MainMvpPresenter;
import com.makatizen.makahanap.ui.main.MainMvpView;
import com.makatizen.makahanap.ui.main.MainPresenter;
import com.makatizen.makahanap.ui.main.feed.FeedMvpPresenter;
import com.makatizen.makahanap.ui.main.feed.FeedMvpView;
import com.makatizen.makahanap.ui.main.feed.FeedPresenter;
import com.makatizen.makahanap.ui.main.home.HomeMvpPresenter;
import com.makatizen.makahanap.ui.main.home.HomeMvpView;
import com.makatizen.makahanap.ui.main.home.HomePresenter;
import com.makatizen.makahanap.ui.register.RegisterMvpPresenter;
import com.makatizen.makahanap.ui.register.RegisterMvpView;
import com.makatizen.makahanap.ui.register.RegisterPresenter;
import com.makatizen.makahanap.ui.report.person.ReportPersonMvpPresenter;
import com.makatizen.makahanap.ui.report.person.ReportPersonMvpView;
import com.makatizen.makahanap.ui.report.person.ReportPersonPresenter;
import com.makatizen.makahanap.ui.report.personal_thing.ReportPersonalThingMvpPresenter;
import com.makatizen.makahanap.ui.report.personal_thing.ReportPersonalThingMvpView;
import com.makatizen.makahanap.ui.report.personal_thing.ReportPersonalThingPresenter;
import com.makatizen.makahanap.ui.report.pet.ReportPetMvpPresenter;
import com.makatizen.makahanap.ui.report.pet.ReportPetMvpView;
import com.makatizen.makahanap.ui.report.pet.ReportPetPresenter;
import dagger.Module;
import dagger.Provides;

@Module(includes = {AdapterModule.class, RxModule.class, UtilityModule.class})
public class ActivityFragmentModule {

    private Activity activity;

    public ActivityFragmentModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityContext
    Context provideActivityContext() {
        return activity;
    }

    @Provides
    @ActivityScope
    BottomSheetDialog provideBottomSheetDialog() {
        return new BottomSheetDialog(activity);
    }

    @Provides
    @ActivityScope
    FeedMvpPresenter<FeedMvpView> provideFeedPresenter(FeedPresenter<FeedMvpView> feedMvpViewFeedPresenter) {
        return feedMvpViewFeedPresenter;
    }

    @Provides
    @ActivityScope
    HomeMvpPresenter<HomeMvpView> provideHomePresenter(HomePresenter<HomeMvpView> homeMvpViewHomePresenter) {
        return homeMvpViewHomePresenter;
    }

    @Provides
    @ActivityScope
    IntroMvpPresenter<IntroMvpView> provideIntroPresenter(IntroPresenter<IntroMvpView> introMvpViewIntroPresenter) {
        return introMvpViewIntroPresenter;
    }

    @Provides
    @ActivityScope
    ItemDetailsMvpPresenter<ItemDetailsMvpView> provideItemDetailsPresenter(
            ItemDetailsPresenter<ItemDetailsMvpView> itemDetailsMvpViewItemDetailsPresenter) {
        return itemDetailsMvpViewItemDetailsPresenter;
    }

    @Provides
    @ActivityScope
    LoaderMvpPresenter<LoaderMvpView> provideLoaderPresenter(
            LoaderPresenter<LoaderMvpView> loaderMvpViewLoaderPresenter) {
        return loaderMvpViewLoaderPresenter;
    }

    @Provides
    @ActivityScope
    LoginMvpPresenter<LoginMvpView> provideLoginPresenter(LoginPresenter<LoginMvpView> loginMvpViewLoginPresenter) {
        return loginMvpViewLoginPresenter;
    }

    @Provides
    @ActivityScope
    MainMvpPresenter<MainMvpView> provideMainPresenter(MainPresenter<MainMvpView> mainMvpViewMainPresenter) {
        return mainMvpViewMainPresenter;
    }

    @Provides
    @ActivityScope
    RegisterMvpPresenter<RegisterMvpView> provideRegisterPresenter(
            RegisterPresenter<RegisterMvpView> registerMvpViewRegisterPresenter) {
        return registerMvpViewRegisterPresenter;
    }

    @Provides
    @ActivityScope
    ReportPersonMvpPresenter<ReportPersonMvpView> provideReportPersonPresenter(
            ReportPersonPresenter<ReportPersonMvpView> reportPersonMvpViewReportPersonPresenter) {
        return reportPersonMvpViewReportPersonPresenter;
    }

    @Provides
    @ActivityScope
    ReportPersonalThingMvpPresenter<ReportPersonalThingMvpView> provideReportPersonalThingPresenter(
            ReportPersonalThingPresenter<ReportPersonalThingMvpView> reportPersonalThingMvpViewReportPersonalThingPresenter) {
        return reportPersonalThingMvpViewReportPersonalThingPresenter;
    }

    @Provides
    @ActivityScope
    ReportPetMvpPresenter<ReportPetMvpView> provideReportPetPresenter(
            ReportPetPresenter<ReportPetMvpView> reportPetMvpViewReportPetPresenter) {
        return reportPetMvpViewReportPetPresenter;
    }
}
