package com.makatizen.makahanap.di.modules;

import android.content.Context;
import com.makatizen.makahanap.di.qualifiers.ActivityContext;
import com.makatizen.makahanap.di.scopes.ActivityScope;
import com.makatizen.makahanap.ui.image_viewer.ImageViewerAdapter;
import com.makatizen.makahanap.ui.intro.IntroAdapter;
import com.makatizen.makahanap.ui.item_details.ImageSliderAdapter;
import com.makatizen.makahanap.ui.main.feed.FeedAdapter;
import com.makatizen.makahanap.ui.report.adapter.ItemImagesAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class AdapterModule {

    @Provides
    @ActivityScope
    FeedAdapter provideFeedAdapter(@ActivityContext Context context) {
        return new FeedAdapter(context);
    }

    @Provides
    @ActivityScope
    ImageViewerAdapter provideImageViewerAdapter(@ActivityContext Context context) {
        return new ImageViewerAdapter(context);
    }

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
    ImageSliderAdapter provideImageSliderAdapter(@ActivityContext Context context) {
        return new ImageSliderAdapter(context);
    }
}
