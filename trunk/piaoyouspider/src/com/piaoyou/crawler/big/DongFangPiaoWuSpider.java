package com.piaoyou.crawler.big;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
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

public class DongFangPiaoWuSpider extends TicketSpider implements SpiderTask {
	public static String BASE_URL="http://www.ticket2010.com/";
	public static Log log=LogFactory.getLog(DongFangPiaoWuSpider.class);
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	@Override
	public void extract() {
		Document doc = getDoc(BASE_URL);
		Iterator<Element> alist = doc.select("div.main_left>ul.menu_left").get(0).select("li>a").iterator();
		while (alist.hasNext()) {
			Element eleA = alist.next();
			String url = eleA.absUrl("href");
			String sort = eleA.childNode(0).outerHtml().replace("&nbsp;", "");
			int sortType = ShowType.OTHER;
			if (sort.contains("演唱")) {
				sortType = ShowType.CONCERT;
			} else if (sort.contains("音乐") && !sort.contains("剧")) {
				sortType = ShowType.SHOW;
			} else if (sort.contains("话剧") || sort.contains("歌剧")|| sort.contains("音乐剧")) {
				sortType = ShowType.DRAMA;
			} else if (sort.contains("芭蕾") || sort.contains("舞蹈")|| sort.contains("舞")) {
				sortType = ShowType.DANCE;
			} else if (sort.contains("戏剧") || sort.contains("戏曲")|| sort.contains("二人转") || sort.contains("魔术")|| sort.contains("杂技") || sort.contains("相声")|| sort.contains("小品") || sort.contains("马戏")) {
				sortType = ShowType.OPERA;
			} else if (sort.contains("儿童亲子")) {
				sortType = ShowType.CHILDREN;
			} else if (sort.contains("体育")) {
				sortType = ShowType.SPORTS;
			} else if (sort.contains("休闲") || sort.contains("旅游")|| sort.contains("电影")) {
				sortType = ShowType.HOLIDAY;
			}
			try {
				processBySort(url, sortType);
			} catch (Exception e) {
				log.error(Const.concat(url, sortType), e);
			}
		}
	}

	public void processBySort(String url, int sortType) {
		Document doc = getDoc(url);
		Element pageElement = doc.select("div.main.bgred>div.main_left2>div[align=right]:has(span)").first();
		if (pageElement == null) {
			Iterator<Element> ita = doc.select("ul.main_left2content>li>div>a").iterator();
			while (ita.hasNext()) {
				Element elea = ita.next();
				String lastPageUrl = elea.absUrl("href").trim();
				try {
					processLastPage(lastPageUrl, sortType);
				} catch (Exception ee) {
					log.error(Const.concat(lastPageUrl, sortType), ee);
				}
			}
		} else {
			String html = pageElement.childNode(0).outerHtml();
			int pageCount = Integer.parseInt(PubFun.getRegex("[^共]*共([\\d]+)页[^页]*", html, 1));
			for (int i = 1; i <= pageCount; i++) {
				String pageUrl = url + "&page=1";
				Document pageDoc = getDoc(pageUrl);
				Iterator<Element> ita = pageDoc.select("ul.main_left2content>li>div>a").iterator();
				while (ita.hasNext()) {
					Element elea = ita.next();
					String lastPageUrl = elea.absUrl("href").trim();
					try {
						processLastPage(lastPageUrl, sortType);
					} catch (Exception ee) {
						log.error(Const.concat(lastPageUrl, sortType), ee);
					}
				}
			}
		}
	}
	
	public void processLastPage(String url,int sort) throws Exception {
		Document doc = getDoc(url);
		Element element = doc.select("div.cornercontent>div").get(0).select("table").first();
		String title = element.select("tr:eq(0)>td>h2").first().text();
		String picUrl = element.select("tr:eq(0)>td>img").first().absUrl("src");
		String site = element.select("tr:eq(2)>td>a").first().text();
		boolean isFirstAgency = (element.select("img[align=right]").size()==0)?false:true;
		Elements elelistp = doc.select("div.cornercontent>div").get(0).select("p");
		for (Element elet : elelistp.select("p:contains(订票专区),p:contains(演出时间),p:contains(商品专区),p:contains(商品款式)")) {
			elelistp.remove(elet);
		}
		String introduction = PubFun.cleanElement(elelistp).text();
		Iterator<Element> eleIt = doc.select("select[name=drp_ShowDate]>option:gt(0)").iterator();
		Map<String, List<TicketPrice>> map = new HashMap<String, List<TicketPrice>>();
		while (eleIt.hasNext()) {
			List<TicketPrice> list = new ArrayList<TicketPrice>();
			Element ele = eleIt.next();
			String value = ele.attr("value");
			String dateAndTime = ele.text();
			String __VIEWSTATE = doc.select("#__VIEWSTATE").first().val();
			dateAndTime = dateAndTime.substring(0, 16);
			Document returnDoc = Jsoup.connect(url).data("__EVENTTARGET","__doPostBack").data("drp_ShowDate", value).data("__VIEWSTATE", __VIEWSTATE).data("ScriptManager1","UpdatePanel1|drp_ShowDate").post();
			Iterator<Element> trPricelist = returnDoc.select("table#gv_Ticket>tr:gt(0)").iterator();
			while (trPricelist.hasNext()) {
				TicketPrice ticket = new TicketPrice();
				Element eleTrPrice = trPricelist.next();
				String price = eleTrPrice.select("td>span[id$=Price]").first().text();
				ticket.setPrice(price);
				String state = eleTrPrice.select("td>span[id$=State]").first().text();
				ticket.setRemark(state);
				Element eleHasTicket = eleTrPrice.select("td>input[name=txt_BuyCount]").first();
				if (eleHasTicket != null) {
					ticket.setExist(true);
				} else {
					ticket.setExist(false);
				}
				ticket.setMainURL(url);
				list.add(ticket);
			}
			map.put(dateAndTime, list);
		}
		Show show = new Show();
		show.setAgent_id(agentID);
		show.setFirstAgent(isFirstAgency);
		show.setImage_path(picUrl);
		show.setIntroduction(introduction);
		show.setName(title);
		show.setSiteName(site);
		show.setType(sort);
		show.setTimeAndPrice(map);
		try {
			getDao().saveShow(show);
//			System.out.println(show);
		} catch (Exception ee) {

		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		DongFangPiaoWuSpider spider=new DongFangPiaoWuSpider();
		spider.setDao(new DataDao());
		spider.extract();
//		spider.processLastPage("http://www.ticket2010.com/program.aspx?id=7829",1);
	}

}
