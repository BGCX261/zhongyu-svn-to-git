

package com.piaoyou.util;

import java.text.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 作者 <a href="mailto:wwwww@hotmail.com">icedcoco</a>
 * @version 创建时间：Jun 27, 2011 4:35:26 PM
 * 类说明
 */
public class DateTime {
	static final long ONE_MINUTE=60*1000L;
	static final long ONE_HOUR=60*ONE_MINUTE;
	static final long ONE_DAY=ONE_HOUR*24;
	static final SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
	static final SimpleDateFormat time=new SimpleDateFormat("HH:mm:ss");
	static final SimpleDateFormat dateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	static Calendar startTime = new GregorianCalendar();
//	private Calendar endTime = new GregorianCalendar();	
	/*
	 * ??��?��??:BeginTime
	 * ???  ???:???EndTime()??��?��??起使???;??��??计�????��????��??
	 * ???  ???:???
	 * �???????:???
	 * 
	 */	
	public static void BeginTime(){
		Date trialTime = new Date();
			startTime.setTime(trialTime); 		
	}
	/*
	 * ??��?��??:BeginTime
	 * ???  ???:???EndTime()??��?��??起使???;??��??计�????��????��??
	 * ???  ???:???
	 * �???????:??��??�?�?�????,??????,�?
	 * 
	 */	
	public static String EndTime(){
		Date trialTime = new Date();
		Calendar endTime = new GregorianCalendar();
		 endTime.setTime(trialTime);               
			endTime.add(Calendar.MILLISECOND , -startTime.get(Calendar.MILLISECOND));
			endTime.add(Calendar.SECOND  , - startTime.get(Calendar.SECOND));
			endTime.add(Calendar.MINUTE  , - startTime.get(Calendar.MINUTE));
			endTime.add(Calendar.HOUR , - startTime.get(Calendar.HOUR_OF_DAY));
		return time.format(endTime.getTime());
 
	}
	/*
	 * ??��?��??:EndTime
	 * ???  ???:?????��??计�????��????��??
	 * ???  ???:beginTime:�?�???��??
	 * �???????:??��??�?�?�????,??????,�?
	 */
	public static String EndTime(Date beginTime){
		Calendar start=new GregorianCalendar();
		start.setTime(beginTime);
		Date trialTime = new Date();
		Calendar endTime = new GregorianCalendar();
 		endTime.setTime(trialTime);               
		endTime.add(Calendar.MILLISECOND , -start.get(Calendar.MILLISECOND));
		endTime.add(Calendar.SECOND  , - start.get(Calendar.SECOND));
		endTime.add(Calendar.MINUTE  , - start.get(Calendar.MINUTE));
		endTime.add(Calendar.HOUR , - start.get(Calendar.HOUR_OF_DAY));
		return time.format(endTime.getTime());
		}
	/*
	 * ??��?��??:paresDate
	 * ???  ???:�?�?�?串转???�?Date???
	 * ???  ???:d1=�?须为yyyy-mm-dd
	 * �???????:Date????????��??
	 * 
	 */
	public static  Date paresDate(String d1){
		String[] tmp=d1.split("\\-");
		tmp[2]=tmp[2].substring(0,2);
		Calendar c1=new GregorianCalendar();
		c1.set(Integer.parseInt(tmp[0]),
			   Integer.parseInt(tmp[1]),
			   Integer.parseInt(tmp[2]),0,0,0);
		return c1.getTime();
	}
	/*
	 * ??��?��??:paresDateTime
	 * ???  ???:�?�?�?串转???�?DateTime???
	 * ???  ???:d1=�?须为yyyy-mm-dd HH:MM:SS
	 * �???????:Date????????��??
	 * 
	 */
	public static  Date paresDateTime(String d1){
		String[] tmp=d1.split("\\-");
		tmp[2]=tmp[2].substring(0,2);
		String[] time=d1.split("\\:");
		time[0]=time[0].substring(11,13);
		Calendar c1=new GregorianCalendar();
		c1.set(Integer.parseInt(tmp[0]),
			   Integer.parseInt(tmp[1]),
			   Integer.parseInt(tmp[2]),
			   Integer.parseInt(time[0]),
			   Integer.parseInt(time[1]),
			   Integer.parseInt(time[2]));
		return c1.getTime();
	}

	/*
	 * ??��?��??:daysBetween
	 * ???  ???:计�??d1???d2两个??��??�???��??天�??
	 * �???????:天�??
	 * 
	 */
	public static  long daysBetween(Date d1,Date d2){
		return ((d2.getTime()-d1.getTime()+ONE_HOUR)/(ONE_HOUR*24));
	}
	/*
	 * ??��?��??:hoursBetween
	 * ???  ???:计�??d1???d2两个??��??�???��?�差�?�?�?�????
	 * �???????:�???��??
	 * 
	 */
	public static  long hoursBetween(Date d1,Date d2){
		return ((d2.getTime()-d1.getTime())/ONE_HOUR);
	}
	/*
	 * ??��?��??:minutesBetween
	 * ???  ???:计�??d1???d2两个??��??�???��?�差�?�?�???????
	 * �???????:?????????
	 * 
	 */
	public static  long minutesBetween(Date d1,Date d2){
		return ((d2.getTime()-d1.getTime())/ONE_MINUTE);
	}
	/*
	 * ??��?��??:isDate
	 * ???  ???:??��????????为�?��??
	 * ???  ???:d1=�?须为yyyy-mm-dd
	 * �???????:true=???/false=???
	 * 
	 */
	public static  boolean isDate(String d1){
		SimpleDateFormat sfDay=new SimpleDateFormat("yyyy-MM-dd");
		boolean isOK=true;
		sfDay.setLenient(false);
		try {
			Date dt2=sfDay.parse(d1);
			
		} catch (ParseException e) {
			isOK=false;
		}
		return isOK;
	}
	/*
	 * ??��?��??:isDate
	 * ???  ???:??��????????为�?��??
	 * ???  ???:d1=�?须为yyyy-mm-dd
	 * �???????:true=???/false=???
	 * 
	 */
	public static  boolean isDateTime(String d1){
		boolean isOK=true;
		SimpleDateFormat sfDay=new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
		sfDay.setLenient(false);
		try {
			Date dt2=sfDay.parse(d1);
		} catch (ParseException e) {
			isOK=false;
		}
		return isOK;
	}
	/*
	 * ??��?��??:getAddDate
	 * ???  ???:�????date??��?????????????????num天�????��??
	 * ???  ???:String date=�???��????��??,int num=-X:???X�?/X:???X�?
	 * 
	 */
	public static  String getAddDate(String date,int num){
		String eDate=null;
		SimpleDateFormat sfDay=new SimpleDateFormat("yyyy-MM-dd");
		try{
			Calendar calendar=Calendar.getInstance();		
			calendar.setTime(sfDay.parse(date));
			int iMonth=calendar.get(Calendar.MONTH);
			int iDay=calendar.get(Calendar.DATE);
			calendar.set(Calendar.DATE,iDay+num);	
			eDate=sfDay.format(calendar.getTime());		
		}
		catch(ParseException ex){
			ex.getStackTrace();
				
		}
		return eDate;		
	}
	/* ??��?��??�?getCurrentDate
	 * ???  ??��??�????�?????????��??
	 * ???  ??��??
	 * �??????��??yyyy-mm-dd
	 */	
	public static  String getCurrentDate(){
		String eDate=null;
		SimpleDateFormat sfDay=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();		
		calendar.setTime((Date) new Date());
		eDate=sfDay.format(calendar.getTime());
		return eDate;		
	}
	/* ??��?��??�?getcurrentTime
	 * ???  ??��??�???��??????????��??
	 * ???  ??��??
	 * �??????��??HH:MI:SS
	 */
	public static  String getCurrentTime(){
		Date time=new Date();
		Calendar cd= new GregorianCalendar();
		cd.setTime(time);
		int hh=cd.get(Calendar.HOUR_OF_DAY);
		int mi=cd.get(Calendar.MINUTE);
		int ss=cd.get(Calendar.SECOND);
		String sHH="",sMI="",sSS="";
		if(hh<10)
			sHH="0";
		sHH=sHH+hh;
		if(mi<10)
			sMI="0";
		sMI=sMI+mi;
		if(ss<10)
			sSS="0";
		sSS=sSS+ss;
		return sHH+":"+sMI+":"+sSS;
	}
	/* ??��?��??�?getYesterdayDate
	 * ???  ??��??�??????�天?????��??
	 * ???  ??��??
	 * �??????��??yyyy-mm-dd
	 */	
	public static  String getYesterdayDate(){
		String eDate=null;
		SimpleDateFormat sfDay=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();		
		calendar.setTime(new Date());
		int iDay=calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE,iDay-1);	
		eDate=sfDay.format(calendar.getTime());
		return eDate;		
	}
	/* ??��?��??�?getPreMonthLastDate
	 * ???  ??��??�????�?�??????????�?天�????��??
	 * ???  ??��??
	 * �??????��??yyyy-mm-dd
	 */	
	public static  String getPreMonthLastDate(){
		String eDate=null;
		SimpleDateFormat sfDay=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();		
		calendar.setTime(new Date());
		calendar.set(Calendar.DATE,1);	
		int iDay=calendar.get(Calendar.DATE);
		calendar.set(Calendar.DATE,iDay-1);	

		eDate=sfDay.format(calendar.getTime());	
		eDate=sfDay.format(calendar.getTime());
		return eDate;		
	}
	/* ??��?��??�?getMaxDate
	 * ???  ??��??�???????�?年�????????大�?��??
	 * ???  ??��??yyyy-mm
	 * �??????��??yyyy-mm-dd
	 */	
	public static  String getMaxDate(String Year_Month){
		String eDate=null;
		SimpleDateFormat sfDay=new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar=Calendar.getInstance();
		try {
			calendar.setTime(sfDay.parse(Year_Month+"-01"));
			int iMon=calendar.get(Calendar.MONTH);
			calendar.set(Calendar.MONTH,iMon+1);
			calendar.set(Calendar.DATE,1);	
			int iDay=calendar.get(Calendar.DATE);
			calendar.set(Calendar.DATE,iDay-1);	
		} catch (ParseException e) {
			e.printStackTrace();
		}

		eDate=sfDay.format(calendar.getTime());	
		return eDate;		
	}
	/* ??��?��??�?cutSplit
	 * ???  ??��????��??年�????��????��??"-"
	 * ???  ??��??yyyy-mm-dd
	 * �??????��??yyyymmdd
	 */	
	public static String cutSplit(String date){
		String date1=date.substring(0,4)+date.substring(5,7)+date.substring(8,10);
		return date1;
	}
	
	/**
	 * 计�??�?�???��???????��??
	 * 1-6     �?�????
	 * 7-13    �?�????
	 * 14-20   �?�????
	 * 21-???�?  �???????
	 * @param date:2008-12-01
	 * @return
	 */
	public static String getWeeks(String date){
		String d = date.substring(8);
		String result = null;
		if(Integer.parseInt(d) < 7)
			result = 1+"";
		else if(Integer.parseInt(d) >= 7 && Integer.parseInt(d) <14)
			result = 2+"";
		else if(Integer.parseInt(d) >= 14 && Integer.parseInt(d) <21)
			result = 3+"";
		else
			result = 4+"";
		return result;
	}
	
	/**
	 * 计�?????�???��????????�?�?
	 * @param year
	 * @param month
	 * @param week
	 * @return yyyy-mm-dd
	 */
	public static String getLastDayOfWeek(String year,String month,String week){
		String result = null;
		if(Integer.parseInt(week) == 1)
			result = year + "-" + month + "-6";
		else if(Integer.parseInt(week) == 2)
			result = year + "-" + month + "-13";
		else if(Integer.parseInt(week) == 3)
			result = year + "-" + month + "-20";
		else if(Integer.parseInt(week) == 4){
//			Calendar c = Calendar.getInstance();
//			c.set(Integer.parseInt(year), Integer.parseInt(month)-1, 1);
			String d = getMaxDate(year+"-"+month);//getAddDate(year + "-" + month + "-21",11);//�?�???��?��??移�?��??�????�?
			System.out.println(d);
			result = d;
		}
		return result;
	}
	/**
	 * 比较两个字符串类型的时间的大小,如果前者小于等于后者，返回TRUE，否则返回false
	 * @param strDate1
	 * @param strDate2
	 * @return
	 * @throws Exception
	 */
	public static boolean DateCompare(String strDate1,String strDate2) throws Exception{
		SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd");
		if(strDate1.equals(strDate2)){
			return true;
		}
		Date date1 = newDate.parse(strDate1);
		Date date2 = newDate.parse(strDate2);
		if(date1.before(date2)){
			return true;
		}
		return false;
	}
	
	/**
	 * 返回相差时间String,X分钟前
	 */
	public static String getTimeBetween(String dateStr){
		String str = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date date_old = sdf.parse(dateStr);
			Date date_new = new Date();
			Long between = date_new.getTime() - date_old.getTime();
			if((between/1000)<=60){
				str = between/1000 + "秒前";
			}else if(between/1000>60 && between/1000/60<=60){
				str = between/1000/60 + "分钟前";
			}else if(between/1000/60>60 && between/1000/60/60<=24){
				str = between/1000/60/60 + "小时前";
			}else if(between/1000/60/60>24 && between/1000/60/60/24<=30){
				str = between/1000/60/60/24 + "天前";
			}else if(between/1000/60/60/24>30 && between/1000/60/60/24/30<12){
				str = between/1000/60/60/24/30 + "月前";
			}else if(between/1000/60/60/24/30>=12 ){
				str = between/1000/60/60/24/30/12 + "年前";
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return str;
	}
	/**
	 *  如果为标准时间格式，返回格式为 MM-dd 星期  否则原样返回
	 * @param dateStr
	 * @return
	 */
	public static String getDate(String dateStr){
		SimpleDateFormat dateAndTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat new_date = new SimpleDateFormat("MM月dd日");
		Calendar cal = Calendar.getInstance();
		String week[] = {"星期天","星期一","星期二","星期三","星期四","星期五","星期六"};
		Date date1 = null;
		String returnDate = "";
		try{
			date1 = dateAndTime.parse(dateStr);
		}catch(Exception e){
			try {
				date1 = date.parse(dateStr);
			} catch (ParseException e1) {
			}
		}finally{
			if(date1 == null){
				returnDate = dateStr;
			}else{
				cal.setTime(date1);
				returnDate = new_date.format(date1);
				returnDate += " "+week[cal.get(Calendar.DAY_OF_WEEK)-1];
			}
		}
		return returnDate;
	}

	
	/**
	 * 如果时间格式为标准时间，则返回时间格式为 HH:mm  否则返回空
	 * @param dateStr
	 * @return
	 */
	public static String getTime(String dateStr){
		SimpleDateFormat time = new SimpleDateFormat("HH:mm");
		Date date1 = null;
		String returnDate = "";
		try{
			date1 = dateAndTime.parse(dateStr);
		}catch(Exception e){
			
		}finally{
			if(date1 == null){
				returnDate = "";
			}else{
				returnDate = time.format(date1);
			}
		}
		return returnDate;
	}
	/**
	 * 输入时间和当前时间进行比较，
	 * @param dateStr 当前日期
	 * @return 如果输入时间大于当前时间，返回1，如果相等，返回0，否则返回-1
	 */
	public static int compareDateWithNow(String dateStr){
		Date date = null;
		int flag = 0;
		Date now = null ;
		try {
			date = sf.parse(sf.format(sf.parse(dateStr)));
			now = sf.parse(sf.format(new Date()));
		} catch (ParseException e) {
			e.printStackTrace();
		}finally{
			if(now.before(date)){
				flag =  1;
			}else if(now.after(date)){
				flag = -1;
			}else{
				flag = 0;
			}
		}
		return flag;
	}
	/**
	 * 传入时间和当前时间进行比较，如果传入时间大约当前时间，则返回true，否则返回false
	 * @param datestr
	 * @return
	 */
	public static boolean compareWithNow(String datestr){
		Date date = null;
		Date now = new Date();
		boolean flag = false;
		try {
			date = dateAndTime.parse(datestr);
		} catch (ParseException e) {
			e.printStackTrace();
		}finally{
			if(now.before(date)){
				flag = true;
			}
		}
		return flag;
	}
	
//	public static String getOrderId(HttpServletRequest request){
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		String s = PartnerStatic.getPartnerId(request)+"-";
//		s += sdf.format(new Date())+"-";
//		
//		s += String.valueOf(Math.random()*90000+100000).substring(0, String.valueOf(Math.random()*90000+100000).indexOf("."));
//		
//		return s;
//	}
	/**
	 * 基础数据在cache中的时间
	 * @return
	 */
	public static Date getBaseOfvalidityTime(){
		return  new Date(new java.util.Date().getTime()+24*60*60*1000);
	}
	/**
	 * 获取看演出 艺人 场馆的列表页面在cache中的时间
	 * @param datestr
	 * @return
	 */
	
	public static Date getPeriodOfvalidityTime(){
		return  new Date(new java.util.Date().getTime()+30*60*1000);
	}
	
	public static String getRiqi(){
		SimpleDateFormat new_date = new SimpleDateFormat("MM月dd日");
		Date date1 = new Date();
		String returnDate = "";
		returnDate = new_date.format(date1);
		return returnDate;
	}
	
	public static String getXingqi(){
		Calendar cal = Calendar.getInstance();
		String week[] = {"星期天","星期一","星期二","星期三","星期四","星期五","星期六"};
		String returnDate = "";
		returnDate = week[cal.get(Calendar.DAY_OF_WEEK)-1];
		return returnDate;
	}
	

}
