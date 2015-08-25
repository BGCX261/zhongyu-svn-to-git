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
 * 非常票务网 112
 * @author jinquan
 * @date   2011-6-22
 */
public class FeiChangPiaoTicketSpider  extends TicketSpider implements SpiderTask {
	 private static Log log=LogFactory.getLog(FeiChangPiaoTicketSpider.class);
	 static final String HOME_PAGE="http://www.fcpiao.com/tickets/";
	@Override
	public void extract() {
		Document doc=getDoc(HOME_PAGE);
		//类型循环
		for(Element typeEls:doc.getElementsByClass("tlist")){
			String typeStr=typeEls.getElementsByTag("a").first().attr("name");
			int type=0;
			if(typeStr.contains("mark1")){
				type=ShowType.SPORTS;
			}else if(typeStr.contains("mark10")){
				type=ShowType.SPORTS;
			}else if(typeStr.contains("mark9")){
				type=ShowType.SPORTS;
			}else if(typeStr.contains("mark2")){
				type=ShowType.CONCERT;
			}else if(typeStr.contains("mark3")){
				type=ShowType.DRAMA;
			}else if(typeStr.contains("mark4")){
				type=ShowType.OPERA;
			}else if(typeStr.contains("mark5")){
				type=ShowType.SHOW;
			}else if(typeStr.contains("mark6")){
				type=ShowType.CHILDREN;
			}else if(typeStr.contains("mark7")){
				type=ShowType.DANCE;
			}else if(typeStr.contains("mark8")){
				type=ShowType.OPERA;
			}else if(typeStr.contains("mark11")){
				type=ShowType.HOLIDAY;
			}else{	
				type=ShowType.OTHER;
			}
			for(Element els:typeEls.getElementsByAttributeValue("onmouseover", "this.style.background='#ecf7fe';")){
				try{
					String oneShowUrl=els.getElementsByTag("td").get(0).getElementsByTag("a").get(0).absUrl("href");
					String showName=els.getElementsByTag("td").get(0).text();
					String siteName=els.getElementsByTag("td").get(1).text();
					extractEach(oneShowUrl,showName,siteName,type); 
				}catch(Exception e){
					log.error(Const.SHOW_ONE_PAGE_URL_ERRO,e);
					continue;
				}
			}
		}
	}
	
	private void extractEach(String oneShowUrl,String showName,String siteName,int type){
		TicketPrice price=null;
		List<TicketPrice> list=null;
		Show show=null;
		Map<String,List<TicketPrice>> timeAndPrice=null;
		Document doc=getDoc(oneShowUrl);
		show=new Show();
		show.setAgent_id("112");
		try{
			show.setType(type);
			String showInfo=PubFun.cleanElement(doc.getElementById("t_info")).toString();
			String photoUrl=doc.getElementsByClass("pic").get(0).getElementsByTag("img").get(0).absUrl("src");
			show.setImage_path(photoUrl);
			show.setSiteName(siteName);
			show.setIntroduction(showInfo);
			show.setName(showName);
		}catch(Exception e){
			log.error(Const.concat(oneShowUrl,showName,siteName,type),e);
			return;
		}
		Elements dateEls=doc.getElementById("price").getElementsByClass("l_date");
		//循环日期
		timeAndPrice=new HashMap<String, List<TicketPrice>>();
		for(int i=0;i<dateEls.size();i++){
			try{
				String date=dateEls.get(i).text().replaceAll("\\(([^\\)]*)\\)", "").replace(" ", " ").trim();
				list=new ArrayList<TicketPrice>();
				for(Element priceEls:doc.getElementById("price").getElementsByClass("r_info").get(i).getElementsByTag("tr")){
					try{
						price=new TicketPrice();
						String remark=priceEls.getElementsByTag("td").get(0).text();
						String priceStr=priceEls.getElementsByTag("td").get(1).text().trim().split("[.]")[0].trim();
						
						if(priceEls.getElementsByTag("td").get(1).getElementsByTag("span").get(0).attr("style").contains("color: #999999;")){
							price.setExist(false);
						}else{
							price.setExist(true);
						}
						price.setMainURL(oneShowUrl);
						price.setPrice(priceStr);
						price.setRemark(remark);
						price.setDetailURL(oneShowUrl);
						list.add(price);
					}catch(Exception e){
						log.error(Const.concat(oneShowUrl,showName,siteName,type),e);
						continue;
					}
				}
				timeAndPrice.put(date, list);
			}catch(Exception e){
				log.error(Const.concat(oneShowUrl,showName,siteName,type),e);
				continue;
			}

		}
		show.setTimeAndPrice(timeAndPrice);
		try {
			getDao().saveShow(show);
		} catch (SQLException e) {
			log.error(e);
		}
	}
	
	public static void main(String args[]){
		FeiChangPiaoTicketSpider feiChangPiaoTicketSpider=new FeiChangPiaoTicketSpider();
		feiChangPiaoTicketSpider.setDao(new DataDao());
		feiChangPiaoTicketSpider.extract();
	}

}
