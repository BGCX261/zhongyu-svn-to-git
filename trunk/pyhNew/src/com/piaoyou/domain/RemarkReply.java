package com.piaoyou.domain;

import java.io.Serializable;

public class RemarkReply implements Serializable {
	private static final long serialVersionUID = 8932281345376985883L;
	private int id;
	private int remark_id;
	private String content;
	private int user_id;
	private String create_date;
	private String user_name;
	private String user_portrait;
	private int reply_user_id;
	private int reply_count;
	private String user_nick;
	
	
	

	public String getUser_nick() {
		return user_nick;
	}
	public void setUser_nick(String userNick) {
		user_nick = userNick;
	}
	public int getReply_user_id() {
		return reply_user_id;
	}
	public void setReply_user_id(int replyUserId) {
		reply_user_id = replyUserId;
	}
	public int getReply_count() {
		return reply_count;
	}
	public void setReply_count(int replyCount) {
		reply_count = replyCount;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
}
