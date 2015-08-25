package com.piaoyou.crawler.spider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bst.flex.Area;
import com.bst.flex.PriceLevel;
import com.bst.flex.SeatIcon;
import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.PubFun;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.ActionContext;
import flex.messaging.io.amf.ActionMessage;
import flex.messaging.io.amf.AmfMessageDeserializer;
import flex.messaging.io.amf.MessageBody;
import flex.messaging.messages.AcknowledgeMessage;

/**
 * 国家大剧院
 * @author Administrator
 *
 */
public class ChncpaSpider extends TicketSpider implements SpiderTask {
	
	private static final Log log = LogFactory.getLog(ChncpaSpider.class);
	
	private static String HOME_PAGE = "http://www.chncpa.org/module/search/inperformance.jsp?sid=21";
	private final static String requestAMF = "http://ticket.chncpa.org/seatManagerNcpaWS/messagebroker/amf";
	private final static String BASE_URL = "http://www.chncpa.org/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
							.get(0).get("agency_id").toString();

	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy年M月d日 HH:mm");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static String currentYear = new SimpleDateFormat("yyyy-M-d").format(new Date()).substring(0, 4);
	
	static final String cookie = "djyhistory=null^^,|; coBUDrHx6D=MDAwM2IyNzNhNDQwMDAwMDAwMDQwVT08UgMxMzEyNTc2OTEw; JSESSIONID=nnv2T8hYdqLJyBYJvyhqfk2jLmt1rjJFCTnLyk2kpqTkmlMwvpQl!-2099153256";
	private final static Map<String,Integer> typeCor = new HashMap<String,Integer>();
	
	static{
		initTypeCor();
	}
	
	/**
	 * 初始化演出类别对应
	 */
	private static void initTypeCor(){
		typeCor.put("国家大剧院音乐厅",ShowType.SHOW);//音乐会
		typeCor.put("国家大剧院歌剧院",ShowType.DRAMA);//话剧歌剧
		typeCor.put("国家大剧院戏剧场",ShowType.OPERA);//曲苑杂坛
		typeCor.put("国家大剧院小剧场",ShowType.OTHER);//其他
	}
	
	@Override
	public void extract() {
		String now = new SimpleDateFormat("yyyy-M-d").format(new Date());
		HOME_PAGE = HOME_PAGE + "&startTime="+ now + "&endTime=" +currentYear+"-12-31";
		Document doc = getDoc(HOME_PAGE+"&Page=1");
		int count = Integer.valueOf(PubFun.getRegex("var maxpage = (\\d+);", doc.toString(), 1));
		for(Element page:doc.select("td.f12b_grey6 a")){
			try {
				String info = page.parent().parent().nextElementSibling().nextElementSibling().html();
				if(info.contains("else if(\"5\" == \"0\")")){//已出票
					extractEach(page.attr("abs:href"));
				}
			} catch (Exception e) {
				log.error(page.attr("abs:href"),e);
			}
		}
		for(int i=1;i<=count;i++){
			doc = getDoc(HOME_PAGE+"&Page="+i);
			for(Element page:doc.select("td.f12b_grey6 a")){
				try {
					String info = page.parent().parent().nextElementSibling().nextElementSibling().html();
					if(info.contains("else if(\"5\" == \"0\")")){//已出票
						extractEach(page.attr("abs:href"));
					}
				} catch (Exception e) {
					log.error(page.attr("abs:href"),e);
				}
			}
		}
	}
	
	
	public static void main(String[] args) throws ParseException, SQLException, IOException {
		ChncpaSpider spider = new ChncpaSpider();
		spider.setDao(new DataDao());
		spider.extract();
	}

	/**
	 * 通过目标url取得演出需要的元素
	 * @param url
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private void extractEach(String url) throws SQLException, IOException, ParseException {
		Document ticket = getDoc(url);
		Show show = new Show();
		show.setAgent_id(agentID);
		show.setName(ticket.select("td.f14b_black").text());//演出标题
		show.setFirstAgent(true);//是否总代
		show.setOnlineSeat(true);//支持在线选座
		show.setSiteName("国家大剧院"+ticket.select("td.linkf12_blue1").first().text());//演出场馆
		show.setType(typeCor.get(show.getSiteName()));//演出类型
		if(show.getType() == ShowType.OTHER){
			if(show.getName().contains("音乐")){
				show.setType(ShowType.SHOW);
			}else if(show.getName().contains("歌剧")){
				show.setType(ShowType.DRAMA);
			}else if(show.getName().contains("话剧")){
				show.setType(ShowType.DRAMA);
			}else{
				show.setType(ShowType.OPERA);
			}
		}
		show.setStatus(1);
		Elements introduction = ticket.select("#ycjs");//演出简介
		for(Element clear:introduction.select("script,noscript,embed,object")){
			clear.remove();
		}
		show.setIntroduction(PubFun.cleanElement(introduction).html());
		show.setImage_path(ticket.select("td.pad_left11 img").first().attr("abs:src"));//演出图片
		String timeURL = ticket.select("#riDiv1 iframe").attr("src");
		Document timeDoc = getDoc(timeURL);
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		for (Element timeEle:timeDoc.select("td:has(a)")){
			String time = newDate.format(oldDate.parse(currentYear+"年"+timeEle.text()));
			List<TicketPrice> priceList = getTicketPrice(timeEle.select("a").first().attr("abs:href"));
			timeAndPrice.put(time, priceList);
		}
		
		show.setTimeAndPrice(timeAndPrice);
		getDao().saveShow(show);
	}
	
	/**
	 * 通过在线选座页面返回票价信息,是否有票等
	 * @param url	在线选座url
	 * @return
	 */
	private List<TicketPrice> getTicketPrice(String url) {
		String query = url.substring(url.indexOf("?")+1);
		
		Map<String,String> queryParam = new HashMap<String,String>();
		for(String param:query.split("&")){
			if(param.contains("=")){
				queryParam.put(param.split("=")[0], param.split("=")[1]);
			}
		}
		
		String unit = queryParam.get("unit");
		String venue = queryParam.get("venue");
		if("XJC".equals(venue)){
			venue = "SJC";
		}else if("SJC".equals(venue)){
			venue = "XJC";
		}
		String zone = queryParam.get("zone");
		
		List<TicketPrice> result = new ArrayList<TicketPrice>();
		List<PriceLevel> priceLevelList = getPrice(unit);
		List<Area> areaList = getArea(unit, venue);
		Set<String> priceCount = new HashSet<String>();
		
		for(int i=0;priceCount.size()!=priceLevelList.size() && i<areaList.size();i++){
			countPriceSellAble(unit, venue, zone, areaList.get(i).getAreaId(), priceCount);
		}
		
		for(PriceLevel priceLevel:priceLevelList){
			TicketPrice price = new TicketPrice();
			price.setMainURL(url);
			price.setPrice(priceLevel.getPlPrice());
			price.setExist(priceCount.contains(priceLevel.getPlPrice()));
			result.add(price);
		}
		
		return result;
	}
	
	/**
	 * 根据unit标识,取得该演出的票价列表(每个unit对应一场演出)
	 * @param unit
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<PriceLevel> getPrice(String unit){
		byte[] priceRequestArr = {0, 3, 0, 0, 0, 1, 0, 4, 110, 117, 108, 108, 0, 2, 47, 50, 0, 0, 1, 30, 10, 0, 0, 0, 1, 17, 10, -127, 19, 79, 102, 108, 101, 120, 46, 109, 101, 115, 115, 97, 103, 105, 110, 103, 46, 109, 101, 115, 115, 97, 103, 101, 115, 46, 82, 101, 109, 111, 116, 105, 110, 103, 77, 101, 115, 115, 97, 103, 101, 13, 115, 111, 117, 114, 99, 101, 19, 111, 112, 101, 114, 97, 116, 105, 111, 110, 15, 104, 101, 97, 100, 101, 114, 115, 21, 116, 105, 109, 101, 84, 111, 76, 105, 118, 101, 9, 98, 111, 100, 121, 17, 99, 108, 105, 101, 110, 116, 73, 100, 19, 116, 105, 109, 101, 115, 116, 97, 109, 112, 23, 100, 101, 115, 116, 105, 110, 97, 116, 105, 111, 110, 19, 109, 101, 115, 115, 97, 103, 101, 73, 100, 1, 6, 45, 103, 101, 116, 65, 108, 108, 80, 114, 105, 99, 101, 76, 101, 118, 101, 108, 66, 121, 85, 110, 105, 116, 10, 11, 1, 9, 68, 83, 73, 100, 6, 73, 49, 52, 65, 52, 67, 67, 55, 66, 45, 66, 50, 49, 70, 45, 55, 57, 54, 52, 45, 51, 66, 57, 67, 45, 49, 57, 66, 66, 68, 50, 49, 67, 48, 48, 70, 50, 21, 68, 83, 69, 110, 100, 112, 111, 105, 110, 116, 1, 1, 4, 0, 9, 3, 1, 6, 17, 48, 48, 48, 48, 48, 55, 87, 78, 1, 4, 0, 6, 27, 115, 101, 97, 116, 77, 97, 110, 97, 103, 101, 114, 87, 83, 6, 73, 57, 49, 52, 70, 66, 65, 68, 57, 45, 52, 49, 65, 52, 45, 53, 52, 57, 67, 45, 69, 69, 57, 48, 45, 57, 56, 51, 53, 69, 70, 69, 57, 52, 53, 70, 51};
		int price_base = 242;
		for(char ch:unit.toCharArray()){
			priceRequestArr[price_base++] = (byte)ch;
		}
		
		AcknowledgeMessage o = getFlexObject(priceRequestArr);
		return (List<PriceLevel>) o.getBody();
	}
	
	/**
	 * 通过unit标识和venue得到根据位置划分的区域
	 * @param unit
	 * @param venue	场馆类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<Area> getArea(String unit,String venue){
		byte[] areaRequestArr = {0, 3, 0, 0, 0, 1, 0, 4, 110, 117, 108, 108, 0, 2, 47, 50, 0, 0, 1, 33, 10, 0, 0, 0, 1, 17, 10, -127, 19, 79, 102, 108, 101, 120, 46, 109, 101, 115, 115, 97, 103, 105, 110, 103, 46, 109, 101, 115, 115, 97, 103, 101, 115, 46, 82, 101, 109, 111, 116, 105, 110, 103, 77, 101, 115, 115, 97, 103, 101, 13, 115, 111, 117, 114, 99, 101, 19, 111, 112, 101, 114, 97, 116, 105, 111, 110, 21, 116, 105, 109, 101, 84, 111, 76, 105, 118, 101, 9, 98, 111, 100, 121, 17, 99, 108, 105, 101, 110, 116, 73, 100, 15, 104, 101, 97, 100, 101, 114, 115, 23, 100, 101, 115, 116, 105, 110, 97, 116, 105, 111, 110, 19, 116, 105, 109, 101, 115, 116, 97, 109, 112, 19, 109, 101, 115, 115, 97, 103, 101, 73, 100, 1, 6, 41, 103, 101, 116, 65, 114, 101, 97, 115, 66, 121, 85, 110, 105, 116, 82, 101, 103, 105, 111, 110, 4, 0, 9, 5, 1, 6, 17, 48, 48, 48, 48, 48, 55, 82, 77, 6, 7, 89, 89, 84, 1, 10, 11, 1, 9, 68, 83, 73, 100, 6, 73, 49, 54, 56, 51, 65, 50, 70, 52, 45, 70, 65, 48, 69, 45, 52, 68, 52, 57, 45, 70, 67, 54, 56, 45, 66, 53, 53, 50, 56, 55, 70, 50, 50, 66, 54, 50, 21, 68, 83, 69, 110, 100, 112, 111, 105, 110, 116, 1, 1, 6, 27, 115, 101, 97, 116, 77, 97, 110, 97, 103, 101, 114, 87, 83, 4, 0, 6, 73, 50, 50, 54, 53, 68, 52, 66, 70, 45, 68, 48, 69, 48, 45, 51, 49, 67, 52, 45, 52, 57, 48, 65, 45, 57, 56, 70, 65, 48, 70, 56, 68, 54, 55, 56, 70};
		int area_base = 181;
		for(char ch:unit.toCharArray()){
			areaRequestArr[area_base++] = (byte)ch;
		}
		area_base += 2;
		for(char ch:venue.toCharArray()){
			areaRequestArr[area_base++] = (byte)ch;
		}
		
		AcknowledgeMessage o = getFlexObject(areaRequestArr);
		return (List<Area>) o.getBody();
	}
	
	/**
	 * 根据每一个区域,遍历该区域下的所有座位.统计有票的档
	 * @param unit
	 * @param venue		场馆类型
	 * @param zone		
	 * @param areaID	区域id
	 * @param priceCount	统计是否有票
	 */
	@SuppressWarnings("unchecked")
	private void countPriceSellAble(String unit,String venue,String zone,String areaID,Set<String> priceCount){
		byte[] countPriceRequestArr = {0, 3, 0, 0, 0, 1, 0, 4, 110, 117, 108, 108, 0, 2, 47, 50, 0, 0, 1, 99, 10, 0, 0, 0, 1, 17, 10, -127, 19, 79, 102, 108, 101, 120, 46, 109, 101, 115, 115, 97, 103, 105, 110, 103, 46, 109, 101, 115, 115, 97, 103, 101, 115, 46, 82, 101, 109, 111, 116, 105, 110, 103, 77, 101, 115, 115, 97, 103, 101, 13, 115, 111, 117, 114, 99, 101, 19, 111, 112, 101, 114, 97, 116, 105, 111, 110, 21, 116, 105, 109, 101, 84, 111, 76, 105, 118, 101, 9, 98, 111, 100, 121, 17, 99, 108, 105, 101, 110, 116, 73, 100, 15, 104, 101, 97, 100, 101, 114, 115, 23, 100, 101, 115, 116, 105, 110, 97, 116, 105, 111, 110, 19, 116, 105, 109, 101, 115, 116, 97, 109, 112, 19, 109, 101, 115, 115, 97, 103, 101, 73, 100, 1, 6, 45, 103, 101, 116, 83, 101, 97, 116, 73, 99, 111, 110, 115, 66, 121, 85, 110, 105, 116, 65, 114, 101, 97, 4, 0, 9, 9, 1, 6, 17, 48, 48, 48, 48, 48, 55, 82, 77, 6, 5, 50, 50, 6, 17, 48, 48, 48, 48, 48, 55, 83, 72, 6, 7, 89, 89, 84, 1, 10, 11, 1, 53, 68, 83, 82, 101, 109, 111, 116, 101, 67, 114, 101, 100, 101, 110, 116, 105, 97, 108, 115, 67, 104, 97, 114, 115, 101, 116, 1, 9, 68, 83, 73, 100, 6, 73, 49, 54, 56, 51, 65, 50, 70, 52, 45, 70, 65, 48, 69, 45, 52, 68, 52, 57, 45, 70, 67, 54, 56, 45, 66, 53, 53, 50, 56, 55, 70, 50, 50, 66, 54, 50, 39, 68, 83, 82, 101, 109, 111, 116, 101, 67, 114, 101, 100, 101, 110, 116, 105, 97, 108, 115, 6, 1, 21, 68, 83, 69, 110, 100, 112, 111, 105, 110, 116, 1, 1, 6, 27, 115, 101, 97, 116, 77, 97, 110, 97, 103, 101, 114, 87, 83, 4, 0, 6, 73, 69, 70, 48, 50, 66, 70, 49, 48, 45, 54, 53, 57, 57, 45, 50, 48, 52, 49, 45, 49, 53, 53, 56, 45, 57, 57, 50, 56, 66, 66, 53, 69, 50, 55, 66, 69};
		int area_base = 183;
		for(char ch:unit.toCharArray()){
			countPriceRequestArr[area_base++] = (byte)ch;
		}
		area_base += 2;
		for(char ch:areaID.toCharArray()){
			countPriceRequestArr[area_base++] = (byte)ch;
		}
		area_base += 2;
		for(char ch:zone.toCharArray()){
			countPriceRequestArr[area_base++] = (byte)ch;
		}
		area_base += 2;
		for(char ch:venue.toCharArray()){
			countPriceRequestArr[area_base++] = (byte)ch;
		}
		
		AcknowledgeMessage o = getFlexObject(countPriceRequestArr);

		List<SeatIcon> seat = (List<SeatIcon>) o.getBody();
		for (SeatIcon s : seat) {
			if("1.0".equals(s.getTicketState())){
				priceCount.add(s.getPrice());
			}
		}
	}

	/**
	 * 模拟发送amf请求
	 * @param requestBody
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private AcknowledgeMessage getFlexObject(byte[] requestBody){
		AcknowledgeMessage o = null;
		for(int i=0;i<5;i++){
			try {
				URL url = new URL(requestAMF);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setConnectTimeout(timeout);
				conn.setReadTimeout(timeout);
				conn.setRequestMethod("POST");
				conn.setDoOutput(true);
				conn.setRequestProperty("Referer", "http://ticket.chncpa.org/selectseatzone/selectSeat.swf/[[DYNAMIC]]/4");
				conn.setRequestProperty("Host", "ticket.chncpa.org");
				conn.setRequestProperty("Content-Length", String.valueOf(requestBody.length));
				conn.setRequestProperty("Cookie", cookie);
				conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; EmbeddedWB 14.52 from: http://www.bsalsa.com/ EmbeddedWB 14.52; .NET CLR 2.0.50727; .NET CLR 3.0.4506.2152; .NET CLR 3.5.30729)");
				
				OutputStream os = conn.getOutputStream();
				os.write(requestBody);
				os.flush();
				os.close();
				
				InputStream in = conn.getInputStream();
				
				ActionMessage am = new ActionMessage();
				AmfMessageDeserializer amd = new AmfMessageDeserializer();
				amd.initialize(new SerializationContext(), in, null);
				ActionContext ac = new ActionContext();
				amd.readMessage(am, ac);
				List<MessageBody> l = am.getBodies();
				o = (AcknowledgeMessage) l.get(0).getData();
			} catch (Exception e) {
				//忽略
			}
			if(o != null && o.getBody() != null){
				break;
			}
		}
		return o;
	}

}
