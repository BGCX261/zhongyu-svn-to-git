package com.piaoyou.crawler.spider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
public class VipticketSpider extends TicketSpider implements SpiderTask {
	private static final String BASE_URL = "http://www.vipticket.cn/";
	private static final String URL ="http://www.vipticket.cn/allticket";
	private static final String AJAX="http://www.vipticket.cn/sort.php";

	private static final SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();

	@Override
	public void extract() {
		Set<String> set= new HashSet<String>();
		set = getURL();
		for (Iterator<String> iterator =set.iterator(); iterator	.hasNext();) {
			String url = (String) iterator.next();
			try {
				parseEach(url);
			} catch (Exception e) {
				LogFactory.getLog(VipticketSpider.class).error(Const.concat(url),e);
			}
		}
	/*	try {
			parseEach("");
		} catch (Exception e) {
		}*/
	}
	public static void main(String[] args) {
		VipticketSpider template = new VipticketSpider();
		template.setDao(new DataDao());
		template.extract();
	}

	public Set<String> getURL() {
		Set<String> set = new HashSet<String>();
		Document document = getDoc(URL);
		//首先获取所有的翻页总数
		int pageCount = Integer.parseInt(document.select("#page a:contains(末页)").attr("onclick").replaceAll("\\D", ""));
		for (int i = 0; i <= pageCount; i++) {
			String url1 = "http://www.vipticket.cn/sort.php?pno="+ i + "&cid=&start=&state=&sort=&show=2&fresh=0.8376499970909208";
			Document doc = getDoc(url1);
			for (Element element : doc.select("tr.font26")) {
				Elements elements = element.select("td");
				// 如果票价为'------' 或者'待定'或者售票状态为'下架',都不进行抓取
				if (!("待定".equals(elements.get(3).text())	|| "------".equals(elements.get(3).text()) || "下架".equals(elements.get(4).text()))) {
					set.add(elements.get(1).select("a").first().attr("abs:href"));
				}
			}
		}
		return set;
	}
	public void parseEach(String url) throws Exception{
			Show show = new Show();
			Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>() ;
			List<TicketPrice> list = null ;
			TicketPrice ticket = null ;
			Document document = getDoc(url);
		    int showType = getShowType(document.select("a.font02").get(1).text());
		    show.setAgent_id(agentID);
		    show.setSiteName(document.select("a.font02").get(2).text());
		    show.setImage_path(document.select("#ticket_name table tr td img").get(0).attr("abs:src"));
		    show.setName(document.select("h1.font23 a").first().text());
		    show.setType(showType);
		    show.setIntroduction(PubFun.cleanElement(document.select("#ticket_intro_cont")).html());
			String tmpUrl = "http://www.vipticket.cn/addcar.php?pid=";
		    for(Element ele :document.select("div.price_box")){
		    	String showTime = ele.select("td[width=150]").text();
		    	showTime=showTime.replaceAll("[星期一二三四五六日]", "").replaceAll(" +"," ").replaceAll("：", ":");
		    	try {
					showTime = newDate.format(newDate.parse(showTime));
				} catch (Exception e) {
				}
		    	list = new ArrayList<TicketPrice>();
		    	for(Element ele1 :ele.select("td[width=300] font")){
		    		ticket = new TicketPrice();
		    		ticket.setMainURL(url);
		    		ticket.setPrice(ele1.text());
		    		ticket.setExist(false);
		    		list.add(ticket);
		    	}
		    	for(Element ele1:ele.select("td[width=300] a")){
		    		ticket =  new TicketPrice();
		    		ticket.setMainURL(url);
		    		ticket.setExist(true);
		    		ticket.setDetailURL(tmpUrl+ele1.attr("onclick").replaceAll("\\D", ""));
		    		ticket.setPrice(ele1.text());
		    		list.add(ticket);
		    	}
		    	timeAndPrice.put(showTime, list);
		    }
		    show.setTimeAndPrice(timeAndPrice);
		    getDao().saveShow(show);
	}
	private int getShowType(String type) {
		/**
		 * 演唱会 音乐会 话剧歌剧 戏曲综艺 魔术马戏 舞蹈芭蕾 儿童亲子 运动生活
		 */
		int showType = 0;
		if("演唱会".equals(type)){
			  showType=ShowType.CONCERT;
		}else if("音乐会".equals(type)){
			  showType=ShowType.SHOW;
		}else if("话剧歌剧".equals(type)){
			  showType=ShowType.DRAMA;
		}else if("戏曲综艺".equals(type)||"魔术马戏".equals(type)){
			  showType=ShowType.OPERA;
		}else if("舞蹈芭蕾".equals(type)){
			  showType=ShowType.DANCE;
		}else if("儿童亲子".equals(type)){
			  showType=ShowType.CHILDREN;
		}else if("运动生活".equals(type)){
			  showType=ShowType.SPORTS;
		}
		return showType;
	}

}
