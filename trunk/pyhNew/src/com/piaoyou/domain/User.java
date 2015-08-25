package com.piaoyou.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.piaoyou.util.Const;
import com.piaoyou.util.ImgPathUtil;

public class User implements Serializable {
	private static final long serialVersionUID = -4650413747213990253L;
	private int user_id;
	private String user_email;
	private String user_password;
	private String user_gender;
	private String user_address;
	private String user_nick;
	private String last_login_time;
	private String register_time;
	private String user_status;
	private String user_name;
	private String user_name_status;
	private String user_birthday;
	private String user_birthday_status;
	private String user_portrait;
	private String safe_email;
	private String commenter;
	@SuppressWarnings("unused")
	private String user_hidden_email;
	@SuppressWarnings("unused")
	private String safe_hidden_email;
	@SuppressWarnings("unused")
	private String wholeImgPath;
	
	private CommonStat stat;//用户信息统计
	
	private List<Map<String,String>> user_tags = new ArrayList<Map<String,String>>();
	
	
	
	
	
	
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

	public User() {}
	
	public User(String userEmail, String userPassword) {
		super();
		user_email = userEmail;
		user_password = userPassword;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int userId) {
		user_id = userId;
	}

	public String getUser_email() {
		return user_email;
	}

	public void setUser_email(String userEmail) {
		user_email = userEmail;
	}

	public String getUser_password() {
		return user_password;
	}

	public void setUser_password(String userPassword) {
		user_password = userPassword;
	}

	public String getUser_gender() {
		return user_gender;
	}

	public void setUser_gender(String userGender) {
		user_gender = userGender;
	}

	public String getUser_address() {
		return user_address;
	}

	public void setUser_address(String userAddress) {
		user_address = userAddress;
	}

	public String getUser_nick() {
		return user_nick;
	}

	public void setUser_nick(String userNick) {
		user_nick = userNick;
	}

	public String getLast_login_time() {
		return last_login_time;
	}

	public void setLast_login_time(String lastLoginTime) {
		last_login_time = lastLoginTime;
	}

	public String getRegister_time() {
		return register_time;
	}

	public void setRegister_time(String registerTime) {
		register_time = registerTime;
	}

	public String getUser_status() {
		return user_status;
	}

	public void setUser_status(String userStatus) {
		user_status = userStatus;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String userName) {
		user_name = userName;
	}

	public String getUser_name_status() {
		return user_name_status;
	}

	public void setUser_name_status(String userNameStatus) {
		user_name_status = userNameStatus;
	}

	public String getUser_birthday() {
		return user_birthday;
	}

	public void setUser_birthday(String userBirthday) {
		user_birthday = userBirthday;
	}

	public String getUser_birthday_status() {
		return user_birthday_status;
	}

	public void setUser_birthday_status(String userBirthdayStatus) {
		user_birthday_status = userBirthdayStatus;
	}

	public String getUser_portrait() {
		return user_portrait;
	}

	public void setUser_portrait(String userPortrait) {
		user_portrait = userPortrait;
	}

	public String getSafe_email() {
		return safe_email;
	}

	public void setSafe_email(String safeEmail) {
		safe_email = safeEmail;
	}

	public String getCommenter() {
		return commenter;
	}

	public void setCommenter(String commenter) {
		this.commenter = commenter;
	}
	
	
	public String getUser_hidden_email() {
		return this.hiddenEmailChar(this.user_email);
	}

	public void setUser_hidden_email(String userHiddenEmail) {
		user_hidden_email = userHiddenEmail;
	}
	
	
	public String getSafe_hidden_email() {
		return this.hiddenEmailChar(this.safe_email);
	}

	public void setSafe_hidden_email(String safeHiddenEmail) {
		safe_hidden_email = safeHiddenEmail;
	}

	private String hiddenEmailChar(String email) {
		if(email==null||email.trim().equals("")) {
			return null;
		} else {
			String newEmail = email.replace("#", "");
			Pattern p = Pattern.compile("([\\w\\-\\.]+)(@\\w+\\..+)");
			Matcher m = p.matcher(newEmail);
			m.matches();
			String first = m.group(1).substring(0, 1);
			String end = m.group(1).substring(m.group(1).length()-1, m.group(1).length());
			return (first+"****"+end+m.group(2));
		}
		
	}

	public String getWholeImgPath() {
		if(this.user_portrait==null) {
			return null;
		} else {
			return Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, this.user_id)+"30x30_"+this.user_portrait;
		}
	}

	public void setWholeImgPath(String wholeImgPath) {
		this.wholeImgPath = wholeImgPath;
	}
	
}
