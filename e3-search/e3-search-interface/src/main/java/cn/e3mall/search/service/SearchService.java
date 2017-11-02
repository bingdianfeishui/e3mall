package cn.e3mall.search.service;

import cn.e3mall.common.pojo.SearchResult;

public interface SearchService {
	SearchResult searchByKeywords(String keyword, int pageNum, int pageSize);
}
