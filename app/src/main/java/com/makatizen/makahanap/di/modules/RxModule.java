package com.makatizen.makahanap.di.modules;

import com.makatizen.makahanap.utils.rx.SchedulerProvider;
import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class RxModule {

    @Provides
    static CompositeDisposable provideCompositeDisposable() {
        return new CompositeDisposable();
    }


    @Provides
    static SchedulerProvider provideSchedulerProvider() {
        return new SchedulerProvider();
    }
}
