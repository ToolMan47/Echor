package com.toolman.linebot.message.process.post;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.toolman.linebot.dao.ReserveDao;
import com.toolman.linebot.entity.Reserve;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CancelReservedStrategy implements ProcessPostBackStrategy {
    
    @Autowired
    private ReserveDao reserveDao;

    @Override
    public Message process(PostbackEvent event) {
	log.info("CancelReservedStrategy UserId:{}", event.getSource().getUserId());
	String userId = event.getSource().getUserId();
	String receiveData = event.getPostbackContent().getData();
	LocalDate reservedDate = LocalDate.parse(receiveData.split("=")[1]);
	
	Reserve reserve = reserveDao.findByUserIdAndReserveDate(userId, reservedDate);
	reserveDao.delete(reserve);
	return new TextMessage("已取消" + reservedDate + "預約");
    }

}
