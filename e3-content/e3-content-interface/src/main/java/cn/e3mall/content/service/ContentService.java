package cn.e3mall.content.service;

import cn.e3mall.common.EasyUIDataGridResult;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.pojo.TbContent;

public interface ContentService {
	EasyUIDataGridResult selectListByCategoryId(int pageNum, int pageSize, Long categoryId);
	E3Result insertContent(TbContent content);
}
