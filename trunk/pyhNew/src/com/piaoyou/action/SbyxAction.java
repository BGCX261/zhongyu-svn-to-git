package com.piaoyou.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import com.piaoyou.domain.User;
import com.piaoyou.domain.UserRecommend;
import com.piaoyou.service.CommonInfoService;
import com.piaoyou.service.CommonService;
import com.piaoyou.util.Const;
import com.piaoyou.util.Division4Page;
import com.piaoyou.util.ImgPathUtil;

@Controller
@RequestMapping(value = "/sbyx")
public class SbyxAction {
	private static final Log log = LogFactory.getLog(SbyxAction.class);
	private static final SimpleDateFormat dayMinite= new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static final SimpleDateFormat day= new SimpleDateFormat("yyyy.MM.dd");
	public SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd");
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
		String city_id = request.getParameter("city_id")==null?"":request.getParameter("city_id");
		String category_id = "0";
		String page = "";
		if("1".equals(city_id)){   //北京
			category_id="1926";
			page="sbyx/sbyx_beijing";
		}else if("2".equals(city_id)){  //广州
			category_id="1930";
			page="sbyx/sbyx_guangzhou";
		}else if("3".equals(city_id)){  //上海
			category_id="1927";
			page="sbyx/sbyx_shanghai";
		}else if("".equals(city_id)&&"".equals(category_id)){  //默认为北京
			category_id="1926";
			page="sbyx/sbyx_beijing";
		}
		
		Map<Object,Object> params = new HashMap<Object,Object>();
		//获取区域信息
		params.put("parent_id", category_id);
		List<Category> categoryList = commonService.selectObjects("category.getListByPid", params);
		params.put("id", category_id);
		Category category = commonService.selectObject("category.getById",params);
		request.setAttribute("categoryList", categoryList);
		request.setAttribute("category", category);
		request.setAttribute("city_id", city_id);
		//焦点图
		params = new HashMap<Object,Object>();
		params.put("category_id", category_id);
		params.put("pageIndex", 0);
		params.put("pageCount", Const.JIAODIAN_PAGECOUNT);
		List<FocusPhoto> listFocus = this.commonService.selectObjects("focusPhoto.getListByCId", params);
		request.setAttribute("listFocus", listFocus);
		request.setAttribute("city_id", city_id);
		return page;
	}
	
	@RequestMapping(value = "/sbyxlist.do", method = RequestMethod.GET)
	public String sbyxlist(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String category_id = request.getParameter("category_id")==null?"":request.getParameter("category_id");
		String city_id = request.getParameter("city_id")==null?"":request.getParameter("city_id");
		String pageNum = (request.getParameter("pageNum")==null||"".equals(request.getParameter("pageNum")))?"1":request.getParameter("pageNum");
		int pageCount = 5;
		Map<Object,Object> params = new HashMap<Object,Object>();
		//获取区域信息
		params.put("id", category_id);
		List<Category> categoryList = commonService.selectObjects("category.getCategoryListByID", params);
		params.put("id", category_id);
		Category category = commonService.selectObject("category.getById",params);
		List<CommonInfo> commonList = commonInfoService.getCommonInfoList(Integer.parseInt(category_id), "","", pageNum, pageCount+"");
		String totalCount = commonInfoService.getCommonTotalCount(Integer.parseInt(category_id), "");
		d4p = new Division4Page(pageNum+"", Integer.parseInt(totalCount),pageCount);
		d4p.setList(commonList);
		request.setAttribute("commonList", commonList);
		request.setAttribute("categoryList", categoryList);
		request.setAttribute("category", category);
		request.setAttribute("city_id", city_id);
		request.setAttribute("d4p", d4p);
		return "sbyx/sbyxlist";
	}
	
	@RequestMapping(value = "/sbyxdetail.do", method = RequestMethod.GET)
	public String quyudetail(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String id=(request.getParameter("id")==null||"".equals(request.getParameter("id")))?"0":request.getParameter("id");
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("id", id);
		//本单所在栏目
		List<Category> categoryList = commonService.selectObjects("category.getListByCommonId", params);
		request.setAttribute("categoryList", categoryList);
		//获取演出基本信息
		CommonInfo commonInfo = commonService.selectObject("result.getCommonInfoDetail", params);
		
		User user = (User)hs.getAttribute("user");
		if(user!=null&&commonInfo!=null){
			commonInfoService.insertHistoty(user.getUser_id(), commonInfo.getId());
		}
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
		Map<Object,CommonInfo> priceMap = commonInfoService.getCommonInfoPriceMap();
		CommonInfo commonPrice = priceMap.get(id+"");
		if(commonPrice!=null){
			commonInfo.setPrice(commonPrice.getPrice());
			commonInfo.setDiscount(commonPrice.getDiscount());
			commonInfo.setMin_price(commonPrice.getMin_price());
			commonInfo.setMainURL(commonPrice.getMainURL());
		}
		
		request.setAttribute("commonInfo", commonInfo);
		
		
		
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
		request.setAttribute("city_id", commonInfo.getCity_id());
		return "sbyx/sbyxDetail";
	}
	
}
