package com.piaoyou.action;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.piaoyou.domain.Category;
import com.piaoyou.domain.CommonComment;
import com.piaoyou.domain.CommonInfo;
import com.piaoyou.domain.User;
import com.piaoyou.domain.UserCollection;
import com.piaoyou.domain.UserRecommend;
import com.piaoyou.service.CommonInfoService;
import com.piaoyou.service.CommonService;
import com.piaoyou.util.Const;
import com.piaoyou.util.DateTime;
import com.piaoyou.util.Division4Page;
import com.piaoyou.util.ImgPathUtil;

@Controller
@RequestMapping(value = "/result")
public class ResultAction {
	private static final Log log = LogFactory.getLog(ResultAction.class);
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
		
		
		String city_id_result=(request.getParameter("city_id_result")==null||"".equals(request.getParameter("city_id_result")))?"":request.getParameter("city_id_result");
		request.setAttribute("city_id_result", city_id_result);
		String show_type=(request.getParameter("show_type")==null||"".equals(request.getParameter("show_type")))?"":request.getParameter("show_type");
		request.setAttribute("show_type", show_type);
		String week=(request.getParameter("week")==null||"".equals(request.getParameter("week")))?"":request.getParameter("week");
		request.setAttribute("week", week);
		String timeType=(request.getParameter("timeType")==null||"".equals(request.getParameter("timeType")))?"":request.getParameter("timeType");
		request.setAttribute("timeType", timeType);
		String priceType=(request.getParameter("priceType")==null||"".equals(request.getParameter("priceType")))?"":request.getParameter("priceType");
		request.setAttribute("priceType", priceType);
		String show_name=(request.getParameter("show_name")==null||"".equals(request.getParameter("show_name")))?"":request.getParameter("show_name");
		//System.out.println(URLEncoder.encode(show_name)+"======================");
		request.setAttribute("show_name_ecode", URLEncoder.encode(show_name));
		try {
			//show_name= new String(show_name.getBytes("ISO-8859-1"), "utf-8");
			show_name = URLDecoder.decode(new String(show_name.trim().getBytes("iso-8859-1"), "utf-8"), "utf-8");
			//URLEncoder.encode(s, enc)
		} catch (UnsupportedEncodingException e1) {
			show_name = "";
			e1.printStackTrace();
		}
		
		request.setAttribute("show_name", show_name);
//		if("请输入搜索内容".equals(show_name)){
//			show_name="";
//		}
		int pageCount = 10;
		int pageNum=1;
		try {
			pageNum = (request.getParameter("pageNum")==null||"".equals(request.getParameter("pageNum")))?1:Integer.parseInt(request.getParameter("pageNum"));
		} catch (Exception e1) {
			pageNum = 1;
		}
		//request.setAttribute("show_name", show_name);
		
		
		String start_time="";
		String end_time="";
		if("today".equals(timeType)){
			start_time = newDate.format(new Date());
			end_time = DateTime.getAddDate(newDate.format(new Date()), 1);
		}else if("tomorrow".equals(timeType)){
			start_time = DateTime.getAddDate(newDate.format(new Date()), 1);
			end_time = DateTime.getAddDate(newDate.format(new Date()), 2);
		}else if("week".equals(timeType)){
			start_time = newDate.format(new Date());
			end_time = DateTime.getAddDate(newDate.format(new Date()), 7);
		}else if("month".equals(timeType)){
			start_time = newDate.format(new Date());
			end_time = DateTime.getAddDate(newDate.format(new Date()), 30);
		}else if("threemonth".equals(timeType)){
			start_time = newDate.format(new Date());
			end_time = DateTime.getAddDate(newDate.format(new Date()), 90);
		}else{
			start_time="";
			end_time="";
		}
		
		int min_price = 0;
		int max_price = 0;
		if("1".equals(priceType)){
			min_price= 0;
			max_price= 100;
		}else if("2".equals(priceType)){
			min_price= 101;
			max_price= 300;
		}else if("3".equals(priceType)){
			min_price= 301;
			max_price= 500;
		}else if("4".equals(priceType)){
			min_price= 500;
			max_price= 1000;
		}else if("5".equals(priceType)){
			min_price= 1000;
			max_price= 100000000;
		}
		//获取所有演出城市信息
		Map<Object,Object> params = new HashMap<Object,Object>();
		List<CommonInfo> cityList = commonInfoService.getCityList();
		Map<Object,Object> timeMap = commonInfoService.getCommonInfoTimeMap();
		Map<Object,CommonInfo> priceMap = commonInfoService.getCommonInfoPriceMap();
		params.put("city_id", city_id_result);
		params.put("show_type", show_type);
		params.put("week", week);
		params.put("timeType", timeType);
		params.put("start_time", start_time);
		params.put("end_time", end_time);
		params.put("min_price", min_price);
		params.put("max_price", max_price);
		params.put("show_name", show_name);
		params.put("pageIndex", (pageNum-1)*pageCount);
		params.put("pageCount", pageCount);
		List<CommonInfo> commonList = commonService.selectObjects("result.getCommonList", params);
		String count = commonService.selectObject("result.getCommonTotalCount", params);
		d4p = new Division4Page(pageNum+"", Integer.parseInt(count), pageCount);
		d4p.setList(commonList);
		for(int i=0;i<commonList.size();i++){
			CommonInfo commonInfo = commonList.get(i);
			//处理简介信息
			String introduction = commonInfo.getIntroduction();
			if(introduction!=null){
				introduction = Jsoup.parse(introduction).text().replaceAll(" ", "");
				if(introduction.length()>120){
					introduction=introduction.substring(0,120);
				}
			}
			commonList.get(i).setIntroduction(introduction);
			//格式化时间
			CommonInfo time = (CommonInfo)timeMap.get(commonInfo.getId()+"");
			if(time!=null){
				String show_time = time.getShow_time();
				String limit_time = time.getLimit_time();
				if(show_time!=null&&!show_time.contains("全年")&&!"".equals(time)){
					try {
						show_time = day.format(dayMinite.parse(show_time));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				commonList.get(i).setShow_time(show_time);
			}
			
			//格式化状态
			String show_status = "";
			if(commonInfo.getStatus()==0){
				show_status ="预售中";
			}else if(commonInfo.getStatus()==2){
				show_status="已过期";
			}else{
				show_status = "售票中";
			}
			commonList.get(i).setShow_status(show_status);
			
			//格式化票价
			commonInfo = priceMap.get(commonInfo.getId()+"");
			if(commonInfo!=null){
				commonList.get(i).setPrice(commonInfo.getPrice());
				commonList.get(i).setMin_price(commonInfo.getMin_price());
				commonList.get(i).setDiscount(commonInfo.getDiscount());
				commonList.get(i).setMainURL(commonInfo.getMainURL());
			}
		}
		request.setAttribute("commonList", commonList);
		request.setAttribute("cityList", cityList);
		request.setAttribute("d4p", d4p);
		return "result/result";
	}
	
	@RequestMapping(value = "/resultDetail.do", method = RequestMethod.GET)
	public String resultDetail(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
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
		request.setAttribute("firstTime", firstTime);
		
		
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
		
		String category_id = "";
		String[] ids = {Const.INDEX_ERRENSHIJIE_ID+"",Const.INDEX_XUNKAIXIN_ID+"",Const.INDEX_DAFASHIGUANG_ID+"",Const.INDEX_SHIFANG_ID+"",Const.INDEX_JINYEYOUXI_ID+"",Const.INDEX_BENZHOUJINGPIN_ID+"",Const.INDEX_HUAJU_ID+"",Const.INDEX_YANCHANGHUI_ID+"",Const.INDEX_XIJUQUYI_ID+"",Const.INDEX_GUDDIAN_ID+"",Const.INDEX_YAOGUNYUE_ID+"",Const.INDEX_SAISHI_ID+"",Const.INDEX_ERTONGQINZI_ID+"",Const.INDEX_YINGXUN_ID+""};
		if(category_id==null || "".equals(category_id)){
			category_id=ids[(int)(Math.random()*10)%ids.length];
		}
		List<CommonInfo> recommendList = commonInfoService.getCommonInfoList(Integer.parseInt(category_id),"", commonInfo.getCity_id(), "1", "5");
		request.setAttribute("recommendList", recommendList);
		
		request.setAttribute("city_id", commonInfo.getCity_id());
		
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
		
		//判断是否已经收藏过
		int isCollect = 0;
		if(user!=null){
			params = new HashMap<Object,Object>();
			params.put("common_id", id);
			params.put("user_id", user.getUser_id());
			List<UserCollection> collectList = commonService.selectObjects("common.isAlreadyCollect", params);
			if(collectList!=null&&collectList.size()>0){
				isCollect = 1;
			}
		}
		request.setAttribute("isCollect", isCollect);
		
		return "result/resultDetail";
	}
	
}
