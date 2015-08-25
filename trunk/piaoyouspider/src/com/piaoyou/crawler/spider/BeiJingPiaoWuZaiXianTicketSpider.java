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
 * @author jinquan
 * @date   2011-7-4
 */
public class BeiJingPiaoWuZaiXianTicketSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(BeiJingPiaoWuZaiXianTicketSpider.class);
	static final String HOME_PAGE="http://www.jchpiao.com/ticked.asp";
	@Override
	public void extract() {
		Document doc=getDoc(HOME_PAGE);
		//类型循环
		for(Element els:doc.getElementsByAttributeValue("class", "STYLE7")){
			try{
				String type=els.text();
				for(Element els1 :els.parent().parent().getElementsByAttributeValue("src", "images/shop.gif")){
					try{
						String oneShowUrl=els1.parent().absUrl("href");
						extractEach(oneShowUrl, getShowType(type));
					}catch(Exception e){
						log.error(Const.SHOW_ONE_PAGE_URL_ERRO,e);
					}
				}
			}catch(Exception e){
				log.error(Const.SHOW_TYPE_ERRO,e);
			}
		}
	}
	private int getShowType(String showType){
		
		int typeInt=0;
		if(showType.contains("演唱会")){
			typeInt=ShowType.CONCERT;
		}else if(showType.contains("音乐会")){
			typeInt=ShowType.SHOW;
		}else if(showType.contains("人艺话剧")){
			typeInt=ShowType.DRAMA;
		}else if(showType.contains("舞蹈 芭蕾")){
			typeInt=ShowType.DANCE;
		}else if(showType.contains("京剧 戏曲 综艺")){
			typeInt=ShowType.OPERA;
		}else if(showType.contains("话剧 音乐剧")){
			typeInt=ShowType.DRAMA;
		}else if(showType.contains("儿童亲子")){
			typeInt=ShowType.CHILDREN;
		}else if(showType.contains("国家大剧院音乐厅")){
			typeInt=ShowType.SHOW;
		}else if(showType.contains("国家大剧院歌剧院")){
			typeInt=ShowType.DRAMA;
		}else if(showType.contains("国家大剧院戏剧场")){
			typeInt=ShowType.OPERA;
		}else{
			typeInt=ShowType.OTHER;
		}
		return typeInt;
	}
	private void extractEach(String oneShowUrl,int typeInt){
		TicketPrice price=null;
		List<TicketPrice> list=null;
		Show show=null;
		Map<String,List<TicketPrice>> timeAndPrice=null;
		Document doc=getDoc(oneShowUrl);
		show=new Show();
		try{
			String showInfo=PubFun.cleanElement(doc.getElementsByAttributeValue("face", "Verdana")).html();
			String imgUrl=doc.getElementsByAttributeValue("height", "300").first().absUrl("src");
			String showName=doc.getElementsByClass("STYLE7").first().text();
			show.setIntroduction(showInfo);
			show.setImage_path(imgUrl);
			show.setName(showName);
			show.setAgent_id("97");
			show.setType(typeInt);
		}catch(Exception e){
			log.error(Const.concat(oneShowUrl,typeInt),e);
			return;
		}
		
		Elements dateEls=doc.getElementsByAttributeValue("bgcolor", "#F0F0F0").first().parent().getElementsByTag("tr");
		timeAndPrice=new HashMap<String, List<TicketPrice>>();
		for(int i=1;i<dateEls.size();i++){
			try{
				String date=dateEls.get(i).getElementsByTag("td").get(1).text().trim();
				try{
					date=PubFun.dateFormat(PubFun.ToDBC(date),true);
				}catch(Exception e){
				}
				
				String siteName=dateEls.get(i).getElementsByTag("td").get(0).text();
				show.setSiteName(siteName);
				list=new ArrayList<TicketPrice>();
				for(Element priceEls:dateEls.get(i).getElementsByTag("td").get(2).children()){
					try{
						price=new TicketPrice();
						price.setMainURL(oneShowUrl);
						String priceStr=PubFun.ToDBC(priceEls.text().trim());
						if(priceEls.tagName().equals("a")){
							price.setExist(true);
							price.setDetailURL(priceEls.absUrl("href"));
						}else{
							price.setExist(false);
						}
						//数字
						if(PubFun.isMatchRegex(priceStr, "[0-9]*")){
							price.setPrice(priceStr);
						}else if(PubFun.isMatchRegex(priceStr,"[\\d]*\\(([^\\)]*)\\)")){//280(100X2)
							price.setPrice(priceStr.substring(0, priceStr.indexOf("(")));
							price.setRemark(priceStr.substring(priceStr.indexOf("("),priceStr.length()));
						}else if(PubFun.isMatchRegex(priceStr,"\\D*\\d*")){//套票100
							price.setPrice(priceStr.replaceAll("\\D*", ""));
							price.setRemark(priceStr.replace(price.getPrice(), ""));
						}else if(PubFun.isMatchRegex(priceStr,"\\d*\\D*")){//100套票
							price.setPrice(priceStr.replaceAll("[A-Za-z\u4e00-\u9fa5]", ""));
							price.setRemark(priceStr.replace(price.getPrice(), ""));
						}else if(PubFun.isMatchRegex(priceStr,"\\D*\\d*\\(([^\\)]*)\\)")){//套票699(380X2)
							priceStr=priceStr.replaceAll("[\u4e00-\u9fa5]", "");
							price.setPrice(priceStr.substring(0, priceStr.indexOf("(")));
							price.setRemark(priceStr.substring(priceStr.indexOf("("),priceStr.length()));
						}else if(PubFun.isMatchRegex(priceStr,"\\D*\\d*\\D*\\(([^\\)]*)\\)")){//三人1500元(680*3)
							priceStr=priceStr.replaceAll("[A-Za-z\u4e00-\u9fa5]*", "");
							price.setPrice(priceStr.substring(0, priceStr.indexOf("(")));
							price.setRemark(priceStr.substring(priceStr.indexOf("("),priceStr.length()));
						}else{
							price.setPrice(priceStr);
						}
						list.add(price);
					}catch(Exception e ){
						log.error(Const.concat(oneShowUrl,typeInt),e);
						continue;
					}
				}
				timeAndPrice.put(date, list);
			}catch(Exception e){
				log.error(Const.concat(oneShowUrl,typeInt),e);
				continue;
			}
		}
		show.setTimeAndPrice(timeAndPrice);
		try {
			getDao().saveShow(show);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		BeiJingPiaoWuZaiXianTicketSpider beiJingPiaoWuZaiXianTicketSpider=new BeiJingPiaoWuZaiXianTicketSpider();
		beiJingPiaoWuZaiXianTicketSpider.setDao(new DataDao());
		beiJingPiaoWuZaiXianTicketSpider.extract();
	}

}
