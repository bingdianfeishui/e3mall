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
	
	/**
	 * 添加商品及商品描述
	 * @param item TbItem对象，无Id
	 * @param desc String，desc字符串
	 * @return boolean, true--保存成功, false--保存失败
	 */
	boolean addItem(TbItem item, String desc);

	/**
	 * 更新商品信息及商品描述
	 * @param item
	 * @param desc
	 * @return
	 */
	boolean updateItemAndDesc(TbItem item, String desc);

	/**
	 * 根据id字符串更新商品状态status<p/>
	 * 商品状态，1-正常，2-下架，3-删除
	 * @param ids id字符串，多个id以','隔开
	 * @param i
	 * @return
	 */
	boolean updateItemStatusByIds(String ids, byte status);
}
