package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.EasyUITreeNode;
import cn.e3mall.common.util.E3Result;
import cn.e3mall.pojo.TbContentCategory;

public interface ContentCategoryService {
	List<EasyUITreeNode> getContentCatList(long parentId);

	E3Result addContentCategory(long parentId, String name);

	TbContentCategory getContentCatByPrimaryKey(Long id);

	E3Result updateContentCategory(Long id, String name);

}
