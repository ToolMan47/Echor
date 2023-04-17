package com.toolman.linebot.message;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.message.Message;
import com.toolman.linebot.message.process.ProcessStrategy;

// Strategy Pattern with generic
public class Context<T extends Event> {

    // compose
    private ProcessStrategy<T> strategy;

    public void setStrategy(ProcessStrategy<T> strategy) {
	this.strategy = strategy;
    }

    public Message processStrategy(T event) {
	return strategy.process(event);
    }

}
