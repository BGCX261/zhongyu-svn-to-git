package com.piaoyou.crawler.spider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;


public class ChinaticketSpider extends TicketSpider implements SpiderTask {
	private static final String BASE_URL ="http://www.chinaticket.com/";
	
	private static final Class<ChinaticketSpider> BASE_CLASS=ChinaticketSpider.class;
	
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();
	
	
	private static final SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@Override
	public void extract() {
		Map<String,Integer> mainURL  = new HashMap<String,Integer>();
		mainURL = getURL();
		Map<String,Integer> detailURL = new HashMap<String,Integer>();
		detailURL = parseMainURL(mainURL);
		for (Iterator<String> iterator = detailURL.keySet().iterator(); iterator.hasNext();) {
			String url = (String) iterator.next();
			try {
				parseEach(url,detailURL.get(url));
			} catch (Exception e) {
				LogFactory.getLog(BASE_CLASS).error(Const.concat(url,detailURL.get(url)),e);
			}
		}
//		try {
//			parseEach("", 1);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	public Map<String,Integer> getURL(){
		Map<String,Integer> map = new HashMap<String,Integer>();
		Document document  = getDoc(BASE_URL);
		for (Element element : document.select("div.main-div #catelist a")) {
			String showType = element.text();
			if ("演唱会".equals(showType)||"HOT!2011下半年热门演唱会推荐".equals(showType)) {
				// 获取该页中的连接地址 包括翻页
				String url1 = element.attr("abs:href");
				map.put(url1, ShowType.CONCERT);
				Document doc = getDoc(url1);
				while (doc.select("div.pagenavi a:contains(下一页)").size() > 0) {
					String url2 = doc.select("div.pagenavi a:contains(下一页)").first().attr("abs:href");
					map.put(url2, ShowType.CONCERT);
					doc = getDoc(url2);
				}
			} else if ("音乐会".equals(showType) || "国家大剧院系列演出".equals(showType)
					|| "2011北京音乐厅 音乐会歌剧系列".equals(showType)
					|| "2011年国家大剧院钢琴系列音乐会".equals(showType)
					||"国家大剧院庆“七一”系列音乐会".equals(showType)) {
				// 获取该页中的连接地址 包括翻页
				String url1 = element.attr("abs:href");
				map.put(url1, ShowType.SHOW);
				Document doc = getDoc(url1);
				while (doc.select("div.pagenavi a:contains(下一页)").size() > 0) {
					String url2 = doc.select("div.pagenavi a:contains(下一页)").first().attr("abs:href");
					map.put(url2, ShowType.SHOW);
					doc = getDoc(url2);
				}
			} else if ("话剧".equals(showType) || "歌剧".equals(showType)
					|| "音乐剧".equals(showType) || "综艺节目".equals(showType)
					|| "舞台剧".equals(showType) || "首都剧场系列演出".equals(showType)
					|| "2011年国家大剧院歌剧节".equals(showType)
					|| "2011年国家大剧院话剧系列演出".equals(showType)
					|| "国家大剧院“庆祝建党90周年”系列演出".equals(showType)
					||"2011第二届南锣鼓巷戏剧节".equals(showType)
					||"2011暑期档热门演出推荐".equals(showType)||"全国戏剧文化奖·2011小剧场优秀戏剧展".equals(showType)) {
				// 获取该页中的连接地址 包括翻页
				String url1 = element.attr("abs:href");
				map.put(url1, ShowType.DRAMA);
				Document doc = getDoc(url1);
				while (doc.select("div.pagenavi a:contains(下一页)").size() > 0) {
					String url2 = doc.select("div.pagenavi a:contains(下一页)").first().attr("abs:href");
					map.put(url2, ShowType.DRAMA);
					doc = getDoc(url2);
				}
			} else if ("舞剧".equals(showType) || "芭蕾舞".equals(showType)) {
				// 获取该页中的连接地址 包括翻页
				String url1 = element.attr("abs:href");
				map.put(url1, ShowType.DANCE);
				Document doc = getDoc(url1);
				while (doc.select("div.pagenavi a:contains(下一页)").size() > 0) {
					String url2 = doc.select("div.pagenavi a:contains(下一页)").first().attr("abs:href");
					map.put(url2, ShowType.DANCE);
					doc = getDoc(url2);
				}
			} else if ("相声/小品".equals(showType) || "杂技".equals(showType)
					|| "马戏".equals(showType) || "魔术".equals(showType)
					|| "梅兰芳大剧院系列演出".equals(showType)
					|| "2011年国家大剧院五大地方戏展演".equals(showType)) {
				// 获取该页中的连接地址 包括翻页
				String url1 = element.attr("abs:href");
				map.put(url1, ShowType.OPERA);
				Document doc = getDoc(url1);
				while (doc.select("div.pagenavi a:contains(下一页)").size() > 0) {
					String url2 = doc.select("div.pagenavi a:contains(下一页)").first().attr("abs:href");
					map.put(url2, ShowType.OPERA);
					doc = getDoc(url2);
				}
			} else if ("亲子家庭".equals(showType)
					|| "2011年国家大剧院儿童档演出".equals(showType)||"六一儿童节巨献大型亲子剧".equals(showType)) {
				// 获取该页中的连接地址 包括翻页
				String url1 = element.attr("abs:href");
				map.put(url1, ShowType.CHILDREN);
				Document doc = getDoc(url1);
				while (doc.select("div.pagenavi a:contains(下一页)").size() > 0) {
					String url2 = doc.select("div.pagenavi a:contains(下一页)").first().attr("abs:href");
					map.put(url2, ShowType.CHILDREN);
					doc = getDoc(url2);
				}
			} else if ("网球".equals(showType) || "足球".equals(showType)
					|| "斯诺克".equals(showType) || "乒乓球".equals(showType)
					|| "排球".equals(showType) || "自行车".equals(showType)
					|| "游泳".equals(showType)) {
				// 获取该页中的连接地址 包括翻页
				String url1 = element.attr("abs:href");
				map.put(url1, ShowType.SPORTS);
				Document doc = getDoc(url1);
				while (doc.select("div.pagenavi a:contains(下一页)").size() > 0) {
					String url2 = doc.select("div.pagenavi a:contains(下一页)").first().attr("abs:href");
					map.put(url2, ShowType.SPORTS);
					doc = getDoc(url2);
				}
			} else if ("电影票".equals(showType) || "博物馆通票".equals(showType)
					|| "景点门票".equals(showType) || "论坛展览".equals(showType)
					|| "运动休闲".equals(showType) || "礼品券".equals(showType)) {
				// 获取该页中的连接地址 包括翻页
				String url1 = element.attr("abs:href");
				map.put(url1, ShowType.HOLIDAY);
				Document doc = getDoc(url1);
				while (doc.select("div.pagenavi a:contains(下一页)").size() > 0) {
					String url2 = doc.select("div.pagenavi a:contains(下一页)").first().attr("abs:href");
					map.put(url2, ShowType.HOLIDAY);
					doc = getDoc(url2);
				}
			}
		}
		return map;
	}
	public Map<String ,Integer> parseMainURL(Map<String,Integer> map){
		 Map<String ,Integer> map1 = new HashMap<String ,Integer>();
		 for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			String url = (String) iterator.next();
			Document document = getDoc(url);
			for(Element element:document.select("#playlist ul li")){
			    map1.put(element.select("a").first().attr("abs:href"), map.get(url));
			}
		}
		 return map1;
	}
	public void parseEach(String url,int showType) throws Exception{
//		url ="http://www.chinaticket.com/ticket-12998.html";
			Document document = getDoc(url);
			Show show = new Show();
			show.setAgent_id(agentID);
			show.setType(showType);
			String detailurl = "http://www.chinaticket.com/cart.php?act=add&&id=";
			show.setName(document.select("#play_info p.title").text());
			show.setImage_path(document.select("#play_pic img").first().attr("abs:src"));
			show.setIntroduction(PubFun.cleanElement(document.select("#detail")).html());
			//售票状态
			String status  = document.select("span[style=display:block;float:left;background:  url(/images/yichu.gif) no-repeat left center;padding-left:32px;width:64px;font-size:12px;font-weight:bold;padding-top:2px;color:#FFF;height:22px;]").text();
			if("已出票".equals(status)){
				show.setStatus(1);
			}else if("未出票".equals(status)){
				show.setStatus(0);
			}
			Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
			List<TicketPrice> list = null ;
			TicketPrice ticket= null;
			Elements elements = document.select("div.list-div table tr:has(td)");
			for(Element element : elements){
				list = new ArrayList<TicketPrice>();
				String site= "";
				if(element.select("td").size()>1){
					String showTime = element.select("td").first().text();
					if(showTime.indexOf("星期")!=-1){
						showTime = showTime.replaceAll("[*\\s星期(一|二|三|四|五|六|日)]", " ");
					}
					showTime = showTime.replaceAll(" +", " ");
					showTime = showTime.replaceAll("/", "-");
					try {
						showTime = newDate.format(newDate.parse(showTime));
					} catch (ParseException e) {
					}
					site = element.select("td").first().nextElementSibling().text();
					String pri = "";
					String remk = "";
					if (element.select("td:has(cite)").size() > 0) {
						for (Element elem : element.select("td:has(cite)")) {
							for (Element ele : elem.select("cite:has(a)")) {
								if (!"#".equals(ele.select("a").first().attr("href"))) {
									pri = "";
									remk = "";
									ticket = new TicketPrice();
									ticket.setMainURL(url);
									String price = ele.text();
									if (price.indexOf("(") != -1) {
										pri = price.substring(0, price.indexOf("("));
										remk = price.substring(price.indexOf("(") + 1, price.indexOf(")"));
									} else {
										pri = price;
									}
									ticket.setPrice(pri);
									ticket.setRemark(remk);
									ticket.setExist(true);
									String id = elem.select("a").attr("href").replaceAll("[javascript:addCart('a-z')]","");
									ticket.setDetailURL(detailurl + id);
									list.add(ticket);
								}
							}
							for (Element ele : elem.select("cite:not(:has(a))")) {
								ticket = new TicketPrice();
								ticket.setMainURL(url);
								String price = ele.text();
								pri = "";
								remk = "";
								if (price.indexOf("(") != -1) {
									pri = price	.substring(0, price.indexOf("("));
									remk = price.substring(	price.indexOf("(") + 1, price.indexOf(")"));
								} else {
									pri = price;
								}
								ticket.setPrice(pri);
								ticket.setRemark(remk);
								ticket.setExist(false);
								list.add(ticket);
							}
						}
					}else{
						ticket = new TicketPrice();
						ticket.setMainURL(url);
						ticket.setExist(false);
						ticket.setPrice("");
						list.add(ticket);
					}
					//如果演出时间没有确定，则不保存相关记录
					if(showTime.contains("待定")||"".equals(showTime)){
						continue;
					}
					timeAndPrice.put(showTime, list);
				}
				//如果演出场馆没有确定，则不保存相关演出信息
				if("".equals(site)||site.contains("待定")){
					continue;
				}
				show.setSiteName(site);
				if(timeAndPrice==null ||timeAndPrice.size()==0){
					continue;
				}
				show.setTimeAndPrice(timeAndPrice);
			}
			getDao().saveShow(show);
		}
	
	public static void main(String[] args) throws Exception {
		  ChinaticketSpider chinaticket = new ChinaticketSpider();
//		  chinaticket.setDao(new DataDao());
//		  chinaticket.extract();
		  chinaticket.parseEach("http://www.chinaticket.com/ticket-13655.html", 1);
	}
}
