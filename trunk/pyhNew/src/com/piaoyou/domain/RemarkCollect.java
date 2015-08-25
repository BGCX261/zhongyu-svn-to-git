package com.piaoyou.domain;

import java.io.Serializable;

public class RemarkCollect implements Serializable {
	private static final long serialVersionUID = -5774996199929020551L;
	
	private int id;
	private int remark_id;
	private int user_id;
	private String title;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getRemark_id() {
		return remark_id;
	}
	public void setRemark_id(int remarkId) {
		remark_id = remarkId;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int userId) {
		user_id = userId;
	}
}
