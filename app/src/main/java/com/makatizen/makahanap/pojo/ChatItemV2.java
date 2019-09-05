package com.makatizen.makahanap.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.utils.enums.ChatType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ChatItemV2 {
    public static Comparator<ChatItemV2> byLatestMessageDate = new Comparator<ChatItemV2>() {
        @Override
        public int compare(ChatItemV2 chatItem, ChatItemV2 t1) {
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

    @SerializedName("item_id")
    @Expose
    private Integer itemId;

    @SerializedName("item_title")
    @Expose
    private String itemTitle;

    public ChatItemV2() {
    }

    public String getItemTitle() {
        return itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getViewed() {
        return isViewed;
    }

    public void setViewed(Boolean viewed) {
        isViewed = viewed;
    }

    public ChatMessage getLastestMessage() {
        return lastestMessage;
    }

    public void setLastestMessage(ChatMessage lastestMessage) {
        this.lastestMessage = lastestMessage;
    }

    public List<MakahanapAccount> getParticipants() {
        return participants;
    }

    public void setParticipants(List<MakahanapAccount> participants) {
        this.participants = participants;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ChatType getType() {
        return type;
    }

    public void setType(ChatType type) {
        this.type = type;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}
