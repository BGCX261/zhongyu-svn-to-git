package com.piaoyou.crawler.spider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
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

public class BaiduPiaoSpider extends TicketSpider implements SpiderTask {
	public static String BASE_URL = "http://piao.youa.baidu.com/";
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static Log log = LogFactory.getLog(BaiduPiaoSpider.class);
	private final static String agentID = new DataDao().searchBySQL(
			"select agency_id from t_agency_info where agency_url=?", BASE_URL)
			.get(0).get("agency_id").toString();

	@Override
	public void extract() {
		Document doc = getDoc(BASE_URL);
		Iterator<Element> aList = doc.select("#itemWy").get(0).select("div>ul>li>a").iterator();
		while (aList.hasNext()) {
			Element eleA = aList.next();
			String sort = eleA.text();
			String url = eleA.absUrl("href").trim();
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
			} else if (sort.contains("休闲") || sort.contains("旅游")
					|| sort.contains("电影")) {
				sortType = ShowType.HOLIDAY;
			}
			try {
				processBySort(url, sortType);
				Thread.sleep(200000);
			} catch (Exception e) {
				log.error(Const.concat(url, sortType), e);
			}
		}

	}

	public void processBySort(String url, int sortType) {
		Document doc = getDoc(url);
		Iterator<Element> itA = doc.select("div.rec-c>ul>li>div.cls>a")
				.iterator();
		while (itA.hasNext()) {
			Element eleA = itA.next();
			String lastUrl = eleA.absUrl("href").trim();
			try {
				processLastPage(lastUrl, sortType);
			} catch (Exception ee) {
				log.error(Const.concat(lastUrl, sortType), ee);
			}
		}
		Element eleHasNextPage = doc.select(".global-page-next").first();
		if (eleHasNextPage != null
				&& eleHasNextPage.tagName().trim().equals("a")) {
			String lastUrl = eleHasNextPage.absUrl("href").trim();
			try {
				processBySort(lastUrl, sortType);
				Thread.sleep(500);
			} catch (Exception ee){
				log.error(Const.concat(lastUrl, sortType), ee);
			}
		} else {
			return;
		}
	}

	public void processLastPage(String url, int sortType) {
		Document doc = getDoc(url);
		Element eleDis = doc.select("div.pw-mod.mod-tinfo").get(0);
		String title = eleDis.select("div.bd>h2").first().ownText();
		String imagePath = eleDis.select(".tinfo-img>img").first().absUrl("src").trim();
		String site = eleDis.select(".pw-tinfo>ul>li:eq(1)>span.v").first().ownText();
		String info = PubFun.cleanElement(doc.select("div.bd.richtext.pw-richtext").first()).html();
		title = title.replaceAll("\\[[^\\]]*\\]", "");
		Map<String, List<TicketPrice>> map = new HashMap<String, List<TicketPrice>>();
		Iterator<Element> timeIt = doc.select("#time>option").iterator();
		while (timeIt.hasNext()) {
			Element eleTime = timeIt.next();
			String showTime = eleTime.val().trim();
			if(showTime.contains("待定")){
				continue;
			}
			try {
				 showTime = newDate.format(oldDate.parse(showTime));
			} catch (Exception e1) {
					//TODO	时间暂时不处理
			}
			Element elePrice = doc.select("#table_ticket tr:eq(1)>td[data-time*=" + showTime + "]").first();
			Iterator<Element> itPriceList = elePrice.select("span:gt(0)").iterator();
			List<TicketPrice> list = new ArrayList<TicketPrice>();
			while (itPriceList.hasNext()) {
				Element eleSpan = itPriceList.next();
				TicketPrice ticket = new TicketPrice();
				ticket.setMainURL(url);
				ticket.setExist(eleSpan.attr("class").trim().equals("pri-1") ? false: true);
				ticket.setPrice(eleSpan.attr("price").trim());
				ticket.setRemark(eleSpan.text().replaceAll("\\d|\\(|\\)|\\*", ""));
				list.add(ticket);
			}
			map.put(showTime, list);
		}
		Show show = new Show();
		show.setAgent_id(agentID);
		show.setImage_path(imagePath);
		show.setName(title);
		show.setSiteName(site);
		show.setTimeAndPrice(map);
		show.setType(sortType);
		show.setIntroduction(info);
		try {
			getDao().saveShow(show);
		} catch (Exception ee) {
				ee.printStackTrace();
		}
	}

	public static void main(String[] args) {
	  BaiduPiaoSpider spider=new BaiduPiaoSpider();
	  spider.setDao(new DataDao());
	  spider.extract();
	}

	

}
