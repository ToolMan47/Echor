package com.toolman.linebot.message.process.text;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.linecorp.bot.model.action.URIAction;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TemplateMessage;
import com.linecorp.bot.model.message.template.CarouselColumn;
import com.linecorp.bot.model.message.template.CarouselTemplate;
import com.toolman.linebot.dao.ProductDao;
import com.toolman.linebot.entity.Product;

// Product Carousel
@Component
public class ProductInfoStrategy implements ProcessTextMessageStrategy {

    @Autowired
    private ProductDao productDao;

    @Override
    public Message process(MessageEvent<TextMessageContent> event) {

	// get product
	List<Product> productList = productDao.findAll();

	List<CarouselColumn> col = new ArrayList<>();

	for (Product product : productList) {

	    col.add(new CarouselColumn(createUri(product.getImgPath()), product.getName(),
		    String.valueOf(product.getPrice()),
		    Arrays.asList(new URIAction(product.getName(), URI.create("https://line.me"), null)))); // Action
													    // can't be
													    // null
	}
	return new TemplateMessage("Carousel alt text", new CarouselTemplate(col));
    }

    private URI createUri(String path) {
	return ServletUriComponentsBuilder.fromCurrentContextPath()
		.scheme("https")
		.path("/products/" + path)
		.build()
		.toUri();
    }

//	URI imageUrl = createUri("/static/buttons/1040.jpg");
//	CarouselTemplate carouselTemplate = new CarouselTemplate(
//			Arrays.asList(
//					new CarouselColumn(imageUrl, "hoge", "fuga",
//							Arrays.asList(new URIAction("Go to line.me", URI.create("https://line.me"), null),
//									new URIAction("Go to line.me", URI.create("https://line.me"), null),
//									new PostbackAction("Say hello1", "hello こんにちは"))),
//					new CarouselColumn(imageUrl, "hoge", "fuga",
//							Arrays.asList(new PostbackAction("言 hello2", "hello こんにちは", "hello こんにちは"),
//									new PostbackAction("言 hello2", "hello こんにちは", "hello こんにちは"),
//									new MessageAction("Say message", "Rice=米"))),
//					new CarouselColumn(imageUrl, "Datetime Picker", "Please select a date, time or datetime",
//							Arrays.asList(DatetimePickerAction.OfLocalDatetime.builder().label("Datetime")
//									.data("action=sel").initial(LocalDateTime.parse("2017-06-18T06:15"))
//									.min(LocalDateTime.parse("1900-01-01T00:00"))
//									.max(LocalDateTime.parse("2100-12-31T23:59")).build(),
//									DatetimePickerAction.OfLocalDate.builder().label("Date")
//											.data("action=sel&only=date").initial(LocalDate.parse("2017-06-18"))
//											.min(LocalDate.parse("1900-01-01")).max(LocalDate.parse("2100-12-31"))
//											.build(),
//									DatetimePickerAction.OfLocalTime.builder().label("Time")
//											.data("action=sel&only=time").initial(LocalTime.parse("06:15"))
//											.min(LocalTime.parse("00:00")).max(LocalTime.parse("23:59")).build()))));

}
