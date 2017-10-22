package cn.e3mall.service;

import cn.e3mall.pojo.TbItemDesc;

public interface ItemDescService {

	TbItemDesc getItemDescByBarCode(String barcode);
	
	TbItemDesc getByItemId(long itemId);
	
}
