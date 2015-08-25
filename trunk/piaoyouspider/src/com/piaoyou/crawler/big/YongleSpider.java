package com.piaoyou.crawler.big;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.crawler.spider.JuoooSpider;
import com.piaoyou.util.PubFun;

public class YongleSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(JuoooSpider.class);
	private static final String BASE_URL =  "http://www.juooo.com/";
	private static final String URL = "http://www.228.com.cn/searchAll.html";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh:mm:ss");
	private static final SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	@Override
	public void extract() {
		Document doc=getDoc(URL);
		Iterator<Element> Alist=doc.select("div[name=m_fenlei].city li:gt(1) a").iterator();
		while(Alist.hasNext()){
			Element ele=Alist.next();
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
		String detail=PubFun.cleanElement(doc.select("#prodts-cont1").first()).html();
		String showName=doc.select("div.big-shows>h1").first().ownText().trim();
		String imagePic=doc.select(".big-bg img").first().absUrl("src").trim();
		String site=doc.select(".rig-select li:eq(1) a[title]").first().text().trim();
		Element eleZongdai=doc.select(".pro-one img").first();
		boolean isZongdai=eleZongdai==null? true:false;
		Map<String, List<TicketPrice>> map = new HashMap<String, List<TicketPrice>>();
		Iterator<Element> itTables=doc.select(".graybor").iterator();
		while(itTables.hasNext()){
			Element table=itTables.next();
			Element td=table.select("td:eq(0)").first();
			String showTime=PubFun.getCurrentYear()+"."+PubFun.getRegex("([\\d]+\\.[\\d]+)[^\\d]*", td.select("span:eq(0)").first().text(), 1)+" "+td.select("span:eq(2)").first().text();
			List<TicketPrice> list=new ArrayList<TicketPrice>();
			Iterator<Element> itDiv=table.select("td.daily-cosb div.daily-price a").iterator();
			while(itDiv.hasNext()){
				Element aEle=itDiv.next();
				TicketPrice ticket=new TicketPrice();
				ticket.setExist(true);
				if("gray".equals(aEle.attr("class").trim())){
					ticket.setExist(false);
				}
				ticket.setDetailURL(url);
				ticket.setMainURL(url);
				String price=aEle.ownText().trim();
				setPriceAndRemark(ticket,price);
				list.add(ticket);
			}
			map.put(showTime, list);
		}
		Show show=new Show();
		show.setImage_path(imagePic);
		show.setIntroduction(detail);
		show.setName(showName);
		show.setSiteName(site);
		show.setType(sort);
		show.setAgent_id("12234");
		show.setTimeAndPrice(map);
		show.setFirstAgent(isZongdai);
		try{
			//this.getDao().saveShow(show);
			System.out.println(show);
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	private void setPriceAndRemark(TicketPrice ticketPrice, String content) {
		if (content.matches("\\d+")) {
			ticketPrice.setPrice(content);
			ticketPrice.setRemark("");
		} else if (content.matches("(\\D+)(\\d+)")) {
			Pattern p = Pattern.compile("(\\D+)(\\d+)");
			Matcher m = p.matcher(content.replaceAll("）|（|\\(|\\)", ""));
			m.matches();
			ticketPrice.setPrice(m.group(2));
			ticketPrice.setRemark(m.group(1));
		} else if (!content.matches("(\\d+)\\(\\)") && content.matches("(\\d+)(\\D+)")) {
			Pattern p = Pattern.compile("(\\d+)(\\D+)");
			Matcher m = p.matcher(content.replaceAll("）|（|\\(|\\)", ""));
			m.matches();
			ticketPrice.setPrice(m.group(1));
			ticketPrice.setRemark(m.group(2));
		} else if (content.matches("(\\D+)(\\d+)(.+)")) {
			Pattern p = Pattern.compile("(\\D+)(\\d+)(.+)");
			Matcher m = p.matcher(content);
			m.matches();
			ticketPrice.setPrice(m.group(2));
			ticketPrice.setRemark(m.group(3).replaceAll("）|（|\\(|\\)", ""));
		} else if (content.matches("(\\d+)(.+)")) {
			Pattern p = Pattern.compile("(\\d+)(.+)");
			Matcher m = p.matcher(content);
			m.matches();
			ticketPrice.setPrice(m.group(1));
			ticketPrice.setRemark(m.group(2).replaceAll("）|（|\\(|\\)", ""));
		}else if (content.matches("(\\d+)\\(\\)")){
			Pattern p = Pattern.compile("(\\d+)(.+)");
			Matcher m = p.matcher(content);
			m.matches();
			ticketPrice.setPrice(m.group(1));
		}
	}
	public int getSortType(String sort) {
		int sortType = ShowType.OTHER;
		if (sort.contains("演唱")) {
			sortType = ShowType.CONCERT;
		} else if (sort.contains("音乐") && !sort.contains("剧")) {
			sortType = ShowType.SHOW;
		} else if (sort.contains("话剧") || sort.contains("歌剧")
				|| sort.contains("音乐剧")) {
			sortType = ShowType.DRAMA;
		} else if (sort.contains("芭蕾") || sort.contains("舞蹈")
				|| sort.contains("舞")) {
			sortType = ShowType.DANCE;
		} else if (sort.contains("戏剧") || sort.contains("戏曲")
				|| sort.contains("二人转") || sort.contains("魔术")
				|| sort.contains("杂技") || sort.contains("相声")
				|| sort.contains("小品") || sort.contains("马戏")) {
			sortType = ShowType.OPERA;
		} else if (sort.contains("儿童亲子")) {
			sortType = ShowType.CHILDREN;
		} else if (sort.contains("体育")) {
			sortType = ShowType.SPORTS;
		} else if (sort.contains("旅游") || sort.contains("电影")) {
			sortType = ShowType.HOLIDAY;
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
		// TODO Auto-generated method stub
		YongleSpider spider=new YongleSpider();
		//spider.extract();
		spider.processLastPage("http://www.228.com.cn/ticket-2498404.html", 1);
	}

}
