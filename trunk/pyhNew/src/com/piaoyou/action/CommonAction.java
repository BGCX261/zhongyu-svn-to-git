package com.piaoyou.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.piaoyou.domain.BlackList;
import com.piaoyou.domain.CommonComment;
import com.piaoyou.domain.CommonInfo;
import com.piaoyou.domain.CommonStat;
import com.piaoyou.domain.ContentRemark;
import com.piaoyou.domain.RemarkRecommend;
import com.piaoyou.domain.RemarkReply;
import com.piaoyou.domain.User;
import com.piaoyou.domain.UserAttention;
import com.piaoyou.domain.UserCollection;
import com.piaoyou.domain.UserPrivateLetter;
import com.piaoyou.domain.UserRecommend;
import com.piaoyou.domain.UserVermicelli;
import com.piaoyou.service.CommonInfoService;
import com.piaoyou.service.CommonService;
import com.piaoyou.util.Const;
import com.piaoyou.util.ImgPathUtil;
import com.piaoyou.util.StringUtil;

@Controller
@RequestMapping(value = "/common")
public class CommonAction {
	@Autowired
	private CommonService commonService;

	@Autowired
	private CommonInfoService commonInfoService;
	
	private SimpleDateFormat YYYY_MM_DD_HH_MM = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	/*
	 * 添加评论
	 */
	@RequestMapping(value = "/addComment.do", method = RequestMethod.POST)
	public void addComment(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		// 0:表示不成功  1:表示未登录 2:表示成功 
		String back="0";
		String common_id=(request.getParameter("common_id")==null||"".equals(request.getParameter("common_id")))?"0":request.getParameter("common_id");
		String comment_info=(request.getParameter("comment_info")==null||"".equals(request.getParameter("comment_info")))?"":request.getParameter("comment_info");
		String reply_user_id=(request.getParameter("reply_user_id")==null||"".equals(request.getParameter("reply_user_id")))?"0":request.getParameter("reply_user_id");
		String remark_id=(request.getParameter("remark_id")==null||"".equals(request.getParameter("remark_id")))?"0":request.getParameter("remark_id");
		String location_url=request.getParameter("location_url");
		String recommendflag = (request.getParameter("recommendflag")==null||"".equals(request.getParameter("recommendflag")))?"0":"1";
		User user = (User)hs.getAttribute("user");
		if(user!=null) {
			if(reply_user_id==null){
				reply_user_id="0";
			}
			//评论的同时作为推荐发布
			if("1".equals(recommendflag)){
				UserRecommend userRecommend=new UserRecommend();
				userRecommend.setCommon_id(Integer.parseInt(common_id));
				userRecommend.setRecommend_reason(comment_info);
				userRecommend.setUser_id(user.getUser_id());
				userRecommend.setLocation_url(location_url);
				int result=commonService.insertObject("common.insertRecommend", userRecommend);
				Map<Object,Object> map=new HashMap<Object,Object>();
				map.put("user_id", user.getUser_id());
				CommonStat commonStat=commonService.selectObject("common.select_commonStat", map);
				commonStat.setRecommend_count(commonStat.getRecommend_count()+1);
				commonService.updateObject("common.updateRecommendCount", commonStat);
 				int common_recommend_count=(Integer)commonService.selectObject("common.selectCommonRecommendCount", Integer.parseInt(common_id));
				Map<Object,Object> newMap=new HashMap<Object,Object>();
				newMap.put("common_id", Integer.parseInt(common_id));
				newMap.put("recommend_count", common_recommend_count+1);
				commonService.updateObject("common.updateCommonCount", newMap);
			}
			CommonComment commonComment=new CommonComment();
			commonComment.setReply_user_id(Integer.parseInt(reply_user_id));
			commonComment.setComment_info(comment_info);
			commonComment.setCommon_id(Integer.parseInt(common_id));
			commonComment.setComment_user_id(user.getUser_id());
			int result=commonService.insertObject("remark.insert_commoncomment", commonComment);
			if(result>0){
				back="2";
				if(!"0".equals(remark_id)){
					CommonComment comment=commonService.selectObject("remark.getCommentById", Integer.parseInt(remark_id));
					Map<Object,Object> mapReply=new HashMap<Object,Object>();
					mapReply.put("id", remark_id);
					mapReply.put("reply_count", comment.getReply_count()+1);
					commonService.updateObject("remark.updateReplyCount",mapReply );
				}
				Map<Object,Object> map=new HashMap<Object,Object>();
				map.put("user_id", user.getUser_id());
				CommonStat commonStat=commonService.selectObject("common.select_commonStat", map);
				commonStat.setReply_count(commonStat.getReply_count()+1);
				commonService.updateObject("common.updateReplyCount", commonStat);
			}
		}else{
			back="1";
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
			e.printStackTrace();
		}
	}
	/**
	 * 删除评论
	 * @param hs
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/delComment.do", method = RequestMethod.GET)
	public void delComment(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		// 0:表示不成功  1:表示未登录 2:表示成功 
		String comment_id=request.getParameter("comment_id");
		String back="0";
		User user = (User)hs.getAttribute("user");
		if(user!=null){
			int user_id=user.getUser_id();
			int result=commonService.deleteObject("remark.delete_commoncomment", Integer.parseInt(comment_id));
			if(result>0){
				back="2";
				Map<Object,Object> map=new HashMap<Object,Object>();
				map.put("user_id", user_id);
				CommonStat commonStat=commonService.selectObject("common.select_commonStat", map);
				if(commonStat.getRecommend_count()>0){
					commonStat.setReply_count(commonStat.getRecommend_count()-1);
					commonService.updateObject("common.updateReplyCount", commonStat);
				}
			}
		}else{
			back="1";
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
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取演出下的所有评论
	 * @param hs
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getCommentList.do", method = RequestMethod.GET)
	public void getCommentList(HttpServletRequest request,HttpServletResponse response,HttpSession hs){
		
		int user_id = 0;
		User user = (User)hs.getAttribute("user");
		if(user==null){
			user_id = 0;
		}else{
			user_id = user.getUser_id();
		}
		
		String common_id=request.getParameter("common_id");
		int pageIndex = 1 ;
		int pageCount = 10 ;
		try{
			pageIndex = (request.getParameter("pageIndex")==null||"".equals(request.getParameter("pageIndex")))?1:Integer.parseInt(request.getParameter("pageIndex"));
		}catch(Exception e){
			pageIndex = 1 ;
		} 
		try{
			pageCount = (request.getParameter("pageCount")==null||"".equals(request.getParameter("pageCount")))?10:Integer.parseInt(request.getParameter("pageCount"));
		}catch(Exception e){
			pageIndex = 10 ;
		} 
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("common_id", Integer.parseInt(common_id));
		params.put("pageIndex", (pageIndex-1)*pageCount);
		params.put("pageCount", pageCount);
		int totalCount  =  commonService.selectObject("remark.selectCommonCommenttCountByCommonId", params);
	    List<CommonComment>	commentList=commonService.selectObjects("remark.selectCommonCommenttByCommonId", params);
	    for(CommonComment commonComment:commentList){
	    	int comment_user_id=commonComment.getComment_user_id();
	    	if(commonComment.getUser_portrait()!=null){
	    		commonComment.setUser_portrait(Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, comment_user_id)+"50x50_"+commonComment.getUser_portrait());
	    	}else{
	    		//设置默认的图像路径
	    		String path=request.getContextPath();
	    		commonComment.setUser_portrait(path+"/public/images/head_default3.png");
	    	}
	    }
	    int pageTotal= 0 ;
		if (totalCount % pageCount == 0) {
			pageTotal = totalCount / pageCount;
		} else {
			pageTotal = totalCount / (pageCount + 1);
		}
		String totaljson = "[{\"totalcount\":\""+totalCount+"\"}]";
		String totalPagejson = "[{\"totalPagecount\":\"" + pageTotal + "\"}]"; 
		JSONArray j = JSONArray.fromObject(commentList);
		String dataString = "{\"user_id\":\""+user_id+"\",\"totalproperty\":"+totaljson+",\"results\":" + j.toString() + ",\"totalPage\":" + totalPagejson + "}";
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(dataString);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	
	/**
	 * 添加影评
	 * @param hs
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/addShowComment.do", method = RequestMethod.POST)
	public void addShowComment(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		// 0:表示不成功  1:表示未登录 2:表示成功 
		String back="0";
		String common_id=request.getParameter("common_id");
		String title=request.getParameter("title");
		String comment_info=request.getParameter("comment_info");
		String level=request.getParameter("level");
		User user = (User)hs.getAttribute("user");
		try{
			if(user!=null) {
				ContentRemark contentRemark=new ContentRemark();
				contentRemark.setCommon_id(Integer.parseInt(common_id));
				contentRemark.setContent(comment_info);
				contentRemark.setTitle(title);
				contentRemark.setLevel(Integer.parseInt(level));
				contentRemark.setUser_id(user.getUser_id());
				int result=commonService.insertObject("remark.insert_contentremark", contentRemark);
				if(result>0){
					back="2";
				}
			}else{
				back="1";
			}
		}catch(Exception e){
			back="0";
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
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 获取电影，演出，团购的所有影评
	 * @param hs
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getShowCommentList.do", method = RequestMethod.GET)
	public void getShowCommentList(HttpServletRequest request,HttpServletResponse response){
		String common_id=request.getParameter("common_id");
		int pageIndex = 1 ;
		int pageCount = 10 ;
		try{
			pageIndex = (request.getParameter("pageIndex")==null||"".equals(request.getParameter("pageIndex")))?1:Integer.parseInt(request.getParameter("pageIndex"));
		}catch(Exception e){
			pageIndex = 1 ;
		} 
		try{
			pageCount = (request.getParameter("pageCount")==null||"".equals(request.getParameter("pageCount")))?10:Integer.parseInt(request.getParameter("pageCount"));
		}catch(Exception e){
			pageIndex = 10 ;
		} 
		String flag=request.getParameter("flag");
		if(flag==null||"".equals(flag)){
			flag="0";
		}
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("common_id", Integer.parseInt(common_id));
		params.put("pageIndex", (pageIndex-1)*pageCount);
		params.put("pageCount", pageCount);
		params.put("flag", flag);
		int totalCount  =  commonService.selectObject("remark.selectContentRemarkByCommonIdCount", params);
	    List<ContentRemark>	commentList=commonService.selectObjects("remark.selectContentRemarkByCommonId", params);
	    for(ContentRemark commonComment:commentList){
	    	int comment_user_id=commonComment.getUser_id();
	    	if(commonComment.getUser_portrait()!=null){
	    		commonComment.setUser_portrait(Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, comment_user_id)+"50x50_"+commonComment.getUser_portrait());
	    	}else{
	    		//设置默认的图像路径
	    		String path=request.getContextPath();
	    		commonComment.setUser_portrait(path+"/public/images/head_default3.png");
	    	}
	    	
	    	//获取剧评的前140个字符
	    	String content = commonComment.getContent();
	    	if(content!=null){
	    		content = Jsoup.parse(content).text();
	    		if(content.length()>140){
	    			content = content.substring(0,140);
	    		}
	    		commonComment.setContent(content);
	    	}
	    }
	    int pageTotal= 0 ;
		if (totalCount % pageCount == 0) {
			pageTotal = totalCount / pageCount;
		} else {
			pageTotal = totalCount / (pageCount + 1);
		}
		String totaljson = "[{\"totalcount\":\""+totalCount+"\"}]";
		String totalPagejson = "[{\"totalPagecount\":\"" + pageTotal + "\"}]"; 
		JSONArray j = JSONArray.fromObject(commentList);
		String dataString = "{\"totalproperty\":"+totaljson+",\"results\":" + j.toString() + ",\"totalPage\":" + totalPagejson + "}";
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(dataString);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	
	
	@RequestMapping(value="getShowCommentById",method=RequestMethod.GET)
	public void getShowCommentById(HttpServletRequest request,HttpServletResponse response){
		String id=request.getParameter("id");
		String type=request.getParameter("type");
		ContentRemark contentRemark = commonService.selectObject("remark.selectContentRemarkById", Integer.parseInt(id));
		String content ="";
		if("expend".equals(type)){
			content = contentRemark.getContent();
		}else{
			content = contentRemark.getContent();
	    	if(content!=null){
	    		content = Jsoup.parse(content).text();
	    		if(content.length()>140){
	    			content = content.substring(0,140);
	    		}
	    	}
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(content);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
		
	}
	
	
	
	/**
	 * 对影评进行回复
	 * @param hs
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/addShowCommentReply.do", method = RequestMethod.POST)
	public void addShowCommentReply(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		// 0:表示不成功  1:表示未登录 2:表示成功 
		String back="0";
		String remark_id=request.getParameter("remark_id");
		String comment_info=request.getParameter("comment_info");
		User user = (User)hs.getAttribute("user");
		String recommendflag = (request.getParameter("recommendflag")==null||"".equals(request.getParameter("recommendflag")))?"0":request.getParameter("recommendflag");
		try{
			if(user!=null) {
				if("1".equals(recommendflag)){  //推荐的同时作为评论发布
					RemarkRecommend remarkRecommend = new RemarkRecommend();
					remarkRecommend.setUser_id(user.getUser_id());
					remarkRecommend.setRemark_id(Integer.parseInt(remark_id));
					remarkRecommend.setRecommend_reason(comment_info);
					commonService.insertObject("common.addRemarkRecommend", remarkRecommend);
				}
				RemarkReply repley=new RemarkReply();
				repley.setRemark_id(Integer.parseInt(remark_id));
				repley.setUser_id(user.getUser_id());
				repley.setContent(comment_info);
				int result=commonService.insertObject("remark.insert_remarkreply", repley);
				if(result>0){
					back="2";
				}
			}else{
				back="1";
			}
		}catch(Exception e){
			back = "0";
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
			e.printStackTrace();
		}
	}
	
	/**
	 * 查看影评回复的所有评论的评论
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getShowCommentReplyList.do", method = RequestMethod.GET)
	public void getShowCommentReplyList(HttpServletRequest request,HttpServletResponse response){
		String remark_id=request.getParameter("remark_id");
		int pageIndex = 1 ;
		int pageCount = 10 ;
		try{
			pageIndex = (request.getParameter("pageIndex")==null||"".equals(request.getParameter("pageIndex")))?1:Integer.parseInt(request.getParameter("pageIndex"));
		}catch(Exception e){
			pageIndex = 1 ;
		} 
		try{
			pageCount = (request.getParameter("pageCount")==null||"".equals(request.getParameter("pageCount")))?10:Integer.parseInt(request.getParameter("pageCount"));
		}catch(Exception e){
			pageIndex = 10 ;
		} 
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("remark_id", Integer.parseInt(remark_id));
		params.put("pageIndex", (pageIndex-1)*pageCount);
		params.put("pageCount", pageCount);
		int totalCount  =  commonService.selectObject("remark.selectRemarkReplyByRemarkIdCount", params);
	    List<RemarkReply>	commentList=commonService.selectObjects("remark.selectRemarkReplyByRemarkId", params);
	    for(RemarkReply commonComment:commentList){
	    	int comment_user_id=commonComment.getUser_id();
	    	if(commonComment.getUser_portrait()!=null){
	    		commonComment.setUser_portrait(Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, comment_user_id)+"50x50_"+commonComment.getUser_portrait());
	    	}else{
	    		//设置默认的图像路径
	    		String path=request.getContextPath();
	    		commonComment.setUser_portrait(path+"/public/images/head_default3.png");
	    	}
	    }
	    int pageTotal= 0 ;
		if (totalCount % pageCount == 0) {
			pageTotal = totalCount / pageCount;
		} else {
			pageTotal = totalCount / (pageCount + 1);
		}
		String totaljson = "[{\"totalcount\":\""+totalCount+"\"}]";
		String totalPagejson = "[{\"totalPagecount\":\"" + pageTotal + "\"}]"; 
		JSONArray j = JSONArray.fromObject(commentList);
		String dataString = "{\"totalproperty\":"+totaljson+",\"results\":" + j.toString() + ",\"totalPage\":" + totalPagejson + "}";
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(dataString);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	
	/**
	 * 跳到用户的收藏页面
	 */
	/**
	 * @param hs
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/gotoMyCollection.do", method = RequestMethod.GET)
	public String gotoMyCollection(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		User user = (User)hs.getAttribute("user");
		if(user!=null){
			int user_id=user.getUser_id();
			String address=user.getUser_address();
			if(address!=null && !"".equals(address)){
				String[] district=address.split("/");
				if(district.length==1) {
					request.setAttribute("address", district[0]);
				} else if(district.length==2) {
					request.setAttribute("address", district[0]+" "+district[1]);
				}
			}
			request.setAttribute("user_name", user.getUser_nick());
			CommonStat stat=commonService.selectObject("common.selectUserInfoByUserId", user_id);
			request.setAttribute("stat", stat);
			if(user.getUser_portrait()!=null){
				request.setAttribute("headPath", Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, user_id)+"50x50_"+user.getUser_portrait());
	    	}else{
	    		//设置默认的图像路径
	    		String path=request.getContextPath();
	    		request.setAttribute("headPath", path+"/public/images/head_default3.png");
	    	}
			Map<Object,Object> mapTag = new HashMap<Object,Object>();
			mapTag.put("user_id", user_id);
			List<Map<String,String>> mapTagList = commonService.selectObjects("user.getMyTags",mapTag);
			request.setAttribute("mapTagList", mapTagList);
			return "user/myfavicon";
		}else{
			//跳到登陆页面
			return "redirect:/user/showUserLogin.do";
		}
	}
	
	/**
	 * 添加用户的收藏
	 */
	@RequestMapping(value = "/addUserCollection.do", method = RequestMethod.POST)
	public void addUserCollection(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		// 0:表示不成功  1:表示未登录 2:表示成功 3:已经收藏啦
		String back="0";
		User user = (User)hs.getAttribute("user");
		String common_id=request.getParameter("common_id");
		String location_url=request.getParameter("location_url");
		if(user!=null) {
			int user_id=user.getUser_id();
			//判断用户是否已经收藏啦
			Map<Object,Object> alreayMap=new HashMap<Object,Object>();
			alreayMap.put("user_id", user_id);
			alreayMap.put("common_id", common_id);
			UserCollection alreayCollection=commonService.selectObject("common.isAlreadyCollect", alreayMap);
			if(alreayCollection!=null){
				back="3";
			}else{
				UserCollection userCollection=new UserCollection();
				userCollection.setCommon_id(Integer.parseInt(common_id));
				userCollection.setUser_id(user_id);
				userCollection.setLocation_url(location_url);
				int result=commonService.insertObject("common.insertCollection", userCollection);
				if(result>0){
					back="2";
					Map<Object,Object> map=new HashMap<Object,Object>();
					map.put("user_id", user_id);
					CommonStat commonStat=commonService.selectObject("common.select_commonStat", map);
					commonStat.setUser_collect_count(commonStat.getUser_collect_count()+1);
					commonService.updateObject("common.updateUserCollection", commonStat);
				}
			}
		}else{
			back="1";
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
			e.printStackTrace();
		}
	}
	/**
	 * 删除用户的收藏
	 * @param hs
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/delUserCollection.do", method = RequestMethod.POST)
	public void delUserCollection(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		// 0:表示不成功  1:表示未登录 2:表示成功 
		String back="0";
		User user = (User)hs.getAttribute("user");
		String common_id=request.getParameter("common_id");
		if(user!=null) {
			int user_id=user.getUser_id();
			Map<Object,Object> map=new HashMap<Object,Object>();
			map.put("common_id", common_id);
			map.put("user_id", user.getUser_id());
			int result=commonService.deleteObject("common.delete_collection", map);
			if(result>0){
				back="2";
				Map<Object,Object> mapStat=new HashMap<Object,Object>();
				mapStat.put("user_id", user_id);
				CommonStat commonStat=commonService.selectObject("common.select_commonStat", map);
				if(commonStat.getUser_collect_count()>0){
					commonStat.setUser_collect_count(commonStat.getUser_collect_count()-1);
					commonService.updateObject("common.updateUserCollection", commonStat);
				}
			}
		}else{
			back="1";
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
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询用户的收藏
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getUserCollectionList.do", method = RequestMethod.GET)
	public void getUserCollectionList(HttpServletRequest request,HttpServletResponse response){
		String user_id=request.getParameter("user_id");
		//0:按照演出日期排序   1:照收藏日期排序
		String flag=request.getParameter("flag")==null? "0":request.getParameter("flag");
		int pageIndex = 1 ;
		int pageCount = 5 ;
		try{
			pageIndex = (request.getParameter("pageIndex")==null||"".equals(request.getParameter("pageIndex")))?1:Integer.parseInt(request.getParameter("pageIndex"));
		}catch(Exception e){
			pageIndex = 1 ;
		} 
		try{
			pageCount = (request.getParameter("pageCount")==null||"".equals(request.getParameter("pageCount")))?5:Integer.parseInt(request.getParameter("pageCount"));
		}catch(Exception e){
			pageIndex = 5 ;
		}
		Map<Object,Object> params=new HashMap<Object,Object>();
		params.put("user_id", user_id);
		params.put("flag", flag);
		int totalCount  =  commonService.selectObject("common.selectCollectionCountByUserId", params);
		params.put("pageIndex", (pageIndex-1)*pageCount);
		params.put("pageCount", pageCount);
		List<UserCollection> collectionList=commonService.selectObjects("common.selectCollectionByUserId", params);
		for(UserCollection collection:collectionList){
			collection.setIntroduction(StringUtil.htmlFilter(collection.getIntroduction()));
			if(collection.getIntroduction()!=null){
				collection.setIntroduction(collection.getIntroduction().replaceAll(" | |	|&nbsp;| ", ""));
			}
		}
		int pageTotal= 0 ;
		if (totalCount % pageCount == 0) {
			pageTotal = totalCount / pageCount;
		} else {
			pageTotal = totalCount / (pageCount + 1);
		}
		String totaljson = "[{\"totalcount\":\""+totalCount+"\"}]";
		String totalPagejson = "[{\"totalPagecount\":\"" + pageTotal + "\"}]"; 
		JSONArray j = JSONArray.fromObject(collectionList);
		String dataString = "{\"totalproperty\":"+totaljson+",\"results\":" + j.toString() + ",\"totalPage\":" + totalPagejson + "}";
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(dataString);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	
	/**
	 * 查询用户的收藏(单条)
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getOnlyUserCollectionList.do", method = RequestMethod.GET)
	public void getOnlyUserCollectionList(HttpServletRequest request,HttpServletResponse response){
		String user_id=request.getParameter("user_id");
		//0:按照演出日期排序   1:照收藏日期排序
		String flag=request.getParameter("flag")==null? "0":request.getParameter("flag");
		int pageIndex = 1 ;
		int pageCount = 1 ;
		try{
			pageIndex = (request.getParameter("pageIndex")==null||"".equals(request.getParameter("pageIndex")))?1:Integer.parseInt(request.getParameter("pageIndex"));
		}catch(Exception e){
			pageIndex = 1 ;
		} 
		try{
			pageCount = (request.getParameter("pageCount")==null||"".equals(request.getParameter("pageCount")))?1:Integer.parseInt(request.getParameter("pageCount"));
		}catch(Exception e){
			pageIndex = 1 ;
		}
		Map<Object,Object> params=new HashMap<Object,Object>();
		params.put("user_id", user_id);
		int totalCount  =  commonService.selectObject("common.selectCollectionCountByUserId", params);
		params.put("flag", flag);
		params.put("pageIndex", (pageIndex-1)*pageCount);
		params.put("pageCount", pageCount);
		List<UserCollection> collectionList=commonService.selectObjects("common.selectCollectionByUserId", params);
		int pageTotal= 0 ;
		if (totalCount % pageCount == 0) {
			pageTotal = totalCount / pageCount;
		} else {
			pageTotal = totalCount / (pageCount + 1);
		}
		String totaljson = "[{\"totalcount\":\""+totalCount+"\"}]";
		String totalPagejson = "[{\"totalPagecount\":\"" + pageTotal + "\"}]"; 
		JSONArray j = JSONArray.fromObject(collectionList);
		String dataString = "{\"totalproperty\":"+totaljson+",\"results\":" + j.toString() + ",\"totalPage\":" + totalPagejson + "}";
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(dataString);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	
	/**
	 * 获取用户收藏的总数
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getOnlyUserCollectionListCount.do", method = RequestMethod.GET)
	public void getOnlyUserCollectionListCount(HttpServletRequest request,HttpServletResponse response){
		String user_id=request.getParameter("user_id");
		//0:按照演出日期排序   1:照收藏日期排序
		Map<Object,Object> params=new HashMap<Object,Object>();
		params.put("user_id", user_id);
		int totalCount  =  commonService.selectObject("common.selectCollectionCountByUserId", params);
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(String.valueOf(totalCount));
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	
	
	/**
	 * 跳到用户的推荐的页面
	 */
	@RequestMapping(value = "/gotoMyRecommend.do", method = RequestMethod.GET)
	public String gotoMyRecommend(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		User user = (User)hs.getAttribute("user");
		if(user!=null){
			int user_id=user.getUser_id();
			CommonStat stat=commonService.selectObject("common.selectUserInfoByUserId", user_id);
			request.setAttribute("stat", stat);
			String address=user.getUser_address();
			if(address!=null && !"".equals(address)){
				String[] district=address.split("/");
				if(district.length==1) {
					request.setAttribute("address", district[0]);
				} else if(district.length==2) {
					request.setAttribute("address", district[0]+" "+district[1]);
				}
			}
			request.setAttribute("user_name", user.getUser_nick());
			if(user.getUser_portrait()!=null){
				request.setAttribute("headPath", Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, user_id)+"50x50_"+user.getUser_portrait());
	    	}else{
	    		//设置默认的图像路径
	    		String path=request.getContextPath();
	    		request.setAttribute("headPath", path+"/public/images/head_default3.png");
	    	}
			Map<Object,Object> mapTag = new HashMap<Object,Object>();
			mapTag.put("user_id", user_id);
			List<Map<String,String>> mapTagList = commonService.selectObjects("user.getMyTags",mapTag);
			request.setAttribute("mapTagList", mapTagList);
			return "user/myRecommend";
		}else{
			//跳到登陆页面
			return "redirect:/user/showUserLogin.do";
		}
	}
	/**
	 * 添加用户的推荐
	 */
	@RequestMapping(value = "/addUserRecommend.do", method = RequestMethod.POST)
	public void addUserRecommend(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		// 0:表示不成功  1:表示未登录 2:表示成功 3:已经推荐啦
		String common_id=request.getParameter("common_id");
		String reason=request.getParameter("reason");
		String location_url=request.getParameter("location_url");
		String flag=request.getParameter("flag");
		User user = (User)hs.getAttribute("user");
		String back="0";
		if(user!=null) {
			int user_id=user.getUser_id();
				UserRecommend userRecommend=new UserRecommend();
				userRecommend.setCommon_id(Integer.parseInt(common_id));
				userRecommend.setRecommend_reason(reason);
				userRecommend.setUser_id(user_id);
				userRecommend.setLocation_url(location_url);
				int result=commonService.insertObject("common.insertRecommend", userRecommend);
				if(result>0){
					back="2";
					Map<Object,Object> map=new HashMap<Object,Object>();
					map.put("user_id", user_id);
					CommonStat commonStat=commonService.selectObject("common.select_commonStat", map);
					commonStat.setRecommend_count(commonStat.getRecommend_count()+1);
					commonService.updateObject("common.updateRecommendCount", commonStat);
					int common_recommend_count=(Integer)commonService.selectObject("common.selectCommonRecommendCount", Integer.parseInt(common_id));
					Map<Object,Object> newMap=new HashMap<Object,Object>();
					newMap.put("common_id", Integer.parseInt(common_id));
					newMap.put("recommend_count", common_recommend_count+1);
					commonService.updateObject("common.updateCommonCount", newMap);
					//同时添加评论
					if(null!=flag && "1".equals(flag)){
						CommonComment commonComment=new CommonComment();
						commonComment.setReply_user_id(0);
						commonComment.setComment_info(reason);
						commonComment.setCommon_id(Integer.parseInt(common_id));
						commonComment.setComment_user_id(user.getUser_id());
						int remark_id=commonService.insertObject("remark.insert_commoncomment", commonComment);
						if(remark_id>0){
							commonStat.setReply_count(commonStat.getReply_count()+1);
							commonService.updateObject("common.updateReplyCount", commonStat);
						}
						
				}
			}
		}else{
			//条转到登陆页面
			back="1";
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(back);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	/**
	 * 删除用户的推荐
	 * @param hs
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/delUserRecommend.do", method = RequestMethod.POST)
	public void delUserRecommend(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		// 0:表示不成功  1:表示未登录 2:表示成功 
		String back="0";
		User user = (User)hs.getAttribute("user");
		String common_id=request.getParameter("common_id");
		if(user!=null){
			int user_id=user.getUser_id();
			Map<Object,Object> map=new HashMap<Object,Object>();
			map.put("common_id", common_id);
			map.put("user_id", user_id);
			int result=commonService.deleteObject("common.delete_Recommend", map);
			if(result>0){
				back="2";
				Map<Object,Object> paramStat=new HashMap<Object,Object>();
				paramStat.put("user_id", user_id);
				CommonStat commonStat=commonService.selectObject("common.select_commonStat", paramStat);
				if(commonStat.getRecommend_count()>0){
					commonStat.setRecommend_count(commonStat.getRecommend_count()-1);
					commonService.updateObject("common.updateRecommendCount", commonStat);
				}
			}
		}else{
			back="1";
			//条转到登陆页面
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(back);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	/**
	 * 查询用户的推荐
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getUserRecommendList.do", method = RequestMethod.GET)
	public void getUserRecommendList(HttpServletRequest request,HttpServletResponse response){
		String user_id=request.getParameter("user_id");
		//0:按照演出日期排序   1:照收藏日期排序
		String flag=(request.getParameter("flag")==null|| "".equals(request.getParameter("flag")))? "0":request.getParameter("flag");
		int pageIndex = 1 ;
		int pageCount = 5 ;
		try{
			pageIndex = (request.getParameter("pageIndex")==null||"".equals(request.getParameter("pageIndex")))?1:Integer.parseInt(request.getParameter("pageIndex"));
		}catch(Exception e){
			pageIndex = 1 ;
		} 
		try{
			pageCount = (request.getParameter("pageCount")==null||"".equals(request.getParameter("pageCount")))?5:Integer.parseInt(request.getParameter("pageCount"));
		}catch(Exception e){
			pageIndex = 5 ;
		}
		Map<Object,Object> params=new HashMap<Object,Object>();
		params.put("user_id", user_id);
		params.put("flag", flag);
		int totalCount  =  commonService.selectObject("common.selectRecommendCountByUserId", params);
		params.put("pageIndex", (pageIndex-1)*pageCount);
		params.put("pageCount", pageCount);
		List<UserRecommend> recommendList=commonService.selectObjects("common.selectRecommendByUserId", params);
		for(UserRecommend recommend:recommendList){
			recommend.setIntroduction(StringUtil.htmlFilter(recommend.getIntroduction()));
			if(recommend.getIntroduction()!=null){
				recommend.setIntroduction(recommend.getIntroduction().replaceAll(" | |	|&nbsp;| ", ""));
			}
		}
		int pageTotal= 0 ;
		if (totalCount % pageCount == 0) {
			pageTotal = totalCount / pageCount;
		} else {
			pageTotal = totalCount / (pageCount + 1);
		}
		String totaljson = "[{\"totalcount\":\""+totalCount+"\"}]";
		String totalPagejson = "[{\"totalPagecount\":\"" + pageTotal + "\"}]"; 
		JSONArray j = JSONArray.fromObject(recommendList);
		String dataString = "{\"totalproperty\":"+totaljson+",\"results\":" + j.toString() + ",\"totalPage\":" + totalPagejson + "}";
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(dataString);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	
	/**
	 * 跳到用户的推荐的页面
	 */
	@RequestMapping(value = "/gotoMyPiaoyou.do", method = RequestMethod.GET)
	public String gotoMyPiaoyou(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		User user = (User)hs.getAttribute("user");
		if(user!=null){
			int user_id=user.getUser_id();
			String address=user.getUser_address();
			if(address!=null && !"".equals(address)){
				String[] district=address.split("/");
				if(district.length==1) {
					request.setAttribute("address", district[0]);
				} else if(district.length==2) {
					request.setAttribute("address", district[0]+" "+district[1]);
				}
			}
			request.setAttribute("user_name", user.getUser_nick());
			CommonStat stat=commonService.selectObject("common.selectUserInfoByUserId", user_id);
			request.setAttribute("stat", stat);
			if(user.getUser_portrait()!=null){
				request.setAttribute("headPath", Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, user_id)+"50x50_"+user.getUser_portrait());
	    	}else{
	    		//设置默认的图像路径
	    		String path=request.getContextPath();
	    		request.setAttribute("headPath", path+"/public/images/head_default3.png");
	    	}
			Map<Object,Object> mapTag = new HashMap<Object,Object>();
			mapTag.put("user_id", user_id);
			List<Map<String,String>> mapTagList = commonService.selectObjects("user.getMyTags",mapTag);
			request.setAttribute("mapTagList", mapTagList);
			return "user/piaoyou";
		}else{
			//跳到登陆页面
			return "redirect:/user/showUserLogin.do";
		}
	}
	
	/**
	 * 添加用户关注
	 * @param hs
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/addUserAttention.do", method = RequestMethod.GET)
	public void addUserAttention(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		// 0:表示不成功  1:表示未登录 2:表示成功 3:已经关注啦 4:表示在黑名单里
		String back="0";
		String attention_user_id=request.getParameter("attention_user_id");
		User user = (User)hs.getAttribute("user");
		if(user!=null){
			int user_id=user.getUser_id();
			Map<Object,Object> alreadyInBlack=new HashMap<Object,Object>();
			alreadyInBlack.put("user_id", user_id);
			alreadyInBlack.put("black_user_id", attention_user_id);
			BlackList bl=commonService.selectObject("user.isOrNotAlreayInBlaclList", alreadyInBlack);
			if(bl!=null){
				//该用户已经被你拉到黑名单里啦
				back="4";
			}else{
			//判断是否关注过此用户
				Map<Object,Object>params=new HashMap<Object,Object>();
				params.put("user_id", user_id);
				params.put("attention_id", attention_user_id);
				UserAttention attention=commonService.selectObject("common.AttentionOrNotThisUser", params);
				if(attention!=null){
					if(attention.getIs_read()==0){
						back="3";
						//已经关注啦
					}else{
						//重新关注
						back="2";
						boolean result=commonService.updateObject("common.againAttention", params);
						if(result){
							Map<Object,Object> map=new HashMap<Object,Object>();
							map.put("user_id", user_id);
							CommonStat commonStat=commonService.selectObject("common.select_commonStat", map);
							commonStat.setUser_attention_count(commonStat.getUser_attention_count()+1);
							commonService.updateObject("common.updateUserAttention", commonStat);
						}
					}
				}else{
					//首次关注
					UserAttention userAttention=new UserAttention();
					userAttention.setUser_id(user_id);
					userAttention.setAttention_id(Integer.parseInt(attention_user_id));
					userAttention.setIs_read(0);
					int result=commonService.insertObject("common.insert_attention_user", userAttention);
					if(result>0){
						back="2";
						Map<Object,Object> map=new HashMap<Object,Object>();
						map.put("user_id", user_id);
						CommonStat commonStat=commonService.selectObject("common.select_commonStat", map);
						commonStat.setUser_attention_count(commonStat.getUser_attention_count()+1);
						commonService.updateObject("common.updateUserAttention", commonStat);
					}
				}
			}
		}else{
				back="1";
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
			e.printStackTrace();
		}
	}
	
	/**
	 * 取消用户关注
	 */
	@RequestMapping(value = "/cancelAttention.do", method = RequestMethod.GET)
	public void cancelAttention(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		// 0:表示不成功  1:表示未登录 2:表示成功
		String back="0";
		String attention_user_id=request.getParameter("attention_user_id");
		User user = (User)hs.getAttribute("user");
		if(user!=null){
			int user_id=user.getUser_id();
			Map<Object,Object>params=new HashMap<Object,Object>();
			params.put("user_id", user_id);
			params.put("attention_id", attention_user_id);
			boolean result=commonService.updateObject("common.cancelAttention", params);
			if(result){
				back="2";
				Map<Object,Object> map=new HashMap<Object,Object>();
				map.put("user_id", user_id);
				CommonStat commonStat=commonService.selectObject("common.select_commonStat", map);
				if(commonStat.getUser_attention_count()>0){
					commonStat.setUser_attention_count(commonStat.getUser_attention_count()-1);
					commonService.updateObject("common.updateUserAttention", commonStat);
				}
			}
		}else{
			//用户还没有登录
			back="1";
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
			e.printStackTrace();
		}
	}
	
	/**
	 * 查询用户关注的列表
	 */
	@RequestMapping(value = "/getAllUserAttentionList.do", method = RequestMethod.GET)
	public void getAllUserAttentionList(HttpServletRequest request,HttpServletResponse response){
		String flag=(request.getParameter("flag")==null||"".equals(request.getParameter("flag")))? "0" : request.getParameter("flag");
		String user_id=request.getParameter("user_id");
		String dataString;
		int pageIndex = 1 ;
		int pageCount = 5 ;
		try{
			pageIndex = (request.getParameter("pageIndex")==null||"".equals(request.getParameter("pageIndex")))?1:Integer.parseInt(request.getParameter("pageIndex"));
		}catch(Exception e){
			pageIndex = 1 ;
		} 
		try{
			pageCount = (request.getParameter("pageCount")==null||"".equals(request.getParameter("pageCount")))?5:Integer.parseInt(request.getParameter("pageCount"));
		}catch(Exception e){
			pageIndex = 5 ;
		}
		if("0".equals(flag)){
			Map<Object,Object> params=new HashMap<Object,Object>();
			params.put("user_id", user_id);
			int totalCount  =  commonService.selectObject("common.select_user_attention_count", params);
			params.put("pageIndex", (pageIndex-1)*pageCount);
			params.put("pageCount", pageCount);
			List<UserAttention> list=commonService.selectObjects("common.select_user_attention", params);
			for(UserAttention attention:list){
				int attention_id=attention.getAttention_id();
				Map<Object,Object> mapTag = new HashMap<Object,Object>();
				mapTag.put("user_id", attention_id);
				List<Map<String,String>> mapTagList = commonService.selectObjects("user.getMyTags",mapTag);
			    String user_tag="";
			    for(int i=0;i<mapTagList.size();i++){
			    	if(i==0){
			    		user_tag+=mapTagList.get(i).get("tag_content");
			    	}else{
			    		user_tag+=(","+mapTagList.get(i).get("tag_content"));
			    	}
			    }
			    attention.setUser_tag(user_tag);
				Map<Object,Object> tmp=new HashMap<Object,Object>();
				tmp.put("user_id", user_id);
				tmp.put("user_attention_id", attention_id);
				//共同关注的人信息
				List<User> sameAttentionList=commonService.selectObjects("common.select_same_attention", tmp);
				String sameAttentionInfo="";
				String alsoAttentionInfo="";
				for(int i=0;i<sameAttentionList.size();i++){
					if(i==0){
						if(sameAttentionList.get(i).getUser_name()!=null){
							sameAttentionInfo+=sameAttentionList.get(i).getUser_name();
						}
					}else{
						if(sameAttentionList.get(i).getUser_name()!=null){
							sameAttentionInfo+=(","+sameAttentionList.get(i).getUser_name());
						}
					}
				}
				//我关注的人也关注了他的
				List<User> alsoAttentionList=commonService.selectObjects("common.selectFromMeAlsoHe", tmp);
				for(int i=0;i<alsoAttentionList.size();i++){
					if(i==0){
						if(alsoAttentionList.get(i).getUser_name()!=null){
							alsoAttentionInfo+=alsoAttentionList.get(i).getUser_name();
						}
					}else{
						if(alsoAttentionList.get(i).getUser_name()!=null){
							alsoAttentionInfo+=(","+alsoAttentionList.get(i).getUser_name());
						}
					}
				}
				attention.setAlsoAttentionInfo(alsoAttentionInfo);
				attention.setSameAttentionInfo(sameAttentionInfo);
				//attention.setSameAttentionInfo(sameAttentionInfo+"等"+sameAttentionList.size()+"人");
				//attention.setAlsoAttentionInfo(alsoAttentionInfo+"等"+alsoAttentionList.size()+"人");
				if(attention.getUser_portrait()!=null){
					attention.setUser_portrait(Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, attention.getAttention_id())+"50x50_"+attention.getUser_portrait());
		    	}else{
		    		//设置默认的图像路径
		    		String path=request.getContextPath();
		    		attention.setUser_portrait(path+"/public/images/head_default3.png");
		    	}
			}
			int pageTotal= 0 ;
			if (totalCount % pageCount == 0) {
				pageTotal = totalCount / pageCount;
			} else {
				pageTotal = totalCount / (pageCount + 1);
			}
			String totaljson = "[{\"totalcount\":\""+totalCount+"\"}]";
			String totalPagejson = "[{\"totalPagecount\":\"" + pageTotal + "\"}]"; 
			JSONArray j = JSONArray.fromObject(list);
			dataString = "{\"totalproperty\":"+totaljson+",\"results\":" + j.toString() + ",\"totalPage\":" + totalPagejson + "}";
		}else{
			Map<Object,Object> params=new HashMap<Object,Object>();
			params.put("user_id", user_id);
			int totalCount  =  commonService.selectObject("common.selectAllFensiCount", params);
			params.put("pageIndex", (pageIndex-1)*pageCount);
			params.put("pageCount", pageCount);
			List<UserAttention> list=commonService.selectObjects("common.selectAllFensiByfalse", params);
			for(UserAttention attention:list){
				int attention_id=attention.getAttention_id();
				Map<Object,Object> mapTag = new HashMap<Object,Object>();
				mapTag.put("user_id", attention_id);
				List<Map<String,String>> mapTagList = commonService.selectObjects("user.getMyTags",mapTag);
			    String user_tag="";
			    for(int i=0;i<mapTagList.size();i++){
			    	if(i==0){
			    		user_tag+=mapTagList.get(i).get("tag_content");
			    	}else{
			    		user_tag+=(","+mapTagList.get(i).get("tag_content"));
			    	}
			    }
			    attention.setUser_tag(user_tag);
				Map<Object,Object> tmp=new HashMap<Object,Object>();
				tmp.put("user_id", user_id);
				tmp.put("user_attention_id", attention_id);
				//共同关注的人信息
				List<User> sameAttentionList=commonService.selectObjects("common.select_same_attention", tmp);
				String sameAttentionInfo="";
				String alsoAttentionInfo="";
				for(int i=0;i<sameAttentionList.size();i++){
					if(i==0){
						if(sameAttentionList.get(i).getUser_name()!=null){
							sameAttentionInfo+=sameAttentionList.get(i).getUser_name();
						}
					}else{
						if(sameAttentionList.get(i).getUser_name()!=null){
							sameAttentionInfo+=(","+sameAttentionList.get(i).getUser_name());
						}
					}
				}
				//我关注的人也关注了他的
				List<User> alsoAttentionList=commonService.selectObjects("common.selectFromMeAlsoHe", tmp);
				for(int i=0;i<alsoAttentionList.size();i++){
					if(i==0){
						if(alsoAttentionList.get(i).getUser_name()!=null){
							alsoAttentionInfo+=alsoAttentionList.get(i).getUser_name();
						}
					}else{
						if(alsoAttentionList.get(i).getUser_name()!=null){
							alsoAttentionInfo+=(","+alsoAttentionList.get(i).getUser_name());
						}
					}
				}
				attention.setAlsoAttentionInfo(alsoAttentionInfo);
				attention.setSameAttentionInfo(sameAttentionInfo);
				//attention.setSameAttentionInfo(sameAttentionInfo+"等"+sameAttentionList.size()+"人");
				//attention.setAlsoAttentionInfo(alsoAttentionInfo+"等"+alsoAttentionList.size()+"人");
				if(attention.getUser_portrait()!=null){
					attention.setUser_portrait(Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, attention.getAttention_id())+"50x50_"+attention.getUser_portrait());
		    	}else{
		    		//设置默认的图像路径
		    		String path=request.getContextPath();
		    		attention.setUser_portrait(path+"/public/images/head_default3.png");
		    	}
			}
			int pageTotal= 0 ;
			if (totalCount % pageCount == 0) {
				pageTotal = totalCount / pageCount;
			} else {
				pageTotal = totalCount / (pageCount + 1);
			}
			String totaljson = "[{\"totalcount\":\""+totalCount+"\"}]";
			String totalPagejson = "[{\"totalPagecount\":\"" + pageTotal + "\"}]"; 
			JSONArray j = JSONArray.fromObject(list);
			dataString = "{\"totalproperty\":"+totaljson+",\"results\":" + j.toString() + ",\"totalPage\":" + totalPagejson + "}";
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(dataString);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	
	/**
	 * 跳到用户的私信页面
	 */
	/**
	 * @param hs
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/gotoMyLetter.do", method = RequestMethod.GET)
	public String gotoMyLetter(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		User user = (User)hs.getAttribute("user");
		if(user!=null){
			int user_id=user.getUser_id();
			String address=user.getUser_address();
			if(address!=null && !"".equals(address)){
				String[] district=address.split("/");
				if(district.length==1) {
					request.setAttribute("address", district[0]);
				} else if(district.length==2) {
					request.setAttribute("address", district[0]+" "+district[1]);
				}
			}
			request.setAttribute("user_name", user.getUser_nick());
			CommonStat stat=commonService.selectObject("common.selectUserInfoByUserId", user_id);
			request.setAttribute("stat", stat);
			if(user.getUser_portrait()!=null){
				request.setAttribute("headPath", Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, user_id)+"50x50_"+user.getUser_portrait());
	    	}else{
	    		//设置默认的图像路径
	    		String path=request.getContextPath();
	    		request.setAttribute("headPath", path+"/public/images/head_default3.png");
	    	}
			Map<Object,Object> mapTag = new HashMap<Object,Object>();
			mapTag.put("user_id", user_id);
			List<Map<String,String>> mapTagList = commonService.selectObjects("user.getMyTags",mapTag);
			request.setAttribute("mapTagList", mapTagList);
			return "user/letter";
		}else{
			//跳到登陆页面
			return "redirect:/user/showUserLogin.do";
		}
	}
	
	/**
	 * 跳到我的的私信对话页面
	 */
	/**
	 * @param hs
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/myLetter.do", method = RequestMethod.GET)
	public String myLetter(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		User user = (User)hs.getAttribute("user");
		String u = request.getParameter("u");
		String c = request.getParameter("c");
		if(user!=null){
			int user_id=user.getUser_id();
			String address=user.getUser_address();
			if(address!=null && !"".equals(address)){
				String[] district=address.split("/");
				if(district.length==1) {
					request.setAttribute("address", district[0]);
				} else if(district.length==2) {
					request.setAttribute("address", district[0]+" "+district[1]);
				}
			}
			request.setAttribute("user_name", user.getUser_nick());
			CommonStat stat=commonService.selectObject("common.selectUserInfoByUserId", user_id);
			request.setAttribute("stat", stat);
			if(user.getUser_portrait()!=null){
				request.setAttribute("headPath", Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, user_id)+"50x50_"+user.getUser_portrait());
	    	}else{
	    		//设置默认的图像路径
	    		String path=request.getContextPath();
	    		request.setAttribute("headPath", path+"/public/images/head_default3.png");
	    	}
			Map<Object,Object> mapTag = new HashMap<Object,Object>();
			mapTag.put("user_id", user_id);
			List<Map<String,String>> mapTagList = commonService.selectObjects("user.getMyTags",mapTag);
			//获取对话用户信息
			User user_duihua = this.commonService.selectObject("user.getUserInfoByUserId", Integer.parseInt(u));
			request.setAttribute("user_duihua", user_duihua);
			//对话数量
			request.setAttribute("c", c);
			request.setAttribute("mapTagList", mapTagList);
			return "user/myletter";
		}else{
			//跳到登陆页面
			return "redirect:/user/showUserLogin.do";
		}
	}
	
	/**
	 * 添加用户私信
	 * @param hs
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/addPrivateLetter.do", method = RequestMethod.POST)
	public void addPrivateLetter(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		// 0:表示不成功  1:表示未登录 2:表示成功 4:表示在黑名单里
		//获取发送信息
		String info=request.getParameter("info");
		String send_title=request.getParameter("send_title");
		if(send_title==null){
			send_title="";
		}
		String to_user_id=request.getParameter("to_user_id");
		//String from_user_id=request.getParameter("from_user_id");
		String back="0";
		User user = (User)hs.getAttribute("user");
		if(user!=null){
			int user_id=user.getUser_id();
			Map<Object,Object> alreadyInBlack=new HashMap<Object,Object>();
			alreadyInBlack.put("user_id", user_id);
			alreadyInBlack.put("black_user_id", to_user_id);
			BlackList bl=commonService.selectObject("common.isOrNotAlreayInBlaclList", alreadyInBlack);
			if(bl!=null){
				back="4";
			}else{
				UserPrivateLetter letter=new UserPrivateLetter();
				letter.setSend_info(info);
				letter.setFrom_user_id(user_id);
				letter.setTo_user_id(Integer.parseInt(to_user_id));
				letter.setSend_title(send_title);
				String group_user = "";
				if(Integer.parseInt(to_user_id)>user_id){
					group_user = user_id+","+to_user_id;
				}else{
					group_user = to_user_id+","+user_id;
				}
				letter.setGroup_user(group_user);
				int result=commonService.insertObject("common.insertPrivateLetter", letter);
				if(result>0){
					back="2";
					Map<Object,Object> map=new HashMap<Object,Object>();
					map.put("user_id", user_id);
					CommonStat commonStat=commonService.selectObject("common.select_commonStat", map);
					commonStat.setUser_sixing_count(commonStat.getUser_sixing_count()+1);
					commonService.updateObject("common.updateUserSixingCount", commonStat);
				}
			}
		}else{
			back="1";
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(back);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错");
		}
	}
	/**
	 * 发送用户删除发送私信
	 * @param hs
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/delUserPrivateLetterFromSendUser.do", method = RequestMethod.GET)
	public void delUserPrivateLetterFromSendUser(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		// 0:表示不成功  1:表示未登录 2:表示成功
		String back="0";
		String user_private_id=request.getParameter("usr_private_id");
		User user = (User)hs.getAttribute("user");
		if(user!=null){
			int user_id=user.getUser_id();
			boolean result=commonService.updateObject("common.delLetterFromSendUser", Integer.parseInt(user_private_id));
			if(result){
				back="2";
				Map<Object,Object> map=new HashMap<Object,Object>();
				map.put("user_id", user_id);
				CommonStat commonStat=commonService.selectObject("common.select_commonStat", map);
				if(commonStat.getUser_sixing_count()>0){
					commonStat.setUser_sixing_count(commonStat.getUser_sixing_count()-1);
					commonService.updateObject("common.updateUserSixingCount", commonStat);
				}
			}
		}else{
			back="1";
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(back);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错");
		}
	}
	/**
	 * 收到用户删除用户私信
	 * @param hs
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/delUserPrivateLetterToUser.do", method = RequestMethod.GET)
	public void delUserPrivateLetterToUser(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		// 0:表示不成功  1:表示未登录 2:表示成功
		String back="0";
		String user_private_id=request.getParameter("usr_private_id");
		User user = (User)hs.getAttribute("user");
		if(user!=null){
			boolean result=commonService.updateObject("common.delLetterFromGetUser", Integer.parseInt(user_private_id));
			if(result){
				back="2";
			}
		}else{
			back="1";
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(back);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错");
		}
	}
	
	/**
	 * 查询用户收到的私信对话列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getUserPrivateLetterGroup.do", method = RequestMethod.GET)
	public void getUserPrivateLetterGroup(HttpServletRequest request,HttpServletResponse response){
		String user_id=request.getParameter("user_id");
		String u = request.getParameter("u");
		String group_user = "";
		if(Integer.parseInt(user_id)>Integer.parseInt(u)){
			group_user = u+","+user_id;
		}else{
			group_user = user_id+","+u;
		}
		int pageIndex = 1 ;
		int pageCount = 10 ;
		try{
			pageIndex = (request.getParameter("pageIndex")==null||"".equals(request.getParameter("pageIndex")))?1:Integer.parseInt(request.getParameter("pageIndex"));
		}catch(Exception e){
			pageIndex = 1 ;
		} 
		Map<Object,Object> params=new HashMap<Object,Object>();
		params.put("user_id", user_id);
		params.put("group_user", group_user);
		int totalCount  =  commonService.selectObject("common.selectLetterOpenByGroupCount", params);
		params.put("pageIndex", (pageIndex-1)*pageCount);
		params.put("pageCount", pageCount);
		List<UserPrivateLetter> list=commonService.selectObjects("common.selectLetterOpenByGroup", params);
		if(list.size()>0){
			for(UserPrivateLetter upl:list){
				upl.setUser_portrait(Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, upl.getFrom_user_id())+"50x50_"+upl.getUser_portrait());
				
			}
		}
		
		int pageTotal= 0 ;
		if (totalCount % pageCount == 0) {
			pageTotal = totalCount / pageCount;
		} else {
			pageTotal = totalCount / (pageCount + 1);
		}
		String totaljson = "[{\"totalcount\":\""+totalCount+"\"}]";
		String totalPagejson = "[{\"totalPagecount\":\"" + pageTotal + "\"}]"; 
		JSONArray j = JSONArray.fromObject(list);
		String dataString = "{\"totalproperty\":"+totaljson+",\"results\":" + j.toString() + ",\"totalPage\":" + totalPagejson + "}";
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(dataString);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	/**
	 * 查询用户收到的私信列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getUserGetPrivateLetterList.do", method = RequestMethod.GET)
	public void getUserGetPrivateLetterList(HttpServletRequest request,HttpServletResponse response){
		String user_id=request.getParameter("user_id");
		int pageIndex = 1 ;
		int pageCount = 10 ;
		try{
			pageIndex = (request.getParameter("pageIndex")==null||"".equals(request.getParameter("pageIndex")))?1:Integer.parseInt(request.getParameter("pageIndex"));
		}catch(Exception e){
			pageIndex = 1 ;
		} 
		Map<Object,Object> params=new HashMap<Object,Object>();
		params.put("user_id", user_id);
		int totalCount  =  commonService.selectObject("common.selectGetPrivateLetterCount", params);
		params.put("pageIndex", (pageIndex-1)*pageCount);
		params.put("pageCount", pageCount);
		List<UserPrivateLetter> list=commonService.selectObjects("common.selectGetPrivateLetter", params);
		if(list.size()>0){
			for(UserPrivateLetter upl:list){
				upl.setUser_portrait(Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, upl.getFrom_user_id())+"50x50_"+upl.getUser_portrait());
				this.commonService.updateObject("common.updateLetter",upl);
			}
		}
		
		int pageTotal= 0 ;
		if (totalCount % pageCount == 0) {
			pageTotal = totalCount / pageCount;
		} else {
			pageTotal = totalCount / (pageCount + 1);
		}
		String totaljson = "[{\"totalcount\":\""+totalCount+"\"}]";
		String totalPagejson = "[{\"totalPagecount\":\"" + pageTotal + "\"}]"; 
		JSONArray j = JSONArray.fromObject(list);
		String dataString = "{\"totalproperty\":"+totaljson+",\"results\":" + j.toString() + ",\"totalPage\":" + totalPagejson + "}";
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(dataString);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	
	/**
	 * 查询用户发送的私信列表
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getUserSendPrivateLetterList.do", method = RequestMethod.GET)
	public void getUserSendPrivateLetterList(HttpServletRequest request,HttpServletResponse response){
		String user_id=request.getParameter("user_id");
		int pageIndex = 1 ;
		int pageCount = 10 ;
		try{
			pageIndex = (request.getParameter("pageIndex")==null||"".equals(request.getParameter("pageIndex")))?1:Integer.parseInt(request.getParameter("pageIndex"));
		}catch(Exception e){
			pageIndex = 1 ;
		} 
		try{
			pageCount = (request.getParameter("pageCount")==null||"".equals(request.getParameter("pageCount")))?10:Integer.parseInt(request.getParameter("pageCount"));
		}catch(Exception e){
			pageIndex = 10 ;
		}
		Map<Object,Object> params=new HashMap<Object,Object>();
		params.put("user_id", user_id);
		int totalCount  =  commonService.selectObject("common.selectSendPrivateLetterCount", params);
		params.put("pageIndex", (pageIndex-1)*pageCount);
		params.put("pageCount", pageCount);
		List<UserPrivateLetter> list=commonService.selectObjects("common.selectSendPrivateLetter", params);
		int pageTotal= 0 ;
		if (totalCount % pageCount == 0) {
			pageTotal = totalCount / pageCount;
		} else {
			pageTotal = totalCount / (pageCount + 1);
		}
		String totaljson = "[{\"totalcount\":\""+totalCount+"\"}]";
		String totalPagejson = "[{\"totalPagecount\":\"" + pageTotal + "\"}]"; 
		JSONArray j = JSONArray.fromObject(list);
		String dataString = "{\"totalproperty\":"+totaljson+",\"results\":" + j.toString() + ",\"totalPage\":" + totalPagejson + "}";
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(dataString);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	
	/**
	 * 成为某人的粉丝
	 */
	@RequestMapping(value = "/addFensi.do", method = RequestMethod.GET)
	public void addFensi(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		// 0:表示不成功  1:表示未登录 2:表示成功 3:已经是某人的粉丝啦
		String back="0"; 
		String fensi_id=request.getParameter("fensi_id");
		User user = (User)hs.getAttribute("user");
		if(user!=null){
			int user_id=user.getUser_id();
			UserVermicelli userVermicelli=new UserVermicelli();
			userVermicelli.setUser_id(user_id);
			userVermicelli.setVermicelli_id(Integer.parseInt(fensi_id));
			Map<Object,Object> verMap=new HashMap<Object,Object>();
			verMap.put("user_id", Integer.parseInt(fensi_id));
			verMap.put("vermicelli_id", user_id);
			UserVermicelli alreayFensi=commonService.selectObject("common.isAreadyfensi", verMap);
			if(alreayFensi!=null){
				back="3";
			}else{
				int result=commonService.insertObject("common.insert_fensi", userVermicelli);
				if(result>0){
					back="2";
					Map<Object,Object> map=new HashMap<Object,Object>();
					map.put("user_id", Integer.parseInt(fensi_id));
					CommonStat commonStat=commonService.selectObject("common.select_commonStat", map);
					commonStat.setUser_fensi_count(commonStat.getUser_fensi_count()+1);
					commonService.updateObject("common.updateUserFenSiCount", commonStat);
				}
			}
		}else{
			back="1";
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(back);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	/**
	 * 获取某个用户所有的粉丝
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getFensiList.do", method = RequestMethod.GET)
	public void getFensiList(HttpServletRequest request,HttpServletResponse response){
		String user_id=request.getParameter("user_id");
		int pageIndex = 1 ;
		int pageCount = 10 ;
		try{
			pageIndex = (request.getParameter("pageIndex")==null||"".equals(request.getParameter("pageIndex")))?1:Integer.parseInt(request.getParameter("pageIndex"));
		}catch(Exception e){
			pageIndex = 1 ;
		} 
		try{
			pageCount = (request.getParameter("pageCount")==null||"".equals(request.getParameter("pageCount")))?10:Integer.parseInt(request.getParameter("pageCount"));
		}catch(Exception e){
			pageIndex = 10 ;
		}
		Map<Object,Object> params=new HashMap<Object,Object>();
		params.put("user_id", user_id);
		int totalCount  =  commonService.selectObject("common.selectAllFensiCount", params);
		params.put("pageIndex", (pageIndex-1)*pageCount);
		params.put("pageCount", pageCount);
		List<UserPrivateLetter> list=commonService.selectObjects("common.selectAllFensi", params);
		int pageTotal= 0 ;
		if (totalCount % pageCount == 0) {
			pageTotal = totalCount / pageCount;
		} else {
			pageTotal = totalCount / (pageCount + 1);
		}
		String totaljson = "[{\"totalcount\":\""+totalCount+"\"}]";
		String totalPagejson = "[{\"totalPagecount\":\"" + pageTotal + "\"}]"; 
		JSONArray j = JSONArray.fromObject(list);
		String dataString = "{\"totalproperty\":"+totaljson+",\"results\":" + j.toString() + ",\"totalPage\":" + totalPagejson + "}";
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(dataString);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	
	/**
	 * 将某个用户加到自己的黑名单里
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/addBlackList.do", method = RequestMethod.GET)
	public void addBlackList(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		//0:表示不成功  1:表示未登录 2:表示成功 4:已经在你的黑名单里
		User user = (User)hs.getAttribute("user");
		String black_user_id=request.getParameter("black_user_id");
		String back="0";
		if(user!=null){
			int user_id=user.getUser_id();
			Map<Object,Object> alreadyInBlack=new HashMap<Object,Object>();
			alreadyInBlack.put("user_id", user_id);
			alreadyInBlack.put("black_user_id", black_user_id);
			BlackList bl=commonService.selectObject("common.isOrNotAlreayInBlaclList", alreadyInBlack);
			if(bl!=null){
				back="4";
			}else{
				User blackUser=commonService.selectObject("user.getUserInfoByUserId", Integer.parseInt(black_user_id));
				Map<Object,Object> mapBlack=new HashMap<Object,Object>();
				mapBlack.put("user_id", user_id);
				mapBlack.put("black_user_id", Integer.parseInt(black_user_id));
				mapBlack.put("black_user_name", blackUser.getUser_name());
				int result=commonService.insertObject("user.addToBlackList", mapBlack);
				if(result>0){
					back="2";
					Map<Object,Object> attentionMap=new HashMap<Object,Object>();
					attentionMap.put("user_id", user_id);
					attentionMap.put("attention_id", black_user_id);
					UserAttention attention=commonService.selectObject("common.AttentionOrNotThisUser", attentionMap);
					if(attention!=null){
						boolean re=commonService.updateObject("common.cancelAttention", attentionMap);
						if(re){
							Map<Object,Object> map=new HashMap<Object,Object>();
							map.put("user_id", user_id);
							CommonStat commonStat=commonService.selectObject("common.select_commonStat", map);
							if(commonStat.getUser_attention_count()>0){
								commonStat.setUser_attention_count(commonStat.getUser_attention_count()-1);
								commonService.updateObject("common.updateUserAttention", commonStat);
							}
						}
					}
				}
			}
		}else{
			back="1";
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(back);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	/**
	 * 将某个用户从自己的黑名单里移除
	 * @param hs
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/delFromBlackList.do", method = RequestMethod.GET)
	public void delFromBlackList(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		//0:表示不成功  1:表示未登录 2:表示成功
		User user = (User)hs.getAttribute("user");
		String black_user_id=request.getParameter("black_user_id");
		String back="0";
		if(user!=null){
			int user_id=user.getUser_id();
			Map<Object,Object> alreadyInBlack=new HashMap<Object,Object>();
			alreadyInBlack.put("user_id", user_id);
			alreadyInBlack.put("black_user_id", black_user_id);
		    int result=commonService.deleteObject("user.removeUserFromBlackList", alreadyInBlack);
		    if(result>0){
		    	back="2";
		    }
		}else{
			back="1";
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(back);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	/**
	 * 举报用户的评论
	 * @param hs
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/jubao.do", method = RequestMethod.POST)
	public void jubao(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		//0:表示不成功  1:表示未登录 2:表示成功
		String back="0";
		String type=request.getParameter("type"); //表示举报的类型
		String user_id=request.getParameter("user_id"); //被举报的用户ID
		String jubao_content=request.getParameter("jubao_content"); //举报理由
		String id=request.getParameter("id");
		User user = (User)hs.getAttribute("user");
		if(user!=null){
			Map<Object,Object> map=new HashMap<Object,Object>();
			map.put("type", type);
			map.put("content_id", id);
			map.put("user_id",user.getUser_id());
			map.put("jubao_user_id", user_id);
			map.put("jubao_content", jubao_content);
			int result=commonService.insertObject("common.insert_jubao", map);
			if(result>0){
				back="2";
			}
		}else{
			back="1";
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(back);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}

	/**
	 * 获取推荐信息
	 * @param hs
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/tuijian.do", method = RequestMethod.GET)
	public void tuijian(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		//0:表示不成功  1:表示未登录 2:表示成功
		String id=request.getParameter("id");
		String back = "0";
		User user = (User)hs.getAttribute("user");
		if(user!=null){
			Map<Object,Object> params=new HashMap<Object,Object>();
			params.put("id", id);
			List<UserRecommend> recommend2List=commonService.selectObjects("common.selectRecommendByCommonIdTwo", params);
			for(UserRecommend recommend:recommend2List){
				if(recommend.getUser_portrait()!=null){
					recommend.setUser_portrait(Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, recommend.getUser_id())+"30x30_"+recommend.getUser_portrait());
		    	}else{
		    		//设置默认的图像路径
		    		String path=request.getContextPath();
		    		recommend.setUser_portrait(path+"/public/images/head_default3.png");
		    	}
			}
			int totalCount=commonService.selectObject("common.selectRecommendByCommonIdCount", params);
			JSONArray j = JSONArray.fromObject(recommend2List);
			String dataString = "{\"flag\":\"1\",\"totalCount\":"+totalCount+",\"results\":" + j.toString() + "}";
			try {
				request.setCharacterEncoding("utf-8");
				response.setContentType("text/json;charset=UTF-8");
				PrintWriter out;
				out = response.getWriter();
				out.write(dataString);
				out.flush();
				out.close();
			} catch (IOException e) {
				System.out.print("获取json相关数据报错"+e.getMessage());
			}
		}else{   //没有登录
			try {
				String regMsg = "{\"flag\":\"0\"}";
				request.setCharacterEncoding("utf-8");
				response.setContentType("text/json;charset=UTF-8");
				PrintWriter out;
				out = response.getWriter();
				out.write(regMsg);
				out.flush();
				out.close();
			} catch (IOException e) {
				System.out.print("获取json相关数据报错"+e.getMessage());
			}
		
		}
	}
	
	/**
	 * 添加影评评论
	 */
	

	@RequestMapping(value = "/addRemarkComment.do", method = RequestMethod.POST)
	public void addRemarkComment(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		// 0:表示不成功  1:表示未登录 2:表示成功 
		String back="0";
		String remak_id=request.getParameter("remak_id");
		String content=request.getParameter("content");
		String reply_user_id=request.getParameter("reply_user_id");
		String re_id=request.getParameter("re_id");
		User user = (User)hs.getAttribute("user");
		if(user!=null) {
			if(reply_user_id==null){
				reply_user_id="0";
			}
			RemarkReply rr = new RemarkReply();
			rr.setRemark_id(Integer.parseInt(remak_id));
			rr.setUser_id(user.getUser_id());
			rr.setReply_user_id(Integer.parseInt(reply_user_id));
			rr.setContent(content);
			rr.setReply_count(0);
			int result=commonService.insertObject("remark.insertRemarkComment", rr);
			if(result>0){
				if(!"0".equals(re_id)){
					RemarkReply rr2 = new RemarkReply();
					rr2.setId(Integer.parseInt(re_id));
					this.commonService.updateObject("remark.updateCount", rr2);
				}
				back="2";
				
			}
		}else{
			back="1";
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
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取影评评论
	 */

	@RequestMapping(value = "/getRemarkComments.do", method = RequestMethod.GET)
	public void getRemarkComments(HttpServletRequest request,HttpServletResponse response){
		String remark_id=request.getParameter("remark_id");
		int pageIndex = 1 ;
		int pageCount = 1 ;
		try{
			pageIndex = (request.getParameter("pageIndex")==null||"".equals(request.getParameter("pageIndex")))?1:Integer.parseInt(request.getParameter("pageIndex"));
		}catch(Exception e){
			pageIndex = 1 ;
		} 
		try{
			pageCount = (request.getParameter("pageCount")==null||"".equals(request.getParameter("pageCount")))?10:Integer.parseInt(request.getParameter("pageCount"));
		}catch(Exception e){
			pageCount = 10 ;
		} 
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("remark_id", Integer.parseInt(remark_id));
		params.put("pageIndex", (pageIndex-1)*pageCount);
		params.put("pageCount", pageCount);
		String totalCount  =  commonService.selectObject("remark.getRemarkCommentsCount", params);
	    List<RemarkReply>	rrList=commonService.selectObjects("remark.getRemarkComments", params);
	    for(RemarkReply rr:rrList){
	    
	    	if(rr.getUser_portrait()!=null){
	    		rr.setUser_portrait(Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, rr.getUser_id())+"50x50_"+rr.getUser_portrait());
	    	}else{
	    		//设置默认的图像路径
	    		String path=request.getContextPath();
	    		rr.setUser_portrait(path+"/public/images/head_default3.png");
	    	}
	    }
	    int pageTotal= 0 ;
		if (Integer.parseInt(totalCount) % pageCount == 0) {
			pageTotal = Integer.parseInt(totalCount) / pageCount;
		} else {
			pageTotal = Integer.parseInt(totalCount) / (pageCount + 1);
		}
		String totaljson = "[{\"totalcount\":\""+totalCount+"\"}]";
		String totalPagejson = "[{\"totalPagecount\":\"" + pageTotal + "\"}]"; 
		JSONArray j = JSONArray.fromObject(rrList);
		String dataString = "{\"totalproperty\":"+totaljson+",\"results\":" + j.toString() + ",\"totalPage\":" + totalPagejson + "}";
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(dataString);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	/**
	 * 票友中心
	 * @param request
	 * @param response
	 * @param hs
	 * @return
	 */
	@RequestMapping(value = "/myCenter.do", method = RequestMethod.GET)
	public  String  myCenter(HttpServletRequest request,HttpServletResponse response,HttpSession hs){
		User user = (User)hs.getAttribute("user");
		if(user!=null){
			int user_id=user.getUser_id();
			String address=user.getUser_address();
			if(address!=null && !"".equals(address)){
				String[] district=address.split("/");
				if(district.length==1) {
					request.setAttribute("address", district[0]);
				} else if(district.length==2) {
					request.setAttribute("address", district[0]+" "+district[1]);
				}
			}
			request.setAttribute("user_name", user.getUser_nick());
			CommonStat stat=commonService.selectObject("common.selectUserInfoByUserId", user_id);
			request.setAttribute("stat", stat);
			if(user.getUser_portrait()!=null){
				request.setAttribute("headPath", Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, user_id)+"50x50_"+user.getUser_portrait());
	    	}else{
	    		//设置默认的图像路径
	    		String path=request.getContextPath();
	    		request.setAttribute("headPath", path+"/public/images/head_default3.png");
	    	}
			Map<Object,Object> mapTag = new HashMap<Object,Object>();
			mapTag.put("user_id", user_id);
			List<Map<String,String>> mapTagList = commonService.selectObjects("user.getMyTags",mapTag);
			request.setAttribute("mapTagList", mapTagList);
			return "user/pyzx";
		}else{
			//跳到登陆页面
			return "redirect:/user/showUserLogin.do";
		}
	}
	/**
	 * 或许你喜欢
	 * @param request
	 * @param response
	 * @param hs
	 */
	@RequestMapping(value = "/huoxu.do", method = RequestMethod.GET)
	public  void  huoxu(HttpServletRequest request,HttpServletResponse response,HttpSession hs){
		String city_id=(request.getParameter("city_id")==null||"".equals(request.getParameter("city_id")))?"1":request.getParameter("city_id");
		
		User user = (User)hs.getAttribute("user");
		if(user!=null){
			int user_id=user.getUser_id();
			Map<Object,Object> params = new HashMap<Object,Object>();
			params.put("user_id", user_id);
			List<UserCollection> listc = this.commonService.selectObjects("common.selectCollectionCenterByUserId", params);
			if(listc.size()>0){
				String ids = "";
				for(UserCollection uc:listc){
					ids = uc.getShow_type()+",";
				}
				ids = ids.substring(0, ids.lastIndexOf(","));
				params.put("coll_ids", ids);
				params.put("city_id", city_id);
				int pageIndex = 1 ;
				int pageCount = 4 ;
				try{
					pageIndex = (request.getParameter("pageIndex")==null||"".equals(request.getParameter("pageIndex")))?1:Integer.parseInt(request.getParameter("pageIndex"));
				}catch(Exception e){
					pageIndex = 1 ;
				} 
				params.put("pageIndex", (pageIndex-1)*pageCount);
				params.put("pageCount", pageCount);
				
				String totalCount  =  commonService.selectObject("commonInfo.getHuoxuCount", params);
			    List<CommonInfo>	commonList=commonService.selectObjects("commonInfo.getHuoxu", params);
			    if(commonList.size()>0){
			    	Map<Object,Object> timeMap = this.commonInfoService.getCommonInfoTimeMap();
					Map<Object,CommonInfo> priceMap = this.commonInfoService.getCommonInfoPriceMap();
			    	for(int i=0,length=commonList.size();i<length;i++){
						CommonInfo commonInfo = commonList.get(i);
//						//String introduction = commonInfo.getIntroduction().replaceAll("\\s*", "");
//						if(introduction!=null&&!"".equals(introduction)){
//							introduction = Jsoup.parse(introduction).text().trim();
//							if(introduction.length()>350){
//								introduction=introduction.substring(0,350);
//							}
//							commonList.get(i).setIntroduction(introduction);
//						}
						
//						if(commonInfo.getStatus()==0){
//							commonInfo.setShow_status("预售中");
//						}else if(commonInfo.getStatus()==2){
//							commonInfo.setShow_status("已过期");
//						}else{
//							commonInfo.setShow_status("热售中");
//						}
						
						String time = timeMap.get(commonInfo.getId()+"")+"";
						if(time!=null&&!"null".equals(time)&&!time.contains("全年")){
							Calendar calendar = Calendar.getInstance();
							try {
								calendar.setTime(YYYY_MM_DD_HH_MM.parse(time));
								int year = calendar.get(Calendar.YEAR);
								int month=calendar.get(Calendar.MONTH)+1;    
								int day =calendar.get(Calendar.DAY_OF_MONTH);  
								int hour =calendar.get(Calendar.HOUR);  
								int minute =calendar.get(Calendar.MINUTE);
								int week = calendar.get(Calendar.DAY_OF_WEEK);
								int AM_PM = calendar.get(Calendar.AM_PM);
								if(AM_PM==0){
									time = "早";
								}else{
									time = "晚";
								}
								if(hour<10){
									time=time+"0"+hour+":";
								}else{
									time=time+hour+":";
								}
								if(minute<10){
									time=time+"0"+minute;
								}else{
									time=time+minute;
								}
								commonList.get(i).setYear(year+"");
								commonList.get(i).setDay(day+"");
								if(month<10){
									commonList.get(i).setMonth("0"+month);
								}else{
									commonList.get(i).setMonth(month+"");
								}
								commonList.get(i).setWeek(Const.week[week-1]);
								commonList.get(i).setShow_time(time);
							} catch (ParseException e) {
								e.printStackTrace();
							}
						}else{
							commonList.get(i).setShow_time(time);
						}
						
						CommonInfo commonPrice = priceMap.get(commonInfo.getId()+"");
						if(commonPrice!=null){
							commonList.get(i).setPrice(commonPrice.getPrice());
							commonList.get(i).setDiscount(commonPrice.getDiscount());
							commonList.get(i).setMin_price(commonPrice.getMin_price());
						}
					}
			    }
			    
			    
			    int pageTotal= 0 ;
				if (Integer.parseInt(totalCount) % pageCount == 0) {
					pageTotal = Integer.parseInt(totalCount) / pageCount;
				} else {
					pageTotal = Integer.parseInt(totalCount) / (pageCount + 1);
				}
				String totaljson = "[{\"totalcount\":\""+totalCount+"\"}]";
				String totalPagejson = "[{\"totalPagecount\":\"" + pageTotal + "\"}]"; 
				JSONArray j = JSONArray.fromObject(commonList);
				String dataString = "{\"totalproperty\":"+totaljson+",\"results\":" + j.toString() + ",\"totalPage\":" + totalPagejson + "}";
				try {
					request.setCharacterEncoding("utf-8");
					response.setContentType("text/json;charset=UTF-8");
					PrintWriter out;
					out = response.getWriter();
					out.write(dataString);
					out.flush();
					out.close();
				} catch (IOException e) {
					System.out.print("获取json相关数据报错"+e.getMessage());
				}
			}
		}else{
			//跳到登陆页面
			return;
		}
	}
	
	/**
	 * 票友中心票友推荐
	 * @param request
	 * @param response
	 * @param hs
	 */
	@RequestMapping(value = "/centerTuijian.do", method = RequestMethod.GET)
	public  void  centerTuijian(HttpServletRequest request,HttpServletResponse response,HttpSession hs){
		Map<Object,Object> params = new HashMap<Object,Object>();
		String city_id=(request.getParameter("city_id")==null||"".equals(request.getParameter("city_id")))?"1":request.getParameter("city_id");
		params.put("city_id", city_id);
		
		int pageIndex = 1 ;
		int pageCount = 4 ;
		try{
			pageIndex = (request.getParameter("pageIndex")==null||"".equals(request.getParameter("pageIndex")))?1:Integer.parseInt(request.getParameter("pageIndex"));
		}catch(Exception e){
			pageIndex = 1 ;
		} 
		params.put("pageIndex", (pageIndex-1)*pageCount);
		params.put("pageCount", pageCount);
		
		List<CommonInfo> commonList = this.commonService.selectObjects("commonInfo.getDouzaikan", params);
		String totalCount = this.commonService.selectObject("commonInfo.getDouzaikanCount", params);
		
		if(commonList.size()>0){
	    	Map<Object,Object> timeMap = this.commonInfoService.getCommonInfoTimeMap();
			Map<Object,CommonInfo> priceMap = this.commonInfoService.getCommonInfoPriceMap();
	    	for(int i=0,length=commonList.size();i<length;i++){
				CommonInfo commonInfo = commonList.get(i);
//				//String introduction = commonInfo.getIntroduction().replaceAll("\\s*", "");
//				if(introduction!=null&&!"".equals(introduction)){
//					introduction = Jsoup.parse(introduction).text().trim();
//					if(introduction.length()>350){
//						introduction=introduction.substring(0,350);
//					}
//					commonList.get(i).setIntroduction(introduction);
//				}
				
//				if(commonInfo.getStatus()==0){
//					commonInfo.setShow_status("预售中");
//				}else if(commonInfo.getStatus()==2){
//					commonInfo.setShow_status("已过期");
//				}else{
//					commonInfo.setShow_status("热售中");
//				}
				
				String time = timeMap.get(commonInfo.getId()+"")+"";
				if(time!=null&&!"null".equals(time)&&!time.contains("全年")){
					Calendar calendar = Calendar.getInstance();
					try {
						calendar.setTime(YYYY_MM_DD_HH_MM.parse(time));
						int year = calendar.get(Calendar.YEAR);
						int month=calendar.get(Calendar.MONTH)+1;    
						int day =calendar.get(Calendar.DAY_OF_MONTH);  
						int hour =calendar.get(Calendar.HOUR);  
						int minute =calendar.get(Calendar.MINUTE);
						int week = calendar.get(Calendar.DAY_OF_WEEK);
						int AM_PM = calendar.get(Calendar.AM_PM);
						if(AM_PM==0){
							time = "早";
						}else{
							time = "晚";
						}
						if(hour<10){
							time=time+"0"+hour+":";
						}else{
							time=time+hour+":";
						}
						if(minute<10){
							time=time+"0"+minute;
						}else{
							time=time+minute;
						}
						commonList.get(i).setYear(year+"");
						commonList.get(i).setDay(day+"");
						if(month<10){
							commonList.get(i).setMonth("0"+month);
						}else{
							commonList.get(i).setMonth(month+"");
						}
						commonList.get(i).setWeek(Const.week[week-1]);
						commonList.get(i).setShow_time(time);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}else{
					commonList.get(i).setShow_time(time);
				}
				
				CommonInfo commonPrice = priceMap.get(commonInfo.getId()+"");
				if(commonPrice!=null){
					commonList.get(i).setPrice(commonPrice.getPrice());
					commonList.get(i).setDiscount(commonPrice.getDiscount());
					commonList.get(i).setMin_price(commonPrice.getMin_price());
				}
			}
	    }
		
		 int pageTotal= 0 ;
			if (Integer.parseInt(totalCount) % pageCount == 0) {
				pageTotal = Integer.parseInt(totalCount) / pageCount;
			} else {
				pageTotal = Integer.parseInt(totalCount) / (pageCount + 1);
			}
			String totaljson = "[{\"totalcount\":\""+totalCount+"\"}]";
			String totalPagejson = "[{\"totalPagecount\":\"" + pageTotal + "\"}]"; 
			JSONArray j = JSONArray.fromObject(commonList);
			String dataString = "{\"totalproperty\":"+totaljson+",\"results\":" + j.toString() + ",\"totalPage\":" + totalPagejson + "}";
			try {
				request.setCharacterEncoding("utf-8");
				response.setContentType("text/json;charset=UTF-8");
				PrintWriter out;
				out = response.getWriter();
				out.write(dataString);
				out.flush();
				out.close();
			} catch (IOException e) {
				System.out.print("获取json相关数据报错"+e.getMessage());
			}
		
	}
	
	/**
	 * 统计私信数量
	 * @param request
	 * @param response
	 * @param hs
	 */
	@RequestMapping(value = "/counter.do", method = RequestMethod.POST)
	public  void counter(HttpServletRequest request,HttpServletResponse response,HttpSession hs){
		User user = (User)hs.getAttribute("user");
		int user_id=0;
		if(user==null){
			return;
		}else{
			user_id=user.getUser_id();
			Map<Object,Object> params=new HashMap<Object,Object>();
			params.put("user_id", user_id);
			int totalCount  =  commonService.selectObject("common.selectGetPrivateLetterCount2", params);
		
		
			String dataString = "{\"results\":" + totalCount + "}";
			try {
				request.setCharacterEncoding("utf-8");
				response.setContentType("text/json;charset=UTF-8");
				PrintWriter out;
				out = response.getWriter();
				out.write(dataString);
				out.flush();
				out.close();
			} catch (IOException e) {
				System.out.print("获取json相关数据报错"+e.getMessage());
			}
		}
	}
	
	/**
	 * 查询用户的浏览历史
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/getUserHistoryList.do", method = RequestMethod.GET)
	public void getUserHistoryList(HttpServletRequest request,HttpServletResponse response,HttpSession hs){
		//0:按照演出日期排序   1:照收藏日期排序
		int pageIndex = 1 ;
		int pageCount = 5 ;
		try{
			pageIndex = (request.getParameter("pageIndex")==null||"".equals(request.getParameter("pageIndex")))?1:Integer.parseInt(request.getParameter("pageIndex"));
		}catch(Exception e){
			pageIndex = 1 ;
		} 
		try{
			pageCount = (request.getParameter("pageCount")==null||"".equals(request.getParameter("pageCount")))?5:Integer.parseInt(request.getParameter("pageCount"));
		}catch(Exception e){
			pageIndex = 5 ;
		}
		String type = (request.getParameter("type")==null||"".equals(request.getParameter("type")))?"":request.getParameter("type");
		String showName = (request.getParameter("showName")==null||"".equals(request.getParameter("showName")))?"":request.getParameter("showName");
		try {
			showName = java.net.URLDecoder.decode(showName, "utf-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		User user = (User)hs.getAttribute("user");
		Map<Object,Object> params=new HashMap<Object,Object>();
		params.put("user_id", user.getUser_id());
		params.put("pageIndex", (pageIndex-1)*pageCount);
		params.put("type", type);
		params.put("showName", showName);
		params.put("pageCount", pageCount);
		int totalCount  =  Integer.parseInt((String)commonService.selectObject("common.getHistoryCount", params));
		List<CommonInfo> commonList=commonService.selectObjects("common.getHistoryList", params);
			Map<Object,Object> timeMap = commonInfoService.getCommonInfoTimeMap();
			Map<Object,CommonInfo> priceMap = commonInfoService.getCommonInfoPriceMap();
			for(int i=0,length=commonList.size();i<length;i++){
				CommonInfo commonInfo = commonList.get(i);
				String introduction = commonInfo.getIntroduction().replaceAll("\\s*", "");
				if(introduction!=null&&!"".equals(introduction)){
					introduction = Jsoup.parse(introduction).text().trim();
					if(introduction.length()>350){
						introduction=introduction.substring(0,350);
					}
					commonList.get(i).setIntroduction(introduction);
				}
				
				if(commonInfo.getStatus()==0){
					commonInfo.setShow_status("预售中");
				}else if(commonInfo.getStatus()==2){
					commonInfo.setShow_status("已过期");
				}else{
					commonInfo.setShow_status("热售中");
				}
				
				String time = timeMap.get(commonInfo.getId()+"")+"";
				if(time!=null&&!"null".equals(time)&&!time.contains("全年")){
					Calendar calendar = Calendar.getInstance();
					try {
						calendar.setTime(YYYY_MM_DD_HH_MM.parse(time));
						int year = calendar.get(Calendar.YEAR);
						int month=calendar.get(Calendar.MONTH)+1;    
						int day =calendar.get(Calendar.DAY_OF_MONTH);  
						int hour =calendar.get(Calendar.HOUR);  
						int minute =calendar.get(Calendar.MINUTE);
						int week = calendar.get(Calendar.DAY_OF_WEEK);
						int AM_PM = calendar.get(Calendar.AM_PM);
						if(AM_PM==0){
							time = "早";
						}else{
							time = "晚";
						}
						if(hour<10){
							time=time+"0"+hour+":";
						}else{
							time=time+hour+":";
						}
						if(minute<10){
							time=time+"0"+minute;
						}else{
							time=time+minute;
						}
						commonList.get(i).setYear(year+"");
						commonList.get(i).setDay(day+"");
						if(month<10){
							commonList.get(i).setMonth("0"+month);
						}else{
							commonList.get(i).setMonth(month+"");
						}
						commonList.get(i).setWeek(Const.week[week-1]);
						commonList.get(i).setShow_time(time);
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}else{
					commonList.get(i).setShow_time(time);
				}
				
				CommonInfo commonPrice = priceMap.get(commonInfo.getId()+"");
				if(commonPrice!=null){
					commonList.get(i).setPrice(commonPrice.getPrice());
					commonList.get(i).setDiscount(commonPrice.getDiscount());
					commonList.get(i).setMin_price(commonPrice.getMin_price());
				}
		}
		int pageTotal= 0 ;
		if (totalCount % pageCount == 0) {
			pageTotal = totalCount / pageCount;
		} else {
			pageTotal = totalCount / (pageCount + 1);
		}
		String totaljson = "[{\"totalcount\":\""+totalCount+"\"}]";
		String totalPagejson = "[{\"totalPagecount\":\"" + pageTotal + "\"}]"; 
		JSONArray j = JSONArray.fromObject(commonList);
		String dataString = "{\"totalproperty\":"+totaljson+",\"results\":" + j.toString() + ",\"totalPage\":" + totalPagejson + "}";
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(dataString);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
	
	/**
	 * 删除用户浏览历史
	 * @param hs
	 * @param request
	 * @param response
	 */
	@RequestMapping(value = "/delUserHistory.do", method = RequestMethod.POST)
	public void delUserHistory(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		// 0:表示不成功  1:表示未登录 2:表示成功 
		String back="0";
		User user = (User)hs.getAttribute("user");
		String common_id=request.getParameter("common_id");
		if(user!=null) {
			Map<Object,Object> map=new HashMap<Object,Object>();
			map.put("common_id", common_id);
			map.put("user_id", user.getUser_id());
			int result=commonService.deleteObject("common.delete_history", map);
			if(result>0){
				back="2";
			}
		}else{
			back="1";
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
			e.printStackTrace();
		}
	}
	
	
	

	
}
