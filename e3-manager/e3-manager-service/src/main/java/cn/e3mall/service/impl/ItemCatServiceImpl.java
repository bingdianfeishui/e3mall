package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.service.ItemCatService;

/**
 * 商品分类管理
 * <p>
 * Title: ItemCatServiceImpl
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
 * @date 2017年10月21日下午4:07:58
 * @version 1.0
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Override
	public List<EasyUITreeNode> getItemCat(long parentId) {
		TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		example.setOrderByClause("sort_order");
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		List<EasyUITreeNode> nodes = new ArrayList<>(list.size());
		for (TbItemCat cat : list) {
			EasyUITreeNode e = new EasyUITreeNode();
			e.setId(cat.getId());
			e.setText(cat.getName());
			e.setState(cat.getIsParent() ? EasyUITreeNode.STATE_CLOSED : EasyUITreeNode.STATE_OPEN);
			nodes.add(e);
		}
		return nodes;
	}

}
