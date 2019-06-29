package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.pojo.ChatItem;
import java.util.List;

public class ChatResponse {

    @SerializedName("chat_rooms")
    @Expose
    private List<ChatItem> chats = null;

    @SerializedName("total_chat_rooms")
    @Expose
    private Integer totalChatRooms;

    public List<ChatItem> getChatRooms() {
        return chats;
    }

    public Integer getTotalChatRooms() {
        return totalChatRooms;
    }
}
