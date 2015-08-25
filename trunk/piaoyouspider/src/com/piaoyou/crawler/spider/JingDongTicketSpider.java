package com.piaoyou.crawler.spider;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.piaoyou.bean.ShowType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

/**
 * 京东票务网  10
 * @author jinquan
 * @date   2011-6-23
 */
public class JingDongTicketSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(JingDongTicketSpider.class);
	static final String JINGDONG_HOME_PAGE="http://www.piaobuy.com/all_ticket/";
	private final static String BASE_URL = "http://www.piaobuy.com/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
		.get(0).get("agency_id").toString();
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy.MM.dd hh:mm");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	@Override
	public void extract() {
		Document doc=getDoc(JINGDONG_HOME_PAGE);
		//演唱会 音乐会 歌剧话剧 舞蹈芭蕾 综艺戏曲 儿童亲子  国家大剧-院音乐厅 国家大剧院-戏剧场 国家大剧院-歌剧院 国家大剧院-小剧场 梅兰芳大剧院
		String idName[]=new String[]{"alla","allb","allc","alld","alle","allf","allg","allh","alli","allj","allk"};
		//类型循环
		Elements elsType=null;
		int showType=0;
		for(int i=0;i<idName.length;i++){//idName.length
			elsType=doc.getElementById(idName[i]).getElementsByClass("alldlp");
			switch(i){
			 case 0:showType=ShowType.CONCERT;
			 case 1:showType=ShowType.SHOW;
			 case 2:showType=ShowType.DRAMA;
			 case 3:showType=ShowType.DANCE;
			 case 4:showType=ShowType.OPERA;
			 case 5:showType=ShowType.CHILDREN;
			 case 6:showType=ShowType.SHOW;
			 case 7:showType=ShowType.DRAMA;
			 case 8:showType=ShowType.DRAMA;
			 case 9:showType=ShowType.DRAMA;
			 case 10:showType=ShowType.OPERA;
			 break;
			}
			for(int x=0;x<elsType.size();x++){
				String oneShowUrl = "";
				try{
					oneShowUrl=elsType.get(x).getElementsByTag("a").get(0).absUrl("href");
					String siteName=elsType.get(x).getElementsByClass("alldlsv").get(0).text();
					extractEach( oneShowUrl, siteName, showType);
				}catch(Exception e){
					log.error(Const.SHOW_ONE_PAGE_URL_ERRO+oneShowUrl,e);
					continue;
				}
			}
		}
	}
	private void extractEach(String oneShowUrl,String siteName,int showType){
		Show show=null;
		TicketPrice price=null;
		List<TicketPrice> list=null;
		Map<String,List<TicketPrice>> timeAndPrice=null;
		Document onePageDoc=getDoc(oneShowUrl);
		if(onePageDoc.getElementById("cmain2_2")!=null){
			show=new Show();
			try{
				String photoUrl=onePageDoc.getElementById("cmain1").getElementsByTag("img").get(0).absUrl("src");
				String showInfo=PubFun.cleanElement(onePageDoc.getElementById("contentl").children()).toString();
				show.setImage_path(photoUrl);
				show.setIntroduction(showInfo);
				String showName=onePageDoc.getElementById("cmain2_1").getElementsByTag("h1").first().text();
				show.setName(showName);
				show.setSiteName(siteName);
			}catch(Exception e){
				log.error(Const.concat(oneShowUrl,siteName,showType),e);
				return;
			}
			Elements dateEls=onePageDoc.getElementById("cmain2_2").getElementsByClass("date");
			if(dateEls.size()==1){
				return;
			}
			Elements pricEls=onePageDoc.getElementById("cmain2_2").getElementsByClass("prices");
			timeAndPrice=new HashMap<String,List<TicketPrice>>();
			for(int c=1;c<dateEls.size();c++){
				try{
					String onedate=dateEls.get(c).text().trim().substring(0,dateEls.get(c).text().trim().lastIndexOf(" ")).trim();
					try {
						onedate = newDate.format(oldDate.parse(onedate));
					} catch (Exception ee) {
						
					}
					Elements onePric=pricEls.get(c).children();
					list=new ArrayList<TicketPrice>() ;
					for(int p=0;p<onePric.size();p++){
						try{
							if(onePric.get(p).text().contains("待定")){
								continue;
							}
							price=new TicketPrice();
							price.setMainURL(oneShowUrl);
							//有票
							if("a".equals(onePric.get(p).tagName())){
								priceFun(price,onePric.get(p).text());
								price.setDetailURL(onePric.get(p).absUrl("href"));
								price.setExist(true);
							}else{
								priceFun(price,onePric.get(p).text());
								price.setExist(false);
							}
							list.add(price);
						}catch(Exception e){
							log.error(Const.concat(oneShowUrl,siteName,showType),e);
							continue;
						}

					}
					timeAndPrice.put(onedate, list);
					show.setTimeAndPrice(timeAndPrice);
					show.setType(showType);
					show.setAgent_id(agentID);
				}catch(Exception e){
					log.error(Const.concat(oneShowUrl,siteName,showType),e);
					continue;
				}

			}
			try {
				getDao().saveShow(show);
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JingDongTicketSpider jingDongTicketSpider=new JingDongTicketSpider();
		jingDongTicketSpider.setDao(new DataDao());
	    jingDongTicketSpider.extract();
		//jingDongTicketSpider.extractEach("http://www.piaobuy.com/ticket_2053.html", "11", 1);

	}
	private void priceFun(TicketPrice price,String priceStr){
		if(PubFun.isMatchRegex(priceStr, "[0-9]*")){
			price.setPrice(priceStr);
		}else if(PubFun.isMatchRegex(priceStr.toLowerCase(),"[\\d]*\\(([^\\)]*)\\)")){
			price.setPrice(priceStr.substring(0,priceStr.indexOf("(")));
			price.setRemark(priceStr.replace(price.getPrice(), ""));
		}else{
			price.setPrice(priceStr);
		}
	}
}
