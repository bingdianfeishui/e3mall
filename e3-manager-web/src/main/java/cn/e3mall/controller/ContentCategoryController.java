package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.content.service.ContentCategoryService;

/**
 * 内容分类管理Controller
 * <p>
 * Title: ContentCategoryController
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2017
 * </p>
 * <p>
 * Company: NULL.Co
 * </p>
 * 
 * @author Lee
 * @date 2017年10月23日下午10:28:51
 * @version 1.0
 */
@Controller
public class ContentCategoryController {
	@Autowired
	private ContentCategoryService contentCategoryService;

	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> list(@RequestParam(name = "id", defaultValue = "0") long parentId) {
		return contentCategoryService.getContentCatList(parentId);
	}

	@RequestMapping("/content/category/create")
	@ResponseBody
	public E3Result createContentCat(long parentId, String name) {
		return  contentCategoryService.addContentCategory(parentId, name);
	}
	
	@RequestMapping("/content/category/update")
	@ResponseBody
	public E3Result updateContentCat(Long id, String name) {
		return  contentCategoryService.updateContentCategory(id, name);
	}
	@RequestMapping("/content/category/delete")
	@ResponseBody
	public E3Result deleteContentCat(Long id) {
		return  contentCategoryService.deleteContentCategory(id);
	}
}
