package cn.e3mall.controller;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

public class ItemControllerTest {

	@Test
	public void test() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/springmvc.xml");
		ItemService bean = context.getBean(ItemService.class);
		TbItem item = bean.getItemById(741524l);
		System.out.println(item);
		context.close();
	}

}
