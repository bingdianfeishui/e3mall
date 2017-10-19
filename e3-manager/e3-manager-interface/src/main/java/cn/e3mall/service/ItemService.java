package cn.e3mall.service;

import cn.e3mall.common.EasyUIDataGridResult;
import cn.e3mall.pojo.TbItem;

public interface ItemService {
	TbItem getItemById(Long id);

	/**
	 * 分页查询Item列表
	 * @param pageNum 页码，从1开始
	 * @param pageSize 每页项目数
	 * @return
	 */
	EasyUIDataGridResult getItemList(int pageNum, int pageSize);
}
