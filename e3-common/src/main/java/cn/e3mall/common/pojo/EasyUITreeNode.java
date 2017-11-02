package cn.e3mall.common.pojo;

import java.io.Serializable;

public class EasyUITreeNode implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2098126314725349937L;

	public static final String STATE_OPEN="open";
	public static final String STATE_CLOSED="closed";
	
	private long id;
	private String text;
	private String state;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Override
	public String toString() {
		return "EasyUITreeNode [id=" + id + ", text=" + text + ", state=" + state + "]";
	}
	
}
