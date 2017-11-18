package cn.e3mall.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.JsonUtils;
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

	private static final String CACHE_KEY_TEMPLATE = "ITEM_INFO:%s:DESC";

	@Autowired
	private TbItemDescMapper itemDescMapper;

	@Autowired
	private JedisClient jedisClient;

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
		String key = String.format(CACHE_KEY_TEMPLATE, itemId);
		// 查询缓存
		try {
			String json = jedisClient.get(key);
			if (StringUtils.isNotBlank(json)) {
				jedisClient.expire(key, 3600);
				TbItemDesc itemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return itemDesc;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		// 添加缓存
		if (itemDesc != null) {
			try {
				jedisClient.set(key, JsonUtils.objectToJson(itemDesc));
				jedisClient.expire(key, 3600);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return itemDesc;
	}

}
