package com.piaoyou.crawler.group;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.piaoyou.bean.CommonInfo;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

public class Tuangou800Spider extends TicketSpider implements SpiderTask {
	private final static SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public static Map<String,String> urlMap=new HashMap<String,String>();
	private final static String BASE_URL = "http://www.tuan800.com/";
	private final static String agentID = new DataDao().searchBySQL("select id from agency_info where agency_url=?", BASE_URL)
	.get(0).get("id").toString();
	
	public static int count=0;
	static{
		urlMap.put("北京市", "http://www.tuan800.com/beijing/huajuyanchu");
		urlMap.put("上海市", "http://www.tuan800.com/shanghai/huajuyanchu");
		urlMap.put("广州市", "http://www.tuan800.com/guangzhou/huajuyanchu");
		urlMap.put("深圳市", "http://www.tuan800.com/shenzhen/huajuyanchu");
		urlMap.put("成都市", "http://www.tuan800.com/chengdu/huajuyanchu");
	}
	@Override
	public void extract() {
		
		setDao(new DataDao());
		
		Iterator<String> itList=urlMap.keySet().iterator();
		while(itList.hasNext()){
			String city=itList.next();
			String url=urlMap.get(city);
			Document doc=getDoc(url);
			Iterator<Element> aList=doc.select(".deal h3 a").iterator();
			while(aList.hasNext()){
				Element aLink=aList.next();
				try{
					extractEach(aLink.absUrl("href"),city);
				}catch(Exception ee){
					ee.printStackTrace();
				}
			}
			Element flagHave=doc.select("a.next_page").first();
			if(flagHave!=null && flagHave.text().contains("下页")){
				String nextPageUrl=flagHave.absUrl("href");
				processSortPage(nextPageUrl,city);
			}
		}
	}
	
	public void processSortPage(String url,String cityName){
		Document doc=getDoc(url);
		Iterator<Element> aList=doc.select(".deal h3 a").iterator();
		while(aList.hasNext()){
			Element aLink=aList.next();
			try{
				extractEach(aLink.absUrl("href"),cityName);
			}catch(Exception ee){
				ee.printStackTrace();
			}
		}
		Element flagHave=doc.select("a.next_page").first();
		if(flagHave!=null && flagHave.text().contains("下页")){
			String nextPageUrl=flagHave.absUrl("href");
			processSortPage(nextPageUrl,cityName);
		}else{
			return;
		}
	}
	public void extractEach(String url,String cityName){
		Document doc=getDoc(url);
		String showName=doc.select(".deal_content h1 a").first().text();
		String info=doc.select("#dealinfo").attr("info").trim();
		String[] array=info.split(",");
		Date  startTime=new Date(Long.parseLong(array[3]));
		Date  endTime=new Date(Long.parseLong(array[2]));
		String discount=doc.select("#dealinfo h3 span").first().text().replaceAll("折扣：", "").replace("折", "");
		String instruction=doc.select(".info").first().text();
		String min_price=doc.select("#dealinfo h3 b").first().text().trim();
		String price=doc.select("#dealinfo h3 em").first().text().trim().replaceAll("原价：", "").replaceAll("元", "");
		String address = doc.select("#dealinfo a").get(0).previousSibling().outerHtml().trim();
		CommonInfo commonInfo=new CommonInfo();
		String imgUrl=doc.select(".deal_content div.left img").first().absUrl("src").trim();
		commonInfo.setRemote_img_url(imgUrl);
		commonInfo.setMainURL(url);
		commonInfo.setIntroduction(instruction);
		commonInfo.setType(Const.COMMON_INFO_TYPE_GROUP);
		commonInfo.setName(showName);
		commonInfo.setAddress(address);
		commonInfo.setPrice(price);
		List<String> stList=new ArrayList<String>();
		stList.add(fmt.format(startTime)+","+fmt.format(endTime));
		commonInfo.setShow_time(stList);
		commonInfo.setCityName(cityName);
		commonInfo.setDiscount(discount);
		commonInfo.setPrice(price);
		commonInfo.setMin_price(min_price);
		commonInfo.setAgency_id(Integer.parseInt(agentID));
		commonInfo.setIs_check("1");
		try{
			this.saveCommonInfo(commonInfo);
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Tuangou800Spider spider=new Tuangou800Spider();
		spider.extract();
//		spider.setDao(new DataDao());
//		spider.extractEach("http://www.tuan800.com/deal/jin62xiaos_2965357", "beijingshi");
	}

}
