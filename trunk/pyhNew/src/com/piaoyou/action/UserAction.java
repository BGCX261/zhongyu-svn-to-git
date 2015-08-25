package com.piaoyou.action;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.piaoyou.domain.BlackList;
import com.piaoyou.domain.CommenterAuthInfo;
import com.piaoyou.domain.CommonStat;
import com.piaoyou.domain.User;
import com.piaoyou.domain.UserAttention;
import com.piaoyou.img.Img;
import com.piaoyou.service.CommonService;
import com.piaoyou.util.CommonMailService;
import com.piaoyou.util.CommonValidate;
import com.piaoyou.util.Const;
import com.piaoyou.util.FileUtil;
import com.piaoyou.util.ImageUtil;
import com.piaoyou.util.ImgPathUtil;
import com.piaoyou.util.TransferDES;
@SuppressWarnings("static-access")
@Controller
@RequestMapping(value = "/user")
public class UserAction {
	private  static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private static final Log log = LogFactory.getLog(UserAction.class);
	private static CommonValidate CV;
	@Autowired
	private CommonService commonService;
	@RequestMapping(value = "/test.do", method = RequestMethod.GET)
	public String test(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		log.info("-----------------------------test successfully!");
		return "user/test";
	}
	
	@RequestMapping(value = "/inviteFriends.do", method = RequestMethod.GET)
	public String inviteFriends(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		User user = (User)hs.getAttribute("user");
		if(user!=null) {
			String link = "http://"+request.getServerName()+request.getContextPath()+"/user/doInviteFriends.do?inviterId="+TransferDES.encrypt(user.getUser_id()+"");
			request.setAttribute("link", link);
			return "user/inviteFriends";
		} else {
			return "redirect:/";
		}
	
	}
	
	@RequestMapping(value = "/showActivitSuccess.do", method = RequestMethod.GET)
	public String showActivitSuccess(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		User user = (User)hs.getAttribute("user");
		if(user!=null) {
			return "user/showActivitSuccess";
		} else {
			return "redirect:/";
		}
	
	}
	
	@RequestMapping(value = "/findPasswordStep1.do", method = RequestMethod.GET)
	public String findPasswordStep1(HttpSession hs,HttpServletRequest request){
		User user = (User)hs.getAttribute("user");
		if(user==null) {
			return "/user/findPasswordStep1";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/findPasswordStep2.do", method = RequestMethod.GET)
	public String findPasswordStep2(HttpSession hs,HttpServletRequest request){
		User user = (User)hs.getAttribute("user");
		User user_1 = (User)hs.getAttribute("hiddenUserInfo_1");
		if(user==null&&user_1!=null) {
			String user_email =  user_1.getUser_email();
			request.setAttribute("user_email",user_email);
			request.setAttribute("mail","http://mail."+user_email.substring(user_email.indexOf("@")+1,user_email.length()));
			return "/user/findPasswordStep2";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/findPasswordStep3.do", method = RequestMethod.GET)
	public String findPasswordStep3(HttpSession hs,HttpServletRequest request){
		User user = (User)hs.getAttribute("user");
		User user_1 = (User)hs.getAttribute("resetPasswordUserInfo");
		if(user==null&&user_1!=null) {
			return "/user/findPasswordStep3";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/findPasswordStep4.do", method = RequestMethod.GET)
	public String findPasswordStep4(HttpSession hs,HttpServletRequest request){
		User user = (User)hs.getAttribute("user");
		User user_1 = (User)hs.getAttribute("resetPasswordUserInfo");
		if(user==null&&user_1!=null) {
			return "/user/findPasswordStep4";
		} else {
			return "redirect:/";
		}
	}
	
	
	
	//userApplyCommenterSecond
	@RequestMapping(value = "/userApplyCommenterThird.do", method = RequestMethod.GET)
	public String userApplyCommenterThird(HttpSession hs,HttpServletRequest request){
		User user = (User)hs.getAttribute("user");
		if(user!=null) {
			return "/user/userApplyCommenterThird";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/userApplyCommenterSecond.do", method = RequestMethod.GET)
	public String userApplyCommenterSecond(HttpSession hs,HttpServletRequest request){
		User user = (User)hs.getAttribute("user");
		if(user!=null) {
			return "/user/userApplyCommenterSecond";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/userModifyPassword.do", method = RequestMethod.GET)
	public String userModifyPassword(HttpSession hs,HttpServletRequest request){
		User user = (User)hs.getAttribute("user");
		if(user!=null) {
			return "/user/userModifyPassword";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/userBlackList.do", method = RequestMethod.GET)
	public String userBlackList(HttpSession hs,HttpServletRequest request){
		User user = (User)hs.getAttribute("user");
		if(user!=null) {
			Map<Object,Object> map = new HashMap<Object,Object>();
			map.put("user_id", user.getUser_id()+"");
			List<BlackList> list = commonService.selectObjects("user.getBlackList", map);
			request.setAttribute("bl", list);
			return "/user/userBlackList";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/userApplyCommenter.do", method = RequestMethod.GET)
	public String userApplyCommenter(HttpSession hs,HttpServletRequest request){
		String f1 = "y";
		String f2 = "y";
		String f3 = "n1";
		User user = (User)hs.getAttribute("user");
		if(user!=null) {
				if(user.getUser_address()==null||user.getUser_address().equals("")) {
					f1 = "n";
				} 
				if(user.getUser_birthday()==null||user.getUser_birthday().equals("")) {
					f1 = "n";
				}
				if(user.getUser_gender()==null||user.getUser_gender().equals("")) {
					f1 = "n";
				}
				if(user.getUser_name()==null||user.getUser_name().equals("")) {
					f1 = "n";
				}
				if(user.getUser_portrait()==null||user.getUser_portrait().equals("")) {
					f2 = "n";
				}
				if(f1.equals("y")&&f2.equals("y")) {
					f3 = "y";
				} else if(f1.equals("y")&&f2.equals("n")) {
					f3 = "n2";
				} else {
					f3 = "n1";
				}
				request.setAttribute("f1", f1);
				request.setAttribute("f2", f2);
				request.setAttribute("f3", f3);
				return "/user/userApplyCommenter";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/userSafeEmail.do", method = RequestMethod.GET)
	public String userSafeEmail(HttpSession hs,HttpServletRequest request){
		User user = (User)hs.getAttribute("user");
		String status = "0";
		if(user!=null) {
			String flag = (String) hs.getAttribute("flag");
			if(flag==null) {
				if(user.getSafe_email()!=null&&user.getSafe_email().startsWith("#")) {
					status = "1";
					String user_email = user.getSafe_email().replace("#", "");
					request.setAttribute("email","http://mail."+user_email.substring(user_email.indexOf("@")+1,user_email.length()));
					Pattern p = Pattern.compile("([\\w\\-\\.]+)@(\\w+)\\.(.+)");
					Matcher m = p.matcher(user_email);
					m.matches();
					request.setAttribute("user_email",user_email);
				} else if(user.getSafe_email()!=null&&user.getSafe_email().matches("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$")) {
					status = "2";
				}
			} else if(flag.equals("first")){
				status = "3";
				hs.setAttribute("flag", null);
			}
			request.setAttribute("status", status);
			return "/user/userSafeEmail";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/userAuthInfo.do", method = RequestMethod.GET)
	public String userAuthInfo(HttpSession hs,HttpServletRequest request){
		User user = (User)hs.getAttribute("user");
		if(user!=null) {
			Map<Object,Object> map = new HashMap<Object,Object>();
			map.put("user_id", user.getUser_id()+"");
			CommenterAuthInfo cai = commonService.selectObject("user.getAppAuthInfo", map);
			if(cai==null) {
				return "/user/userAuthInfo";
			} else if(cai.getApplication_status().equals("0")){
				return "/user/userApplyCommenterThird";
			} else if(cai.getApplication_status().equals("1")) {
				return "";
			} else {
				return "";
			}
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/userHeadPortrait.do", method = RequestMethod.GET)
	public String userHeadPortrait(HttpSession hs,HttpServletRequest request){
		User user = (User)hs.getAttribute("user");
		if(user!=null) {
			request.setAttribute("head_path",  Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, user.getUser_id()));
			return "/user/userHeadPortrait";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/userPersonalTags.do", method = RequestMethod.GET)
	public String userPersonalTags(HttpSession hs,HttpServletRequest request){
		User user = (User)hs.getAttribute("user");
		if(user!=null) {
			Map<Object,Object> map = new HashMap<Object,Object>();
			map.put("user_id", user.getUser_id()+"");
			List<Map<String,String>> list = commonService.selectObjects("user.getMyTags",map);
			request.setAttribute("myTags", list);
			List<Map<String,String>> list_1 = commonService.selectObjects("user.getUserRecommondTags",null);
			request.setAttribute("reTags", list_1);
			return "/user/userPersonalTags";
		} else {
			return "redirect:/";
		}
	}
	@RequestMapping(value = "/userBasicInfo.do", method = RequestMethod.GET)
	public String userBasicInfo(HttpSession hs,HttpServletRequest request){
		User user = (User)hs.getAttribute("user");
		if(user!=null) {
			return "/user/userBasicInfo";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/showUserRegister.do", method = RequestMethod.GET)
	public String showUserRegister(HttpSession hs,HttpServletRequest request){
		User user = (User)hs.getAttribute("user");
		if(user==null) {
			return "/user/showUserRegister";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/showUserLogin.do", method = RequestMethod.GET)
	public String showUserLogin(HttpSession hs,HttpServletRequest request){
		User user = (User)hs.getAttribute("user");
		if(user==null) {
			String referer = request.getHeader("referer");
			request.setAttribute("referer", referer);
			return "/user/showUserLogin";
		} else {
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/showRegisterSuccess.do", method = RequestMethod.GET)
	public String showRegisterSuccess(HttpSession hs,HttpServletRequest request){
		//做判断跳转
		User user = (User) hs.getAttribute("hiddenUserInfo");
		if(user!=null) {
			String user_email = user.getUser_email();
			request.setAttribute("email","http://mail."+user_email.substring(user_email.indexOf("@")+1,user_email.length()));
			Pattern p = Pattern.compile("([\\w\\-\\.]+)@(\\w+)\\.(.+)");
			Matcher m = p.matcher(user_email);
			m.matches();
			request.setAttribute("user_email",user.getUser_email());
			request.setAttribute("domain", m.group(2));
			return "/user/showRegisterSuccess";
		} else {
			return "/user/showUserLogin";
		}
	}
	
	@RequestMapping(value = "/showFindPasswordOver.do", method = RequestMethod.GET)
	public String showFindPasswordOver(HttpServletRequest request){
		String user_email = TransferDES.decrypt(request.getParameter("user_email"));
		request.setAttribute("user_email", user_email);
		request.setAttribute("mail","http://mail."+user_email.substring(user_email.indexOf("@")+1,user_email.length()));
		return "/user/showFindPasswordOver";
	}
	
	@RequestMapping(value = "/getValidateCode.do", method = RequestMethod.GET)
	public String getValidateCode(){
		return "/user/image";
	}
	
	@RequestMapping(value = "/getValidateCode2.do", method = RequestMethod.GET)
	public String getValidateCode2(){
		return "/user/image2";
	}
	
	@RequestMapping(value = "/doUserRegister.do", method = RequestMethod.POST)
	public String doUserRegister(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		User user = (User)hs.getAttribute("user");
		if(user==null) {
			String user_email = CV.validateEmail(request, "user_email");
			String user_password = CV.validatePassword(request, "user_password");
			String user_nick = CV.validateNickName(request, "user_nick");
			String inviterId = (String) hs.getAttribute("inviterId");
			
			Map<Object,Object> map = new HashMap<Object,Object>();
			map.put("user_email", user_email);
			map.put("user_password", user_password);
			map.put("user_nick", user_nick);
			map.put("user_status", "0");
			
			int primaryKey = commonService.insertObject("user.addOneRegisterUser", map);
			User hiddenUserInfo = new User(user_email,user_password);
			hiddenUserInfo.setUser_status("0");
			hiddenUserInfo.setUser_id(primaryKey);
			new CommonMailService().sendLinkForActivateNewUser(hiddenUserInfo,inviterId);
			hs.setAttribute("hiddenUserInfo", hiddenUserInfo);
			
			/*String inviterId = (String) hs.getAttribute("inviterId");
			if(inviterId!=null) {
				followEachOther(primaryKey+"",inviterId);
				followEachOther(inviterId,primaryKey+"");
			}*/
			log.info("-----------------------------sign up successfully!");
			return "redirect:/user/showRegisterSuccess.do";
		} else {
			log.info("-----------------------------the user has already sign in!");
			return "redirect:/";
		}
	}
	
	@RequestMapping(value = "/doUserLogin.do", method = RequestMethod.POST)
	public String doUserLogin(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String back = "/user/showUserLogin";
		String user_email = CV.validateEmail(request, "user_email");
		String user_password = CV.validatePassword(request, "user_password");
		String referer = request.getParameter("referer");
		if(referer==null || "".equals(referer)) {
			referer = "redirect:/";
		} else {
			referer = "redirect:"+referer;
		}
		//后台再次验证输入信息格式
		if(user_email!=null&&user_password!=null) {
			Map<Object,Object> map = new HashMap<Object,Object>();
			map.put("user_email", user_email);
			map.put("user_password", user_password);
			User user = commonService.selectObject("user.getUserInfo", map);
			
			//将登陆用户名存入cookie
			Cookie cookie_ = new Cookie("pyh_loginName",user_email);
			cookie_.setMaxAge(365*24*3600);// 有效时间为一年，单位是秒
			cookie_.setPath("/");// 路径，一定要设置，不然可能产生多个COOKIE文件
			response.addCookie(cookie_);
			
			if(user!=null) {
				//未通过邮箱验证的用户
				if(user.getUser_status().equals("0")) {
					request.setAttribute("error", "registerError");
				} else {
					commonService.updateObject("user.updateLoginTime",user);
					String inviterId = (String) hs.getAttribute("inviterId");
					if(inviterId!=null) {
						followEachOther(user.getUser_id()+"",inviterId);
						followEachOther(inviterId,user.getUser_id()+"");
					}
					hs.setAttribute("user",user);
					back = referer;
					//cookie处理
					if(request.getParameter("remember")!=null) {
						Cookie cookie = new Cookie("pyh_login",TransferDES.encrypt(user_email)+"|"+user.getUser_password());
						cookie.setMaxAge(7*24*3600);// 有效时间为一周，单位是秒
						cookie.setPath("/");// 路径，一定要设置，不然可能产生多个COOKIE文件
						response.addCookie(cookie);
					}
				}
			} else {
				request.setAttribute("error", "loginError");
			}
		} else {
			request.setAttribute("error", "loginError");
		}
		return back;
	}
	
	@RequestMapping(value = "/doUserAjaxLogin.do", method = RequestMethod.POST)
	public void doUserAjaxLogin(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String back = "error";
		String user_email = CV.validateEmail(request, "user_email");
		String user_password = CV.validatePassword(request, "user_password");
		
		//后台再次验证输入信息格式
		if(user_email!=null&&user_password!=null) {
			Map<Object,Object> map = new HashMap<Object,Object>();
			map.put("user_email", user_email);
			map.put("user_password", user_password);
			User user = commonService.selectObject("user.getUserInfo", map);
			
			//将登陆用户名存入cookie
			Cookie cookie_ = new Cookie("pyh_loginName",user_email);
			cookie_.setMaxAge(365*24*3600);// 有效时间为一年，单位是秒
			cookie_.setPath("/");// 路径，一定要设置，不然可能产生多个COOKIE文件
			response.addCookie(cookie_);
			
			if(user!=null) {
				//未通过邮箱验证的用户
				if(user.getUser_status().equals("0")) {
					back = "noActivation";
				} else {
					commonService.updateObject("user.updateLoginTime",user);
					String inviterId = (String) hs.getAttribute("inviterId");
					if(inviterId!=null) {
						followEachOther(user.getUser_id()+"",inviterId);
						followEachOther(inviterId,user.getUser_id()+"");
					}
					hs.setAttribute("user",user);
					back = "success";
					//cookie处理
					if(request.getParameter("remember")!=null) {
						Cookie cookie = new Cookie("pyh_login",TransferDES.encrypt(user_email)+"|"+user.getUser_password());
						cookie.setMaxAge(7*24*3600);// 有效时间为一周，单位是秒
						cookie.setPath("/");// 路径，一定要设置，不然可能产生多个COOKIE文件
						response.addCookie(cookie);
					}
				}
			} 
		} 
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(back);
			out.flush();
			out.close();
		} catch (IOException e) {
			log.error(e);
		}
	}
	
	@RequestMapping(value = "/doUserLogin.do", method = RequestMethod.GET)
	public String doUserLoginGet(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		return "redirect:/user/showUserLogin.do";
	}
	
	@RequestMapping(value = "/doUserExit.do", method = RequestMethod.GET)
	public String exitThisWebSite(HttpSession hs,HttpServletRequest request,HttpServletResponse response) {
		User user = (User)hs.getAttribute("user");
			Cookie cookie = new Cookie("pyh_login", null);
			cookie.setMaxAge(0);
			cookie.setPath("/");
			response.addCookie(cookie);
			if(user!=null) {
				hs.invalidate();
				//return "redirect:/";
			} 
			//else {
				//return "redirect:/user/showUserLogin.do";
			//	return "/";
			//}
			return "redirect:/";
	}
			
	@RequestMapping(value = "/activateNewUser.do", method = RequestMethod.GET)
	public String  activateNewUser(HttpSession hs,@RequestParam("ue") String ue,@RequestParam("up") String user_password,HttpServletRequest request,HttpServletResponse response) {
		User user = null;
		hs.setAttribute("user", user);
		String back = "redirect:/";
		if(user==null) {
			Map<Object,Object> map = new HashMap<Object,Object>();
			String user_email = TransferDES.decrypt(ue);
			map.put("user_email", user_email);
			map.put("user_password", user_password);
			map.put("user_status", "1");
			user = commonService.selectObject("user.getUserInfo", map);
			if(user!=null&&user.getUser_status().equals("0")) {
				map.put("user_id", user.getUser_id()+"");
				commonService.updateObject("user.updateUserStatus", map);
				commonService.insertObject("common.insert_commonStat", map);
				hs.setAttribute("user", user);
				String inviterId = request.getParameter("inviterId");
				if(inviterId!=null) {
					inviterId = TransferDES.decrypt(inviterId);
					if(inviterId!=null&&inviterId.matches("\\d+")) {
						followEachOther(user.getUser_id()+"",inviterId);
						followEachOther(inviterId,user.getUser_id()+"");
					}
				}
				back = "redirect:/user/showActivitSuccess.do";
			} else {
				log.info("this user cannot be activated right now!");
			}
		} 
		return back;
	}
	
	
	@RequestMapping(value = "/reSendEmail.do", method = RequestMethod.POST)
	public void reSendEmail(HttpSession hs,HttpServletRequest request,HttpServletResponse response) {
		String flag = "n"; 
		//check the correctness of data by server;
		User user = (User) hs.getAttribute("hiddenUserInfo");
		String inviterId = (String) hs.getAttribute("inviterId");
		if(user!=null&&user.getUser_status().equals("0")) {
			new CommonMailService().sendLinkForActivateNewUser(user,inviterId);
			log.info(user.getUser_email());
			flag = "y";
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(flag);
			out.flush();
			out.close();
		} catch (IOException e) {
			log.error(e);
		}
	}
	
	@RequestMapping(value = "/reSendFPEmail.do", method = RequestMethod.POST)
	public void reSendFPEmail(HttpSession hs,HttpServletRequest request,HttpServletResponse response) {
		String flag = "n"; 
		//check the correctness of data by server;
		User user = (User) hs.getAttribute("hiddenUserInfo_1");
		if(user!=null&&!user.getUser_status().equals("0")) {
			new CommonMailService().sendLinkForFindPassword(user);
			log.info(user.getUser_email());
			flag = "y";
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(flag);
			out.flush();
			out.close();
		} catch (IOException e) {
			log.error(e);
		}
	}
	
	@RequestMapping(value = "/reSendSafeEmail.do", method = RequestMethod.POST)
	public void reSendSafeEmail(HttpSession hs,HttpServletRequest request,HttpServletResponse response) {
		String flag = "n"; 
		//check the correctness of data by server;
		User user = (User) hs.getAttribute("user");
		if(user!=null&&user.getSafe_email().startsWith("#")) {
			new CommonMailService().validateUserSafeEmail(user);
			log.info(user.getUser_email());
			flag = "y";
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(flag);
			out.flush();
			out.close();
		} catch (IOException e) {
			log.error(e);
		}
	}
	
	@RequestMapping(value = "/checkValidationCode.do", method = RequestMethod.POST)
	public void checkValidationCode(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String flag = "n";
		String genCode = (String) hs.getAttribute("validationCode");
		String incode = request.getParameter("code");
		if(incode!=null&&incode.trim().equals(genCode)) {
			flag = "y";
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(flag);
			out.flush();
			out.close();
		} catch (IOException e) {
			log.error("the action of checkUserEmailUnique.do have a trouble!");
		}
	}
	//checkUserEmailExistence
	@RequestMapping(value = "/checkUserEmailExistence.do", method = RequestMethod.POST)
	public void checkUserEmailExistence(HttpServletRequest request,HttpServletResponse response){
		Map<Object,Object> map = new HashMap<Object,Object>();
		map.put("user_email",request.getParameter("user_email"));
		//if this email is unique.This flag is y,or is n.
		String flag = (commonService.countParams("user.checkUserEmailExistence", map))>0?"y":"n";
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(flag);
			out.flush();
			out.close();
		} catch (IOException e) {
			log.error("the action of checkUserEmailUnique.do have a trouble!");
		}
	}
	
	@RequestMapping(value = "/checkUserEmailUnique.do", method = RequestMethod.POST)
	public void checkUserEmailUnique(HttpServletRequest request,HttpServletResponse response){
		Map<Object,Object> map = new HashMap<Object,Object>();
		map.put("user_email",request.getParameter("user_email"));
		//if this email is unique.This flag is y,or is n.
		String flag = (commonService.countParams("user.checkUserEmailUnique", map))>0?"n":"y";
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(flag);
			out.flush();
			out.close();
		} catch (IOException e) {
			log.error("the action of checkUserEmailUnique.do have a trouble!");
		}
	}
	
	
	@RequestMapping(value = "/checkUserNickUnique.do", method = RequestMethod.POST)
	public void checkUserNameUnique(HttpServletRequest request,HttpServletResponse response){
		Map<Object,Object> map = new HashMap<Object,Object>();
		map.put("user_nick",request.getParameter("user_nick"));
		
		//if this user's nick is unique.This flag is y,or is n.
		String flag = commonService.countParams("user.checkUserNickUnique", map)>0?"n":"y";
		
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(flag);
			out.flush();
			out.close();
		} catch (IOException e) {
			log.error("the action of checkUserNickUnique.do have a trouble!");
		}
	}
	
	@RequestMapping(value = "/checkAnotherUserNickUnique.do", method = RequestMethod.POST)
	public void checkAnotherUserNameUnique(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		User user = (User) hs.getAttribute("user");
		String flag = "y"; 
		int counter = 1;
		if(user!=null) {
			Map<Object,Object> map = new HashMap<Object,Object>();
			map.put("user_nick",request.getParameter("user_nick"));
			map.put("user_id", user.getUser_id()+"");
			counter = commonService.countParams("user.checkUserNickUnique", map);
			if(counter!=0) {
				int me = commonService.countParams("user.checkAnotherUserNickUnique", map);
				if(me==1) {
					flag = "y";
				} else {
					flag = "n";
				}
		   }
		}
		 try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(flag);
			out.flush();
			out.close();
			} catch (IOException e) {
				log.error("the action of checkUserNameUnique.do have a trouble!");
			}
	}
	
	@RequestMapping(value = "/resetUserPassword.do", method = RequestMethod.GET)
	public String  resetUserPassword(HttpSession hs,HttpServletRequest request) {
		String back = "redirect:/";
		String user_email = CV.validateCommon(request, "ue");
		String user_password = CV.validateCommon(request, "up");
		User user = null;
		hs.setAttribute("user", user);
		if (user_email != null && user_password != null) {
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("user_email", TransferDES.decrypt(user_email));
			map.put("user_password", user_password);
			user = commonService.selectObject("user.getUserInfo", map);
		}
		
		if(user!=null&&!user.getUser_status().equals("0")) {
			//this address should be corrected in the future
			hs.setAttribute("resetPasswordUserInfo", user);
			back = "redirect:/user/findPasswordStep3.do";
		}
		return back;
	}
	
	@RequestMapping(value = "/modifyUserPassword.do", method = RequestMethod.POST)
	public String modifyUserPassword(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		boolean flag = false;
		String back = "redirect:/";
		String user_password = CV.validatePassword(request, "user_password");
		User user = (User) hs.getAttribute("user");
		User user_ = (User) hs.getAttribute("resetPasswordUserInfo");
		if(user==null&&user_!= null) {
			Map<Object,Object> map = new HashMap<Object,Object>();
			map.put("user_id", user_.getUser_id()+"");
			if(user_password!=null) {
				map.put("user_password", user_password); 
				flag = commonService.updateObject("user.modifyUserPassword", map);
			}
		}
		if(flag==true) {
			user_.setUser_password(user_password);
			back = "redirect:/user/findPasswordStep4.do";
		}
		return back;
	}
	
	@RequestMapping(value = "/doModifyPasswordInner.do", method = RequestMethod.POST)
	public void doModifyPassword(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		User user = (User) hs.getAttribute("user");
		String back = "n";
		String old_password = CV.validatePassword(request, "old_password");
		String new_password = CV.validatePassword(request, "new_password");
		if(user!=null) {
			if(old_password!=null&&new_password!=null&&old_password.equals(user.getUser_password())) {
				//if it satisfies the rules
				Map<String,String> map = new HashMap<String,String>();
				map.put("user_id",user.getUser_id()+"");
				map.put("user_password", new_password);
				boolean b = commonService.updateObject("user.modifyUserPassword", map);
				if(b) {
					back = "y";
					user.setUser_password(new_password);
				} else {
					back = "n";
				}
			} else {
				//if it not satisfies the rules
				back = "n";
			}
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(back);
			out.flush();
			out.close();
		} catch (IOException e) {
		}
	}
	
	@RequestMapping(value = "/doFindPassword.do", method = RequestMethod.POST)
	public String doFindPassword(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String back = "redirect:/user/findPasswordStep1.do";
		User user = (User) hs.getAttribute("user");
		String user_email = CV.validateEmail(request,"email");
		if(user==null&&user_email!=null) {
			Map<Object,Object> map = new HashMap<Object,Object>();
			map.put("user_email",user_email);
			user = commonService.selectObject("user.getUserInfoByEmail", map);
			if(user!=null) {
				//未通过邮箱验证的用户，即未激活用户
				if(user.getUser_status().equals("0")) {
					request.setAttribute("error", "noActivation");
				} else {
					new CommonMailService().sendLinkForFindPassword(user);
					hs.setAttribute("hiddenUserInfo_1", user);
					return "redirect:/user/findPasswordStep2.do";
				}
			} else {
				request.setAttribute("error", "noThisUser");
			}
		} else {
			request.setAttribute("error", "noThisUser");
		}
		return back;
	}
	
	@RequestMapping(value = "/addUserTags.do", method = RequestMethod.POST)
	public void addUserTags(HttpServletResponse response,HttpServletRequest request,HttpSession hs){
		String back = "y";
		User user = (User) hs.getAttribute("user");
		String tag = request.getParameter("tag_content");
		Map<Object,Object> map = new HashMap<Object,Object>();
		if(user!=null) {
			map.put("user_id", user.getUser_id());
			//commonService.deleteObject("user.delMyTags", map);
			if(tag!=null&&tag.length()<21) {
					map.put("tag_content", tag);
					String pk = commonService.selectObject("user.checkTagUnique",map);
					if(pk==null) {
						pk = String.valueOf(commonService.insertObject("user.addTagToLib", map));
					}
					map.put("tag_id", pk);
					commonService.insertObject("user.addUserTag", map);
				back = "y";
			} else {
				back = "n";
			}
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("json/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(back);
			out.flush();
			out.close();
		} catch (IOException e) {
			log.error("ajax got error!");
		}
		
	}
	
	@RequestMapping(value = "/delUserTags.do", method = RequestMethod.POST)
	public void delUserTags(HttpServletResponse response,HttpServletRequest request,HttpSession hs){
		String back = "n";
		User user = (User) hs.getAttribute("user");
		String tag = request.getParameter("tag_content");
		Map<Object,Object> map = new HashMap<Object,Object>();
		if(user!=null) {
			map.put("user_id", user.getUser_id());
			map.put("tag_content", tag);
			int counter = commonService.deleteObject("user.delUserTags",map);
			if(counter>0) {
				back = "y";
			} 
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("json/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(back);
			out.flush();
			out.close();
		} catch (IOException e) {
			log.error("ajax got error!");
		}
		
	}
	
	@RequestMapping(value = "/getUserRecommondTags.do", method = RequestMethod.GET)
	public void getUserRecommondTags(HttpServletResponse response,HttpServletRequest request){
		List<Map<String,String>> list = commonService.selectObjects("user.getUserRecommondTags",null);
		JSONArray j = JSONArray.fromObject(list);
	    String dataString = "{\"results\":" + j.toString() + "}";
	    try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(dataString);
			out.flush();
			out.close();
		} catch (IOException e) {
			log.info("getUserRecommondTags.do"+e);
		}
		
	}

	@RequestMapping(value = "/getMyTags.do", method = RequestMethod.GET)
	public void getMyTags(HttpServletResponse response,HttpServletRequest request,HttpSession hs){
		User user = (User) hs.getAttribute("user");
		Map<Object,Object> map = new HashMap<Object,Object>();
		map.put("user_id", user.getUser_id());
		List<Map<String,String>> list = commonService.selectObjects("user.getMyTags",map);
		if(list!=null&&list.size()!=0) {
			for (Map<String, String> m : list) {
				log.info(m.get("tag_content")+"--------------------------------");
			}
		}
	}
	
	@RequestMapping(value = "/updateUserBasicInfo.do", method = RequestMethod.POST)
	public String updateUserBasicInfo(HttpServletResponse response,HttpServletRequest request,HttpSession hs){
		User user = (User) hs.getAttribute("user");
		boolean flag = true;
		String user_nick = CV.validateNickName(request, "user_nick");
		String user_gender = CV.validateGender(request, "user_gender");
		String user_name = CV.validateNickName(request, "user_name");
		String user_name_status = request.getParameter("user_name_status");
		String province = request.getParameter("province");
		String year = request.getParameter("year");
		String user_address = "";
		//数据库存储过滤
		if(province==null||province.trim().equals("")) {
			user_address = "";
		} else {
			user_address = request.getParameter("province")+"/"+request.getParameter("city");
		}
		String user_birthday = "";
		if(year==null||year.trim().equals("")) {
			user_birthday = "";
		} else {
			user_birthday = request.getParameter("year")+"/"+request.getParameter("month")+"/"+request.getParameter("days");
		}
		String user_birthday_status = request.getParameter("user_birthday_status");
		
		if(user!=null) {
			Map<Object,Object> map = new HashMap<Object,Object>();
			map.put("user_id", user.getUser_id()+"");
			map.put("user_nick", user_nick);
			map.put("user_name", user_name);
			map.put("user_name_status", user_name_status);
			map.put("user_address", user_address);
			map.put("user_gender", user_gender);
			map.put("user_birthday", user_birthday);
			map.put("user_birthday_status", user_birthday_status);
			
			flag = commonService.updateObject("user.updateUserBasicInfo", map);
			if(flag) {
				user.setUser_address(user_address);
				user.setUser_birthday(user_birthday);
				user.setUser_birthday_status(user_birthday_status);
				user.setUser_gender(user_gender);
				user.setUser_name(user_name);
				user.setUser_name_status(user_name_status);
				user.setUser_nick(user_nick);
			} 
			return "redirect:/user/userBasicInfo.do";
			
		} else {
			return "redirect:/user/showUserLogin.do";
		}
	}
	
	@RequestMapping(value = "/addToBlackList.do", method = RequestMethod.POST)
	public void addToBlackList(HttpServletResponse response,HttpServletRequest request,HttpSession hs){
		String flag = "n";
		User user = (User) hs.getAttribute("user");
		if (user != null) {
			String black_user_id = request.getParameter("black_user_id");
			String black_user_name = request.getParameter("black_user_name");
			Map<Object, Object> map = new HashMap<Object, Object>();
			map.put("user_id", user.getUser_id() + "");
			map.put("black_user_id", black_user_id);
			map.put("black_user_name", black_user_name);
			int counter = commonService.countParams("user.checkBlackList", map);
			// this user has already existed in the blacklist.
			if (counter != 0) {
				flag = "n";
			} else {
				commonService.insertObject("user.addToBlackList", map);
				flag = "y";
			}
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(flag);
			out.flush();
			out.close();
		} catch (IOException e) {
			log.info("addToBlackList.do" + e);
		}
	}
	
	@RequestMapping(value = "/removeUserFromBlackList.do", method = RequestMethod.POST)
	public void removeUserFromBlackList(HttpServletResponse response,HttpServletRequest request,HttpSession hs){
		String flag = "n";
		User user = (User) hs.getAttribute("user");
		if(user!=null) {
			String black_user_id = request.getParameter("black_user_id");
			Map<Object,Object> map = new HashMap<Object,Object>();
			map.put("user_id", user.getUser_id()+"");
			map.put("black_user_id", black_user_id);
			commonService.insertObject("user.removeUserFromBlackList", map);
			flag = "y";
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(flag);
			out.flush();
			out.close();
		} catch (IOException e) {
			log.info("removeUserFromBlackList.do" + e);
		}
	}
	
	@RequestMapping(value = "/getBlackList.do", method = RequestMethod.POST)
	public void getBlackList(HttpServletResponse response,HttpServletRequest request,HttpSession hs){
		User user = (User) hs.getAttribute("user");
		if(user!=null) {
			Map<Object,Object> map = new HashMap<Object,Object>();
			map.put("user_id", user.getUser_id()+"");
			List<BlackList> list = commonService.selectObjects("user.getBlackList", map);
			for (BlackList blackList : list) {
				log.info(blackList.getBlack_user_name());
			}
		} else {
			//the user who is visiting the server is not login in or the session has expired;
		}
	}
	
	@RequestMapping(value = "/validateUserSafeEmail.do", method = RequestMethod.GET)
	public String validateUserSafeEmail(HttpSession hs,@RequestParam("ue") String ue,@RequestParam("se") String se,@RequestParam("up") String user_password,HttpServletRequest request,HttpServletResponse response){
		String back = "redirect:/";
		User user = null;
		hs.setAttribute("user", user);
		if(user==null) {//stuation 
			//situation one
			Map<Object,Object> map = new HashMap<Object,Object>();
			String user_email = TransferDES.decrypt(ue);
			String safe_email = TransferDES.decrypt(se);
			map.put("user_email", user_email);
			map.put("user_password", user_password);
			user = commonService.selectObject("user.getUserInfo", map);
			if(user!=null&&user.getSafe_email()!=null&&user.getSafe_email().equals(safe_email)) {
				map.put("user_id", user.getUser_id()+"");
				map.put("safe_email", user.getSafe_email().substring(1));
				boolean flag = commonService.updateObject("user.updateSafeEmailByUserId", map);
				
				if(flag) {
					user.setSafe_email(user.getSafe_email().substring(1));
					hs.setAttribute("user", user);
					//the safe email has been validated,so this action will redirect the special page.
					//back = "";
					hs.setAttribute("flag", "first");
					back = "redirect:/user/userSafeEmail.do";
				}
			}
		} 
		return back;
	}
	
	@RequestMapping(value = "/setUserSafeEmail.do", method = RequestMethod.POST)
	public void setUserSafeEmail(HttpServletResponse response,HttpServletRequest request,HttpSession hs){
		User user = (User) hs.getAttribute("user");
		String back = "error";
		if(user!=null) {
			String safe_email = CV.validateEmail(request, "safe_email");
			String user_password = CV.validatePassword(request, "user_password");
			if(safe_email!=null&&user_password!=null) {
				if(user_password.equals(user.getUser_password())) {
					if(user.getSafe_email()!=null&&user.getSafe_email().equals(safe_email)) {
						back = "same";
					} else {
						if(!safe_email.equals(user.getUser_email())) {
							safe_email = "#"+safe_email;
						}
						Map<Object,Object> map = new HashMap<Object,Object>();
						map.put("user_id", user.getUser_id()+"");
						map.put("safe_email", safe_email);
						
						boolean flag = commonService.updateObject("user.updateSafeEmailByUserId", map);
						
						if(flag) {
							user.setSafe_email(safe_email);
							if(safe_email.equals(user.getUser_email())) {
								back = "own";
							} else {
								back = "wait";
								new CommonMailService().validateUserSafeEmail(user);
							}
						}
					}
				}
			} 
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(back);
			out.flush();
			out.close();      
			} catch (IOException e) {
				log.error("the action of setUserSafeEmail.do have a trouble!");
			}
	}
	
	@RequestMapping(value = "/changeUserSafeEmail.do", method = RequestMethod.POST)
	public void changeUserSafeEmail(HttpServletResponse response,HttpServletRequest request,HttpSession hs){
		User user = (User) hs.getAttribute("user");
		if(user!=null) {
			String new_safe_email = CV.validateEmail(request, "new_safe_email");
			String user_password = CV.validatePassword(request, "user_password");
			
			if(new_safe_email!=null&&user_password!=null) {
				if(user_password.equals(user.getUser_password())) {
					new_safe_email = "#"+new_safe_email;
					Map<Object,Object> map = new HashMap<Object,Object>();
					map.put("user_id", user.getUser_id()+"");
					map.put("safe_email", new_safe_email);
					
					boolean flag = commonService.updateObject("user.updateSafeEmailByUserId", map);
					
					if(flag) {
						user.setSafe_email(new_safe_email);
						new CommonMailService().validateUserSafeEmail(user);
					}
				}
			}
		} else {
			
			//situation two
		}
	}
	
	@RequestMapping(value = "/checkPassword.do", method = RequestMethod.POST)
	public void checkPassword(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String flag = "n"; 
		User user = (User) hs.getAttribute("user");
		String user_password = CV.validatePassword(request, "user_password");
		if(user!=null) {
			if(user_password!=null&&user_password.equals(user.getUser_password())) {
				flag = "y";
			}
		}
		 try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(flag);
			out.flush();
			out.close();      
			} catch (IOException e) {
				log.error("the action of checkPassword.do have a trouble!");
			}
	}
	
	/**
	 * 
	 * 上传用户图像
	 * 
	 */
	@RequestMapping(value = "/uploadUserImage.do", method = RequestMethod.POST)
	public void uploadUserImage(HttpServletRequest request,HttpServletResponse response){
		String path = ImgPathUtil.setImgPath(Const.IMG_TEMP,0);
		File directory = new File(path);
		HttpSession session=request.getSession();
		if(session.getAttribute("userImagefileName")!=null){
			String userImagefileName=(String)session.getAttribute("userImagefileName");
			File orgFile=new File(directory,userImagefileName);
			orgFile.delete();
		}
		String sessionId=session.getId();
		String result="0";
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		CommonsMultipartFile file=(CommonsMultipartFile)multipartRequest.getFile("Filedata");
		if(!file.isEmpty()){
			String oldFileName=file.getOriginalFilename();    
			String ext=oldFileName.substring(oldFileName.lastIndexOf("."));
			Date todayDate=new Date();
			String fileName=String.valueOf(sdf.format(todayDate))+"_"+Math.abs(sessionId.hashCode());
			try {
				FileItem item=file.getFileItem();
				if(item.getSize()>10*1000*1000){
					
				}else{
					String defaultFileName=fileName+ext;
					String tempFileName="temp"+fileName+ext;
					session.setAttribute("userImagefileName", defaultFileName);
					File tempFile=new File(directory,tempFileName);
					if(!tempFile.exists()){
						item.write(tempFile);
					}
					if(tempFile.exists()){
						int width=ImageUtil.getWidth(new FileInputStream(tempFile));
						int height=ImageUtil.getHeight(new FileInputStream(tempFile));
						if(width>=300){
							double rate=(double)width/300;
							int newHeight=(int)((double)height/rate);
							if(newHeight>300){
								if(Const.off==0){
									ImageUtil.cutPicture(new FileInputStream(new File(path+"/"+tempFileName)), new FileOutputStream(new File(path+"/"+defaultFileName)), new Rectangle(0,0,width,(int)(300*rate)));
								}else{
									Img.cutImg(path+"/"+tempFileName, path+"/"+defaultFileName, width, (int)(300*rate), 0, 0);
								}
							}else{
								 FileUtil.copyFile(new FileInputStream(tempFile), new FileOutputStream(new File(directory,defaultFileName)));
							}
						}else if(width==height && width==300){
						     FileUtil.copyFile(new FileInputStream(tempFile), new FileOutputStream(new File(directory,defaultFileName)));
						}else{
							width=300;
							if(Const.off==0){
								ImageUtil.processUserImage(new FileInputStream(new File(directory,tempFileName)), new FileOutputStream(new File(directory,defaultFileName)));
							}else{
								String temptempFileName="temptemp"+fileName+ext;
								Img.createThumbnail(path+"/"+tempFileName, path+"/"+temptempFileName, 300, 300, 0, 0, 1, path+"/"+defaultFileName, "wideth", 0,new Rectangle(0,0,300,300));
							}
						}
						tempFile.delete();
						int newheight=ImageUtil.getHeight(new FileInputStream(new File(directory,defaultFileName)));
						result=fileName+","+ext+","+newheight+","+width;
					}
				}
			} catch (Exception ee) {
				result="0";
				ee.printStackTrace();
			}
		}
		try{
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(result);
			out.flush();
			out.close();
		}catch(Exception e){
				e.printStackTrace();
		}
	}
	
	/**
	 * 图像上传的操作
	 * @param hs
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/uploadUserPortrait.do", method = RequestMethod.POST)
	public void uploadUserPortrait(HttpSession hs,HttpServletRequest request,HttpServletResponse response)throws Exception {
		User uci = (User)hs.getAttribute("user");
		boolean  b = false;
		if(uci!=null) {
			String path = ImgPathUtil.setImgPath(Const.IMG_HEAD,uci.getUser_id());
			File directory = new File(path);
			File directory1 = new File(ImgPathUtil.setImgPath(Const.IMG_TEMP,0));
			String lastUserImage=uci.getUser_portrait();
			File lastImageDefault=new File(directory,"d_"+lastUserImage);
			if(lastImageDefault.exists()){
				lastImageDefault.delete();
			}
			File lastImage180=new File(directory,"180x180_"+lastUserImage);
			if(lastImage180.exists()){
				lastImage180.delete();
			}
			File lastImage30=new File(directory,"30x30_"+lastUserImage);
			if(lastImage30.exists()){
				lastImage30.delete();
			}
			File lastImage50=new File(directory,"50x50_"+lastUserImage);
			if(lastImage50.exists()){
				lastImage50.delete();
			}
			String returnFileName=request.getParameter("returnFileName");
			String top=request.getParameter("top");
			String left=request.getParameter("left");
			String width=request.getParameter("width");
			String rate=request.getParameter("rate");
			
			File orgFile=new File(directory1,returnFileName);
			String defaultName="d_"+returnFileName;
			Rectangle rect=new Rectangle();
			rect.setRect(Double.parseDouble(left)*Double.parseDouble(rate), Double.parseDouble(top)*Double.parseDouble(rate), Double.parseDouble(width)*Double.parseDouble(rate), Double.parseDouble(width)*Double.parseDouble(rate));
			if(Const.off==0){
				ImageUtil.cutPicture(new FileInputStream(orgFile), new FileOutputStream(new File(directory,defaultName)), rect);
			}else{
				Img.cutImg(ImgPathUtil.setImgPath(Const.IMG_TEMP,0)+"/"+returnFileName, path+"/"+defaultName, (int)rect.getWidth(), (int)rect.getHeight(), (int)rect.x, (int)rect.y);
			}
			/**
			 * 生成180px
			 */
			String file180="180x180_"+returnFileName;
			if(Const.off==0){
				double rate180=Double.parseDouble(width)*Double.parseDouble(rate)/180;
				ImageUtil.tarPicture(new FileInputStream(new File(directory,defaultName)), new FileOutputStream(new File(directory,file180)), rate180);
			}
			/**
			 * 生成30px
			 */
			String file30="30x30_"+returnFileName;
			if(Const.off==0){
				double rate30=Double.parseDouble(width)*Double.parseDouble(rate)/30;
				ImageUtil.tarPicture(new FileInputStream(new File(directory,defaultName)), new FileOutputStream(new File(directory,file30)), rate30);
			}
			/**
			 * 生成50px
			 */
			String file50="50x50_"+returnFileName;
			if(Const.off==0){
				double rate50=Double.parseDouble(width)*Double.parseDouble(rate)/50;
				ImageUtil.tarPicture(new FileInputStream(new File(directory,defaultName)), new FileOutputStream(new File(directory,file50)), rate50);
			}
			
			Map<Object,Object> map = new HashMap<Object,Object>();
			map.put("user_id", uci.getUser_id());
			//图片路径
			map.put("user_portrait", returnFileName);
			b = commonService.updateObject("user.uploadUserPortrait", map);
			if(b){
				uci.setUser_portrait(returnFileName);
				orgFile.delete();
			}
		}
		 try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(b+"");
			out.flush();
			out.close();      
			} catch (IOException e) {
				
			}
	}
	@RequestMapping(value = "/addAppAuthInfo.do", method = RequestMethod.POST)
	public String addAppAuthInfo(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		User user = (User) hs.getAttribute("user");
		if(user!=null) {
			String comment_inner = request.getParameter("comment_inner");
			String comment_inner_links = CV.validateSpecialText(request,"comment_inner_links",200);
			String comment_outer = request.getParameter("comment_outer");
			String comment_outer_links = CV.validateSpecialText(request,"comment_outer_links",200);
			String weibo_or_zone = request.getParameter("weibo_or_zone");
			String weibo_or_zone_links = CV.validateSpecialText(request,"weibo_or_zone_links",200);
			
			Map<Object,Object> map = new HashMap<Object,Object>();
			map.put("user_id", user.getUser_id()+"");
			map.put("comment_inner", comment_inner);
			map.put("comment_inner_links", comment_inner_links);
			map.put("comment_outer", comment_outer);
			map.put("comment_outer_links", comment_outer_links);
			map.put("weibo_or_zone", weibo_or_zone);
			map.put("weibo_or_zone_links", weibo_or_zone_links);
			map.put("application_status", "0");
			
			commonService.insertObject("user.addAppAuthInfo", map);
			return "/user/userApplyCommenterThird";
		} else {
			return "redirect:/";
		}
		 
	}
	
	@RequestMapping(value = "/checkTheSameSafeEmail.do", method = RequestMethod.POST)
	public void checkTheSameSafeEmail(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String flag = "n"; 
		User user = (User) hs.getAttribute("user");
		String safe_email = request.getParameter("safe_email");
		if(user!=null) {
			Map<Object,Object> map = new HashMap<Object,Object>();
			map.put("user_id", user.getUser_id()+"");
			map.put("safe_email", safe_email);
			int counter = commonService.countParams("user.checkTheSameSafeEmail", map);
			if(!(counter == 1)) {
				flag = "y";
			}
		}
		 try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(flag);
			out.flush();
			out.close();      
			} catch (IOException e) {
				log.error("the action of checkTheSameSafeEmail.do have a trouble!");
			}
	}
	
	@RequestMapping(value = "/fakeUrl.do", method = RequestMethod.GET)
	public String fakeUrl(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		request.setAttribute("picurl", request.getParameter("url"));
		System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"+request.getParameter("url"));
		return "user/showPic";
	}
	
	@RequestMapping(value = "/doInviteFriends.do", method = RequestMethod.GET)
	public String doInviteFriends(HttpServletResponse response,HttpServletRequest request,HttpSession hs){
		String back = "redirect:/";
		User user = (User) hs.getAttribute("user");
		String inviterId = TransferDES.decrypt(request.getParameter("inviterId"));
		
		if(user!=null) {
			if(inviterId!=null&&inviterId.matches("\\d+")&&!inviterId.equals(user.getUser_id()+"")) {
				//follow the user
			} 
		} else {
			if(inviterId!=null&&inviterId.matches("\\d+")) {
				hs.setAttribute("inviterId", inviterId);
				back = "redirect:/user/showUserLogin.do";
			} 
		}
		return back;
	}
	
	private void followEachOther(String hostId,String guestId) {
		String back = "";
		Map<Object,Object> alreadyInBlack = new HashMap<Object,Object>();
		alreadyInBlack.put("user_id", hostId);
		alreadyInBlack.put("black_user_id", guestId);
		BlackList bl=commonService.selectObject("user.isOrNotAlreayInBlaclList", alreadyInBlack);
		if(bl!=null){
			//该用户为黑名单用户
			back = "4";
		}else{
		//判断是否关注过此用户
			Map<Object,Object>params=new HashMap<Object,Object>();
			params.put("user_id", hostId);
			params.put("attention_id", guestId);
			UserAttention attention=commonService.selectObject("common.AttentionOrNotThisUser", params);
			if(attention!=null){
				if(attention.getIs_read()==0){
					back = "3";
					//已关注
				}else{
					//重新关注
					back = "2";
					boolean result = commonService.updateObject("common.againAttention", params);
					if(result){
						Map<Object,Object> map=new HashMap<Object,Object>();
						map.put("user_id", hostId);
						CommonStat commonStat=commonService.selectObject("common.select_commonStat", map);
						commonStat.setUser_attention_count(commonStat.getUser_attention_count()+1);
						commonService.updateObject("common.updateUserAttention", commonStat);
					}
				}
			}else{
				//首次关注
				UserAttention userAttention = new UserAttention();
				userAttention.setUser_id(Integer.parseInt(hostId));
				userAttention.setAttention_id(Integer.parseInt(guestId));
				userAttention.setIs_read(0);
				int result=commonService.insertObject("common.insert_attention_user", userAttention);
				if(result>0){
					back = "2";
					Map<Object,Object> map=new HashMap<Object,Object>();
					map.put("user_id", hostId);
					CommonStat commonStat=commonService.selectObject("common.select_commonStat", map);
					commonStat.setUser_attention_count(commonStat.getUser_attention_count()+1);
					commonService.updateObject("common.updateUserAttention", commonStat);
				}
			}
		}
	}
	
	@RequestMapping(value = "/groupInvitation.do", method = RequestMethod.POST)
	public void groupInvitation(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String flag = "n"; 
		User user = (User) hs.getAttribute("user");
		String name = request.getParameter("name");
		String sum = request.getParameter("sum");
		String emails[] = sum.split("#");
		if(user!=null) {
			if(emails!=null&&emails.length!=0&&emails.length<21) {
				for (String string : emails) {
					if(string!=null&&!string.equals("")&&string.matches("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$")) {
						new CommonMailService().sendLinkForInvitaion(user.getUser_id()+"", name, string);
						//System.out.println("-----------------------------------------------------id:"+user.getUser_id()+"name:"+name+"string:"+string);
					}
				}
				flag = "y";
			}
		}
		 try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(flag);
			out.flush();
			out.close();      
			} catch (IOException e) {
				log.error("the action of groupInvitation.do have a trouble!");
			}
	}
}
