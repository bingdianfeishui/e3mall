package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

@Repository
public class SearchDao {
	@Autowired
	private SolrClient client;

	public SearchResult search(SolrQuery query) {
		SearchResult result = new SearchResult();
		result.setItemList(new ArrayList<SearchItem>());
		try {
			// 查询索引库
			QueryResponse queryResponse = client.query(query);
			SolrDocumentList documentList = queryResponse.getResults();
			// 取查询结果总记录数
			result.setRecordCount(documentList.getNumFound());

			// 取高亮
			Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
			// 取查询结果
			for (SolrDocument doc : documentList) {
				SearchItem item = new SearchItem();
				item.setId((String) doc.get("id"));
				item.setCatagory_name((String) doc.get("item_catagory_name"));
				item.setImage((String) doc.get("item_image"));
				item.setPrice((Long) doc.get("item_price"));
				item.setSell_point((String) doc.get("item_sell_point"));

				// 设置高亮(可能没有高亮)
				List<String> highlightList = highlighting.get(doc.get("id")).get("item_title");
				if (highlightList != null && highlightList.size() > 0)
					item.setTitle(highlightList.get(0));
				else
					item.setTitle((String) doc.get("item_title"));

				// 添加SearchItem进查询结果
				result.getItemList().add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}
}
