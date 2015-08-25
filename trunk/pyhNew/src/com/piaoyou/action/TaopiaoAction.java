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

import com.piaoyou.domain.Category;
import com.piaoyou.domain.CommonComment;
import com.piaoyou.domain.CommonInfo;
import com.piaoyou.domain.FocusPhoto;
import com.piaoyou.domain.UserRecommend;
import com.piaoyou.service.CommonInfoService;
import com.piaoyou.service.CommonService;
import com.piaoyou.service.MovieService;
import com.piaoyou.service.TaopiaoService;
import com.piaoyou.util.Const;
import com.piaoyou.util.Division4Page;
import com.piaoyou.util.ImgPathUtil;

@Controller
@RequestMapping(value = "/taopiao")
public class TaopiaoAction {
	private static final Log log = LogFactory.getLog(TaopiaoAction.class);
	@Autowired
	private CommonService commonService;
	@Autowired
	private TaopiaoService taopiaoService;
	@Autowired
	private CommonInfoService commonInfoService;
	@Autowired
	private MovieService movieService;
	private Division4Page d4p;
	
	@RequestMapping(value = "/test.do", method = RequestMethod.GET)
	public String test(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		log.info("-----------------------------test successfully!");
		return "user/test";
	}
	
	@RequestMapping(value = "/main.do", method = RequestMethod.GET)
	public String main(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		Map<Object,Object> params = null;
		String city_id=(request.getParameter("city_id")==null||"".equals(request.getParameter("city_id")))?"1":request.getParameter("city_id");
		//总数统计
		params = new HashMap<Object,Object>();
		//热票速递
		String repiaosudiTotalCount = commonInfoService.getCommonTotalCount(Const.TAOPIAO_REPIAOSUDI_ID, "");
		//团购
		params =new HashMap<Object,Object>();
		params.put("category_id", Const.TAOPIAO_TUANGOUTUIJIAN_ID);
		String groupMinPrice = commonService.selectObject("group.getGroupCategoryMinPrice", params);
		String groupTotalCount = commonInfoService.getGroupTotalCount(Const.TAOPIAO_TUANGOUTUIJIAN_ID, "","");
		//推荐
		String commendTotalCount = commonInfoService.getCommonTotalCount(Const.TAOPIAO_TUIJIAN_ID, "");
		//小剧场
		params =new HashMap<Object,Object>();
		params.put("category_id", Const.TAOPIAO_XIAOJUCHANG_ID);
		String xiaojuchangTotalCount = commonInfoService.getCommonTotalCount(Const.TAOPIAO_XIAOJUCHANG_ID, "");
		String xiaojuchangMinPrice = commonService.selectObject("commonInfo.getCommonInfoCategoryMinPrice", params);
		//热票速递
		String pageIndex = "1";
		String pageCount = "3";
		List<CommonInfo> repiaosudiList = commonInfoService.getCommonInfoList(Const.TAOPIAO_REPIAOSUDI_ID, "","", pageIndex, pageCount);
		//团购
		List<CommonInfo> groupList = commonInfoService.getGroupList(Const.TAOPIAO_TUANGOUTUIJIAN_ID, "","","", pageIndex, pageCount);
		//推荐
		List<CommonInfo> commendList = commonInfoService.getCommonInfoList(Const.TAOPIAO_TUIJIAN_ID, "","", pageIndex, pageCount);
		//小剧场
		List<CommonInfo> xiaojuchangList = commonInfoService.getCommonInfoList(Const.TAOPIAO_XIAOJUCHANG_ID, "","", pageIndex, pageCount);
		
		//总数统计
		request.setAttribute("groupTotalCount", groupTotalCount);
		request.setAttribute("commendTotalCount", commendTotalCount);
		request.setAttribute("xiaojuchangTotalCount", xiaojuchangTotalCount);
		request.setAttribute("groupMinPrice", groupMinPrice);
		request.setAttribute("xiaojuchangMinPrice", xiaojuchangMinPrice);
		
		//分类列表
		request.setAttribute("repiaosudiList", repiaosudiList);
		request.setAttribute("repiaosudiTotalCount", repiaosudiTotalCount);
		request.setAttribute("groupList", groupList);
		request.setAttribute("commendList", commendList);
		request.setAttribute("xiaojuchangList", xiaojuchangList);
		
		
		request.setAttribute("repiaosudi_id", Const.TAOPIAO_REPIAOSUDI_ID);
		request.setAttribute("group_id", Const.TAOPIAO_TUANGOUTUIJIAN_ID);
		request.setAttribute("tuijian_id", Const.TAOPIAO_TUIJIAN_ID);
		request.setAttribute("xiaojuchang_id", Const.TAOPIAO_XIAOJUCHANG_ID);
		
		request.setAttribute("city_id", city_id);
		
		//焦点图
		params.put("category_id", Const.JIAODIAN_TAOPIAO);
		params.put("pageIndex", 0);
		params.put("pageCount", Const.JIAODIAN_PAGECOUNT);
		List<FocusPhoto> listFocus = this.commonService.selectObjects("focusPhoto.getListByCId", params);
		request.setAttribute("listFocus", listFocus);
		return "taopiao/taopiao";
	}
	
	@RequestMapping(value = "/taopiaosub.do", method = RequestMethod.GET)
	public String taopiaosub(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String category_id=(request.getParameter("category_id")==null||"".equals(request.getParameter("category_id")))?"1799":request.getParameter("category_id");
		String city_id=(request.getParameter("city_id")==null||"".equals(request.getParameter("city_id")))?"1":request.getParameter("city_id");
		String pageNum=(request.getParameter("pageNum")==null||"".equals(request.getParameter("pageNum")))?"1":request.getParameter("pageNum");
		int pageCount = 10;
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("id", category_id);
		Category category = commonService.selectObject("category.getById", params);
		List<CommonInfo> commonList = commonInfoService.getCommonInfoList(Integer.parseInt(category_id),"","", pageNum, pageCount+"");
		String totalCount = commonInfoService.getCommonTotalCount(Integer.parseInt(category_id), city_id);
		d4p = new Division4Page(pageNum+"", Integer.parseInt(totalCount), pageCount);
		d4p.setList(commonList);
		request.setAttribute("d4p", d4p);
		request.setAttribute("category_id", category_id);
		request.setAttribute("city_id", city_id);
		request.setAttribute("commonList", commonList);
		request.setAttribute("category", category);
		request.setAttribute("totalCount", totalCount);
		//焦点图
		params.put("category_id", Const.JIAODIAN_TAOPIAO);
		params.put("pageIndex", 0);
		params.put("pageCount", Const.JIAODIAN_PAGECOUNT);
		List<FocusPhoto> listFocus = this.commonService.selectObjects("focusPhoto.getListByCId", params);
		request.setAttribute("listFocus", listFocus);
		return "taopiao/taopiaosub";
	}
	
	@RequestMapping(value = "/taopiaoGroupsub.do", method = RequestMethod.GET)
	public String taopiaoGroupsub(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String category_id=(request.getParameter("category_id")==null||"".equals(request.getParameter("category_id")))?"1799":request.getParameter("category_id");
		String city_id=(request.getParameter("city_id")==null||"".equals(request.getParameter("city_id")))?"1":request.getParameter("city_id");
		String pageNum=(request.getParameter("pageNum")==null||"".equals(request.getParameter("pageNum")))?"1":request.getParameter("pageNum");
		int pageCount = 10;
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("id", category_id);
		Category category = commonService.selectObject("category.getById", params);
		List<CommonInfo> commonList = commonInfoService.getGroupList(Integer.parseInt(category_id),"","","", pageNum, pageCount+"");
		String totalCount = commonInfoService.getGroupTotalCount(Integer.parseInt(category_id), "","");
		d4p = new Division4Page(pageNum+"", Integer.parseInt(totalCount), pageCount);
		d4p.setList(commonList);
		request.setAttribute("d4p", d4p);
		request.setAttribute("category_id", category_id);
		request.setAttribute("city_id", city_id);
		request.setAttribute("commonList", commonList);
		request.setAttribute("category", category);
		request.setAttribute("totalCount", totalCount);
		//焦点图
		params.put("category_id", Const.JIAODIAN_TAOPIAO);
		params.put("pageIndex", 0);
		params.put("pageCount", Const.JIAODIAN_PAGECOUNT);
		List<FocusPhoto> listFocus = this.commonService.selectObjects("focusPhoto.getListByCId", params);
		request.setAttribute("listFocus", listFocus);
		return "taopiao/taopiaogroupsub";
	}
	
	@RequestMapping(value = "/taopiaoChannel.do", method = RequestMethod.GET)
	public String taopiaoChannel(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String category_id=(request.getParameter("category_id")==null||"".equals(request.getParameter("category_id")))?"1799":request.getParameter("category_id");
		String order=(request.getParameter("order")==null||"".equals(request.getParameter("order")))?"":request.getParameter("order");
		String pageNum=(request.getParameter("pageNum")==null||"".equals(request.getParameter("pageNum")))?"1":request.getParameter("pageNum");
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("parent_id", category_id);
		int pageCount=9;
		List<Category> categoryList = commonService.selectObjects("category.getListByPid", params);
		String city_id=(request.getParameter("city_id")==null||"".equals(request.getParameter("city_id")))?"1":request.getParameter("city_id");
		List<CommonInfo> commonList = commonInfoService.getCommonInfoList(Integer.parseInt(category_id),order,"", pageNum, pageCount+"");
		String totalCount = commonInfoService.getCommonTotalCount(Integer.parseInt(category_id),"");
		params =new HashMap<Object,Object>();
		params.put("category_id", category_id);
		String minPrice = commonService.selectObject("commonInfo.getCommonInfoCategoryMinPrice", params);
		d4p = new Division4Page(pageNum+"", Integer.parseInt(totalCount), pageCount);
		d4p.setList(commonList);
		request.setAttribute("category_id", category_id);
		request.setAttribute("city_id", city_id);
		request.setAttribute("commonList", commonList);
		request.setAttribute("categoryList", categoryList);
		request.setAttribute("d4p", d4p);
		request.setAttribute("minPrice", minPrice);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("order", order);
		return "taopiao/taopiaochannel";
	}
	
	@RequestMapping(value = "/taopiaoList.do", method = RequestMethod.GET)
	public String taopiaoList(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String category_id=(request.getParameter("category_id")==null||"".equals(request.getParameter("category_id")))?"1799":request.getParameter("category_id");
		String order=(request.getParameter("order")==null||"".equals(request.getParameter("order")))?"":request.getParameter("order");
		String pageNum=(request.getParameter("pageNum")==null||"".equals(request.getParameter("pageNum")))?"1":request.getParameter("pageNum");
		int pageCount=9;
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("id",category_id);
		Category category = commonService.selectObject("category.getById", params);
		params.put("parent_id", category.getParent_id());
		List<Category> categoryList = commonService.selectObjects("category.getListByPid", params);
		String city_id=(request.getParameter("city_id")==null||"".equals(request.getParameter("city_id")))?"1":request.getParameter("city_id");
		List<CommonInfo> commonList = commonInfoService.getCommonInfoList(Integer.parseInt(category_id), order,"", pageNum, pageCount+"");
		String totalCount = commonInfoService.getCommonTotalCount(Integer.parseInt(category_id), "");
		params =new HashMap<Object,Object>();
		params.put("category_id", category_id);
		String minPrice = commonService.selectObject("commonInfo.getCommonInfoCategoryMinPrice", params);
		d4p = new Division4Page(pageNum+"", Integer.parseInt(totalCount), pageCount);
		d4p.setList(commonList);
		request.setAttribute("parent_category_id", category.getParent_id());
		request.setAttribute("category_id", category_id);
		request.setAttribute("city_id", city_id);
		request.setAttribute("commonList", commonList);
		request.setAttribute("categoryList", categoryList);
		request.setAttribute("d4p", d4p);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("minPrice", minPrice);
		return "taopiao/taopiaolist";
	}
	
	@RequestMapping(value = "/taopiaoDetail.do", method = RequestMethod.GET)
	public String taopiaoDetail(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String id=(request.getParameter("id")==null||"".equals(request.getParameter("id")))?"0":request.getParameter("id");
		CommonInfo commonInfo = taopiaoService.getCommonInfoDetail(Integer.parseInt(id));
		//获取演出时间
		List<CommonInfo> timeList =  commonInfoService.getTimeList(id);
		CommonInfo firstTime = null;
		if(timeList!=null&&timeList.size()>0){
			firstTime = timeList.get(0);
			commonInfo.setYear(firstTime.getYear());
			commonInfo.setMonth(firstTime.getMonth());
			commonInfo.setDay(firstTime.getDay());
			commonInfo.setWeek(firstTime.getWeek());
			commonInfo.setShow_time(firstTime.getShow_time());
			timeList.remove(0);
		}
		commonInfo.setTimeList(timeList);
		request.setAttribute("commonInfo", commonInfo);
		List<CommonInfo> recommendList = commonInfoService.getCommonInfoList(Const.TAOPIAO_ID,"", commonInfo.getCity_id(), "1", "5");
		request.setAttribute("recommendList", recommendList);
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("id", id);
		List<Category> categoryList = commonService.selectObjects("category.getListByCommonId", params);
		request.setAttribute("categoryList", categoryList);
		
		//拿到2条该演出的推荐信息
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
		request.setAttribute("recommend2List", recommend2List);
		int recommendCount=commonService.selectObject("common.selectRecommendByCommonIdCount", params);
		request.setAttribute("recommendCount", recommendCount);
		//获取评论信息
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
		request.setAttribute("city_id",commonInfo.getCity_id());
		return "taopiao/taopiaomj";
	}
	
	
	
	
	
	
}
