package com.piaoyou.domain;

import java.io.Serializable;

public class FocusPhoto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7891260812823133742L;

	/**
	 * 
	 */
	
	private int id;
	
	private int category_id;
	
	private String title;
	
	private String content;
	
	private String links;
	
	private String mod_time;
	
	

	public String getLinks() {
		return links;
	}

	public void setLinks(String links) {
		this.links = links;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMod_time() {
		return mod_time;
	}

	public void setMod_time(String modTime) {
		mod_time = modTime;
	}

	public int getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int categoryId) {
		category_id = categoryId;
	}
	
	

}
