package com.makatizen.makahanap.di.modules;

import android.app.Activity;
import com.makatizen.makahanap.di.scopes.ActivityScope;
import com.makatizen.makahanap.utils.AppAlertDialog;
import com.makatizen.makahanap.utils.ImageUtils;
import com.makatizen.makahanap.utils.PermissionUtils;
import dagger.Module;
import dagger.Provides;

@Module
public class UtilityModule {

    private Activity activity;

    public UtilityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    AppAlertDialog provideAppDialogUtils() {
        return new AppAlertDialog(activity);
    }

    @Provides
    @ActivityScope
    ImageUtils provideCameraUtils() {
        return new ImageUtils(activity);
    }

    @Provides
    @ActivityScope
    PermissionUtils providePermissionUtils() {
        return new PermissionUtils(activity);
    }
}
