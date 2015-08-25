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

public class GouPiaoTongSpider extends TicketSpider implements SpiderTask {
	
	private static final Log log = LogFactory.getLog(GouPiaoTongSpider.class);
	private final static String HOME_PAGE = "http://www.goupiaotong.com/Perform.html";
	private final static String BASE_URL = "http://www.goupiaotong.com";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat otherDate = new SimpleDateFormat("yyyy-M-d HH:mm");
	@Override
	public void extract() {
		Document start = getDoc(HOME_PAGE);
		Elements every  = start.select("div.pagedisplay");
		for (Element each : every) {
			String single = each.select("h2").text().trim();
			Elements urls = each.nextElementSibling().select("a[target=_blank][href^=/Product-]");
			for(int i=0;i<urls.size();i++) {
				Element url = urls.get(i);
				if(i%2==0) {
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
		
	}
	private void extractEach(String url, int showType, String title) {
		Document ticket = getDoc(url);
		Elements showSite = ticket.select("p[class=mt10 ml25] a[href^=/TheaterDetail-]");
		if(showSite.size()!=0) {
			Show show = new Show();
			show.setType(showType);//演出类型
			show.setAgent_id(agentID);
			show.setName(title);//演出标题
			show.setSiteName(showSite.get(0).text());
			Elements path = ticket.select("img.img[width=201][height=254][src^=http://houtai.goupiaotong.com/ProductImg/PerformImg/]");
			if(path.size()!=0) {
				show.setImage_path(path.first().attr("abs:src"));//演出图片
			}
			show.setIntroduction(PubFun.cleanElement(ticket.select("div[class=in clear],div[class=bd]").get(0)).html());//演出简介
			show.setTimeAndPrice(getTimeAndPrice(ticket,url));//演出时间及相应时间下的票价情况
//			try {
//				getDao().saveShow(show);
//			} catch (SQLException e) {
//				log.error(e.getMessage());
//			}
			System.out.println(show.toString());
		}
	}
	//设置每个时间的每种票价的具体情况
	private Map<String, List<TicketPrice>> getTimeAndPrice(Document ticket,
			String url) {
		Elements trLines = ticket.select("table#per1[class=tborder]");
		Map<String, List<TicketPrice>> timeAndPrice = new HashMap<String, List<TicketPrice>>();
		for (Element trLine : trLines) {
			String standardTime = string2Date(trLine.attr("time").trim());
			List<TicketPrice> list = new ArrayList<TicketPrice>();
			timeAndPrice.put(standardTime, list);
			if (!trLine.select("td").get(2).text().trim().equals("")) {
				Elements price = trLine.select("a[href^=/cart.aspx?priceid=]");
				for (Element yPrice : price) {// 有票的时候
					String param = yPrice.attr("href");
					TicketPrice ticketPrice = new TicketPrice();
					ticketPrice.setExist(true);// 是否有票
					ticketPrice.setMainURL(url);// 买票的主页面
					setPriceAndRemark(ticketPrice, yPrice.text().trim(), param);// 设置票价及备注
					ticketPrice.setDetailURL(yPrice.attr("abs:href"));// 买票的详细页面
					list.add(ticketPrice);
				}
				Element e = trLine.select("td.piaoprice").first();
				e.select("a").remove();
				String[] es = e.text().split("\\|");
				for (String string : es) {// 没票的时候
					if (!string.trim().equals("")
							&& !string.trim().equals("套票：")
							&& !string.trim().equals("价位： 套票：")
							&& !string.trim().equals("价位：")
							&& !string.trim().equals("价位： 待定")) {
						TicketPrice ticketPrice = new TicketPrice();
						ticketPrice.setExist(false);// 是否有票
						ticketPrice.setMainURL(url);// 买票的主页面
						setPriceAndRemark(ticketPrice, string.trim(), null);// 设置票价及备注
						ticketPrice.setDetailURL("");// 买票的详细页面
						list.add(ticketPrice);
					}
				}
			}
		}
		return timeAndPrice;
	}
	//对票价与备注进行重新设置
	private void setPriceAndRemark(TicketPrice ticketPrice, String content,String url) {
		String nowStr = content.replaceAll("价位：", "").trim();
		if(nowStr.matches("\\d+")) {
			ticketPrice.setPrice(nowStr);
			ticketPrice.setRemark("");
		} else if(nowStr.matches("(\\D+)(\\d+)")) {
			Pattern p = Pattern.compile("(\\D+)(\\d+)");
			Matcher m = p.matcher(nowStr.replaceAll("）|（|\\(|\\)", ""));
			m.matches();
			ticketPrice.setPrice(m.group(2));
			ticketPrice.setRemark(m.group(1));
		} else if(nowStr.matches("(\\d+)(\\D+)")) {
			Pattern p = Pattern.compile("(\\d+)(\\D+)");
			Matcher m = p.matcher(nowStr.replaceAll("）|（|\\(|\\)", ""));
			m.matches();
			ticketPrice.setPrice(m.group(1));
			ticketPrice.setRemark(m.group(2));
		} else if((nowStr.matches("\\D+")||nowStr.matches("\\d+\\*\\d+"))&&url!=null){
			Pattern p = Pattern.compile("/cart\\.aspx\\?priceid=\\d+&price=(\\d+)");
			Matcher m = p.matcher(url.trim());
			m.matches();
			ticketPrice.setPrice(m.group(1));
			ticketPrice.setRemark(nowStr);
		} else if(content.matches("(\\D+)(\\d+)(.+)")) {
			Pattern p = Pattern.compile("(\\D+)(\\d+)(.+)");
			Matcher m = p.matcher(content);
			m.matches();
			ticketPrice.setPrice(m.group(2));
			ticketPrice.setRemark(m.group(1)+m.group(3).replaceAll("）|（|\\(|\\)", ""));
		} else if(nowStr.matches("\\d+.+")) {
			Pattern p = Pattern.compile("(\\d+)(.+)");
			Matcher m = p.matcher(nowStr);
			m.matches();
			ticketPrice.setPrice(m.group(1));
			ticketPrice.setRemark(m.group(2).replaceAll("）|（|\\(|\\)", ""));
		} else {
			ticketPrice.setPrice(nowStr);
		}
	}
	//将日期重新格式化成存储的标准格式
	private String string2Date(String time) {
		String standardTime = null;
		if(time.matches("\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}:\\d{1,2}")) {
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
	//根据演出名称与类名进行详细分类
	private int getShowType(String single, String detailType) {
		int showType = 0;
		if ("演唱会".equals(single)) {
			showType = ShowType.CONCERT;
		} else if ("音乐会".equals(single) || "国家大剧院 —国家大剧院音乐厅".equals(single)) {
			showType = ShowType.SHOW;
		} else if ("话剧歌剧—话剧".equals(single)) {
			showType = ShowType.DRAMA;
		} else if ("话剧歌剧—歌剧".equals(single)) {
			showType = ShowType.DRAMA;
		} else if ("儿童亲子".equals(single)) {
			showType = ShowType.CHILDREN;
		} else if ("戏曲综艺 —戏曲综艺".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("舞蹈芭蕾 —舞蹈芭蕾".equals(single)) {
			showType = ShowType.DANCE;
		} else if ("国家大剧院 —国家大剧院戏剧场".equals(single)) {
			if (detailType.contains("音乐会")) {
				showType = ShowType.SHOW;
			} else if (detailType.contains("芭蕾舞")) {
				showType = ShowType.DANCE;
			} else {
				showType = ShowType.DRAMA;
			}
		} else if ("国家大剧院 —国家大剧院歌剧院".equals(single)
				|| "国家大剧院 —国家大剧院-小剧场".equals(single)) {
			if (detailType.contains("音乐会")) {
				showType = ShowType.SHOW;
			} else if (detailType.contains("芭蕾舞")) {
				showType = ShowType.DANCE;
			} else if (detailType.contains("演唱会")) {
				showType = ShowType.CONCERT;
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
		GouPiaoTongSpider gouPiaoTongSpider = new GouPiaoTongSpider();
		gouPiaoTongSpider.setDao(new DataDao());
		gouPiaoTongSpider.extract();
	}

}
