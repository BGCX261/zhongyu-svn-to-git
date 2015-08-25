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

public class Piao55Spider extends TicketSpider implements SpiderTask {
	
	private static final Log log = LogFactory.getLog(Piao55Spider.class);
	private final static String HOME_PAGE = "http://www.55piao.com/Perform/a/Performo.asp";
	private final static String BASE_URL = "http://www.55piao.com/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat otherDate = new SimpleDateFormat("yyyy-M-d HH:mm");
	@Override
	public void extract() {
		Document start = getDoc(HOME_PAGE);
		Elements every = start.select("iframe[src^=../List/]");
		String title = null;
		String standardUrl = null;
		int showType = 0;
		for (Element each : every) {
			String _url = each.attr("abs:src");
			try {
				Document doc = getDoc(_url,"GB2312");
				String single = doc.select("td[width=25][style][bgColor]").text().replaceAll("\\s", "");
				Elements urls = doc.select("a.ptitle[href^=/perform/]");
				for (Element url : urls) {
					title = url.text().trim();
					standardUrl = url.attr("abs:href").trim();
					showType = getShowType(single,title);
				}
			} catch (Exception e) {
				//不做任何处理,只要保证能继续运行即可
				log.error(e);
				continue;
			}
			try {
				extractEach(standardUrl, showType, title);
			} catch (Exception e) {
				log.error(standardUrl+Const.separator+showType+Const.separator+title,e);
				continue;
			}
		}
	}
	private void extractEach(String url, int showType, String title) {
		Document ticket = getDoc(url);
		Show show = new Show();
		show.setType(showType);//设置演出类型
		show.setAgent_id(agentID);
		show.setName(title);//设置演出标题
		show.setSiteName(ticket.select("td[width=215][height=35]").get(0).text());//设置演出场馆
		show.setImage_path(ticket.select("img[width=200][height=300][src^=/UploadFile/Perform/]").first().attr("abs:src"));//演出图片
		show.setIntroduction(PubFun.cleanElement(ticket.select("td[style=line-height:20px; word-wrap:break-word;]")).html());//演出简介
		show.setTimeAndPrice(getTimeAndPrice(ticket,url));//演出时间及相应时间下的票价情况
		try {
			getDao().saveShow(show);
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}
	//每个时间下的票价的详细情况
	private Map<String, List<TicketPrice>> getTimeAndPrice(Document ticket,String url) {
		Elements trLines = ticket.select("table[width=783][bgcolor=#FEFBF2] tr");
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
			for (Element trLine : trLines) {
				String time = trLine.select("td").get(1).text().trim(); 
				String standardTime = string2Date(time);
				if(!time.equals("-")) {
					List<TicketPrice> list = new ArrayList<TicketPrice>();
					timeAndPrice.put(standardTime, list);
					Elements price = trLine.select("a[href^=../../Purchase/Cart.asp?sid]");
					for (Element yPrice : price) {//有票的时候
						TicketPrice ticketPrice = new TicketPrice();
						ticketPrice.setExist(true);//是否有票
						ticketPrice.setMainURL(url);//买票的主页面
						setPriceAndRemark(ticketPrice,yPrice.text().trim());//设置票价及备注
						ticketPrice.setDetailURL(yPrice.attr("abs:href"));//买票的详细页面
						list.add(ticketPrice);
					}
					Element e = trLine.select("td[width=371][style=color:#cccccc;]").first();
					e.select("a").remove();
					String[] es = e.text().split("、");
					for (String nPrice : es) {//没票的时候
						if(!nPrice.trim().equals("")) {
							TicketPrice ticketPrice = new TicketPrice();
							ticketPrice.setExist(false);//是否有票
							setPriceAndRemark(ticketPrice,nPrice.trim());//设置票价及备注
							ticketPrice.setMainURL(url);//买票的主页面
							ticketPrice.setDetailURL("");//买票的详细页面
							list.add(ticketPrice);
						}
					}
				}
			}
			return timeAndPrice;
	}
	//把评价与备注分开设置
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
	//对日期进行处理
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
	//进行详细划分类别
	private int getShowType(String single, String detailType) {
		int showType = 0;
		if ("演唱会".equals(single)) {
			showType = ShowType.CONCERT;
		} else if ("音乐会".equals(single) || "国家大剧院音乐厅".equals(single)) {
			showType = ShowType.SHOW;
		} else if ("话剧歌剧".equals(single)) {
			showType = ShowType.DRAMA;
		} else if ("亲子儿童".equals(single)) {
			showType = ShowType.CHILDREN;
		} else if ("戏曲综艺".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("舞蹈芭蕾".equals(single)) {
			showType = ShowType.DANCE;
		} else if ("魔术杂技".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("梅兰芳大剧院".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("长安大戏院".equals(single)) {
			showType = ShowType.OPERA;
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
		} else if ("体育休闲".equals(single)) {
			showType = ShowType.SPORTS;
		} else {
			showType = ShowType.OTHER;
		}
		return showType;
	}
	
	public static void main(String[] args) {
		Piao55Spider piao55Spider = new Piao55Spider();
		piao55Spider.setDao(new DataDao());
		piao55Spider.extract();
	}
	
}
