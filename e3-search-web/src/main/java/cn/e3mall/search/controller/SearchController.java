package cn.e3mall.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.service.SearchService;

/**
 * 搜索
 * 
 * @author 60238
 *
 */
@Controller
public class SearchController {
	@Autowired
	private SearchService searchService;

	@Value("${SEARCH_RESULT.PAGESIZE}")
	private Integer pageSize = 10;

	@RequestMapping("/search")
	public String search(String keyword, @RequestParam(name = "page", defaultValue = "1") Integer pageNum,
			Model model) {
		//查询结果
		SearchResult result = searchService.searchByKeywords(keyword, pageNum, pageSize);
		//添加结果数据
		model.addAttribute("query", keyword);
		model.addAttribute("page", result.getPageInfo().getPageNum());
		model.addAttribute("recordCount", result.getRecordCount());
		model.addAttribute("itemList", result.getItemList());
		model.addAttribute("totalPages", result.getTotalPages());

		return "search";
	}
}
