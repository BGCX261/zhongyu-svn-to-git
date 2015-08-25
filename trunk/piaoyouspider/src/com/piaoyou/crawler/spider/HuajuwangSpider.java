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

public class HuajuwangSpider extends TicketSpider implements SpiderTask {
	private static final String BASE_URL = "http://www.huajuwang.com/";
	private static final String URL ="http://www.huajuwang.com/yanchuliebiao.html";
	private static final Class<HuajuwangSpider> BASE_CLASS=HuajuwangSpider.class;
	private static final SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
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
		
	/*	try {
			parseEach("");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	public static void main(String[] args) {
		HuajuwangSpider huaju = new HuajuwangSpider();
		huaju.setDao(new DataDao());
		huaju.extract();
	}

	public Set<String> getURL() {
		Set<String> set = new HashSet<String>();
		Document document = getDoc(URL);
		for(Element element:document.select("a.nin")){
			set.add(element.attr("abs:href"));
		}
		return set;
	}

	public void parseEach(String url) throws Exception {
		Show show = new Show();
		Map<String, List<TicketPrice>> timeAndPrice = new HashMap<String, List<TicketPrice>>();
		List<TicketPrice> list = null;
		TicketPrice ticket = null;
		Document document = getDoc(url);
		show.setAgent_id(agentID);
		show.setName(document.select("div[class=cc chs f16 b]").text());
		show.setSiteName(document.select("div.db table tr td:contains(场馆) a").text());
		show.setImage_path(document.select("div.da img").first().attr("abs:src"));
		show.setIntroduction(PubFun.cleanElement(document.select("div.dbb").first()).html());
		show.setType(ShowType.DRAMA);
		for (Element element : document.select("div.dbb table[width=100%] tr:has(td)")) {
			String showTime = element.select("td").get(1).text();
			try {
				showTime = newDate.format(newDate.parse(showTime));
			} catch (Exception e) {
			}
			list = new ArrayList<TicketPrice>();
			if (element.select("td").size() == 3) { // 不包含套票
				List<Node> listNode = element.select("td").get(2).childNodes();
				for (int i = 0, length = listNode.size(); i < length; i++) {
					Node node = (Node) listNode.get(i);
					if (node instanceof Element) {
						ticket = new TicketPrice();
						ticket.setMainURL(url);
						ticket.setDetailURL(node.attr("abs:href"));
						ticket.setExist(true);
						ticket.setPrice(((Element) node).text());
						list.add(ticket);
					} else if (node instanceof TextNode) {
						String price[] = ((TextNode) node).text().split(" ");
						for (int j = 0; j < price.length; j++) {
							if (!"".equals(price[j].trim())) {
								ticket = new TicketPrice();
								ticket.setMainURL(url);
								ticket.setExist(false);
								ticket.setPrice(price[j]);
								list.add(ticket);
							}
						}
					}
				}
			} else if (element.select("td").size() == 4) { // 包含套票
				List<Node> listNode = element.select("td").get(2).childNodes();
				for (int i = 0, length = listNode.size(); i < length; i++) {
					Node node = (Node) listNode.get(i);
					if (node instanceof Element) {
						ticket = new TicketPrice();
						ticket.setMainURL(url);
						ticket.setDetailURL(node.attr("abs:href"));
						ticket.setExist(true);
						ticket.setPrice(((Element) node).text());
						ticket.setRemark(node.attr("title"));
						list.add(ticket);
					} else if (node instanceof TextNode) {
						String price[] = ((TextNode) node).text().split(" ");
						for (int j = 0; j < price.length; j++) {
							if (!"".equals(price[j].trim())) {
								ticket = new TicketPrice();
								ticket.setMainURL(url);
								ticket.setExist(false);
								ticket.setPrice(price[j]);
								list.add(ticket);
							}
						}
					}
				}
				listNode = element.select("td").get(3).childNodes();
				for (int i = 0, length = listNode.size(); i < length; i++) {
					Node node = (Node) listNode.get(i);
					if (node instanceof Element) {
						ticket = new TicketPrice();
						ticket.setMainURL(url);
						ticket.setDetailURL(node.attr("abs:href"));
						ticket.setExist(true);
						ticket.setPrice(((Element) node).text());
						list.add(ticket);
					} else if (node instanceof TextNode) {
						String price[] = ((TextNode) node).text().split(" ");
						for (int j = 0; j < price.length; j++) {
							if (!"".equals(price[j].trim())) {
								ticket = new TicketPrice();
								ticket.setMainURL(url);
								ticket.setExist(false);
								ticket.setPrice(price[j]);
								list.add(ticket);
							}
						}
					}
				}
			}
			timeAndPrice.put(showTime, list);
		}
		show.setTimeAndPrice(timeAndPrice);
		 getDao().saveShow(show);
	}

}
