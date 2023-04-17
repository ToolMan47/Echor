package com.toolman.linebot.service.impl;

import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.ReplyMessage;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.response.BotApiResponse;
import com.toolman.linebot.message.Context;
import com.toolman.linebot.message.process.text.DefaultTextStrategy;
import com.toolman.linebot.message.process.text.ProductInfoStrategy;
import com.toolman.linebot.message.process.text.ReservationInquiryStrategy;
import com.toolman.linebot.message.process.text.ReserveStrategy;
import com.toolman.linebot.message.process.text.StoreInfoStrategy;
import com.toolman.linebot.service.HandleTextService;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HandleTextServiceImpl implements HandleTextService {

    private final static String STORE = "店家資訊";
    private final static String RESERVE = "預約時間";
    private final static String PRODUCT = "產品資訊";
    private final static String INQUIRY = "查詢預約"; // 預約管理
    
    @Autowired
    private StoreInfoStrategy storeInfoStrategy;
    @Autowired
    private ReserveStrategy reserveStrategy;
    @Autowired
    private ProductInfoStrategy productInfoStrategy;
    @Autowired
    private ReservationInquiryStrategy reservationInquiryStrategy;
    @Autowired
    private DefaultTextStrategy defaultTextStrategy;
    
    @Autowired
    private LineMessagingClient lineMessagingClient;

    @Override
    public Message create(MessageEvent<TextMessageContent> event) throws Exception {
	String received = event.getMessage().getText();
	Context<MessageEvent<TextMessageContent>> context = new Context<>();

	switch (received) {
	case STORE: {
	    context.setStrategy(storeInfoStrategy);
	    break;
	}
	case RESERVE: {
	    context.setStrategy(reserveStrategy);
	    break;
	}
	case PRODUCT: {
	    context.setStrategy(productInfoStrategy);
	    break;
	}
	case INQUIRY: {
	    context.setStrategy(reservationInquiryStrategy);
	    break;
	}
	default:
	    context.setStrategy(defaultTextStrategy);
	}

	return context.processStrategy(event);
    }

    @Override
    public void reply(@NonNull String replyToken, @NonNull Message message) {
	try {
	    BotApiResponse apiResponse = lineMessagingClient.replyMessage(new ReplyMessage(replyToken, message)).get();
	    log.info("Sent messages: {}", apiResponse);
	} catch (InterruptedException | ExecutionException e) {
	    throw new RuntimeException(e);
	}
    }

}
