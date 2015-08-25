package com.piaoyou.interceptors;


import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.piaoyou.domain.User;
import com.piaoyou.service.CommonService;
import com.piaoyou.util.TransferDES;

public class InterceptorLogin extends HandlerInterceptorAdapter {
	@Autowired
	private CommonService commonService;
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp,Object handler) throws Exception {
		HttpSession hs = req.getSession();
		User user = (User) hs.getAttribute("user");
		//String domainName = req.getContextPath();
		
		//页面访问的时候自动检测是否有登陆的cookies
		Cookie[] cookies = req.getCookies();
    	if (cookies != null && user == null) {
    		for (int i = 0; i < cookies.length; i++) {
    			if (("pyh_login").equals(cookies[i].getName())) {
    				String[] content = cookies[i].getValue().split("\\|");
    					if(content.length == 2) {
    						content[0] = TransferDES.decrypt(content[0]);
    						User user_1 = null;
    						Map<Object,Object> map = new HashMap<Object,Object>();
    						if(content[0].matches("^([a-zA-Z0-9_\\.\\-])+\\@(([a-zA-Z0-9\\-])+\\.)+([a-zA-Z0-9]{2,4})+$")) {
    							map.put("user_email", content[0]);
    							map.put("user_password", content[1]);
    							user_1 = commonService.selectObject("user.getUserInfo", map);
    						} 
        					if (user_1 != null) {
        						hs.setAttribute("user", user_1);
        						user = user_1;
        						break;
        					}
    					}
    			}
    		}
    	}
    	
		return true;
	}
}