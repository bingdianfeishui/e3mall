package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.e3mall.common.EasyUIDataGridResult;
import cn.e3mall.common.PageInfoCriteria;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

/**
 * 内容管理Service
 * <p>
 * Title: ContentCategoryServiceImpl
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
 * @date 2017年10月24日下午9:25:59
 * @version 1.0
 */
@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;

	@Autowired
	private JedisClient jedisClient;

	@Value("${REDIS.CONTENT.LIST_BY_CID}")
	private String REDIS_CONTENT_LIST_BY_CID;

	@Override
	public EasyUIDataGridResult selectListByCategoryId(int pageNum, int pageSize, Long categoryId) {
		PageInfoCriteria pageCriteria = new PageInfoCriteria(pageNum, pageSize);
		PageHelper.startPage(pageCriteria.getPageNum(), pageCriteria.getPageSize());

		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExampleWithBLOBs(example);
		EasyUIDataGridResult result = new EasyUIDataGridResult(page.getTotal(), page.getResult());
		return result;
	}

	@Override
	public E3Result insertContent(TbContent content) {
		content.setCreated(new Date());
		content.setUpdated(new Date());
		int i = contentMapper.insert(content);
		if (i == 1) {
			// 更新缓存：从hash对象中删除cid对应的缓存数据，下次查询该cid时从数据库获取
			try {
				jedisClient.hdel(REDIS_CONTENT_LIST_BY_CID, content.getCategoryId().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return E3Result.ok();
		}
		return new E3Result(HttpStatus.NOT_ACCEPTABLE.value(), "插入内容失败！", null);
	}

	@Override
	public E3Result updateContent(TbContent content) {
		int i = contentMapper.updateByPrimaryKeySelective(content);
		if (i == 1) {
			// 更新缓存：从hash对象中删除cid对应的缓存数据，下次查询该cid时从数据库获取
			try {
				jedisClient.hdel(REDIS_CONTENT_LIST_BY_CID, content.getCategoryId().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return E3Result.ok();
		}
		return new E3Result(HttpStatus.NOT_ACCEPTABLE.value(), "更新内容失败！", null);
	}

	@Override
	public E3Result deleteContents(Long[] ids) {
		if (ids != null && ids.length > 0) {
			contentMapper.deleteContentByIds(ids);
			// 更新缓存：从hash对象中删除cid对应的缓存数据，下次查询该cid时从数据库获取
			try {
				List<Long> cids = contentMapper.selectCidsByIds(ids);
				for (Long cid : cids) {
					jedisClient.hdel(REDIS_CONTENT_LIST_BY_CID, cid.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			return E3Result.ok();
		}
		return new E3Result(HttpStatus.NOT_ACCEPTABLE.value(), "删除内容失败！", null);
	}

	@Override
	public List<TbContent> selectListByCategoryId(Long categoryId) {
		try {
			// 通过categoryId在redis缓存的hash对象中查询是否存在缓存
			// redis中数据模型为 Hash[key=REDIS_CONTENT_LIST_BY_CID, field=cid, value=list2Json]
			String json = jedisClient.hget(REDIS_CONTENT_LIST_BY_CID, categoryId.toString());
			// 若存在缓存，直接响应结果
			if (StringUtils.isNotBlank(json)) {
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		try {
			// 将结果添加到缓存
			jedisClient.hset(REDIS_CONTENT_LIST_BY_CID, categoryId.toString(), JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
