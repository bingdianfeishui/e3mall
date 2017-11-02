package cn.e3mall.search.service.test;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.search.service.SearchItemService;

public class ImportSearchIndexTest {
	//@Test
	public void importAll() {
		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"classpath:spring/*.xml");
		SearchItemService searchItemService = applicationContext.getBean(SearchItemService.class);
		searchItemService.importAllItems();
		applicationContext.close();
	}
}
