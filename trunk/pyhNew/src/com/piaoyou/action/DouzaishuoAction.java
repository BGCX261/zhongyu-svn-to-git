package com.piaoyou.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.piaoyou.domain.RemarkCollect;
import com.piaoyou.domain.RemarkRecommend;
import com.piaoyou.domain.CommonStat;
import com.piaoyou.domain.ContentRemark;
import com.piaoyou.domain.RemarkReply;
import com.piaoyou.domain.User;
import com.piaoyou.domain.UserRecommend;
import com.piaoyou.service.CommonInfoService;
import com.piaoyou.service.CommonService;
import com.piaoyou.util.Const;
import com.piaoyou.util.Division4Page;
import com.piaoyou.util.ImgPathUtil;

@Controller
@RequestMapping(value = "/douzaishuo")
public class DouzaishuoAction {
	@Autowired
	private CommonService commonService;
	@Autowired
	private CommonInfoService commonInfoService;
	private Division4Page d4p;

	@RequestMapping(value = "/douzaishuo.do", method = RequestMethod.GET)
	public String douzaishuo(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		int pageCount = 10;
		int pageNum=1;
		try {
			pageNum = (request.getParameter("pageNum")==null||"".equals(request.getParameter("pageNum")))?1:Integer.parseInt(request.getParameter("pageNum"));
		} catch (Exception e1) {
			pageNum = 1;
		}
		int pageIndex = (pageNum-1)*pageCount;
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("pageIndex", pageIndex);
		params.put("pageCount", pageCount);
		List<ContentRemark> crlist = this.commonService.selectObjects("remark.getDouzaishuo", params);
		for(ContentRemark cr:crlist){
			//头像
			cr.setUser_portrait(Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, cr.getUser_id())+"50x50_"+cr.getUser_portrait());
			//其他剧评
			params.put("user_id", cr.getUser_id());
			List<ContentRemark> list = this.commonService.selectObjects("remark.getOtherRecommond", params);
			cr.setOther_recommond(list);
			//用户信息统计
			CommonStat stat=commonService.selectObject("common.selectUserInfoByUserId", cr.getUser_id());
			cr.setStat(stat);
			//用户标签
			List<Map<String,String>> listtars = commonService.selectObjects("user.getMyTags",params);
			cr.setUser_tags(listtars);
		}
		
		String count = this.commonService.selectObject("remark.getDouzaishuoCount", params);
		d4p = new Division4Page(pageNum+"", Integer.parseInt(count), pageCount);
		d4p.setList(crlist);
		map.put("crlist", crlist);
		request.setAttribute("d4p", d4p);
		return "douzaishuo/douzaishuo";
	}
	
	@RequestMapping(value = "/douzaishuoDetail.do", method = RequestMethod.GET)
	public String douzaishuoDetail(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		String id = request.getParameter("id");
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("id", id);
		ContentRemark cr = this.commonService.selectObject("remark.getDouzaishuoDetail", params);
		//头像
		cr.setUser_portrait(Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, cr.getUser_id())+"50x50_"+cr.getUser_portrait());
		//其他剧评
		params.put("user_id", cr.getUser_id());
		List<ContentRemark> list = this.commonService.selectObjects("remark.getOtherRecommond", params);
		cr.setOther_recommond(list);
		//用户信息统计
		CommonStat stat=commonService.selectObject("common.selectUserInfoByUserId", cr.getUser_id());
		cr.setStat(stat);
		//用户标签
		List<Map<String,String>> listtars = commonService.selectObjects("user.getMyTags",params);
		cr.setUser_tags(listtars);
		//更新浏览数量
		this.commonService.updateObject("remark.updateContentRemarkByid", cr);
		map.put("cr", cr);
		
		//推荐列表
		params = new HashMap<Object,Object>();
		params.put("remark_id", id);
		params.put("pageIndex", 0);
		params.put("pageCount", 3);
		List<RemarkRecommend> recommendList = commonService.selectObjects("common.getRemarkRecommendList", params);
		String recommendCount = commonService.selectObject("common.getRemarkRecommendCount", params);
		for(int i=0;i<recommendList.size();i++){
			if(recommendList.get(i).getUser_portrait()!=null){
				recommendList.get(i).setUser_portrait(Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, recommendList.get(i).getUser_id())+"30x30_"+recommendList.get(i).getUser_portrait());
	    	}else{
	    		//设置默认的图像路径
	    		String path=request.getContextPath();
	    		recommendList.get(i).setUser_portrait(path+"/public/images/head_default3.png");
	    	}
		}
		
		request.setAttribute("recommendList", recommendList);
		request.setAttribute("recommendCount", recommendCount);
		
		
		//评论
		params.put("remark_id", id);
		params.put("pageIndex", 0);
		params.put("pageCount", 3);
		String commentCount  =  commonService.selectObject("remark.getRemarkCommentsCount", params);
	    List<RemarkReply>	commentList=commonService.selectObjects("remark.getRemarkComments", params);
	    for(RemarkReply rr:commentList){
	    
	    	if(rr.getUser_portrait()!=null){
	    		rr.setUser_portrait(Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, rr.getUser_id())+"50x50_"+rr.getUser_portrait());
	    	}else{
	    		//设置默认的图像路径
	    		String path=request.getContextPath();
	    		rr.setUser_portrait(path+"/public/images/head_default3.png");
	    	}
	    }
	    request.setAttribute("commentList", commentList);
	    request.setAttribute("commentCount", commentCount);
		
		
		
		return "douzaishuo/douzaishuoDetail";
	}
	
	
	/**
	 *  剧评推荐
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/getCommonRecommendList.do", method = RequestMethod.GET)
	public void getCommonRecommendList(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		String remark_id = request.getParameter("remark_id");
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("remark_id", remark_id);
		params.put("pageIndex", 0);
		params.put("pageCount", 3);
		List<RemarkRecommend> recommendList = commonService.selectObjects("common.getRemarkRecommendList", params);
		String recommendTotalCount = commonService.selectObject("common.getRemarkRecommendCount", params);
		for(int i=0;i<recommendList.size();i++){
			if(recommendList.get(i).getUser_portrait()!=null){
				recommendList.get(i).setUser_portrait(Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, recommendList.get(i).getUser_id())+"30x30_"+recommendList.get(i).getUser_portrait());
	    	}else{
	    		//设置默认的图像路径
	    		String path=request.getContextPath();
	    		recommendList.get(i).setUser_portrait(path+"/public/images/head_default3.png");
	    	}
		}
		JSONArray j = JSONArray.fromObject(recommendList);
		String dataString = "{\"totalCount\":"+recommendTotalCount+",\"results\":" + j.toString() + "}";
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
	 *  添加剧评推荐
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/addRemarkRecommend.do", method = RequestMethod.POST)
	public void AddRecommend(HttpServletRequest request,HttpServletResponse response,ModelMap map,HttpSession hs){
		String retMsg = "";
		String remark_id = request.getParameter("remark_id");
		String recommend_reason = request.getParameter("recommend_reason");
		String recommendflag = (request.getParameter("recommendflag")==null||"".equals(request.getParameter("recommendflag")))?"0":request.getParameter("recommendflag");
		
		User user = (User)hs.getAttribute("user");
		if(user==null){
			retMsg ="1";
		}else{
			try {
			if("1".equals(recommendflag)){  //推荐的同时作为评论发布
				RemarkReply repley=new RemarkReply();
				repley.setRemark_id(Integer.parseInt(remark_id));
				repley.setUser_id(user.getUser_id());
				repley.setContent(recommend_reason);
				int result=commonService.insertObject("remark.insert_remarkreply", repley);
				
			}
			RemarkRecommend remarkRecommend = new RemarkRecommend();
			remarkRecommend.setUser_id(user.getUser_id());
			remarkRecommend.setRemark_id(Integer.parseInt(remark_id));
			remarkRecommend.setRecommend_reason(recommend_reason);
			commonService.insertObject("common.addRemarkRecommend", remarkRecommend);
			commonService.updateObject("common.updateRemarkRecommendCount", remarkRecommend);
			retMsg="2";
			} catch (Exception e) {
				retMsg="3";
			}
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(retMsg);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
		

	}
	
	
	/**
	 * 获取收藏列表
	 */
	
	
	
	
	
	/**
	 *  剧评收藏
	 * @param request
	 * @param response
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/addCollect.do", method = RequestMethod.POST)
	public void collect(HttpServletRequest request,HttpServletResponse response,ModelMap map,HttpSession hs){
		String retMsg = "";
		String remark_id = request.getParameter("remark_id");
		User user = (User)hs.getAttribute("user");
		Map<Object,Object> params = new HashMap<Object,Object>();
		if(user==null){
			retMsg = "1";
		}else{
			try {
				RemarkCollect remarkCollect = new RemarkCollect();
				remarkCollect.setUser_id(user.getUser_id());
				remarkCollect.setRemark_id(Integer.parseInt(remark_id));
				params.put("user_id", user.getUser_id());
				params.put("remark_id", remark_id);
				List<RemarkCollect> collect = commonService.selectObjects("common.getRemarkCollectList", params);
				if(collect!=null&&collect.size()>0){
					retMsg = "3";
				}else{
					commonService.insertObject("common.addRemarkCollect", remarkCollect);
					retMsg ="2";
				}
			} catch (Exception e) {
				retMsg ="0";
			}
		}
		try {
			request.setCharacterEncoding("utf-8");
			response.setContentType("text/json;charset=UTF-8");
			PrintWriter out;
			out = response.getWriter();
			out.write(retMsg);
			out.flush();
			out.close();
		} catch (IOException e) {
			System.out.print("获取json相关数据报错"+e.getMessage());
		}
	}
}
