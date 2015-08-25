package com.piaoyou.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Test {
	public static void main(String args[]){
		String reg ="(a{3})";
		String str = "aasdfasdfasdfasdfasdfasdfasdfaaasdfasdfasdfasdfaaasdfasdfasdfasdfaa";
		Pattern pat = Pattern.compile(reg);
		Matcher matcher = pat.matcher(str);
		if(matcher.find()){  //find()方法用来搜索与正则表达式匹配的字符串  
			System.out.println(matcher.group(1));  //group()方法用来返回包含了所匹配的字符串
			System.out.println(matcher.start());
			System.out.println(matcher.end());
		}
	}

}
