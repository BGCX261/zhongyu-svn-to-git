package com.piaoyou.domain;

import java.io.Serializable;

public class CommonStat implements Serializable {
	private static final long serialVersionUID = 1989883767831170982L;
	private int id;
	private int user_id;
	private int recommend_count;
	private int reply_count;
	private int user_fensi_count;
	private int user_sixing_count;
	private int user_attention_count;
	private int user_collect_count;
	public int getUser_collect_count() {
		return user_collect_count;
	}
	public void setUser_collect_count(int userCollectCount) {
		user_collect_count = userCollectCount;
	}
	public int getId() {
		return id;
	}
	public int getUser_attention_count() {
		return user_attention_count;
	}
	public void setUser_attention_count(int userAttentionCount) {
		user_attention_count = userAttentionCount;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int userId) {
		user_id = userId;
	}
	public int getUser_fensi_count() {
		return user_fensi_count;
	}
	public void setUser_fensi_count(int userFensiCount) {
		user_fensi_count = userFensiCount;
	}
	public int getUser_sixing_count() {
		return user_sixing_count;
	}
	public void setUser_sixing_count(int userSixingCount) {
		user_sixing_count = userSixingCount;
	}

}
