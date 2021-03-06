package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.search.service.SearchItemService;

/**
 * 索引
 * 
 * @author 60238
 *
 */
@Controller
public class SearchItemController {
	@Autowired
	private SearchItemService searchItemService;

	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3Result importAll() {
		return searchItemService.importAllItems();
	}
}
