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

public class BjMxTicketSpider extends TicketSpider implements SpiderTask {
	
	private static final Log log = LogFactory.getLog(BjMxTicketSpider.class);
	private final static String HOME_PAGE = "http://bj.mxticket.com/alltickets.asp";
	private final static String BASE_URL = "http://bj.mxticket.com/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat otherDate = new SimpleDateFormat("yyyy-M-d HH:mm");
	@Override
	public void extract() {
		Document start = getDoc(HOME_PAGE);
		Elements every  = start.select("table[width=990][bgcolor=#FFFFFF] table[width=978]:has(td[width=799][height=30])");
		for (Element each : every) {
			String single = each.select("span.style23").text().trim();
			Elements urls = each.select("tr a.zt1[href^=ticket_]:eq(0)");
			for (Element url : urls) {
				String url_ = url.attr("href").trim();
			String status = each.select("tr[onmouseout][onmouseover]:has(a[href="+url_+"])").select("td.newszt").get(2).text().trim();
			int status4Show ;
			if(status.equals("已出票")) {
				status4Show = 1;
			} else {
				status4Show = 0;
			} 
				String _url = url.attr("abs:href").trim();
				String title = url.text().trim();
				int showType = getShowType(single,title);
				try {
				extractEach(_url, showType, title,status4Show);
				} catch (Exception e) {
					log.error(_url+Const.separator+showType+Const.separator+title+Const.separator+status4Show, e);
					continue;
				}
			}
		}
	}

	private void extractEach(String url, int showType, String title,int status) {
		Document ticket = getDoc(url);
		Show show = new Show();
		show.setType(showType);//演出类型
		show.setName(title);//演出名称
		show.setAgent_id(agentID);
		show.setStatus(status);//演出状态
		show.setSiteName(ticket.select("span.guest").get(3).text());
		Elements path = ticket.select("img[width=240][height=320][src^=/Attachments/PreviewImage/]");
		if(path.size()!=0) {
			show.setImage_path(path.first().attr("abs:src"));//演出图片
		}
		show.setIntroduction(PubFun.cleanElement(ticket.select("table[width=100%][cellpadding=6] td").first()).html());//演出简介
		show.setTimeAndPrice(getTimeAndPrice(ticket,url));//演出时间及相应时间下的票价情况
		try {
			getDao().saveShow(show);
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}
	private Map<String, List<TicketPrice>> getTimeAndPrice(Document ticket,String url) {
		Elements trLines = ticket.select("tr[onmouseout][onmouseover][bgcolor=#FFFFFF]");
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		for (Element trLine : trLines) {
			if(!trLine.select("td").get(2).text().trim().equals("")) {
				String time = trLine.select("td").get(1).text().trim();
				String standardTime = string2Time(time);
				List<TicketPrice> list = new ArrayList<TicketPrice>();
				timeAndPrice.put(standardTime, list);
				Elements price = trLine.select("td:eq(2) a.link003[href^=myorder.asp?id=]");
				for (Element yPrice : price) {//有票的时候
					TicketPrice ticketPrice = new TicketPrice();
					ticketPrice.setExist(true);//是否有票
					ticketPrice.setMainURL(url);//买票的主页面
					setPriceAndRemark(ticketPrice,yPrice.text().trim());//设置票价及备注
					ticketPrice.setDetailURL(yPrice.attr("abs:href"));//买票的详细页面
					list.add(ticketPrice);
				} 
				//Elements _price_ = trLine.select("td:eq(2) font[size=5]");
				Elements _price_ = trLine.select("td:eq(2) font[color=#999999]");
				for (Element nPrice : _price_) {//没票的时候
					//Element _price = nPrice.nextElementSibling(); 
					if(!nPrice.text().trim().equals("")) {
						if(!nPrice.parent().nodeName().equals("a")) {
							TicketPrice ticketPrice = new TicketPrice();
							ticketPrice.setExist(false);//是否有票
							setPriceAndRemark(ticketPrice,nPrice.text().trim());//设置票价及备注
							ticketPrice.setMainURL(url);//买票的主页面
							ticketPrice.setDetailURL("");//买票的详细页面
							list.add(ticketPrice);
						}
					}
				}
				Elements price_ = trLine.select("td:eq(3) a.link003[href^=myorder.asp?id=]");
				for (Element yPrice : price_) {//有票的时候
					TicketPrice ticketPrice = new TicketPrice();
					ticketPrice.setExist(true);//是否有票
					ticketPrice.setMainURL(url);//买票的主页面
					ticketPrice.setPrice(yPrice.text().trim());//设置票价
					ticketPrice.setRemark(yPrice.nextSibling().toString().replaceAll("）|（|\\(|\\)", ""));//设置备注
					ticketPrice.setDetailURL(yPrice.attr("abs:href"));//买票的详细页面
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

	//格式化成存储的标准数据
	private String string2Time(String time) {
		String standardTime = null;
		if(time.matches("(\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2})(.+)")) {
			Pattern p = Pattern.compile("(\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2})(.+)");
			Matcher m = p.matcher(time);
			m.matches();
			try {
				standardTime = newDate.format(otherDate.parse(m.group(1)));
			} catch (ParseException e) {
				log.error(e.getMessage());
			}
		} else {
			standardTime = time;
		}
		return standardTime;
	}

	//根据演出类型与演出名称详细分类
	private int getShowType(String single, String detailType) {
		int showType = 0;
		if ("演 唱 会".equals(single)) {
			showType = ShowType.CONCERT;
		} else if ("音 乐 会".equals(single)) {
			showType = ShowType.SHOW;
		} else if ("歌剧话剧".equals(single)) {
			showType = ShowType.DRAMA;
		} else if ("儿童亲子".equals(single)) {
			showType = ShowType.CHILDREN;
		} else if ("综艺戏曲".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("舞蹈芭蕾".equals(single)) {
			showType = ShowType.DANCE;
		} else if ("魔术杂技".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("体育竞赛".equals(single)) {
			showType = ShowType.SPORTS;
		} else if ("会展观光".equals(single)) {
			showType = ShowType.HOLIDAY;
		} else {
			showType = ShowType.OTHER;
		}
		return showType;
	}

	public static void main(String[] args) {
		BjMxTicketSpider bjMxTicketSpider = new BjMxTicketSpider();
		bjMxTicketSpider.setDao(new DataDao());
		//bjMxTicketSpider.extract();
		bjMxTicketSpider.extractEach("http://bj.mxticket.com/ticket_3304.html", 1, "123", 1);
		
	}
}
