/**    
 * @{#} JsonDateValueProcessor.java Create on Jun 27, 2011 4:35:26 PM    
 *    
 * Copyright (c) 2007 by KongZhong.    
 */  
package com.piaoyou.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

/**
 * @author 作者 <a href="mailto:wwwww@hotmail.com">icedcoco</a>
 * @version 创建时间：Jun 27, 2011 4:35:26 PM
 * 类说明
 */
public class JsonDateValueProcessor implements JsonValueProcessor {

	private String format = "yyyy-MM-dd HH:mm:ss";

	 public JsonDateValueProcessor() {

	 }

	 public JsonDateValueProcessor(String format) {
	  this.format = format;
	 }

	 public Object processArrayValue(Object value, JsonConfig jsonConfig) {
	  return process(value, jsonConfig);
	 }

	 public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
	  return process(value, jsonConfig);
	 }
	 
	 private Object process( Object value, JsonConfig jsonConfig ) {
	  if (value instanceof Date) {
	   String str = new SimpleDateFormat(format).format((Date) value);
	   return str;
	  }
	  return value == null ? null : value.toString();
	 }

	 public String getFormat() {
	  return format;
	 }

	 public void setFormat(String format) {
	  this.format = format;
	 }

}
