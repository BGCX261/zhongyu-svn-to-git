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

public class ZsPiaoSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(ZsPiaoSpider.class);
	private final static String HOME_PAGE = "http://www.zspiao.com/all.html";
	private final static String BASE_URL = "http://www.zspiao.com/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat otherDate = new SimpleDateFormat("yyyy-M-d HH:mm");
	@Override
	public void extract() {
		Document start = getDoc(HOME_PAGE);
		Elements every  = start.select("table[width=980][cellspacing=0][cellpadding=0][align=center]:has(span.tall)");
		for (Element e : every) {
			String single = e.select("span.tall").text().trim();
			Elements urls = e.select("td[width=35%] a[href^=t_]");
			for (Element url : urls) {
				String title = url.text().trim();
				String url4Param = url.attr("abs:href");
				int showType = getShowType(single,title);
				try {
					extractEach(url4Param, showType, title);
				} catch (Exception ex) {
					log.error(url4Param+Const.separator+showType+Const.separator+title, ex);
					continue;
				}
			}
		}
		
	}

	private void extractEach(String url, int showType, String title) {
		Document ticket = getDoc(url);
		Show show = new Show();
		show.setName(title);//演出标题
		show.setType(showType);//演出类型
		show.setAgent_id(agentID);
		show.setWatermark(true);//有水印
		Elements path = ticket.select("img[width=180][height=240][src^=upload/tickets/");
		if(path.size()!=0) {
			show.setImage_path(path.first().attr("abs:src"));//演出图片
		}
		show.setSiteName(ticket.select("a[href^=cg_] font[color=#003366]").text());//演出场馆
		show.setIntroduction(PubFun.cleanElement(ticket.select("span.b+div[style=line-height:20px;width:720px;overflow:hidden;]").get(0)).html());//演出简介
		show.setTimeAndPrice(getTimeAndPrice(ticket,url));//演出时间及相应时间下的票价情况
		try {
			getDao().saveShow(show);
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}
	//每个时间对应下的具体票价的情况
	private Map<String, List<TicketPrice>> getTimeAndPrice(Document ticket,String url) {
		Elements trLines = ticket.select("tr[bgcolor=#FFFFFF][onmouseout][onmouseover]");
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		for (Element trLine : trLines) {
			String time1 = trLine.select("td").get(0).text().trim();
			String time2 = trLine.select("td").get(1).text().trim();
			String time = time1+" "+time2;
			String standardTime = string2Date(time);
			List<TicketPrice> list = new ArrayList<TicketPrice>();
			timeAndPrice.put(standardTime, list);
			Elements price = trLine.select("td").get(2).select("a.wz[href^=b_]");
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
	//对各种形式的标价与备注进行集中处理
	private void setPriceAndRemark(TicketPrice ticketPrice, String content) {
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
		}
	}
	//对日期做处理，转化成标准的存储格式
	private String string2Date(String time) {
		String standardTime = null;
		if(time.matches("\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}:\\d{1,2}")||time.matches("\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}")) {
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
	//对类型进行详细分类
	private int getShowType(String single, String detailType) {
		int showType = 9;
		if("演唱会".equals(single)) {
			showType = ShowType.CONCERT;
		} else if("音乐会".equals(single)) {
			showType = ShowType.SHOW;
		} else if("话剧歌剧".equals(single)) {
			showType = ShowType.DRAMA;
		} else if("舞蹈芭蕾".equals(single)) {
			showType = ShowType.DANCE;
		} else if("体育休闲".equals(single)) {
			showType = ShowType.SPORTS;
		} else if("戏曲综艺".equals(single)) {
			showType = ShowType.OPERA;
		} else if("儿童亲子".equals(single)) {
			showType = ShowType.CHILDREN;
		} else if("魔术马戏".equals(single)) {
			showType = ShowType.OPERA;
		} else if("国家大剧院".equals(single)) {
			if (detailType.contains("音乐会")) {
				showType = ShowType.SHOW;
			} else if (detailType.contains("芭蕾舞")) {
				showType = ShowType.DANCE;
			} else if (detailType.contains("演唱会")) {
				showType = ShowType.CONCERT;
			} else if (detailType.contains("话剧")) {
				showType = ShowType.DRAMA;
			} else if (detailType.contains("歌剧")) {
				showType = ShowType.DRAMA;
			} else {
				showType = ShowType.OTHER;
			}
		} else {
			showType = ShowType.OTHER;
		}
		return showType;
	}

	public static void main(String[] args) {
		ZsPiaoSpider zsPiaoSpider = new ZsPiaoSpider();
		zsPiaoSpider.setDao(new DataDao());
		zsPiaoSpider.extractEach("http://www.zspiao.com/t_1120.html", ShowType.CHILDREN, "喜羊羊与灰太狼鸟巢嘉年华");
	}

}
