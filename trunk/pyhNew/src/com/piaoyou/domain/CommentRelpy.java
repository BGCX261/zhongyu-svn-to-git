package com.piaoyou.domain;

import java.io.Serializable;

public class CommentRelpy implements Serializable {
	private static final long serialVersionUID = -3008152201352332055L;
	private int id;
	private int comment_id;
	private String create_date;
	private int user_id;
	private String content;
	private String user_name;
	private String user_portrait;
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
	public int getComment_id() {
		return comment_id;
	}
	public void setComment_id(int commentId) {
		comment_id = commentId;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String createDate) {
		create_date = createDate;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int userId) {
		user_id = userId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
}
