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
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

public class KaixinpiaoSpider extends TicketSpider implements SpiderTask {
	
	private static final String BASE_URL = "http://www.kaixinpiao.com/";
	private static final String URL ="http://www.kaixinpiao.com/allticket/";
	private static final Class<KaixinpiaoSpider> BASE_CLASS=KaixinpiaoSpider.class;
	private static final SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();
	
	@Override
	public void extract() {
		Set<String> set = new HashSet<String>();
		set = getURL();
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String url = (String) iterator.next();
			try {
				parseEach(url);
			} catch (Exception e) {
				LogFactory.getLog(BASE_CLASS).error(Const.concat(url),e);
			}
		}
	
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		KaixinpiaoSpider kaixin = new KaixinpiaoSpider();
		kaixin.setDao(new DataDao());
		//kaixin.extract();
		try{
			//http://www.kaixinpiao.com/ticket_7887.html
			kaixin.parseEach("http://www.kaixinpiao.com/ticket_7887.html");
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	public Set<String> getURL(){
		Set<String> set = new HashSet<String>();
		Document document = getDoc(URL);
		for(Element ele : document.select("tr[style^=background-color]")){
			if(ele.select("td").get(1).text().contains("待定")||ele.select("td").get(2).text().contains("待定")||ele.select("td").get(3).text().contains("待定")||ele.select("td").get(3).text().contains("预定")){
			   continue;
			}else{
				set.add(ele.select("td").first().select("a").first().attr("abs:href"));
			}
		}
		return set;
	}

	public void parseEach(String url) throws Exception {
		Show show = new Show();
		Map<String, List<TicketPrice>> timeAndPrice = null;
		List<TicketPrice> list = null;
		TicketPrice ticket = null;
		Document document = getDoc(url);
		show = new Show();
		show.setAgent_id(agentID);
		show.setName(document.select("div.zzuos").text());
		show.setImage_path(document.select("div.zzuod dl dd img").attr("abs:src"));
		show.setIntroduction(PubFun.cleanElement(document.select("div.xinxi")).html());
		show.setSiteName(document.select("span.lan a").text());
		show.setType(getShowType(document.select("p.lan a").text()));
		show.setWatermark(true);
		timeAndPrice = new HashMap<String, List<TicketPrice>>();
		for (Element element : document.select("div.dingwq")) {
			String showTime = element.select("p").get(0).text();
			try {
				showTime=showTime.replaceAll("[ +]", " ");
				showTime=showTime.replace("：", ":");
				showTime = newDate.format(newDate.parse(showTime));
				 // 和当前时间进行比较，如果该演出过期，则不进行抓取
				if (new Date().after(newDate.parse(showTime))) {
					continue;
				}
			} catch (ParseException e) {
			}

			list = new ArrayList<TicketPrice>();
			for (int i = 0, length = element.select("p").get(2).childNodes()
					.size(); i < length; i++) {
				Node node = element.select("p").get(2).childNodes().get(i);
				if (node instanceof TextNode) {
					if (!"".equals(((TextNode) node).text().trim())) {
						ticket = new TicketPrice();
						ticket.setMainURL(url);
						String price = ((TextNode) node).text();
						String pri = price;
						String remark = "";
						if (price.indexOf("(") != -1) {
							pri = price.substring(0, price.indexOf("("));
							remark = price.substring(price.indexOf("(") + 1,price.indexOf(")"));
						} else if (price.indexOf("（") != -1) {
							pri = price.substring(0, price.indexOf("（"));
							remark = price.substring(price.indexOf("（") + 1,price.indexOf("）"));
						}
						ticket.setPrice(pri.replaceAll("[^\\d+]", ""));
						ticket.setRemark(remark+"   "+pri.replaceAll("^ |\\d+", ""));
						ticket.setExist(false);
						list.add(ticket);
					}
				} else if (node instanceof Element) {
					if ("a".equals(node.nodeName())) {
						Element nod = (Element) node;
						ticket = new TicketPrice();
						if (nod.text().contains("待定")) {
							continue;
						}
						ticket.setPrice(nod.text().replaceAll("[^0-9]", ""));
						ticket.setMainURL(url);
						ticket.setDetailURL(nod.attr("abs:href"));
						ticket.setExist(true);
						if (nod.nextElementSibling() != null) {
							ticket.setRemark(nod.nextElementSibling().text().replaceAll("[\\((|)（|）\\)]", ""));
						}
						list.add(ticket);
					}
				}
			}
			if (list.size() == 0 || list.isEmpty()) {
				continue;
			}
			timeAndPrice.put(showTime, list);
		}
		if (timeAndPrice.size() == 0 || timeAndPrice.isEmpty()) {
			return;
		}
		show.setTimeAndPrice(timeAndPrice);
		getDao().saveShow(show);
	}
	private int getShowType(String type){
		// 演唱会  音乐会  话剧歌剧  舞蹈芭蕾  戏曲综艺  魔术马戏  儿童亲子  曲艺杂技  体育休闲  电影票  会议讲座  
		int showType = 0 ;
		if("演唱会".equals(type)){
			  showType = ShowType.CONCERT;
		}else if("音乐会".equals(type)){
			showType = ShowType.SHOW;
		}else if("话剧歌剧".equals(type)){
			showType = ShowType.DRAMA;
		}else if("舞蹈芭蕾".equals(type)){
			showType = ShowType.DANCE;
		}else if("戏曲综艺".equals(type)||"魔术马戏".equals(type)||"曲艺杂技".equals(type)){
			showType = ShowType.OPERA;
		}else if("儿童亲子".equals(type)){
			showType = ShowType.CHILDREN;
		}else if("体育休闲".equals(type)){
			showType = ShowType.HOLIDAY;
		}else if("电影票".equals(type)){
			showType = ShowType.HOLIDAY;
		}
		return showType;
	}

}
