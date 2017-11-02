package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemDescService;

/**
 * 商品描述Controller
 * <p>
 * Title: ItemDescController
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
 * @date 2017年10月22日下午10:06:31
 * @version 1.0
 */
@Controller
public class ItemDescController {

	@Autowired
	private ItemDescService itemDescService;

	@RequestMapping("/rest/item/query/item/desc/{itemId}")
	@ResponseBody
	public E3Result queryByBarCode(@PathVariable("itemId") long itemId) {
		TbItemDesc itemDesc = itemDescService.getByItemId(itemId);
		return E3Result.ok(itemDesc);
	}
}
