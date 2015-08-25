package com.piaoyou.util.date;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {
	
	public static final String now = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	
	public static final Matcher timeMatcher = Pattern.compile("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$").matcher("");
	
	public static boolean isStandard(String timeStr){
		timeMatcher.reset(timeStr);
		return timeMatcher.matches();
	}
	
}
