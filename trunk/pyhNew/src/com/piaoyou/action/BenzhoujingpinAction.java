package com.piaoyou.action;

import java.text.SimpleDateFormat;
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
import com.piaoyou.domain.UserCollection;
import com.piaoyou.domain.UserRecommend;
import com.piaoyou.service.CommonInfoService;
import com.piaoyou.service.CommonService;
import com.piaoyou.util.Const;
import com.piaoyou.util.Division4Page;
import com.piaoyou.util.ImgPathUtil;

@Controller
@RequestMapping(value = "/benzhoujingpin")
public class BenzhoujingpinAction {
	private static final Log log = LogFactory.getLog(BenzhoujingpinAction.class);
	private Division4Page d4p;
	@Autowired
	private CommonService commonService;
	@Autowired
	private CommonInfoService commonInfoService;
	
	private static final SimpleDateFormat YYYY_MM_DD_HH_MM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	@RequestMapping(value = "/test.do", method = RequestMethod.GET)
	public String test(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		log.info("-----------------------------test successfully!");
		return "user/test";
	}
	
	@RequestMapping(value = "/benzhoujingpin.do", method = RequestMethod.GET)
	public String benzhoujingpin(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String city_id=(request.getParameter("city_id")==null||"".equals(request.getParameter("city_id")))?"1":request.getParameter("city_id");
		String pageIndex = "1";
		String pageCount = "3";
		List<CommonInfo> jinrishangyanList = commonInfoService.getCommonInfoList(Const.QINGGAN_BENZHOUJINGPIN_JINRISHANGYAN,"", city_id, pageIndex, pageCount);
		String jinrishangyanTotalCount = commonInfoService.getCommonTotalCount(Const.QINGGAN_BENZHOUJINGPIN_JINRISHANGYAN, city_id);
		List<CommonInfo> ranqingzhoumoList = commonInfoService.getCommonInfoList(Const.QINGGAN_BENZHOUJINGPIN_RANQINGZHOUMO, "",city_id, pageIndex, pageCount);
		String ranqingzhoumoTotalCount = commonInfoService.getCommonTotalCount(Const.QINGGAN_BENZHOUJINGPIN_RANQINGZHOUMO, city_id);
		List<CommonInfo> benzhoujingpinList = commonInfoService.getCommonInfoList(Const.QINGGAN_BENZHOUJINGPIN_BENZHOUJINGPIN,"", city_id, pageIndex, pageCount);
		String benzhoujingpinTotalCount = commonInfoService.getCommonTotalCount(Const.QINGGAN_BENZHOUJINGPIN_BENZHOUJINGPIN, city_id);
		List<CommonInfo> jijiangshangyanList = commonInfoService.getCommonInfoList(Const.QINGGAN_BENZHOUJINGPIN_JIJIANGSHANGYAN,"", city_id, pageIndex, pageCount);
		String jijiangshangyuanTotalCount = commonInfoService.getCommonTotalCount(Const.QINGGAN_BENZHOUJINGPIN_JIJIANGSHANGYAN, city_id);
		request.setAttribute("jinrishangyanList", jinrishangyanList);
		request.setAttribute("jinrishangyanTotalCount", jinrishangyanTotalCount);
		request.setAttribute("ranqingzhoumoList", ranqingzhoumoList);
		request.setAttribute("ranqingzhoumoTotalCount", ranqingzhoumoTotalCount);
		request.setAttribute("benzhoujingpinList", benzhoujingpinList);
		request.setAttribute("benzhoujingpinTotalCount", benzhoujingpinTotalCount);
		request.setAttribute("jijiangshangyanList", jijiangshangyanList);
		request.setAttribute("jijiangshangyuanTotalCount", jijiangshangyuanTotalCount);
		request.setAttribute("jinrishangyan_id",Const.QINGGAN_BENZHOUJINGPIN_JINRISHANGYAN);
		request.setAttribute("ranqingzhoumo_id",Const.QINGGAN_BENZHOUJINGPIN_RANQINGZHOUMO);
		request.setAttribute("benzhoujingpin_id",Const.QINGGAN_BENZHOUJINGPIN_BENZHOUJINGPIN );
		request.setAttribute("jijiangshangyan_id",Const.QINGGAN_BENZHOUJINGPIN_JIJIANGSHANGYAN);
		request.setAttribute("city_id",city_id );
		
		//焦点图
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("category_id", Const.JIAODIAN_BENZHOUJINGPIN);
		params.put("pageIndex", 0);
		params.put("pageCount", Const.JIAODIAN_PAGECOUNT);
		List<FocusPhoto> listFocus = this.commonService.selectObjects("focusPhoto.getListByCId", params);
		request.setAttribute("listFocus", listFocus);
		return "benzhoujingpin/benzhoujingpin";
	}
	@RequestMapping(value = "/benzhoujingpinsub.do", method = RequestMethod.GET)
	public String benzhoujingpinsub(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String city_id=(request.getParameter("city_id")==null||"".equals(request.getParameter("city_id")))?"1":request.getParameter("city_id");
		int category_id=(request.getParameter("category_id")==null||"".equals(request.getParameter("category_id")))?1:Integer.parseInt(request.getParameter("category_id"));
		String pageNum=(request.getParameter("pageNum")==null||"".equals(request.getParameter("pageNum")))?"1":request.getParameter("pageNum");
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("city_id", city_id);
		params.put("id", category_id);
		Category category = this.commonService.selectObject("category.getById", params);
		String pageCount = "10";
		List<CommonInfo> qinggansubList = commonInfoService.getCommonInfoList(category_id, "",city_id, pageNum, pageCount);
		String totalCount = commonInfoService.getCommonTotalCount(category_id, city_id);
		d4p = new Division4Page(pageNum+"", Integer.parseInt(totalCount), Integer.parseInt(pageCount));
		d4p.setList(qinggansubList);
		request.setAttribute("qinggansubList", qinggansubList);
		request.setAttribute("city_id", city_id);
		request.setAttribute("category", category);
		request.setAttribute("totalCount", totalCount);
		request.setAttribute("d4p", d4p);
		return "benzhoujingpin/benzhoujingpinsub";
	}
	@RequestMapping(value = "/commonInfoDetail.do", method = RequestMethod.GET)
	public String commonInfoDetail(HttpSession hs,HttpServletRequest request,HttpServletResponse response){
		String id=(request.getParameter("id")==null||"".equals(request.getParameter("id")))?"1":request.getParameter("id");
		CommonInfo commonInfo = commonInfoService.getCommonInfoDetail(Integer.parseInt(id));
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
		User user = (User)hs.getAttribute("user");
		if(user!=null&&commonInfo!=null){
			commonInfoService.insertHistoty(user.getUser_id(), commonInfo.getId());
		}
		
		List<CommonInfo> recommendList = commonInfoService.getCommonInfoList(Const.INDEX_BENZHOUJINGPIN_ID, "",commonInfo.getCity_id(), "1", "5");
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
		return "benzhoujingpin/commonInfoDetail";
	}
}
