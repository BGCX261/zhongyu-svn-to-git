package com.piaoyou.crawler.spider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.PubFun;

public class JuoooSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(JuoooSpider.class);
	private static final String BASE_URL =  "http://www.juooo.com/";
	private static final String URL = "http://www.juooo.com/booking?city=-1&page=1";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
	private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();
	@Override
	public void extract() {
		Document doc=getDoc(URL);
		Iterator<Element> Alist=doc.select(".booking_con>ul>li:eq(1) a:gt(1)").iterator();
		while(Alist.hasNext()){
			Element ele=Alist.next();
			String href=ele.absUrl("href").trim();
			String sort=ele.text().trim();
			int sortType=getSortType(sort);
			Document sortDoc=getDoc(href);
			try{
				processByPage(sortDoc,sortType);
			}catch(Exception ee){
				ee.printStackTrace();
			}
			
		}
	}
	/**
	 * @param args
	 */
	public void processByPage(Document doc,int sort){
		Iterator<Element> it=doc.select(".booking_list>.booking>ul>li").iterator();
		while(it.hasNext()){
			Element eleIt=it.next();
		    String imgPath=eleIt.select(".bookimg img").first().absUrl("src").trim();
		    String showName=eleIt.select("h3 a").first().text().trim();
		    String showPath=eleIt.select("h3 a").first().absUrl("href").trim();
		    try{
		    	processDetailPage(imgPath,showName,showPath,sort);
		    }catch(Exception ee){
		    	ee.printStackTrace();
		    }
		}
		if(doc.select(".page a:contains(下一页)").first()==null){
			return;
		}
		String nextPageUrl=doc.select(".page a:contains(下一页)").first().absUrl("href").trim();
		if(nextPageUrl.contains("booking?")){
			Document newdoc=getDoc(nextPageUrl);
			try{
				processByPage(newdoc,sort);
			}catch(Exception ee){
				ee.printStackTrace();
			}
		}else{
			return;
		}
	}
	
	public void processDetailPage(String imgPath,String showName,String showPath,int sort){
		String orderTicket=showPath+"/booking";
		String ticketDetail=showPath+"/info";
		Document OrderDoc=getDoc(orderTicket);
		Document showDetailDoc=getDoc(ticketDetail);
		Element temp=showDetailDoc.select(".Details_text").first();
		if(temp==null){
			return;
		}
		String detail=PubFun.cleanElement(showDetailDoc.select(".Details_text").first()).html();
		Iterator<Element> itList=OrderDoc.select("div.option").iterator();
		while(itList.hasNext()){
			Element div=itList.next();
			processOnlyShow(imgPath,showName, detail,showPath,div,sort);
		}
	}
	public void processOnlyShow(String imgPath,String showName,String detail,String showPath,Element eleDiv,int sort){
		String site=eleDiv.select("h2 a").first().text().trim();
		Map<String, List<TicketPrice>> map = new HashMap<String, List<TicketPrice>>();
		Iterator<Element> itList=eleDiv.select("dd.time a").iterator();
		while(itList.hasNext()){
			Element eleIt=itList.next();
			String showTime=eleIt.text().trim();
			showTime=PubFun.getCurrentYear()+"年"+showTime;
			showTime=showTime.replaceAll("[^日]*星期[^\\d]+", " ").trim();
			String sid=eleIt.attr("sid").trim();
			try{
				Date myDate=sdf.parse(showTime);
				showTime=sdf1.format(myDate);
			}catch(Exception ee){
				ee.printStackTrace();
				return;
			}
			List<TicketPrice> ticketList=new ArrayList<TicketPrice>();
			Document doc=getState("http://www.juooo.com/show/ajax/ticket",sid);
			Iterator<Element> liAlist=doc.select("a").iterator();
			while(liAlist.hasNext()){
				Element A=liAlist.next();
				TicketPrice ticket=new TicketPrice();
				String price=A.text().trim();
			    price=PubFun.getRegex("([^<]+)<[^>]*>", price, 1);
			    try{
			    	price=price.replaceAll("\\\\u[\\w|\\d]{4}", "").replace(" ", "").replace(" ", "");
			    }catch(Exception ee){
			    	ee.printStackTrace();
			    }
				String isExists=A.attr("onmouseover").trim();
				ticket.setExist(true);
				if(isExists.contains("removeClass")){
					ticket.setExist(false);
				}
				ticket.setDetailURL(showPath+"/booking");
				ticket.setMainURL(showPath+"/booking");
				setPriceAndRemark(ticket,price);
				String remark=ticket.getRemark();
				if(remark.contains("=")){
					ticket.setRemark(PubFun.getRegex("([^=]*)=([^=]*)", remark, 1));
				}
				if(!ticket.getPrice().trim().equals("0")){
					ticketList.add(ticket);
				}
			}
			map.put(showTime, ticketList);
		}
		Show show=new Show();
		show.setImage_path(imgPath);
		show.setIntroduction(detail);
		show.setName(showName);
		show.setSiteName(site);
		show.setType(sort);
		show.setAgent_id(agentID);
		show.setTimeAndPrice(map);
		try{
			this.getDao().saveShow(show);
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	private  static Document getState(String url,String sid) {
		Document doc=null;
		for(int i=0;i<tryTimes;i++){
			try {
				doc = Jsoup.connect(url).userAgent(userAgent)
							.timeout(timeout).data("sid", sid).post();
			} catch (IOException e) {
			}
			if(doc != null){
				break;
			}
		}
		return doc;
	}
	
	private void setPriceAndRemark(TicketPrice ticketPrice, String content) {
		if (content.matches("\\d+")) {
			ticketPrice.setPrice(content);
			ticketPrice.setRemark("");
		} else if (content.matches("(\\D+)(\\d+)")) {
			Pattern p = Pattern.compile("(\\D+)(\\d+)");
			Matcher m = p.matcher(content.replaceAll("）|（|\\(|\\)", ""));
			m.matches();
			ticketPrice.setPrice(m.group(2));
			ticketPrice.setRemark(m.group(1));
		} else if (!content.matches("(\\d+)\\(\\)") && content.matches("(\\d+)(\\D+)")) {
			Pattern p = Pattern.compile("(\\d+)(\\D+)");
			Matcher m = p.matcher(content.replaceAll("）|（|\\(|\\)", ""));
			m.matches();
			ticketPrice.setPrice(m.group(1));
			ticketPrice.setRemark(m.group(2));
		} else if (content.matches("(\\D+)(\\d+)(.+)")) {
			Pattern p = Pattern.compile("(\\D+)(\\d+)(.+)");
			Matcher m = p.matcher(content);
			m.matches();
			ticketPrice.setPrice(m.group(2));
			ticketPrice.setRemark(m.group(3).replaceAll("）|（|\\(|\\)", ""));
		} else if (content.matches("(\\d+)(.+)")) {
			Pattern p = Pattern.compile("(\\d+)(.+)");
			Matcher m = p.matcher(content);
			m.matches();
			ticketPrice.setPrice(m.group(1));
			ticketPrice.setRemark(m.group(2).replaceAll("）|（|\\(|\\)", ""));
		}else if (content.matches("(\\d+)\\(\\)")){
			Pattern p = Pattern.compile("(\\d+)(.+)");
			Matcher m = p.matcher(content);
			m.matches();
			ticketPrice.setPrice(m.group(1));
		}
	}
	public int getSortType(String sort) {
		int sortType = ShowType.OTHER;
		if (sort.contains("演唱")) {
			sortType = ShowType.CONCERT;
		} else if (sort.contains("音乐") && !sort.contains("剧")) {
			sortType = ShowType.SHOW;
		} else if (sort.contains("话剧") || sort.contains("歌剧")
				|| sort.contains("音乐剧")) {
			sortType = ShowType.DRAMA;
		} else if (sort.contains("芭蕾") || sort.contains("舞蹈")
				|| sort.contains("舞")) {
			sortType = ShowType.DANCE;
		} else if (sort.contains("戏剧") || sort.contains("戏曲")
				|| sort.contains("二人转") || sort.contains("魔术")
				|| sort.contains("杂技") || sort.contains("相声")
				|| sort.contains("小品") || sort.contains("马戏")) {
			sortType = ShowType.OPERA;
		} else if (sort.contains("儿童亲子")) {
			sortType = ShowType.CHILDREN;
		} else if (sort.contains("体育")) {
			sortType = ShowType.SPORTS;
		} else if (sort.contains("旅游") || sort.contains("电影")) {
			sortType = ShowType.HOLIDAY;
		}
		return sortType;
	}
	public static void main(String[] args) {
		JuoooSpider spider=new JuoooSpider();
		spider.setDao(new DataDao());
		spider.extract();
	}
}
