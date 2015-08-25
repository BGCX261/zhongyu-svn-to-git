package com.piaoyou.domain;

public class RemarkRecommend {
	private int id;
	private int remark_id;
	private int user_id;
	private String create_date;
	private String recommend_reason;
	private String user_portrait;
	private String user_name;
	private String user_nick;
	
	public String getUser_nick() {
		return user_nick;
	}
	public void setUser_nick(String userNick) {
		user_nick = userNick;
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
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String createDate) {
		create_date = createDate;
	}
	public String getRecommend_reason() {
		return recommend_reason;
	}
	public void setRecommend_reason(String recommendReason) {
		recommend_reason = recommendReason;
	}

}
