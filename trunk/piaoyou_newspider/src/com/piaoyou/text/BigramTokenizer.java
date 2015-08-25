package com.piaoyou.text;

import java.util.ArrayList;
import java.util.List;

/**   
 * @Title: Tokenizer.java
 * @Package com.pm800.text
 *
 * @author jinfeng.zhang@pm800.com
 * @date 2011-6-10 下午01:55:59    
 * @version V1.0   
 */
public class BigramTokenizer {
	public List<String> tokenize(String text){
		List<String> tokens = new ArrayList<String>();
		
//		for(int i = 0; i < text.length()-1; i++){
//			tokens.add(text.substring(i,i+2));
//		}
		for(int i = 0; i < text.length(); i++){
			tokens.add(text.substring(i,i+1));
		}
		return tokens;
	}
}
