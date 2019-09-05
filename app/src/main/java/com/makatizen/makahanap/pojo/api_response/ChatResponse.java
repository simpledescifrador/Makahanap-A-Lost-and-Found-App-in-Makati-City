package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.pojo.ChatItemV2;

import java.util.List;

public class ChatResponse {

    @SerializedName("chat_rooms")
    @Expose
    private List<ChatItemV2> chats = null;

    @SerializedName("total_chat_rooms")
    @Expose
    private Integer totalChatRooms;

    public List<ChatItemV2> getChats() {
        return chats;
    }

    public Integer getTotalChatRooms() {
        return totalChatRooms;
    }
}
