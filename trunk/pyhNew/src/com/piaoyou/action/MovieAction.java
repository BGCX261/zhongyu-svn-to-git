package com.piaoyou.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.piaoyou.domain.Category;
import com.piaoyou.domain.CommonInfo;
import com.piaoyou.domain.FocusPhoto;
import com.piaoyou.service.CommonInfoService;
import com.piaoyou.service.CommonService;
import com.piaoyou.service.MovieService;
import com.piaoyou.util.Const;
import com.piaoyou.util.Division4Page;

@Controller
@RequestMapping(value = "/movie")
public class MovieAction {
	@Autowired
	private CommonService commonService;
	@Autowired
	private MovieService movieService;
	private Division4Page d4p;

	@RequestMapping(value = "/movie.do", method = RequestMethod.GET)
	public String movie(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		String city_id=(request.getParameter("city_id")==null||"".equals(request.getParameter("city_id")))?"1":request.getParameter("city_id");
		Map<Object,Object> params = new HashMap<Object,Object>();
		//正在热映
		List<CommonInfo> zhengzaireyingList = movieService.getCommonInfoList(Const.MOVIE_ZHENGZAIREYING, "weight", "1", "10");
		String zhengzaireyingtotal =movieService.getCommonTotalCount(Const.MOVIE_ZHENGZAIREYING);
		map.put("zhengzaireyingList", zhengzaireyingList);
		map.put("zhengzaireyingtotal", zhengzaireyingtotal);
		
		//即将上映
		List<CommonInfo> jijiangshangyingList = movieService.getCommonInfoList(Const.MOVIE_JIJIANGSHANGYING, "weight", "1", "10");
		String jijiangshangyingtotal = movieService.getCommonTotalCount(Const.MOVIE_JIJIANGSHANGYING);
		map.put("jijiangshangyingList", jijiangshangyingList);
		map.put("jijiangshangyingtotal", jijiangshangyingtotal);
		
		//新片热评
		List<CommonInfo> xinpianList = movieService.getCommonInfoList(Const.MOVIE_XINPIANREPING, "weight", "1", "10");
		String xinpiantotal = movieService.getCommonTotalCount(Const.MOVIE_XINPIANREPING);
		map.put("xinpianList", xinpianList);
		map.put("xinpiantotal", xinpiantotal);
		
		//焦点图
		params.put("category_id", Const.JIAODIAN_MOVIE);
		params.put("pageIndex", 0);
		params.put("pageCount", Const.JIAODIAN_PAGECOUNT);
		List<FocusPhoto> listFocus = this.commonService.selectObjects("focusPhoto.getListByCId", params);
		request.setAttribute("listFocus", listFocus);
		map.put("city_id", city_id);
		return "movie/movie";
	}
	
	@RequestMapping(value = "/movieCategory.do", method = RequestMethod.GET)
	public String ertongqinziCategory(HttpServletRequest request,HttpServletResponse response,ModelMap map){
		String category_id = request.getParameter("category_id");
		String city_id=(request.getParameter("city_id")==null||"".equals(request.getParameter("city_id")))?"1":request.getParameter("city_id");
		String pageNum=(request.getParameter("pageNum")==null||"".equals(request.getParameter("pageNum")))?"1":request.getParameter("pageNum");
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("city_id", city_id);
		params.put("id", category_id);
		Category c = this.commonService.selectObject("category.getById", params);
		map.put("c", c);
		String pageCount = "15";
		List<CommonInfo> list = movieService.getCommonInfoList(Integer.parseInt(category_id), city_id, pageNum, pageCount);
		String total = movieService.getCommonTotalCount(Integer.parseInt(category_id));
		d4p = new Division4Page(pageNum+"", Integer.parseInt(total), Integer.parseInt(pageCount));
		d4p.setList(list);
		map.put("d4p", d4p);
		map.put("total", total);
		map.put("list", list);
		map.put("city_id", city_id);
		map.put("category_id", category_id);
		//焦点图
		params.put("category_id", category_id);
		params.put("pageIndex", 0);
		params.put("pageCount", Const.JIAODIAN_PAGECOUNT);
		List<FocusPhoto> listFocus = this.commonService.selectObjects("focusPhoto.getListByCId", params);
		request.setAttribute("listFocus", listFocus);
		return "movie/moviesub";
	}
}
