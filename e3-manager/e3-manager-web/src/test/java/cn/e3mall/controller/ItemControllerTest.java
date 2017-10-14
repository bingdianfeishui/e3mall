package cn.e3mall.controller;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration("classpath:applicationContext-*.xml")
public class ItemControllerTest {

	@Test
	public void testGetItemById() {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
		ItemService obj = context.getBean(ItemService.class);
		System.out.println(obj);
		TbItemMapper mapper = context.getBean(TbItemMapper.class);
		System.out.println(mapper);
		TbItem item = mapper.selectByPrimaryKey(536563L);
		System.out.println(item);
		context.close();
	}

}
