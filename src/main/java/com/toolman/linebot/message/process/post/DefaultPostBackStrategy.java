package com.toolman.linebot.message.process.post;

import org.springframework.stereotype.Component;

import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.message.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DefaultPostBackStrategy implements ProcessPostBackStrategy {

    @Override
    public Message process(PostbackEvent event) {
	log.info("Default PostBack\nUserId: {};\nData : {};\nParams : {}", event.getSource().getUserId(),
		event.getPostbackContent().getData(), event.getPostbackContent().getParams());
	return null;
    }

}
