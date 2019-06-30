package com.makatizen.makahanap.di.modules;

import com.makatizen.makahanap.service.MyFirebaseMessagingService;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ServiceModule {

    @ContributesAndroidInjector
    abstract MyFirebaseMessagingService contributeMyFirebaseMessagingService();
}
