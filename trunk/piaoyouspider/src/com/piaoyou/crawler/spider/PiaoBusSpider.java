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

public class PiaoBusSpider extends TicketSpider implements SpiderTask {
	
	private static final Log log = LogFactory.getLog(PiaoBusSpider.class);
	private final static String HOME_PAGE = "http://www.piaobus.com/allpiao.asp";
	private final static String BASE_URL = "http://www.piaobus.com/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat otherDate = new SimpleDateFormat("yyyy年M月d日HH:mm");
	@Override
	public void extract() {
		Document start = getDoc(HOME_PAGE);
		Elements every  = start.select("tr[bgcolor=#D6E6F3]");
		for (Element e : every) {
			String single = e.previousElementSibling().select("span.STYLE1 b:eq(1)").text().trim();
			Element e1 = e;
			while(e1.nextElementSibling().hasAttr("bgcolor")) {
				e1 = e1.nextElementSibling();
				String title = e1.select("a[href^=ticket_]").text().trim();
				int showType = getShowType(single,title);
				String url4Param = e1.select("a[href^=ticket_]").first().attr("abs:href");
				try {
					extractEach(url4Param, showType, title);
				} catch (Exception ex) {
					log.error(url4Param+Const.separator+showType+Const.separator+title, ex);
					continue;
				}
				if(e1.nextElementSibling() == null) {
					break;
				}
			}
		}
		
	}
	private void extractEach(String url, int showType, String title) {
		Document ticket = getDoc(url);
		Show show = new Show();
		show.setAgent_id(agentID);
		show.setName(title);//演出名称
		show.setType(showType);//演出类型
		show.setSiteName(ticket.select("a[href^=theater_]").first().text());//演出场馆
		Elements path = ticket.select("img[width=240][height=133][src^=/UpFiles/");
		if(path.size()!=0) {
			show.setImage_path(path.first().attr("abs:src"));//演出图片
		}
		show.setIntroduction(PubFun.cleanElement(ticket.select("td.flan[height=100]").first()).html());//演出简介
		show.setTimeAndPrice(getTimeAndPrice(ticket,url));//演出时间及相应时间下的票价情况
		try {
			getDao().saveShow(show);
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}
	//处理演出时间及相应时间下的票价情况
	private Map<String, List<TicketPrice>> getTimeAndPrice(Document ticket,String url) {
		boolean flag = false;
		int num = 2;
		String standard = ticket.select("table[width=100%][bgcolor=#EFEFEF] tr[bgcolor=#D7D7B0]").select("td").get(2).text().trim();
		Elements trLines = ticket.select("tr[bgcolor=#EEEEEE]");
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		if(standard.equals("套票")) {
			flag = true;
			num = 3;
		}
		for (Element trLine : trLines) {
			if(!trLine.select("td").get(2).text().trim().equals("")) {
				String time = trLine.select("td").get(0).text().trim(); 
				String standardTime = string2Date(time);
				List<TicketPrice> list = new ArrayList<TicketPrice>();
				timeAndPrice.put(standardTime, list);
				Elements price = trLine.select("td").get(num).select("a[href^=cart.asp?productid=]");
				for (Element yPrice : price) {//有票的时候
					TicketPrice ticketPrice = new TicketPrice();
					ticketPrice.setExist(true);//是否有票
					ticketPrice.setMainURL(url);//买票的主页面
					setPriceAndRemark(ticketPrice,yPrice.text().trim());//设置票价及备注
					ticketPrice.setDetailURL(yPrice.attr("abs:href"));//买票的详细页面
					list.add(ticketPrice);
				}
				Elements _price = trLine.select("td").get(num).select("font[color=#999999]");
				for (Element nPrice : _price) {//没票的时候
					TicketPrice ticketPrice = new TicketPrice();
					ticketPrice.setExist(false);//是否有票
					setPriceAndRemark(ticketPrice,nPrice.text().trim());//设置票价及备注
					ticketPrice.setMainURL(url);//买票的主页面
					ticketPrice.setDetailURL("");//买票的详细页面
					list.add(ticketPrice);
				}
				if(flag) {
					Elements price_ = trLine.select("td").get(2).select("a[href^=cart.asp?productid=]");
					for (Element yPrice : price_) {//有票的时候
						TicketPrice ticketPrice = new TicketPrice();
						ticketPrice.setExist(true);//是否有票
						ticketPrice.setMainURL(url);//买票的主页面
						ticketPrice.setPrice(yPrice.text().trim());
						String remark = null;
						if(yPrice.nextElementSibling() != null) {
							remark = yPrice.nextElementSibling().text().trim();
						} else {
							remark = "套票";
						}
						ticketPrice.setRemark(remark);
						ticketPrice.setDetailURL(yPrice.attr("abs:href"));//买票的详细页面
						list.add(ticketPrice);
					}
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
	private String string2Date(String content) {
		String standardTime = null;
		if(content.matches("(\\d{4}年\\d{1,2}月\\d{1,2}日\\d{1,2}:\\d{1,2})\\s+星期.")) {
			Pattern p = Pattern.compile("(\\d{4}年\\d{1,2}月\\d{1,2}日\\d{1,2}:\\d{1,2})\\s+星期.");
			Matcher m = p.matcher(content);
			m.matches();
			try {
				standardTime = newDate.format(otherDate.parse(m.group(1)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			standardTime = content;
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
		} else if("亲子家庭".equals(single)) {
			showType = ShowType.CHILDREN;
		} else if("魔术杂技".equals(single)) {
			showType = ShowType.OPERA;
		} else if("国家大剧院专场".equals(single)||"人艺演出".equals(single)) {
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
		PiaoBusSpider piaoBusSpider = new PiaoBusSpider();
		piaoBusSpider.setDao(new DataDao());
		piaoBusSpider.extract();
	}
}
