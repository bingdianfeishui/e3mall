package cn.e3mall.search.service.impl;

import org.apache.solr.client.solrj.SolrQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.PageInfoCriteria;
import cn.e3mall.common.pojo.SearchResult;
import cn.e3mall.search.dao.SearchDao;
import cn.e3mall.search.service.SearchService;

@Service
public class SearchServiceImpl implements SearchService {

	@Autowired
	private SearchDao searchDao;

	@Override
	public SearchResult searchByKeywords(String keyword, int pageNum, int pageSize) {
		SolrQuery query = new SolrQuery();
		// 查询条件
		query.setQuery(keyword);
		query.setFields("id", "item_title", "item_sell_point", "item_price", "item_image", "item_category_name");

		// 分页条件
		PageInfoCriteria pageInfo = new PageInfoCriteria(pageNum, pageSize);
		query.setStart((pageInfo.getPageNum() - 1) * pageInfo.getPageSize());
		query.setRows(pageInfo.getPageSize());

		// 默认搜索域
		query.set("df", "item_title");

		// 高亮
		query.setHighlight(true);
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em style='color:red'>");
		query.setHighlightSimplePost("</em>");

		// 查询
		SearchResult searchResult = searchDao.search(query);
		
		searchResult.setPageInfo(pageInfo);// 自动计算totalPages
		// int a=1/0; //全局异常处理测试
		return searchResult;
	}

}
