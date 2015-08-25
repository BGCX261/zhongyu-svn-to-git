package com.piaoyou.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/about")
public class AboutAction {
	@RequestMapping(value = "/helpCenter.do", method = RequestMethod.GET)
	public String test(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		request.setAttribute("target", request.getParameter("target"));
		return "about/helpCenter";
	}
	@RequestMapping(value = "/usingManual.do", method = RequestMethod.GET)
	public String usingManual(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		request.setAttribute("target", request.getParameter("target"));
		return "about/usingManual";
	}
	@RequestMapping(value = "/ticketBuyManual.do", method = RequestMethod.GET)
	public String ticketBuyManual(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		request.setAttribute("target", request.getParameter("target"));
		return "about/ticketBuyManual";
	}
	@RequestMapping(value = "/ticketCooperation.do", method = RequestMethod.GET)
	public String ticketCooperation(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		request.setAttribute("target", request.getParameter("target"));
		return "about/ticketCooperation";
	}
	@RequestMapping(value = "/enterpriseClient.do", method = RequestMethod.GET)
	public String enterpriseClient(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		request.setAttribute("target", request.getParameter("target"));
		return "about/enterpriseClient";
	}
	@RequestMapping(value = "/cooperationWebsite.do", method = RequestMethod.GET)
	public String cooperationWebsite(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		request.setAttribute("target", request.getParameter("target"));
		return "about/cooperationWebsite";
	}
	@RequestMapping(value = "/aboutUs.do", method = RequestMethod.GET)
	public String aboutUs(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		//request.setAttribute("target", request.getParameter("target"));
		return "about/aboutUs";
	}
	//enterprise
	@RequestMapping(value = "/contactUs.do", method = RequestMethod.GET)
	public String contactUs(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		//request.setAttribute("target", request.getParameter("target"));
		return "about/contactUs";
	}
	
	@RequestMapping(value = "/disclaimer.do", method = RequestMethod.GET)
	public String disclaimer(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		//request.setAttribute("target", request.getParameter("target"));
		return "about/disclaimer";
	}
}
