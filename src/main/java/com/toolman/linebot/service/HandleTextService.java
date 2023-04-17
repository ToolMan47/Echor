package com.toolman.linebot.service;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;

import lombok.NonNull;

public interface HandleTextService {

	Message create(MessageEvent<TextMessageContent> event) throws Exception;
	
	void reply(@NonNull String replyToken, @NonNull Message message);
}
