package com.toolman.linebot.message.process.text;

import static java.util.Arrays.asList;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.FlexMessage;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.flex.component.Box;
import com.linecorp.bot.model.message.flex.component.Button;
import com.linecorp.bot.model.message.flex.component.Button.ButtonHeight;
import com.linecorp.bot.model.message.flex.component.Button.ButtonStyle;
import com.linecorp.bot.model.message.flex.component.FlexComponent;
import com.linecorp.bot.model.message.flex.component.Icon;
import com.linecorp.bot.model.message.flex.component.Image;
import com.linecorp.bot.model.message.flex.component.Image.ImageAspectMode;
import com.linecorp.bot.model.message.flex.component.Image.ImageAspectRatio;
import com.linecorp.bot.model.message.flex.component.Image.ImageSize;
import com.linecorp.bot.model.message.flex.component.Separator;
import com.linecorp.bot.model.message.flex.component.Text;
import com.linecorp.bot.model.message.flex.component.Text.TextWeight;
import com.linecorp.bot.model.message.flex.container.Bubble;
import com.linecorp.bot.model.message.flex.unit.FlexFontSize;
import com.linecorp.bot.model.message.flex.unit.FlexLayout;
import com.linecorp.bot.model.message.flex.unit.FlexMarginSize;
import com.toolman.linebot.dao.StoreDao;
import com.toolman.linebot.entity.Store;

import jakarta.annotation.PostConstruct;

@Component
public class StoreInfoStrategy implements ProcessTextMessageStrategy {
    
    @Autowired
    private StoreDao storeDao;
    
    private Store store;
    
    @PostConstruct
    private void setStore() {
	store = storeDao.findByName("XXX");
    }

    @Override
    public Message process(MessageEvent<TextMessageContent> event) {
	FlexComponent hero = createHeroBlock();
	Box body = createBodyBox();
	Box footer = createFooterBox();

	Bubble bubble = Bubble.builder().hero(hero).body(body).footer(footer).build();

	return new FlexMessage("店家資訊", bubble);
    }

    private FlexComponent createHeroBlock() {
	return Image.builder()
		.url(createUri(store.getImgPath()))
		.size(ImageSize.FULL_WIDTH)
		.aspectRatio(ImageAspectRatio.R20TO13)
		.aspectMode(ImageAspectMode.Cover)
//		.action(new URIAction("label", URI.create("http://example.com"), null))
		.build();

    }

    @SuppressWarnings(value = { "unused" })
    private Box createHeaderBox() {
	return Box.builder().build();
    }

    private Box createBodyBox() {
	final Text title = Text.builder().text(store.getName()).weight(TextWeight.BOLD).size(FlexFontSize.XL).build();

	final Box review = createReviewBox();

	final Box info = createInfoBox();

	return Box.builder().layout(FlexLayout.VERTICAL).contents(title, review, info).build();
    }

    private Box createFooterBox() {
        final Button callAction = Button
                .builder()
                .style(ButtonStyle.LINK)
                .height(ButtonHeight.SMALL)
                .action(new URIAction("電話", URI.create("tel:" + store.getTel()), null))
                .build();
        final Button websiteAction =
                Button.builder()
                      .style(ButtonStyle.LINK)
                      .height(ButtonHeight.SMALL)
                      .action(new URIAction("網站", URI.create("https://example.com"), null))
                      .build();

        return Box.builder()
                  .layout(FlexLayout.VERTICAL)
                  .spacing(FlexMarginSize.SM)
                  .contents(asList(Separator.builder().build(), callAction, Separator.builder().build(), websiteAction))
                  .build();
    }

    private Box createInfoBox() {
	final Box place = Box.builder()
		.layout(FlexLayout.BASELINE)
		.spacing(FlexMarginSize.SM)
		.contents(Text.builder().text("地址").color("#aaaaaa").size(FlexFontSize.SM).flex(2).build(),
			Text.builder()
				.text(store.getAddress())
				.wrap(true)
				.color("#666666")
				.size(FlexFontSize.SM)
				.flex(5)
				.build())
		.build();
	final Box time = Box.builder()
		.layout(FlexLayout.BASELINE)
		.spacing(FlexMarginSize.SM)
		.contents(Text.builder().text("營業時間").color("#aaaaaa").size(FlexFontSize.SM).flex(2).build(),
			Text.builder()
				.text(store.getOpenTime() + "-" + store.getCloseTime())
				.wrap(true)
				.color("#666666")
				.size(FlexFontSize.SM)
				.flex(5)
				.build())
		.build();

	return Box.builder()
		.layout(FlexLayout.VERTICAL)
		.margin(FlexMarginSize.LG)
		.spacing(FlexMarginSize.SM)
		.contents(asList(place, time))
		.build();
    }

    private Box createReviewBox() {
	final Icon goldStar = Icon.builder()
		.size(FlexFontSize.SM)
		.url(URI.create("https://scdn.line-apps.com/n/channel_devcenter/img/fx/review_gold_star_28.png"))
		.build();
//        final Icon grayStar =
//                Icon.builder().size(FlexFontSize.SM).url(URI.create("https://example.com/gray_star.png")).build();
	final Text point = Text.builder()
		.text("5.0")
		.size(FlexFontSize.SM)
		.color("#999999")
		.margin(FlexMarginSize.MD)
		.flex(0)
		.build();

	return Box.builder()
		.layout(FlexLayout.BASELINE)
		.margin(FlexMarginSize.MD)
		.contents(asList(goldStar, goldStar, goldStar, goldStar, goldStar, point))
		.build();
    }

    private URI createUri(String path) {
	return ServletUriComponentsBuilder.fromCurrentContextPath()
		.scheme("https")
		.path("/store/" + path)
		.build()
		.toUri();
    }

}
