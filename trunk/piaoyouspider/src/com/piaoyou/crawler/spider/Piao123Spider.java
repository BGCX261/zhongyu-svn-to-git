package com.piaoyou.crawler.spider;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.PubFun;

/**
 * piao123
 * @author Administrator
 *
 */
public class Piao123Spider extends TicketSpider implements SpiderTask {

	private static final Log log = LogFactory.getLog(Piao123Spider.class);
	
	private final static String BASE_URL = "http://www.piao123.com/";
	private final static String HOME_PAGE = "http://www.piao123.com/city/index.html";
	private final static String All_TICKET = "http://www.piao123.com/live_list_all.php";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
				.get(0).get("agency_id").toString();
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy-M-dd h:mm");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static Map<String,Integer> typeCor = new HashMap<String,Integer>();
	
	static{
		initTypeCor();
	}
	
	/**
	 * 初始化演出类别对应
	 */
	private static void initTypeCor(){
		typeCor.put("演唱会",ShowType.CONCERT);//演唱会
		typeCor.put("音乐会",ShowType.SHOW);//音乐会
		typeCor.put("国际室内乐音乐季",ShowType.SHOW);//音乐会
		typeCor.put("话剧",ShowType.DRAMA);//话剧歌剧
		typeCor.put("歌剧",ShowType.DRAMA);//话剧歌剧
		typeCor.put("舞蹈芭蕾",ShowType.DANCE);//舞蹈芭蕾
		typeCor.put("京剧戏曲",ShowType.OPERA);//曲苑杂坛
		typeCor.put("魔术杂技",ShowType.OPERA);//曲苑杂坛
		typeCor.put("相声综艺",ShowType.OPERA);//曲苑杂坛
		typeCor.put("儿童演出",ShowType.CHILDREN);//儿童亲子
		typeCor.put("电影票",ShowType.HOLIDAY);//其他
		typeCor.put("体育竞技",ShowType.SPORTS);//体育比赛
		typeCor.put("温泉滑雪",ShowType.HOLIDAY);//其他
		typeCor.put("休闲马戏",ShowType.HOLIDAY);//其他
		typeCor.put("展览其他",ShowType.HOLIDAY);//其他
		typeCor.put("酒店",ShowType.HOLIDAY);//其他
		typeCor.put("旅游及景区门票",ShowType.HOLIDAY);//其他
	}
	
	@Override
	public void extract() {
		String session = getSessionID();
		if(session == null){
			throw new RuntimeException("首页抓取失败");
		}
		Document index = getHomePage(HOME_PAGE,session,BASE_URL);
		for(Element city:index.select("td>span.city_cell a")){//遍历所有城市
			getHomePage(city.attr("abs:href"),session,BASE_URL);
			Document every = getHomePage(All_TICKET, session, city.attr("abs:href"));
			//遍历各城市的全部演出
			for(Element ticket:every.select("a:has(img[src=/images/dingpiao_button.jpg])")){
				extractEach(ticket.attr("abs:href"));
			}
		}
	}

	/**
	 * 提取每一场演出的票价
	 * @param url		演出url
	 */
	private void extractEach(String url) {
		Show show = new Show();
		try {
			show.setAgent_id(agentID);
			Document ticket = getDoc(url);
			show.setType(typeCor.get(ticket.select("a.font12hui_bottom:eq(2)").text().trim()));
			show.setName(ticket.select("td.PERFORM_BOLD_NAME").text());//演出标题
			show.setStatus("出票".equals(ticket.select("#taopiao").text())?1:0);
			//演出简介
			show.setIntroduction(PubFun.cleanElement(ticket.select("body>table").get(6).select("table").get(3)).html());
			String siteName = ticket.select(".font12hui:contains(演出场馆)").text().replace("演出场馆：", "");
			if(!"待定".equals(siteName)){
				show.setSiteName(siteName);
			}else{
				return;
			}
			show.setImage_path(ticket.select("img[width=240]").first().attr("abs:src"));
			
			Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String, List<TicketPrice>>();
			show.setTimeAndPrice(timeAndPrice);
			for(Element each:ticket.select("body>table").get(6).select("table tr[id^=perform_price_line]")){
				Elements tmp = each.select("td");
				String time = tmp.get(1).text();
				if(time.length() == 18){//正常时间
					time = time.substring(0, 16);
				}else if(time.matches("\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}.*")){
					time = newDate.format(oldDate.parse(time));
				}
				List<TicketPrice> ticketPrice = new ArrayList<TicketPrice>();
				timeAndPrice.put(time, ticketPrice);
				int priceIndex = 2;
				if(tmp.size()>3){//含有套票
					String[] prices = tmp.get(priceIndex).select("span.font14lanse").text().split("\\s+");
					for(int i=0;i<prices.length;i++){
						if("无套票".equals(prices[i])){
							continue;
						}
						Elements a = tmp.get(priceIndex).select("span.font14lanse a:matches(\\b"+prices[i]+"\\b)");
						TicketPrice price = new TicketPrice();
						price.setMainURL(url);
						price.setPrice(prices[i]);
						price.setExist(!a.isEmpty());
						if(price.isExist()){
							price.setRemark(a.first().attr("title"));
						}
						ticketPrice.add(price);
					}
					priceIndex = 3;
				}
				String[] prices = tmp.get(priceIndex).select("span.font14lanse").text().split("\\s+");
				for(int i=0;i<prices.length;i++){//正常的非套票
					Elements a = tmp.get(priceIndex).select("span.font14lanse a:matches(\\b"+prices[i]+"\\b)");
					TicketPrice price = new TicketPrice();
					price.setMainURL(url);
					price.setPrice(prices[i]);
					price.setExist(!a.isEmpty());
					ticketPrice.add(price);
				}
			}
			getDao().saveShow(show);
		} catch (Exception e) {
			log.error(url,e);
		}
	}

	/**
	 * 
	 * @param url
	 * @param session
	 * @param reffer
	 * @return
	 */
	private Document getHomePage(String url,String session,String reffer) {
		Document doc = null;
		for(int i=0;i<tryTimes;i++){
			try {
				doc = Jsoup.connect(url).referrer(reffer).userAgent(userAgent)
					.timeout(timeout).cookie("PHPSESSID", session).get();
			} catch (IOException e) {
				//ignore
			}
			if(doc != null){
				break;
			}
		}
		if(doc == null){
			LogFactory.getLog(Piao123Spider.class).error("error:首页读取失败!");
		}
		return doc;
	}
	
	/**
	 * 取得本次连接的sessionID
	 * @return
	 */
	private String getSessionID() {
		Response response = null;
		for(int i=0;i<tryTimes;i++){
			try {
				response = Jsoup.connect(HOME_PAGE).userAgent(userAgent)
							.timeout(timeout).execute();
			} catch (IOException e) {
				//ignore
			}
			if(response != null){
				break;
			}
		}
		return response.cookie("PHPSESSID");
	}

	public static void main(String[] args) throws IOException, ParseException {
		Piao123Spider spider = new Piao123Spider();
//		spider.setDao(new DataDao());
//		spider.extract();
//		spider.extractEach("http://www.piao123.com/bj/Ticket_12685.html");
//		spider.extractEach("http://www.piao123.com/bj/Ticket_15834.html");
//		spider.extractEach("http://www.piao123.com/bj/Ticket_15122.html");
//		spider.extractEach("http://www.piao123.com/bj/Ticket_15003.html");
//		spider.extractEach("http://www.piao123.com/bj/Ticket_15667.html");
//		spider.extractEach("http://www.piao123.com/sh/Ticket_14031.html");
		spider.extractEach("http://www.piao123.com/bj/Ticket_14906.html");
	}

}
