package com.piaoyou.action;

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
import com.piaoyou.domain.FocusPhoto;
import com.piaoyou.service.CommonInfoService;
import com.piaoyou.service.CommonService;
import com.piaoyou.util.Const;
import com.piaoyou.util.Division4Page;

@Controller
@RequestMapping(value = "/douzaikan")
public class DouzaikanAction {
	@Autowired
	private CommonService commonService;
	@Autowired
	private CommonInfoService commonInfoService;
	private Division4Page d4p;

	@RequestMapping(value = "/douzaikan.do", method = RequestMethod.GET)
	public String douzaikan(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		int pageCount = 25;
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
		
		params.put("pageIndex", pageIndex);
		params.put("pageCount", pageCount);
		params.put("order", "recommend_count");
		List<CommonInfo> doukanList = this.commonService.selectObjects("commonInfo.getDouzaikan", params);
		String count = this.commonService.selectObject("commonInfo.getDouzaikanCount", params);
		d4p = new Division4Page(pageNum+"", Integer.parseInt(count), pageCount);
		d4p.setList(doukanList);
		map.put("doukanList", doukanList);
		request.setAttribute("d4p", d4p);
		map.put("city_id", city_id);
		map.put("pageNum", pageNum);
		map.put("category_id", category_id);
		
		//焦点图
		String[] ids = {Const.JIAODIAN_HUAJU+"",Const.JIAODIAN_YANCHANGHUI+"",Const.JIAODIAN_XIQUQUYI+"",Const.JIAODIAN_GUDIAN+"",Const.JIAODIAN_YAOGUN+"",Const.JIAODIAN_SAISHI+"",Const.JIAODIAN_ERTONGQINZI+""};
		if(category_id==null || "".equals(category_id)){
			category_id=ids[(int)(Math.random()*10)%ids.length];
		}
		params.put("category_id", category_id);
		params.put("pageIndex", 0);
		params.put("pageCount", Const.JIAODIAN_PAGECOUNT);
		List<FocusPhoto> listFocus = this.commonService.selectObjects("focusPhoto.getListByCId", params);
		request.setAttribute("listFocus", listFocus);
		
		return "douzaikan/douzaikan";
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
	
