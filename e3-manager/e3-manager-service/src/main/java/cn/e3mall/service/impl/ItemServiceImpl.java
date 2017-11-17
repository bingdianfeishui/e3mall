package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.pojo.PageInfoCriteria;
import cn.e3mall.common.util.IDUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
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

	protected static final String PREFIX_UPDATE = "U";
	protected static final String PREFIX_DELETE = "D";
	protected static final String SEPARATOR = ":";

	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbItemDescMapper itemDescMapper;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public TbItem getItemById(Long id) {
		// return itemMapper.selectByPrimaryKey(id);

		System.out.println("==============" + id);
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

		// PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		// //第二种方案，利用PageInfo解析list后获取信息
		return new EasyUIDataGridResult(pageResult.getTotal(), pageResult);
	}

	@Override
	public boolean addItem(TbItem item, String desc) {
		try {
			// 生成商品ID
			long itemId = IDUtils.genItemId();
			// 补全item属性并保存
			item.setId(itemId);
			// 商品状态，1-正常，2-下架，3-删除
			item.setStatus((byte) 1);
			Date now = new Date();
			item.setCreated(now);
			item.setUpdated(now);

			// 创建ItemDesc对象并保存
			TbItemDesc itemDesc = new TbItemDesc();
			itemDesc.setItemDesc(desc);
			itemDesc.setItemId(itemId);
			itemDesc.setCreated(now);
			itemDesc.setUpdated(now);

			itemMapper.insert(item);
			itemDescMapper.insert(itemDesc);

			// 发布item更新消息
			try {

				final long id = item.getId();
				jmsTemplate.send(new MessageCreator() {

					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(PREFIX_UPDATE + SEPARATOR + id);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateItemAndDesc(TbItem item, String desc) {
		if (item == null)
			return false;
		try {
			TbItem oldItem = itemMapper.selectByPrimaryKey(item.getId());

			if (oldItem == null)
				return false;

			// 商品状态，1-正常，2-下架，3-删除
			item.setStatus(oldItem.getStatus());
			Date now = new Date();
			item.setCreated(oldItem.getCreated());
			item.setUpdated(now);

			// 创建ItemDesc对象并保存
			TbItemDesc oldItemDesc = itemDescMapper.selectByPrimaryKey(item.getId());
			if (oldItemDesc == null) {
				// 若不存在则创建新的
				TbItemDesc itemDesc = new TbItemDesc();
				itemDesc.setItemDesc(desc);
				itemDesc.setItemId(item.getId());
				itemDesc.setCreated(now);
				itemDesc.setUpdated(now);

				itemDescMapper.insert(itemDesc);
			} else {
				oldItemDesc.setItemDesc(desc);
				oldItemDesc.setUpdated(now);
				itemDescMapper.updateByPrimaryKeyWithBLOBs(oldItemDesc);
			}
			itemMapper.updateByPrimaryKey(item);

			// 发布item更新消息
			try {

				final long id = item.getId();
				jmsTemplate.send(new MessageCreator() {

					@Override
					public Message createMessage(Session session) throws JMSException {
						return session.createTextMessage(PREFIX_UPDATE + SEPARATOR + id);
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean updateItemStatusByIds(String ids, byte status) {
		// status 商品状态，1-正常，2-下架，3-删除
		String[] idsStrArr = ids.split(",");
		List<Long> idsList = new ArrayList<>(idsStrArr.length);
		try {
			for (String idsStr : idsStrArr) {
				idsList.add(Long.valueOf(idsStr));
			}
			itemMapper.updateItemsStatus(idsList.toArray(new Long[0]), status);

			// 批量发布item更新消息
			try {
				final String idsMsg = ids;
				MessageCreator msgCreator = null;
				if (status == 1) {
					// 添加或更新
					msgCreator = new MessageCreator() {

						@Override
						public Message createMessage(Session session) throws JMSException {
							return session.createTextMessage(PREFIX_UPDATE + SEPARATOR + idsMsg);
						}
					};
				} else {
					// 删除
					msgCreator = new MessageCreator() {

						@Override
						public Message createMessage(Session session) throws JMSException {
							return session.createTextMessage(PREFIX_DELETE + SEPARATOR + idsMsg);
						}
					};
				}
				jmsTemplate.send(msgCreator);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
