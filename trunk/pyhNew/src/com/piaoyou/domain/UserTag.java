package com.piaoyou.domain;

import java.io.Serializable;

public class UserTag implements Serializable {
	private static final long serialVersionUID = -1578143651681882244L;
	private int tag_id;
	private String tag_content;
	private int user_id;
	public int getTag_id() {
		return tag_id;
	}
	public void setTag_id(int tagId) {
		tag_id = tagId;
	}
	public String getTag_content() {
		return tag_content;
	}
	public void setTag_content(String tagContent) {
		tag_content = tagContent;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int userId) {
		user_id = userId;
	}
	

}
