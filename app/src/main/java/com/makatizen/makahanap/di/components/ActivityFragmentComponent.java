package com.makatizen.makahanap.di.components;

import com.makatizen.makahanap.di.modules.ActivityFragmentModule;
import com.makatizen.makahanap.di.scopes.ActivityScope;
import com.makatizen.makahanap.ui.image_viewer.ImageViewerActivity;
import com.makatizen.makahanap.ui.intro.IntroActivity;
import com.makatizen.makahanap.ui.item_details.ItemDetailsActivity;
import com.makatizen.makahanap.ui.loader.LoaderActivity;
import com.makatizen.makahanap.ui.login.LoginActivity;
import com.makatizen.makahanap.ui.main.MainActivity;
import com.makatizen.makahanap.ui.main.account.AccountFragment;
import com.makatizen.makahanap.ui.main.feed.FeedFragment;
import com.makatizen.makahanap.ui.main.home.HomeFragment;
import com.makatizen.makahanap.ui.main.map.MapFragment;
import com.makatizen.makahanap.ui.main.notification.NotificationFragment;
import com.makatizen.makahanap.ui.register.RegisterActivity;
import com.makatizen.makahanap.ui.report.person.ReportPersonActivity;
import com.makatizen.makahanap.ui.report.personal_thing.ReportPersonalThingActivity;
import com.makatizen.makahanap.ui.report.pet.ReportPetActivity;
import dagger.Component;

@ActivityScope
@Component(dependencies = ApplicationComponent.class, modules = ActivityFragmentModule.class)
public interface ActivityFragmentComponent {

    void inject(IntroActivity introActivity);

    void inject(RegisterActivity registerActivity);

    void inject(LoaderActivity loaderActivity);

    void inject(LoginActivity loginActivity);

    void inject(MainActivity mainActivity);

    void inject(HomeFragment homeFragment);

    void inject(FeedFragment feedFragment);

    void inject(MapFragment mapFragment);

    void inject(NotificationFragment notificationFragment);

    void inject(AccountFragment accountFragment);

    void inject(ReportPersonalThingActivity reportPersonalThingActivity);

    void inject(ImageViewerActivity imageViewerActivity);

    void inject(ReportPetActivity reportPetActivity);

    void inject(ReportPersonActivity reportPersonActivity);

    void inject(ItemDetailsActivity itemDetailsActivity);
}
