package com.piaoyou.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.piaoyou.domain.Category;
import com.piaoyou.domain.CommonComment;
import com.piaoyou.domain.CommonInfo;
import com.piaoyou.domain.CommonStat;
import com.piaoyou.domain.ContentRemark;
import com.piaoyou.domain.FocusPhoto;
import com.piaoyou.domain.News;
import com.piaoyou.domain.RemarkCollect;
import com.piaoyou.domain.RemarkRecommend;
import com.piaoyou.domain.RemarkReply;
import com.piaoyou.domain.User;
import com.piaoyou.domain.UserRecommend;
import com.piaoyou.service.CommonInfoService;
import com.piaoyou.service.CommonService;
import com.piaoyou.util.Const;
import com.piaoyou.util.Division4Page;
import com.piaoyou.util.ImgPathUtil;

@Controller
@RequestMapping(value = "/otherpl")
public class OtherplAction {
	@Autowired
	private CommonService commonService;
	@Autowired
	private CommonInfoService commonInfoService;
	private Division4Page d4p;

	@RequestMapping(value = "/otherpl.do", method = RequestMethod.GET)
	public String saishi(HttpServletRequest request,HttpServletResponse response){
		String user_id=request.getParameter("user_id");
		User user=commonService.selectObject("user.getUserInfoByUserId", Integer.parseInt(user_id));
		if(user.getUser_portrait()!=null){
			request.setAttribute("headPath", Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, user.getUser_id())+"50x50_"+user.getUser_portrait());
    	}else{
    		//设置默认的图像路径
    		String path=request.getContextPath();
    		request.setAttribute("headPath", path+"/public/images/head_default3.png");
    	}
		Map<Object,Object> map=new HashMap<Object,Object>();
		map.put("user_id", user_id);
		//用户的前三条影评
		List<ContentRemark> contentList=commonService.selectObjects("remark.selectContentRemarkByUserIdThree", map);
		
		
		for(int i=0;i<contentList.size();i++){
			if(contentList.get(i).getContent()!=null){
				contentList.get(i).setContent(Jsoup.parse(contentList.get(i).getContent()).text());
			}
		}
		
		//查询前3条推荐
		List<UserRecommend> recommendList=commonService.selectObjects("common.getUserRecommdByUserId3", map);
		//查询用户的前3条评论
		List<CommonComment> commentList=commonService.selectObjects("remark.getCommentByUserId3", map);
		//查看用户前三条剧评收藏
		List<RemarkCollect> collectList = commonService.selectObjects("common.getRemarkCollectList", map);
		//用户信息统计
		CommonStat stat=commonService.selectObject("common.selectUserInfoByUserId", user.getUser_id());
		user.setStat(stat);
		//用户标签
		List<Map<String,String>> listtars = commonService.selectObjects("user.getMyTags",map);
		user.setUser_tags(listtars);
		
		request.setAttribute("contentList", contentList);
		request.setAttribute("recommendList", recommendList);
		request.setAttribute("collectList", collectList);
		request.setAttribute("commentList", commentList);
		request.setAttribute("user2", user);
		if(contentList.size()>0){
			request.setAttribute("remarkComment", contentList.get(0));
			
			
			Map<Object,Object> params = new HashMap<Object,Object>();
			int id = contentList.get(0).getId();
			params.put("remark_id", id);
			params.put("pageIndex", 0);
			params.put("pageCount", 3);
			List<RemarkRecommend> remarkRecommendList = commonService.selectObjects("common.getRemarkRecommendList", params);
			String remarkRecommendTotalCount = commonService.selectObject("common.getRemarkRecommendCount", params);
			for(int i=0;i<remarkRecommendList.size();i++){
				if(remarkRecommendList.get(i).getUser_portrait()!=null){
					remarkRecommendList.get(i).setUser_portrait(Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, remarkRecommendList.get(i).getUser_id())+"30x30_"+remarkRecommendList.get(i).getUser_portrait());
		    	}else{
		    		//设置默认的图像路径
		    		String path=request.getContextPath();
		    		remarkRecommendList.get(i).setUser_portrait(path+"/public/images/head_default3.png");
		    	}
			}
			request.setAttribute("remarkRecommendList", remarkRecommendList);
			request.setAttribute("remarkRecommendTotalCount", remarkRecommendTotalCount);
			
			params = new HashMap<Object,Object>();
			params.put("remark_id", id);
			params.put("pageIndex", 0);
			params.put("pageCount", 3);
			String totalCount  =  commonService.selectObject("remark.getRemarkCommentsCount", params);
		    List<RemarkReply>	rrList=commonService.selectObjects("remark.getRemarkComments", params);
		    for(RemarkReply rr:rrList){
		    	if(rr.getUser_portrait()!=null){
		    		rr.setUser_portrait(Const.RETURN_IMG_PATH+ImgPathUtil.getImgPath(Const.IMG_HEAD, rr.getUser_id())+"50x50_"+rr.getUser_portrait());
		    	}else{
		    		//设置默认的图像路径
		    		String path=request.getContextPath();
		    		rr.setUser_portrait(path+"/public/images/head_default3.png");
		    	}
		    }
		    
		    request.setAttribute("rrList", rrList);
		    request.setAttribute("totalCount", totalCount);
		}
		return "otherpl/otherpl";
	}
}
