package com.piaoyou.crawler.spider;

import java.sql.SQLException;
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
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;


/**
 * 北京演出票务网  12
 * @author jinquan
 * @date   2011-6-22
 */
public class BeiJingYanChuSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(BeiJingYanChuSpider.class);
	static final String HOME_PAGE="http://www.yanchupiao.com/All_Ticket.html";
	@Override
	public void extract() {
		Document doc=getDoc(HOME_PAGE);
		Elements els = doc.getElementsByAttributeValue("onmouseover", "this.bgColor='#EDEFF1'");
		for(int i=0;i<els.size();i++){
			try{
				Elements elsTd=els.get(i).getElementsByTag("td");
				String oneShowUrl=elsTd.get(0).getElementsByTag("a").get(0).absUrl("href");
				String siteName=elsTd.get(2).text().trim();
				extractEach(oneShowUrl,siteName);
			}catch(Exception e){
				log.error(Const.SHOW_ONE_PAGE_URL_ERRO,e);
				continue;
			}
		}
	}
	
	private void extractEach(String oneShowUrl,String siteName){
		Show show=null;
		TicketPrice price=null;
		List<TicketPrice> list=null;
		Map<String,List<TicketPrice>> timeAndPrice=null;
		Document oneDoc=getDoc(oneShowUrl);
		show=new Show();
		try{
			String showInfo=PubFun.cleanElement(oneDoc.getElementsByTag("ul")).toString();
			String typeStr=oneDoc.getElementsByAttributeValue("height", "29").get(0).getElementsByTag("a").last().text();
			String showName=oneDoc.getElementsByClass("font1").text();
			String shwoImg=oneDoc.getElementsByAttributeValue("width", "225").get(0).getElementsByTag("img").get(0).absUrl("src");
//			String showStatus=oneDoc.getElementsContainingOwnText("售票状态").first().parent().getElementsByTag("b").text();
//			if(showStatus.equals("预定")){
//				show.setStatus(0);
//			}else{
//				show.setStatus(1);
//			}
			if("待定".equals(siteName.trim())){
				return ;
			}
			show.setSiteName(siteName);
			show.setName(showName);
			show.setImage_path(shwoImg);
			show.setAgent_id("12");
			show.setIntroduction(showInfo);
			if(typeStr.contains("演唱会")){
				show.setType(ShowType.CONCERT);
			}else if(typeStr.contains("音乐会")){
				show.setType(ShowType.SHOW);
			}else if(typeStr.contains("舞 ")){
				show.setType(ShowType.DANCE);
			}else if(typeStr.contains("亲子儿童")){
				show.setType(ShowType.CHILDREN);
			}else if(typeStr.contains("常年演出")){
				show.setType(ShowType.OTHER);
			}else if(typeStr.contains("相声")){
				show.setType(ShowType.OPERA);
			}else if(typeStr.contains("综艺杂技")){
				show.setType(ShowType.OPERA);
			}else if(typeStr.contains("话剧")){
				show.setType(ShowType.DRAMA);
			}else if(typeStr.contains("音乐剧")){
				show.setType(ShowType.DRAMA);
			}else if(typeStr.contains("歌剧")){
				show.setType(ShowType.DRAMA);
			}else if(typeStr.contains("电影票")){
				show.setType(ShowType.OTHER);
			}else if(typeStr.contains("电影票")){
				show.setType(ShowType.OTHER);
			}else if(typeStr.contains("戏曲")){
				show.setType(ShowType.OPERA);
			}else if(typeStr.contains("体育赛事")){
				show.setType(ShowType.SPORTS);
			}else if(typeStr.contains("休闲类")){
				show.setType(ShowType.HOLIDAY);
			}else if(typeStr.contains("国家大剧院")){
				show.setType(ShowType.DRAMA);
			}else if(typeStr.contains("梅兰芳大剧院")){
				show.setType(ShowType.OPERA);
			}else{
				show.setType(ShowType.OTHER);
			}
		}catch(Exception e){
			log.error(Const.concat(oneShowUrl,siteName),e);
			return;
		}
		Elements dateEls=null;
		try{
			dateEls=oneDoc.getElementsByClass("biaoge").get(5).getElementsByTag("table").get(1).getElementsByTag("tr");
		}catch(Exception e){
			dateEls=oneDoc.getElementsByClass("biaoge").get(6).getElementsByTag("table").get(1).getElementsByTag("tr");
		}
		//没有订票区域
		if(dateEls.size()<=1){
		}else{
			timeAndPrice=new HashMap<String,List<TicketPrice>>();
			for(int d=1;d<dateEls.size();d++){
				try{
					String date=dateEls.get(d).getElementsByTag("td").get(0).text();
					//2011-12-12 12:30:00
					if(date.length()==19){
						date=date.substring(0,date.lastIndexOf(":"));
					}else if(date.length()==10){
						//需要在前面查找时间
						String temp=oneDoc.getElementsByClass("font_nei").first().getElementsByTag("strong").first().childNode(0).toString();
						date=date+" "+temp.substring(temp.indexOf(":")-2,temp.indexOf(":")+3);
					}else if(date.contains("常年")){
//						date="all";
					}else{
//						date="all";
					}
					String []prices=dateEls.get(d).getElementsByTag("td").get(2).text().trim().split(" ");
					Elements isExit=dateEls.get(d).getElementsByTag("td").get(2).children();
					Elements isExit1 = null;
					//有套票
					if(dateEls.get(d).getElementsByTag("td").size()==4){
						isExit1=dateEls.get(d).getElementsByTag("td").get(3).children();
					}
					list=new ArrayList<TicketPrice>() ;
					//票价循环
					for(int p=0;p<prices.length;p++){
						
						//单票
						price=new TicketPrice();
						price.setMainURL(oneShowUrl);
						for(int b=0;b<isExit.size();b++){
							try{
								if(prices[p].equals(isExit.get(b).text())){
									price.setExist(true);
									price.setDetailURL(isExit.get(b).absUrl("href"));
								//	price.setPrice(prices[p]);
									break;
								}else{
									price.setExist(false);
								}
							}catch(Exception e){
								log.error(Const.concat(oneShowUrl,siteName),e);
								continue;
							}
						}
						price.setPrice(prices[p]);
						list.add(price);
						
					}
					//套票
					if(dateEls.get(d).getElementsByTag("td").size()==4){
						for(int p=0;p<isExit1.size();p++){
							if(PubFun.isOdd(p)){
								price=new TicketPrice();
								try{
									price.setMainURL(oneShowUrl);
									price.setPrice(isExit1.get(p-1).text());
									price.setRemark(isExit1.get(p).text());
									if(!isExit1.get(p).getElementsByTag("a").isEmpty()){
										price.setExist(true);
										price.setDetailURL(isExit1.get(p).getElementsByTag("a").get(0).absUrl("href"));
									}
									list.add(price);
								}catch(Exception e){
									log.error(Const.concat(oneShowUrl,siteName),e);
									continue;
								}
							}
						}
					}
					timeAndPrice.put(date, list);

				}catch(Exception e){
					log.error(Const.concat(oneShowUrl,siteName),e);
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
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BeiJingYanChuSpider beiJingYanChuSpider=new BeiJingYanChuSpider();
		beiJingYanChuSpider.setDao(new DataDao()); 
		beiJingYanChuSpider.extract();
	}

}
