package com.piaoyou.crawler.spider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

@SuppressWarnings("unused")
public class HypiaoSpider extends TicketSpider implements SpiderTask {
	private static final String BASE_URL = "http://www.hypiao.com/";
	private static final String URL ="http://www.hypiao.com/product.asp";
	private static final Class<HypiaoSpider> BASE_CLASS=HypiaoSpider.class;
	private static final SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static String agentId = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();

	@Override
	public void extract() {
		Set<String> set = new HashSet<String>();
		set = getURL();
		for (Iterator<String> iterator = set.iterator(); iterator	.hasNext();) {
			String url = (String) iterator.next();
			try {
				parseEach(url);
			} catch (Exception e) {
				LogFactory.getLog(BASE_CLASS).error(Const.concat(url),e);
			}
		}
//		try {
//			parseEach("");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
	}
	public static void main(String[] args) {
		HypiaoSpider hypiao = new HypiaoSpider();
		hypiao.setDao(new DataDao());
		hypiao.extract();
	}

	public Set<String> getURL() {
		Set<String> set = new HashSet<String>();
		Document document = getDoc(URL);
		for(Element element :document.select("img[src=images/booking_bn.gif]")){
			  set.add(element.parent().attr("abs:href"));
		}
		return set;
	}

	public void parseEach(String url) throws Exception {
		Show show = new Show();
		Map<String, List<TicketPrice>> timeAndPrice = new HashMap<String, List<TicketPrice>>();
		List<TicketPrice> list = null;
		TicketPrice ticket = null;
		Document document = getDoc(url);
		show.setAgent_id(agentId);
		Elements eles = document.select("td[style=PADDING-RIGHT: 10px; PADDING-BOTTOM: 7px] a");
		show.setType(getShowType(document.select("td[style=PADDING-RIGHT: 10px; PADDING-BOTTOM: 7px] a").last().text()));
		show.setName(document.select("table[width=385] table tr").get(0)	.text());
		if (document.select("table[width=385] table tr").get(5).text().contains("待定")	|| // 场馆待定
				document.select("table[width=385] table tr").get(3).text().contains("待定") || // 票价待定
				document.select("table[width=385] table tr").get(7).text().contains("待定")) { // 时间待定
			return;
		}
		show.setSiteName(document.select("table[width=385] table tr").get(5).select("a").first().text());
		show.setImage_path(document.select("table[width=300] tr").get(2)	.select("img").first().attr("abs:src"));
		show.setIntroduction(PubFun.cleanElement((document.select("#right table[width=100%]")).get(8)).html());
		Elements elems = document	.select("table[width=100%] table[width=100%] table[width=100%]>tr");
		for (int i = 2; i < elems.size(); i++) {
			list = new ArrayList<TicketPrice>();
			String showTime = elems.get(i).select("td").get(1).text();
			try {
				showTime = newDate.format(newDate.parse(showTime.replaceAll(" ", " ")));
			} catch (Exception e) {
			}
			for (Element ele : elems.get(i).select("table tr td")) {
				if ("a".equals(ele.child(0).nodeName())) {
					ticket = new TicketPrice();
					ticket.setMainURL(url);
					ticket.setExist(true);
					ticket.setDetailURL(ele.child(0).attr("abs:href"));
					//本网站的票价分为两种格式    VIP400  和   400(230*2)
					String price = ele.text();
					price = price.trim().replaceAll("[  ]+", "");
					String remark="";
					if(Character.isDigit(price.charAt(0))){  //400(230*2)
						remark = price.replaceAll("^\\d+", "");
						 price = price.replace(remark, "");
						remark = remark.replaceAll("[{}()｛｝（）]", "");
					}else if(Character.isDigit(price.charAt(price.length()-1))){  // VIP400
						remark = price.replaceAll("\\d+$", "");
						 price = price.replace(remark, "");
					}
					ticket.setPrice(price);
					ticket.setRemark(remark);
					list.add(ticket);
				} else if ("span".equals(ele.child(0).nodeName())) {
					ticket = new TicketPrice();
					ticket.setMainURL(url);
					ticket.setExist(false);
					//本网站的票价分为两种格式    VIP400  和   400(230*2)
					String price = ele.text();
					price = price.trim().replaceAll("[  ]+", "");
					String remark="";
					if(Character.isDigit(price.charAt(0))){  //400(230*2)
						remark = price.replaceAll("^\\d+", "");
						 price = price.replace(remark, "");
						remark = remark.replaceAll("[{}()｛｝（）]", "");
					}
					if(Character.isDigit(price.charAt(price.length()-1))){  // VIP400
						remark = price.replaceAll("\\d+$", "");
						 price = price.replace(remark, "");
					}
					ticket.setPrice(price);
					ticket.setRemark(remark);
					list.add(ticket);
				}
			}
			timeAndPrice.put(showTime, list);
		}
		show.setTimeAndPrice(timeAndPrice);
		getDao().saveShow(show);
	}
	private int getShowType(String type){
		int showType = 0;
		if("演 唱 会".equals(type)){
			 showType = ShowType.CONCERT;
		}else if("音乐会".equals(type)||"国家大剧院音乐厅".equals(type)){
			showType = ShowType.SHOW;
		}else if("话剧歌剧".equals(type)||"话剧・歌剧".equals(type)||"国家大剧院歌剧院".equals(type)
				||"国家大剧院戏剧场".equals(type)||"国家大剧院小剧场".equals(type)){
			showType = ShowType.DRAMA;
		}else if("舞蹈芭蕾".equals(type)||"舞蹈・芭蕾".equals(type)){
			showType = ShowType.DANCE;
		}else if("曲苑杂坛".equals(type)||"戏曲・综艺".equals(type)||"魔术杂技".equals(type)){
			showType = ShowType.OPERA;
		}else if("儿童亲子".equals(type)||"亲子儿童".equals(type)){
			showType = ShowType.CHILDREN;
		}else if("体育比赛".equals(type)||"体 育".equals(type)){
			showType = ShowType.SPORTS;
		}else if("度假休闲".equals(type)||"电影票券".equals(type)){
			showType = ShowType.HOLIDAY;
		}
		return showType;
	}

}
