package cn.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/**
 * 首页展示Controller
 * <p>Title: IndexController</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: NULL.Co</p>
 * @author Lee
 * @date 2017年10月23日下午8:51:48
 * @version 1.0
 */
@Controller
public class IndexController {

	@Autowired
	private ContentService contentService;
	
	@Value("${CONTENT_LUNBO_CID}")
	private Long CONTENT_LUNBO_CID;
	
	@RequestMapping("/index")
	public String showIndex(Model model){
		//查询内容列表
		List<TbContent> ad1List = contentService.selectListByCategoryId(CONTENT_LUNBO_CID);
		model.addAttribute("ad1List", ad1List);
		
		return "index";
	}
}
