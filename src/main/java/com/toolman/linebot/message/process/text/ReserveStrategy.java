package com.toolman.linebot.message.process.text;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.linecorp.bot.model.action.DatetimePickerAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Button.ButtonHeight;
import com.linecorp.bot.model.message.flex.component.Button.ButtonStyle;
import com.linecorp.bot.model.message.flex.component.FlexComponent;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Spacer;
import com.linecorp.bot.model.message.flex.component.Span;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.component.Text.TextWeight;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import com.toolman.linebot.dao.ReserveDao;

@Component
public class ReserveStrategy implements ProcessTextMessageStrategy {

    private final String[] WEEK_ARRAY = { "", "一", "二", "三", "四", "五", "六", "日" };

    @Autowired
    private ReserveDao reserveDao;

    @Override
    public Message process(MessageEvent<TextMessageContent> event) {
	Box head = createHeaderBox();
	Box body = createBodyBox();
	Box footer = createFooterBox();

	Bubble bubble = Bubble.builder().header(head).body(body).footer(footer).build();

	return new FlexMessage("預約日期", bubble);
    }

    @SuppressWarnings(value = { "unused" })
    private FlexComponent createHeroBlock() {

	return Image.builder().build();
    }

    private Box createHeaderBox() {
	Text title = Text.builder().text("預約日期").weight(TextWeight.BOLD).size(FlexFontSize.XL).build();

	return Box.builder().layout(FlexLayout.VERTICAL).contents(title).build();
    }

    private Box createBodyBox() {
	List<FlexComponent> content = getContent();

	return Box.builder().layout(FlexLayout.VERTICAL).contents(content).build();
    }

    private Box createFooterBox() {
	Spacer spacer = Spacer.builder().size(FlexMarginSize.SM).build();
	DatetimePickerAction<LocalDate> dateAction = DatetimePickerAction.OfLocalDate.builder()
		.label("預約日期")
		.data("RESERVED_DATA")
		.initial(LocalDate.now())
		.min(LocalDate.now())
		.max(LocalDate.now().plusWeeks(3))
		.build();

	Button dateButton = Button.builder()
		.style(ButtonStyle.SECONDARY)
		.height(ButtonHeight.SMALL)
		.action(dateAction)
		.build();

	return Box.builder()
		.layout(FlexLayout.VERTICAL)
		.spacing(FlexMarginSize.SM)
		.contents(spacer, dateButton)
		.build();
    }

    // 取得可預約日期
    public List<LocalDate> getDate() {
	LocalDate today = LocalDate.now();
	LocalDate lastday = today.plusWeeks(3);

	// get reserved
	List<LocalDate> reservedDate = reserveDao.findByReserveDateBetween(today, lastday)
		.stream()
		.map(reserve -> reserve.getReserveDate())
		.collect(Collectors.toList());

	// get unreserved
	List<LocalDate> unreservedDate = today.datesUntil(lastday.plusDays(1))
		.filter((date) -> date.getDayOfWeek() != DayOfWeek.SATURDAY)
		.filter((date) -> !reservedDate.contains(date))
		.collect(Collectors.toList());

	return unreservedDate;
    }

    // 組成內容
    private List<FlexComponent> getContent() {
	List<LocalDate> dateList = getDate();
	List<FlexComponent> contentList = IntStream.range(0, dateList.size())
		.boxed()
		.collect(Collectors.groupingBy(i -> i / 3))
		.values()
		.stream()
		.map(indices -> {
		    List<Span> spanList = indices.stream().map(dateList::get).map(date -> {
			StringBuilder sb = new StringBuilder();
			String monthdate = date.format(DateTimeFormatter.ofPattern("MM-dd"));
			String chineseWeekday = WEEK_ARRAY[date.getDayOfWeek().getValue()];

			return Span.builder()
				.text(sb.append(monthdate).append("(").append(chineseWeekday).append(")\t").toString())
				.weight(TextWeight.REGULAR)
				.size(FlexFontSize.Md)
				.build();
		    }).collect(Collectors.toList());

		    return Text.builder().contents(spanList).weight(TextWeight.BOLD).size(FlexFontSize.XL).build();
		})
		.collect(Collectors.toList());
	return contentList;
    }

}
