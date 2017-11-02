package cn.e3mall.common.pojo;

import java.io.Serializable;

public class PageInfoCriteria implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 504248955907343620L;
	private Integer pageNum = 1;
	private Integer pageSize = 10;

	public PageInfoCriteria() {
	}

	public PageInfoCriteria(Integer pageNum, Integer pageSize) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		if (pageNum > 0)
			this.pageNum = pageNum;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize > 0 && pageSize <= 200)
			this.pageSize = pageSize;
	}
}
