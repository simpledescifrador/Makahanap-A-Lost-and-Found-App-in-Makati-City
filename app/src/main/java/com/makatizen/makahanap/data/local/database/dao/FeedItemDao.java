package com.makatizen.makahanap.data.local.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import com.makatizen.makahanap.pojo.FeedItem;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.util.List;

@Dao
public interface FeedItemDao {
    @Insert
    void addAllFeedItems(List<FeedItem> feedItemList);

    @Update
    void updateFeedItem(FeedItem feedItem);

    @Delete
    void deleteFeedItem(FeedItem feedItem);

    @Insert
    void addFeedItem(FeedItem feedItem);

    @Query("SELECT * FROM feed_item")
    Observable<List<FeedItem>> getAllFeedItems();

    @Query("SELECT * FROM feed_item WHERE itemId=:itemId")
    Single<FeedItem> getFeedItem(int itemId);
}
