package com.piaoyou.util;

import java.util.ArrayList;
import java.util.List;
@SuppressWarnings("unchecked")
public class GetByCache {
	/**
	 * 
	 * @param cu cache对象
	 * @param page 分页对新
	 * @param objectName 索引值(相当于key-value中的key)
	 * @return 一个存储所需数据对象的List
	 */
	public static List loadByCache(CacheUtil cu, PageBean page,String objectName) {
		List back = null;
		List result = (List) cu.getCacheObject(objectName);
		if (result != null) {
			back = new ArrayList();
			int count = (Integer.parseInt(page.getPageIndex()) - 1) * Integer.parseInt(page.getPageCount());
			int length=result.size();
			for (int i = count; i < ((count+Integer.parseInt(page.getPageCount()))< length ? (count+Integer.parseInt(page.getPageCount())): length); i++) {
				back.add(result.get(i));
			}
		} 
		return back;
	}
	/**
	 * 
	 * @param cu cache对象
	 * @param objectName 索引值(相当于key-value中的key)
	 * @return 仅仅返回所需新闻数据前十条
	 */
	public static List loadByCacheNews(CacheUtil cu, String objectName) {
		List back = null;
		List result = (List) cu.getCacheObject(objectName);
		if (result != null) {
			back = new ArrayList();
			int length=result.size();
			for(int i=0;i<(10>length?length:10);i++){
				back.add(result.get(i));
			}
		} 
		return back;
	}
	/**
	 * 
	 * @param cu cache对象
	 * @param objectName objectName 索引值(相当于key-value中的key)
	 * @return 取得所有当前索引目标的数目
	 */
	public static int getItemsTotalByCache(CacheUtil cu,String objectName){
		int back = 0;
		Long result = (Long) cu.getCacheObject(objectName);
		if (result == null) {
			back = 0;
		}	
		return back;
	}
	public static List loadByCache(CacheUtil cu,String objectName) {
		
		if (CacheKey.IS_START_CACHE==false){
			return null;
		}
		List back = null;
		List result = (List) cu.getCacheObject(objectName);
		if (result != null) {
			back = result;
		}
		return back;
	}
	
	public static Object loadByCacheBasic(CacheUtil cu,String objectName) {
		if (CacheKey.IS_START_CACHE==false){
			return null;
		}
		Object result =cu.getCacheObject(objectName);
		if (result != null) {
			return result;
		}else{
			return null;
		}
		
	}
}
