package com.makatizen.makahanap.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.utils.enums.ChatType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ChatItem {

    public static Comparator<ChatItem> byLatestMessageDate = new Comparator<ChatItem>() {
        @Override
        public int compare(final ChatItem chatItem, final ChatItem t1) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if (chatItem.getLastestMessage().getCreatedAt() == null || t1.getLastestMessage().getCreatedAt() == null) {
                return 0;
            }
            Date date1 = null;
            Date date2 = null;

            try {
                date1 = sdf.parse(chatItem.getLastestMessage().getCreatedAt());
                date2 = sdf.parse(t1.getLastestMessage().getCreatedAt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date2.compareTo(date1);
        }
    };

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("id")
    @Expose
    private String id;

    private Boolean isViewed = false;

    @SerializedName("lastest_message")
    @Expose
    private ChatMessage lastestMessage;

    @SerializedName("participants")
    @Expose
    private List<MakahanapAccount> participants = null;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("type")
    @Expose
    private ChatType type;

    public ChatItem() {
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final String createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public ChatMessage getLastestMessage() {
        return lastestMessage;
    }

    public void setLastestMessage(final ChatMessage lastestMessage) {
        this.lastestMessage = lastestMessage;
    }

    public List<MakahanapAccount> getParticipants() {
        return participants;
    }

    public void setParticipants(final List<MakahanapAccount> participants) {
        this.participants = participants;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }

    public ChatType getType() {
        return type;
    }

    public void setType(final ChatType type) {
        this.type = type;
    }

    public Boolean isViewed() {
        return isViewed;
    }

    public void setIsViewed(final Boolean viewed) {
        isViewed = viewed;
    }
}
