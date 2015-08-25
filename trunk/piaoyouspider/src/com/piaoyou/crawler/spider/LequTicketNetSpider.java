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

public class LequTicketNetSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(PiaoWTongSpider.class);
	private final static String BASE_URL = "http://lequ.ticketnet.cn/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat otherDate = new SimpleDateFormat("yyyy-M-d HH:mm");
	@Override
	public void extract() {
		/*Document start = getDoc(HOME_PAGE);
		Elements targets = start.select("a[id^=ctl00_Navigation1_Rep_Category_ct]");
		for (Element target : targets) {
			Map<String,String> param = new HashMap<String, String>();
			param.put("__VIEWSTATE",start.select("#__VIEWSTATE").val());
			param.put("__EVENTVALIDATION",start.select("#__EVENTVALIDATION").val());
			Matcher matcher = Pattern.compile("'(.*?)'").matcher(target.toString());
			matcher.find();
			param.put("__EVENTTARGET",matcher.group(1));
			matcher.find();
			param.put("__EVENTARGUMENT","");
			String url = BASE_URL + start.select("form#aspnetForm").attr("action");
			Document sortPage = getDocPost(url, param);
		}*/
		String[] urls = {
				         "http://lequ.ticketnet.cn/ProTypeList1.html",
						 "http://lequ.ticketnet.cn/ProTypeList7.html",
						 "http://lequ.ticketnet.cn/ProTypeList8.html",
						 "http://lequ.ticketnet.cn/ProTypeList59.html",
						 "http://lequ.ticketnet.cn/ProTypeList65.html",
						 "http://lequ.ticketnet.cn/ProTypeList75.html",
						 "http://lequ.ticketnet.cn/ProTypeList81.html",
						 "http://lequ.ticketnet.cn/ProTypeList87.html",
						 "http://lequ.ticketnet.cn/ProTypeList96.html",
						 "http://lequ.ticketnet.cn/ProTypeList99.html",
						 "http://lequ.ticketnet.cn/ProTypeList106.html"
		                 };
		
		for (String target : urls) {
			int showType = getShowType(target);
			Document start = getDoc(target);
			Elements _links = start.select("a.i[href^=ProDetails]");
			for (Element link : _links) {
				try {
					extractEach(link.attr("abs:href"), showType, link.text());
				} catch (Exception ex) {
					log.error(link.attr("abs:href")+Const.separator+showType+Const.separator+link.text(),ex);
					continue;
				}
			}
			Document doc = getDoc(target);
			Map<String, String> param = new HashMap<String, String>();
			param.put("__VIEWSTATE", doc.select("#__VIEWSTATE").val());
			param.put("__EVENTVALIDATION", doc.select("#__EVENTVALIDATION")
					.val());

			Elements links = doc.select("a[href]:contains(后页)");
			while (!links.isEmpty()) {
				Matcher matcher = Pattern.compile("'(.*?)'").matcher(
						links.toString());
				matcher.find();
				param.put("__EVENTTARGET", matcher.group(1));
				matcher.find();
				param.put("__EVENTARGUMENT", matcher.group(1));

				Document nextPage = getDocPost(target, param);
				links = nextPage.select("a[href]:contains(后页)");
				Elements es = nextPage.select("a.i[href^=ProDetails]");
				for (Element e : es) {
					try {
						extractEach(e.attr("abs:href"), showType, e.text());
					} catch (Exception ex) {
						log.error(e.attr("abs:href")+Const.separator+showType+Const.separator+e.text(),ex);
					}
				}
			}
		}
	}
	
	private void extractEach(String url, int showType, String title) {
		Document ticket = getDoc(url);
		Show show = new Show();
		show.setType(showType);//演出类型
		show.setName(title);//演出名称
		show.setAgent_id(agentID);
		show.setSiteName(ticket.select("span#ctl00_ContentPlaceHolder1_ProVenues").get(0).text());//演出场馆
		Elements path = ticket.select("img#ctl00_ContentPlaceHolder1_ProImg[width=210][height=280]");
		if(path.size()!=0) {
			show.setImage_path(path.first().attr("abs:src"));//演出图片
		}
		show.setWatermark(true);//有水印
		show.setIntroduction(PubFun.cleanElement(ticket.select("div#nry4-2:has(strong)")).html());//演出简介
		show.setTimeAndPrice(getTimeAndPrice(ticket,url));//演出时间及相应时间下的票价情况
		try {
			getDao().saveShow(show);
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}

	private Map<String, List<TicketPrice>> getTimeAndPrice(Document ticket,String url) {
		Elements trLines = ticket.select("div[id=nrym3][class=peicelist]");
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		for (Element trLine : trLines) {
			String time = trLine.select("div[id=nrym4][class=ptime]").text().trim();
			String standardTime = string2Date(time);
			List<TicketPrice> list = new ArrayList<TicketPrice>();
			timeAndPrice.put(standardTime, list);
			Elements price = trLine.select("a.price[href=javascript:void(0)]");
			for (Element yPrice : price) {//有票的时候
				if(!yPrice.text().trim().equals("")) {
					TicketPrice ticketPrice = new TicketPrice();
					ticketPrice.setExist(true);//是否有票
					ticketPrice.setMainURL(url);//买票的主页面
					setPriceAndRemark(ticketPrice,yPrice.text().trim());//设置票价及备注
					ticketPrice.setDetailURL(url);//买票的详细页面
					list.add(ticketPrice);
				}
			}
			Elements _price = trLine.select("span[style=color:#ccc]");
			for (Element nPrice : _price) {//没票的时候
				TicketPrice ticketPrice = new TicketPrice();
				ticketPrice.setExist(false);//是否有票
				setPriceAndRemark(ticketPrice,nPrice.text().trim());//设置票价及备注
				ticketPrice.setMainURL(url);//买票的主页面
				ticketPrice.setDetailURL("");//买票的详细页面
				list.add(ticketPrice);
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
	//根据演出类型与演出名称详细分类
	private int getShowType(String target) {
		int showType = 0;
		Pattern p = Pattern.compile("http://lequ\\.ticketnet\\.cn/ProTypeList(\\d+)\\.html");
		Matcher m =  p.matcher(target);
		m.matches();
		String num = m.group(1);
		if("1".equals(num)) {
			showType = ShowType.CONCERT;
		} else if("7".equals(num)) {
			showType = ShowType.SHOW;
		} else if("8".equals(num)) {
			showType = ShowType.SPORTS;
		} else if("59".equals(num)) {
			showType = ShowType.DANCE;
		} else if("65".equals(num)) {
			showType = ShowType.OPERA;
		} else if("75".equals(num)) {
			showType = ShowType.DRAMA;
		} else if("81".equals(num)) {
			showType = ShowType.OPERA;
		} else if("106".equals(num)) {
			showType = ShowType.CHILDREN;
		} else {
			showType = ShowType.OTHER;
		}
		return showType;
	}
	public static void main(String[] args) {
		LequTicketNetSpider lequTicketNetSpider = new LequTicketNetSpider();
		lequTicketNetSpider.setDao(new DataDao());
		lequTicketNetSpider.extract();
	}
}
