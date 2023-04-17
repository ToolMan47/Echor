package com.group.artifact;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.PushMessage;
import com.linecorp.bot.model.message.TextMessage;
import com.toolman.linebot.LinebotApplication;
import com.toolman.linebot.dao.ProductDao;
import com.toolman.linebot.dao.StoreDao;
import com.toolman.linebot.entity.Product;
import com.toolman.linebot.entity.Store;

@SpringBootTest(classes = LinebotApplication.class)
class LinebotApplicationTests {

    @Autowired
    private ProductDao productDao;
    @Autowired
    private StoreDao storeDao;

    @Autowired
    private LineMessagingClient lineMessagingClient;

//    @Test
//    void contextLoads() {
//	System.out.println("Test");
//	List<FlexComponent> result = new PreserveStrategy().getContent();
//	
//	result.forEach(e -> System.out.println(e));
//
//	
//    }

    @Test
    @SuppressWarnings("unused")
    void tryStream() {
	System.out.println("tryStream");
	Collection<List<Integer>> aa = IntStream.range(0, 10)
		.boxed()
		.peek(e -> System.out.println("> " + e))
		.collect(Collectors.groupingBy(i -> i % 3))
		.values();

	List<List<Integer>> bb = aa.stream().peek(e -> System.out.println(">>> " + e)).collect(Collectors.toList());
    }

    @Test
    void tryList() {
	Store store = storeDao.findByName("XXX");
	Assert.assertNotNull(store);

	Product product1 = productDao.findByName("AAA");
	Assert.assertNotNull(product1);

	List<Product> productList = productDao.findAll();
	Assert.assertFalse(productList.isEmpty());
    }

    @Test
    void testPushMessage() {
	final String testId = "userId from line";
	lineMessagingClient.pushMessage(new PushMessage(testId, new TextMessage("From Echor"), false));
    }

}
