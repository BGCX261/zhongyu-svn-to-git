package com.piaoyou.crawler.show;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.piaoyou.bean.CommonInfo;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

public class YongleSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(YongleSpider.class);
	private static final String BASE_URL =  "http://www.228.com.cn/";
	private static final String URL = "http://www.228.com.cn/searchAll.html";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private static final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy.MM.dd HH:mm");
	private final static String agentID = new DataDao().searchBySQL("select id from agency_info where agency_url=?", BASE_URL)
			.get(0).get("id").toString();
	@Override
	public void extract() {
		Document doc=getDoc(URL);
		List<Element> list=doc.select("div[name=m_fenlei].city li:gt(1) a");
	    int aLength=doc.select("div[name=m_fenlei].city li:gt(1) a").size();
	    for(int k=0;k<aLength-3;k++){
	    	Element ele=list.get(k);
			String href=ele.absUrl("href").trim();
			String sort=ele.text().trim();
			int sortType=getSortType(sort);
			Document sortDoc=getDoc(href);
			try{
				processByPage(sortDoc,sortType);
			}catch(Exception ee){
				ee.printStackTrace();
			}
	    }

	}
	public void processByPage(Document doc,int sort){
		Iterator<Element> it=doc.select("div[name=image_list].pros-detail").iterator();
		while(it.hasNext()){
			Element eleIt=it.next();
			String href=eleIt.select("dd h1 a").first().absUrl("href").trim();
			try{
				processLastPage(href,sort);
			}catch(Exception ee){
				
			}
		}
		if(doc.select("div.page").first()==null){
			return;
		}
		//获取总页数
		String needString=doc.select("div.page script[language]").first().html();
		String pageCount=PubFun.getRegex("pg.pageCount[^=]*=([\\d]+)[^;]*;", needString, 1);
		String href=doc.select("#pagef").first().absUrl("action").trim();
		if(pageCount!=null && Integer.parseInt(pageCount)>1){
			for(int i=2;i<=Integer.parseInt(pageCount);i++){
				Document nextDoc=getNextPageDoc(href,String.valueOf(i));
				Iterator<Element> nextit=nextDoc.select("div[name=image_list].pros-detail").iterator();
				while(nextit.hasNext()){
					Element eleIt=nextit.next();
					String nexthref=eleIt.select("dd h1 a").first().absUrl("href").trim();
					try{
						processLastPage(nexthref,sort);
					}catch(Exception ee){
						
					}
				}
			}
			
		}
	}
	/**
	 * 处理最终页面
	 * @param url
	 * @param sort
	 */
	public void processLastPage(String url,int sort){
		Document doc=getDoc(url);
		String detail=doc.select("#prodts-cont1").first().html();
		String showName=doc.select("div.big-shows>h1").first().ownText().trim();
		String imagePic=doc.select(".big-bg img").first().absUrl("src").trim();
		String site=doc.select(".rig-select li:eq(1) a[title]").first().text().trim();
		
		int status = 9 ;
		try {
			String tmp = doc.select(".big-shows h1 .red").text();
			if("[热卖]".equals(tmp)){
				status = 1;
			}else if("[预售]".equals(tmp)){
				status = 0 ;
			}else{
				status =  9;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Element eleZongdai=doc.select(".pro-one img").first();
		int isZongdai= (eleZongdai!=null? 1:0);
		TreeSet<Integer> priceSet = new TreeSet<Integer>();
		Iterator<Element> itTables=doc.select(".graybor").iterator();
		List<String> time = new ArrayList<String>();
		String price = "";
		while(itTables.hasNext()){
			Element table=itTables.next();
			Element td=table.select("td:eq(0)").first();
			String showTime= PubFun.getCurrentYear()+"."+PubFun.getRegex("([\\d]+\\.[\\d]+)[^\\d]*", td.select("span:eq(0)").first().text(), 1)+" "+td.select("span:eq(1)").first().text();
			try{
				showTime=sdf1.format(sdf2.parse(showTime));
			}catch(Exception ee){
				ee.printStackTrace();
			}
			Iterator<Element> itDiv=table.select("td.daily-cosb div.daily-price a").iterator();
			while(itDiv.hasNext()){
				Element aEle=itDiv.next();
				String tmp=aEle.ownText().trim();
				float priceTmp = getPrice(tmp);
				if(priceTmp==0){
					continue;
				}
				priceSet.add(getPrice(tmp));
			}
			time.add(showTime);
		}
		CommonInfo commonInfo=new CommonInfo();
		
		for (Iterator<Integer> iterator = priceSet.iterator(); iterator.hasNext();) {
			int tmp = (int) iterator.next();
			price +=tmp+",";
		}
		if(time==null||time.size()==0){
			return;
		}
		if("".equals(price)){
			return ;
		}else{
			price = price.substring(0,price.length()-1);
		}
		commonInfo.setMin_price(price.split(",")[0]);
		commonInfo.setAgency_id(Integer.parseInt(agentID));
		commonInfo.setRemote_img_url(imagePic);
		commonInfo.setIntroduction(detail);
		commonInfo.setIs_first_agency(isZongdai);
		commonInfo.setMainURL(url);
		commonInfo.setName(showName);
		commonInfo.setPrice(price);
		commonInfo.setShow_time(time);
		commonInfo.setShow_type(sort);
		commonInfo.setSite_name(site);
		commonInfo.setStatus(status);
		commonInfo.setType(Const.COMMON_INFO_TYPE_SHOW);
		commonInfo.setIs_check("0");
		
		try{
			this.saveCommonInfo(commonInfo);
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	private int getPrice(String content) {
		int price =0;
		String tmp ="";
		if (content.matches("\\d+")) {
			tmp = content;
		} else if (content.matches("(\\D+)(\\d+)")) {
			Pattern p = Pattern.compile("(\\D+)(\\d+)");
			Matcher m = p.matcher(content.replaceAll("）|（|\\(|\\)", ""));
			m.matches();
			tmp = m.group(2);
		} else if (!content.matches("(\\d+)\\(\\)") && content.matches("(\\d+)(\\D+)")) {
			Pattern p = Pattern.compile("(\\d+)(\\D+)");
			Matcher m = p.matcher(content.replaceAll("）|（|\\(|\\)", ""));
			m.matches();
			tmp = m.group(1);
		} else if (content.matches("(\\D+)(\\d+)(.+)")) {
			Pattern p = Pattern.compile("(\\D+)(\\d+)(.+)");
			Matcher m = p.matcher(content);
			m.matches();
			tmp = m.group(2);
		} else if (content.matches("(\\d+)(.+)")) {
			Pattern p = Pattern.compile("(\\d+)(.+)");
			Matcher m = p.matcher(content);
			m.matches();
			tmp = m.group(1);
		}else if (content.matches("(\\d+)\\(\\)")){
			Pattern p = Pattern.compile("(\\d+)(.+)");
			Matcher m = p.matcher(content);
			m.matches();
			tmp = m.group(1);
		}
		try{
			if(!"".equals(tmp)){
				price = Integer.parseInt(tmp);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return price;
	}
	public int getSortType(String sort) {
		int sortType = Const.OTHER;
		if (sort.contains("演唱")) {
			sortType = Const.CONCERT;
		} else if (sort.contains("音乐") && !sort.contains("剧")) {
			sortType = Const.SHOW;
		} else if (sort.contains("话剧") || sort.contains("歌剧")
				|| sort.contains("音乐剧")) {
			sortType = Const.DRAMA;
		} else if (sort.contains("芭蕾") || sort.contains("舞蹈")
				|| sort.contains("舞")) {
			sortType = Const.DANCE;
		} else if (sort.contains("戏剧") || sort.contains("戏曲")
				|| sort.contains("二人转") || sort.contains("魔术")
				|| sort.contains("杂技") || sort.contains("相声")
				|| sort.contains("小品") || sort.contains("马戏")) {
			sortType = Const.OPERA;
		} else if (sort.contains("儿童亲子")) {
			sortType = Const.CHILDREN;
		} else if (sort.contains("体育")) {
			sortType = Const.SPORTS;
		} else if (sort.contains("旅游") || sort.contains("电影")) {
			sortType = Const.HOLIDAY;
		}
		return sortType;
	}
	
	private  static Document getNextPageDoc(String url,String pageNum) {
		Document doc=null;
		for(int i=0;i<tryTimes;i++){
			try {
				doc = Jsoup.connect(url).userAgent(userAgent)
							.timeout(timeout).data("pageNum", pageNum).post();
			} catch (IOException e) {
				
			}
			if(doc != null){
				break;
			}
		}
		return doc;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		YongleSpider spider=new YongleSpider();
		spider.setDao(new DataDao());
		spider.extract();
//		spider.processLastPage("http://www.228.com.cn/ticket-3175006.html", 1);
	}

}
