package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddChatMessageResponse {

    @SerializedName("message_sent")
    @Expose
    private Boolean messageSent;

    @SerializedName("message_time")
    @Expose
    private String messageTime;

    public String getMessageTime() {
        return messageTime;
    }

    public Boolean isMessageSent() {
        return messageSent;
    }
}
