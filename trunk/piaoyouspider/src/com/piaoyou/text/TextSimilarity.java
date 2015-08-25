package com.piaoyou.text;

import gnu.trove.TObjectIntHashMap;
import gnu.trove.TObjectIntProcedure;

import java.text.DecimalFormat;
import java.util.List;

/**   
 * @Title: TextSimilarity.java
 * @Package com.pm800.text
 *
 * @author jinfeng.zhang@pm800.com
 * @date 2011-6-10 下午02:00:48    
 * @version V1.0   
 */
public class TextSimilarity {
	public double similarity(String text1, String text2){
		BigramTokenizer tokenizer = new BigramTokenizer();
		List<String> tokens1 = tokenizer.tokenize(text1);
		List<String> tokens2 = tokenizer.tokenize(text2);
		TObjectIntHashMap<String> tokenCountMap1 = toTokenCountMap(tokens1);
		final TObjectIntHashMap<String> tokenCountMap2 = toTokenCountMap(tokens2);
		
		DotProductProcedure dotProductProcedure = new DotProductProcedure(tokenCountMap2);
		tokenCountMap1.forEachEntry(dotProductProcedure);
		
		VectorModuleProcedure moduleProcedure1 = new VectorModuleProcedure();
		tokenCountMap1.forEachEntry(moduleProcedure1);
		VectorModuleProcedure moduleProcedure2 = new VectorModuleProcedure();
		tokenCountMap2.forEachEntry(moduleProcedure2);
		
		return dotProductProcedure.getResult() / (Math.sqrt(moduleProcedure1.getResult()) * Math.sqrt(moduleProcedure2.getResult())) ;
	}
	
	public TObjectIntHashMap<String> toTokenCountMap(List<String> tokens){
		TObjectIntHashMap<String> tokenCountMap = new TObjectIntHashMap<String>();
		for(String token : tokens){
			int count = tokenCountMap.get(token);
			tokenCountMap.put(token, count + 1);
		}
		return tokenCountMap;
	}
	
	private static class DotProductProcedure implements TObjectIntProcedure<String>{
		private double result = 0;
		private TObjectIntHashMap<String> tokenCountMap;
		
		public DotProductProcedure(TObjectIntHashMap<String> tokenCountMap){
			this.tokenCountMap = tokenCountMap;
		}
		
		public boolean execute(String token, int count1) {
			int count2 = tokenCountMap.get(token);
			result += count1 * count2;
			return true;
		}
		
		public double getResult(){
			return result;
		}
	}
	
	private static class VectorModuleProcedure implements TObjectIntProcedure<String>{
		private double result = 0;
		
		public boolean execute(String token, int count) {
			result += count * count;
			return true;
		}
		
		public double getResult(){
			return result;
		}
	}
	
	public static void main(String[] args) {
		 //http://www.damai.cn/ticket_17762.html
		 //http://www.t3.com.cn/project/182112.html
//		 double similarity = textSimilarity.similarity("音乐会河酒吧的10周年民谣剧场音乐会", "静水深流——纪念河酒吧10周年民谣剧场音乐会");
		 
//		 String a = "马德里的夏日 西班牙 - 爱德瓦尔多•费尔南德斯钢琴独奏音乐会";
//		 String b = "马德里的夏日—西班牙-爱德瓦尔多·费尔南德斯钢琴独奏音乐会";
//		 String a = "梁静茹 爱的那一页 苏州演唱会";
//		 String b = "梁静茹 爱的那一页 北京演唱会";
//		 String a = "梁祝——黄安源与黄晨达的胡琴世界 ";
//		 String b = "梁祝——黄安源黄晨达胡琴音乐会";
//		 String a = "嘻哈包袱铺广茗阁剧场（鼓楼）专场演出";
//		 String b = "嘻哈包袱铺广茗阁剧场（鼓楼）专场演出（6月份）";
//		 String a = "周华健 2011广州演唱会";
//		 String b = "罗大佑2011广州演唱会";
		 String a = "木偶系列剧《功夫猪2》";
		 String b = "木偶系列剧《功夫猪》";
		 System.out.println(a.equals(b));

		 
		 TextSimilarity textSimilarity = new TextSimilarity();
		 System.out.println(textSimilarity.similarity(a,b));
		 
		 char[] ca = a.toCharArray();
		 char[] cb = b.toCharArray();
		 int count = 0;
		 for(int i=0;i<ca.length;i++){
			 if(b.contains(String.valueOf(ca[i]))){
				 count ++;
			 }
		 }
		 for(int i=0;i<cb.length;i++){
			 if(a.contains(String.valueOf(cb[i]))){
				 count ++;
			 }
		 }
		 DecimalFormat df=new DecimalFormat("0.0000");
		 System.out.println(df.format((double)(count)/(ca.length+cb.length)));
	}
}
