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
import org.jsoup.nodes.Node;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

public class BeijingYanchuPiaowuSpider extends TicketSpider implements SpiderTask {
	private static Log log=LogFactory.getLog(BeijingYanchuPiaowuSpider.class);
	@Override
	public void extract() {
		Document doc=getDoc("http://www.lxpiao.com/all.asp");
		if(doc==null){
			throw new RuntimeException();
		}
		Iterator<Element> trEleList=doc.select("body>table.table>tr:gt(1)>td>table>tr>td>table>tr").iterator();
		while(trEleList.hasNext()){
			Element eleTr=trEleList.next();
			String sort=eleTr.select("td.txt14 div font").text().trim();
			int sortType=ShowType.OTHER;
			  if(sort.contains("梅兰芳大剧院演出季")){
					Iterator<Element> listEleDiv=eleTr.select("td>table>tr:gt(0)").iterator();
					while(listEleDiv.hasNext()){
						Element childeleDiv=listEleDiv.next();
						String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
						String name=childeleDiv.select("td:eq(0) a").first().text();
						if(name.contains("音乐会")){
		        			sortType=ShowType.SHOW;
		        		}
		        		if(name.contains("芭蕾")||name.contains("舞")){
		        			sortType=ShowType.DANCE;
		        		}
		        		if(name.contains("京剧")||name.contains("戏")){
		        			sortType=ShowType.OPERA;
		        		}
		        		else if(name.contains("演唱会")){
		        			sortType=ShowType.CONCERT;
		        		}
		        		else if(name.contains("歌剧")||name.contains("话剧")||name.contains("剧")){
		        			sortType=ShowType.DRAMA;
		        		}
		        		try{
							 processLastPage(href,sortType);
						}catch(Exception e){
							log.error(Const.concat(href,sortType),e) ;
						}
					}
					continue;
				}
			  else  if(sort.contains("国家大剧院戏剧场")){
				Iterator<Element> listEleDiv=eleTr.select("td>table>tr:gt(0)").iterator();
				while(listEleDiv.hasNext()){
					Element childeleDiv=listEleDiv.next();
					String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
					String name=childeleDiv.select("td:eq(0) a").first().text();
					if(name.contains("音乐会")){
	        			sortType=ShowType.SHOW;
	        		}
	        		if(name.contains("芭蕾")||name.contains("舞")){
	        			sortType=ShowType.DANCE;
	        		}
	        		if(name.contains("京剧")||name.contains("戏")){
	        			sortType=ShowType.OPERA;
	        		}
	        		else if(name.contains("演唱会")){
	        			sortType=ShowType.CONCERT;
	        		}
	        		else if(name.contains("歌剧")||name.contains("话剧")||name.contains("剧")){
	        			sortType=ShowType.DRAMA;
	        		}
	        		try{
						 processLastPage(href,sortType);
					}catch(Exception e){
						log.error(Const.concat(href,sortType),e) ;
					}
				}
				continue;
			}
			else if(sort.contains("国家大剧院小剧场")){
				Iterator<Element> listEleDiv=eleTr.select("td>table>tr:gt(0)").iterator();
				while(listEleDiv.hasNext()){
					Element childeleDiv=listEleDiv.next();
					String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
					String name=childeleDiv.select("td:eq(0) a").first().text();
					
					if(name.contains("音乐会")){
	        			sortType=ShowType.SHOW;
	        		}
	        		if(name.contains("芭蕾")||name.contains("舞")){
	        			sortType=ShowType.DANCE;
	        		}
	        		if(name.contains("京剧")||name.contains("戏")||name.contains("戏剧")){
	        			sortType=ShowType.OPERA;
	        		}
	        		else if(name.contains("演唱会")){
	        			sortType=ShowType.CONCERT;
	        		}
	        		else if(name.contains("歌剧")||name.contains("话剧")||name.contains("剧")){
	        			sortType=ShowType.DRAMA;
	        		}
	        		try{
						 processLastPage(href,sortType);
					}catch(Exception e){
						log.error(Const.concat(href,sortType),e) ;
					}
				}
				continue;
			}
			else if(sort.contains("国家大剧院音乐厅")){
				Iterator<Element> listEleDiv=eleTr.select("td>table>tr:gt(0)").iterator();
				while(listEleDiv.hasNext()){
					Element childeleDiv=listEleDiv.next();
					String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
					String name=childeleDiv.select("td:eq(0) a").first().text();
					if(name.contains("音乐会")){
	        			sortType=ShowType.SHOW;
	        		}
	        		if(name.contains("芭蕾")||name.contains("舞")){
	        			sortType=ShowType.DANCE;
	        		}
	        		if(name.contains("京剧")||name.contains("戏")){
	        			sortType=ShowType.OPERA;
	        		}
	        		else if(name.contains("演唱会")){
	        			sortType=ShowType.CONCERT;
	        		}
	        		else if(name.contains("歌剧")||name.contains("话剧")||name.contains("剧")){
	        			sortType=ShowType.DRAMA;
	        		}
	        		try{
						 processLastPage(href,sortType);
					}catch(Exception e){
						log.error(Const.concat(href,sortType),e) ;
					}
				}
				continue;
			}
			else if(sort.contains("国家大剧院歌剧院")){
				Iterator<Element> listEleDiv=eleTr.select("td>table>tr:gt(0)").iterator();
				while(listEleDiv.hasNext()){
					Element childeleDiv=listEleDiv.next();
					String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
					String name=childeleDiv.select("td:eq(0) a").first().text();
					if(name.contains("音乐会")){
	        			sortType=ShowType.SHOW;
	        		}
	        		if(name.contains("芭蕾")||name.contains("舞")){
	        			sortType=ShowType.DANCE;
	        		}
	        		if(name.contains("京剧")||name.contains("戏")){
	        			sortType=ShowType.OPERA;
	        		}
	        		else if(name.contains("演唱会")){
	        			sortType=ShowType.CONCERT;
	        		}
	        		else if(name.contains("歌剧")||name.contains("话剧")||name.contains("剧")){
	        			sortType=ShowType.DRAMA;
	        		}
	        		try{
						 processLastPage(href,sortType);
					}catch(Exception e){
						log.error(Const.concat(href,sortType),e) ;
					}
				}
				continue;
			}
			else if(sort.contains("打开艺术之门")){
				Iterator<Element> listEleDiv=eleTr.select("td>table>tr:gt(0)").iterator();
				while(listEleDiv.hasNext()){
					Element childeleDiv=listEleDiv.next();
					String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
					String name=childeleDiv.select("td:eq(0) a").first().text();
					if(name.contains("音乐会")){
	        			sortType=ShowType.SHOW;
	        		}
	        		if(name.contains("芭蕾")||name.contains("舞")){
	        			sortType=ShowType.DANCE;
	        		}
	        		if(name.contains("京剧")||name.contains("戏")){
	        			sortType=ShowType.OPERA;
	        		}
	        		else if(name.contains("演唱会")){
	        			sortType=ShowType.CONCERT;
	        		}
	        		else if(name.contains("歌剧")||name.contains("话剧")||name.contains("剧")){
	        			sortType=ShowType.DRAMA;
	        		}
	        		try{
						 processLastPage(href,sortType);
					}catch(Exception e){
						log.error(Const.concat(href,sortType),e) ;
					}
				}
				continue;
			}
			else if(sort.contains("打开音乐之门")){
				Iterator<Element> listEleDiv=eleTr.select("td>table>tr:gt(0)").iterator();
				while(listEleDiv.hasNext()){
					Element childeleDiv=listEleDiv.next();
					String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
					String name=childeleDiv.select("td:eq(0) a").first().text();
					if(name.contains("音乐会")){
	        			sortType=ShowType.SHOW;
	        		}
	        		if(name.contains("芭蕾")||name.contains("舞")){
	        			sortType=ShowType.DANCE;
	        		}
	        		if(name.contains("京剧")||name.contains("戏")){
	        			sortType=ShowType.OPERA;
	        		}
	        		else if(name.contains("演唱会")){
	        			sortType=ShowType.CONCERT;
	        		}
	        		else if(name.contains("歌剧")||name.contains("话剧")||name.contains("剧")){
	        			sortType=ShowType.DRAMA;
	        		}
	        		try{
						 processLastPage(href,sortType);
					}catch(Exception e){
						log.error(Const.concat(href,sortType),e) ;
					}
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
		    else if(sort.contains("戏剧")||sort.contains("京剧")||sort.contains("戏曲")||sort.contains("二人转") || sort.contains("魔术")||sort.contains("杂技") || sort.contains("相声")||sort.contains("小品") || sort.contains("马戏")){
				    sortType=ShowType.OPERA;
			}
			else if(sort.contains("儿童")){
				 sortType=ShowType.CHILDREN;
			}
			else if(sort.contains("体育") ){
				 sortType=ShowType.SPORTS;
			}
			else if(sort.contains("休闲")||sort.contains("旅游")|| sort.contains("电影")||sort.contains("大片")){
				 sortType=ShowType.HOLIDAY;
			}
		    Iterator<Element> listEleDiv=eleTr.select("td>table>tr:gt(0)").iterator();
			while(listEleDiv.hasNext()){
				Element childeleDiv=listEleDiv.next();
				String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
				try{
					 processLastPage(href,sortType);
				}catch(Exception e){
					log.error(Const.concat(href,sortType),e) ;
				}
			}
		}
	}
	
	public void processLastPage(String url,int sort){
		  Document doc=getDoc(url);
		  Element eleHtml=doc.select("body>table[width=968]").get(1).select("tr>td>table>tr>td.line>table>tr").first();
		  String imagePath=eleHtml.select("td:eq(0) img").first().absUrl("src").trim();
		  String showName=eleHtml.select("td:eq(1)>table>tr>td").first().text().trim();
		  List<Node> nodeList=eleHtml.select("td:eq(1)>table>tr:eq(1)>td:eq(0)>font.table").first().childNodes();
		  String showSite=nodeList.get(4).toString();
		  showSite=showSite.replace("场馆：", "").replace("：", "").replace("场馆", "").trim();
          Element eleTable=eleHtml.parent().parent().parent().parent();
		  String summary=PubFun.cleanElement(eleTable.select("tr:eq(4)>td.line>table>tr>td.table>table.table>tr>td").first()).html();
		  Map<String,List<TicketPrice>> map=getMap(eleTable,url);
		  Show show=new Show();
		  show.setAgent_id("62");
		  show.setImage_path(imagePath);
		  show.setIntroduction(summary);
		  show.setName(showName);
		  show.setSiteName(showSite);
		  show.setTimeAndPrice(map);
		  show.setType(sort);
		  try{
			 getDao().saveShow(show);
		  }catch(Exception ee){
		  }
	}
	 public Map<String,List<TicketPrice>> getMap(Element doc,String url){
	    	Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
	    	Iterator<Element> trList=doc.select("tr:eq(2)>td.line>table>tr>td>table.table>tr:gt(0)").iterator();
	    	while(trList.hasNext()){
	    		Element eleTr=trList.next();
	    		Element eleDate=eleTr.select("td:eq(1)").first();
	    		if(eleDate==null)
	    			continue;
	    	    String showTime=eleDate.text().trim();
	    	    String newshowTime=null;
	    	    if(showTime.matches("[\\d]{1,4}-[\\d]{1,2}-[\\d]{1,2} [\\d]{1,2}:[\\d]{1,2}:[\\d]{1,2}")){
 			     newshowTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm");
	    	    }
 			    if(newshowTime!=null){
 			    	showTime=newshowTime;
 			    }
 			    List<TicketPrice> list=new ArrayList<TicketPrice>();
	    		Iterator<Element> YespirceList=eleTr.select("td:eq(3)>a").iterator();
	    		while(YespirceList.hasNext()){
	    			Element elePrice=YespirceList.next();
	    			TicketPrice ticket=new TicketPrice();
	    			String price=elePrice.text().trim();
	    			if("".equals(price)||price.contains("待定")||price.contains("套票"))
	    				continue;
	    			ticket.setPrice(price);
	    			if(price.matches("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*")){
	    				String newPrice=PubFun.getRegex("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*", price, 1);
	    				String remark=PubFun.getRegex("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*", price, 2);
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(remark);
	    			}
	    			if(price.contains("套")){
	    				String newPrice=price.replace("套", "");
	    				String reamrk="套";
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(reamrk);
	    			}
	    			ticket.setDetailURL(url);
	    			ticket.setExist(true);
	    			ticket.setMainURL(url);
	    			list.add(ticket);
	    		}
	    		Iterator<Element> nOpirceList=eleTr.select("td:eq(3)>font").iterator();
	    		while(nOpirceList.hasNext()){
	    			Element elePrice=nOpirceList.next();
	    			TicketPrice ticket=new TicketPrice();
	    			String price=elePrice.text().trim();
	    			if("".equals(price)||price.contains("待定")||price.contains("套票"))
	    				continue;
	    			ticket.setPrice(price);
	    			if(price.matches("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*")){
	    				String newPrice=PubFun.getRegex("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*", price, 1);
	    				String remark=PubFun.getRegex("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*", price, 2);
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(remark);
	    			}
	    			if(price.contains("套")){
	    				String newPrice=price.replace("套", "");
	    				String reamrk="套";
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(reamrk);
	    			}
	    			ticket.setExist(false);
	    			ticket.setMainURL(url);
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
		BeijingYanchuPiaowuSpider spider= new BeijingYanchuPiaowuSpider();
//	    spider.setDao(new DataDao());
//		spider.extract();
		spider.processLastPage("http://www.lxpiao.com/piao.asp?id=1925", 1);
      
	}

}
