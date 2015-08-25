package com.piaoyou.domain;

import java.io.Serializable;

public class Category  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7371123168056360431L;
	private int id;
	private int parent_id;
	private String title;
	private String url ;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getParent_id() {
		return parent_id;
	}
	public void setParent_id(int parentId) {
		parent_id = parentId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	

}
