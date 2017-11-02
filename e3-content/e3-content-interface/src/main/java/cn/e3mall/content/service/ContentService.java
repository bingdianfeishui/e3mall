package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.E3Result;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.pojo.TbContent;

public interface ContentService {
	EasyUIDataGridResult selectListByCategoryId(int pageNum, int pageSize, Long categoryId);
	List<TbContent> selectListByCategoryId(Long categoryId);
	E3Result insertContent(TbContent content);
	E3Result updateContent(TbContent content);
	E3Result deleteContents(Long[] ids);
}
