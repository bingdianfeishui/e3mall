package cn.e3mall.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemDescService;

/**
 * 商品描述Service
 * <p>
 * Title: ItemDescServiceImpl
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
 * @date 2017年10月22日下午10:11:42
 * @version 1.0
 */
@Service
public class ItemDescServiceImpl implements ItemDescService {

	@Autowired
	private TbItemDescMapper itemDescMapper;

	@Override
	public TbItemDesc getItemDescByBarCode(String barcode) {
		if (StringUtils.isBlank(barcode))
			return null;

		barcode = barcode.trim();
		// 根据barcode获取ItemDesc
		TbItemDesc itemDesc = itemDescMapper.selectByItemBarCode(barcode);
		return itemDesc;
	}

	@Override
	public TbItemDesc getByItemId(long itemId) {
		return itemDescMapper.selectByPrimaryKey(itemId);
	}

}
