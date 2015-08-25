package com.piaoyou.crawler.spider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
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

/**
 * 51piao
 * VIP会员返款2%
 * @author Administrator
 *
 */
public class Piao51Spider extends TicketSpider implements SpiderTask {

	private static final Log log = LogFactory.getLog(Piao51Spider.class);
	
	private final static String BASE_URL = "http://www.51piao.com/";
	private final static String HOME_PAGE = "http://www.51piao.com/Ticket/TicketListAll.aspx";
	private final static String SHANGHAI = "http://www.51piao.com/Ticket/Default.aspx?CityId=157";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
				.get(0).get("agency_id").toString();
	private final static Map<String,Integer> typeCor = new HashMap<String,Integer>();

	
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	static{
		initTypeCor();
	}
	
	/**
	 * 初始化演出类别对应
	 */
	private static void initTypeCor(){
		typeCor.put("演唱会",ShowType.CONCERT);//演唱会
		typeCor.put("音乐会",ShowType.SHOW);//音乐会
		typeCor.put("话剧歌剧",ShowType.DRAMA);//话剧歌剧
		typeCor.put("舞蹈芭蕾",ShowType.DANCE);//舞蹈芭蕾
		typeCor.put("杂技魔术",ShowType.OPERA);//曲苑杂坛
		typeCor.put("戏剧综艺",ShowType.OPERA);//曲苑杂坛
		typeCor.put("马戏会展",ShowType.OPERA);//曲苑杂坛
		typeCor.put("儿童亲子",ShowType.CHILDREN);//儿童亲子
		typeCor.put("电影休闲",ShowType.HOLIDAY);//其他
		typeCor.put("礼品券",ShowType.HOLIDAY);//其他
	}
	
	@Override
	public void extract() {
		String session = getSessionID();
		if(session == null){
			throw new RuntimeException("首页获取session失败");
		}
		Document bj = getHomePage(HOME_PAGE,session,HOME_PAGE);
		Elements all = bj.select("#Bottom1_LabQuickSearch table");
		
		for(int i=1;i<6;i=i+2){//先处理国家大剧院的
			for(Element every:all.get(i).select("tr:gt(0)")){
				String site_name = every.select("td:eq(2)").text();
				String url = every.select("td:eq(0) a").first().attr("abs:href");
				try {
					extractEach(url,site_name.contains("音乐")?ShowType.SHOW:ShowType.DRAMA);
				} catch (Exception e) {
					log.error(Const.concat(url,site_name.contains("音乐")?ShowType.SHOW:ShowType.DRAMA),e);
				}
			}
		}
		
		for(int i=6;i<all.size();i++){//处理非国家大剧院的
			String title = all.get(i).select("tr:eq(1)").text().replace(">> ", "").replace("打印", "");
			i++;
			for(Element every:all.get(i).select("tr:gt(0)")){
				String url = every.select("td:eq(0) a").first().attr("abs:href");
				try {
					extractEach(url,typeCor.get(title.trim()));
				} catch (Exception e) {
					log.error(Const.concat(url,typeCor.get(title.trim())),e);
				}
			}
		}
		
		getHomePage(SHANGHAI, session, SHANGHAI);
		Document sh = getHomePage(HOME_PAGE, session, SHANGHAI);
		
		all = sh.select("#Bottom1_LabQuickSearch table");
		
		for(Element every:all.get(1).select("tr:gt(0)")){//处理上海大剧院的
			Element title = every.select("td:eq(0) a").first();
			String url = title.attr("abs:href");
			int type = ShowType.OTHER;
			if(title.text().contains("音乐")){
				type = ShowType.SHOW;
			}else if(title.text().contains("剧")){
				type = ShowType.DRAMA;
			}else if(title.text().contains("舞")){
				type = ShowType.DANCE;
			}
			try {
				extractEach(url,type);
			} catch (Exception e) {
				log.error(Const.concat(url,type), e);
			}
		}
		
		for(int i=2;i<all.size();i++){//处理非上海大剧院的
			String title = all.get(i).select("tr:eq(1)").text().replace(">> ", "").replace("打印", "");
			i++;
			for(Element every:all.get(i).select("tr:gt(0)")){
				String url = every.select("td:eq(0) a").first().attr("abs:href");
				try {
					extractEach(url,typeCor.get(title.trim()));
				} catch (Exception e) {
					log.error(Const.concat(url,typeCor.get(title.trim())), e);
				}
			}
		}
	}

	/**
	 * 提取每一场演出的票价
	 * @param url		演出url
	 * @param type		演出类型
	 */
	private void extractEach(String url, int type) {
		Show show = new Show();
		try {
			show.setAgent_id(agentID);
			Document ticket = getDoc(url);
			show.setImage_path(ticket.select("#ImgMain>img").first().attr("abs:src"));
			show.setName(ticket.select("#NAME").text());//演出标题
			show.setSiteName(ticket.select("#ADDRINFO").text());//演出场馆
			if(show.getSiteName().startsWith("国家大剧院") && show.getName().contains("音乐")){
				type = ShowType.SHOW;
			}
			show.setType(type);//演出类型
			Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String, List<TicketPrice>>();
			show.setTimeAndPrice(timeAndPrice);
			for(Element each:ticket.select("#LabTicketUi tr")){
				String time = each.select("td:eq(0)").text().replaceAll(" 星期.", "")
						.replaceAll("：|\\.", ":").replaceAll(";|；", ":");
				if(time.length()>0){
					try {
						time = newDate.format(oldDate.parse(time));
					} catch (Exception e1) {
						//TODO	时间暂时不处理
					}
					List<TicketPrice> priceList = new ArrayList<TicketPrice>();
					timeAndPrice.put(time, priceList);//时间与票价
					Elements e = each.select("td.PriceTd4 :not(font.PriceSplit)");
					for(int i=0;i<e.size();i++){
						TicketPrice price = new TicketPrice();
						price.setMainURL(url);
						if("a".equals(e.get(i).nodeName())){
							price.setExist(true);
							price.setDetailURL(e.get(i).attr("abs:href"));
						}
						price.setPrice(e.get(i).text());
						if(i+1<e.size() && "PriceTicketType".equals(e.get(i+1).className())){
							price.setRemark(e.get(++i).text().replaceAll("[()（）]", ""));
						}
						priceList.add(price);
					}
				}
			}
			getDao().saveShow(show);
		} catch (Exception e) {
			log.error(Const.concat(url,type),e);
		}
	}

	/**
	 * 
	 * @param url
	 * @param session
	 * @param reffer
	 * @return
	 */
	private Document getHomePage(String url,String session,String reffer) {
		Document doc = null;
		for(int i=0;i<tryTimes;i++){
			try {
				doc = Jsoup.connect(url).referrer(reffer).userAgent(userAgent)
					.timeout(timeout).cookie("ASP.NET_SessionId", session).get();
			} catch (IOException e) {
				//ignore
			}
			if(doc != null){
				break;
			}
		}
		if(doc == null){
			LogFactory.getLog(Piao51Spider.class).error("error:首页读取失败!");
		}
		return doc;
	}
	
	/**
	 * 取得本次连接的sessionID
	 * @return
	 */
	private String getSessionID() {
		Response response = null;
		for(int i=0;i<tryTimes;i++){
			try {
				response = Jsoup.connect(HOME_PAGE).userAgent(userAgent)
							.timeout(timeout).execute();
			} catch (IOException e) {
				//ignore
			}
			if(response != null){
				break;
			}
		}
		return response.cookie("ASP.NET_SessionId");
	}

	public static void main(String[] args) throws IOException {
		Piao51Spider spider = new Piao51Spider();
		spider.setDao(new DataDao());
//		spider.extract();
		spider.extractEach("http://www.51piao.com/Ticket/TicketDetail.aspx?id=12394", 1);
	}

}
