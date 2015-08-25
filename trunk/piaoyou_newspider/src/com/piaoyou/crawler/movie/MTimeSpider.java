package com.piaoyou.crawler.movie;
    
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import net.sf.json.JSONObject;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.piaoyou.bean.CommonInfo;
import com.piaoyou.bean.movie.DateControl;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;
    
public class MTimeSpider extends TicketSpider implements SpiderTask{
	
	private static final String URL = "http://theater.mtime.com/China_Beijing/movie/";
	private final static String BASE_URL = "http://www.mtime.com/";
	private final static String agentID = new DataDao().searchBySQL("select id from agency_info where agency_url=?", BASE_URL)
	.get(0).get("id").toString();
	private static final SimpleDateFormat YEAR_MONTH_DAY = new SimpleDateFormat("yyyy-MM-dd");
	private static final SimpleDateFormat MONTH_DAY = new SimpleDateFormat("yyyy年MM月dd");
	
	
	public Map<String,String> getMainURL(Document doc){
		Map<String,String> url = new HashMap<String,String>(); 
		for(Element element : doc.select("#willReleaseContent .i_willlist")){
			for(Element ele : element.select("ul.clearfix li")){
				Element el = ele.select(".clearfix h3 a").get(0);
				url.put(el.attr("abs:href"), Const.COMMON_STATUS_REPORT+"");
			}
		}
		for(Element element :doc.select("#hotplayRegion li")){
			for(Element ele :element.select(".table")){
				Element el = ele.select(".clearfix h3 a").get(0);
				url.put(el.attr("abs:href"), Const.COMMON_STATUS_HOT+"");
			}
		}
		return url;
	}
	    
	public void parseEach(String url, int status) throws IOException {
		CommonInfo movie = new CommonInfo();
		if (status == Const.COMMON_STATUS_REPORT) { // 即将上映 只抓取基本信息 不抓取价格
			Document doc = getDoc(url);
			String name = doc.select(".ele_inner h1 span.px28").text();
			String imgURL = "";
			if(doc.select("img.movie_film_img").size()>0){
				imgURL = doc.select("img.movie_film_img").get(0).attr("abs:src");
			}
			//上映时间
			List<String> timeList = new ArrayList<String>();
			timeList.add(doc.select("span[property=v:initialReleaseDate]").attr("content"));
			//演出类型
//			movie.setMovie_type(doc.select("ul.lh20 li").get(2).text().replaceAll("类型：", ""));
			for(Element ele :doc.select("ul.movie_film_info_list li")){
				if(ele.text().contains("类型")){
					movie.setMovie_type(ele.text().replaceAll("类型：", ""));
				}else if(ele.text().contains("片长")){
					movie.setTime(ele.text().replaceAll("片长：", "").replaceAll("分钟", ""));
				}
			}
			
			//电影时常
			String time = doc.select("ul.movie_film_info_list li").get(2).text().replaceAll("片长：", "").replaceAll("分钟", "").replaceAll("min", "");
			try{
				Integer.parseInt(time);
			}catch(Exception e){
				time="";
			}
			movie.setTime(time);
			movie.setMainURL(url);
			movie.setName(name);
			movie.setRemote_img_url(imgURL);
			movie.setType(Const.COMMON_INFO_TYPE_MOVIE);
			movie.setStatus(Const.COMMON_STATUS_HOT);
			movie.setStatus(status);
			movie.setAgency_id(Integer.parseInt(agentID));
			movie.setShow_time(timeList);
			movie.setIs_check("1");
			
			
			
		} else if (status == Const.COMMON_STATUS_HOT) { // 正在热映 抓取基本信息和票价
			Document doc = Jsoup.connect(url).timeout(10000).get();
			Element element  =  doc.select(".table").get(0);
			String name = element.select("div div h3 a").text();
			String imgURL = "";
			if(element.select(".t_r .td a img").size()>0){
				imgURL = doc.select(".t_r .td a img").get(0).attr("abs:src");
			}
			String time = element.select("div div p.mt9").text().split(" ")[0];
			time = time.replaceAll(time.replaceAll("\\d", ""), "");
			String movie_type = element.select("div div p.mt9 .c_666").text();
			
			
			String def = PubFun.getRegex("(?i)var\\s+showtimeSv\\s+=\\s+(.*);", doc.html(), 1);
			
			def = def.substring(0,def.indexOf("}")+1);
			JSONObject js = JSONObject.fromObject(def);
			DateControl dc = new DateControl();
			try{
				dc = (DateControl) JSONObject.toBean(js,DateControl.class);
			}catch(Exception e){
				e.printStackTrace();
			}
			String showTime = dc.getDateControl();
			Document doc1 = Jsoup.parse(showTime);
			List<String> timeList = new ArrayList<String>();
			for(Element ele:doc1.select("ul li")){
				showTime  = ele.text();
				try {
					String tmp = showTime.replaceAll("(.*)(\\d{1,2}月\\d{1,2}日)(.*)", "$2");
				    String year = PubFun.getCurrentYear();//获取年份
					tmp = year+"年"+tmp;
					showTime = YEAR_MONTH_DAY.format(MONTH_DAY.parse(tmp));
					timeList.add(showTime);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			doc1 = Jsoup.parse(dc.getMovieShowtimeListControl());
			Set<Integer> price = new TreeSet<Integer>();
			for(Element ele :doc1.select("dd")){
				for(Element e :ele.select(".s_timelist li")){
					if(!e.select("a em").text().replaceAll("￥", "").equals("")){
						price.add(Integer.parseInt(e.select("a em").text().replaceAll("￥", "")));
					}
				}
			}
			String movie_price = "";
			for (Iterator<Integer> iterator = price.iterator(); iterator.hasNext();) {
				int  p = iterator.next();
				movie_price+=p+",";
			}
			
			movie.setMin_price(movie_price.split(",")[0]);
			movie.setMainURL(url);
			movie.setStatus(status);
			movie.setName(name);
			movie.setRemote_img_url(imgURL);
			movie.setShow_time(timeList);
			movie.setPrice(movie_price);
			movie.setMovie_type(movie_type);
			movie.setAgency_id(Integer.parseInt(agentID));
			movie.setTime(time);
			movie.setType(Const.COMMON_INFO_TYPE_MOVIE);
			movie.setStatus(Const.COMMON_STATUS_HOT);
			movie.setIs_check("1");
		} else if (status == Const.COMMON_STATUS_PAST) { // 已过期 抓取基本信息
			
		}
//		System.out.println(movie);
		try {
//			System.out.println(movie);
			this.saveCommonInfo(movie);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void extract(){
		Document doc = getDoc(URL);
		Map<String,String> mainURL = new HashMap<String,String>();
		mainURL = this.getMainURL(doc);
		for (Iterator<String> iterator = mainURL.keySet().iterator(); iterator.hasNext();) {
			String url =  iterator.next();
			int type = Integer.parseInt(mainURL.get(url).toString());
			try{
				this.parseEach(url,type);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	    
	public static void main(String[] args) throws IOException{
		MTimeSpider mtime = new MTimeSpider();
		String url = "http://theater.mtime.com/China_Beijing/movie/104687/";
		mtime.setDao(new DataDao());
		mtime.parseEach(url, 1);
//		mtime.extract();
	}
    
}