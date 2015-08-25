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
 * 飞翔票务在线
 * @author jinquan
 * @date   2011-6-22
 */
public class FyPiaoTicketSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(FyPiaoTicketSpider.class);
	static final String HOME_PAGE="http://www.fypiao.com/all.asp";

	@Override
	public void extract() {
		Document doc=getDoc(HOME_PAGE);
		Elements type = doc.getElementsByAttributeValue("color", "ffffff");
		for(int i=0;i<type.size();i++){
			int typeInt=0;
			if(type.get(i).text().contains("演唱会")){
				typeInt=ShowType.CONCERT;
			}else if(type.get(i).text().contains("音乐会")){
				typeInt=ShowType.SHOW;
			}else if(type.get(i).text().contains("话剧歌剧")){
				typeInt=ShowType.DRAMA;
			}else if(type.get(i).text().contains("歌舞芭蕾")){
				typeInt=ShowType.DANCE;
			}else if(type.get(i).text().contains("戏曲曲艺")){
				typeInt=ShowType.OPERA;
			}else if(type.get(i).text().contains("儿童亲子")){
				typeInt=ShowType.CHILDREN;
			}else if(type.get(i).text().contains("魔术马戏")){
				typeInt=ShowType.OPERA;
			}else if(type.get(i).text().contains("综艺相声")){
				typeInt=ShowType.OPERA;
			}else if(type.get(i).text().contains("电影通票")){
				typeInt=ShowType.OTHER;
			}else if(type.get(i).text().contains("体育赛事")){
				typeInt=ShowType.SPORTS;
			}else{
				typeInt=ShowType.OTHER;
			}
			Element typeEl=doc.getElementsByAttributeValue("cellpadding", "3").get(i);
			for(Element els:typeEl.getElementsByAttributeValue("onmouseover", "this.bgColor='#EDEFF1'")){
				try{
					String showName=els.getElementsByTag("td").get(0).text();
					String siteName=els.getElementsByTag("td").get(2).text();
					String oneShowUrl=els.getElementsByTag("td").get(0).getElementsByTag("a").get(0).absUrl("href");
					extractEach(oneShowUrl,showName,siteName,typeInt);
				}catch(Exception e){
					log.error(Const.SHOW_ONE_PAGE_URL_ERRO,e);
					continue;
					
				}
				
			}
		}
	}
	
	
	private void extractEach(String oneShowUrl,String showName,String siteName,int typeInt){
		TicketPrice price=null;
		List<TicketPrice> list=null;
		Show show=null;
		Map<String,List<TicketPrice>> timeAndPrice=null;
		Document oneDoc=getDoc(oneShowUrl);
		show=new Show();
		Elements trEls=null;
		try{
			String photoUrl=oneDoc.getElementsByAttributeValue("width", "170").get(0).absUrl("src");
			trEls=oneDoc.getElementsByAttributeValue("cellpadding", "5").get(0).getElementsByTag("tr");
			show.setAgent_id("25");
			show.setImage_path(photoUrl);
			show.setName(showName);
			show.setSiteName(siteName);
			show.setType(typeInt);
		}catch(Exception e){
			log.error(Const.concat(oneShowUrl,showName,siteName,typeInt),e);
			return;
		}
		
		timeAndPrice=new HashMap<String,List<TicketPrice>>();
		for(int x=1;x<trEls.size();x++){
			try{
				String date=PubFun.ToDBC(trEls.get(x).getElementsByTag("td").get(1).text());
				if(!date.equals("常年")){
					date=PubFun.dateFormat(date.replace(" ", " "), true);
				}
				list=new ArrayList<TicketPrice>();
				for(Element priceEls:trEls.get(x).getElementsByTag("td").get(2).children()){
					try{
						price=new TicketPrice();
						//有票
						if(priceEls.tagName().equals("a")){
							price.setExist(true);
							price.setDetailURL(oneShowUrl);
//							price.setDetailURL(priceEls.absUrl("href"));
						}else{
							price.setExist(false);
							price.setDetailURL(oneShowUrl);
//							price.setDetailURL(oneShowUrl);
						}
						String priceTemp=PubFun.ToDBC(priceEls.text().trim().replace(" ", ""));
						String remark = null;
						String onePrice = null;
						if(PubFun.isMatchRegex(priceTemp, "[0-9]*")){
							onePrice=priceTemp;
							remark=null;
						//场地1680
						}else if(PubFun.isMatchRegex(priceTemp, "\\D*\\d*")){
							onePrice=priceTemp.replaceAll("\\D*", "");
							remark=onePrice.replace(onePrice, "");
						//套票：1000(680x2)
						}else if(PubFun.isMatchRegex(priceTemp,"\\D*\\:\\d*\\(([^\\)]*)\\)")){
							onePrice=priceTemp.substring(priceTemp.indexOf(":")+1,priceTemp.indexOf("("));
							remark=priceTemp.replace(onePrice, "");
						//2000(1280x2) 2011(场地)
						}else if(PubFun.isMatchRegex(priceTemp,"\\d*\\(([^\\)]*)\\)")){
							onePrice=priceTemp.substring(0,priceTemp.indexOf("("));
							remark=priceTemp.replace(onePrice, "");
						//480SVIP	
						}else if(PubFun.isMatchRegex(priceTemp,"\\d*\\D*")){
							onePrice=priceTemp.replaceAll("\\D*", "");
							remark=priceTemp.replace(onePrice, "");
						//580VIP套票:230(180X2)
						}else if(PubFun.isMatchRegex(priceTemp,"\\d*\\D*\\:\\d*\\(([^\\)]*)\\)")){
							onePrice=priceTemp.substring(priceTemp.indexOf(":")+1,priceTemp.indexOf("("));
							remark=priceTemp.replace(onePrice, "");
						}else{
						}
						price.setPrice(onePrice);
						price.setRemark(remark);
						price.setMainURL(oneShowUrl);
						list.add(price);
					}catch(Exception e){
						log.error(Const.concat(oneShowUrl,showName,siteName,typeInt),e);
						continue;
					}
				}
				timeAndPrice.put(date, list);	
			}catch(Exception e){
				log.error(Const.concat(oneShowUrl,showName,siteName,typeInt),e);
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		FyPiaoTicketSpider fyPiaoTicketSpider=new FyPiaoTicketSpider();
		fyPiaoTicketSpider.setDao(new DataDao());
		fyPiaoTicketSpider.extract();
		}

}
