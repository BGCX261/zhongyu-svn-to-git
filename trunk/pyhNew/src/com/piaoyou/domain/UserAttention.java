package com.piaoyou.domain;

import java.io.Serializable;

public class UserAttention implements Serializable {
	private static final long serialVersionUID = 4430599018540530473L;
	
	private int id;
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
	public int getAttention_id() {
		return attention_id;
	}
	public void setAttention_id(int attentionId) {
		attention_id = attentionId;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String createDate) {
		create_date = createDate;
	}
	public int getIs_read() {
		return is_read;
	}
	public void setIs_read(int isRead) {
		is_read = isRead;
	}
	private int user_id;
	private int attention_id;
	private String create_date;
	private int is_read;
	
	
	
	/**
	 * 共同关注的人的信息
	 */
	private  String sameAttentionInfo;
	/**
	 * 我关注的人中,关注了她
	 */
	private  String alsoAttentionInfo;
	public String getSameAttentionInfo() {
		return sameAttentionInfo;
	}
	public void setSameAttentionInfo(String sameAttentionInfo) {
		this.sameAttentionInfo = sameAttentionInfo;
	}
	public String getAlsoAttentionInfo() {
		return alsoAttentionInfo;
	}
	public void setAlsoAttentionInfo(String alsoAttentionInfo) {
		this.alsoAttentionInfo = alsoAttentionInfo;
	}
	
	private String user_name;
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
	private String user_portrait;
	private int recommend_count;
	private int reply_count;
	private int user_fensi_count;
	
	public String getUser_address() {
		return user_address;
	}
	public String getUser_tag() {
		return user_tag;
	}
	public void setUser_tag(String userTag) {
		user_tag = userTag;
	}
	public void setUser_address(String userAddress) {
		user_address = userAddress;
	}
	private String user_address;
	
	
	private String user_tag;
	
	
}
