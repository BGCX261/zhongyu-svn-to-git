package com.piaoyou.domain;

import java.io.Serializable;

public class UserPrivateLetter implements Serializable {
	private static final long serialVersionUID = -4352662514108382990L;
	private int id;
	private int from_user_id;
	private int to_user_id;
	private String send_date;
	private String send_info;
	private String  send_title;
	private int from_delete;
	private String user_nick;
	private String user_portrait;
	private String user_address;
	private String group_user;
	private String show_date;
	private String count;
	private int is_read;
	
	
	
	
	
	
	
	public int getIs_read() {
		return is_read;
	}
	public void setIs_read(int isRead) {
		is_read = isRead;
	}
	public String getShow_date() {
		return show_date;
	}
	public void setShow_date(String showDate) {
		show_date = showDate;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getGroup_user() {
		return group_user;
	}
	public void setGroup_user(String groupUser) {
		group_user = groupUser;
	}
	public void setUser_address(String userAddress) {
		user_address = userAddress;
	}
	public String getUser_address() {
		return user_address;
	}
	public void setUser_adress(String userAddress) {
		user_address = userAddress;
	}
	public String getUser_nick() {
		return user_nick;
	}
	public void setUser_nick(String userNick) {
		user_nick = userNick;
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
	public int getFrom_user_id() {
		return from_user_id;
	}
	public void setFrom_user_id(int fromUserId) {
		from_user_id = fromUserId;
	}
	public int getTo_user_id() {
		return to_user_id;
	}
	public void setTo_user_id(int toUserId) {
		to_user_id = toUserId;
	}
	public String getSend_date() {
		return send_date;
	}
	public void setSend_date(String sendDate) {
		send_date = sendDate;
	}
	public String getSend_info() {
		return send_info;
	}
	public void setSend_info(String sendInfo) {
		send_info = sendInfo;
	}
	public int getFrom_delete() {
		return from_delete;
	}
	public void setFrom_delete(int fromDelete) {
		from_delete = fromDelete;
	}
	public int getTo_delete() {
		return to_delete;
	}
	public String getSend_title() {
		return send_title;
	}
	public void setSend_title(String sendTitle) {
		send_title = sendTitle;
	}
	public void setTo_delete(int toDelete) {
		to_delete = toDelete;
	}
	private int to_delete;
}
