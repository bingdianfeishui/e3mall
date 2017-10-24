package cn.e3mall.content.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.e3mall.common.EasyUIDataGridResult;
import cn.e3mall.common.PageInfoCriteria;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

/**
 * 内容管理Service
 * <p>Title: ContentCategoryServiceImpl</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2017</p>
 * <p>Company: NULL.Co</p>
 * @author Lee
 * @date 2017年10月24日下午9:25:59
 * @version 1.0
 */
@Service
public class ContentServiceImpl implements ContentService{

	@Autowired
	private TbContentMapper contentMapper;
	
	@Override
	public EasyUIDataGridResult selectListByCategoryId(int pageNum, int pageSize, Long categoryId) {
		PageInfoCriteria pageCriteria = new PageInfoCriteria(pageNum, pageSize);
		PageHelper.startPage(pageCriteria.getPageNum(), pageCriteria.getPageSize());
		
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(example);
		EasyUIDataGridResult result = new EasyUIDataGridResult(page.getTotal(), page.getResult());
		return result;
	}

	@Override
	public E3Result insertContent(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		int i = contentMapper.insert(content);
		if(i == 1)
			return E3Result.ok();
		return new E3Result(HttpStatus.NOT_ACCEPTABLE.value(), "插入内容失败！", null);
	}

}
