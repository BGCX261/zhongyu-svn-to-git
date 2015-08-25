package com.piaoyou.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class PubFun {
	/*
	 *   regex:正则表达式
	 *   input :要匹配的文本
	 *   i:选择要匹配的内容(),()
	 */
	public static String getRegex(String regex, String input, int i) {
		if (input != null) {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(input);
			if (m.find()) {
				return m.group(i);
			}
		}
		return null;
	}
	/*
	 *  regex:正则表达式
	 *   input :要匹配的文本
	 *   i:选择要匹配的内容(),()  
	 */
	public static List<String> getRegexs(String regex, String input, int i) {
		if (regex != null && input != null && !regex.trim().equals("")) {
			List<String> list = new ArrayList<String>();
			Pattern p = null;
			Matcher m = null;
			try {
				p = Pattern.compile(regex);
				m = p.matcher(input);
				while (m.find()) {
					list.add(m.group(i));
				}
				return list;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/*
	 * strDate:需要将字符串转成日期类型
	 * formate:"yyyy-mm-dd"
	 */
	public static Date parseStringToDate(String strDate ,String formatestr ){
		Date date=null;
		try{
			DateFormat formate =new SimpleDateFormat(formatestr);
			date=formate.parse(strDate);
		}
		catch(Exception ee){
			
		}
		return date;
		
	}
	/**
	 *   功能：将日期转成字符串
	 *   date：要转换的日期
	 *   format:转换的格式
	 * 
	 */
	public static String parseDateToStr(Date date,String formatStr){
	       String str=null;
		   try{
		   SimpleDateFormat format = new SimpleDateFormat(formatStr);
		   str = format.format(date);
		   }
		   catch(Exception ee){
			   
		   }
		   return str;

	}
	/**
	 * 
	 * @param i 
	 * @return 
	 */
	public static boolean isOdd(int i){
		return i%2!=0;
	}
	/**
	 * 功能 ：判断给定的字符串是否和正则表达式匹配
	 * @param input 
	 * @param regex
	 * @return
	 */
	public static boolean isMatchRegex(String input,String regex){
         return input.matches(regex);
	}
	/**
	 * 功能：获取当天的年份
	 * @return
	 */
	public static String getCurrentYear(){
	      Calendar cal=Calendar.getInstance();//使用日历类
		  int year=cal.get(Calendar.YEAR);//得到年
		  return String.valueOf(year);

	}
	/**
	 * 获取下一年
	 * @return
	 */
	public static String getNextYear(){
	      Calendar cal=Calendar.getInstance();//使用日历类
		  int year=cal.get(Calendar.YEAR)+1;//得到年
		  return String.valueOf(year);

	}
	/**
	 * 功能：获取当天的月份
	 * @return
	 */
    public static String getCurrentMonth(){
    	  Calendar cal=Calendar.getInstance();//使用日历类
		  int month=cal.get(Calendar.MONTH)+1;//得到月，因为从0开始的，所以要加1
		  return String.valueOf(month);
		
	}
    /**
     * 功能 ：获取当天的日期(具体指天数)
     * @return
     */
    public static String getCurrentDay(){
    	  Calendar cal=Calendar.getInstance();//使用日历类
		  int day=cal.get(Calendar.DAY_OF_MONTH);//得到日
		  return String.valueOf(day);
	}
    
    /**
     * 得到两个日期内的所有日期
     * @param startDate 开始日期
     * @param endDate   结束日期
     * @param dateFormat 日期格式
     * @return List<String'yyyy-mm-dd'>
     */
    public static List<String> getTwoDateAllDate(String startDate,String endDate,String time,String dateFormat){
    	List <String>dateList=new ArrayList<String>();
		try {
			Date start = new SimpleDateFormat(dateFormat).parse(startDate);
			Date end = new SimpleDateFormat(dateFormat).parse(endDate);
			while(start.getTime() <= end.getTime()){

		         dateList.add(new SimpleDateFormat("yyyy-MM-dd").format(start));

		         dateList.add((new SimpleDateFormat("yyyy-MM-dd").format(start)+" "+time).trim());


		         start = new Date(start.getTime() + 86400000);
		        }
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return dateList;
    }
    
    /***
     * 
     * @param stratDate
     * @param endDate
     * @return
     */
    public static long getDaysBetweenTwoDate(String stratDate,String endDate,String dateFormat){
    	try {
			Date start = new SimpleDateFormat(dateFormat).parse(stratDate);
			Date end = new SimpleDateFormat(dateFormat).parse(endDate);
			return ((end.getTime()-start.getTime())/86400000+1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return 0;
    }
    /**
     * 将字符串转换为半角
     * @param input
     * @return
     */
    public static String ToDBC(String input) {    
  	  char[] c = input.toCharArray();    
  	  for (int i = 0; i < c.length; i++) {    
  	   if (c[i] == 12288) {    
  	    c[i] = (char) 32;    
  	    continue;    
  	   }    
  	   if (c[i] > 65280 && c[i] < 65375)    
  	    c[i] = (char) (c[i] - 65248);    
  	  }    
  	  return new String(c);    
  	 }
    
    /**
     * @param date 日期
     * @param isTime 是否有时间
     * @return
     */
    public static String dateFormat(String date,boolean isTime){
    	String rDate = null;
    	Date start=null;
		if(isTime){
			try {
				start = new SimpleDateFormat("yyyy-MM-dd HH:SS").parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			rDate = new SimpleDateFormat("yyyy-MM-dd HH:SS").format(start);
		}else{
			try {
				start = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			rDate = new SimpleDateFormat("yyyy-MM-dd").format(start);
		}
    	return rDate;
    }
  
	final static Matcher keyMatcher = Pattern.compile("《(.*?)》").matcher("");
	
    /**
     * 去除字符串中特殊字符(非中文,数字和字母的)
     * @param input
     * @return
     */
	public static String cleanString(String input){
		StringBuilder sb = new StringBuilder("");
		keyMatcher.reset(input);
		while(keyMatcher.find()){
			sb.append(keyMatcher.group(1));
//			input = keyMatcher.group(1);
		}
		if(sb.toString().equals("")){
			sb.append(input);
		}
		return sb.toString().replaceAll("[^\u4e00-\u9fa5\\d\\w]", "");
	}
	
    /**
     * 去除字符串中特殊字符(非中文,数字和字母的)
     * @param input
     * @return
     */
	public static String cleanString2(String input){
		keyMatcher.reset(input);
		if(keyMatcher.find()){
			input = keyMatcher.group(1);
		}
		return input.replaceAll("[^\u4e00-\u9fa5\\d\\w]", "");
	}
	/*
	 * java比较两个日期时间
	 */
	public static int compareDate(String DATE1, String DATE2) {
         DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
         try {
                 Date dt1 = df.parse(DATE1);
                 Date dt2 = df.parse(DATE2);
                 if (dt1.getTime() >= dt2.getTime()) {
                         return 1;
                 } else {
                         return -1;
                 }
         } catch (Exception exception) {
                 exception.printStackTrace();
         }
         return 0;
    }
	
	 /**  
     * 返回图片名称
     * @param zhongwen 中文  
     * @return  
     * @throws BadHanyuPinyinOutputFormatCombination  
     */  
    public static String getPinYin(String zhongwen){   
        String zhongWenPinYin = "";
        char[] chars = PubFun.cleanString(zhongwen).toCharArray();
        for (int i = 0; i < chars.length; i++){
        	try{
	            String[] pinYin = PinyinHelper.toHanyuPinyinStringArray(chars[i],getDefaultOutputFormat());
	            // 当转换不是中文字符时,返回null   
	            if(pinYin != null){
	                zhongWenPinYin += capitalize(pinYin[0]).charAt(0);
	            }else{
	                zhongWenPinYin += chars[i];
	            }  
        	}catch(BadHanyuPinyinOutputFormatCombination e){
        		e.printStackTrace();
        	}
        }
        String date=new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
        return zhongWenPinYin+"_"+date;
    }
  
    /**  
     * Default Format 默认输出格式  
     *   
     * @return  
     */  
    public static HanyuPinyinOutputFormat getDefaultOutputFormat() {   
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();   
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 小写   
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 没有音调数字   
        format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON);// u显示  
  
        return format;   
    }   
  
    /**  
     * Capitalize 首字母大写  
     *   
     * @param s  
     * @return  
     */  
    public static String capitalize(String s) {   
        char ch[];   
        ch = s.toCharArray();   
//        if (ch[0] >= 'a' && ch[0] <= 'z') {   
//            ch[0] = (char) (ch[0] - 32);   
//        }   
        String newString = new String(ch);   
        return newString;   
    }
    
    /**
     * 得到任何字符的首字符，返回大写
     * @param zhongwen
     * @return
     */
    public static String getHeadChar(String zhongwen){   
        String zhongWenPinYin = "";
        char[] chars = PubFun.cleanString(zhongwen).toCharArray();
        for (int i = 0; i < chars.length;){
        	try{
	            String[] pinYin = PinyinHelper.toHanyuPinyinStringArray(chars[i],getDefaultOutputFormat());
	            // 当转换不是中文字符时,返回null   
	            if(pinYin != null){
	                zhongWenPinYin += capitalize(pinYin[0]).charAt(0);
	            }else{
	                zhongWenPinYin += chars[i];
	            }  
        	}catch(BadHanyuPinyinOutputFormatCombination e){
        		e.printStackTrace();
        	}
        	break;
        }
//        String date=new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date());
        return zhongWenPinYin;
    }
	
    /**
     * 过滤jsoup对象中script标签等,并将img元素转换为绝对路径
     * @param target	目标元素
     * @return
     */
    public static Element cleanElement(Element target){
    	for(Element clear:target.select("script,noscript,embed,object")){
			clear.remove();
		}
    	for(Element tmp:target.select("img")){
			tmp.attr("src",tmp.attr("abs:src"));
		}
    	return target;
    }
    
    /**
     * 过滤jsoup对象中script标签等,并将img元素转换为绝对路径
     * @param target	目标元素
     * @return
     */
    public static Elements cleanElement(Elements target){
    	for(Element clear:target.select("script,noscript,embed,object")){
    		clear.remove();
    	}
    	for(Element tmp:target.select("img")){
    		tmp.attr("src",tmp.attr("abs:src"));
    	}
    	return target;
    }
	
    
    public static int getWeek(String str){
    	SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    	int week  = 10;
        Calendar c = Calendar.getInstance();
        try{
        	c.setTime( sf.parse(str));
        	week= c.get(Calendar.DAY_OF_WEEK);
        }catch(Exception e){
        	week = 10;
        }
        return week;
    }
}
