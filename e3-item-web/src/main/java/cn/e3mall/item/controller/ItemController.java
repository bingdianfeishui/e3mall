package cn.e3mall.item.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.item.vo.ItemVO;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemDescService;
import cn.e3mall.service.ItemService;

@Controller
public class ItemController {
	@Autowired
	private ItemService itemService;

	@Autowired
	private ItemDescService itemDescService;

	@RequestMapping("item/{itemId}.html")
	public String getItemDetail(@PathVariable Long itemId, Model model) {
		TbItem tbItem = itemService.getItemById(itemId);
		TbItemDesc itemDesc = itemDescService.getByItemId(itemId);
		
		ItemVO item = new ItemVO(tbItem);

		model.addAttribute("item", item);
		model.addAttribute("itemDesc", itemDesc);

		return "item";
	}
}
