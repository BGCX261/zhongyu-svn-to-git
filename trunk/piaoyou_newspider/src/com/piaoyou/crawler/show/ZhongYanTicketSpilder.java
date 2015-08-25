package com.piaoyou.crawler.show;


import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.piaoyou.bean.CommonInfo;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

/**
 * 中演票务网  
 */
public class ZhongYanTicketSpilder extends TicketSpider implements SpiderTask{
	private static final Log log = LogFactory.getLog(ZhongYanTicketSpilder.class);
	static final String BASE_URL="http://www.t3.com.cn";
	static final Map<Integer, Integer> typeMap=new HashMap<Integer, Integer>();
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static String agentID = new DataDao().searchBySQL("select id from agency_info where agency_url=?", BASE_URL)
	.get(0).get("id").toString();
	static{initShowType();}
	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		ZhongYanTicketSpilder zhongYan=new ZhongYanTicketSpilder();
		zhongYan.setDao(new DataDao());
		zhongYan.extract();
//		zhongYan.extractEach("http://www.t3.com.cn/project/193115.html", "待定", Const.CONCERT);
	}
	
	private static void initShowType(){
		typeMap.put(0, 1);
		typeMap.put(1, 3);
		typeMap.put(2, 2);
		typeMap.put(3, 7);
		typeMap.put(4, 5);
		typeMap.put(5, 6);
		typeMap.put(6, 4);
		typeMap.put(7, 8);
		typeMap.put(8, 9);
		typeMap.put(9, 9);
	}
	/**
	 * 日期处理
	 * @param date
	 * @return
	 */
	static List<String> dateFormat(String date){
		List <String>rList=new ArrayList<String>();
		date=date.replaceAll(";", ":");
		//标准时间  2011-06-25 星期日 20:00 
		if(PubFun.isMatchRegex(date, "\\d{4}-\\d{1,2}-\\d{1,2}\\s\\D{3}\\s\\d{1,2}\\:\\d{1,2}")){
			rList.add(date.replaceAll("\\D{4}", ""));
		//标准跨度日期    2011-01-01 星期六 至 2011-12-31 星期六
		}else if(PubFun.isMatchRegex(date, "\\d{4}-\\d{1,2}-\\d{1,2}\\s\\D{3}\\s\\D{1}\\s\\d{4}-\\d{1,2}-\\d{1,2}\\s\\D{3}")){
//			String begDate=date.split("至")[0].trim().replaceAll("\\D{4}", "");
//			String endDate=date.split("至")[1].trim().replaceAll("\\D{4}", "");
//			rList=PubFun.getTwoDateAllDate(begDate, endDate,"", "yyyy-MM-dd");
			rList.add(date);
		//标准日期没时间   2011-07-08 星期五	
		}else if(PubFun.isMatchRegex(date,"\\d{4}-\\d{1,2}-\\d{1,2}\\s\\D{3}")){
			rList.add(date.replaceAll("[A-Za-z\u4e00-\u9fa5]", "").trim());
		//标准格式带说明    2011-06-25 星期日 毕业季半价专场
		}else if(PubFun.isMatchRegex(date, "\\d{4}-\\d{1,2}-\\d{1,2}\\s\\D{3}\\s\\D{1,100}")){
			rList.add(date.replaceAll("[A-Za-z\u4e00-\u9fa5]", "").trim());
		//跨度日期 没有星期   2010-10-12 至 2011-12-31	
		}else if(PubFun.isMatchRegex(date, "\\d{4}-\\d{1,2}-\\d{1,2}\\s\\D{1}\\s\\d{4}-\\d{1,2}-\\d{1,2}")){
//			String begDate=date.split("至")[0].trim().replaceAll("[A-Za-z\u4e00-\u9fa5]", "");
//			String endDate=date.split("至")[1].trim().replaceAll("[A-Za-z\u4e00-\u9fa5]", "");
//			rList=PubFun.getTwoDateAllDate(begDate, endDate,"", "yyyy-MM-dd");
			rList.add(date);
		//2011-07-06 星期三 19:30 毕业季半价专场
		}else if(PubFun.isMatchRegex(date, "\\d{4}-\\d{1,2}-\\d{1,2}\\s\\D{3}\\s\\d{1,2}\\:\\d{1,2}\\s\\D{1,100}")){
			rList.add(date.replaceAll("[A-Za-z\u4e00-\u9fa5]", "").replace("  ", " ").trim());
		//2011-08-13 至2011-10-13 19:30
		}else if(PubFun.isMatchRegex(date, "\\d{4}-\\d{1,2}-\\d{1,2}\\s\\D{1}\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{1,2}\\:\\d{1,2}")){
			String begDate=date.split("至")[0].trim().replaceAll(" ", "");
			String endDate=date.split("至")[1].trim().substring(0,begDate.length());
			rList=PubFun.getTwoDateAllDate(begDate, endDate,date.substring(date.lastIndexOf(" "),date.length()).trim(), "yyyy-MM-dd");
				
		//每天 每天 20:00	    
//		}else if(PubFun.isMatchRegex(date,"\\D{2}\\s\\d{1,2}\\:\\d{1,2}")){
//			rList.add("all");
//		}else{
//			rList=null;
//		}
		}else{
			rList.add(date);
		}
		return rList;
	}
	
	@Override
	public void extract() {
		Document cityListDocument=getDoc(BASE_URL);
		Elements citysList=cityListDocument.getElementById("citySelect").getElementsByTag("a");
		//城市循环
		for(int i=1;i<citysList.size();i++){//citysList.size()
			try{
				if(citysList.get(i).text().equals("")){
					continue;
				}
				String cityUrl=citysList.get(i).absUrl("href");
				Document cityDocument=getDoc(cityUrl);
				Elements typeList=cityDocument.getElementById("productCategorys").getElementsByTag("a");
				//演出类型循环
				for(int x=0;x<typeList.size();x++){//typeList.size()
					try{
						String typeUrl=typeList.get(x).absUrl("href");
						Document typeDocument=getDoc(typeUrl);
						if(typeDocument.getElementsByClass("page").isEmpty()){
							//没有演出
						}else{
							//判断总页数
							String temp=typeDocument.getElementsByClass("page").first().getElementsByTag("a").last().absUrl("href").toString();
							String url_title=temp.substring(0,temp.lastIndexOf("-")+1);
							int pageCount=Integer.parseInt(temp.substring(url_title.length(),temp.length()-1));
							//每页循环
							for(int y=1;y<=pageCount;y++){
								try{
									//第一页不需要再抓去数据
									if(y!=1){
										typeDocument=getDoc(url_title+y+"/");
									}
									Elements els=typeDocument.getElementsByClass("pj-w");
									String oneShowUrl="";
									String site_Name="";
									int showType=typeMap.get(x);
									//每页演出循环
									for(int z=0;z<els.size();z++){//els.size()
										try{
											oneShowUrl=els.get(z).getElementsByTag("a").get(0).absUrl("href");
											site_Name=els.get(z).getElementsByTag("a").get(1).text().trim();
											if("待定".equals(site_Name)){
												continue;
											}
											extractEach(oneShowUrl,site_Name,showType);
										}catch(Exception e){
											continue;
										}
									}
								}catch(Exception e){
									continue;
								}
							}
						}
					}catch(Exception e){
						continue;
					}

				}
			}catch(Exception e){
				continue;
			}
		}
	}
	private void extractEach(String oneShowUrl,String site_Name,int showType){
		String showName="";
		String showInfo="";
		String photoUrl="";
		String projectId="";
		String showStatus="";
		Document oneShow=getDoc(oneShowUrl);
		CommonInfo common = new CommonInfo();
		List<String> time = new ArrayList<String>();
		TreeSet<Integer> priceSet = new TreeSet<Integer>();
		String price = "";
		try{
			photoUrl=oneShow.getElementsByClass("introTicket").first().getElementsByTag("img").first().absUrl("src");
			showStatus=oneShow.getElementsByClass("tip_red02").first().text().trim();
			if(showStatus.equals("售票中")){
				common.setStatus(1);
			}else{
				common.setStatus(0);
			}
			showInfo=oneShow.getElementById("inDiv").html();//introTicket
			common.setRemote_img_url(photoUrl);
			common.setAgency_id(Integer.parseInt(agentID));
			common.setIntroduction(showInfo);
			common.setSite_name(site_Name);
			common.setShow_type(showType);
			projectId=oneShow.getElementById("projectId").attr("value");  //中演的演出ID用于票价请求
			showName=oneShow.getElementsByAttributeValue("name", "project.name").attr("value");
			common.setName(showName);
			if(!oneShow.getElementsByClass("buyTinfo").first().getElementsByTag("strong").isEmpty()){
				common.setIs_first_agency(1);
				//自助选坐
				if(!oneShow.getElementsByClass("seat").isEmpty()){
					common.setIs_first_agency(1);
				}
			}else{
				common.setIs_first_agency(0);
			}
		}catch(Exception e){
			return;
		}
		//是否有选票标签
		boolean b=false;
		if(!oneShow.getElementsByClass("perform_time02").isEmpty()){
			b=true;
		}else{
			b=false;
		}
		List<String> dateList=null;
		if(b){
			Elements datesEls=oneShow.getElementsByClass("perform_time02");
			Elements priceEls=oneShow.getElementsByClass("perform_prive02");
			for(int c=0;c<datesEls.size();c++){
				try{
					dateList=dateFormat(PubFun.ToDBC(datesEls.get(c).text().trim()));
					if(dateList==null){
						continue;
					}
					//票价
					String prices[]=priceEls.get(c).text().toLowerCase().replaceAll(" ", "").split("[|]");
					//受否有票
					Elements tee=priceEls.get(c).getElementsByTag("tr").first().getElementsByTag("td").first().getAllElements();
					
					for(int d=0;d<dateList.size();d++){
						for(int s=0;s<prices.length;s++){
							String rStr="";
							String remark="";
							String priceStr=PubFun.ToDBC(prices[s].trim()).trim();
							
							priceStr=priceStr.replace("（", "(");
							priceStr=priceStr.replace("）", ")");
							//如果是纯数字
							if(PubFun.isMatchRegex(priceStr, "[0-9]*")){
								rStr=priceStr;
								remark=null;
							}else{ 
								//1000(680*2)
								if(PubFun.isMatchRegex(priceStr,"[\\d]*\\(([^\\)]*)\\)")){
									rStr=priceStr.substring(0,priceStr.indexOf("("));
									remark=priceStr.replace(rStr, "");
								//1980场地	
								}else if(PubFun.isMatchRegex(priceStr,"\\d+\\D+")){
									rStr=priceStr.replaceAll("\\D*", "");
									remark=priceStr.replace(rStr, "");
								//买一送一280(280*2)	
									
								}else if(PubFun.isMatchRegex(priceStr, "[^\\d]+([\\d]+)\\([^\\)]*\\)\\D*") && !PubFun.isMatchRegex(priceStr,"\\d*\\(([^\\)]*)\\)\\D*")){
									rStr=PubFun.getRegex("[^\\d]+([\\d]+)\\([^\\)]*\\)\\D*", priceStr, 1);
									remark=PubFun.getRegex("[^\\d]+([\\d]+)\\(([^\\)]*)\\)\\D*", priceStr, 2);
								}
								//内场至尊vip500
								//家庭套999(380*3)
								else if(PubFun.isMatchRegex(priceStr,"\\D+\\d+")){
									rStr=priceStr.replaceAll("\\D*", "");
									remark=priceStr.replace(rStr, "");
								//498探花套票(280*2)	
									//789元套票(480*2)
								}else if(PubFun.isMatchRegex(priceStr,"\\d*\\(([^\\)]*)\\)\\D*")){
									rStr=priceStr.replaceAll("\\(([^\\)]*)\\)\\D*", "");
									remark=priceStr.replace(rStr, "");
								}
								//1111元套票(200*3)
								//680元套票（480*2）
								//123元(380*3张)
								else if(PubFun.isMatchRegex(priceStr, "\\D*[\\d]+[^\\d]+\\([^\\)]*\\)\\D*")){
									rStr=PubFun.getRegex("\\D*([\\d]+)[^\\d]+\\([^\\)]*\\)\\D*", priceStr, 1);
									remark=PubFun.getRegex("\\D*[\\d]+[^\\d]+\\(([^\\)]*)\\)\\D*", priceStr, 1);
									remark=remark.replace("张", "").replace("元", "").replace(" ", "").trim();
								}else{
									//需要再次请求
									if(tee!=null){
										if(tee.get(s+1).tagName().equals("a")){
											String oneId=tee.get(s+1).getElementsByTag("a").get(0).attr("id");
											oneId=oneId.substring(oneId.indexOf("_")+1,oneId.length());
											try {
												Document ajaxPrice=null;
												for(int i=0;i<3;i++){
													ajaxPrice = Jsoup.connect("http://www.t3.com.cn/front/order/addItem.htm")
													.data("projectId", projectId)
													.data("param","projectFinal")
													.data("sellPolicyId",oneId).post();
													if(ajaxPrice!=null){
														break;
													}
												}
												rStr=ajaxPrice.getElementById("price_"+oneId).text().replace(".00", "");
												remark=priceStr;
												if("待定".equals(rStr)){
													rStr=null;
													remark="待定";
												}
											} catch (IOException e) {
												e.printStackTrace();
											}
										}
									}else{
										rStr=priceStr;
										remark=priceStr;
									}
								}
							}
							try{
								if(rStr!=null&&!"".equals(rStr)){
									rStr=rStr.replaceAll("\\.\\d+", "");
									priceSet.add(Integer.parseInt(rStr));
								}
							}catch(Exception e){
								
							}
						}
						String newShowTime=dateList.get(d);
						newShowTime=newShowTime.replace("()", "").replace("《》", "").trim();
						newShowTime=newShowTime.replaceAll("\\([^\\)]*\\)", "");
						newShowTime=newShowTime.replaceAll("[^\\d]*星期[^\\d]*", " ");
						try{
							newShowTime = newDate.format(oldDate.parse(newShowTime));
						}catch(Exception ee){
							
						}
						time.add(newShowTime);
					}
				}catch(Exception e){
					continue;
				}
			}
		}
		
		for (Iterator<Integer> iterator = priceSet.iterator(); iterator.hasNext();) {
			Integer tmp = (Integer) iterator.next();
			price+=tmp+",";
		}
		if(time==null||time.size()==0){
			return ;
		}
		if("".equals(price)){
			return ;
		}else{
			price = price.substring(0,price.length()-1);
		}
		common.setMin_price(price.split(",")[0]);
		common.setMainURL(oneShowUrl);
		common.setShow_time(time);
		common.setPrice(price);
		common.setIs_check("0");
		try {
			this.saveCommonInfo(common);
		} catch (SQLException e) {
			log.error(e);
		}
	}
}
