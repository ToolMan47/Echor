package com.toolman.linebot.message.process.post;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.PostbackEvent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Button.ButtonHeight;
import com.linecorp.bot.model.message.flex.component.Button.ButtonStyle;
import com.linecorp.bot.model.message.flex.component.Spacer;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.component.Text.TextWeight;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import com.toolman.linebot.dao.ReserveDao;
import com.toolman.linebot.entity.Reserve;
import com.toolman.linebot.message.process.text.ReserveStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AcceptReserveStrategy implements ProcessPostBackStrategy {

    @Autowired
    private ReserveDao reserveDao;
    
    @Autowired
    private ReserveStrategy reserveStrategy;

    @Autowired
    private LineMessagingClient lineMessagingClient;

    @Override
    public Message process(PostbackEvent event) {
	String result = getContent(event);
	Box head = createHeaderBox();
	Box body = createBodyBox(result);

	Bubble bubble = Bubble.builder().header(head).body(body).build();

	return new FlexMessage("預約結果", bubble);
    }

    public String getContent(PostbackEvent event) {
	String userId = event.getSource().getUserId();
	LocalDate reserveDate = LocalDate.parse(event.getPostbackContent().getParams().get("date"));
	
	// 預約檢核
	if (!validDate(reserveDate)) {
	    return reserveDate + "\t 此時段已被預約，請選擇其他時段";
	}

	// 預約成功
	lineMessagingClient.getProfile(userId).whenComplete((profile, exception) -> {
	    if (exception != null) {
		log.error("User:{} can not get profile from LINE, Error is:{}", userId, exception.getMessage());
	    }
	    Reserve newReserve = new Reserve();
	    newReserve.setUserId(userId);
	    newReserve.setUserName(profile.getDisplayName());
	    newReserve.setReserveDate(reserveDate);
	    reserveDao.save(newReserve);
	});

	return "已成功預約" + reserveDate;
    }

    private Box createHeaderBox() {
	Text title = Text.builder().text("預約結果").weight(TextWeight.BOLD).size(FlexFontSize.XL).build();

	return Box.builder().layout(FlexLayout.VERTICAL).contents(title).build();
    }

    private Box createBodyBox(String result) {
	Text text = Text.builder().text(result).color("#aaaaaa").size(FlexFontSize.SM).flex(1).build();

	return Box.builder().layout(FlexLayout.VERTICAL).content(text).build();
    }
    
    @SuppressWarnings("unused")
    private Box createFooterBox() {
	Spacer spacer = Spacer.builder().size(FlexMarginSize.SM).build();
	PostbackAction cancelAction = PostbackAction.builder().label("取消預約").data("CANCEL_RESERVE").build();

	Button dateButton = Button.builder()
		.style(ButtonStyle.SECONDARY)
		.height(ButtonHeight.SMALL)
		.action(cancelAction)
		.build();

	return Box.builder()
		.layout(FlexLayout.VERTICAL)
		.spacing(FlexMarginSize.SM)
		.contents(spacer, dateButton)
		.build();
    }
    
    
    private Boolean validDate(LocalDate reserveDate) {
	List<LocalDate> unreservedDate = reserveStrategy.getDate();
	if (unreservedDate.contains(reserveDate))
	    return true;
	return false;
    }

}
