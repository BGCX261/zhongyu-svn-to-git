package com.piaoyou.crawler.spider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

public class JingWenPiaoWu extends TicketSpider implements SpiderTask {
	private static final String BASE_URL ="http://www.bjjwpw.com/allpiao.asp";
    private static Log log=LogFactory.getLog(JingWenPiaoWu.class);
    private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();
	@Override
	public void extract() {
		Document doc=getDoc("http://www.bjjwpw.com/allpiao.asp");
		Iterator<Element> tableEleList=doc.select("body>table[width=960]").subList(9, 20).iterator();
		while(tableEleList.hasNext()){
			Element tableEle=tableEleList.next();
			String sort=tableEle.select("td[width=3%] td").first().text().trim();
			int sortType=ShowType.OTHER;
			if(sort.contains("国家大剧院")){
				if(sort.contains("歌剧院")){
					Iterator<Element> trEleList=tableEle.select("td[valign=top] tr:gt(0)").iterator();
					while(trEleList.hasNext()){
						Element trEle=trEleList.next();
						String name=trEle.select("td>a").first().text().trim();
						if(name.contains("音乐会")){
		        			sortType=ShowType.SHOW;
		        		}
		        		if(name.contains("芭蕾")||name.contains("舞")){
		        			sortType=ShowType.DANCE;
		        		}
		        		if(name.contains("京剧")){
		        			sortType=ShowType.OPERA;
		        		}
		        		else if(name.contains("演唱会")){
		        			sortType=ShowType.CONCERT;
		        		}
		        		else if(name.contains("歌剧")||name.contains("话剧")){
		        			sortType=ShowType.DRAMA;
		        		}
						String href=trEle.select("td>a").first().absUrl("href").trim();
						String time=trEle.select("td:eq(2) td").text().trim();
						time=time.replace(" ", "").replace("　", "");
						if(time.matches("[\\d]+-[\\d]+-[\\d]+")){
							int compare=PubFun.compareDate(PubFun.getCurrentYear()+"-"+PubFun.getCurrentMonth()+"-"+PubFun.getCurrentDay(),time);
							if(compare>0)
								continue;
						}
		                if(time.matches("[\\d]+-[\\d]+-[\\d]+至[\\d]+-[\\d]+-[\\d]+")){
		                	String endTime=PubFun.getRegex("[\\d]+-[\\d]+-[\\d]+至([\\d]+-[\\d]+-[\\d]+)", time,1);
		                	int compare=PubFun.compareDate(PubFun.getCurrentYear()+"-"+PubFun.getCurrentMonth()+"-"+PubFun.getCurrentDay(), endTime);
		                	if(compare>0)
								continue;
						}
		                try{
		                   processLastPage(href,sortType);
		                }catch(Exception ee){
		                	log.error(Const.concat(href,sortType),ee);
		                }
					}
					continue;
				}else if(sort.contains("音乐厅")){
					Iterator<Element> trEleList=tableEle.select("td[valign=top] tr:gt(0)").iterator();
					while(trEleList.hasNext()){
						Element trEle=trEleList.next();
						String name=trEle.select("td>a").first().text().trim();
						if(name.contains("音乐会")){
		        			sortType=ShowType.SHOW;
		        		}
		        		if(name.contains("芭蕾")||name.contains("舞")){
		        			sortType=ShowType.DANCE;
		        		}
		        		if(name.contains("京剧")){
		        			sortType=ShowType.OPERA;
		        		}
		        		else if(name.contains("演唱会")){
		        			sortType=ShowType.CONCERT;
		        		}
		        		else if(name.contains("歌剧")||name.contains("话剧")){
		        			sortType=ShowType.DRAMA;
		        		}
						String href=trEle.select("td>a").first().absUrl("href").trim();
						String time=trEle.select("td:eq(2) td").text().trim();
						time=time.replace(" ", "").replace("　", "");
						if(time.matches("[\\d]+-[\\d]+-[\\d]+")){
							int compare=PubFun.compareDate(PubFun.getCurrentYear()+"-"+PubFun.getCurrentMonth()+"-"+PubFun.getCurrentDay(),time);
							if(compare>0)
								continue;
						}
		                if(time.matches("[\\d]+-[\\d]+-[\\d]+至[\\d]+-[\\d]+-[\\d]+")){
		                	String endTime=PubFun.getRegex("[\\d]+-[\\d]+-[\\d]+至([\\d]+-[\\d]+-[\\d]+)", time,1);
		                	int compare=PubFun.compareDate(PubFun.getCurrentYear()+"-"+PubFun.getCurrentMonth()+"-"+PubFun.getCurrentDay(), endTime);
		                	if(compare>0)
								continue;
						}
		                try{
			                   processLastPage(href,sortType);
			                }catch(Exception ee){
			                	log.error(Const.concat(href,sortType),ee);
			                }
					}
					continue;
				}
				else if(sort.contains("戏剧场")){
					Iterator<Element> trEleList=tableEle.select("td[valign=top] tr:gt(0)").iterator();
					while(trEleList.hasNext()){
						Element trEle=trEleList.next();
						String name=trEle.select("td>a").first().text().trim();
						if(name.contains("音乐会")){
		        			sortType=ShowType.SHOW;
		        		}
		        		if(name.contains("芭蕾")||name.contains("舞")){
		        			sortType=ShowType.DANCE;
		        		}
		        		if(name.contains("京剧")){
		        			sortType=ShowType.OPERA;
		        		}
		        		else if(name.contains("演唱会")){
		        			sortType=ShowType.CONCERT;
		        		}
		        		else if(name.contains("歌剧")||name.contains("话剧")){
		        			sortType=ShowType.DRAMA;
		        		}
						String href=trEle.select("td>a").first().absUrl("href").trim();
						String time=trEle.select("td:eq(2) td").text().trim();
						time=time.replace(" ", "").replace("　", "");
						if(time.matches("[\\d]+-[\\d]+-[\\d]+")){
							int compare=PubFun.compareDate(PubFun.getCurrentYear()+"-"+PubFun.getCurrentMonth()+"-"+PubFun.getCurrentDay(),time);
							if(compare>0)
								continue;
						}
		                if(time.matches("[\\d]+-[\\d]+-[\\d]+至[\\d]+-[\\d]+-[\\d]+")){
		                	String endTime=PubFun.getRegex("[\\d]+-[\\d]+-[\\d]+至([\\d]+-[\\d]+-[\\d]+)", time,1);
		                	int compare=PubFun.compareDate(PubFun.getCurrentYear()+"-"+PubFun.getCurrentMonth()+"-"+PubFun.getCurrentDay(), endTime);
		                	if(compare>0)
								continue;
						}
		                try{
			                   processLastPage(href,sortType);
			                }catch(Exception ee){
			                	log.error(Const.concat(href,sortType),ee);
			                }
					}
					continue;
				}
				continue;
			}
			else if(sort.contains("演唱")){
				    sortType=ShowType.CONCERT;
			}
			else if(sort.contains("音乐")&&!sort.contains("剧")){
					sortType=ShowType.SHOW;
		    }
			else if(sort.contains("话剧") || sort.contains("歌剧")||sort.contains("音乐剧")){
					sortType=ShowType.DRAMA;
			}
			else if(sort.contains("芭蕾") || sort.contains("舞蹈")||sort.contains("舞")){
					sortType=ShowType.DANCE;
			}
		    else if(sort.contains("戏剧")||sort.contains("戏曲")||sort.contains("二人转") || sort.contains("魔术")||sort.contains("杂技") || sort.contains("相声")||sort.contains("小品") || sort.contains("马戏")){
				    sortType=ShowType.OPERA;
			}
			else if(sort.contains("亲子休闲")){
				 sortType=ShowType.CHILDREN;
			}
			else if(sort.contains("体育") ){
				 sortType=ShowType.SPORTS;
			}
			else if(sort.contains("旅游")|| sort.contains("电影")){
				 sortType=ShowType.HOLIDAY;
			}
			Iterator<Element> trEleList=tableEle.select("td[valign=top] tr:gt(0)").iterator();
			while(trEleList.hasNext()){
				Element trEle=trEleList.next();
				String href=trEle.select("td>a").first().absUrl("href").trim();
				String time=trEle.select("td:eq(2) td").text().trim();
				time=time.replace(" ", "").replace("　", "");
				if(time.matches("[\\d]+-[\\d]+-[\\d]+")){
					int compare=PubFun.compareDate(PubFun.getCurrentYear()+"-"+PubFun.getCurrentMonth()+"-"+PubFun.getCurrentDay(),time);
					if(compare>0)
						continue;
				}
                if(time.matches("[\\d]+-[\\d]+-[\\d]+至[\\d]+-[\\d]+-[\\d]+")){
                	String endTime=PubFun.getRegex("[\\d]+-[\\d]+-[\\d]+至([\\d]+-[\\d]+-[\\d]+)", time,1);
                	int compare=PubFun.compareDate(PubFun.getCurrentYear()+"-"+PubFun.getCurrentMonth()+"-"+PubFun.getCurrentDay(), endTime);
                	if(compare>0)
						continue;
				}
                try{
	                   processLastPage(href,sortType);
	            }catch(Exception ee){
	                	log.error(Const.concat(href,sortType),ee);
	            }
			}
		}
	}
	
	public void processLastPage(String url,int sort){
		Document doc=getDoc(url);
		Element element=doc.select("body>table[width=960]").get(6).select("td[valign=top] table[width=100%] td[width=160]").first();
		String imagePath=element.select("td img").first().absUrl("src").trim();
		List<Element> eleList=element.parent().child(2).select("table[width=100%]>tr>td[width=100%]>table[width=100%]>tr>td>table");//
		String showName=eleList.get(0).select("tr div").first().text();
		String showSite=eleList.get(2).select("table tr:eq(1) td[valign=top]").first().text();
		String summary=PubFun.cleanElement(eleList.get(6).select("tr>td>table>tr:eq(1) tr[valign=top]").first()).html();
		Map<String,List<TicketPrice>> map=getMap(eleList.get(4),url);
		Show show=new Show();
		show.setAgent_id(agentID);
		show.setImage_path(imagePath);
		show.setIntroduction(summary);
		show.setName(showName);
		show.setSiteName(showSite);
		show.setTimeAndPrice(map);
		show.setType(sort);
		try{
			this.getDao().saveShow(show);
		}
		catch(Exception ee){
			
		}
		
	}
	
	public Map<String,List<TicketPrice>> getMap(Element ele,String url){
		Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
		Iterator<Element> trList=ele.select("tr[bgcolor^=#FE]").iterator();
		while(trList.hasNext()){
			Element eleTr=trList.next();
			if(eleTr==null)
				continue;
			String showTime=eleTr.select("td:eq(1)").first().text();
			String newshowTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm");
			if(newshowTime!=null)
				showTime=newshowTime;
			List<TicketPrice> list=new ArrayList<TicketPrice>();
			Iterator<Element> priceEleList=eleTr.select("td:eq(2)>a").iterator();
			while(priceEleList.hasNext()){
				Element priceEle=priceEleList.next();
				TicketPrice ticket=new TicketPrice();
				if(null==priceEle||"".equals(priceEle.text().trim())||priceEle.text().trim().contains("待定"))
					continue;
				String detailUrl=priceEle.absUrl("href").trim();
				String price=priceEle.text().trim();
				price.replace(" ", "");
				ticket.setExist(true);
				ticket.setMainURL(url);
				String remark=null;
			    if(price.matches("[\\d]*[（\\(]套[）\\)]")){
			    	String newPrice=price;
			    	price=PubFun.getRegex("([\\d]*)[（\\(]套[）\\)]", newPrice, 1);
			    	remark=PubFun.getRegex("([\\d]*)([（\\(]套[）\\)])", newPrice, 2);
			    	ticket.setRemark(remark);
			    }
			    ticket.setPrice(price);
			    ticket.setDetailURL(detailUrl);
			    list.add(ticket);
			}
			Iterator<Element> nopriceEleList=eleTr.select("td:eq(2)>span").iterator();
			while(nopriceEleList.hasNext()){
				Element eleNoPrice=nopriceEleList.next();
				TicketPrice ticket=new TicketPrice();
				if(null==eleNoPrice||"".equals(eleNoPrice.text().trim())||eleNoPrice.text().trim().contains("待定"))
					continue;
				String price=eleNoPrice.text().trim();
				price.replace(" ", "");
				ticket.setExist(false);
				ticket.setMainURL(url);
				String remark=null;
			    if(price.matches("[\\d]*[（\\(]套[）\\)]")){
			    	String newPrice=price;
			    	price=PubFun.getRegex("([\\d]*)[（\\(]套[）\\)]", newPrice, 1);
			    	remark=PubFun.getRegex("([\\d]*)([（\\(]套[）\\)]", newPrice, 2);
			    	ticket.setRemark(remark);
			    }
			    ticket.setPrice(price);
			    list.add(ticket);
			}
			map.put(showTime, list);
		}
		return map;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
      
        JingWenPiaoWu spider=new JingWenPiaoWu();
        spider.setDao(new DataDao());
        spider.extract();
	}

}
