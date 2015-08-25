package com.piaoyou.util;

public class OrderUtil {

	//订单状态
	public static final String ORDER_WEIZHIFU = "未支付";
	public static final int ORDER_WEIZHIFU_INT = 0;
	public static final String ORDER_YIZHIFU = "已支付";
	public static final int ORDER_YIZHIFU_INT = 1;
	public static final String ORDER_YIQUXIAO = "已取消";
	public static final int ORDER_YIQUXIAO_INT = -1;
	public static final String ORDER_YIWANCHENG = "已完成";
	//订单有效期
	public static final int ORDER_EXPIRY = 60 * 30 * 1000;
	//是否支付
	public static final int ORDER_PAY = 1;
	public static final int ORDER_NOPAY = 0;
}
