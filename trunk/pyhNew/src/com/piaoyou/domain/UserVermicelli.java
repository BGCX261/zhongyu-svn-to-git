package com.piaoyou.domain;

import java.io.Serializable;

public class UserVermicelli implements Serializable {
	private static final long serialVersionUID = -4016059847437057284L;
	private int id;
	private int vermicelli_id;
	private int user_id;
	private String create_date;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getVermicelli_id() {
		return vermicelli_id;
	}
	public void setVermicelli_id(int vermicelliId) {
		vermicelli_id = vermicelliId;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int userId) {
		user_id = userId;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String createDate) {
		create_date = createDate;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String userName) {
		user_name = userName;
	}
	public String getUser_portrait() {
		return user_portrait;
	}
	public void setUser_portrait(String userPortrait) {
		user_portrait = userPortrait;
	}
	public int getRecommend_count() {
		return recommend_count;
	}
	public void setRecommend_count(int recommendCount) {
		recommend_count = recommendCount;
	}
	public int getReply_count() {
		return reply_count;
	}
	public void setReply_count(int replyCount) {
		reply_count = replyCount;
	}
	public int getUser_fensi_count() {
		return user_fensi_count;
	}
	public void setUser_fensi_count(int userFensiCount) {
		user_fensi_count = userFensiCount;
	}
	private String user_name;
	private String user_portrait;
	private int recommend_count;
	private int reply_count;
	private int user_fensi_count;
}
