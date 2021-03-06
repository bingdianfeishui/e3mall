package cn.e3mall.common.pojo;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -833055022615037521L;
	private long recordCount;
	private int totalPages;
	private List<SearchItem> itemList;

	private PageInfoCriteria pageInfo;

	public long getRecordCount() {
		return recordCount;
	}

	public void setRecordCount(long recordCount) {
		this.recordCount = recordCount;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public List<SearchItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<SearchItem> itemList) {
		this.itemList = itemList;
	}

	public PageInfoCriteria getPageInfo() {
		return pageInfo;
	}

	public void setPageInfo(PageInfoCriteria pageInfo) {
		this.pageInfo = pageInfo;
		if (this.recordCount > 0 && pageInfo != null)
			this.totalPages = (int) ((this.recordCount + pageInfo.getPageSize() - 1) / pageInfo.getPageSize());
	}
}
