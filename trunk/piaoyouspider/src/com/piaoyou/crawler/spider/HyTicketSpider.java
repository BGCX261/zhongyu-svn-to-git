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

public class HyTicketSpider extends TicketSpider implements SpiderTask {
	
	private static final Log log = LogFactory.getLog(HyTicketSpider.class);
	private final static String HOME_PAGE = "http://www.hyticket.com/all.asp";
	private final static String BASE_URL = "http://www.hyticket.com/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat otherDate = new SimpleDateFormat("yyyy-M-d HH:mm");
	@Override
	public void extract() {
	
		Document start = getDoc(HOME_PAGE);
		Elements every  = start.select("table:has(a[class=li][href^=piao.asp?id=])[width=1000]");

		for(int i=0;i<every.size();i++) {
			if(i>0) {
				String single = every.get(i).select("font[color=ffffff]").text();
				Elements urls = every.get(i).select("a[class=li][href^=piao.asp?id=]");
				for (Element url : urls) {
					int showType = getShowType(single.trim(), url.text().trim());
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
	private void extractEach(String url, int showType,String title) {
		Document ticket = getDoc(url);
		Show show = new Show();
		show.setType(showType);//设置演出的类型
		show.setAgent_id(agentID);
		show.setName(title);//演出名称
		show.setImage_path(ticket.select("img[width=180][height=236][src^=UploadImg/]").first().attr("abs:src"));//设置演出图片的路径
		show.setSiteName(ticket.select("td.lin1>a.li[target=_blank]").get(0).text());//演出场馆
		show.setIntroduction(PubFun.cleanElement(ticket.select("table.table[align=center][width=99%]").get(0)).html());//演出介绍
		show.setTimeAndPrice(getTimeAndPrice(ticket,url));//演出时间及相应时间下的票价情况
		try {
			getDao().saveShow(show);
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}
	private Map<String, List<TicketPrice>> getTimeAndPrice(Document ticket,String url) {
		Elements trLines = ticket.select("table[class=table][width=100%] tr:has(td[class=lin1])");
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		for (Element trLine : trLines) {
			String standardTime = string2Date(trLine.select("td").get(1).text().trim()); 
			List<TicketPrice> list = new ArrayList<TicketPrice>();
			timeAndPrice.put(standardTime,list);
			if(!trLine.select("td").get(2).text().trim().equals("")) {
				Elements price = trLine.select("td[class=lin1] a[class=li][href^=dingdan.asp?pro2id=]");
				for (Element yPrice : price) {//有票的时候
					TicketPrice ticketPrice = new TicketPrice();
					ticketPrice.setExist(true);//是否有票
					ticketPrice.setMainURL(url);//买票的主页面
					setPriceAndRemark(ticketPrice,yPrice.text().trim());//设置票价及备注
					ticketPrice.setDetailURL(yPrice.attr("abs:href"));//买票的详细页面
					list.add(ticketPrice);
				}
				Elements _price = trLine.select("td.lin1>font[color=#cccccc]");
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
	//对票的类别做详细处理	
	private int getShowType(String single, String detailType) {
		int showType = 0;
		if ("演唱会".equals(single)) {
			showType = ShowType.CONCERT;
		} else if ("音乐会".equals(single)|| "国家大剧院音乐厅".equals(single)) {
			showType = ShowType.SHOW;
		} else if ("话剧歌剧".equals(single)) {
			showType = ShowType.DRAMA;
		} else if ("北京人艺".equals(single)) {
			showType = ShowType.DRAMA;
		} else if ("儿童亲子".equals(single)) {
			showType = ShowType.CHILDREN;
		} else if ("戏曲曲艺".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("舞蹈芭蕾".equals(single)) {
			showType = ShowType.DANCE;
		} else if ("魔术马戏".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("长安大戏院".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("体育赛事".equals(single)) {
			showType = ShowType.SPORTS;
		} else if ("梅兰芳大剧院".equals(single)) {
			showType = ShowType.OPERA;
		} else if ("国家大剧院戏剧场".equals(single)) {
			if (detailType.contains("音乐会")) {
				showType = ShowType.SHOW;
			} else if (detailType.contains("芭蕾舞")) {
				showType = ShowType.DANCE;
			} else {
				showType = ShowType.DRAMA;
			}
		} else if ("国家大剧院小剧场".equals(single)) {
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
		} else {
			showType = ShowType.OTHER;
		}
		return showType;
	}
	//转化成标准的存储日期
	private String string2Date(String time) {
		String standardTime = null;
		String _time = time.replaceAll("：", ":").replaceAll("[^\\d-:]", " ");
		if(_time.matches("[\\d\\s-:]+")&&!(_time.trim().equals(""))) {
				try {
					standardTime = newDate.format(otherDate.parse(_time));
				} catch (ParseException e) {
					log.error(e.getMessage());
				}
			}  else {
			standardTime = time;
		}
		return standardTime;
	}
	
	//对价格与日期做详细处理
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
			if(m.group(2).trim().equals("缺货登记")) {
				ticketPrice.setExist(false);
			}
		} else {
			ticketPrice.setPrice("-1");
			ticketPrice.setRemark(content.replaceAll("）|（|\\(|\\)", ""));
			ticketPrice.setExist(false);
		}
	}
	public static void main(String[] args) {
		HyTicketSpider hyTicketSpider = new HyTicketSpider();
		hyTicketSpider.setDao(new DataDao());
		hyTicketSpider.extract();
	}

}
