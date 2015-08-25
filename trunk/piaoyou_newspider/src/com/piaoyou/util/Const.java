package com.piaoyou.util;

import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * @date   2011-7-28
 */
public class Const {
	
	/**
	 *   基本信息类型
	 */
	
	public static final int COMMON_INFO_TYPE_SHOW = 0;
	public static final int COMMON_INFO_TYPE_MOVIE= 1;
	public static final int COMMON_INFO_TYPE_GROUP= 2;
	
	/**
	 * 状态
	 */
	public static final int COMMON_STATUS_REPORT = 0 ;  //即将上映
	public static final int COMMON_STATUS_HOT = 1 ;  //热映
	public static final int COMMON_STATUS_PAST = 2 ;  //过期
	
	
	/**
	 *  演出类别
	 */
	public static final int CONCERT = 1;    //演唱会
	public static final int SHOW = 2;     //音乐会
	public static final int DRAMA = 3;      //话剧歌剧
	public static final int DANCE = 4;   //舞蹈芭蕾
	public static final int OPERA = 5 ;   //戏曲
	public static final int CHILDREN = 6 ;  //儿童亲子
	public static final int SPORTS = 7;   //体育
	public static final int HOLIDAY = 8 ;//度假休闲
	public static final int OTHER = 9 ;  // 其他
	
	
	//演出图片路径
	private  static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	public static final String IMG_PATH="/usr/local/tomcat6/webapps/pyhNewImg/"+sdf.format(new Date())+"/";
	public static final String RETURN_IMG_PATH="http://img2.piaoyouhui.com/"+sdf.format(new Date())+"/";
	/**
	 * 演出标题图片大图宽度
	 */
	public static final int SHOW_PHOTO_WIDTH=1024;
	/**
	 * 演出标题图片大图高度
	 */
	public static final int SHOW_PHOTO_HEIGHT=768;
	/**
	 * 保存图片统一格式
	 */
	public static final String IMG_TYPE=".jpg";
	
}
