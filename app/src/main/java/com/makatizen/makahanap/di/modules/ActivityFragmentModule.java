package com.makatizen.makahanap.di.modules;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.BottomSheetDialog;

import com.makatizen.makahanap.di.qualifiers.ActivityContext;
import com.makatizen.makahanap.di.scopes.ActivityScope;
import com.makatizen.makahanap.ui.chat.ChatMvpPresenter;
import com.makatizen.makahanap.ui.chat.ChatMvpView;
import com.makatizen.makahanap.ui.chat.ChatPresenter;
import com.makatizen.makahanap.ui.chat_convo.ChatConvoMvpPresenter;
import com.makatizen.makahanap.ui.chat_convo.ChatConvoMvpView;
import com.makatizen.makahanap.ui.chat_convo.ChatConvoPresenter;
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
import com.makatizen.makahanap.ui.main.account.AccountMvpPresenter;
import com.makatizen.makahanap.ui.main.account.AccountMvpView;
import com.makatizen.makahanap.ui.main.account.AccountPresenter;
import com.makatizen.makahanap.ui.main.account.about.AccountAboutMvpPresenter;
import com.makatizen.makahanap.ui.main.account.about.AccountAboutMvpView;
import com.makatizen.makahanap.ui.main.account.about.AccountAboutPresenter;
import com.makatizen.makahanap.ui.main.account.gallery.AccountGalleryMvpPresenter;
import com.makatizen.makahanap.ui.main.account.gallery.AccountGalleryMvpView;
import com.makatizen.makahanap.ui.main.account.gallery.AccountGalleryPresenter;
import com.makatizen.makahanap.ui.main.account.reports.AccountReportsMvpPresenter;
import com.makatizen.makahanap.ui.main.account.reports.AccountReportsMvpView;
import com.makatizen.makahanap.ui.main.account.reports.AccountReportsPresenter;
import com.makatizen.makahanap.ui.main.feed.FeedMvpPresenter;
import com.makatizen.makahanap.ui.main.feed.FeedMvpView;
import com.makatizen.makahanap.ui.main.feed.FeedPresenter;
import com.makatizen.makahanap.ui.main.home.HomeMvpPresenter;
import com.makatizen.makahanap.ui.main.home.HomeMvpView;
import com.makatizen.makahanap.ui.main.home.HomePresenter;
import com.makatizen.makahanap.ui.main.map.MapMvpPresenter;
import com.makatizen.makahanap.ui.main.map.MapMvpView;
import com.makatizen.makahanap.ui.main.map.MapPresenter;
import com.makatizen.makahanap.ui.main.notification.NotificationMvpPresenter;
import com.makatizen.makahanap.ui.main.notification.NotificationMvpView;
import com.makatizen.makahanap.ui.main.notification.NotificationPresenter;
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
import com.makatizen.makahanap.ui.search.SearchMvpPresenter;
import com.makatizen.makahanap.ui.search.SearchMvpView;
import com.makatizen.makahanap.ui.search.SearchPresenter;
import com.makatizen.makahanap.ui.transaction.MeetTransactionMvpPresenter;
import com.makatizen.makahanap.ui.transaction.MeetTransactionMvpView;
import com.makatizen.makahanap.ui.transaction.MeetTransactionPresenter;

import dagger.Module;
import dagger.Provides;

@Module(includes = {AdapterModule.class, RxModule.class, UtilityModule.class})
public class ActivityFragmentModule {

    private Activity activity;

    public ActivityFragmentModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    static AccountAboutMvpPresenter<AccountAboutMvpView> provideAccountAboutPresenter(
            AccountAboutPresenter<AccountAboutMvpView> accountAboutMvpViewAccountAboutPresenter) {
        return accountAboutMvpViewAccountAboutPresenter;
    }

    @Provides
    @ActivityScope
    static AccountGalleryMvpPresenter<AccountGalleryMvpView> provideAccountGalleryPresenter(
            AccountGalleryPresenter<AccountGalleryMvpView> accountGalleryMvpViewAccountGalleryPresenter) {
        return accountGalleryMvpViewAccountGalleryPresenter;
    }

    @Provides
    @ActivityScope
    static AccountReportsMvpPresenter<AccountReportsMvpView> provideAccountReportsPresenter(
            AccountReportsPresenter<AccountReportsMvpView> accountReportsMvpViewAccountReportsPresenter) {
        return accountReportsMvpViewAccountReportsPresenter;
    }

    @Provides
    @ActivityScope
    static ChatConvoMvpPresenter<ChatConvoMvpView> provideChatConvoPresenter(
            ChatConvoPresenter<ChatConvoMvpView> chatConvoMvpViewChatConvoPresenter) {
        return chatConvoMvpViewChatConvoPresenter;
    }

    @Provides
    @ActivityScope
    static ChatMvpPresenter<ChatMvpView> provideChatPresenter(ChatPresenter<ChatMvpView> chatMvpViewChatPresenter) {
        return chatMvpViewChatPresenter;
    }

    @Provides
    @ActivityScope
    static FeedMvpPresenter<FeedMvpView> provideFeedPresenter(FeedPresenter<FeedMvpView> feedMvpViewFeedPresenter) {
        return feedMvpViewFeedPresenter;
    }

    @Provides
    @ActivityScope
    static HomeMvpPresenter<HomeMvpView> provideHomePresenter(HomePresenter<HomeMvpView> homeMvpViewHomePresenter) {
        return homeMvpViewHomePresenter;
    }

    @Provides
    @ActivityScope
    static IntroMvpPresenter<IntroMvpView> provideIntroPresenter(
            IntroPresenter<IntroMvpView> introMvpViewIntroPresenter) {
        return introMvpViewIntroPresenter;
    }

    @Provides
    @ActivityScope
    static ItemDetailsMvpPresenter<ItemDetailsMvpView> provideItemDetailsPresenter(
            ItemDetailsPresenter<ItemDetailsMvpView> itemDetailsMvpViewItemDetailsPresenter) {
        return itemDetailsMvpViewItemDetailsPresenter;
    }

    @Provides
    @ActivityScope
    static LoaderMvpPresenter<LoaderMvpView> provideLoaderPresenter(
            LoaderPresenter<LoaderMvpView> loaderMvpViewLoaderPresenter) {
        return loaderMvpViewLoaderPresenter;
    }

    @Provides
    @ActivityScope
    static LoginMvpPresenter<LoginMvpView> provideLoginPresenter(
            LoginPresenter<LoginMvpView> loginMvpViewLoginPresenter) {
        return loginMvpViewLoginPresenter;
    }

    @Provides
    @ActivityScope
    static MainMvpPresenter<MainMvpView> provideMainPresenter(MainPresenter<MainMvpView> mainMvpViewMainPresenter) {
        return mainMvpViewMainPresenter;
    }

    @Provides
    @ActivityScope
    static NotificationMvpPresenter<NotificationMvpView> provideNotificationPresenter(
            NotificationPresenter<NotificationMvpView> notificationMvpViewNotificationPresenter) {
        return notificationMvpViewNotificationPresenter;
    }

    @Provides
    @ActivityScope
    static RegisterMvpPresenter<RegisterMvpView> provideRegisterPresenter(
            RegisterPresenter<RegisterMvpView> registerMvpViewRegisterPresenter) {
        return registerMvpViewRegisterPresenter;
    }

    @Provides
    @ActivityScope
    static ReportPersonMvpPresenter<ReportPersonMvpView> provideReportPersonPresenter(
            ReportPersonPresenter<ReportPersonMvpView> reportPersonMvpViewReportPersonPresenter) {
        return reportPersonMvpViewReportPersonPresenter;
    }

    @Provides
    @ActivityScope
    static ReportPersonalThingMvpPresenter<ReportPersonalThingMvpView> provideReportPersonalThingPresenter(
            ReportPersonalThingPresenter<ReportPersonalThingMvpView> reportPersonalThingMvpViewReportPersonalThingPresenter) {
        return reportPersonalThingMvpViewReportPersonalThingPresenter;
    }

    @Provides
    @ActivityScope
    static ReportPetMvpPresenter<ReportPetMvpView> provideReportPetPresenter(
            ReportPetPresenter<ReportPetMvpView> reportPetMvpViewReportPetPresenter) {
        return reportPetMvpViewReportPetPresenter;
    }

    @Provides
    @ActivityScope
    static MapMvpPresenter<MapMvpView> provideMapPresenter(MapPresenter<MapMvpView> mapMvpViewMapPresenter) {
        return mapMvpViewMapPresenter;
    }

    @Provides
    @ActivityScope
    AccountMvpPresenter<AccountMvpView> provideAccountPresenter(
            AccountPresenter<AccountMvpView> accountMvpViewAccountPresenter) {
        return accountMvpViewAccountPresenter;
    }

    @Provides
    @ActivityScope
    SearchMvpPresenter<SearchMvpView> provideSearchPresenter(SearchPresenter<SearchMvpView> searchMvpViewSearchPresenter) {
        return searchMvpViewSearchPresenter;
    }

    @Provides
    @ActivityScope
    MeetTransactionMvpPresenter<MeetTransactionMvpView> provideMeetTransactionPresenter(MeetTransactionPresenter<MeetTransactionMvpView> meetTransactionMvpViewMeetTransactionPresenter) {
        return meetTransactionMvpViewMeetTransactionPresenter;
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


}
