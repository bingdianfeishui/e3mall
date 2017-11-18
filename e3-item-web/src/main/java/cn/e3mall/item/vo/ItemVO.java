package cn.e3mall.item.vo;

import cn.e3mall.pojo.TbItem;

public class ItemVO extends TbItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = -583744017696042904L;

	public ItemVO(TbItem pojo) {
		this.setId(pojo.getId());
		this.setTitle(pojo.getTitle());
		this.setSellPoint(pojo.getSellPoint());
		this.setPrice(pojo.getPrice());
		this.setNum(pojo.getNum());
		this.setBarcode(pojo.getBarcode());
		this.setImage(pojo.getImage());
		this.setCid(pojo.getCid());
		this.setStatus(pojo.getStatus());
		this.setCreated(pojo.getCreated());
		this.setUpdated(pojo.getUpdated());
	}
	
	public String[] getImages() {
		return this.getImage().split(",");
	}
}
