package cn.e3mall.controller;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.service.ItemCatService;

public class ItemCatControllerTest {

	//@Test
	public void test() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/springmvc.xml");
		ItemCatService bean = context.getBean(ItemCatService.class);
		List<EasyUITreeNode> nodes = bean.getItemCat(0);
		System.out.println(nodes);
		context.close();
	}

}
