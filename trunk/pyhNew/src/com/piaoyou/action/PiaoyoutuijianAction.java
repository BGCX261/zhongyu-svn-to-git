package com.piaoyou.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.piaoyou.domain.CommonInfo;
import com.piaoyou.domain.UserRecommend;
import com.piaoyou.service.CommonInfoService;
import com.piaoyou.service.CommonService;
import com.piaoyou.util.Const;
import com.piaoyou.util.Division4Page;

@Controller
@RequestMapping(value = "/piaoyoutuijian")
public class PiaoyoutuijianAction {
	@Autowired
	private CommonService commonService;
	@Autowired
	private CommonInfoService commonInfoService;
	private Division4Page d4p;
	private SimpleDateFormat YYYY_MM_DD_HH_MM = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@RequestMapping(value = "/piaoyoutuijian.do", method = RequestMethod.GET)
	public String douzaikan(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		int pageCount = 6;
		int pageNum=1;
		try {
			pageNum = (request.getParameter("pageNum")==null||"".equals(request.getParameter("pageNum")))?1:Integer.parseInt(request.getParameter("pageNum"));
		} catch (Exception e1) {
			pageNum = 1;
		}
		int pageIndex = (pageNum-1)*pageCount;
		String city_id=(request.getParameter("city_id")==null||"".equals(request.getParameter("city_id")))?"1":request.getParameter("city_id");
		String category_id = request.getParameter("category_id");
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("city_id", city_id);
		
		if(category_id!=null && !"".equals(category_id)){
			params.put("category_id", category_id);
		}
		Map<Object,Object> timeMap = commonInfoService.getCommonInfoTimeMap();
		Map<Object,CommonInfo> priceMap = commonInfoService.getCommonInfoPriceMap();
		params.put("pageIndex", pageIndex);
		params.put("pageCount", pageCount);
		params.put("order", "recommend_count");
		List<CommonInfo> doukanList = this.commonService.selectObjects("commonInfo.getDouzaikan", params);
		for(CommonInfo common:doukanList){
			params.put("pageIndex", 0);
			params.put("pageCount", 4);
			params.put("common_id", common.getId());
			List<UserRecommend> list = this.commonService.selectObjects("common.getUserRecommenByShowId", params);
			common.setRecommens(list);
			
			CommonInfo time = (CommonInfo)timeMap.get(common.getId()+"");
			
			if(time!=null){
				
			
			String show_time = time.getShow_time();
			String limit_time = time.getLimit_time();
			if(show_time!=null&&!"null".equals(show_time)&&!show_time.contains("全年")){
				Calendar calendar = Calendar.getInstance();
				try {
					calendar.setTime(YYYY_MM_DD_HH_MM.parse(show_time));
					int year = calendar.get(Calendar.YEAR);
					int month=calendar.get(Calendar.MONTH)+1;    
					int day =calendar.get(Calendar.DAY_OF_MONTH);  
					int hour =calendar.get(Calendar.HOUR);  
					int minute =calendar.get(Calendar.MINUTE);
					int week = calendar.get(Calendar.DAY_OF_WEEK);
					int AM_PM = calendar.get(Calendar.AM_PM);
					if(AM_PM==0){
						show_time = "早";
					}else{
						show_time = "晚";
					}
					if(hour<10){
						show_time=show_time+"0"+hour+":";
					}else{
						show_time=show_time+hour+":";
					}
					if(minute<10){
						show_time=show_time+"0"+minute;
					}else{
						show_time=show_time+minute;
					}
					common.setYear(year+"");
					common.setDay(day+"");
					if(month<10){
						common.setMonth("0"+month);
					}else{
						common.setMonth(month+"");
					}
					common.setWeek(Const.week[week-1]);
					common.setShow_time(show_time);
					common.setLimit_time(limit_time);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}else{
				common.setShow_time(show_time);
				common.setLimit_time(limit_time);
			}
			}
			
			CommonInfo commonPrice = priceMap.get(common.getId()+"");
			if(commonPrice!=null){
				common.setPrice(commonPrice.getPrice());
				common.setDiscount(commonPrice.getDiscount());
				common.setMin_price(commonPrice.getMin_price());
			}
		
		}
		String count = this.commonService.selectObject("commonInfo.getDouzaikanCount", params);
		d4p = new Division4Page(pageNum+"", Integer.parseInt(count), pageCount);
		d4p.setList(doukanList);
		map.put("doukanList", doukanList);
		request.setAttribute("d4p", d4p);
		map.put("city_id", city_id);
		map.put("pageNum", pageNum);
		
		
		return "piaoyoutuijian/piaoyoutuijian";
	}
	
//	public static void main(String[] args){
//		String[] ids = {Const.JIAODIAN_HUAJU+"",Const.JIAODIAN_YANCHANGHUI+"",Const.JIAODIAN_XIQUQUYI+"",Const.JIAODIAN_GUDIAN+"",Const.JIAODIAN_YAOGUN+"",Const.JIAODIAN_SAISHI+"",Const.JIAODIAN_ERTONGQINZI+""};
//		//System.out.println((int)(Math.random()*ids.length)+1);
//		//System.out.println(ids[(int)(Math.random()*ids.length)+1]);
//		for(int i=0;i<100;i++){
//			System.out.println((int)(Math.random()*10)%ids.length);
//			System.out.println(ids[(int)(Math.random()*10)%ids.length]);
//		}
//		
//	}

}
	
