package com.piaoyou.domain;

import java.io.Serializable;

public class CommenterAuthInfo implements Serializable {

	private static final long serialVersionUID = 3899823100509689287L;
	private int id;
	private int user_id;
	private String comment_inner;
	private String comment_inner_links;
	private String comment_outer;
	private String comment_outer_links;
	private String weibo_or_zone;
	private String weibo_or_zone_links;
	private String application_status;
	private String application_date;
	
	public CommenterAuthInfo(){}
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
	public String getComment_inner() {
		return comment_inner;
	}
	public void setComment_inner(String commentInner) {
		comment_inner = commentInner;
	}
	public String getComment_inner_links() {
		return comment_inner_links;
	}
	public void setComment_inner_links(String commentInnerLinks) {
		comment_inner_links = commentInnerLinks;
	}
	public String getComment_outer() {
		return comment_outer;
	}
	public void setComment_outer(String commentOuter) {
		comment_outer = commentOuter;
	}
	public String getComment_outer_links() {
		return comment_outer_links;
	}
	public void setComment_outer_links(String commentOuterLinks) {
		comment_outer_links = commentOuterLinks;
	}
	public String getWeibo_or_zone() {
		return weibo_or_zone;
	}
	public void setWeibo_or_zone(String weiboOrZone) {
		weibo_or_zone = weiboOrZone;
	}
	public String getWeibo_or_zone_links() {
		return weibo_or_zone_links;
	}
	public void setWeibo_or_zone_links(String weiboOrZoneLinks) {
		weibo_or_zone_links = weiboOrZoneLinks;
	}
	public String getApplication_status() {
		return application_status;
	}
	public void setApplication_status(String applicationStatus) {
		application_status = applicationStatus;
	}
	public String getApplication_date() {
		return application_date;
	}
	public void setApplication_date(String applicationDate) {
		application_date = applicationDate;
	}
	
	
}
