package cn.e3mall.search.service;

import cn.e3mall.common.pojo.E3Result;

public interface SearchItemService {
	E3Result importAllItems();

	/**
	 * 根据ItemId查询Item，并以此更新索引
	 * 更新方式：直接插入。若存在相同id的索引，solr会先删除再插入。
	 * @param itemId
	 */
	void insertItemIndexById(long itemId);
}
