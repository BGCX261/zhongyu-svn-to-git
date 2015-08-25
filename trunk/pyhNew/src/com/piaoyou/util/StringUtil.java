package com.piaoyou.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.net.URLCodec;


public class StringUtil {
	  public static final String EN = "ISO-8859-1";
	  public static final String CN = "GBK";

	  public static String ISO2GB(String isoStr)
	    throws UnsupportedEncodingException
	  {
	    return new String(isoStr.getBytes("ISO-8859-1"), "GBK");
	  }

	  public static String GB2ISO(String gbStr) throws UnsupportedEncodingException
	  {
	    return new String(gbStr.getBytes("GBK"), "ISO-8859-1");
	  }

	  public static String getIP(HttpServletRequest request) {
	    String ip = request.getRemoteAddr();
	    return ((ip != null) ? ip : ""); }

	  public static String subStr(String source, String from, String to) {
	    if (source == null) {
	      return "";
	    }
	    int start = source.indexOf(from);
	    if (start != -1) {
	      start += from.length();
	    }
	    int end = source.indexOf(to, start);
	    if ((start != -1) && (end != -1)) {
	      return source.substring(start, end);
	    }
	    return "";
	  }

	  public static String replaceChar(String source, char oldChar, char newChar)
	  {
	    if (source == null) {
	      return "";
	    }
	    return source.replace(oldChar, newChar);
	  }

	  public static String replaceString(String source, String oldString, String newString)
	  {
	    if ((source == null) || (oldString == null) || (newString == null)) {
	      return "";
	    }
	    return replace(source, oldString, newString);
	  }

	  public static String[] split(String source, String regex)
	  {
	    if ((source == null) || (source.equals(""))) {
	      return new String[0];
	    }
	    return source.split(regex);
	  }

	  private static String replace(String source, String oldString, String newString)
	  {
	    StringBuffer output = new StringBuffer();
	    int lengthOfSource = source.length();
	    int lengthOfOld = oldString.length();
	    int posStart = 0;
	    int pos;
	    for (posStart = 0; (pos = source.indexOf(oldString, posStart)) >= 0; posStart = pos + lengthOfOld) {
	      output.append(source.substring(posStart, pos));
	      output.append(newString);
	    }

	    if (posStart < lengthOfSource) {
	      output.append(source.substring(posStart));
	    }
	    return output.toString();
	  }

	  public static String toQuoteMark(String str) {
	    str = replaceString(str, "'", "&#39;");
	    str = replaceString(str, "\"", "&#34;");
	    str = replace(str, "\\", "&#92;");
	    str = replaceString(str, "\r\n", "\n");
	    str = replaceString(str, "\\n", "\n");
	    return str;
	  }

	  public static String toHtml(String str) {
	    str = replaceString(str, "<", "&lt;");
	    str = replaceString(str, ">", "&gt;");
	    return str;
	  }

	  public static String toBR(String str) {
	    str = replaceString(str, "\\n", "\n");
	    str = replaceString(str, "\n", "<br>\n");
	    str = replaceString(str, "  ", "&nbsp;&nbsp;");
	    return str;
	  }

	  public static String toSQL(String str) {
	    str = replaceString(str, "\r\n", "\n");
	    return str; 
	  }

	  public static String htmlFilter(String str) {
		  if(str==null){
			  return null;
		  }
	  // StringBuffer stringbuffer = new StringBuffer();
	   /*    for (int i = 0; i < str.length(); ++i) {
	      char c = str.charAt(i);
	      switch (c)
	      {
	      case '\'':
	        stringbuffer.append("&#039;");
	        break;
	      case '"':
	        stringbuffer.append("&quot;");
	        break;
	      case '<':
	        stringbuffer.append("&lt;");
	        break;
	      case '>':
	        stringbuffer.append("&gt;");
	        break;
	      case '&':
	        stringbuffer.append("&amp;");
	        break;
	      case ' ':
	        stringbuffer.append("&nbsp;");
	        break;
	      case '\n':
	        stringbuffer.append("<br>");
	        break;
	      default:
	        stringbuffer.append(c);
	      }

	    }*/
		 String[] array={"html","head","body","font","div","span","a","li","javascript","script","p","br","img","h1","h2","h3","h4","h5","h6","table","tr","td","th","hr","link","label","title","b","big","em","i","small","strong","sub","sup","ins","del","s","strike","ul","caption","thead","tbody","tfoot","col","colgroup","frame","frameset","ol","ul","dl","dt","dd","dir","menu","form","input","textarea","button","fieldset","legend","select","optgroup","option","isinex","img","map","area","meta","code","kbd","samp","tt","var","pre","listing","plaintext","xmp","abbr","acronym","address","bdo","blockquote","q","cite","dfn","basefont","center","base"};
		 for(String temp: array){
			 String regexPre="<[\\s]*"+temp+"[^>]*>";
			 String regexSub="<[\\s]*/[\\s]*"+temp+"[^>]*>";
			 Pattern pre = Pattern.compile(regexPre,Pattern.CASE_INSENSITIVE);
			 Matcher mPre = pre.matcher(str);
			 while (mPre.find()) {
					str = str.replace(mPre.group(), "");
			 }
			 Pattern sub = Pattern.compile(regexSub,Pattern.CASE_INSENSITIVE);
			 Matcher mSub = sub.matcher(str);
			 while (mSub.find()) {
					str = str.replace(mSub.group(), "");
			 }
		 }
		 str=str.replaceAll("[\\s]*var[\\s]*", "");
		 str=str.replaceAll("\"", "");
		 str=str.replace("'", "");
		 str=str.replace("entry.", "");
		 return str;
	  }
	  public static String base64Encode(String str) {
	    if ((str != null) && (str.length() > 0)) {
	      str = new String(new Base64().encode(str.getBytes()));
	    }
	    return str;
	  }

	  public static String base64Decode(String str) {
	    if ((str != null) && (str.length() > 0)) {
	      byte[] buf = new Base64().decode(str.getBytes());
	      str = new String(buf);
	    }
	    return str;
	  }

	  public static String base64Encode(byte[] str) {
	    String encodeStr = "";
	    if ((str != null) && (str.length > 0)) {
	      encodeStr = new String(new Base64().encode(str));
	    }
	    return encodeStr;
	  }

	  public static byte[] base64DecodeForByte(String str) {
	    byte[] buf = (byte[])null;
	    if ((str != null) && (str.length() > 0)) {
	      buf = new Base64().decode(str.getBytes());
	    }
	    return buf;
	  }

	  public static String getRandomNumber(int bits, int to) {
	    StringBuffer randBuffer = new StringBuffer();
	    Random RANDOM = new Random();
	    for (int i = 1; i <= bits; ++i) {
	      randBuffer.append(RANDOM.nextInt(to));
	    }

	    return randBuffer.toString();
	  }

	  public static String urlEncode(String str) throws EncoderException
	  {
	    URLCodec URLCODEC = new URLCodec("UTF-8");
	    return URLCODEC.encode(str);
	  }

	  public static String urlDecode(String str) throws DecoderException
	  {
	    return str;
	  }

	  public static String getSQL(String sql, boolean hasOffset, int from, int to) {
	    return getMysqlSQL(sql, hasOffset);
	  }

	  public static String getMysqlSQL(String sql, boolean hasOffset) {
	    StringBuffer sb = new StringBuffer(sql.length() + 20);
	    sb.append(sql).append((hasOffset) ? " limit ?, ?" : " limit ?");
	    return sb.toString();
	  }

	  public static String getDb2SQL(String sql, boolean hasOffset) {
	    StringBuffer sb = new StringBuffer(sql.length() + 100);
	    return sb.toString();
	  }

	  public static String getOracleSQL(String sql, boolean hasOffset) {
	    StringBuffer sb = new StringBuffer(sql.length() + 100);
	    return sb.toString();
	  }

	  public static String getMssqlSQL(String sql, boolean hasOffset, int from, int to) {
	    StringBuffer sb = new StringBuffer(sql.length() + 100);
	    return sb.toString();
	  }

	  private static String getRowNumber(String sql) {
	    StringBuffer rownumber = new StringBuffer(50);
	    return rownumber.toString();
	  }

	  private static int getAfterSelectInsertPoint(String sql) {
	    int selectIndex = sql.indexOf("select");
	    return (selectIndex + 6); }

	  public static String getTopic(String str, int max) {
	    String tempStr = null;
	    tempStr = str;
	    if ((tempStr != null) && (tempStr.length() > max)) {
	      tempStr = tempStr.substring(0, max) + "..";
	      return tempStr;
	    }
	    return tempStr;
	  }

	  public static String removeHtml(String str) {
	    String tempStr = null;
	    tempStr = str;
	    Pattern pattern = Pattern.compile("<.+?>");
	    Matcher matcher = pattern.matcher(str);
	    if (matcher.find()) {
	      tempStr = matcher.replaceAll("");
	    }
	    return tempStr; }

	  public static String returnToBr(String s) {
	    if ((s == null) || (s.equals(""))) {
	      return s;
	    }
	    StringBuffer stringbuffer = new StringBuffer();
	    for (int i = 0; i <= s.length() - 1; ++i) {
	      if (s.charAt(i) == '\r') {
	        stringbuffer = stringbuffer.append("<br>");
	      }
	      else if (s.charAt(i) == ' ')
	        stringbuffer = stringbuffer.append("&nbsp;");
	      else {
	        stringbuffer = stringbuffer.append(s.substring(i, i + 1));
	      }
	    }

	    String s1 = stringbuffer.toString();
	    return s1;
	  }

	  public static String returnToHTML(String s) {
	    if ((s == null) || (s.equals(""))) {
	      return s;
	    }
	    StringBuffer stringbuffer = new StringBuffer();
	    for (int i = 0; i <= s.length() - 1; ++i) {
	      if (s.charAt(i) == '<') {
	        stringbuffer = stringbuffer.append("&lt;");
	      }
	      else if (s.charAt(i) == '>')
	        stringbuffer = stringbuffer.append("&gt;");
	      else {
	        stringbuffer = stringbuffer.append(s.substring(i, i + 1));
	      }
	    }

	    String s1 = stringbuffer.toString();
	    return s1;
	  }
	public static String escape(String src) {
		int i;
		char j;
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length() * 6);

		for (i = 0; i < src.length(); i++) {

			j = src.charAt(i);

			if (Character.isDigit(j) || Character.isLowerCase(j)
					|| Character.isUpperCase(j))
				tmp.append(j);
			else if (j < 256) {
				tmp.append("%");
				if (j < 16)
					tmp.append("0");
				tmp.append(Integer.toString(j, 16));
			} else {
				tmp.append("%u");
				tmp.append(Integer.toString(j, 16));
			}
		}
		return tmp.toString();
	}

	public static String unescape(String src) {
		StringBuffer tmp = new StringBuffer();
		tmp.ensureCapacity(src.length());
		int lastPos = 0, pos = 0;
		char ch;
		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					ch = (char) Integer.parseInt(src
							.substring(pos + 2, pos + 6), 16);
					tmp.append(ch);
					lastPos = pos + 6;
				} else {
					ch = (char) Integer.parseInt(src
							.substring(pos + 1, pos + 3), 16);
					tmp.append(ch);
					lastPos = pos + 3;
				}
			} else {
				if (pos == -1) {
					tmp.append(src.substring(lastPos));
					lastPos = src.length();
				} else {
					tmp.append(src.substring(lastPos, pos));
					lastPos = pos;
				}
			}
		}
		return tmp.toString();
	}
	/**
	 * 过滤不文明的单词
	 * @param content
	 * @param fileName
	 * @return
	 */
	public static String filterNotCivilizationWord(String content,String fileName) {
		if (content == null || fileName == null) {
			return null;
		}
		CacheUtil util=CacheUtil.getInstance();
		List<String> listStr=(List<String>)util.getCacheObject("NotCivilizationWords");
		if(listStr==null ||listStr.size()==0){
			File file = new File(fileName);
			BufferedReader in = null;
			List<String> list = new ArrayList<String>();
			try {
				in = new BufferedReader(new FileReader(file));
				String line = null;
				while ((line = in.readLine()) != null) {
					list.add(line);
				}
				listStr=list;
				util.putCache("NotCivilizationWords", listStr, DateTime.getBaseOfvalidityTime());
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
		for (int i = 0; i < listStr.size(); i++) {
			String word = listStr.get(i);
			content = content.replace(word, "***");
		}
		return content;
	}
	public static String getRegex(String regex, String input, int i) {
		if (input != null) {
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(input);
			if (m.find()) {
				if (i == 0) {
					return m.group();
				} else {
					return m.group(i);
				}
			}
		}
		return null;
	}
	
	public static String strInArr(String[] arr,String str){
		String result = "";
		for(String s:arr){
			if(str.equals(s)){
				result = " checked=\"checked\" ";
			}
		}
		return result;
	}


}
