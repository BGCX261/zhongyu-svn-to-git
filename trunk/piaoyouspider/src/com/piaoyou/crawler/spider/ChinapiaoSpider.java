package com.piaoyou.crawler.spider;

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
 * 有水印
 * @author Administrator
 *
 */
public class ChinapiaoSpider extends TicketSpider implements SpiderTask {
	
	private static final Log log = LogFactory.getLog(ChinapiaoSpider.class);
	
	private static final String BASE_URL = "http://www.chinapiao.com/";
	private final static Map<String,Integer> typeCor = new HashMap<String,Integer>();

	private static final String HOME_PAGE ="http://www.chinapiao.com/allticket/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
			.get(0).get("agency_id").toString();
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy-M-d HH:mm");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	static{
		initTypeCor();
	}
	
	/**
	 * 初始化演出类别对应
	 */
	private static void initTypeCor(){
		typeCor.put("[ 演唱会 ]",ShowType.CONCERT);//演唱会
		typeCor.put("[ 音乐会 ]",ShowType.SHOW);//音乐会
		typeCor.put("[ 话剧歌剧 ]",ShowType.DRAMA);//话剧歌剧
		typeCor.put("[ 舞蹈芭蕾 ]",ShowType.DANCE);//舞蹈芭蕾
		typeCor.put("[ 戏曲综艺 ]",ShowType.OPERA);//曲苑杂坛
		typeCor.put("[ 魔术马戏 ]",ShowType.OPERA);//曲苑杂坛
		typeCor.put("[ 曲艺杂技 ]",ShowType.OPERA);//曲苑杂坛
		typeCor.put("[ 儿童亲子 ]",ShowType.CHILDREN);//儿童亲子
		typeCor.put("[ 体育休闲 ]",ShowType.HOLIDAY);//度假休闲
	}
	
	@Override
	public void extract() {
		Document home = getDoc(HOME_PAGE);
		for(Element every:home.select("#contenttable td:eq(0) a")){
			extractEach(every.attr("abs:href"));
		}
	}
	
	/**
	 * 遍历详细演出页面，获取演出信息
	 * @param url		要遍历的url地址
	 */
	private void extractEach(String url) {
		try {
			Document ticket = getDoc(url);
			Show show = new Show();
			show.setAgent_id(agentID);
			show.setType(typeCor.get(ticket.select("span.STYLE57").text()));//类型
			Element img = ticket.select("div>img[width=275]").first();
			show.setImage_path("/".equals(img.attr("src"))?null:img.attr("abs:src"));//图片
			show.setName(img.attr("alt"));//演出名称
			show.setIntroduction(PubFun.cleanElement(ticket.select("div.STYLE59")).html());//简介
			Elements tmp = ticket.select("h6");
			if(tmp.size()==0){//未开始售票,没有票价信息
				return;
			}
			show.setSiteName(tmp.get(0).text());//场馆名称
			
			Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String, List<TicketPrice>>();
			for(int i=0;i<tmp.size();i++){
				List<TicketPrice> priceList = new ArrayList<TicketPrice>();
				i++;//处理时间
				try {
					timeAndPrice.put(newDate.format(oldDate.parse(tmp.get(i).text().replaceAll("：", ":"))),priceList);
				} catch (ParseException e) {
					//TODO 日期未处理完成
					timeAndPrice.put(tmp.get(i).text(),priceList);
				}
				i++;//处理票价
				Elements priceElements = tmp.get(i).select("span.a7,a.a6");
				String[] prices = tmp.get(i).text().split("  ");
				for(int j=0;j<priceElements.size();j++){
					TicketPrice price = new TicketPrice();
					price.setPrice(priceElements.get(j).text().replaceFirst("^(\\d+).*$", "$1"));//票价
					price.setExist("a".equals(priceElements.get(j).nodeName()));//是否有票
					price.setMainURL(url);//页面主地址
					if(price.isExist()){
						price.setDetailURL(priceElements.get(j).attr("abs:href"));//详细地址
					}else if(!price.getPrice().matches("^\\d+$")){
						price.setPrice(price.getPrice().replaceFirst("^(\\d+).*$", "$1"));
					}
					price.setRemark(prices[j].replaceFirst("^\\d+", "").replaceAll("[(（）)]", "").trim());
					if(price.getPrice().length()>0){
						priceList.add(price);
					}
				}
			}
			show.setTimeAndPrice(timeAndPrice);
			getDao().saveShow(show);
		} catch (Exception e) {
			log.error(url,e);
		}
	}

	public static void main(String[] args) {
		ChinapiaoSpider spider = new ChinapiaoSpider();
		spider.setDao(new DataDao());
//		spider.extract();
//		spider.extractEach("http://www.chinapiao.com/ticket_7837.html");
//		spider.extractEach("http://www.chinapiao.com/ticket_7978.html");
//		spider.extractEach("http://www.chinapiao.com/ticket_7960.html");
		spider.extractEach("http://www.chinapiao.com/ticket_8184.html");
//		spider.extractEach("http://www.chinapiao.com/ticket_8157.html");
//		spider.extractEach("http://www.chinapiao.com/ticket_7889.html");
		//http://www.chinapiao.com/ticket_7043.html常年
	}
}
