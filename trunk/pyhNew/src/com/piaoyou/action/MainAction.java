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

import com.piaoyou.domain.CommonInfo;
import com.piaoyou.service.CommonInfoService;
import com.piaoyou.service.CommonService;
import com.piaoyou.service.MovieService;
import com.piaoyou.util.Const;

@Controller
@RequestMapping(value = "/")
public class MainAction {
	private static final Log log = LogFactory.getLog(MainAction.class);
	@Autowired
	private CommonService commonService;
	@Autowired
	private CommonInfoService commonInfoService;
	@Autowired
	private MovieService movieService;
	@RequestMapping(value = "/test.do", method = RequestMethod.GET)
	public String test(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		log.info("-----------------------------test successfully!");
		return "user/test";
	}
	
	@RequestMapping(value = "/main.do", method = RequestMethod.GET)
	public String main(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String city_id=(request.getParameter("city_id")==null||"".equals(request.getParameter("city_id")))?"1":request.getParameter("city_id");
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("city_id", city_id);
		
		
		//获取北京演出总数
		String beijingTotalCount = commonService.selectObject("commonInfo.getShowTotalCount", 1);
		List<CommonInfo> beijingList = commonInfoService.getCommonInfoList(Const.INDEX_BEIJING_ID, "", "", "1", "5");
		request.setAttribute("beijingTotalCount", beijingTotalCount);
		request.setAttribute("beijingList", beijingList);
		//获取上海演出总数
		String shanghaiTotalCount = commonService.selectObject("commonInfo.getShowTotalCount", 3);
		List<CommonInfo> shanghaiList = commonInfoService.getCommonInfoList(Const.INDEX_SHANGHAI_ID, "", "", "1", "5");
		request.setAttribute("shanghaiList", shanghaiList);
		request.setAttribute("shanghaiTotalCount", shanghaiTotalCount);
		//获取广州演出列表
		String guangzhouTotalCount = commonService.selectObject("commonInfo.getShowTotalCount", 2);
		List<CommonInfo> guangzhouList = commonInfoService.getCommonInfoList(Const.INDEX_GUANGZHOU_ID, "", "", "1", "5");
		request.setAttribute("guangzhouList", guangzhouList);
		request.setAttribute("guangzhouTotalCount", guangzhouTotalCount);
		
		
		//二人世界
		String pageIndex = "1";
		String pageCount = "5";
		List<CommonInfo> errenshijieList = commonInfoService.getCommonInfoList(Const.INDEX_ERRENSHIJIE_ID, "weight",city_id, pageIndex, pageCount);
		//寻开心
		List<CommonInfo> xunkaixinList = commonInfoService.getCommonInfoList(Const.INDEX_XUNKAIXIN_ID,"weight", city_id, pageIndex, pageCount);
		//打发时光
		List<CommonInfo> dafashiguangList = commonInfoService.getCommonInfoList(Const.INDEX_DAFASHIGUANG_ID,"weight", city_id, pageIndex, pageCount);
		//释放吧，少年
		List<CommonInfo> shifangList = commonInfoService.getCommonInfoList(Const.INDEX_SHIFANG_ID,"weight", city_id, pageIndex, pageCount);
		//今夜有戏
		List<CommonInfo> jinyeyouxiList = commonInfoService.getCommonInfoList(Const.INDEX_JINYEYOUXI_ID, "weight",city_id, pageIndex, pageCount);
		//本周精品
		List<CommonInfo> benzhoujingpinList = commonInfoService.getCommonInfoList(Const.INDEX_BENZHOUJINGPIN_ID, "weight",city_id, pageIndex, pageCount);
		
		//话剧
		pageIndex = "1";
		pageCount = "3";
		List<CommonInfo> huajuList = commonInfoService.getCommonInfoList(Const.INDEX_HUAJU_ID, "weight",city_id, pageIndex, pageCount);
		CommonInfo huajuIndex =new CommonInfo() ;
		if(huajuList!=null&&huajuList.size()>0){
			huajuIndex  = huajuList.get(0);
			huajuList.remove(0);
		}
		//演唱会
		List<CommonInfo> yanchanghuiList = commonInfoService.getCommonInfoList(Const.INDEX_YANCHANGHUI_ID,"weight", city_id, pageIndex, pageCount);
		CommonInfo yanchanghuiIndex = new CommonInfo();
		if(yanchanghuiList!=null&&yanchanghuiList.size()>0){
			yanchanghuiIndex= yanchanghuiList.get(0);
			yanchanghuiList.remove(0);
		}
		//戏剧曲艺
		pageIndex = "1";
		pageCount = "8";
		List<CommonInfo> xijuqiyuList = commonInfoService.getCommonInfoList(Const.INDEX_XIJUQUYI_ID, "weight",city_id, pageIndex, pageCount);
		//古典
		pageIndex = "1";
		pageCount = "3";
		List<CommonInfo> gudianList = commonInfoService.getCommonInfoList(Const.INDEX_GUDDIAN_ID,"weight", city_id, pageIndex, pageCount);
		CommonInfo gudianIndex = new CommonInfo() ;
		if(gudianList!=null&&gudianList.size()>0){
			gudianIndex = gudianList.get(0);
			gudianList.remove(0);
		}
	
		//摇滚乐
		pageIndex = "1";
		pageCount = "4";
		List<CommonInfo> yaogunyueList = commonInfoService.getCommonInfoList(Const.INDEX_YAOGUNYUE_ID, "weight",city_id, pageIndex, pageCount);
		CommonInfo yaogunyueIndex = new CommonInfo();
		if(yaogunyueList!=null&&yaogunyueList.size()>0){
			yaogunyueIndex  = yaogunyueList.get(0);
			yaogunyueList.remove(0);
		}
		
		//赛事
		pageIndex = "1";
		pageCount = "1";
		List<CommonInfo> saishiList = commonInfoService.getCommonInfoList(Const.INDEX_SAISHI_ID,"weight", city_id, pageIndex, pageCount);
		CommonInfo saishiIndex = new CommonInfo();
		if(saishiList!=null&&saishiList.size()>0){
			 saishiIndex = saishiList.get(0);
		}
		
		//儿童亲子
		pageIndex = "1";
		pageCount = "3";
		List<CommonInfo> ertongqinziList = commonInfoService.getCommonInfoList(Const.INDEX_ERTONGQINZI_ID,"weight", city_id, pageIndex, pageCount);
		CommonInfo ertongqinziIndex = new CommonInfo();
		if(ertongqinziList!=null&&ertongqinziList.size()>0){
			ertongqinziIndex  = ertongqinziList.get(0);
			ertongqinziList.remove(0);
		}
		
		//影讯
		pageIndex = "1";
		pageCount = "8";
		params = new HashMap<Object,Object>();
		params.put("pageIndex", (Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageCount));
		params.put("pageCount", Integer.parseInt(pageCount));
		List<CommonInfo> yingxunList = movieService.getCommonInfoList(Const.INDEX_YINGXUN_ID, city_id, pageIndex, pageCount);
		request.setAttribute("errenshijieList", errenshijieList);
		request.setAttribute("category_errenshijie_id", Const.INDEX_ERRENSHIJIE_ID);
		request.setAttribute("xunkaixinList", xunkaixinList);
		request.setAttribute("category_xunkaixin_id", Const.INDEX_XUNKAIXIN_ID);
		request.setAttribute("dafashiguangList", dafashiguangList);
		request.setAttribute("category_dafashiguang_id", Const.INDEX_DAFASHIGUANG_ID);
		request.setAttribute("shifangList", shifangList);
		request.setAttribute("category_shifang_id", Const.INDEX_SHIFANG_ID);
		request.setAttribute("jinyeyouxiList", jinyeyouxiList);
		request.setAttribute("category_jinyeyouxi_id", Const.INDEX_JINYEYOUXI_ID);
		request.setAttribute("benzhoujingpinList", benzhoujingpinList);
		request.setAttribute("category_benzhoujingpin_id", Const.INDEX_BENZHOUJINGPIN_ID);
		request.setAttribute("huajuList", huajuList);
		request.setAttribute("huajuIndex", huajuIndex);
		request.setAttribute("yanchanghuiList", yanchanghuiList);
		request.setAttribute("yanchanghuiIndex", yanchanghuiIndex);
		request.setAttribute("xijuqiyuList", xijuqiyuList);
		request.setAttribute("gudianList", gudianList);
		request.setAttribute("gudianIndex", gudianIndex);
		request.setAttribute("yaogunyueList", yaogunyueList);
		request.setAttribute("yaogunyueIndex", yaogunyueIndex);
		request.setAttribute("saishiList", saishiList);
		request.setAttribute("saishiIndex", saishiIndex);
		request.setAttribute("ertongqinziList", ertongqinziList);
		request.setAttribute("ertongqinziIndex", ertongqinziIndex);
		request.setAttribute("yingxunList", yingxunList);
		request.setAttribute("city_id", city_id);
		return "main";
	}
	
}
