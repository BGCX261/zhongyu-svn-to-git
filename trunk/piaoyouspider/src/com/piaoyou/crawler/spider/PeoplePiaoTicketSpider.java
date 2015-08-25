package com.piaoyou.crawler.spider;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

/**
 * 人民票务在线 77
 * @author jinquan
 * @date   2011-7-4
 */
public class PeoplePiaoTicketSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(PeoplePiaoTicketSpider.class);
	static final String HOME_PAGE="http://www.peoplepiao.com/allpiao.asp";
	@Override
	public void extract() {
		Document doc=getDoc(HOME_PAGE);
		for(Element el :doc.getElementsByAttributeValue("src", "images/go_d_5.gif")){
			String oneShowUrl=el.parent().absUrl("href");
			extractEach(oneShowUrl);
//			break;
		}
	}
	private void extractEach(String oneShowUrl){
		Document doc=getDoc(oneShowUrl);
		TicketPrice price=null;
		List<TicketPrice> list=null;
		Show show=null;
		Map<String,List<TicketPrice>> timeAndPrice=null;
		show=new Show();
		try{
			String showInfo=PubFun.cleanElement(doc.getElementsByClass("main")).html();
			String showName=doc.getElementsByAttributeValue("color", "#6600CC").text();
			String photoUrl=doc.getElementsByAttributeValue("height", "200").first().child(0).absUrl("src");
			String siteName=doc.getElementsMatchingOwnText("场馆：").first().parent().getElementsByTag("td").get(1).text();
			String showType=doc.getElementsMatchingOwnText("您的位置：").first().parent().parent().getElementsByTag("a").last().text();
			show.setIntroduction(showInfo);
			show.setImage_path(photoUrl);
			show.setName(showName);
			show.setSiteName(siteName);
			show.setAgent_id("77");
			show.setType(ShowTypeFormat(showType));
		}catch(Exception e){
			log.error(Const.concat(oneShowUrl),e);
		}
		timeAndPrice=new HashMap<String,List<TicketPrice>>();
		//日期循环
		for(Element dateEls:doc.getElementsByAttributeValue("bgcolor","#FEFBF1")){
			try{
				String date=dateEls.getElementsByTag("td").get(1).text();
				list=new ArrayList<TicketPrice>();
				try{
					date=PubFun.dateFormat(date,true);
				}catch(Exception e){
				}
				for(Element priceEls:dateEls.getElementsByTag("td").get(2).children()){
					try{
						price=new TicketPrice();
						//有票
						if(priceEls.tagName()=="a"){
							price.setExist(true);
							price.setDetailURL(priceEls.absUrl("href"));
						}else{
							price.setExist(false);
						}
						String pricStr=PriceFormat(PubFun.ToDBC(priceEls.text()));
						price.setMainURL(oneShowUrl);
						price.setPrice(pricStr);
						list.add(price);
					}catch(Exception e){
						log.error(Const.concat(oneShowUrl),e);
					}
				}
				timeAndPrice.put(date, list);
			}catch(Exception e){
				log.error(Const.concat(oneShowUrl),e);
			}
		}
		show.setTimeAndPrice(timeAndPrice);
		try {
			getDao().saveShow(show);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 格式化票价
	 * @param price
	 * @return
	 */
	private String PriceFormat(String price){
		if(PubFun.isMatchRegex(price, "[0-9]*")){
			return price;
		}else{
			return price.substring(0,price.indexOf("("));
		}
	}
	
	private int ShowTypeFormat(String showType){
		int typeInt=0;
		if(showType.contains("演唱会")){
			typeInt=ShowType.CONCERT;
		}else if(showType.contains("音乐会")){
			typeInt=ShowType.SHOW;
		}else if(showType.contains("国家大剧院")){
			typeInt=ShowType.DRAMA;
		}else if(showType.contains("舞蹈芭蕾")){
			typeInt=ShowType.DANCE;
		}else if(showType.contains("戏曲综艺 ")){
			typeInt=ShowType.OPERA;
		}else if(showType.contains("话剧歌剧")){
			typeInt=ShowType.DRAMA;
		}else if(showType.contains("北京人艺演出中心")){
			typeInt=ShowType.DRAMA;
		}else if(showType.contains("运动休闲")){
			typeInt=ShowType.SPORTS;
		}else if(showType.contains("梅兰芳大剧院演出季")){
			typeInt=ShowType.DRAMA;
		}else if(showType.contains("儿童亲子")){
			typeInt=ShowType.CHILDREN;
		}else if(showType.contains("魔术马戏 ")){
			typeInt=ShowType.OPERA;
		}else if(showType.contains("长安大戏院 ")){
			typeInt=ShowType.DRAMA;
		}else if(showType.contains("会议展览")){
			typeInt=ShowType.HOLIDAY;
		}else if(showType.contains("电影 ")){
			typeInt=ShowType.OTHER;
		}else if(showType.contains("保利剧院 ")){
			typeInt=ShowType.DRAMA;
		}else{
			typeInt=ShowType.OTHER;
		}
		return typeInt;
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PeoplePiaoTicketSpider peoplePiaoTicketSpider=new PeoplePiaoTicketSpider();
		peoplePiaoTicketSpider.setDao(new DataDao());
		peoplePiaoTicketSpider.extract();
	}

}
