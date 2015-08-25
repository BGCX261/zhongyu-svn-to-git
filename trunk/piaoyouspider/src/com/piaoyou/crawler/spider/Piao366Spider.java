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

public class Piao366Spider extends TicketSpider implements SpiderTask {

	private static final Log log = LogFactory.getLog(Piao366Spider.class);
	private final static String HOME_PAGE = "http://www.piao366.com/query.asp";
	private final static String BASE_URL = "http://www.piao366.com/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat otherDate = new SimpleDateFormat("yyyy-M-d HH:mm");

	@Override
	public void extract() {
//		Document start = getDoc(HOME_PAGE);
//		Elements every  = start.select("strong>font[color=#ffffff]");
//		for (int i = 0; i < every.size(); i++) {
//			if(i<2) {
//				Elements each = every.get(i).parent().parent().nextElementSibling().select("td[width=30%]>a[href^=ticket_look.asp?id=");
//				for (Element e1 : each) {
//					try {
//						extractEach(e1.attr("abs:href"), e1.text(), every
//								.get(i).text());
//					} catch (Exception e) {
//						log.error(e1.attr("abs:href")+Const.separator+e1.text()+Const.separator+every
//								.get(i).text(), e);
//						continue;
//					}
//				} 
//			} else {
//				Elements each = every.get(i).parent().parent().parent().nextElementSibling().select("td[width=30%]>a[href^=ticket_look.asp?id=");
//				for (Element e1 : each) {
//					try {
//						extractEach(e1.attr("abs:href"), e1.text(), every
//								.get(i).text());
//					} catch (Exception e) {
//						log.error(e1.attr("abs:href")+Const.separator+e1.text()+Const.separator+every
//								.get(i).text(), e);
//						continue;
//					}
//				} 
//			}
//		}
	}
	
	private void extractEach(String url,String detailType,String showType) {
		Document ticket = getDoc(url);
		Show show = new Show();
		setShowType(show,detailType,showType);//设置演出类型
		show.setName(filter(detailType));//设置演出标题
		show.setAgent_id(agentID);
	    String showSite = ticket.select("strong>strong>font[color=#003366]").get(3).text();
	    show.setSiteName(showSite);//设置演出场馆
		Element path = ticket.select("font[color=#5f82a6]>img[width=180][height=236]").first();
		if(path != null) {//有的页面根本没有图片，所以必须做null处理
			String path4Img = ticket.select("font[color=#5f82a6]>img[width=180][height=236][src^=/uploadfiles/uppic/]").first().attr("abs:src");
			show.setImage_path(path4Img);//演出图片
		}else {
			show.setImage_path(null);
		}
		String infoUrl = ticket.select("a[href^=detail.asp?id=]:has(font)").first().attr("abs:href");
		String info = getDetailInfo(infoUrl);
		show.setIntroduction(info);
		show.setTimeAndPrice(getTimeAndPrice(ticket,url));
		try {
			getDao().saveShow(show);
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}
	private String getDetailInfo(String infoUrl) {
		Document dInfo = getDoc(infoUrl);
		Element newDInfo =  PubFun.cleanElement(dInfo.select("table[width=98%][cellspacing=4] td").get(0));
		return newDInfo.html();
	}
	//过滤（小剧场）
	private String filter(String oldString) {
		String newString = oldString.replaceAll("（小剧场）", "");
		return newString;
	}
	
	//对票的类型进行匹配存储
	private void setShowType(Show show,String detailType,String showType) {
		if("演唱会".equals(showType)) {
			show.setType(ShowType.CONCERT);
		}  else if("音乐会".equals(showType)||"国家大剧院·音乐厅".equals(showType)) {
			show.setType(ShowType.SHOW);
		}  else if("舞蹈芭蕾".equals(showType)) {
			show.setType(ShowType.DANCE);
		}  else if("国家大剧院".equals(showType)) {
			show.setType(ShowType.DRAMA);
		}  else if("魔术杂技".equals(showType)) {
			show.setType(ShowType.OPERA);
		}  else if("戏曲综艺".equals(showType)||"".equals(showType)) {
			show.setType(ShowType.OPERA);
		}  else if("儿童亲子".equals(showType)) {
			show.setType(ShowType.CHILDREN);
		}  else if("话剧歌剧".equals(showType)||("国家大剧院·歌剧院").equals(showType)) {
			show.setType(ShowType.DRAMA);
		}  else if("运动休闲".equals(showType)) {
			show.setType(ShowType.SPORTS);
		}  else if("人艺票品".equals(showType)) {
			show.setType(ShowType.DRAMA);
		}  else if("国家大剧院·戏剧场".equals(showType)) {
			if(detailType.contains("芭蕾舞")) {
				show.setType(ShowType.DANCE);
			} else if(detailType.contains("音乐会")) {
				show.setType(ShowType.SHOW);
			} else if(detailType.contains("话剧")) {
				show.setType(ShowType.DRAMA);
			} else {
				show.setType(ShowType.OPERA);
			}
		}  else {
			show.setType(ShowType.OTHER);
		}
	}
	//存储各个价位的票价及信息
	private Map<String,List<TicketPrice>> getTimeAndPrice(Element ticket,String url) {
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		Elements trLine = ticket.select("table[style=WORD-BREAK: keep-all] tr[height=32]");
		for (Element eachLine : trLine) {
			String time = eachLine.select("td").get(0).text().trim();
			String standardTime = string2Date(time);
			List<TicketPrice> list = new ArrayList<TicketPrice>();
			timeAndPrice.put(standardTime, list);
			if(!eachLine.select("td").get(2).text().trim().equals("")) {
				Elements price = eachLine.select("td").get(2).select("a[href^=shopping.asp?id=]");
				for (Element yPrice : price) {//有票的时候
					String nodeName = "";
					if(yPrice.nextElementSibling()!=null&&yPrice.nextElementSibling().nextElementSibling()!=null) {
						nodeName = yPrice.nextElementSibling().nextElementSibling().nodeName();
						if(nodeName.equals("span")) {
							if(yPrice.nextElementSibling().nextElementSibling().hasAttr("id")) {
								nodeName = "("+yPrice.nextElementSibling().nextElementSibling().text()+")";
							} else {
								nodeName = "";
							}
						} else {
							nodeName = "";
						}
					}
					TicketPrice ticketPrice = new TicketPrice();
					ticketPrice.setExist(true);
					ticketPrice.setMainURL(url);
					setPriceAndRemark(ticketPrice,yPrice.text()+nodeName);
					ticketPrice.setDetailURL(yPrice.attr("abs:href"));
					list.add(ticketPrice);
				}
				Elements _price = eachLine.select("td").get(2).select("span[style=color:#999999]");
				for (Element nPrice : _price) {//没票的时候
					if(!nPrice.text().equals("")) {
						TicketPrice ticketPrice = new TicketPrice();
						ticketPrice.setExist(false);
						setPriceAndRemark(ticketPrice,nPrice.text());
						ticketPrice.setMainURL(url);
						ticketPrice.setDetailURL("");
						list.add(ticketPrice);
					}
				}
			}
			
		}
		return timeAndPrice;
	}
	//设置价额与备注
	private void setPriceAndRemark(TicketPrice ticketPrice,String content) {
/*		String[] result = content.trim().split("\\s");
		if(result.length>1) {
			ticketPrice.setRemark(result[1]);
		} else {
			ticketPrice.setRemark("");
		}
		ticketPrice.setPrice(result[0]);*/
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
	//转换成标准日期
	private String string2Date(String time) {
		String[] str = time.split(":");
		String standardTime = null;
		String _standardTime = null;
		if(str.length>1) {
			 _standardTime = str[0]+":"+str[1];
			try {
				 standardTime = newDate.format(otherDate.parse(_standardTime));
			} catch (ParseException e) {
				log.error(e.getMessage());
			}
		} else {
			standardTime = "全年有效";
		}
		return standardTime;
	}
	public static void main(String[] args) {
		Piao366Spider piao366Spider = new Piao366Spider();
		piao366Spider.setDao(new DataDao());
		piao366Spider.extract();
	}
}
