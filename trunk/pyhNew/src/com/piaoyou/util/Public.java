package com.piaoyou.util;

public class Public {
	//应用的地址
	public static final String APP_URL="http://192.168.0.168/app_api";
	
	//public static final String APP_URL="http://app.piaoyouhui.com";
	/**
	 * 图片地址根
	 */
	private static final String IMG_ROOT = "/usr/local/tomcat6.0.33/webapps/piaoyouImg/img/";
	
	/**
	 * 剧照图片地址
	 */
	private static final String STAGE_PATH="/stage";
	
	/**
	 * 剧照大图地址600*600
	 */
	private static final String STAGE_BIG_PATH="/big";
	/**
	 * 剧照大图地址100*100
	 */
	private static final String STAGE_SMALL_PATH="/small";
	
	/**
	 * 剧照原始图片地址
	 */
	private static final String STAGE_DEFAULT_PATH="/default";
	
	//比票价排序算法参数
	public static float COMPARE_PRICE_WEIGHT = 0.7f;
	public static float COMPARE_SCORE_WEITHT = 0.2f;
	public static float COMPARE_CLICK_WEIGHT = 0.1f;
	public static int FIRST_AGENCY_WEIGHT = 1;
	
	
	//演出列表按热门排序算法参数
	public static float SHOW_LIST_SHOW_WEIGHT = 0.3f;
	public static float SHOW_LIST_CLICK_WEIGHT = 0.1f;
	public static float SHOW_LIST_ORDER_WEIGHT = 0.3f;
	public static float SHOW_LIST_GRADE_WEIGHT = 0.15f;
	public static float SHOW_LIST_RATE_WEIGHT = 0.1f;
	public static float SHOW_LIST_COMMENT_WEIGHT = 0.3f;
	
    public static int ACTIVIT_ID=1;
    
    
    //演出主题分值
    public static int SHOW_COMMENT_COUNT_SCORE = 3;
    public static int SHOW_COMMENT_BACK_SCORE = 1;
    public static int SHOW_COMMENT_COUNT_MORE_SCORE = 10;
    public static int SHOW_COMMENT_COUNT_MORE = 5;
}
