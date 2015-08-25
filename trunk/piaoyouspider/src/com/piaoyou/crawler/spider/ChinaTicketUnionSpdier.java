package com.piaoyou.crawler.spider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class ChinaTicketUnionSpdier extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(ChinaTicketUnionSpdier.class);
	private final static String BASE_URL = "http://www.piao5.com";
	private final static String home_URL = "http://www.piao5.com/all.html";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
		.get(0).get("agency_id").toString();
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	@Override
	public void extract() {
		Document doc = getDoc(home_URL);
		Iterator<Element> itList = doc.select(
				"table[id]>tr:eq(1)>td:eq(0)>span").iterator();
		while (itList.hasNext()) {
			Element eleIt = itList.next();
			String sort = eleIt.text().trim();
			Element temp = eleIt.parent().nextElementSibling();
			Iterator<Element> showEles = temp.select("table>tr:gt(0)")
					.iterator();
			while (showEles.hasNext()) {
				Element showEle = showEles.next();
				String showName = showEle.select("a").first().text().trim();
				int sortType = getSortType(sort);
				if (sortType == ShowType.OTHER) {
					sortType = getSortType(sort, showName);
				}
				String lastUrl = showEle.select("a").first().absUrl("href").trim();
				System.out.println("lastUrl:"+lastUrl);
				try {
					processlastPage(lastUrl, sortType);
				} catch (Exception ee) {
					log.error(Const.concat(lastUrl, sortType), ee);
				}
			}
		}
	}

	/**
	 * 处理最终页面
	 * 
	 * @param url
	 * @param sortType
	 */
	public void processlastPage(String url, int sortType) {
		System.out.println(url);
		Document doc = getDoc(url);
		String showName = doc.select("span.ptitle").first().text().trim();
		String site = doc.select("div.ptitlediv>div>span:contains(场馆)").first().select("a").text().trim();
		String pic = doc.select("div.ptitlediv").first().parent().lastElementSibling().lastElementSibling().select("img").first().absUrl("src").trim();
		String info = doc.select("div:containsOwn(详细介绍)").first().parent().html();
		Show show = new Show();
		show.setImage_path(pic);
		show.setIntroduction(info);
		show.setName(showName);
		show.setSiteName(site);
		show.setType(sortType);
		Map<String, List<TicketPrice>> map = getMap(doc, url);
		show.setTimeAndPrice(map);
		show.setAgent_id(agentID);
		try {
			this.getDao().saveShow(show);
		} catch (Exception ee) {

		}
	}

	/**
	 * 获取时间和票价
	 * 
	 * @param doc
	 * @param url
	 * @return
	 */
	public Map<String, List<TicketPrice>> getMap(Document doc, String url) {
		Map<String, List<TicketPrice>> map = new HashMap<String, List<TicketPrice>>();
		Element temp = doc.select("table[width=740] tr:contains(价格(订票请点票价))").first().parent().parent();
		Iterator<Element> itlist = temp.select("tr:gt(0)").iterator();
		while (itlist.hasNext()) {
			Element ele = itlist.next();
			String showTime = ele.select("td:eq(0)").first().text().trim();
			try {
				showTime = newDate.format(oldDate.parse(showTime));
			} catch (Exception ee) {

			}
			List<TicketPrice> list = new ArrayList<TicketPrice>();
			Element priceEle = ele.select("td:eq(2)").first();
			Iterator<Element> priceList = priceEle.select("a").iterator();
			while (priceList.hasNext()) {
				TicketPrice ticket = new TicketPrice();
				Element eleA = priceList.next();
				String price = eleA.text().trim();
				setPriceAndRemark(ticket, price);
				ticket.setExist(true);
				ticket.setMainURL(url);
				String link = eleA.absUrl("href").trim();
				ticket.setDetailURL(link);
				list.add(ticket);
			}
			for (Element clear : priceEle.select("a")) {
				clear.remove();
			}
			String noPriceText = priceEle.text();
			if (!"".equals(noPriceText.trim())) {
				String tt[] = noPriceText.trim().split(" ");
				for (String noP : tt) {
					TicketPrice ticket = new TicketPrice();
					ticket.setDetailURL(url);
					ticket.setMainURL(url);
					ticket.setExist(false);
					setPriceAndRemark(ticket, noP);
					list.add(ticket);
				}
			}
			map.put(showTime, list);
		}
		return map;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ChinaTicketUnionSpdier spider = new ChinaTicketUnionSpdier();
		spider.setDao(new DataDao());
		spider.extract();
		//spider.processlastPage("http://www.piao5.com/t_1557.html", 1);
	}

	public int getSortType(String sort) {
		int sortType = ShowType.OTHER;
		if (sort.contains("国家大剧院戏剧场") || sort.contains("国家大剧院音乐厅")
				|| sort.contains("国家大剧院歌剧院") || sort.contains("国家大剧院戏剧场")
				|| sort.contains("国家大剧院小剧场") || sort.contains("梅兰芳大剧院")
				|| sort.contains("长安大戏院") || sort.contains("北京9剧场")
				|| sort.contains("北京音乐厅") || sort.contains("北大百年讲堂演出")) {
			return sortType;
		}
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

	public int getSortType(String sort, String name) {
		int sortType = ShowType.OTHER;
		if (sort.contains("国家大剧院戏剧场")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("国家大剧院音乐厅")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("国家大剧院歌剧院")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("国家大剧院戏剧场")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("国家大剧院小剧场")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("梅兰芳大剧院")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("长安大戏院")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("北京9剧场")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("北京音乐厅")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("北大百年讲堂演出")) {
			sortType = getChildSortType(name);
		}
		return sortType;
	}

	public int getChildSortType(String name) {
		int sortType = ShowType.OTHER;
		if (name.contains("音乐会")) {
			sortType = ShowType.SHOW;
		}
		if (name.contains("芭蕾") || name.contains("舞")) {
			sortType = ShowType.DANCE;
		}
		if (name.contains("京剧") || name.contains("戏")) {
			sortType = ShowType.OPERA;
		} else if (name.contains("演唱会")) {
			sortType = ShowType.CONCERT;
		} else if (name.contains("歌剧") || name.contains("话剧")
				|| name.contains("剧")) {
			sortType = ShowType.DRAMA;
		}
		return sortType;
	}

	private void setPriceAndRemark(TicketPrice ticketPrice, String content) {
		if (content.matches("\\d+")) {
			ticketPrice.setPrice(content);
			ticketPrice.setRemark("");
		} else if (content.matches("(\\D+)(\\d+)")) {
			Pattern p = Pattern.compile("(\\D+)(\\d+)");
			Matcher m = p.matcher(content.replaceAll("）|（|\\(|\\)", ""));
			m.matches();
			ticketPrice.setPrice(m.group(2));
			ticketPrice.setRemark(m.group(1));
		} else if (content.matches("(\\d+)(\\D+)")) {
			Pattern p = Pattern.compile("(\\d+)(\\D+)");
			Matcher m = p.matcher(content.replaceAll("）|（|\\(|\\)", ""));
			m.matches();
			ticketPrice.setPrice(m.group(1));
			ticketPrice.setRemark(m.group(2));
		} else if (content.matches("(\\D+)(\\d+)(.+)")) {
			Pattern p = Pattern.compile("(\\D+)(\\d+)(.+)");
			Matcher m = p.matcher(content);
			m.matches();
			ticketPrice.setPrice(m.group(2));
			ticketPrice.setRemark(m.group(3).replaceAll("）|（|\\(|\\)", ""));
		} else if (content.matches("(\\d+)(.+)")) {
			Pattern p = Pattern.compile("(\\d+)(.+)");
			Matcher m = p.matcher(content);
			m.matches();
			ticketPrice.setPrice(m.group(1));
			ticketPrice.setRemark(m.group(2).replaceAll("）|（|\\(|\\)", ""));
		}
	}
}
