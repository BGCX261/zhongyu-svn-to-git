package com.piaoyou.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * 
 * @author hp
 *
 */
public class ContentRemark implements Serializable {
	private static final long serialVersionUID = -497878496983993570L;
	private int id;
	private int common_id;
	private int user_id;
	private String title;
	private String content;
	private String create_date;
	private int level;
	private String user_name;
	private String user_gender;
	private String user_portrait;
	private int replyCount;
	private int is_recommend;
	private String user_nick;
	private String img_url;//演出图片
	private String name;//演出名称
	private int show_id;//演出id
	private CommonStat stat;//用户信息统计
	private List<Map<String,String>> user_tags = new ArrayList<Map<String,String>>();
	private int b_num;
	private List<ContentRemark> other_recommond = new ArrayList<ContentRemark>();
	
	
	
	

	public String getUser_gender() {
		return user_gender;
	}
	public void setUser_gender(String userGender) {
		user_gender = userGender;
	}
	public List<Map<String, String>> getUser_tags() {
		return user_tags;
	}
	public void setUser_tags(List<Map<String, String>> userTags) {
		user_tags = userTags;
	}
	public CommonStat getStat() {
		return stat;
	}
	public void setStat(CommonStat stat) {
		this.stat = stat;
	}
	public List<ContentRemark> getOther_recommond() {
		return other_recommond;
	}
	public void setOther_recommond(List<ContentRemark> otherRecommond) {
		other_recommond = otherRecommond;
	}
	public int getShow_id() {
		return show_id;
	}
	public void setShow_id(int showId) {
		show_id = showId;
	}
	public int getB_num() {
		return b_num;
	}
	public void setB_num(int bNum) {
		b_num = bNum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String imgUrl) {
		img_url = imgUrl;
	}
	public String getUser_nick() {
		return user_nick;
	}
	public void setUser_nick(String userNick) {
		user_nick = userNick;
	}
	public int getIs_recommend() {
		return is_recommend;
	}
	public void setIs_recommend(int isRecommend) {
		is_recommend = isRecommend;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
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
	public int getCommon_id() {
		return common_id;
	}
	public void setCommon_id(int commonId) {
		common_id = commonId;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int userId) {
		user_id = userId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String createDate) {
		create_date = createDate;
	}
	
	public int getReplyCount() {
		return replyCount;
	}
	public void setReplyCount(int replyCount) {
		this.replyCount = replyCount;
	}
	
}
