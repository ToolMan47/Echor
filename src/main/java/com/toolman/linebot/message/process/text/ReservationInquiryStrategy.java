package com.toolman.linebot.message.process.text;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linecorp.bot.model.action.PostbackAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Button.ButtonHeight;
import com.linecorp.bot.model.message.flex.component.Button.ButtonStyle;
import com.linecorp.bot.model.message.flex.component.FlexComponent;
import com.linecorp.bot.model.message.flex.component.Spacer;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.component.Text.TextWeight;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import com.toolman.linebot.dao.ReserveDao;
import com.toolman.linebot.entity.Reserve;

@Component
public class ReservationInquiryStrategy implements ProcessTextMessageStrategy {

    @Autowired
    private ReserveDao reserveDao;

    @Override
    public Message process(MessageEvent<TextMessageContent> event) {
	Box head = createHeaderBox();
	Box footer = createFooterBox(event);

	Bubble bubble = Bubble.builder().header(head).footer(footer).build();

	return new FlexMessage("預約管理", bubble);
    }

    private Box createHeaderBox() {
	Text title = Text.builder().text("預約管理").weight(TextWeight.BOLD).size(FlexFontSize.XL).build();

	return Box.builder().layout(FlexLayout.VERTICAL).contents(title).build();
    }

    private Box createFooterBox(MessageEvent<TextMessageContent> event) {
	List<Reserve> reserveList = reserveDao.findByUserIdAndReserveDateBetween(event.getSource().getUserId(),
		LocalDate.now(), LocalDate.now().plusWeeks(3));

	List<FlexComponent> buttons = getButtons(reserveList);

	return Box.builder().layout(FlexLayout.VERTICAL).spacing(FlexMarginSize.SM).contents(buttons).build();
    }

    List<FlexComponent> getButtons(List<Reserve> reserveList) {
	List<FlexComponent> buttons = new LinkedList<>();
	Spacer spacer = Spacer.builder().size(FlexMarginSize.SM).build();
	
	if (reserveList.isEmpty()) {
	    Text text = Text.builder().text("沒有預約時段").weight(TextWeight.REGULAR).size(FlexFontSize.Md).build();
	    buttons.add(text);
	}
	
	for (Reserve reserve : reserveList) {
	    LocalDate reserveDate = reserve.getReserveDate();

	    PostbackAction cancelAction = PostbackAction.builder()
		    .label("取消" + reserveDate)
		    .data("CANCEL_RESERVE=" + reserveDate)
		    .build();
	    Button dateButton = Button.builder()
		    .style(ButtonStyle.SECONDARY)
		    .height(ButtonHeight.SMALL)
		    .action(cancelAction)
		    .build();
	    buttons.add(dateButton);
	}
	
	buttons.add(0, spacer);

	return buttons;
    }

}
