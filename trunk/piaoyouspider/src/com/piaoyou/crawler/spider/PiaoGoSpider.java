package com.piaoyou.crawler.spider;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class PiaoGoSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(PiaoGoSpider.class);
	private final static String HOME_PAGE = "http://www.piaogo.com/ticketsall.html";
	private final static String BASE_URL = "http://www.piaogo.com/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat otherDate = new SimpleDateFormat("yyyy-M-d HH:mm");
	@Override
	public void extract() {
		Document start = getDoc(HOME_PAGE);
		Elements every  = start.select("table[width=980]:has(span.tall)");
		for (Element each : every) {
			String single = each.select("span.tall").text().trim();
			Elements urls = each.select("td[width=35%] a[href^=t_]");
			for (Element url : urls) {
				try {
					extractEach(url.attr("abs:href"),single);
				} catch (Exception e) {
					log.error(url.attr("abs:href")+Const.separator+single, e);
					continue;
				}
			}
		}
	}
	
	private void extractEach(String url, String single) {
		Document ticket = getDoc(url);
		Show show = new Show();
		show.setAgent_id(agentID);
		String showName = ticket.select("div[style=border-bottom:1px dashed silver;width:100%;margin:6px auto;] span.b").text().trim();
		show.setName(showName);//演出名称
		show.setType(getShowType(single,showName));//演出类型
		Elements path = ticket.select("img[width=240][height=340][src^=upload/tickets/]");
		if(path.size()!=0) {
			show.setImage_path(path.first().attr("abs:src"));//演出图片
		}
		show.setSiteName(ticket.select("span.b1").get(1).text().replaceAll("场馆：", "").trim());//演出场馆
		//演出简介
		show.setIntroduction(PubFun.cleanElement(ticket.select("div[style=margin-top:10px;line-heigt:24px;border:1px solid silver;padding:10px; width:740px;] p")).html());
		show.setTimeAndPrice(getTimeAndPrice(ticket,url));//演出时间及相应时间下的票价情况
		try {
			getDao().saveShow(show);
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}

	private Map<String, List<TicketPrice>> getTimeAndPrice(Document ticket,String url) {
		Elements trLines = ticket.select("tr[bgcolor=#FFFFFF][onmouseout][onmouseover]");
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
			for (Element trLine : trLines) {
				String time1 = trLine.select("td").get(0).text().trim();
				String time2 = trLine.select("td").get(1).text().trim();
				String standardTime = string2Date(time1+" "+time2);
				List<TicketPrice> list = new ArrayList<TicketPrice>();
				timeAndPrice.put(standardTime, list);
				Elements price = trLine.select("a.wz[href^=b_]");
				for (Element yPrice : price) {//有票的时候
					TicketPrice ticketPrice = new TicketPrice();
					ticketPrice.setExist(true);//是否有票
					ticketPrice.setMainURL(url);//买票的主页面
					setPriceAndRemark(ticketPrice,yPrice.text().trim());//设置票价及备注
					ticketPrice.setDetailURL(yPrice.attr("abs:href"));//买票的详细页面
					list.add(ticketPrice);
				}
				Element e = trLine.select("td").get(2);
				e.select("a").remove();
				String[] es = e.text().split("、");
				for (String string : es) {//没票的时候
					if(!"".equals(string)) {
						TicketPrice ticketPrice = new TicketPrice();
						ticketPrice.setExist(false);//是否有票
						setPriceAndRemark(ticketPrice,string);//设置票价及备注
						ticketPrice.setMainURL(url);//买票的主页面
						ticketPrice.setDetailURL("");//买票的详细页面
						list.add(ticketPrice);
					}
				}
			}
		return timeAndPrice;
	}

	private void setPriceAndRemark(TicketPrice ticketPrice, String content) {
		ticketPrice.setPrice(content);
		ticketPrice.setRemark("");
	}

	private String string2Date(String time) {
		String standardTime = null;
		if(time.matches("\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}")) {
			try {
				standardTime = newDate.format(otherDate.parse(time));
			} catch (ParseException e) {
				e.printStackTrace();
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
		} else if ("音乐会".equals(single) || "国家大剧院 音乐厅".equals(single)
				|| "打开艺术之门-2011暑期音乐会".equals(single)) {
			showType = ShowType.SHOW;
		} else if ("话剧 歌剧".equals(single)) {
			showType = ShowType.DRAMA;
		} else if ("儿童亲子".equals(single)) {
			showType = ShowType.CHILDREN;
		} else if ("戏曲综艺".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("舞蹈芭蕾".equals(single)) {
			showType = ShowType.DANCE;
		} else if ("魔术/杂技/马戏".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("梅兰芳大剧院".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("长安大戏院".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("国家大剧院 戏剧场".equals(single)) {
			if (detailType.contains("音乐会")) {
				showType = ShowType.SHOW;
			} else if (detailType.contains("芭蕾舞")) {
				showType = ShowType.DANCE;
			} else if (detailType.contains("演唱会")) {
				showType = ShowType.CONCERT;
			} else {
				showType = ShowType.DRAMA;
			}
		} else if ("国家大剧院 歌剧院".equals(single) || "国家大剧院 小剧场".equals(single)
				|| "人民大会堂演出".equals(single)) {
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
		} else if ("体育—休闲运动".equals(single)) {
			showType = ShowType.SPORTS;
		} else {
			showType = ShowType.OTHER;
		}
		return showType;
	}
	
	public static void main(String[] args) {
		PiaoGoSpider piaoGoSpider = new PiaoGoSpider();
		piaoGoSpider.setDao(new DataDao());
		piaoGoSpider.extract();
	}
}
