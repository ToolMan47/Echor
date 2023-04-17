package com.toolman.linebot.handler;

import org.springframework.beans.factory.annotation.Autowired;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;
import com.toolman.linebot.service.HandlePostBackService;
import com.toolman.linebot.service.HandleTextService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@LineMessageHandler
public class EchoHandler {

    @Autowired
    private HandleTextService handleTextService;

    @Autowired
    private HandlePostBackService handlePostBackService;

    @EventMapping
    public void handleTextMessageEvent(MessageEvent<TextMessageContent> event) throws Exception {
	String replyToken = event.getReplyToken();
	Message replyMsg = handleTextService.create(event);

	if (replyMsg != null) {
	    handleTextService.reply(replyToken, replyMsg);
	}
    }

    @EventMapping
    public void handlePostbackEvent(PostbackEvent event) throws Exception {
	log.info("PostBack: {}" , event);
	String replyToken = event.getReplyToken();
	Message replyMsg = handlePostBackService.create(event);
	handlePostBackService.reply(replyToken, replyMsg);
    }

}
