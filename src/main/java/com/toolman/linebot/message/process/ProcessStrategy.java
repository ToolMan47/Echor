package com.toolman.linebot.message.process;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.message.Message;

// Line Event Process Strategy
public interface ProcessStrategy<T extends Event> {

    public Message process(T event);
}
