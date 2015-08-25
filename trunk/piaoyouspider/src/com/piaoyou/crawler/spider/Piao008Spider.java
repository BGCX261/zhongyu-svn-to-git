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

public class Piao008Spider extends TicketSpider implements SpiderTask {
	
	private static final String BASE_URL ="http://www.piao008.com/";  //网站首页
	private static final String URL ="http://www.piao008.com//allticket.php?channel=9";   //获取所有演出信息的页面
	private static final Class<Piao008Spider> BASE_CLASS =  Piao008Spider.class;
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();
	private static final SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Override
	public void extract() {
		 Map<String,Integer> map = new HashMap<String,Integer>();
		 map = getURL();
		  for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			String url = (String) iterator.next();
			try {
				parseEach(url, map.get(url));
			} catch (Exception e) {
				LogFactory.getLog(BASE_CLASS).error(Const.concat(url,map.get(url)),e);
			}
		}
	}
	
	public  Map<String, Integer> getURL() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		Document document = getDoc(URL);
		Elements elements = document.select("div.allticket");
		for (Element element : elements.select("table[class^=allticket]")) {
			String type = element.select("tr.tit th").first().text();
			Elements eles1 = element.select("tr:not(tr.tit)");
			if("演唱会".equals(type)){
				for(Element ele1 : eles1){
					Element ele2 = ele1.select("td a").first();
					map.put(ele2.attr("abs:href"), ShowType.CONCERT);
				}
			}else if("音乐会".equals(type)||"国家大剧院音乐厅".equals(type)){
				for(Element ele1 : eles1){
					Element ele2 = ele1.select("td a").first();
					map.put(ele2.attr("abs:href"), ShowType.SHOW);
				}
			}else if("亲子儿童".equals(type)){
				for(Element ele1 : eles1){
					Element ele2 = ele1.select("td a").first();
					map.put(ele2.attr("abs:href"), ShowType.CHILDREN);
				}
			}else if("话剧歌剧".equals(type)||"国家大剧院歌剧院".equals(type)||"国家大剧院小剧场".equals(type)||"北京人艺演出中心".equals(type)||"东联艺术工社".equals(type)){
				for(Element ele1 : eles1){
					Element ele2 = ele1.select("td a").first();
					map.put(ele2.attr("abs:href"), ShowType.DRAMA);
				}
			}else if("舞蹈芭蕾".equals(type)){
				for(Element ele1 : eles1){
					Element ele2 = ele1.select("td a").first();
					map.put(ele2.attr("abs:href"), ShowType.DANCE);
				}
			}else if("戏曲曲艺".equals(type)||"梅兰芳大剧院".equals(type)||"长安大戏院".equals(type)){
				for(Element ele1 : eles1){
					Element ele2 = ele1.select("td a").first();
					map.put(ele2.attr("abs:href"), ShowType.OPERA);
				}
			}else if("电影通票".equals(type)){
				for(Element ele1 : eles1){
					Element ele2 = ele1.select("td a").first();
					map.put(ele2.attr("abs:href"), ShowType.HOLIDAY);
				}
			}else if("体育类".equals(type)){
				for(Element ele1 : eles1){
					Element ele2 = ele1.select("td a").first();
					map.put(ele2.attr("abs:href"), ShowType.SPORTS);
				}
			}
		}
		return map;
	}
	public void parseEach(String url, int showType) throws Exception {
		Show show = new Show();
		Document document = getDoc(url);
		show.setAgent_id(agentID);
		// 宣传海报
		show.setImage_path(document.select("dd.g_pic a img").first().attr("abs:src"));
		// 演出标题
		show.setName(document.select("dl.tickets_info dd ul li").first()	.text());
		// 演出类型
		show.setType(showType);
		// 演出场馆
		String siteName = document.select("dl.tickets_info dd ul li:contains(场馆)").select("a").first()	.text();
		// 如果演出场馆待定，则不记录该条信息
		if ("待定".equals(siteName)) {
			return;
		}
		show.setSiteName(siteName);
		// 演出简介
		show.setIntroduction(PubFun.cleanElement(document.select("td.con p")).text());
		Map<String, List<TicketPrice>> timeAndPrice = new HashMap<String, List<TicketPrice>>();
		for (Element ele : document.select("table.tickets_table tr.tr")) {
			List<TicketPrice> list = null;
			TicketPrice ticketPrice = null;
			String showTime = ele.select("td").first().text();
			// 根据时间格式化来判断给时间是否正常，如果异常，则表明该时间不是正常的演出时间，将时间作为票价的备注存在数据库中
			String remk = "";
			try {
				showTime = newDate.format(newDate.parse(showTime));
			} catch (ParseException e) {
			}
			for (Element ele1 : ele.select("td.price")) {
				list = new ArrayList<TicketPrice>();
				String[] prices = ele1.text().split(" +");
				for (int i = 0; i < prices.length; i++) {
					Elements eles = ele1.select("a:matches(\\b"+ prices[i].replaceAll("\\([^)]*\\)", "") + "\\b)");
					ticketPrice = new TicketPrice();
					ticketPrice.setMainURL(url);
					if (eles.isEmpty()) {
						ticketPrice.setExist(false);
						ticketPrice.setDetailURL("");
					} else {
						ticketPrice.setExist(true);
						ticketPrice.setDetailURL(eles.first().attr("abs:href"));
					}
					String pri1 = "";
					String remark1 = "";
					// 获取票价
					pri1 = PubFun.getRegex("((^\\d+\\.+\\d+)|(^\\d+)).*",prices[i], 1);
					if (pri1 == null) {
						continue;
					}
					// 获取备注
					remark1 = prices[i].replaceAll(pri1, "");
					remark1 = remark1.replaceAll("[()（）]", "");
					// 如果票价待定，将不记录这条信息
					if ("待定".equals(remark1)) {
						pri1 = "";
						continue;
					}
					ticketPrice.setPrice(pri1);
					ticketPrice.setRemark(remark1 + "	" + remk);
					list.add(ticketPrice);
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
	public static void main(String args[]){
		Piao008Spider piao = new Piao008Spider();
		piao.setDao(new DataDao());
		piao.extract();
	}
}
