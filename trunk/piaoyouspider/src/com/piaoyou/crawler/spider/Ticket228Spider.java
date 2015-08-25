package com.piaoyou.crawler.spider;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

/**
 * @author chaishu
 *
 */
public class Ticket228Spider extends TicketSpider implements SpiderTask {
	
	private static final Log log = LogFactory.getLog(Ticket228Spider.class);
	private final static String HOME_PAGE = "http://www.228ticket.com/allpiao.asp";
	private final static String BASE_URL = "http://www.228ticket.com/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();

	@Override
	public void extract() {
		Document start = getDoc(HOME_PAGE);
		Elements every = start.select("td[style=color:#068;font-weight:bold;]");
		for (Element e : every) {
			Elements destTd = e.parent().parent().parent().nextElementSibling().select("a[href^=piaodisp.asp?id=]:has(font)");
			for (Element e1 : destTd) {
				String url = e1.attr("abs:href");
				try {
					extractEach(url, e.text().trim());
				} catch (Exception e2) {
					log.error(url+Const.separator+e.text().trim(), e2);
					continue;
				}
			}
		}
	}
	
	private void extractEach(String url,String showType) {
		Document ticket = getDoc(url);
		Show show = new Show();
		setNewType(showType,show);//设置票的类型
		show.setAgent_id(agentID);
		String showName = ticket.select("td>div>img[src=image/2006_arr1.gif]").get(0).nextElementSibling().text();
		show.setName(showName);//演出标题
		String showSite = ticket.select("a[href^=theater.asp?id=][target=_blank]").get(0).text();
		show.setSiteName(showSite);//演出场馆
		String infoUrl = ticket.select("a[href^=piaodisp1.asp?id=]:has(font)").first().attr("abs:href");
		String info = getDetailInfo(infoUrl);
		show.setIntroduction(info);//演出简介
		Element path = ticket.select("td[valign=top]>img[width=180][height=200]").first();
		if(path != null) {//有的页面根本没有图片，所以必须做null处理
			String path4Img = ticket.select("td[valign=top]>img[width=180][height=200]").first().attr("abs:src");
			show.setImage_path(path4Img);//演出图片
		}else {
			show.setImage_path(null);
		}
		show.setTimeAndPrice(getTimeAndPrice(ticket,url));
		try {
			getDao().saveShow(show);
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}
	
	//对票的类型进行匹配存储
	private void setNewType(String showType,Show show) {
		if("演唱会".equals(showType)) {
			show.setType(ShowType.CONCERT);
		} else if("音乐会".equals(showType)) {
			show.setType(ShowType.SHOW);
		}  else if("芭蕾舞蹈".equals(showType)) {
			show.setType(ShowType.DANCE);
		}  else if("国家大剧院".equals(showType)) {
			show.setType(ShowType.DRAMA);
		}  else if("魔术杂技".equals(showType)) {
			show.setType(ShowType.OPERA);
		}  else if("戏曲综艺".equals(showType)) {
			show.setType(ShowType.OPERA);
		}  else if("儿童节目".equals(showType)) {
			show.setType(ShowType.CHILDREN);
		}  else if("话剧".equals(showType)) {
			show.setType(ShowType.DRAMA);
		}  else {
			show.setType(ShowType.OTHER);
		}
	}

	private Map<String,List<TicketPrice>> getTimeAndPrice(Element ticket,String url) {
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		Elements lineTr = ticket.select("tr[bgcolor=#FEFBF1],tr[bgcolor=#FEF8E0]");
		for (Element element : lineTr) {
			String time = element.select("td.lan-12p18pnn").get(1).text();
			String standardTime = string2Date(time);
			standardTime=standardTime.replace("：", ":");
			List<TicketPrice> list = new ArrayList<TicketPrice>();
			timeAndPrice.put(standardTime, list);//存入对于的时间
			Elements eachPrice = element.select("td").get(2).select("span");
			for (Element price : eachPrice) {
				TicketPrice ticketPrice = new TicketPrice();
				ticketPrice.setPrice(price.select("span>font").text());//存储每个票价
				String detailUrl = price.parent().attr("abs:href");
				if("".equals(detailUrl)) {
					ticketPrice.setExist(false);//是否有票
					ticketPrice.setDetailURL(detailUrl);//买票的具体对应页面
					ticketPrice.setMainURL(url);//买票的的页面
				} else {
					ticketPrice.setExist(true);//是否有票
					ticketPrice.setDetailURL("");//买票的具体对应页面
					ticketPrice.setMainURL(url);//买票的的页面
				}
				ticketPrice.setRemark("");//是否有备注，因为本网站没有任何备注，所以同意设置为"";
				list.add(ticketPrice);
			}
		}
		return timeAndPrice;
	}
	//从另外一个页面取得相关的详细信息
	private String getDetailInfo(String infoUrl) {
		Document dInfo = getDoc(infoUrl);
		Element newDInfo =  PubFun.cleanElement(dInfo.select("td[height=100]").get(0));
		return newDInfo.html();
	}
	
	//转化成标准存储日期
	private String string2Date(String str) { 
		SimpleDateFormat old = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat  now = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String result = null;
		try {
			result = now.format(old.parse(str));
			return result;
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return result;
	}

	public static void main(String[] args) {
		Ticket228Spider ticket228Spider = new Ticket228Spider();
		ticket228Spider.setDao(new DataDao());
		ticket228Spider.extract();
	}
}
