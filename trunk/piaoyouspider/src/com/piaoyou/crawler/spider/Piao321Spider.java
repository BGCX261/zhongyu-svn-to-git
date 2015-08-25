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

public class Piao321Spider extends TicketSpider implements SpiderTask {
	
	private static final Log log = LogFactory.getLog(Piao321Spider.class);
	private final static String HOME_PAGE = "http://www.piao321.com/ticked.asp";
	private final static String BASE_URL = "http://www.piao321.com/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat otherDate = new SimpleDateFormat("yyyy-M-d HH:mm");
	@Override
	public void extract() {
		Document start = getDoc(HOME_PAGE);
		Elements every  = start.select("table:has(font[color=#FFFFFF])[width=772]:has(div.STYLE7)");
		for (int i = 0; i < every.size(); i++) {
			if(i>2) {
				Element each = every.get(i);
				String single = each.select("font[color=#FFFFFF]").get(0).text();
				Elements urls = each.select("td[width=237] a.l[target=_blank][href^=html/]");
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
		
	}
	private void extractEach(String url, int showType, String title) {
		Document ticket = getDoc(url);
		Elements siteName = ticket.select("a[href^=../yccg/] font[color=#0C4976]");
		if (siteName.size() != 0) {
			Show show = new Show();
			show.setAgent_id(agentID);//场馆ID
			show.setName(title);//演出标题
			show.setType(showType);//演出类型
			show.setSiteName(ticket.select("a[href^=../yccg/] font[color=#0C4976]").first().text());//演出场馆
			Elements path = ticket.select("img[width=180][height=300][src^=../admin/UploadImg/]");
			if(path.size()!=0) {
				show.setImage_path(path.first().attr("abs:src"));//演出图片
			}
			show.setIntroduction(PubFun.cleanElement(ticket.select("table[class=table][width=587] td[valign=top][align=left]").get(2)).html());//演出简介
			show.setTimeAndPrice(getTimeAndPrice(ticket,url));//演出时间及相应时间下的票价情况
			try {
				getDao().saveShow(show);
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		}
	}
			
	private Map<String, List<TicketPrice>> getTimeAndPrice(Document ticket,String url) {
		Elements trLines = ticket.select("table[width=100%][class=table][align=center] tr:gt(0)");
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		for (Element trLine : trLines) {
			String time = trLine.select("td").get(1).text().trim();
			String standardTime = string2Date(time);
			standardTime=standardTime.replace(";", ":").replace("：", ":");
			if(standardTime.matches("[^星]*星期[^期]*")){
				standardTime=PubFun.getRegex("([^星]*)星期[^期]*", standardTime, 1);
			}
			try{
				standardTime = newDate.format(oldDate.parse(standardTime));
			}catch(Exception ee){
				
			}
			List<TicketPrice> list = new ArrayList<TicketPrice>();
			timeAndPrice.put(standardTime, list);
			if(!trLine.select("td").get(2).text().trim().equals("")) {
				Elements price = trLine.select("a.a[href^=../dingdan.asp?shop_id=]");
				for (Element yPrice : price) {//有票的时候
					TicketPrice ticketPrice = new TicketPrice();
					ticketPrice.setExist(true);//是否有票
					ticketPrice.setMainURL(url);//买票的主页面
					boolean flag = setPriceAndRemark(ticketPrice,yPrice.text().trim());
					if(flag == false) {
						continue;
					}
					setPriceAndRemark(ticketPrice,yPrice.text().trim());//设置票价及备注
					ticketPrice.setDetailURL(url);//买票的详细页面
					list.add(ticketPrice);
				}
				Elements _price = trLine.select("font[color=cccccc]");
				for (Element nPrice : _price) {//没票的时候
					TicketPrice ticketPrice = new TicketPrice();
					ticketPrice.setExist(false);//是否有票
					boolean flag = setPriceAndRemark(ticketPrice,nPrice.text().trim());
					if(flag == false) {
						continue;
					}
					setPriceAndRemark(ticketPrice,nPrice.text().trim());//设置票价及备注
					ticketPrice.setMainURL(url);//买票的主页面
					ticketPrice.setDetailURL("");//买票的详细页面
					list.add(ticketPrice);
				}
			}
		}
		return timeAndPrice;
	}
	//详细设置票价与备注
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
	
	//转换成存储的标准格式
	//2011-08-12-19:30
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
	
	//详细分类
	private int getShowType(String single, String detailType) {
		int showType = 0;
		if ("演唱会".equals(single)) {
			showType = ShowType.CONCERT;
		} else if ("音乐会".equals(single) || "国家大剧院音乐厅".equals(single)) {
			showType = ShowType.SHOW;
		} else if ("话剧 音乐剧".equals(single)) {
			showType = ShowType.DRAMA;
		} else if ("人艺话剧".equals(single)) {
			showType = ShowType.DRAMA;
		} else if ("儿童亲子".equals(single)) {
			showType = ShowType.CHILDREN;
		} else if ("京剧 戏曲 综艺".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("舞蹈 芭蕾".equals(single)) {
			showType = ShowType.DANCE;
		} else if ("国家大剧院戏剧场".equals(single)) {
			if (detailType.contains("音乐会")) {
				showType = ShowType.SHOW;
			} else if (detailType.contains("芭蕾舞")) {
				showType = ShowType.DANCE;
			} else {
				showType = ShowType.DRAMA;
			}
		} else if ("常年演出频道".equals(single)) {
			if (detailType.contains("杂技")) {
				showType = ShowType.OPERA;
			} else {
				showType = ShowType.HOLIDAY;
			}
		} else if ("国家大剧院歌剧院".equals(single)) {
			if (detailType.contains("音乐会")) {
				showType = ShowType.SHOW;
			} else if (detailType.contains("芭蕾舞")) {
				showType = ShowType.DANCE;
			} else if (detailType.contains("演唱会")) {
				showType = ShowType.CONCERT;
			} else {
				showType = ShowType.DRAMA;
			}
		} else if ("综合版块".equals(single)) {
			showType = ShowType.HOLIDAY;
		} else {
			showType = ShowType.OTHER;
		}
		return showType;
	}
	


	public static void main(String[] args) {
		Piao321Spider piao321Spider = new Piao321Spider();
		piao321Spider.setDao(new DataDao());
		piao321Spider.extract();
	}
}
