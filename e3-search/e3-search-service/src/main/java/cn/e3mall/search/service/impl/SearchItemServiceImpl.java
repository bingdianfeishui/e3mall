package cn.e3mall.search.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.search.mapper.SearchItemMapper;
import cn.e3mall.search.service.SearchItemService;

@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private SearchItemMapper itemMapper;
	@Autowired
	private SolrClient solrClient;

	@Override
	public E3Result importAllItems() {
		try {
			// 查询商品列表
			List<SearchItem> itemList = itemMapper.getSearchItemList();
			// 遍历创建SolrInputDocument
			List<SolrInputDocument> docs = new ArrayList<>();
			for (SearchItem item : itemList) {
				SolrInputDocument doc = new SolrInputDocument();
				// <field name="item_title" type="text_ik" indexed="true" stored="true"/>
				// <field name="item_sell_point" type="text_ik" indexed="true" stored="true"/>
				// <field name="item_price" type="long" indexed="true" stored="true"/>
				// <field name="item_image" type="string" indexed="false" stored="true" />
				// <field name="item_category_name" type="string" indexed="true" stored="true"
				// />
				doc.setField("id", item.getId());
				doc.setField("item_title", item.getTitle());
				doc.setField("item_sell_point", item.getSell_point());
				doc.setField("item_price", item.getPrice());
				doc.setField("item_image", item.getImage());
				doc.setField("item_category_name", item.getCatagory_name());
				docs.add(doc);
			}
			solrClient.add(docs);
			solrClient.commit();
			return E3Result.ok();
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		return new E3Result(HttpStatus.INTERNAL_SERVER_ERROR.value(), "创建索引失败！", null);
	}

	@Override
	public void updateItemIndexById(long itemId) {
		SearchItem item = itemMapper.getSearchItemById(itemId);
		if (item != null) {
			// 查找到item，则更新索引
			SolrInputDocument doc = new SolrInputDocument();
			doc.setField("id", item.getId());
			doc.setField("item_title", item.getTitle());
			doc.setField("item_sell_point", item.getSell_point());
			doc.setField("item_price", item.getPrice());
			doc.setField("item_image", item.getImage());
			doc.setField("item_category_name", item.getCatagory_name());
			try {
				solrClient.add(doc);
				solrClient.commit();
			} catch (SolrServerException | IOException e) {
				e.printStackTrace();
			}
		} else {
			// 若找不到item，说明是被禁用了，需要删除索引
			try {
				solrClient.deleteById(itemId + "");
				solrClient.commit();
			} catch (SolrServerException | IOException e) {
				e.printStackTrace();
			}
		}
	}
}
