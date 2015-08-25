package com.piaoyou.util;

import javax.servlet.http.HttpServletRequest;

public class CommonValidate {
	/**
	 * 后台的邮箱的校验
	 * @param request
	 * @param target 
	 * @return
	 */
	public static String validateEmail(HttpServletRequest request,String target) {
		String email = request.getParameter(target);
		if(email!=null&&email.trim().length()<=80&&email.trim().matches("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$")) {
			return email.trim();
		} else {
			return null;
		}
	}
	/**
	 * 后台用户名或昵称的校验
	 * @param request
	 * @param target
	 * @return
	 */
	public static String validateNickName(HttpServletRequest request,String target) {
		String nickName = request.getParameter(target);
		if(nickName!=null&&nickName.trim().matches("^[\u4e00-\u9fa5A-Za-z0-9-_]{2,12}$")) {
			return nickName.trim();
		} else {
			return null;
		}
	}
	/**
	 * 后台密码校验，返回MD5加密后的密码
	 * @param request
	 * @param target
	 * @return
	 */
	public static String validatePassword(HttpServletRequest request,String target) {
		String password = request.getParameter(target);
		if(password!=null&&password.trim().matches("^[a-zA-Z0-9_]{6,18}$")) {
			return TransferMD5.Md5To16(password.trim());
		} else {
			return null;
		}
	}
	/**
	 * 后台预处理
	 * @param request
	 * @param target
	 * @return
	 */
	public static String handle(HttpServletRequest request,String target) {
		String preBack = request.getParameter(target);
		String back = null;
		if(preBack!=null) {
			back = preBack.trim();
		}
		return back;
	}
	
	/**
	 * 手机号码的后台校验
	 * @param request
	 * @param target
	 * @return
	 */
	public static String validatePhoneNumber(HttpServletRequest request,String target) {
		String number = request.getParameter(target);
		if(number!=null&&number.trim().matches("^1[3|5|8][0-9]\\d{8}$")) {
			return number.trim();
		} else {
			return null;
		}
	}
	/**
	 * 普通空检查
	 * @param request
	 * @param target
	 * @return
	 */
	public static String validateCommon(HttpServletRequest request,String target) {
		String dest = request.getParameter(target);
		if(dest!=null&&!dest.trim().equals("")) {
			return dest.trim();
		} else {
			return null;
		}
	}
	/**
	 * 身份证校验
	 * @param request
	 * @param target
	 * @return
	 */
	public static String validateID(HttpServletRequest request,String target) {
		String number = request.getParameter(target);
		if(number!=null&&number.trim().matches("^1[3|5|8][0-9]\\d{8}$")) {
			return number.trim();
		} else {
			return null;
		}
	}
	/**
	 * 性别数据检测与默认赋值
	 * @param request
	 * @param target
	 * @return
	 */
	public static String validateGender(HttpServletRequest request,String target) {
		String gender = request.getParameter(target);
		if(gender==null) {
			return "1";
		} else if(gender.equals("0")){
			return "0";
		} else {
			return "1";
		}
	}
	/**
	 * 检查注册类型的正确性
	 * @param request
	 * @param target
	 * @return
	 */
	public static String validateRegType(HttpServletRequest request,String target) {
		String type = request.getParameter(target);
		if(type!=null&&type.trim().matches("^[123]{1}$")) {
			return type.trim();
		} else {
			return "1";
		}
	}
	/**
	 * 核查个性域名的格式正确性
	 * @param request
	 * @param target
	 * @return
	 */
	public static String validateZoneAddress(HttpServletRequest request,String target) {
		String zone = request.getParameter(target);
		if(zone!=null&&zone.trim().matches("^[a-zA-Z0-9_]{6,18}$")&&zone.trim().matches("^[A-Za-z0-9_]*[A-Za-z]+[A-Za-z_0-9]*$")) {
			return zone.trim();
		} else {
			return null;
		}
	}
	/**
	 * 用户身份后台校验是否符合逻辑
	 */
	public static String validateUserIdentity(HttpServletRequest request,String target) {
		String identity = request.getParameter(target);
		if(identity!=null&&identity.trim().matches("^\\d+$")) {
			if(identity.trim().equals("0")) {
				return null;
			} else {
				return identity.trim();
			}
		} else {
			return null;
		}
	}
	/**
	 * 校验指定长度的文本
	 * @param request
	 * @param target
	 * @param len 指定的长度
	 * @return
	 */
	public static String validateSpecialText(HttpServletRequest request,String target,int len) {
		String zone = request.getParameter(target);
		if(zone!=null&&zone.trim().length()<=len) {
			return zone.trim();
		} else {
			return null;
		}
	}
}
