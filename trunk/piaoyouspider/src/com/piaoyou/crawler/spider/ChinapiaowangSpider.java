package com.piaoyou.crawler.spider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

@SuppressWarnings("unused")
public class ChinapiaowangSpider extends TicketSpider implements SpiderTask {
	private static final String BASE_URL = "http://www.chinapiaowang.com/";
	private static final String URL ="http://www.chinapiaowang.com/act_list.php";
	private static final Class<ChinapiaowangSpider> BASE_CLASS=ChinapiaowangSpider.class;
	private static final SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static String agentId = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();

	@Override
	public void extract() {
		Set<String> set = new HashSet<String>();
		set = getURL();
		for (Iterator<String> iterator = set.iterator(); iterator	.hasNext();) {
			String url = (String) iterator.next();
			try {
				parseEach(url);
			} catch (Exception e) {
				LogFactory.getLog(BASE_CLASS).error(Const.concat(url),e);
			}
		}
		/*try {
			parseEach("");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}
	public static void main(String[] args) {
		ChinapiaowangSpider chinapiao = new ChinapiaowangSpider();
		chinapiao.setDao(new DataDao());
		chinapiao.extract();
	}

	public Set<String> getURL() {
		Set<String> set = new HashSet<String>();
		Document document = getDoc(URL);
		int count = Integer.parseInt(document.select("a.page:contains(尾页)").attr("href").replaceAll("\\D", ""));
		for (int i = 1; i <= count; i++) {
			String tmpUrl = "http://www.chinapiaowang.com/act_list.php?page="+i;
			Document doc = getDoc(tmpUrl);
			for (Element ele : doc.select("#border1 tr")) {
				// 如果场馆待定，不保存相关链接
				if (!"".equals(ele.select("td").get(1).text())) {
					set.add(ele.select("td").get(0).select("a").first().attr(	"abs:href"));
				}
			}
		}
		return set;
	}
	public void parseEach(String url) throws Exception{
			Show show = new Show();
			Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>() ;
			List<TicketPrice> list = null ;
			TicketPrice ticket = null ;
			Document document = getDoc(url);
			show.setAgent_id(agentId);
			show.setName(document.select("p.act").get(0).text());
			String showType=document.select("#ticket_title").text();
			show.setType(getShowType(showType));
			show.setSiteName(document.select("p.act").get(3).select("a").text());
			//售票状态
			String status = document.select("p.act:contains(状态)").select("span").text();
			if("售票中".equals(status)){
				show.setStatus(1);
			}else if("预售".equals(status)){
				show.setStatus(0);
			}
			show.setImage_path(document.select("img[style=padding:4px; margin:0 20px; border:#dcdcdc 1px solid; background-color:#ececec; width:180px; height:228px;]").first().attr("abs:src"));
			show.setIntroduction(PubFun.cleanElement(document.select("#act_detail_content")).text());
			for(Element element :document.select("table").get(2).select("tr:has(td)")){
				list = new ArrayList<TicketPrice>();
				String showTime = element.select("td").get(1).text();
				try {
					showTime = newDate.format(newDate.parse(showTime));
				} catch (Exception e) {
				}
				for(Element ele :element.select("td").get(2).select("span")){
					for(Element elem:ele.select("span:not(:has(a))")){
						ticket = new TicketPrice();
						ticket.setMainURL(url);
						ticket.setExist(false);
						String price = ele.text();
						 String remark = price.replaceAll("\\d+$", "");
						 String pri = price.replace(remark, "");
						ticket.setPrice(pri);
						ticket.setRemark(remark);
						list.add(ticket);
					}
					for(Element elem:ele.select("span:has(a)")){
						ticket = new TicketPrice();
						ticket.setMainURL(url);
						ticket.setDetailURL(elem.select("a").first().attr("abs:href"));
						ticket.setExist(true);
						String price = ele.text();
						 String remark = price.replaceAll("\\d+$", "");
						 String pri = price.replace(remark, "");
						ticket.setPrice(pri);
						ticket.setRemark(remark);
						list.add(ticket);
					}
				}
				timeAndPrice.put(showTime, list);
			}
			show.setTimeAndPrice(timeAndPrice);
			getDao().saveShow(show);
	}
	private int getShowType(String type){
		int showType = 0;
		if("演唱会".equals(type)){
			 showType = ShowType.CONCERT;
		}else if("音乐会".equals(type)){
			showType = ShowType.SHOW;
		}else if("话剧歌剧".equals(type)){
			showType = ShowType.DRAMA;
		}else if("舞蹈芭蕾".equals(type)){
			showType = ShowType.DANCE;
		}else if("戏曲综艺".equals(type)||"魔术马戏".equals(type)){
			showType = ShowType.OPERA;
		}else if("亲子儿童".equals(type)){
			showType = ShowType.CHILDREN;
		}else if("体育赛事".equals(type)){
			showType = ShowType.SPORTS;
		}else if("时尚休闲".equals(type)){
			showType = ShowType.HOLIDAY;
		}else{
			showType = ShowType.OTHER;
		}
		return showType;
	}

}
