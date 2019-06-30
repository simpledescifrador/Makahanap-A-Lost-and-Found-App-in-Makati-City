package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makatizen.makahanap.pojo.ChatMessage;
import java.util.List;

public class ChatMessagesResponse {

    @SerializedName("messages")
    @Expose
    private List<ChatMessage> chatMessageList;

    @SerializedName("total_messages")
    @Expose
    private Integer totalMessages;

    public List<ChatMessage> getChatMessageList() {
        return chatMessageList;
    }

    public Integer getTotalMessages() {
        return totalMessages;
    }
}
