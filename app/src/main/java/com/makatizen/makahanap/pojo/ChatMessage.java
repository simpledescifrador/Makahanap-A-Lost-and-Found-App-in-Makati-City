package com.makatizen.makahanap.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class ChatMessage {

    public static Comparator<ChatMessage> ByLatestMessageDate = new Comparator<ChatMessage>() {
        @Override
        public int compare(final ChatMessage message, final ChatMessage t1) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Date date1 = null;
            Date date2 = null;
            try {
                date1 = sdf.parse(message.getCreatedAt());
                date2 = sdf.parse(t1.getCreatedAt());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return date1.compareTo(date2);
        }
    };

    @SerializedName("account_id")
    @Expose
    private String accountId;

    @SerializedName("chat_room_id")
    @Expose
    private String chatRoomId;

    @SerializedName("created_at")
    @Expose
    private String createdAt;

    @SerializedName("id")
    @Expose
    private String id;

    private Boolean isSending;

    private Boolean isSent;

    @SerializedName("message")
    @Expose
    private String message;

    public ChatMessage() {
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(final String accountId) {
        this.accountId = accountId;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(final String chatRoomId) {
        this.chatRoomId = chatRoomId;
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

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public Boolean getSending() {
        return isSending;
    }

    public void setSending(final Boolean sending) {
        isSending = sending;
    }

    public Boolean getSent() {
        return isSent;
    }

    public void setSent(final Boolean sent) {
        isSent = sent;
    }
}
