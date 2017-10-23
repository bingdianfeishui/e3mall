package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.EasyUITreeNode;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;

/**
 * 内容分类管理Service
 * <p>
 * Title: ContentCatagoryServiceImpl
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
 * @date 2017年10月23日下午10:28:57
 * @version 1.0
 */
@Service
public class ContentCatagoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCatMapper;

	@Override
	public List<EasyUITreeNode> getContentCatList(long parentId) {
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCatMapper.selectByExample(example);
		List<EasyUITreeNode> nodes = new ArrayList<>(list.size());
		for (TbContentCategory cat : list) {
			EasyUITreeNode e = new EasyUITreeNode();
			e.setId(cat.getId());
			e.setText(cat.getName());
			e.setState(cat.getIsParent() ? "closed" : "open");
			nodes.add(e);
		}

		return nodes;
	}

	@Override
	public E3Result addContentCategory(long parentId, String name) {
		TbContentCategory contentCat = new TbContentCategory();
		contentCat.setParentId(parentId);
		contentCat.setName(name);
		// 新加节点一定为叶子节点
		contentCat.setIsParent(false);
		// 状态 1--正常 0--删除
		contentCat.setStatus(1);
		// 默认排序为1
		contentCat.setSortOrder(1);
		contentCat.setCreated(new Date());
		contentCat.setUpdated(new Date());

		// 插入数据库
		contentCatMapper.insert(contentCat);

		// 对父节点isParent属性进行更新
		TbContentCategory parentContent = contentCatMapper.selectByPrimaryKey(parentId);
		if (!parentContent.getIsParent()) {
			parentContent.setIsParent(true);
			contentCatMapper.updateByPrimaryKey(parentContent);
		}
		Map<String, Long> map = new HashMap<>();
		map.put("id", contentCat.getId());
		return new E3Result(map);
	}

	@Override
	public TbContentCategory getContentCatByPrimaryKey(Long id) {
		return contentCatMapper.selectByPrimaryKey(id);
	}

	@Override
	public E3Result updateContentCategory(Long id, String name) {
		TbContentCategory content = new TbContentCategory();
		content.setId(id);
		content.setName(name);
		int i = contentCatMapper.updateByPrimaryKeySelective(content);
		if (i == 1)
			return E3Result.ok();
		else
			return new E3Result(500, "更新内容分类失败", null);
	}

}