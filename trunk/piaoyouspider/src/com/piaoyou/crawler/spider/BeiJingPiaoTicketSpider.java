package com.piaoyou.crawler.spider;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.PubFun;
import com.piaoyou.util.Const;

/**
 * 北京票务在线  21
 * @author jinquan
 * @date   2011-6-22
 */
public class BeiJingPiaoTicketSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(BeiJingPiaoTicketSpider.class);
	static final String HOME_PAGE="http://www.beijingpiao.com/ProjectAllList.aspx?vid=pp";
	@Override
	public void extract() {
		Document doc=getDoc(HOME_PAGE);
		Elements els = doc.getElementsByClass("list");
		//类型循环
		for(int i=0;i<els.size();i++){
			String showType=els.get(i).select("h1").text();
			int typeInt=0;
			if(showType.contains("演唱会")){
				typeInt=ShowType.CONCERT;
			}else if(showType.contains("音乐会")){
				typeInt=ShowType.SHOW;
			}else if(showType.contains("体育休闲")){
				typeInt=ShowType.SPORTS;
			}else if(showType.contains("舞蹈芭蕾")){
				typeInt=ShowType.DANCE;
			}else if(showType.contains("曲苑杂坛")){
				typeInt=ShowType.OPERA;
			}else if(showType.contains("话剧歌剧")){
				typeInt=ShowType.DRAMA;
			}else if(showType.contains("儿童亲子")){
				typeInt=ShowType.CHILDREN;
			}else{
				typeInt=ShowType.OTHER;
			}
			Elements trEls=els.get(i).getElementsByTag("tr");
			//每场演出循环
			for(int x=1;x<trEls.size();x++){
				String oneShowUrl="";
				try{
					 oneShowUrl=trEls.get(x).getElementsByTag("td").get(0).getElementsByTag("a").get(0).absUrl("href");
				}catch(Exception e){
					log.error(Const.SHOW_ONE_PAGE_URL_ERRO,e);
					continue;
				}
				extractEach(oneShowUrl,typeInt);
			}
		}
	}
	private void extractEach(String oneShowUrl,int typeInt){
		TicketPrice price=null;
		List<TicketPrice> list=null;
		Show show=null;
		Map<String,List<TicketPrice>> timeAndPrice=null;
		
		Document doc=getDoc(oneShowUrl);
		Elements dateEls=doc.getElementById("ISL_Cont_1").getElementsByClass("pl");
		Elements priceEls=doc.getElementsByClass("information");
		show=new Show();
		try{
//			if(doc.getElementById("lblProStuats").text().trim().equals("售票中")){
//				show.setStatus(1);
//			}else{
//				show.setStatus(0);
//			}
			show.setImage_path(doc.getElementsByClass("img_div").get(0).getElementsByTag("img").get(0).absUrl("src"));
			show.setSiteName(doc.getElementById("lblVanueName").text());
			show.setName(doc.getElementById("lblProject_Name").text());
			show.setIntroduction(PubFun.cleanElement(doc.getElementById("H_Project_Introduction").children()).toString());
			show.setType(typeInt);
		}catch(Exception e){
			log.error(Const.concat(oneShowUrl,typeInt),e);
		}
		show.setAgent_id("21");
		timeAndPrice=new HashMap<String,List<TicketPrice>>();
		for(int d=0;d<dateEls.size();d++){
			try{
				String date=dateEls.get(d).getElementsByAttributeValue("type", "hidden").attr("value");
				date=PubFun.dateFormat(date.split("[|]")[1], true);
				Elements prices=priceEls.get(0).getElementsByTag("ul").get(d).getElementsByTag("li");
				list=new ArrayList<TicketPrice>();
				for(int e=1;e<prices.size();e++){
					try{
						price=new TicketPrice();
						price.setDetailURL(oneShowUrl);
						price.setMainURL(oneShowUrl);
						String priceStr=PubFun.ToDBC(prices.get(e).getElementsByTag("a").get(1).text());
						priceFormat(price,priceStr);
						if(prices.get(e).getElementsByTag("a").get(1).attr("class").equals("gray")){
							price.setExist(false);
						}else{
							price.setExist(true);
						}
						list.add(price);
					}catch(Exception e1){
						log.error(Const.concat(oneShowUrl,typeInt),e1);
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
	
	public static void priceFormat(TicketPrice price,String priceStr)throws Exception{
		if(PubFun.isMatchRegex(priceStr,"[\\￥]\\d*")){
			price.setPrice(priceStr.replace("￥", ""));
		}else if(PubFun.isMatchRegex(priceStr,"[\\￥][\\d]*\\(([^\\)]*)\\)")){
			
			price.setPrice(priceStr.substring(1,priceStr.indexOf("(")));
			price.setRemark(priceStr.substring(priceStr.indexOf("("),priceStr.length()));
		}else{
			
		}
	}
	public static void main(String []arg){
		BeiJingPiaoTicketSpider beiJingPiaoTicketSpider=new BeiJingPiaoTicketSpider();
		beiJingPiaoTicketSpider.setDao(new DataDao());
		beiJingPiaoTicketSpider.extract();
	}

}
