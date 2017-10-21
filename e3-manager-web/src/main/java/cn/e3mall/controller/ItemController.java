package cn.e3mall.controller;

import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.service.ItemService;

/**
 * 商品管理Controller
 * <p>
 * Title: ItemController
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @version 1.0
 * @author Lee
 *
 */

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;

	@RequestMapping("/item/{itemId}")
	public @ResponseBody TbItem getItemById(@PathVariable Long itemId, HttpSession session) {
		TbItem tbItem = itemService.getItemById(itemId);
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
	
	@RequestMapping("rest/page/item-edit")
	public String showEditForm(Integer id){
		return "item-edit";
	}
}
