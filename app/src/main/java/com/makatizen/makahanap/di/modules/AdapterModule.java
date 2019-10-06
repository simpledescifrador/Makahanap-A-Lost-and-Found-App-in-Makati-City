package com.makatizen.makahanap.di.modules;

import android.content.Context;

import com.makatizen.makahanap.di.qualifiers.ActivityContext;
import com.makatizen.makahanap.di.scopes.ActivityScope;
import com.makatizen.makahanap.ui.chat.ChatAdapter;
import com.makatizen.makahanap.ui.chat.ChatItemAdapter;
import com.makatizen.makahanap.ui.chat_convo.ChatConvoAdapter;
import com.makatizen.makahanap.ui.image_viewer.ImageViewerAdapter;
import com.makatizen.makahanap.ui.intro.IntroAdapter;
import com.makatizen.makahanap.ui.item_details.ImageSliderAdapter;
import com.makatizen.makahanap.ui.main.account.gallery.ImageGalleryAdapter;
import com.makatizen.makahanap.ui.main.account.reports.AccountReportsAdapter;
import com.makatizen.makahanap.ui.main.feed.FeedAdapter;
import com.makatizen.makahanap.ui.main.map.MapItemsAdapter;
import com.makatizen.makahanap.ui.main.notification.NotificationAdapter;
import com.makatizen.makahanap.ui.report.adapter.ItemImagesAdapter;
import com.makatizen.makahanap.ui.search.SearchAdapter;

import dagger.Module;
import dagger.Provides;
import retrofit2.http.POST;

@Module
public class AdapterModule {

    @Provides
    @ActivityScope
    static AccountReportsAdapter provideAccountReportsAdapter(@ActivityContext Context context) {
        return new AccountReportsAdapter(context);
    }

    @Provides
    @ActivityScope
    static ChatConvoAdapter provideChatConvoAdapter(@ActivityContext Context context) {
        return new ChatConvoAdapter(context);
    }

    @Provides
    @ActivityScope
    static FeedAdapter provideFeedAdapter(@ActivityContext Context context) {
        return new FeedAdapter(context);
    }

    @Provides
    @ActivityScope
    static ImageGalleryAdapter provideImageGalleryAdapter(@ActivityContext Context context) {
        return new ImageGalleryAdapter(context);
    }

    @Provides
    @ActivityScope
    static ImageViewerAdapter provideImageViewerAdapter(@ActivityContext Context context) {
        return new ImageViewerAdapter(context);
    }

    @Provides
    @ActivityScope
    static IntroAdapter provideIntroAdapter(@ActivityContext Context context) {
        return new IntroAdapter(context);
    }

    @Provides
    @ActivityScope
    static ItemImagesAdapter provideItemImageAdapter(@ActivityContext Context context) {
        return new ItemImagesAdapter(context);
    }

    @Provides
    @ActivityScope
    static NotificationAdapter provideNotificationAdapter(@ActivityContext Context context) {
        return new NotificationAdapter(context);
    }

    @Provides
    @ActivityScope
    static SearchAdapter provideSearchAdapter(@ActivityContext Context context) {
        return new SearchAdapter(context);
    }

    @Provides
    @ActivityScope
    ChatAdapter provideChatAdapter(@ActivityContext Context context) {
        return new ChatAdapter(context);
    }

    @Provides
    @ActivityScope
    ImageSliderAdapter provideImageSliderAdapter(@ActivityContext Context context) {
        return new ImageSliderAdapter(context);
    }

    @Provides
    @ActivityScope
    ChatItemAdapter provideChatItemAdapter(@ActivityContext Context context) {
        return new ChatItemAdapter(context);
    }

    @Provides
    @ActivityScope
    MapItemsAdapter provideMapItemsAdapter(@ActivityContext Context context) {
        return new MapItemsAdapter(context);
    }

}
