package com.piaoyou.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.piaoyou.domain.CommonComment;
import com.piaoyou.domain.CommonInfo;
import com.piaoyou.domain.FocusPhoto;
import com.piaoyou.domain.UserRecommend;
import com.piaoyou.service.CommonInfoService;
import com.piaoyou.service.CommonService;
import com.piaoyou.util.Const;
import com.piaoyou.util.Division4Page;
import com.piaoyou.util.ImgPathUtil;

@Controller
@RequestMapping(value = "/group")
public class GroupAction {
	private static final Log log = LogFactory.getLog(GroupAction.class);
	private Division4Page d4p;
	@Autowired
	private CommonService commonService;
	@Autowired
	private CommonInfoService commonInfoService;
	@RequestMapping(value = "/test.do", method = RequestMethod.GET)
	public String test(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		log.info("-----------------------------test successfully!");
		return "user/test";
	}
	@RequestMapping(value = "/main.do", method = RequestMethod.GET)
	public String main(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String city_id=(request.getParameter("city_id")==null||"".equals(request.getParameter("city_id")))?"1":request.getParameter("city_id");
		String pageNum=(request.getParameter("pageNum")==null||"".equals(request.getParameter("pageNum")))?"1":request.getParameter("pageNum");
		String name=(request.getParameter("name")==null||"".equals(request.getParameter("name")))?"":request.getParameter("name");
		try{
			name = java.net.URLDecoder.decode(name,"utf-8");
		}catch(Exception e){
			
		}
		int pageCount = 9;
		List<CommonInfo> groupList = commonInfoService.getGroupList(Const.GROUP_REMENTUANGOU_ID, city_id, name,"",pageNum, pageCount+"");
		String  totalCount = commonInfoService.getGroupTotalCount(Const.GROUP_REMENTUANGOU_ID, name,city_id);
		d4p = new Division4Page(pageNum+"", Integer.parseInt(totalCount), pageCount);
		d4p.setList(groupList);
		request.setAttribute("groupList", groupList);
		request.setAttribute("d4p", d4p);
		request.setAttribute("city_id", city_id);
		//焦点图
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("category_id", Const.JIAODIAN_TUANGOU);
		params.put("pageIndex", 0);
		params.put("pageCount", Const.JIAODIAN_PAGECOUNT);
		List<FocusPhoto> listFocus = this.commonService.selectObjects("focusPhoto.getListByCId", params);
		request.setAttribute("listFocus", listFocus);
		return "group/tuangou";
	}
	
	@RequestMapping(value = "/groupdetail.do", method = RequestMethod.GET)
	public String groupdetail(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String id=(request.getParameter("id")==null||"".equals(request.getParameter("id")))?"0":request.getParameter("id");
		CommonInfo commonInfo = commonInfoService.getGroupDetail(Integer.parseInt(id));
		request.setAttribute("commonInfo", commonInfo);
		List<CommonInfo> recommendList = commonInfoService.getGroupList(Const.GROUP_REMENTUANGOU_ID, commonInfo.getCity_id(),"","", "1", "5");
		request.setAttribute("recommendList", recommendList);
		
		//获取评论信息
		Map<Object,Object> params = new HashMap<Object,Object>();
		params = new HashMap<Object,Object>();
		params.put("common_id", Integer.parseInt(id));
		params.put("pageIndex", 0);
		params.put("pageCount", 2);
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
	    request.setAttribute("commentList", commentList);
		request.setAttribute("commentCount", totalCount);
		
		request.setAttribute("city_id", commonInfo.getCity_id());
		return "group/tuangoumj";
	}
	
}
