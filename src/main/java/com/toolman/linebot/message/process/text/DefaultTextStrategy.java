package com.toolman.linebot.message.process.text;

import org.springframework.stereotype.Component;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class DefaultTextStrategy implements ProcessTextMessageStrategy {

    @Override
    public Message process(MessageEvent<TextMessageContent> event) {
	log.info("userId: {};\n msg:{}", event.getSource().getSenderId(), event.getMessage().getText());
	return null;
    }

}
