package cn.e3mall.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.EasyUIDataGridResult;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

/**
 * 商品管理Controller
 * <p>Title: ItemController</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: NULL.Co</p>
 * @author Lee
 * @date 2017年10月22日下午8:03:24
 * @version 1.0
 */

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;

	@RequestMapping("/item/{itemId}")
	public @ResponseBody TbItem getItemById(@PathVariable Long itemId, HttpSession session) {
		TbItem tbItem = itemService.getItemById(itemId);
		// 热部署session持久化测试
		if(itemId == 1L)
			session.setAttribute("key", new Date());
		System.out.println(itemId + "\t===" + session.getAttribute("key") + "==+"+session.getId());
		System.out.println(session.getAttributeNames());
		return tbItem;
	}
	
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(int page, int rows){
		return itemService.getItemList(page, rows);
	}
	
	@RequestMapping("/rest/page/item-edit")
	public String showEditForm(Integer id){
		return "item-edit";
	}
	
	@RequestMapping("/item/save")
	@ResponseBody
	public E3Result saveItem(TbItem item, String desc){
		boolean result = itemService.addItem(item, desc);
		return result? E3Result.ok() : new E3Result(500, "保存商品失败！", null);
	}
	
	@RequestMapping("/rest/item/update")
	@ResponseBody
	public E3Result updateItem(TbItem item, String desc){
		boolean result = itemService.updateItemAndDesc(item, desc);
		return result? E3Result.ok() : new E3Result(500, "更新商品失败！", null);
	}
	
	@RequestMapping("/rest/item/instock")
	@ResponseBody
	public E3Result instockItem(TbItem item){
		return null;
	}
	@RequestMapping("/rest/item/reshelf")
	@ResponseBody
	public E3Result reshelfItem(TbItem item){
		return null;
	}
	
	
}
