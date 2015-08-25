package com.piaoyou.crawler.spider;

import java.io.IOException;
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
import com.piaoyou.util.PubFun;

/**
 * piaoCN
 * 图片带有水印
 * @author Administrator
 *
 */
public class PiaoCNSpider extends TicketSpider implements SpiderTask {
	
	private static final Log log = LogFactory.getLog(PiaoCNSpider.class);
	
	private final static String HOME_PAGE = "http://www.piaocn.com/All.html";
	private final static String BASE_URL = "http://www.piaocn.com/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
							.get(0).get("agency_id").toString();
	private final static Map<String,Integer> typeCor = new HashMap<String,Integer>();
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	static{
		initTypeCor();
	}
	
	@Override
	public void extract() {
		Document start = getDoc(HOME_PAGE);
		for(Element every:start.select("a.preview")){
			try {
				extractEach(every.attr("abs:href"));
			} catch (Exception e) {
				log.error(every.attr("abs:href"),e);
			}
		}
	}
	
	/**
	 * 初始化演出类别对应
	 */
	private static void initTypeCor(){
		typeCor.put("演唱会",ShowType.CONCERT);//演唱会
		typeCor.put("音乐会",ShowType.SHOW);//音乐会
		typeCor.put("话剧歌剧",ShowType.DRAMA);//话剧歌剧
		typeCor.put("舞蹈芭蕾",ShowType.DANCE);//舞蹈芭蕾
		typeCor.put("亲子儿童",ShowType.CHILDREN);//儿童亲子
		typeCor.put("京剧",ShowType.OPERA);//曲苑杂坛
		typeCor.put("杂技",ShowType.OPERA);//曲苑杂坛
		typeCor.put("功夫表演",ShowType.OPERA);//曲苑杂坛
		typeCor.put("体育/休闲/滑雪/温泉",ShowType.HOLIDAY);//休闲
		typeCor.put("国家大剧院戏剧场",ShowType.OPERA);//曲苑杂坛
		typeCor.put("国家大剧院音乐厅",ShowType.SHOW);//音乐会
		typeCor.put("国家大剧院歌剧院",ShowType.DRAMA);//话剧歌剧
		typeCor.put("相声/晚会",ShowType.OPERA);//曲苑杂坛
		typeCor.put("打开艺术之门(中山音乐堂)",ShowType.SHOW);//音乐会
		typeCor.put("打开音乐之门(北京音乐厅)",ShowType.SHOW);//音乐会
		typeCor.put("打开音乐之门--北京音乐厅",ShowType.SHOW);//音乐会
		typeCor.put("电影票/兑换券",ShowType.HOLIDAY);//休闲
		typeCor.put("国家大剧院小剧场",ShowType.OTHER);//未处理
	}
	
	public static void main(String[] args) throws ParseException, SQLException, IOException {
		PiaoCNSpider spider = new PiaoCNSpider();
		spider.setDao(new DataDao());
		spider.extract();
//	spider.extractEach("http://www.piaocn.com/YanChuPiao/3779.Html");
		//spider.extractEach("http://www.piaocn.com/YanChuPiao/4022.Html");
	}

	/**
	 * 通过目标url取得演出需要的元素
	 * @param url
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private void extractEach(String url) throws SQLException, IOException, ParseException {
		Document ticket = getDoc(url);
		Show show = new Show();
		show.setAgent_id(agentID);
		show.setImage_path(ticket.select("div.PiaoShow_Limg>img").first().attr("abs:src"));
		Elements title = ticket.select("div.PiaoShow_Rtxt>div");
		show.setName(title.get(0).text());//标题
		show.setSiteName(title.get(2).text().split("\\b")[3]);//场馆
		show.setIntroduction(PubFun.cleanElement(ticket.select("#tab").first().child(0).child(1)).html());
		show.setType(typeCor.get(ticket.select("a.Dh:eq(2)").text()));
		if(show.getType() == ShowType.OTHER){//单独处理小剧场
			if(show.getName().contains("音乐")){
				show.setType(ShowType.SHOW);
			}else if(show.getName().contains("剧")){
				show.setType(ShowType.DRAMA);
			}else if(show.getName().contains("舞")){
				show.setType(ShowType.DANCE);
			}
		}
		
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		show.setTimeAndPrice(timeAndPrice);
		
		for(Element priceElement:ticket.select("table#pjinfo tr:gt(0)")){
			Elements tmp = priceElement.select("td");
			String time = "";
			if(tmp.get(0).text().length() == 15){
				time = tmp.get(0).text().substring(0,10)+" "+tmp.get(1).text().replace("：", ":");
			}else{
				//TODO 时间未处理
				time = tmp.get(0).text()+" "+tmp.get(1).text();
			}
			time=time.replace(" ", " ");
			try{
				time = newDate.format(oldDate.parse(time));
			}catch(Exception ee){
				
			}
			List<TicketPrice> priceList = new ArrayList<TicketPrice>();
			timeAndPrice.put(time, priceList);
			for(Element each:tmp.get(3).select("span,a.A_S_2")){
				if(each.text().matches("^\\D.*")){
					continue;
				}
				TicketPrice price = new TicketPrice();
				price.setPrice(each.text().replaceAll("(^\\d+).*$", "$1"));
				price.setExist("a".equals(each.nodeName()));
				if(price.isExist() && each.hasAttr("title")){
					price.setRemark(each.attr("title"));
				}
				if(each.text().contains("：")){
					price.setRemark(each.text().substring(each.text().indexOf("：")+1));
				}
				price.setMainURL(url);
				priceList.add(price);
			}
		}
		getDao().saveShow(show);
	}
	
}
