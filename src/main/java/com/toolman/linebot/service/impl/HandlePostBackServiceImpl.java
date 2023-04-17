package com.toolman.linebot.service.impl;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.response.BotApiResponse;
import com.toolman.linebot.message.Context;
import com.toolman.linebot.message.process.post.AcceptReserveStrategy;
import com.toolman.linebot.message.process.post.CancelReservedStrategy;
import com.toolman.linebot.message.process.post.DefaultPostBackStrategy;
import com.toolman.linebot.service.HandlePostBackService;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HandlePostBackServiceImpl implements HandlePostBackService {
    
    private final static String RESERVED_DATA = "RESERVED_DATA";
    private final static String CANCEL_RESERVE = "CANCEL_RESERVE";
    
    @Autowired
    private AcceptReserveStrategy acceptReserveStrategy;
    @Autowired
    private CancelReservedStrategy cancelReservedStrategy;
    @Autowired
    private DefaultPostBackStrategy defaultPostBackStrategy;
    
    @Autowired
    private LineMessagingClient lineMessagingClient;
    
    public Message create(PostbackEvent event) throws Exception {
	String[] received = event.getPostbackContent().getData().split("=");
	Context<PostbackEvent> context = new Context<>();

	switch (received[0]) {
	case RESERVED_DATA: {
	    context.setStrategy(acceptReserveStrategy);
	    break;
	}
	case CANCEL_RESERVE: {
	    context.setStrategy(cancelReservedStrategy);
	    break;
	}
	default:
	    context.setStrategy(defaultPostBackStrategy);
	}

	return context.processStrategy(event);
    }

    public void reply(@NonNull String replyToken, @NonNull Message message) {
	try {
	    BotApiResponse apiResponse = lineMessagingClient.replyMessage(new ReplyMessage(replyToken, message)).get();
	    log.info("Sent messages: {}", apiResponse);
	} catch (InterruptedException | ExecutionException e) {
	    throw new RuntimeException(e);
	}
    }

}
