package com.piaoyou.domain;

import java.io.Serializable;

public class BlackList implements Serializable {
	private static final long serialVersionUID = -1591033687697862868L;
	private int id;
	private int user_id;
	private int black_user_id;
	private String black_user_name;
	private String add_time;
	
	public BlackList(){}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int userId) {
		user_id = userId;
	}

	public int getBlack_user_id() {
		return black_user_id;
	}

	public void setBlack_user_id(int blackUserId) {
		black_user_id = blackUserId;
	}

	public String getBlack_user_name() {
		return black_user_name;
	}

	public void setBlack_user_name(String blackUserName) {
		black_user_name = blackUserName;
	}

	public String getAdd_time() {
		return add_time;
	}

	public void setAdd_time(String addTime) {
		add_time = addTime;
	}
	
	
}
