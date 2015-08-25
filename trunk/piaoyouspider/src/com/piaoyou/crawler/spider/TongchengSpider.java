package com.piaoyou.crawler.spider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.PubFun;

public class TongchengSpider extends TicketSpider implements SpiderTask {
	
	private static final String BASE_URL = "http://piao.17u.cn/";
	private static final String URL1 = "http://piao.17u.cn";
	private static final String URL = "http://piao.17u.cn/all/list.html";
	private static final String AJAX = "http://piao.17u.cn/HttpHandlers/DPHandler.ashx";
	private static final Class<TongchengSpider> BASE_CLASS=TongchengSpider.class;
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();
	private static final SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Override
	public void extract() {
		Set<String> set = new HashSet<String>();
		set = getURL();
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String url = (String) iterator.next();
			try {
				parseEach(url);
			} catch (Exception e) {
				LogFactory.getLog(BASE_CLASS).error(url);
			}
		}
	}
	public Set<String> getURL(){
		Set<String> set = new HashSet<String>();
		String url  = URL;
		Document document = getDoc(URL);
		 for(Element ele :document.select("#search_list li.sc_item a.sc_piao_pic")){
			     set.add(ele.attr("abs:href"));
		 }
		while(document.select("div.page_link a:contains(下一页)").size()>0){
			  url =document.select("div.page_link a:contains(下一页)").attr("onclick");
			  url = url.replaceAll("window.location='", "").replaceAll("'\\;return false","");
			  url = url.substring(0,url.length()-1);
			  url=URL1+url;
			  document = getDoc(url);
			  for(Element ele :document.select("#search_list li.sc_item a.sc_piao_pic")){
				     set.add(ele.attr("abs:href"));
			  }
		}
		return set;
	}
	public void parseEach(String url) throws  Exception{
			Show show = new Show();
			Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
			List<TicketPrice> list = null;
			TicketPrice ticket= null;
			Document document = getDoc(url);
			show.setAgent_id(agentID);
			show.setImage_path(document.select("div.merchandise_left img").attr("src"));
			show.setName(document.select("h1.merchandise_title").text());
			String siteName = document.select("span.sp_right:has(a)").text();
			if(siteName.contains("待定")){
				return;
			}
			show.setSiteName(siteName);
			show.setType(getShowType(document.select("div.rt_nav a").get(2).text()));
			show.setIntroduction(PubFun.cleanElement(document.select("div.acting_play_se")).html());
			String showTime = "";
			if(document.select("#dateList a").size()==0){
				showTime = "all";
				list = new ArrayList<TicketPrice>();
				for(Element ele1 :document.select("#select_price_list")){
					for(Element ele2 : ele1.select("a.sty_06")){
						ticket = new TicketPrice();
						ticket.setPrice(ele2.select("p").get(0).text().replaceAll("¥", ""));
						ticket.setRemark(ele2.select("p").get(1).text());
						ticket.setExist(true);
						ticket.setMainURL(url);
						ticket.setDetailURL(url);
						list.add(ticket);
					}
					for(Element ele2 :ele1.select("a.sty_03")){
						ticket = new TicketPrice();
						ticket.setPrice(ele2.select("p").get(0).text().replaceAll("¥", ""));
						ticket.setRemark(ele2.select("p").get(1).text());
						ticket.setExist(false);
						ticket.setMainURL(url);
						ticket.setDetailURL(url);
						list.add(ticket);
					}
				}
				timeAndPrice.put(showTime, list);
			}else if(document.select("#dateList a").size()==1){
				showTime = document.select("#dateList").text();
				showTime= new SimpleDateFormat("yyyy").format(new Date())+"-"+showTime.replaceAll("[月]", "-").replaceAll("[日 星期\\(一|二|三|四|五|六|日\\)+]", " ").replaceAll(" +", " ");
				try {
					showTime =newDate.format(newDate.parse(showTime));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				list = new ArrayList<TicketPrice>();
				for(Element ele1 :document.select("#select_price_list")){
					for(Element ele2 : ele1.select("a.sty_06")){
						ticket = new TicketPrice();
						ticket.setPrice(ele2.select("p").get(0).text().replaceAll("¥", ""));
						ticket.setRemark(ele2.select("p").get(1).text());
						ticket.setExist(true);
						ticket.setMainURL(url);
						ticket.setDetailURL(url);
						list.add(ticket);
					}
					for(Element ele2 :ele1.select("a.sty_03")){
						ticket = new TicketPrice();
						ticket.setPrice(ele2.select("p").get(0).text().replaceAll("¥", ""));
						ticket.setRemark(ele2.select("p").get(1).text());
						ticket.setExist(false);
						ticket.setMainURL(url);
						ticket.setDetailURL(url);
						list.add(ticket);
					}
				}
				timeAndPrice.put(showTime, list);
			} else {
				for (Element ele1 : document.select("#dateList a")) {
					showTime = ele1.attr("date");
					String tid = ele1.attr("tid");
					list = new ArrayList<TicketPrice>();
					Document doc = Jsoup.connect(AJAX).timeout(1000 * 10).data(
							"action", "GetPriceByDate").data("date", showTime)
							.data("tid", tid).post();
					showTime = showTime.replaceAll("\\.", "-");
					showTime=showTime.replaceAll(" +", " ");
					try {
						showTime =newDate.format(newDate.parse(showTime));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					for (Element ele : doc.select("a.sty_03")) {
						if (ele.select("p").size() == 2) {
							ticket = new TicketPrice();
							ticket.setMainURL(url);
							ticket.setPrice(ele.select("p").get(0).text().replaceAll("¥", ""));
							ticket.setRemark(ele.select("p").get(1).text().replaceAll("[（）()]", ""));
							ticket.setExist(false);
							ticket.setDetailURL(url);
							list.add(ticket);
						}
					}
					for (Element ele : doc.select("a.sty_06")) {
						if (ele.select("p").size() == 2) {
							ticket = new TicketPrice();
							ticket.setMainURL(url);
							ticket.setPrice(ele.select("p").get(0).text().replaceAll("¥", ""));
							ticket.setRemark(ele.select("p").get(1).text().replaceAll("[（）()]", ""));
							ticket.setExist(true);
							ticket.setDetailURL(url);
							list.add(ticket);
						}
					}
					timeAndPrice.put(showTime,list);
				}
			}
			show.setTimeAndPrice(timeAndPrice);
			getDao().saveShow(show);
	}
	
	int getShowType(String type){
		//全部	演唱会 音乐会 戏曲曲艺 舞蹈芭蕾 魔术马戏 话剧歌剧 电影通票 亲子家庭 体育赛事
		int showType = 0;
		if("演唱会".equals(type)){
			 showType = 1;
		}else if("音乐会".equals(type)){
			showType = 2 ;
		}else if("戏曲曲艺".equals(type)){
			showType = 5 ;
		}else if("芭蕾舞蹈".equals(type)){
			showType = 4 ;
		}else if("魔术马戏".equals(type)){
			showType = 5 ;
		}else if("歌剧话剧".equals(type)){
			showType = 3 ;
		}else if("电影通票".equals(type)){
			showType = 8 ;
		}else if("亲子家庭".equals(type)){
			showType = 6 ;
		}else if("体育赛事".equals(type)){
			showType = 7 ;
		}else {
			showType = ShowType.OTHER;
		}
		return showType;
	}
	public static void main(String args[]){
		  TongchengSpider tongcheng = new TongchengSpider();
		  tongcheng.setDao(new DataDao());
		  tongcheng.extract();
	}
}
