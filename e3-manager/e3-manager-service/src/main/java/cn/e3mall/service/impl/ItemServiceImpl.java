package cn.e3mall.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.e3mall.common.EasyUIDataGridResult;
import cn.e3mall.common.PageInfoCriteria;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.service.ItemService;

/**
 * 商品管理Service实现类
 * <p>
 * Title: ItemServiceImpl
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @version 1.0
 * @author Lee
 *
 */
@Component
public class ItemServiceImpl implements ItemService {

	@Autowired
	private TbItemMapper itemMapper;

	@Override
	public TbItem getItemById(Long id) {
		// return itemMapper.selectByPrimaryKey(id);
		
		System.out.println("=============="+id);
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(id);
		List<TbItem> list = itemMapper.selectByExample(example);
		if (list != null && list.size() > 0)
			return list.get(0);
		else
			return null;
	}

	@Override
	public EasyUIDataGridResult getItemList(int pageNum, int pageSize) {
		PageInfoCriteria pageCriteria = new PageInfoCriteria(pageNum, pageSize);
		PageHelper.startPage(pageCriteria.getPageNum(), pageCriteria.getPageSize());
		TbItemExample example = new TbItemExample();
		Page<TbItem> pageResult = (Page<TbItem>) itemMapper.selectByExample(example);

		// PageInfo<TbItem> pageInfo = new PageInfo<>(list);	//第二种方案，利用PageInfo解析list后获取信息
		return new EasyUIDataGridResult(pageResult.getTotal(), pageResult);
	}
}
