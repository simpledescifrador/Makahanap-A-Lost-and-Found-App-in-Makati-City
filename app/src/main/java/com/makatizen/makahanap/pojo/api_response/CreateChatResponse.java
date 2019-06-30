package com.makatizen.makahanap.pojo.api_response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateChatResponse {

    @SerializedName("chat_id")
    @Expose
    private Integer chatId;

    @SerializedName("success")
    @Expose
    private Boolean success;

    public Integer getChatId() {
        return chatId;
    }

    public Boolean isSuccess() {
        return success;
    }
}
