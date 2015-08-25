package com.piaoyou.crawler.spider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class HuaXiaPiaoWuZhongXinSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(HuaXiaPiaoWuZhongXinSpider.class);
	private final static String BASE_URL = "http://www.huaxiapiao.com";
	private final static String home_URL = "http://www.huaxiapiao.com/all_ticket.asp";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
		.get(0).get("agency_id").toString();
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	@Override
	public void extract() {
		Document doc=getDoc(home_URL);
		Iterator<Element> itList=doc.select("div:has(img[src$=icon_note.gif])").iterator();
		while(itList.hasNext()){
			Element eleIt=itList.next().parent();
			String sortName=eleIt.nextElementSibling().select(".STYLE4").first().ownText().trim().replace(".", "").replace("全部票品", "");
			int sortType=getSortType(sortName);
			List<Element> itMyList =eleIt.parent().nextElementSibling().select("table tr:gt(0)");
			for(int i=0;i<itMyList.size()-1;i++){
				Element eleSure=eleIt.parent().nextElementSibling().select("table tr:eq(1)").first();
				if(eleSure.text().contains("暂无")){
					break;
				}
				Element eleTr=itMyList.get(i);
				String href=eleTr.select("td>a").first().absUrl("href").trim();
				String showName=eleTr.select("td>a").first().text().trim();
				String site=eleTr.select("td:eq(2)").first().text().trim();
				try{
					processlastPage(href, sortType,site,showName);
					Random rand=new Random();
					Thread.sleep(rand.nextInt(1000));
				}catch(Exception ee){
					log.error(Const.concat(href, sortType,site,showName), ee);
				}
			}
		}
	}
	
	public void processlastPage(String url,int sortType,String site,String showName){
		Document doc=getDoc(url);
		String pic=doc.select("table[width=266] td[width=240]>img").first().absUrl("src").trim();
		String info=PubFun.cleanElement(doc.select("td.bzt p.guest.STYLE4").first()).html();
		Element temp=doc.select("td[bgcolor=#F4F3F1]:contains(演出场馆)").first().parent().parent();
		Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
		Iterator<Element> trE=temp.select("tr:gt(0)").iterator();
		while(trE.hasNext()){
			Element te=trE.next();
			Element tempt=te.select("td:eq(1)").first();
			if(tempt==null){
				continue;
			}
			String showTime=tempt.text().trim();
			showTime=showTime.replace("：", ":").replace("：", ":").replace(" ", " ");;
			try {
				showTime = newDate.format(oldDate.parse(showTime));
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			List<TicketPrice> list=new ArrayList<TicketPrice>();
			Element tempPrice=te.select("td:eq(2)").first();
			Iterator<Element> hasPriceList=tempPrice.select("a").iterator();
			while(hasPriceList.hasNext()){
				Element elePrice=hasPriceList.next();
				String price=elePrice.text().trim();
				String priceUrl=elePrice.absUrl("href").trim();
				TicketPrice ticket=new TicketPrice();
				ticket.setExist(true);
				ticket.setMainURL(url);
				ticket.setDetailURL(priceUrl);
				this.setPriceAndRemark(ticket, price);
				list.add(ticket);
			}
			Iterator<Element> noPriceList=tempPrice.select("font").iterator();
			while(noPriceList.hasNext()){
				Element elePrice=noPriceList.next();
				String price=elePrice.text().trim();
				TicketPrice ticket=new TicketPrice();
				ticket.setExist(false);
				ticket.setMainURL(url);
				ticket.setDetailURL(url);
				this.setPriceAndRemark(ticket, price);
				list.add(ticket);
			}
			map.put(showTime,list);
		}
		Show show=new Show();
		show.setAgent_id(agentID);
		show.setIntroduction(info);
		show.setSiteName(site);
		show.setName(showName);
		show.setImage_path(pic);
		show.setType(sortType);
		show.setWatermark(true);
		show.setTimeAndPrice(map);
		try{
			getDao().saveShow(show);
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HuaXiaPiaoWuZhongXinSpider spider=new HuaXiaPiaoWuZhongXinSpider();
		spider.setDao(new DataDao());
		spider.extract();
		//spider.processlastPage("http://www.huaxiapiao.com/ticket.asp?/=2148", 1, "asfaf", "asfdafa");
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
		} else if (content.matches("(\\d+)(\\D+)")) {
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
		}
	}

}
