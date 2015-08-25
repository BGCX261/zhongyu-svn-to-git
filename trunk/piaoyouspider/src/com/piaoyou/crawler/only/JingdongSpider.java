package com.piaoyou.crawler.only;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
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

public class JingdongSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(JingdongSpider.class);
	private final static String BASE_URL = "http://www.360buy.com/";
	private final static String home_URL = "http://www.360buy.com/products/4938-4943-4945.html";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy年MM月dd hh:mm");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	@Override
	public void extract() {
		Document doc = getDoc(home_URL);
		Iterator<Element> liList = doc.select("div.item.current>ul>li").iterator();
		while (liList.hasNext()) {
			Element eleLi = liList.next();
			String sort = eleLi.select("a").first().text().trim();
			String url = eleLi.select("a").first().absUrl("href").trim();
			int sortType = getSortType(sort);
			Map<String, Show> map = new HashMap<String, Show>();
			try {
				processSortPage(url, sortType, map);
			} catch (Exception ee) {
				log.error(Const.concat(url, sortType, map), ee);
			}
			try {
				for (Show show : map.values()) {
					this.getDao().saveShow(show);
				}
			} catch (Exception ee) {

			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JingdongSpider spider=new JingdongSpider();
		spider.setDao(new DataDao());
		spider.extract();
	    //spider.processLastPage("http://www.360buy.com/product/1000544911.html" ,"123",1, new HashMap<String,Show>());
	}
	
	public void processSortPage(String url,int sortType,Map<String,Show> map){
		boolean flag = false;
		Document doc = getDoc(url);
		Iterator<Element> itli = doc.select("div#plist>ul>li").iterator();
		while (itli.hasNext()) {
			flag = true;
			Element li = itli.next();
			String name = li.select("img").first().attr("alt").trim();
			String href = li.select("a").first().absUrl("href").trim();
			try {
				processLastPage(href, name, sortType, map);
			} catch (Exception ee) {
				ee.printStackTrace();
				log.error(Const.concat(href, name, sortType, map), ee);
			}
		}
		Element ele = doc.select("div.pagin.fr>a.next").first();
		if (ele != null) {
			if (flag) {
				String nexthref = ele.absUrl("href").trim();
				processSortPage(nexthref, sortType, map);
			}
		} else {
			return;
		}
	}
	/***
	 * 处理最终最终页面
	 * @param url
	 * @param name
	 * @param sortType
	 * @param map
	 */
	public void processLastPage(String url,String name,int sortType,Map<String,Show> map){
		Document doc = getDoc(url);
		name = name.replaceAll("（[^）]*）", "").replaceAll("\\([^\\)]*\\)", "").trim();
		name = name.replace("上午场", "").replace("下午场", "").replace("晚场", "").replace("午场", "").trim();
		name = name.replaceAll("[\\d]{1,4}年[\\d]{1,2}月[\\d]{1,2}日", "").trim();
		name = name.replaceAll("[\\d]{1,2}月[\\d]{1,2}日", "").trim();
		if (map.keySet().contains(name)) {
			Show show = map.get(name);
			String showTime = doc.select("span:contains(具体时间)").first().text().replace("具体时间：", "").trim();
			showTime = showTime.replace("：", ":").replace(" ", "");
			String date = PubFun.getRegex("([\\d]{1,4}年[\\d]{1,2}月[\\d]{1,2})",showTime, 1);
			String time = PubFun.getRegex("[^\\d]*([\\d]{1,2}:[\\d]{1,2})[^\\d]*", showTime, 1);
			if (date != null && time != null) {
				showTime = date + " " + time;
			}
			try {
				showTime = newDate.format(oldDate.parse(showTime));
			} catch (Exception ee) {

			}
			List<TicketPrice> listPrice = new ArrayList<TicketPrice>();
			Iterator<Element> itPrice = doc.select("#choose>dl.size>dd a").iterator();
			while (itPrice.hasNext()) {
				Element elePrice = itPrice.next();
				String price = elePrice.text().trim().replace("元", "");
				String link = elePrice.absUrl("href").trim();
				TicketPrice ticket = new TicketPrice();
				ticket.setMainURL(url);
				ticket.setExist(true);
				ticket.setPrice(price);
				ticket.setDetailURL(link);
				Document priceDoc = getDoc(link);
				String script=priceDoc.select("#storeinfocontainer>script").first().html();
				String skuid=PubFun.getRegex("sid[^\"]*\"([^\"]*)\"", script, 1).trim();
				String priceUrl="http://price.360buy.com/stocksoa/StockHandler.ashx?callback=getProvinceStockCallback&type=provincestock&skuid="+skuid+"&provinceid=1";
				String state=getState(priceUrl);
				if(state.contains("\u65e0\u8d27")){
					ticket.setExist(false);
				}
				listPrice.add(ticket);
			}
			Map<String, List<TicketPrice>> showTimePrice = show.getTimeAndPrice();
			showTimePrice.put(showTime, listPrice);
		} else {
			Show show = new Show();
			show.setName(name);
			String showTime = doc.select("span:contains(具体时间)").first().text().replace("具体时间：", "").trim();
			showTime = showTime.replace("：", ":").replace(" ", "");
			String date = PubFun.getRegex("([\\d]{1,4}年[\\d]{1,2}月[\\d]{1,2})",showTime, 1);
			String time = PubFun.getRegex("[^\\d]*([\\d]{1,2}:[\\d]{1,2})[^\\d]*", showTime, 1);
			if (date != null && time != null) {
				showTime = date + " " + time;
			}
			try {
				showTime = newDate.format(oldDate.parse(showTime));
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			String site = null;
			Element siteEle = doc.select("span:contains(演出地点：)").first();
			if (siteEle == null) {
				if (name.contains("法籍钢琴家")) {
					site = "北京音乐厅";
				}
			} else {
				site = siteEle.text().replace("演出地点：", "");
			}
			if (site == null) {
				return;
			}
			String pic = doc.select("#spec-n1>img").first().absUrl("src").trim();
			Element infoEg=doc.select("#i-detail>div.content>div>div>div>div").first();
			Element info=null;
			if(infoEg!=null){
				 info = PubFun.cleanElement(infoEg);
			}
			if (null == info) {
				info = PubFun.cleanElement(doc.select("div.mc.fore.tabcon>div.content").first());
			}
			String instruction = info.html();
			show.setSiteName(site);
			show.setAgent_id(agentID);
			show.setImage_path(pic);
			show.setIntroduction(instruction);
			show.setType(sortType);
			Map<String, List<TicketPrice>> showTimePrice = new HashMap<String, List<TicketPrice>>();
			List<TicketPrice> listPrice = new ArrayList<TicketPrice>();
			Iterator<Element> itPrice = doc.select("#choose>dl.size>dd a").iterator();
			while (itPrice.hasNext()) {
				Element elePrice = itPrice.next();
				String price = elePrice.text().trim().replace("元", "");
				String link = elePrice.absUrl("href").trim();
				TicketPrice ticket = new TicketPrice();
				ticket.setMainURL(url);
				ticket.setExist(true);
				ticket.setPrice(price);
				ticket.setDetailURL(link);
				Document priceDoc = getDoc(link);
				String script=priceDoc.select("#storeinfocontainer>script").first().html();
				String skuid=PubFun.getRegex("sid[^\"]*\"([^\"]*)\"", script, 1).trim();
				String priceUrl="http://price.360buy.com/stocksoa/StockHandler.ashx?callback=getProvinceStockCallback&type=provincestock&skuid="+skuid+"&provinceid=1";
				String state=getState(priceUrl);
				String stock=PubFun.getRegex("getProvinceStockCallback\\(([^\\)]*)\\)", state, 1);
				JSONObject j = JSONObject.fromObject(stock).getJSONObject("stock");
				String StockStateName=j.getString("StockStateName");
				if(StockStateName.toUpperCase(Locale.ENGLISH).contains("\u65e0\u8d27".toUpperCase(Locale.ENGLISH))){
					ticket.setExist(false);
				}
				listPrice.add(ticket);
			}
			showTimePrice.put(showTime, listPrice);
			show.setTimeAndPrice(showTimePrice);
			map.put(name, show);
		}
	}
	public int getSortType(String sort){
		int sortType = ShowType.OTHER;
		if (sort.contains("演唱")) {
			sortType = ShowType.CONCERT;
		} else if (sort.contains("音乐") && !sort.contains("剧")) {
			sortType = ShowType.SHOW;
		} else if (sort.contains("话剧") || sort.contains("歌剧")
				|| sort.contains("音乐剧")) {
			sortType = ShowType.DRAMA;
		} else if (sort.contains("芭蕾") || sort.contains("舞蹈")
				|| sort.contains("舞")) {
			sortType = ShowType.DANCE;
		} else if (sort.contains("戏剧") || sort.contains("戏曲")
				|| sort.contains("二人转") || sort.contains("魔术")
				|| sort.contains("杂技") || sort.contains("相声")
				|| sort.contains("小品") || sort.contains("马戏")) {
			sortType = ShowType.OPERA;
		} else if (sort.contains("儿童亲子")) {
			sortType = ShowType.CHILDREN;
		} else if (sort.contains("体育")) {
			sortType = ShowType.SPORTS;
		} else if (sort.contains("旅游") || sort.contains("电影")) {
			sortType = ShowType.HOLIDAY;
		}
		return sortType;
	}
	
	private  static String getState(String url) {
		Response response = null;
		for(int i=0;i<tryTimes;i++){
			try {
				response = Jsoup.connect(url).userAgent(userAgent)
							.timeout(timeout).execute();
			} catch (IOException e) {
			}
			if(response != null){
				break;
			}
		}
		return response.body();
	}
}
