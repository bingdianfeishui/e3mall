package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.search.service.SearchItemService;

/**
 * 搜索
 * 
 * @author 60238
 *
 */
@Controller
public class SearchController {
	@Autowired
	private SearchItemService searchItemService;

	@RequestMapping("/search")
	public String search() {
		return null;
	}
}
