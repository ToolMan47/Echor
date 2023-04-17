package com.toolman.linebot.service;

import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.message.Message;

import lombok.NonNull;

public interface HandlePostBackService {

    Message create(PostbackEvent event) throws Exception;
    
    void reply(@NonNull String replyToken, @NonNull Message message);
}
