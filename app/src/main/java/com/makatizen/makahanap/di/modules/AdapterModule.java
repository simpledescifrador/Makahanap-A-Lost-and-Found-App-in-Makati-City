package com.makatizen.makahanap.di.modules;

import android.content.Context;
import com.makatizen.makahanap.di.qualifiers.ActivityContext;
import com.makatizen.makahanap.di.scopes.ActivityScope;
import com.makatizen.makahanap.ui.image_viewer.ImageViewerAdapter;
import com.makatizen.makahanap.ui.intro.IntroAdapter;
import com.makatizen.makahanap.ui.report.adapter.ItemImagesAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class AdapterModule {

    @Provides
    @ActivityScope
    IntroAdapter provideIntroAdapter(@ActivityContext Context context) {
        return new IntroAdapter(context);
    }


    @Provides
    @ActivityScope
    ItemImagesAdapter provideItemImageAdapter(@ActivityContext Context context) {
        return new ItemImagesAdapter(context);
    }

    @Provides
    @ActivityScope
    ImageViewerAdapter provideImageViewerAdapter(@ActivityContext Context context) {
        return new ImageViewerAdapter(context);
    }
}
