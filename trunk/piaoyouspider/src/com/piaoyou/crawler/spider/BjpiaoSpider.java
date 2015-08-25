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

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

public class BjpiaoSpider extends TicketSpider implements SpiderTask {
	private static final String BASE_URL = "http://bjpiao.com/";
	private static final String URL ="http://bjpiao.com/allticket.asp";
	private static final Class<BjpiaoSpider> BASE_CLASS=BjpiaoSpider.class;
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
				e.printStackTrace();
			}
		}
//		try {
//			parseEach("");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	public static void main(String[] args) {
		BjpiaoSpider bjpiao = new BjpiaoSpider();
		bjpiao.setDao(new DataDao());
		bjpiao.extract();
	}

	public Set<String> getURL() {
		Set<String> set = new HashSet<String>();
		Document document = getDoc(URL);
		for(int i=2;i<25;i++){
			for(Element element :document.select("table[width=960]").get(i).select("tr[bgcolor=#FFFFFF]")){
				if(!(element.select("td").get(1).text().contains("待定")||element.select("td").get(2).text().contains("待定"))){
					    set.add(element.select("td").get(0).select("a").first().attr("abs:href"));
				}
			}
		}
		return set;
	}

	public void parseEach(String url) throws Exception {
//		url="http://bjpiao.com/flist.asp?bid=4310";
		Show show = new Show();
		Map<String, List<TicketPrice>> timeAndPrice = new HashMap<String, List<TicketPrice>>();
		List<TicketPrice> list = null;
		TicketPrice ticket = null;
		Document document = getDoc(url);
		show.setAgent_id(agentId);
		show.setType(getShowType(document.select("a.titlefont").get(4).text()));
		show.setName(document.select("table[width=960]").get(2).select("table[width=100%]").get(1).text());
		show.setImage_path(document.select("img.spimgs").first().attr("abs:src"));
		show.setWatermark(true);
		show.setSiteName(document.select("td.spheng").get(3).select("a").first().text());
		show.setIntroduction(PubFun.cleanElement(document.select("table[width=960]").get(4).select("table[width=740]").get(1)).html());
		for (Element element : document.select("table[bgcolor=E7D4BB] tr")) {
			if (element.select("td[height=30]").size() != 0) {
				String showTime = element.select("td").get(1).text();
				showTime = showTime.replaceAll("[\\( +\\)星期一二三四五六日]", " ").trim();
				try {
					showTime = newDate.format(newDate.parse(showTime));
				} catch (Exception e) {
				}
				list = new ArrayList<TicketPrice>();
				for (Element elem : element.select("td").get(2).select("font")) {
					if ("a".equals(elem.parent().nodeName())) {
						ticket = new TicketPrice();
						ticket.setMainURL(url);
						ticket.setDetailURL(elem.parent().attr("abs:href"));
						ticket.setExist(true);
						String remark = elem.text().replaceAll("^\\d+", "");
						String price = elem.text().replace(remark, "");
						//去掉备注的中的各种括号
						remark=remark.replaceAll("[{}()｛｝（）]", "");
						ticket.setPrice(price);
						ticket.setRemark(remark);
						list.add(ticket);
					} else {
						ticket = new TicketPrice();
						ticket.setMainURL(url);
						ticket.setExist(false);
						String remark = elem.text().replaceAll("^\\d+", "");
						String price = elem.text().replace(remark, "");
						//去掉备注的中的各种括号
						remark=remark.replaceAll("[{}()｛｝（）]", "");
						ticket.setPrice(price);
						ticket.setRemark(remark);
						
						list.add(ticket);
					}
				}
				timeAndPrice.put(showTime, list);
			}
		}
		show.setTimeAndPrice(timeAndPrice);
		getDao().saveShow(show);
	}
	private int getShowType(String type){
		int showType = 0;
		if("演唱会".equals(type)){
			 showType = ShowType.CONCERT;
		}else if("音乐会".equals(type)||"国家大剧院五月音乐节".equals(type)||"国家大剧院".equals(type)){
			showType = ShowType.SHOW;
		}else if("话剧歌剧".equals(type)||"北京人艺".equals(type)||"朝阳9个剧场专区".equals(type)||"国家大剧院歌剧节".equals(type)){
			showType = ShowType.DRAMA;
		}else if("舞蹈芭蕾".equals(type)){
			showType = ShowType.DANCE;
		}else if("曲苑杂坛".equals(type)||"戏曲曲艺".equals(type)||"长安大戏院".equals(type)||"梅兰芳大剧院演出季".equals(type)||"杂技马戏".equals(type)){
			showType = ShowType.OPERA;
		}else if("亲子儿童".equals(type)){
			showType = ShowType.CHILDREN;
		}else if("体育比赛".equals(type)){
			showType = ShowType.SPORTS;
		}else if("度假休闲".equals(type)||"体育休闲".equals(type)){
			showType = ShowType.HOLIDAY;
		}else{
			showType = ShowType.OTHER;
		}
		return showType;
	}

}
