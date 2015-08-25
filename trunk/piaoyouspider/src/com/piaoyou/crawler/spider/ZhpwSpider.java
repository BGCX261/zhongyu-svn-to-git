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

public class ZhpwSpider extends TicketSpider implements SpiderTask {
	
	private static final Log log = LogFactory.getLog(ZhpwSpider.class);
	private final static String HOME_PAGE = "http://www.zhpw.com/allticket.html";
	private final static String BASE_URL = "http://www.zhpw.com/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat otherDate = new SimpleDateFormat("yyyy-M-d HH:mm");
	@Override
	public void extract() {
		Document start = getDoc(HOME_PAGE);
		Elements every  = start.select("table:has(td[style=font-weight:bold;width:2%;color:#ffffff;])[width=100%]");
		for (Element each : every) {
			String single = each.select("td[style=font-weight:bold;width:2%;color:#ffffff;]").text().trim();
			Elements urls = each.select("table[width=100%][cellspacing=4] a[target=_blank]");
			for (Element url : urls) {
				int showType = getShowType(single,url.text().trim()); 
				try {
					extractEach(url.attr("abs:href"),showType,url.text().trim());
				} catch (Exception e) {
					log.error(url.attr("abs:href")+Const.separator+showType+Const.separator+url.text().trim(),e);
					continue;
				}
			}
		}
	}
	private void extractEach(String url, int showType, String title) {
		Document ticket = getDoc(url);
		Show show  = new Show();
		show.setAgent_id(agentID);//设置场馆ID
		show.setWatermark(true);//有水印
		show.setName(title);//演出名称
		show.setType(showType);//演出类型
		Elements path = ticket.select("img[width=180][height=236][src^=http://www.zhpw.com//UpFiles/]");
		if(path.size()==0) {//演出图片的展示
			show.setImage_path("");
		} else {
			show.setImage_path(ticket.select("img[width=180][height=236][src^=http://www.zhpw.com//UpFiles/]").get(0).attr("abs:src"));
		}
		show.setSiteName(ticket.select("td[style^=PADDING-TOP:]:has(a[target=_blank][href])").get(0).text());//演出场馆
		show.setIntroduction(PubFun.cleanElement(ticket.select("div#ticketnrjs")).html());//演出简介
		show.setTimeAndPrice(getTimeAndPrice(ticket,url));//演出时间及相应时间下的票价情况
		try {
			getDao().saveShow(show);
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}
	
	//处理演出时间及相应时间下的票价情况
	private Map<String, List<TicketPrice>> getTimeAndPrice(Document ticket,String url) {
		Elements trLines = ticket.select("table[width=100%][bgcolor=#ffffff] tr[align=center]:has(td[style])");
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		for (Element trLine : trLines) {
			String time = trLine.select("td").get(1).text().trim();
			String standardTime = string2Date(time,ticket);
			List<TicketPrice> list = new ArrayList<TicketPrice>();
			timeAndPrice.put(standardTime, list);
			if(!trLine.select("td").get(2).text().trim().equals("")) {
				Elements price = trLine.select("a.yuding[href^=cart.asp?productid=]");
				for (Element yPrice : price) {//有票的时候
					TicketPrice ticketPrice = new TicketPrice();
					ticketPrice.setExist(true);//是否有票
					ticketPrice.setMainURL(url);//买票的主页面
					setPriceAndRemark(ticketPrice,yPrice.text().trim());//设置票价及备注
					ticketPrice.setDetailURL(yPrice.attr("abs:href"));//买票的详细页面
					list.add(ticketPrice);
				}
				Elements _price = trLine.select("span.font7");
				for (Element nPrice : _price) {//没票的时候
					TicketPrice ticketPrice = new TicketPrice();
					ticketPrice.setExist(false);//是否有票
					setPriceAndRemark(ticketPrice,nPrice.text().trim());//设置票价及备注
					ticketPrice.setMainURL(url);//买票的主页面
					ticketPrice.setDetailURL("");//买票的详细页面
					list.add(ticketPrice);
				}
			}
		}
		return timeAndPrice;
	}
	private void setPriceAndRemark(TicketPrice ticketPrice, String content) {
		if (content.matches("\\d+")) {
			ticketPrice.setPrice(content);
			ticketPrice.setRemark("");
		} else if (content.matches("(\\d+)(.+)")) {
			Pattern p = Pattern.compile("(\\d+)(.+)");
			Matcher m = p.matcher(content.replaceAll("）|（|\\(|\\)", ""));
			m.matches();
			ticketPrice.setPrice(m.group(1));
			ticketPrice.setRemark(m.group(2));
		} else {
			ticketPrice.setPrice("-1");
			ticketPrice.setRemark(content.replaceAll("）|（|\\(|\\)", ""));
			ticketPrice.setExist(false);
		}
		
	}
	//对票的类别做详细处理
	private int getShowType(String single, String detailType) {
		int showType = 0;
		if ("演唱会".equals(single)) {
			showType = ShowType.CONCERT;
		} else if ("音乐会".equals(single) || "国家大剧院音乐厅".equals(single)) {
			showType = ShowType.SHOW;
		} else if ("话剧歌剧".equals(single)) {
			showType = ShowType.DRAMA;
		} else if ("北京人艺演出".equals(single)) {
			showType = ShowType.DRAMA;
		} else if ("亲子休闲".equals(single)) {
			showType = ShowType.CHILDREN;
		} else if ("戏曲综艺".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("舞蹈芭蕾".equals(single)) {
			showType = ShowType.DANCE;
		} else if ("魔术杂技马戏".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("长安大戏院".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("体育赛事".equals(single)) {
			showType = ShowType.SPORTS;
		} else if ("梅兰芳大剧院".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("国家大剧院戏剧场".equals(single)) {
			if (detailType.contains("音乐会")) {
			} else if (detailType.contains("芭蕾舞")) {
				showType = ShowType.DANCE;
			} else {
				showType = ShowType.DRAMA;
			}
		} else if ("国家大剧院小剧场".equals(single)) {
			if (detailType.contains("音乐会")) {
				showType = ShowType.CONCERT;
			} else if (detailType.contains("芭蕾舞")) {
				showType = ShowType.DANCE;
			} else if (detailType.contains("演唱会")) {
				showType = ShowType.CONCERT;
			} else {
				showType = ShowType.OTHER;
			}
		} else if ("国家大剧院歌剧院".equals(single) || "国家大剧院歌剧节".equals(single)) {
			if (detailType.contains("音乐会")) {
				showType = ShowType.CONCERT;
			} else if (detailType.contains("芭蕾舞")) {
				showType = ShowType.DANCE;
			} else if (detailType.contains("演唱会")) {
				showType = ShowType.CONCERT;
			} else {
				showType = ShowType.DRAMA;
			}
		} else if ("电影卡兑换券".equals(single) || "滑雪温泉旅游度假".equals(single)) {
			showType = ShowType.HOLIDAY;
		} else {
			showType = ShowType.OTHER;
		}
		return showType;
	}
	//对日期做处理
	private String string2Date(String time,Document ticket) {
		String standardTime = null;
		if(time.contains(":")) {
			try {
				standardTime = newDate.format(otherDate.parse(time));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			standardTime = ticket.select("div.dakuang div#ticketnrysnrt").get(0).text().trim();
		}
		
		return standardTime;
	}
public static void main(String[] args) {
	ZhpwSpider zhpwSpider = new ZhpwSpider();
	zhpwSpider.setDao(new DataDao());
	zhpwSpider.extract();
}
}
