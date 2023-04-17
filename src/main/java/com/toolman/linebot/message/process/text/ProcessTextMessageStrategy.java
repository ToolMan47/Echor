package com.toolman.linebot.message.process.text;

import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.toolman.linebot.message.process.ProcessStrategy;

public interface ProcessTextMessageStrategy extends ProcessStrategy <MessageEvent<TextMessageContent>>{
    
}
