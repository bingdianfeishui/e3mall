package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.EasyUIDataGridResult;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/**
 * 内容管理Controller
 * <p>Title: ContentController</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: NULL.Co</p>
 * @author Lee
 * @date 2017年10月24日下午9:20:15
 * @version 1.0
 */
@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult listContent(int page, int rows, Long categoryId){
		
		return contentService.selectListByCategoryId(page, rows, categoryId);
	}
	
	@RequestMapping(value="/content/save")
	@ResponseBody
	public E3Result addContent(TbContent content){
		
		return contentService.insertContent(content);
	}
}
