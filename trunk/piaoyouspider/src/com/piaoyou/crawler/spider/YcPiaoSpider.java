package com.piaoyou.crawler.spider;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
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
public class YcPiaoSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(YcPiaoSpider.class);
	private final static String HOME_PAGE = "http://www.ycpiao.com.cn/second.asp";
	private final static String BASE_URL = "http://www.ycpiao.com.cn/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat otherDate = new SimpleDateFormat("yyyy-M-d HH:mm:ss");
	@Override
	public void extract() {
		Document start = getDoc(HOME_PAGE);
		Elements every = start.select("table[width=1010][style=border:#CCCCCC 1px solid;]");
		for (Element each : every) {
			String single = each.select("td[width=20][valign=middle][style]").text();
			Elements urls = each.select("td[width=296][height=20][align=left] a.links[href^=third.asp?id=]");
			for (Element url : urls) {
				int showType = getShowType(single,url.text().trim());
				try {
					extractEach(url.attr("abs:href"), showType, url.text().trim());
				} catch (Exception e) {
					log.error(url.attr("abs:href")+Const.separator+showType+Const.separator+single, e);
					continue;
				}
			}
		}
	}
	private void extractEach(String url, int showType, String title) {
		Document ticket = getDoc(url);
		if(ticket != null) {
			Elements showSite = ticket.select("td[width=25%][height=30] a.links[href^=cgjj.asp?ycarea=]");
			if(showSite.size()!=0) {
				Show show = new Show();
				show.setAgent_id(agentID);
				show.setName(title);
				show.setType(showType);
				Elements path = ticket.select("td[width=220] img[src^=../uploadImages/],td[width=220] img[src^=uploadImages/]");
				if(path.size()!=0) {
					show.setImage_path(path.get(0).attr("abs:src"));
				}
				show.setSiteName(showSite.first().text());
				show.setIntroduction(PubFun.cleanElement(ticket.select("td[bgcolor=#ffffff] div[class=th1]")).html());
				show.setTimeAndPrice(getTimeAndPrice(ticket,url));
				try {
					getDao().saveShow(show);
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}
		}
	}
	private Map<String, List<TicketPrice>> getTimeAndPrice(Document ticket,String url) {
		Elements trLines = ticket.select("table[width=95%][bgcolor=#cccccc] tr[bgcolor=#FFFFFF]:gt(0)");
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		for (Element trLine : trLines) {
			String standard = trLine.select("td").get(1).text().trim();
				String standardTime = string2Date(standard);
				List<TicketPrice> list = new ArrayList<TicketPrice>();
				timeAndPrice.put(standardTime, list);
				if(!trLine.select("td").get(2).text().trim().equals("")) {
					Elements price = trLine.select("td").get(2).select("a.price[href^=addcart.asp?tempid=]");
					for (Element yPrice : price) {//有票的时候
						TicketPrice ticketPrice = new TicketPrice();
						ticketPrice.setExist(true);
						ticketPrice.setMainURL(url);
						boolean flag = setPriceAndRemark(ticketPrice,yPrice.text());
						ticketPrice.setDetailURL(yPrice.attr("abs:href"));
						if(flag == false) {
							continue;
						}
						list.add(ticketPrice);
					}
					Elements _price = trLine.select("td.price[width=40%] font[color=ff0000]");
					for (Element nPrice : _price) {//没票的时候
						TicketPrice ticketPrice = new TicketPrice();
						ticketPrice.setExist(false);
						boolean flag = setPriceAndRemark(ticketPrice,nPrice.text());
						if(flag == false) {
							continue;
						}
						ticketPrice.setMainURL(url);
						ticketPrice.setDetailURL("");
						list.add(ticketPrice);
					}
				}
			}
		return timeAndPrice;
	}
	private boolean setPriceAndRemark(TicketPrice ticketPrice, String content) {
		boolean flag = true;
		if(content.matches("\\d+")) {
			ticketPrice.setPrice(content);
			ticketPrice.setRemark("");
		} else if(content.matches("(\\D+)(\\d+)")) {
			Pattern p = Pattern.compile("(\\D+)(\\d+)");
			Matcher m = p.matcher(content.replaceAll("）|（|\\(|\\)", ""));
			m.matches();
			ticketPrice.setPrice(m.group(2));
			ticketPrice.setRemark(m.group(1));
		} else if(content.matches("(\\d+)(\\D+)")) {
			Pattern p = Pattern.compile("(\\d+)(\\D+)");
			Matcher m = p.matcher(content.replaceAll("）|（|\\(|\\)", ""));
			m.matches();
			ticketPrice.setPrice(m.group(1));
			ticketPrice.setRemark(m.group(2));
		} else if(content.matches("(\\D+)(\\d+)(.+)")) {
			Pattern p = Pattern.compile("(\\D+)(\\d+)(.+)");
			Matcher m = p.matcher(content);
			m.matches();
			ticketPrice.setPrice(m.group(2));
			ticketPrice.setRemark(m.group(3).replaceAll("）|（|\\(|\\)", ""));
		}  else if(content.matches("(\\d+)(.+)")) {
			Pattern p = Pattern.compile("(\\d+)(.+)");
			Matcher m = p.matcher(content);
			m.matches();
			ticketPrice.setPrice(m.group(1));
			ticketPrice.setRemark(m.group(2).replaceAll("）|（|\\(|\\)", ""));
		} else {
			flag = false;
		}
		return flag;
	}
	private String string2Date(String time) {
		String standardTime = null;
		if(time.matches("\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}.+")) {
			Pattern p = Pattern.compile("(\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2})(.+)");
			Matcher m = p.matcher(time);
			m.matches();
			try {
				standardTime = newDate.format(otherDate.parse(m.group(1)));
			} catch (ParseException e) {
			}
		} else if(time.matches("\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}.+")) {
			Pattern p = Pattern.compile("(\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2})(.+)");
			Matcher m = p.matcher(time);
			m.matches();
			try {
				standardTime = newDate.format(otherDate.parse(m.group(1)));
			} catch (ParseException e) {
			}
		} else {
			standardTime = time;
		}
		return standardTime;
	}
	private int getShowType(String single, String detailType) {
		int showType = 0;
		if ("演唱会".equals(single)) {
			showType = ShowType.CONCERT;
		} else if ("音乐会".equals(single) || "国家大剧院音乐厅".equals(single)
				|| "打开音乐之门(北京音乐厅)".equals(single)) {
			showType = ShowType.SHOW;
		} else if ("北京人艺话剧演出中心".equals(single)) {
			showType = ShowType.DRAMA;
		} else if ("中国儿童艺术剧院".equals(single)) {
			showType = ShowType.CHILDREN;
		} else if ("戏曲综艺".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("舞蹈芭蕾".equals(single)) {
			showType = ShowType.DANCE;
		} else if ("魔术马戏".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("梅兰芳大剧院".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("长安大戏院".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("话剧歌剧音乐剧".equals(single)) {
				showType = ShowType.DRAMA;
		} else if ("国家大剧院戏剧场".equals(single)) {
			if (detailType.contains("音乐会")) {
				showType = ShowType.SHOW;
			} else if (detailType.contains("芭蕾舞")) {
				showType = ShowType.DANCE;
			} else if (detailType.contains("演唱会")) {
				showType = ShowType.CONCERT;
			} else {
				showType = ShowType.DRAMA;
			}
		} else if ("国家大剧院歌剧院".equals(single) || "国家大剧院小剧场".equals(single)) {
			if (detailType.contains("音乐会")) {
				showType = ShowType.SHOW;
			} else if (detailType.contains("芭蕾舞")) {
				showType = ShowType.DANCE;
			} else if (detailType.contains("演唱会")) {
				showType = ShowType.CONCERT;
			} else if (detailType.contains("歌剧") || detailType.contains("话剧")) {
				showType = ShowType.DRAMA;
			} else {
				showType = ShowType.OTHER;
			}
		} else if ("休闲体育".equals(single)) {
			showType = ShowType.SPORTS;
		} else if ("全家总动员".equals(single)||"打开艺术之门―暑期艺术节".equals(single)||"精彩电影".equals(single)) {
			showType = ShowType.HOLIDAY;
		} else if ("雷子乐笑工厂".equals(single)||"东联艺术工社".equals(single)) {
			showType = ShowType.DRAMA;
		} else {
			showType = ShowType.OTHER;
		}
		return showType;
	}

	public static void main(String[] args) {
		YcPiaoSpider ycPiaoSpider = new YcPiaoSpider();
		ycPiaoSpider.setDao(new DataDao());
		ycPiaoSpider.extract();
	}
}
