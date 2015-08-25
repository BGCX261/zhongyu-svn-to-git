package com.piaoyou.domain;

import java.io.Serializable;

public class CommonComment implements Serializable {
	private static final long serialVersionUID = 5360913469566577439L;
	private int id;
	private int common_id;
	private int comment_user_id;
	private String comment_info;
	private String comment_date;
	private int reply_user_id;
	public int getReply_user_id() {
		return reply_user_id;
	}
	public void setReply_user_id(int replyUserId) {
		reply_user_id = replyUserId;
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
	private String user_name;
	private String user_portrait;
	private int reply_count;
	
	public int getReply_count() {
		return reply_count;
	}
	public void setReply_count(int replyCount) {
		reply_count = replyCount;
	}
	public int getComment_user_id() {
		return comment_user_id;
	}
	public void setComment_user_id(int commentUserId) {
		comment_user_id = commentUserId;
	}
	public String getComment_info() {
		return comment_info;
	}
	public void setComment_info(String commentInfo) {
		comment_info = commentInfo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCommon_id() {
		return common_id;
	}
	public void setCommon_id(int commonId) {
		common_id = commonId;
	}
	public String getComment_date() {
		return comment_date;
	}
	public void setComment_date(String commentDate) {
		comment_date = commentDate;
	}
}
